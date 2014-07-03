package be.jforce.nosql.dao.cassandra;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.melexis.ape.hector.wrapper.annotation.*;
import com.melexis.ape.hector.wrapper.column.family.ColumnFamily;
import com.melexis.ape.hector.wrapper.dao.GenericCassandraDao;
import com.melexis.ape.hector.wrapper.dao.GenericDao;
import com.melexis.ape.hector.wrapper.util.GenericType;
import com.melexis.ape.hector.wrapper.util.Types;
import me.prettyprint.cassandra.model.BasicColumnFamilyDefinition;
import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.service.ThriftCfDef;
import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ColumnType;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

import static me.prettyprint.hector.api.ddl.ComparatorType.*;

public class GenericCassandraDaoFactory {
    private String clusterName = "DefaultCluster";
    private String host;
    private String keyspaceName = "DefaultKeyspace";
    private int replicationFactor;

    public GenericCassandraDaoFactory() {
        this("localhost", 1);
    }

    public GenericCassandraDaoFactory(String host, int replicationFactor) {
        this.host = host;
        this.replicationFactor = replicationFactor;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public void setKeyspaceName(String keyspaceName) {
        this.keyspaceName = keyspaceName;
    }

    public GenericDao createDaoFor(Class<?>... entities) {
        Cluster cluster = HFactory.getOrCreateCluster(clusterName, host);

        dropKeyspace(cluster);
        createSchema(cluster, entities);
        Keyspace keyspace = createKeyspace(cluster);
        return new GenericCassandraDao(keyspace);
    }

    public void dropKeyspace(Cluster cluster) {
        if (keySpaceAlreadyExists(cluster)) {
            cluster.dropKeyspace(keyspaceName);
        }
    }

    private boolean keySpaceAlreadyExists(Cluster cluster) {
        return cluster.describeKeyspace(keyspaceName) != null;
    }

    private Keyspace createKeyspace(Cluster cluster) {
        ConfigurableConsistencyLevel consistencyLevelPolicy = new ConfigurableConsistencyLevel();
        consistencyLevelPolicy
                .setDefaultReadConsistencyLevel(HConsistencyLevel.ONE);
        consistencyLevelPolicy
                .setDefaultWriteConsistencyLevel(HConsistencyLevel.ONE);
        return HFactory.createKeyspace(keyspaceName, cluster,
                consistencyLevelPolicy);
    }

    private void createSchema(Cluster cluster, Class<?>... entities) {
        List<ColumnFamilyDefinition> columnFamilyDefinitions = createColumnFamilies(entities);

        KeyspaceDefinition keyspace = HFactory.createKeyspaceDefinition(
                keyspaceName, ThriftKsDef.DEF_STRATEGY_CLASS, replicationFactor,
                columnFamilyDefinitions);

        cluster.addKeyspace(keyspace, true);
    }

    private List<ColumnFamilyDefinition> createColumnFamilies(Class<?>... entities) {
        List<ColumnFamilyDefinition> columnFamilyDefinitions = new ArrayList<>();

        for (Class<?> entity : entities) {
            Map<List<Class<?>>, String> columnFamilies = columnFamiliesFor(GenericType.of(entity));
            for (Map.Entry<List<Class<?>>, String> columnFamily : columnFamilies.entrySet()) {
                ThriftCfDef columnFamilyDefinition = createColumnFamily(columnFamily.getKey(), columnFamily.getValue(), entity);
                columnFamilyDefinitions.add(columnFamilyDefinition);
            }
        }
        return columnFamilyDefinitions;
    }

    private ThriftCfDef createColumnFamily(List<Class<?>> key, String columnFamilyName, Class<?> entity) {
        BasicColumnFamilyDefinition basicCFDefinition = createBasicColumnFamilyDefinition(columnFamilyName);
        addAutoColumnFamily(entity, key, basicCFDefinition);
        return new ThriftCfDef(basicCFDefinition);
    }

    private BasicColumnFamilyDefinition createBasicColumnFamilyDefinition(String columnFamilyName) {
        BasicColumnFamilyDefinition columnFamilyDefinition = new BasicColumnFamilyDefinition();
        columnFamilyDefinition.setKeyspaceName(keyspaceName);
        columnFamilyDefinition.setComparatorType(ComparatorType.UTF8TYPE);
        columnFamilyDefinition.setName(columnFamilyName);
        columnFamilyDefinition.setColumnType(ColumnType.STANDARD);
        columnFamilyDefinition.setDefaultValidationClass(ComparatorType.UTF8TYPE.getClassName());
        return columnFamilyDefinition;
    }

    private void addAutoColumnFamily(Class<?> entity, List<Class<?>> key, BasicColumnFamilyDefinition columnFamilyDefinition) {
        ComparatorType columnNameType = columnNameTypeOf(entity);
        ComparatorType columnValueType = columnValueTypeOf(entity);
        String keyValidationClass = validationClassOf(composite(key));
        String valueValidationClass = validationClassOf(columnValueType);
        columnFamilyDefinition.setKeyValidationClass(keyValidationClass);
        columnFamilyDefinition.setDefaultValidationClass(valueValidationClass);
        columnFamilyDefinition.setComparatorType(columnNameType);
        columnFamilyDefinition.setComparatorTypeAlias("");
    }

    private ComparatorType columnNameTypeOf(Class<?> entity) {
        Field columnName = Types.optionalFieldWithAnnotation(entity, ColumnName.class);
        if (columnName != null) {
            ColumnName annotation = columnName.getAnnotation(ColumnName.class);
            Class<?> type = annotation.storeAs() == Identical.class ? columnName.getType() : annotation.storeAs();
            return comparatorTypeFor(type);

        }
        Field columnsField = Types.optionalFieldWithAnnotation(entity, Columns.class);
        if(columnsField != null) {
            return columnNameTypeOf(GenericType.of(columnsField.getGenericType()).getArgumentAsRawType(0));
        }
        Field rowsField = Types.optionalFieldWithAnnotation(entity, Rows.class);
        if(rowsField != null) {
            return columnNameTypeOf(GenericType.of(rowsField.getGenericType()).getArgumentAsRawType(0));
        }
        return UTF8TYPE;
    }
    private ComparatorType columnValueTypeOf(Class<?> entity) {
        Field columnName = Types.optionalFieldWithAnnotation(entity, ColumnValue.class);
        if (columnName != null) {
            ColumnValue annotation = columnName.getAnnotation(ColumnValue.class);
            Class<?> type = annotation.storeAs() == Identical.class ? columnName.getType() : annotation.storeAs();
            return comparatorTypeFor(type);

        }
        Field columnsField = Types.optionalFieldWithAnnotation(entity, Columns.class);
        if(columnsField != null) {
            return columnValueTypeOf(GenericType.of(columnsField.getGenericType()).getArgumentAsRawType(0));
        }
        Field rowsField = Types.optionalFieldWithAnnotation(entity, Rows.class);
        if(rowsField != null) {
            return columnValueTypeOf(GenericType.of(rowsField.getGenericType()).getArgumentAsRawType(0));
        }
        return UTF8TYPE;
    }


    private ComparatorType comparatorTypeFor(Class<?> type) {
        if(type == String.class) {
            return UTF8TYPE;
        } else if(type == Long.class) {
            return LONGTYPE;
        } else if(type == UUID.class) {
            return UUIDTYPE;
        } else if(type == BigDecimal.class) {
            return DECIMALTYPE;
        } else {
            throw new RuntimeException("No appropriate type found for " + type);
        }
    }

    private String validationClassOf(ComparatorType... rowKeyTypes) {
        if (rowKeyTypes.length == 1) {
            return rowKeyTypes[0].getTypeName();
        }
        return COMPOSITETYPE.getTypeName() + "(" + StringUtils.join(typeNamesOf(rowKeyTypes), ",") + ")";
    }

    private String[] typeNamesOf(ComparatorType[] rowKeyTypes) {
        return Lists.transform(Lists.newArrayList(rowKeyTypes), new Function<ComparatorType, String>() {
            @Override
            public String apply(ComparatorType input) {
                return input.getTypeName();
            }
        }).toArray(new String[rowKeyTypes.length]);
    }

    private ComparatorType[] composite(List<Class<?>> components) {
        return Lists.transform(components, new Function<Class<?>, ComparatorType>() {
            @Override
            public ComparatorType apply(Class<?> input) {
                return comparatorTypeFor(input);
            }
        }).toArray(new ComparatorType[components.size()]);
    }

    private static Map<List<Class<?>>, String> columnFamiliesFor(GenericType<?> entityType) {
        Map<List<Class<?>>, String> columnFamilies = Maps.newHashMap();
        NoSQLEntity annotation = entityAnnotation(entityType);
        Class<? extends ColumnFamily> columnFamily = annotation.columnFamily();
        List<Field> rowKeyFields = Types.fieldsWithAnnotation(entityType.getRawType(), RowKey.class);
        if(rowKeyFields.isEmpty()) {
            Field rowsField = Types.fieldWithAnnotation(entityType.getRawType(), Rows.class);
            if(rowsField != null) {
                rowKeyFields = Types.fieldsWithAnnotation(GenericType.of(rowsField.getGenericType()).getArgumentAsRawType(0), RowKey.class);
            }
        }
        List<Class<?>> types = Lists.transform(rowKeyFields, new Function<Field, Class<?>>() {
            @Override
            public Class<?> apply(Field input) {
                return input.getType();
            }
        });
        if (ColumnFamily.None.class == columnFamily) {
            if (annotation.columnFamilies().length > 0) {
                for (ForCriteria forCriteria : annotation.columnFamilies()) {
                    columnFamilies.put(Lists.newArrayList(forCriteria.criteria()), forCriteria.columnFamily().getSimpleName());
                }
            }
            if (columnFamilies.isEmpty()) {
                columnFamilies.put(types, entityType.getRawType().getSimpleName());
            }
        } else {
            columnFamilies.put(types, columnFamily.getSimpleName());
        }
        return columnFamilies;
    }

    private static NoSQLEntity entityAnnotation(GenericType<?> entityType) {
        if (Collection.class.isAssignableFrom(entityType.getRawType())) {
            entityType = entityType.getArgument(0);
        }
        NoSQLEntity annotation = Types.getAnnotation(entityType.getRawType(), NoSQLEntity.class);
        if (annotation == null) {
            throw new IllegalArgumentException("Missing " + NoSQLEntity.class + " on " + entityType + ".");
        }
        return annotation;
    }
}

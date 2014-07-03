package be.jforce.nosql.dao.cassandra.example;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.melexis.ape.hector.wrapper.annotation.NoSQLEntity;
import com.melexis.ape.hector.wrapper.annotation.Rows;

import java.util.List;
import java.util.Map;

@NoSQLEntity(columnFamily = ColumnFamilies.PerformanceByDepartmentAndId.class)
public class DepartmentPerformance {

    @Rows
    private List<Performance> individualPerformances;

    private transient Map<String, Performance> performanceById;

    public DepartmentPerformance(List<Performance> individualPerformances) {
        this.individualPerformances = individualPerformances;
        this.performanceById = Maps.uniqueIndex(individualPerformances, new Function<Performance, String>() {
            @Override
            public String apply(Performance input) {
                return input.getTrigram();
            }
        });
    }

    public List<Performance> getIndividualPerformances() {
        return individualPerformances;
    }

    public Performance get(String id) {
        return performanceById.get(id);
    }
}

package be.jforce.nosql.dao.cassandra.example;

import com.melexis.ape.hector.wrapper.column.family.ColumnFamilyBase;
import me.prettyprint.hector.api.ddl.ComparatorType;

public class ColumnFamilies {
    public static class PerformanceByDepartmentAndId extends ColumnFamilyBase {

        public PerformanceByDepartmentAndId() {
            super(composite(2), ComparatorType.UTF8TYPE);
        }
    }
    public static class PerformanceByDepartment extends ColumnFamilyBase {

        public PerformanceByDepartment() {
            super(composite(1), ComparatorType.UTF8TYPE);
        }
    }
}

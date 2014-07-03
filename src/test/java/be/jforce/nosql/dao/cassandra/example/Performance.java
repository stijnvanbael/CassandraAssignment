package be.jforce.nosql.dao.cassandra.example;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.melexis.ape.hector.wrapper.annotation.Columns;
import com.melexis.ape.hector.wrapper.annotation.ForCriteria;
import com.melexis.ape.hector.wrapper.annotation.NoSQLEntity;
import com.melexis.ape.hector.wrapper.annotation.RowKey;
import com.melexis.ape.hector.wrapper.criteria.QueryCriteria;
import org.joda.time.Duration;
import org.joda.time.LocalDate;

import java.util.List;

@NoSQLEntity(
        columnFamilies = {
                @ForCriteria(criteria = ByDepartmentAndTrigram.class, columnFamily = ColumnFamilies.PerformanceByDepartmentAndId.class),
                @ForCriteria(criteria = ByDepartment.class, columnFamily = ColumnFamilies.PerformanceByDepartment.class)
        }
)
public class Performance {
    @RowKey
    private QueryCriteria<?, ?> key;

    @Columns
    private List<Registration> registrations;

    public Performance(QueryCriteria<?, ?> key, List<Registration> registrations) {
        this.key = key;
        this.registrations = registrations;
    }

    public String getDepartment() {
        return key.get(Department.class).getValue();
    }

    public String getTrigram() {
        return key.get(Trigram.class).getValue();
    }

    public QueryCriteria<?, ?> getKey() {
        return key;
    }

    public List<Registration> getRegistrations() {
        return registrations;
    }

    public Duration valueAt(final LocalDate date) {
        return Iterables.find(registrations, new Predicate<Registration>() {
            @Override
            public boolean apply(Registration input) {
                return input.getDate().equals(date);
            }
        }).getDuration();
    }
}

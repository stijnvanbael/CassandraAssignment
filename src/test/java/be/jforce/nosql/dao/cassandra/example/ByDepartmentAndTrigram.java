package be.jforce.nosql.dao.cassandra.example;

import com.melexis.ape.hector.wrapper.criteria.Criterion;
import com.melexis.ape.hector.wrapper.criteria.QueryCriteria;
import com.melexis.ape.hector.wrapper.criteria.UnionCriterion;
import com.melexis.ape.hector.wrapper.dao.GenericDao;
import org.joda.time.LocalDate;

import java.util.Arrays;
import java.util.List;

public class ByDepartmentAndTrigram<N, T extends ByDepartmentAndTrigram<N, T>> extends QueryCriteria<N, T> {
    public ByDepartmentAndTrigram(GenericDao dao) {
        super(dao);
    }

    @Override
    public List<Class<? extends Criterion>> typeOrder() {
        return Arrays.<Class<? extends Criterion>>asList(Department.class, Trigram.class);
    }

    public T withDepartment(String department) {
        return copyWith(new Department(department));
    }

    public T withTrigram(String trigram) {
        return copyWith(new Trigram(trigram));
    }

    public T withTrigrams(String... trigrams) {
        return copyWith(UnionCriterion.of(trigrams).as(Trigram.class));
    }

    public static class Text extends ByDepartmentAndTrigram<String, Text> {
        public Text(GenericDao dao) {
            super(dao);
        }
    }

    public static class Date extends ByDepartmentAndTrigram<LocalDate, Date> {
        public Date(GenericDao dao) {
            super(dao);
        }

        public Date withDate(LocalDate date) {
            return copyWithColumnName(date);
        }

    }
}

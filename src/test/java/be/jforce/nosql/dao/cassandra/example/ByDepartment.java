package be.jforce.nosql.dao.cassandra.example;

import com.google.common.collect.Lists;
import com.melexis.ape.hector.wrapper.criteria.Criterion;
import com.melexis.ape.hector.wrapper.criteria.QueryCriteria;
import com.melexis.ape.hector.wrapper.dao.GenericDao;
import org.joda.time.LocalDate;

import java.util.List;

public class ByDepartment<N, T extends ByDepartment<N, T>> extends QueryCriteria<N, T> {
    public ByDepartment(GenericDao dao) {
        super(dao);
    }

    @Override
    public List<Class<? extends Criterion>> typeOrder() {
        return Lists.<Class<? extends Criterion>>newArrayList(Department.class);
    }

    public T withDepartment(String department) {
        return copyWith(new Department(department));
    }

    public static class Date extends ByDepartment<LocalDate, Date> {
        public Date(GenericDao dao) {
            super(dao);
        }
    }

    public static class Text extends ByDepartment<String, Text> {
        public Text(GenericDao dao) {
            super(dao);
        }

        public Text withTrigram(String trigram) {
            return copyWithColumnName(trigram);
        }
    }
}

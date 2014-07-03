package be.jforce.nosql.dao.cassandra.solution;

import com.melexis.ape.hector.wrapper.criteria.QueryCriteria;
import com.melexis.ape.hector.wrapper.dao.GenericDao;
import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.List;

public class ByEmailAndRoute extends QueryCriteria<DateTime, ByEmailAndRoute> {
    public ByEmailAndRoute(GenericDao dao) {
        super(dao);
    }

    @Override
    public List<String> nameOrder() {
        return Arrays.asList("email", "route");
    }

    public ByEmailAndRoute withEmail(String email) {
        return copyWith("email", email);
    }

    public ByEmailAndRoute withRoutes(String... routes) {
        return copyWith("route", routes);
    }
}

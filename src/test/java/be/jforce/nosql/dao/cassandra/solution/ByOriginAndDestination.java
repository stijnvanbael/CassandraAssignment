package be.jforce.nosql.dao.cassandra.solution;


import com.melexis.ape.hector.wrapper.criteria.QueryCriteria;
import com.melexis.ape.hector.wrapper.dao.GenericDao;

import java.util.Arrays;
import java.util.List;

public class ByOriginAndDestination extends QueryCriteria<String, ByOriginAndDestination> {
    public ByOriginAndDestination(GenericDao dao) {
        super(dao);
    }

    @Override
    public List<String> nameOrder() {
        return Arrays.asList("origin", "destination");
    }


    public ByOriginAndDestination withOrigin(String origin) {
        return copyWith("origin", origin);
    }

    public ByOriginAndDestination withDestination(String destination) {
        return copyWith("destination", destination);
    }
}

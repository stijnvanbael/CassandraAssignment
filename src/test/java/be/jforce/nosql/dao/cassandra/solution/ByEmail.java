package be.jforce.nosql.dao.cassandra.solution;


import com.melexis.ape.hector.wrapper.criteria.QueryCriteria;
import com.melexis.ape.hector.wrapper.dao.GenericDao;

import java.util.Arrays;
import java.util.List;

public class ByEmail extends QueryCriteria<String, ByEmail> {
    public ByEmail(GenericDao dao) {
        super(dao);
    }

    @Override
    public List<String> nameOrder() {
        return Arrays.asList("email");
    }

    public ByEmail withEmail(String email) {
        return copyWith("email", email);
    }
}

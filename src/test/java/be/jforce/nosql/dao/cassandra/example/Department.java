package be.jforce.nosql.dao.cassandra.example;

import com.melexis.ape.hector.wrapper.criteria.AbstractStringValueCriterion;

public class Department extends AbstractStringValueCriterion<Department> {
    public Department(String value) {
        super(value);
    }
}

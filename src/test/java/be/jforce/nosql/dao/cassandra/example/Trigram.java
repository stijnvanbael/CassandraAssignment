package be.jforce.nosql.dao.cassandra.example;


import com.melexis.ape.hector.wrapper.criteria.AbstractStringValueCriterion;

public class Trigram extends AbstractStringValueCriterion<Trigram> {
    public Trigram(String value) {
        super(value);
    }
}

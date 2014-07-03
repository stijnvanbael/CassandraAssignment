package be.jforce.nosql.dao.cassandra.assignment;

import java.util.Objects;

public class Route {
    private String origin;

    private String destination;

    private final String name;

    private String legs;

    public Route(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getLegs() {
        return legs;
    }

    public void setLegs(String legs) {
        this.legs = legs;
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, destination, name);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Route)) {
            return false;
        }
        Route that = this.getClass().cast(obj);
        return Objects.equals(this.origin, that.origin) &&
                Objects.equals(this.destination, that.destination) &&
                Objects.equals(this.name, that.name);
    }
}

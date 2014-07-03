package be.jforce.nosql.dao.cassandra.assignment;


import com.google.common.base.Function;
import com.google.common.collect.Multimaps;

import java.util.Collection;
import java.util.Map;

public class TripsByRoute {
    private Collection<Trip> trips;

    private transient Map<String, Collection<Trip>> tripsByRoute;

    public TripsByRoute(Collection<Trip> trips) {
        this.trips = trips;
        this.tripsByRoute = Multimaps.index(trips, new Function<Trip, String>() {
            @Override
            public String apply(Trip input) {
                return input.getRoute();
            }
        }).asMap();
    }

    public Collection<Trip> getTrips() {
        return trips;
    }

    public Collection<Trip> getTripsByRoute(String route) {
        return tripsByRoute.get(route);
    }
}

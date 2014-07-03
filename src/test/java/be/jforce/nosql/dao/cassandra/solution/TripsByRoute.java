package be.jforce.nosql.dao.cassandra.solution;


import com.google.common.base.Function;
import com.google.common.collect.Multimaps;
import com.melexis.ape.hector.wrapper.annotation.NoSQLEntity;
import com.melexis.ape.hector.wrapper.annotation.Rows;

import java.util.Collection;
import java.util.Map;

@NoSQLEntity
public class TripsByRoute {
    @Rows
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

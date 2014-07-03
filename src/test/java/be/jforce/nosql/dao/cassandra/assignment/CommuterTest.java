package be.jforce.nosql.dao.cassandra.assignment;

import be.jforce.nosql.dao.cassandra.GenericCassandraDaoFactory;
import com.google.common.collect.Lists;
import com.lordofthejars.nosqlunit.cassandra.CassandraRule;
import com.lordofthejars.nosqlunit.cassandra.EmbeddedCassandra;
import com.melexis.ape.hector.wrapper.dao.GenericDao;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.lordofthejars.nosqlunit.cassandra.EmbeddedCassandraConfigurationBuilder.newEmbeddedCassandraConfiguration;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class CommuterTest {

    private GenericCassandraDaoFactory factory;

    @Test
    public void findTheCommuter() {
        dao = factory.createDaoFor(Commuter.class);
        Commuter commuter = new Commuter("john.doe@acme.inc");
        commuter.setName("John Doe");
        commuter.setHomeAddress("1, St. John Street, Doeville");
        commuter.setWorkAddress("999, Acme Avenue, Metropolis");

        dao.store(commuter);

        Commuter storedCommuter = null; // TODO: find the commuter with email address "john.doe@acme.inc"

        assertThat(storedCommuter, is(not(nullValue())));
        assertThat(storedCommuter.getName(), is("John Doe"));
        assertThat(storedCommuter.getHomeAddress(), is("1, St. John Street, Doeville"));
        assertThat(storedCommuter.getWorkAddress(), is("999, Acme Avenue, Metropolis"));
    }

    @Test
    public void findAllRoutesBetweenTwoAddresses() {
        dao = factory.createDaoFor(Route.class);
        Route route1 = new Route("car");
        route1.setOrigin("1, St. John Street, Doeville");
        route1.setDestination("999, Acme Avenue, Metropolis");
        route1.setLegs(LEGS_1);

        Route route2 = new Route("bike/train");
        route2.setOrigin("1, St. John Street, Doeville");
        route2.setDestination("999, Acme Avenue, Metropolis");
        route2.setLegs(LEGS_2);

        dao.store(route1);
        dao.store(route2);

        List<Route> routes = Lists.newArrayList(); // TODO: find the routes between "1, St. John Street, Doeville" and "999, Acme Avenue, Metropolis"

        assertThat(routes.size(), is(2));
        assertTrue(routes.contains(route1));
        assertTrue(routes.contains(route2));
    }

    @Test
    public void findTripsByRoute() {
        dao = factory.createDaoFor(TripsByRoute.class);
        Trip trip1 = new Trip("john.doe@acme.inc", "car");
        trip1.setStart(DateTime.parse("2014-01-06T07:45:00.000Z"));
        trip1.setDuration(Duration.standardMinutes(17));
        Trip trip2 = new Trip("john.doe@acme.inc", "bike/train");
        trip2.setStart(DateTime.parse("2014-01-07T07:03:00.000Z"));
        trip2.setDuration(Duration.standardMinutes(45));
        Trip trip3 = new Trip("john.doe@acme.inc", "car");
        trip3.setStart(DateTime.parse("2014-01-08T07:48:00.000Z"));
        trip3.setDuration(Duration.standardMinutes(16));
        TripsByRoute tripsByRoute = new TripsByRoute(Arrays.asList(trip1, trip2, trip3));
        dao.store(tripsByRoute);

        TripsByRoute trips = null; // TODO: find the trips for user with email "john.doe@acme.inc" with routes "car" or "bike/train" between 2014-01-06 and 2014-01-13

        assertThat(trips, is(not(nullValue())));
        Collection<Trip> tripsByCar = trips.getTripsByRoute("car");
        assertThat(tripsByCar.size(), is(2));
        assertTrue(tripsByCar.contains(trip1));
        assertTrue(tripsByCar.contains(trip3));

        Collection<Trip> tripsByBikeAndTrain = trips.getTripsByRoute("bike/train");
        assertThat(tripsByBikeAndTrain.size(), is(1));
        assertTrue(tripsByBikeAndTrain.contains(trip2));
    }

    private GenericDao dao;

    @ClassRule
    public static EmbeddedCassandra embeddedCassandraRule = EmbeddedCassandra.EmbeddedCassandraRuleBuilder.newEmbeddedCassandraRule()
            .build();

    private String clusterName = "DefaultCluster";
    @Rule
    public CassandraRule cassandraRule = new CassandraRule(newEmbeddedCassandraConfiguration().clusterName(clusterName).build());

    @Before
    public void before() {
        factory = new GenericCassandraDaoFactory("localhost", 1);
        factory.setClusterName(clusterName);
    }

    public static final String LEGS_1 = "[" +
            "  { origin: '1, St. John Street, Doeville', destination: '100, St. John Street, Doeville', distance: 1000, transport: 'CAR' }" +
            "  { origin: 'Interstate 16, exit 4', destination: 'Interstate 16, exit 1', distance: 17500, transport: 'CAR' }" +
            "  { origin: '1, Acme Avenue, Metropolis', destination: '999, Acme Avenue, Metropolis', distance: 4500, transport: 'CAR' }" +
            "]";
    public static final String LEGS_2 = "[" +
            "  { origin: '1, St. John Street, Doeville', destination: 'Doeville Station', distance: 500, transport: 'BICYCLING' }" +
            "  { origin: 'Doeville Station', destination: 'Metropolis Central', distance: 16500, transport: 'TRAIN' }" +
            "  { origin: 'Metropolis Central', destination: '999, Acme Avenue, Metropolis', distance: 300, transport: 'WALKING' }" +
            "]";

}

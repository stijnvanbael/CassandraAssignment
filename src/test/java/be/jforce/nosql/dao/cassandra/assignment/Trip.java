package be.jforce.nosql.dao.cassandra.assignment;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Objects;

public class Trip {
    private final String email;

    private final String route;

    private DateTime start;

    private Duration duration;

    public Trip(String email, String route) {
        this.email = email;
        this.route = route;
    }

    public String getEmail() {
        return email;
    }

    public String getRoute() {
        return route;
    }

    public DateTime getStart() {
        return start;
    }

    public void setStart(DateTime start) {
        this.start = start;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, route, start.getMillis());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Trip)) {
            return false;
        }
        Trip that = this.getClass().cast(obj);
        return Objects.equals(this.email, that.email) &&
                Objects.equals(this.route, that.route) &&
                Objects.equals(this.start.getMillis(), that.start.getMillis());
    }
}

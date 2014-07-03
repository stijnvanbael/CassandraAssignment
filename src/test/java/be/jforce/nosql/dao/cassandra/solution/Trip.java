package be.jforce.nosql.dao.cassandra.solution;

import com.melexis.ape.hector.wrapper.annotation.ColumnName;
import com.melexis.ape.hector.wrapper.annotation.ColumnValue;
import com.melexis.ape.hector.wrapper.annotation.RowKey;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Objects;

public class Trip {
    @RowKey
    private final String email;

    @RowKey
    private final String route;

    @ColumnName(storeAs = Long.class)
    private DateTime start;

    @ColumnValue(storeAs = Long.class)
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

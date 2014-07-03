package be.jforce.nosql.dao.cassandra.example;

import com.melexis.ape.hector.wrapper.annotation.ColumnName;
import com.melexis.ape.hector.wrapper.annotation.ColumnValue;
import org.joda.time.Duration;
import org.joda.time.LocalDate;

public class Registration {
    @ColumnName(storeAs = String.class)
    private LocalDate date;

    @ColumnValue(storeAs = Long.class)
    private Duration duration;

    public Registration(LocalDate date, Duration duration) {
        this.date = date;
        this.duration = duration;
    }

    public LocalDate getDate() {
        return date;
    }

    public Duration getDuration() {
        return duration;
    }
}

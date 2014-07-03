package be.jforce.nosql.dao.cassandra.solution;


import com.melexis.ape.hector.wrapper.annotation.NamedColumn;
import com.melexis.ape.hector.wrapper.annotation.NoSQLEntity;
import com.melexis.ape.hector.wrapper.annotation.RowKey;

@NoSQLEntity
public class Commuter {
    @RowKey
    private final String email;

    @NamedColumn
    private String name;

    @NamedColumn
    private String homeAddress;

    @NamedColumn
    private String workAddress;

    public Commuter(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }
}

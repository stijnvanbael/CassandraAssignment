package be.jforce.nosql.dao.cassandra.example;


import com.melexis.ape.hector.wrapper.annotation.ColumnName;
import com.melexis.ape.hector.wrapper.annotation.ColumnValue;
import com.melexis.ape.hector.wrapper.annotation.NoSQLEntity;
import com.melexis.ape.hector.wrapper.annotation.RowKey;

@NoSQLEntity
public class RoleAssignment {
    @RowKey
    private String department;

    @ColumnName
    private String id;

    @ColumnValue(storeAs = String.class)
    private Role role;

    public RoleAssignment(String department, String id, Role role) {
        this.department = department;
        this.id = id;
        this.role = role;
    }

    public String getDepartment() { return department; }

    public String getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }
}

package be.jforce.nosql.dao.cassandra.example;


import com.melexis.ape.hector.wrapper.annotation.NamedColumn;
import com.melexis.ape.hector.wrapper.annotation.NoSQLEntity;
import com.melexis.ape.hector.wrapper.annotation.RowKey;

@NoSQLEntity
public class Employee {
    @RowKey
    private String department;

    @RowKey
    private String trigram;

    @NamedColumn
    private String name;

    @NamedColumn
    private String phoneNumber;

    public Employee(String department, String trigram) {
        this.department = department;
        this.trigram = trigram;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDepartment() {
        return department;
    }

    public String getTrigram() {
        return trigram;
    }
}

package model;

import Factories.VFactory;
import com.Vickx.Biblix.DataBase.DataBase;
import com.Vickx.Biblix.DataBase.Query;
import com.Vickx.Biblix.Date.DateTime;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;

public class Identity implements Comparable<Identity>{

    //region Membres
    private String name = "";
    private String firstName = "";
    private DateTime dateDeNaissance;
    private String knownFor = "";
    private String street = "",city = "";
    private int postCode = 0, number = 0;
    private int id =0;

    //endregion

    public Identity(String name, String firstName, DateTime dateDeNaissance){
        this.name = name;
        this.firstName = firstName;
        this.dateDeNaissance = dateDeNaissance;
    }

    public int compareTo(Identity identity){
        Comparator<Identity> comparator = Comparator.comparing(Identity::getName).thenComparing(Identity::getFirstName).thenComparing(Identity::getDateDeNaissance);
        return comparator.compare(this,identity);
    }

    @Override
    public String toString(){
        return this.name.toUpperCase() + " " + this.firstName + " " + this.dateDeNaissance;
    }

    @Override
    public boolean equals(Object obj){
        if(getClass() != obj.getClass())
            return  false;

        return this.name.equals(((Identity)obj).name) && this.firstName.equals(((Identity)obj).firstName) && this.dateDeNaissance.equals(((Identity)obj).dateDeNaissance);
    }

    public static RecordCollection getPhoneNumbers(Identity identity){
        RecordCollection records = new RecordCollection();

        DataBase db = VFactory.getDbo();
        Query query = db.getQuery(true);
        ArrayList<Register> registers = Register.getRegisters();

        for(Register register : registers) {
            query.setQuery("SELECT * FROM t_register_" + register.getUnitCode() + " WHERE identity = " + identity.getId());
            db.setQuery(query);
            ResultSet resultSet = db.executeQuery();
            try {
                while (resultSet.next()) {
                    PhoneNumber phoneNumber = new PhoneNumber(resultSet.getString("phoneNumber"));
                    Record record = new Record(identity, phoneNumber);
                    record.setSource(register.getName());
                    record.setLastOccurence(resultSet.getInt("year"));
                    records.add(record);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return records;
    }

    //region Ascesseurs

    public String getKnownFor() {
        return knownFor;
    }

    public void setKnownFor(String knownFor) {
        this.knownFor = knownFor;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public DateTime getDateDeNaissance() {
        return dateDeNaissance;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //endregion
}

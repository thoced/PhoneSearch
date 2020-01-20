package model;

import Factories.VFactory;
import com.Vickx.Biblix.DataBase.DataBase;
import com.Vickx.Biblix.DataBase.Query;
import com.Vickx.Biblix.Date.DateTime;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class PhoneNumber implements Comparable<PhoneNumber>{

    private String phoneNumber;

    public PhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public static PhoneNumber getInstance(String string){
        return new PhoneNumber(PhoneNumber.parse(string));
    }

    /**
     *
     * @param phoneNumbers The list of phone numbers to search
     * @return The result of the query
     */
    public static RecordCollection getIdentities(ArrayList<String> phoneNumbers) {

        RecordCollection records = new RecordCollection();

        DataBase db = VFactory.getDbo();

        Query query = db.getQuery(true);

        String conditionElements = " WHERE ";

        ArrayList<Register> registers = Register.getRegisters();

        if(registers.size() == 0)
            return new RecordCollection();

        for(String number : phoneNumbers)
            conditionElements = conditionElements.concat("phoneNumber LIKE " + db.quote("%" + number + "%") + " OR ");

        conditionElements = conditionElements.substring(0,conditionElements.length() - 4);

        for(Register register : registers){

            query.setQuery("SELECT * FROM " + db.quoteName("t_register_" + register.getUnitCode()) + " AS R JOIN " + db.quoteName("t_base_identities") + " AS I ON R.identity = I.id " + conditionElements);
            db.setQuery(query);
            ResultSet resultSet = db.executeQuery();
            try{
                while (resultSet.next()){
                    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                    PhoneNumber number = new PhoneNumber(resultSet.getString("phoneNumber"));
                    Identity identity = new Identity(resultSet.getString("name"), resultSet.getString("firstName"), DateTime.parse(resultSet.getString("dateOfBirth")));
                    identity.setCity(resultSet.getString("city"));
                    identity.setNumber(resultSet.getInt("number"));
                    identity.setPostCode(resultSet.getInt("postCode"));
                    identity.setStreet(resultSet.getString("street"));
                    identity.setId(resultSet.getInt("identity"));
                    Record record = new Record(identity,number);
                    identity.setKnownFor(resultSet.getString("bng"));
                    record.setLastOccurence(resultSet.getInt("year"));
                    record.setSource(register.getName());
                    records.add(record);
                }
            }
            catch (SQLException e){
                System.out.println(e.getMessage());
            }

        }

        return records;
    }


    public static RecordCollection getIdentities(PhoneNumber number){
        ArrayList<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(number.toString());
        return getIdentities(phoneNumbers);
    }


    public static String parse(String string){

        if(string.toCharArray()[0] == '0'){
            return  "32" + string.substring(1);
        }
        else{
            if(string.toCharArray()[0] == '+'){
                return string.substring(1);
            }
            else
                return string;
        }

    }



    @Override
    public String toString(){
        return this.phoneNumber;
    }

    public int compareTo(PhoneNumber phoneNumber){
        return this.phoneNumber.compareTo(phoneNumber.toString());
    }

    @Override
    public boolean equals(Object obj) {
        if(getClass() != obj.getClass())
            return  false;
        return this.phoneNumber.equals(((PhoneNumber)obj).phoneNumber);
    }

}

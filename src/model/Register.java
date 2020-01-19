package model;

import Factories.VFactory;
import com.Vickx.Biblix.DataBase.DataBase;
import com.Vickx.Biblix.DataBase.Query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Register {

    private int id = 0;

    public String getName() {
        return name;
    }

    private String name;

    private int unitCode;

    public Register(int id, int unitCode, String name){
        this.id = id;
        this.unitCode = unitCode;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getUnitCode() {
        return unitCode;
    }

    public static ArrayList<Register> getRegisters() {
        ArrayList<Register> list = new ArrayList<Register>();

        DataBase db = VFactory.getDbo();

        Query query = db.getQuery(true);
        query.Select("id,name,unit");
        query.From("t_base_registers");

        db.setQuery(query);

        ResultSet resultSet = db.executeQuery();

        try{
            while(resultSet.next()){
                Register register = new Register(resultSet.getInt("id"), resultSet.getInt("unit"), resultSet.getString("name"));
                list.add(register);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            return new ArrayList<>();
        }

        return list;
    }

}

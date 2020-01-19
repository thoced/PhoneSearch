package model;

import Factories.VFactory;
import com.Vickx.Biblix.DataBase.DataBase;
import com.Vickx.Biblix.DataBase.Query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;

public class Section {

    private String name;

    private int id;

    public Section(){
        this(0,null);
    }

    public Section(int id, String name) {
        this.id = id;
        this.name = name;
        if(id==0)
            this.name = null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Dossier> getDossiers() {
        ArrayList<Dossier> liste = new ArrayList<Dossier>();
        DataBase dataBase = VFactory.getDbo();

        Query query = dataBase.getQuery(true);

        query.Select("*");
        query.From(dataBase.quoteName("t_base_dossiers"));
        query.Where(dataBase.quoteName("section") + "=" + this.id);
        dataBase.setQuery(query);
        ResultSet resultSet = dataBase.executeQuery();
        try {

            while (resultSet.next()) {
                Dossier dossier = new Dossier();
                dossier.setName(resultSet.getString("name"));
                liste.add(dossier);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            return new ArrayList<Dossier>();
        }

        return liste;
    }

    public static ArrayList<Section> getSections(){

        DataBase dataBase = VFactory.getDbo();
        if(!dataBase.isConnected())
            return new ArrayList<>();

        Query query = dataBase.getQuery(true);

        query.Select("id," + dataBase.quoteName("name"));
        query.From("t_base_sections");

        dataBase.setQuery(query);
        ResultSet resultSet = dataBase.executeQuery();

        ArrayList<Section> sections = new ArrayList<>();

        try {
            while (resultSet.next()) {
                Section section = new Section();
                section.setId(resultSet.getInt("id"));
                section.setName(resultSet.getString("name"));
                sections.add(section);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        sections.sort(new SortByName());
        return sections;
    }

}

class SortByName implements Comparator<Section>{
    public int compare(Section a, Section b){
        return a.getName().compareTo(b.getName());
    }
}

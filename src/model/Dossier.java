package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Dossier {


    private StringProperty name = null;

   public Dossier(){
        this(null);
   }

   public Dossier(String name){
       this.name = new SimpleStringProperty(name);
   }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String toString(){
       return this.name.getValue();
    }



}

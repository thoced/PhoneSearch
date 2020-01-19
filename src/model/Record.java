package model;

import com.Vickx.Biblix.Date.DateTime;
import com.Vickx.Biblix.Helper;
import javafx.beans.property.*;
import javafx.beans.value.ObservableBooleanValue;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Comparator;

public class Record {

    //region Membres

    private PhoneNumber phoneNumber;

    private Identity identity;

    int lastOccurence = 0;

    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    private String source = "";

    //endregion

    public Record(Identity identity, PhoneNumber phoneNumber){
        this.identity = identity;
        this.phoneNumber = phoneNumber;
    }

    //region Ascesseurs

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public Identity getIdentity() {
        return identity;
    }

    public StringProperty getNameProperty(){
        return new SimpleStringProperty(this.identity.getName().toUpperCase());
    }

    public StringProperty getFirstNameProperty(){
        return new SimpleStringProperty(this.identity.getFirstName());
    }

    public StringProperty getDateDeNaissanceProperty(){
        return new SimpleStringProperty(Helper.AddZerro(this.identity.getDateDeNaissance().getDay()) + "." + Helper.AddZerro(this.identity.getDateDeNaissance().getMonth()) + "." + this.identity.getDateDeNaissance().getYear());
    }

    public StringProperty getKnownForProperty(){
        return new SimpleStringProperty( this.identity.getKnownFor());
    }

    public StringProperty getPhoneNumberProperty(){
        return new SimpleStringProperty(this.phoneNumber.toString());
    }

    public StringProperty getLastOccurenceProperty(){
        return new SimpleStringProperty(String.valueOf(this.lastOccurence));
    }

    public Integer getLastOccurence() {
        return lastOccurence;
    }

    public void setLastOccurence(int lastOccurence) {
        this.lastOccurence = lastOccurence;
    }

    public final boolean isSelected() {
        return selected.get();
    }

    public final void setSelected(boolean value) {
        selected.set(value);
    }

    public final BooleanProperty selectedProperty() {
        return selected;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    //endregion

    @Override
    public String toString(){
        return this.phoneNumber + ": " + this.identity.getName() + " " + this.identity.getFirstName() + "(" + this.identity.getDateDeNaissance().toString() + ")";
    }

    @Override
    public Record clone(){
        try {
            super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new Record(this.identity,this.phoneNumber);
    }

}

class SortByNumbers implements Comparator<Record> {

    public int compare(Record a, Record b){
        Comparator<Record> comparator = Comparator.comparing(Record::getPhoneNumber).thenComparing(Record::getIdentity);
        return comparator.compare(a,b);

    }
}


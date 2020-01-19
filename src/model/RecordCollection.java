package model;

import javafx.beans.value.ObservableValue;

import java.util.ArrayList;

/**
 * Provide an ArrayList with specific fonctions for model.Record objects
 */
public class RecordCollection extends ArrayList<Record> {

    String filter = "";

    public void sort(){
        this.sort(new SortByNumbers());
    }

    private int indexOfCouple(Identity identity, PhoneNumber phoneNumber){
        int index = -1;

        for (int i=0; i< this.size();i++){
            Record record = this.get(i);
            if(record.getPhoneNumber().equals(phoneNumber) && record.getIdentity().equals(identity))
                return i;
        }

        return -1;
    }

    public RecordCollection getGroupedRecords(){
        RecordCollection collection = new RecordCollection();
        int index = -1;

        for(Record record : this){

            if(!filter.equals("")){
                if(!filter.equals("U")) {
                    if (!record.getIdentity().getKnownFor().contains(filter))
                        continue;
                }
                else
                    if(!record.getIdentity().getKnownFor().equals(""))
                        continue;
            }

            index = collection.indexOfCouple(record.getIdentity(),record.getPhoneNumber());
            if(index == -1){
                collection.add(record);
            }
            else{
                Record collectionRecord = collection.get(index);
                if(collectionRecord.getLastOccurence() < record.getLastOccurence()){
                    collection.remove(collectionRecord);
                    collection.add(record);
                }
            }
        }

        collection.sort();
        return collection;
    }

    public RecordCollection getRecords(){
        RecordCollection collection = new RecordCollection();
        int index = -1;

        for(Record record : this){

            //Si le filtre contient quelque chose
            if(!filter.equals("")){
                if(!filter.equals("U")) {
                    if (!record.getIdentity().getKnownFor().contains(filter))
                        continue;
                }
            }
            collection.add(record);
        }

        return collection;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

}

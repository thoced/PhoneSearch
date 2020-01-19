package model.Exporters;

import Factories.VFactory;
import com.Vickx.Biblix.Date.DateTime;
import model.Record;
import model.RecordCollection;
import model.Settings;
import java.util.ArrayList;

public class ConvocationExporter {

    private RecordCollection records;
    private ArrayList<DateTime> dateTimes;

    public ConvocationExporter(RecordCollection records, ArrayList<DateTime> datetimes){
        this.records = records;
        this.dateTimes = datetimes;
    }

    public void Export(){
        Settings settings = VFactory.getSettings();

        Record record = this.records.get(1);
        DateTime dateTime = this.dateTimes.get(1);

        try
        {
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //private Bookmarks




}

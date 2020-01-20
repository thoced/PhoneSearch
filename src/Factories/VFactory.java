package Factories;

import com.Vickx.Biblix.DataBase.DataBase;
import model.Settings;

import java.io.IOException;

public abstract class VFactory {

    private static DataBase dataBase;

    public static DataBase getDbo(){
        return dataBase;
    }

    public static void initDbo(String  host, int socket, String Login, String passWord, String base){
        dataBase = DataBase.getInstance();
        dataBase.Connect(host,socket,Login,passWord,base);
    }

    public static Settings getSettings(){
        try {
            return Settings.getInstance();
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public final static Boolean DEBUG_NUMBER_SEARCH_FORM = true;
    public final static Boolean DEBUG_WORD_EXPORT_FORM = false;

    public final static Boolean Enable_NumSearch_Excel_Export = true;
    public final static Boolean Enable_NumSearch_Word_Export = false;
}

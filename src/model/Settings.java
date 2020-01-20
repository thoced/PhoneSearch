package model;

import com.Vickx.Biblix.Date.TimeSpan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {

    private static Settings settings = null;

    private String fileName;
    private String host = "localhost";
    private int socket = 3306;
    private String login = "login";
    private String passWord = "passWord";
    private String base = "base";

    private TimeSpan convocationFirstHour = new TimeSpan(9,0,0);
    private int convocationInterval = 30;
    private String convocationSourceFileName = "";
    private String exportersDefaultDirectory = "";
    private Boolean genericConvocation= false;
    private Boolean openAppOnExport = true;

    private Settings() throws IOException{
        fileName = "." + File.separator + "config.cfg";

        File file = new File(fileName);
        if(!file.exists()){
            saveConfig();
        }
    }

    public void saveConfig() throws IOException {
        Properties properties = new Properties();
        properties.setProperty("host",host);
        properties.setProperty("socket",String.valueOf(socket));
        properties.setProperty("login",login);
        properties.setProperty("passWord", passWord);
        properties.setProperty("dbase", base);
        properties.setProperty("convocationSourceFileName", this.convocationSourceFileName);
        properties.setProperty("defaultExportDirectory", this.exportersDefaultDirectory);
        properties.setProperty("convocationFirstHour", this.convocationFirstHour.getHour() + ":" + this.convocationFirstHour.getMinute() + ":" + this.convocationFirstHour.getSecond());
        properties.setProperty("convocationInterval", String.valueOf(this.convocationInterval));
        properties.setProperty("genericConvocation", this.genericConvocation?"1":"0");
        properties.setProperty("openAppOnExport", this.openAppOnExport?"1":"0");

        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        properties.store(fileOutputStream,null);
    }

    public static Settings getInstance() throws IOException{

        if(settings == null) {
            settings = new Settings();
            settings.loadSettings();
        }

        return settings;
    }

    private void loadSettings(){
        try {
        Settings s = new Settings();

            FileInputStream fileInputStream = new FileInputStream(fileName);
            Properties properties = new Properties();
            properties.load(fileInputStream);
            host = properties.getProperty("host");
            try {
                socket = Integer.parseInt(properties.getProperty("socket"));
            }
            catch (NumberFormatException e){
                    socket = 3306;
                }
            login = properties.getProperty("login");
            passWord = properties.getProperty("passWord");
            base = properties.getProperty("dbase");
            if(host == null){ host = "127.0.0.1"; }
            if(login == null){ host = "login"; }
            if(passWord == null){ host = "pwd"; }
            if(base == null){ host = "dataBase"; }
            convocationSourceFileName = properties.getProperty("convocationSourceFileName");
            exportersDefaultDirectory = properties.getProperty("defaultExportDirectory");
            String value = properties.getProperty("convocationFirstHour");
            if(value == null || value.equals(""))
                this.convocationFirstHour = new TimeSpan(9,0,0);
            else{
                try{
                    String[] parts = value.split(":");
                    this.convocationFirstHour = new TimeSpan(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]),Integer.parseInt(parts[2]));
                }catch (Exception e){
                    System.out.println("Cannot parse \"" + value + "\" from de settings file to convert it to TimeSpan");
                    this.convocationFirstHour =  new TimeSpan(9,0,0);
                }
            }

            if(properties.getProperty("genericConvocation")!=null)
                this.genericConvocation = properties.getProperty("genericConvocation").equals("1");
            if(properties.getProperty("openAppOnExport")!=null)
                this.genericConvocation = properties.getProperty("openAppOnExport").equals("1");
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getSocket() {
        return socket;
    }

    public void setSocket(int socket) {
        this.socket = socket;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setConvocationFirstHour(TimeSpan timeSpan){
        this.convocationFirstHour = timeSpan;
    }

    public TimeSpan getConvocationFirstHour() {
        return convocationFirstHour;
    }

    public void setConvocationInterval(int interval){
        this.convocationInterval = interval;
    }

    public int getConvocationInterval() {
        return convocationInterval;
    }

    public String getConvocationSourceFileName() {
        return this.convocationSourceFileName;
    }

    public void setConvocationSourceFileName(String convocationSourceFileName) {
        this.convocationSourceFileName = convocationSourceFileName;
    }

    public String getExportersDefaultDirectory() {
        return exportersDefaultDirectory;
    }

    public void setExportersDefaultDirectory(String exportersDefaultDirectory) {
        this.exportersDefaultDirectory = exportersDefaultDirectory;
    }

    public Boolean getGenericConvocation() {
        return genericConvocation;
    }

    public void setGenericConvocation(Boolean genericConvocation) {
        this.genericConvocation = genericConvocation;
    }

    public Boolean getOpenAppOnExport(){return  openAppOnExport; }

    public void setOpenAppOnExport(Boolean value){ this.openAppOnExport = value; }
}

package view.UtilitiesForms.SettingsForm;

import Factories.VFactory;
import com.Vickx.Biblix.Controls.HourTextField;
import com.Vickx.Biblix.Controls.NumericTextField;
import com.Vickx.Biblix.DataBase.DataBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.event.*;
import model.Settings;

import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class SettingsFormController {

    @FXML
    Button buttonSave;

    @FXML
    Button buttonTest;

    @FXML
    Button buttonCancel;

    @FXML
    Button fileSelectButton;

    @FXML
    Button dirSelectButton;

    @FXML
    TextField textFieldHost;

    @FXML
    TextField textFieldSocket;

    @FXML
    TextField textFieldLogin;

    @FXML
    PasswordField passwordField;

    @FXML
    TextField textFieldDataBase;

    @FXML
    TextField textFieldConvocationFileName;

    @FXML
    TextField textFieldExportDirectory;

    @FXML
    CheckBox checkBoxDefaultConvocation;

    @FXML
    TabPane tabPane;

    @FXML
    AnchorPane tabDB;

    @FXML
    AnchorPane tabExport;

    NumericTextField numericTextField;

    HourTextField hourTextField;

    Stage stage;

    Settings settings;


    @FXML
    private void initialize(){
       this.settings = VFactory.getSettings();

        this.textFieldHost.setText(this.settings.getHost());
        this.textFieldSocket.setText(String.valueOf(this.settings.getSocket()));
        this.textFieldLogin.setText(this.settings.getLogin());
        this.passwordField.setText(this.settings.getPassWord());
        this.textFieldDataBase.setText(this.settings.getBase());

        this.buttonCancel.setOnAction(this::buttonCancel_click);
        this.buttonSave.setOnAction(this::buttonSave_click);
        this.buttonTest.setOnAction(this::buttonTest_click);

        this.textFieldSocket.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d*")){
                    textFieldSocket.setText(newValue.replaceAll("[^\\d]",""));
                }
            }
        });

        this.textFieldConvocationFileName.setText(settings.getConvocationSourceFileName());
        this.fileSelectButton.setOnAction(this::fileSelectButton_Click);
        this.dirSelectButton.setOnAction(this::dirSelectButton_Click);
        this.textFieldExportDirectory.setText(settings.getExportersDefaultDirectory());
        this.checkBoxDefaultConvocation.setSelected(this.settings.getGenericConvocation());
        this.checkBoxDefaultConvocation.setOnAction(this::checkBoxDefaultConvocation_Change);
    }

    private void checkBoxDefaultConvocation_Change(ActionEvent event) {
        this.settings.setGenericConvocation(this.checkBoxDefaultConvocation.isSelected());
    }

    private void buttonCancel_click(ActionEvent event){
        this.stage.close();
    }

    private void buttonSave_click(ActionEvent event){
        this.settings.setHost(this.textFieldHost.getText());
        this.settings.setBase(this.textFieldDataBase.getText());
        this.settings.setLogin(this.textFieldLogin.getText());
        this.settings.setPassWord(this.passwordField.getText());
        this.settings.setSocket(Integer.parseInt(this.textFieldSocket.getText()));
        VFactory.initDbo(this.settings.getHost(),this.settings.getSocket(),this.settings.getLogin(),this.settings.getPassWord(), this.settings.getBase());
        this.settings.setExportersDefaultDirectory(this.textFieldExportDirectory.getText());
        this.settings.setConvocationSourceFileName(this.textFieldConvocationFileName.getText());
        this.settings.setConvocationFirstHour(this.hourTextField.getTimeSpan());
        this.settings.setConvocationInterval(this.numericTextField.getValue());
        try{
        this.settings.saveConfig();
        }catch (Exception e){
            e.printStackTrace();
        }

        this.stage.close();
    }

    private void buttonTest_click(ActionEvent event){

        DataBase myConnection = VFactory.getDbo();
        Boolean alreadyConnected = myConnection.isConnected();

        if(alreadyConnected)
            myConnection.Disconnect();

        DataBase db = new DataBase();
        db.Connect(this.textFieldHost.getText(),Integer.parseInt(this.textFieldSocket.getText()), this.textFieldLogin.getText(),this.passwordField.getText(),this.textFieldDataBase.getText());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Test de la connexion");
        if(db.isConnected()){
            alert.setContentText("La connexion à la base de données a réussi.");
        }
        else {

            alert.setContentText("La connexion à la base de données a échoué.");
        }
        alert.showAndWait();

        if(alreadyConnected)
            myConnection.ReConnect();
    }

    private void fileSelectButton_Click(ActionEvent event){
        FileChooser fileChooser =new FileChooser();
        fileChooser.setInitialDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
        File file = fileChooser.showOpenDialog(this.stage);
        if(file == null)
            return;
        this.textFieldConvocationFileName.setText(this.settings.getConvocationSourceFileName());
    }

    private void dirSelectButton_Click(ActionEvent event){
        DirectoryChooser chooser =new DirectoryChooser();
        chooser.setInitialDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
        File dir = chooser.showDialog(this.stage);
        if(dir == null)
            return;

        this.textFieldExportDirectory.setText(dir.getAbsolutePath());
    }

    public Stage getStage() {
        return this.stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        hourTextField = new HourTextField();
        hourTextField.setLayoutX(30);
        hourTextField.setLayoutY(110);
        AnchorPane.setLeftAnchor(hourTextField,230.0);
        tabExport.getChildren().add(hourTextField);

        numericTextField = new NumericTextField();
        numericTextField.setPlaceHolder("min");
        numericTextField.setLayoutX(30);
        numericTextField.setLayoutY(150);
        AnchorPane.setLeftAnchor(numericTextField,230.0);
        numericTextField.setMaxSize((short)60);
        numericTextField.setPrefWidth(36);
        tabExport.getChildren().add(numericTextField);


        this.numericTextField.setValue(settings.getConvocationInterval());
        this.hourTextField.setTimeSpan(settings.getConvocationFirstHour());
    }

}

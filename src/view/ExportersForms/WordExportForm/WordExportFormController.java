package view.ExportersForms.WordExportForm;

import Factories.VFactory;
import com.Vickx.Biblix.Controls.HourTextField;
import com.Vickx.Biblix.Controls.NumericTextField;
import com.Vickx.Biblix.Controls.PlaceHolderTextField;
import com.Vickx.Biblix.Date.DateTime;
import com.Vickx.Biblix.Date.TimeSpan;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Record;
import model.RecordCollection;
import model.Settings;
import java.time.LocalDate;
import java.util.ArrayList;

public class WordExportFormController {

    //region Membres

    Stage stage;

    ButtonType result = ButtonType.CANCEL;

    RecordCollection records;

    AnchorPane scene = null;
    ScrollPane scrollPane = null;
    Pane pane = new Pane();
    ArrayList<DateTime> dateTimes;

    @FXML
    Button buttonOk;

    @FXML
    Button buttonCancel;

    //endregion

    @FXML
    private void initialize(){
        this.buttonCancel.setOnAction(event -> {this.result = ButtonType.CANCEL; this.stage.close();});
        this.buttonOk.setOnAction(event -> {
            this.dateTimes = new ArrayList<>();

            for(int i = 0; i< this.records.size(); i++){
                DatePicker datePicker = (DatePicker)this.pane.lookup("#datePicker" + i );
                this.dateTimes.add(new DateTime(datePicker.getValue()));
            }
            this.result = ButtonType.OK;
            this.stage.close();});
    }

    private void addRow(Record record, int index) {

        int Y = 14 + (index * 30);
        int YLabel = Y + 4;

        Label labelDate = new Label("Date :");
        labelDate.setLayoutX(14);
        labelDate.setLayoutY(YLabel);
        pane.getChildren().add(labelDate);

        DatePicker datePicker = new DatePicker();
        datePicker.setId("datePicker" + index);
        datePicker.setLayoutX(50);
        datePicker.setLayoutY(Y);
        datePicker.setValue(LocalDate.now());
        datePicker.setOnAction(this::datePickerOnAction);
        //datePicker.setStyle("-fx-control-inner-background: #add6ff;");
        pane.getChildren().add(datePicker);

        Label labelHour = new Label("Heure :");
        labelHour.setLayoutX(235);
        labelHour.setLayoutY(YLabel);
        pane.getChildren().add(labelHour);


        HourTextField hourTextField = new HourTextField();
        hourTextField.setLayoutX(285);
        hourTextField.setId("hourTextField" + index);
        hourTextField.setLayoutY(Y);
        hourTextField.setPrefWidth(50);
        hourTextField.setTimeSpan(new TimeSpan(9,0,0));
        hourTextField.setHourListener(this::hourTextFieldChange);
        //hourTextField.setStyle("-fx-control-inner-background: #add6ff;");
        pane.getChildren().add(hourTextField);

        Label labelConvoque = new Label("Convoqué :");
        labelConvoque.setLayoutX(345);
        labelConvoque.setLayoutY(YLabel);
        pane.getChildren().add(labelConvoque);

        TextField textFieldName = new TextField();
        textFieldName.setLayoutX(415);
        textFieldName.setLayoutY(Y);
        textFieldName.setText(record.getIdentity().getName());
        pane.getChildren().add(textFieldName);

        TextField textFieldFirtName = new TextField();
        textFieldFirtName.setLayoutX(570);
        textFieldFirtName.setLayoutY(Y);
        textFieldFirtName.setPrefWidth(100);
        textFieldFirtName.setText(record.getIdentity().getFirstName());
        pane.getChildren().add(textFieldFirtName);

        PlaceHolderTextField placeHolderTextFieldAdress = new PlaceHolderTextField();
        placeHolderTextFieldAdress.setLayoutX(680);
        placeHolderTextFieldAdress.setLayoutY(Y);
        placeHolderTextFieldAdress.setPlaceHolder("Rue");
        pane.getChildren().add(placeHolderTextFieldAdress);

        PlaceHolderTextField placeHolderTextFieldNumber = new PlaceHolderTextField();
        placeHolderTextFieldNumber.setLayoutX(835);
        placeHolderTextFieldNumber.setLayoutY(Y);
        placeHolderTextFieldNumber.setPlaceHolder("N°");
        placeHolderTextFieldNumber.setPrefWidth(50);
        pane.getChildren().add(placeHolderTextFieldNumber);

        NumericTextField numericTextFieldCP = new NumericTextField();
        numericTextFieldCP.setLayoutX(895);
        numericTextFieldCP.setLayoutY(Y);
        numericTextFieldCP.setPlaceHolder("CP");
        numericTextFieldCP.setPrefWidth(80);
        numericTextFieldCP.setMaxSize((short)4);
        pane.getChildren().add(numericTextFieldCP);

        PlaceHolderTextField placeHolderTextFieldCity = new PlaceHolderTextField();
        placeHolderTextFieldCity.setLayoutX(980);
        placeHolderTextFieldCity.setLayoutY(Y);
        placeHolderTextFieldCity.setPrefWidth(100);
        placeHolderTextFieldCity.setPlaceHolder("Ville");
        pane.getChildren().add(placeHolderTextFieldCity);

    }

    private void datePickerOnAction(ActionEvent event) {
        DatePicker datePicker = (DatePicker)event.getSource();
        LocalDate date = datePicker.getValue();
        int index = Integer.parseInt(datePicker.getId().replaceAll("datePicker",""));

        for(int i = index+1; i<this.records.size(); i++){
            DatePicker datePicker1 = (DatePicker)this.pane.lookup("#datePicker" + i);

            //Si la date suivante est plus petite que la nouvelle -> mettre à jour
            if(date.compareTo(datePicker1.getValue()) > 0) {
                datePicker1.setValue(date);
            }
            else
                break;
        }

    }

    private void hourTextFieldChange(TimeSpan timeSpan, HourTextField hourTextField){
        int index = Integer.parseInt(hourTextField.getId().replaceAll("hourTextField",""));
        TimeSpan newTimeSpan = timeSpan.clone();
        DatePicker datePicker = (DatePicker)this.pane.lookup("#datePicker" + index);
        LocalDate date = datePicker.getValue();
        Settings settings = VFactory.getSettings();

        for(int i = index+1; i< this.records.size(); i++){

            LocalDate newDate = ((DatePicker)this.pane.lookup("#datePicker" + i)).getValue();

            if(date.isEqual(newDate)){
                HourTextField textField = (HourTextField)this.pane.lookup("#hourTextField" + i);
                textField.setHourListener(null);
                if(settings != null)
                    newTimeSpan.addMinutes(settings.getConvocationInterval());
                else
                    newTimeSpan.addMinutes(30);

                textField.setTimeSpan(newTimeSpan);
                textField.setHourListener(this::hourTextFieldChange);
            }
            else {
                date = newDate;
                if(settings != null)
                    newTimeSpan = settings.getConvocationFirstHour().clone();
                else
                    newTimeSpan = new TimeSpan(9,0,0);
            }

        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setMinWidth(1150);
        this.stage.setMinHeight(700);
        scene = (AnchorPane)this.stage.getScene().getRoot();
        scrollPane = (ScrollPane)scene.getChildren().get(0);
        scrollPane.setContent(pane);
        scrollPane.setPannable(true);
        pane.setLayoutX(14);
        pane.setLayoutY(14);
        pane.setMinWidth(1100);
        pane.setMaxWidth(1100);
        pane.setMinHeight(30);
    }

    public ButtonType getResult() {
        return result;
    }

    public void setRecords(RecordCollection records) {
        this.records = records;
        int index = 0;
        for(Record record : this.records) {
            this.addRow(record, index++);
        }
    }

    public ArrayList<DateTime> getDateTimes() {
        return dateTimes;
    }
}

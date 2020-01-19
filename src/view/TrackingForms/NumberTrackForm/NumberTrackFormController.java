package view.TrackingForms.NumberTrackForm;

import com.Vickx.Biblix.Date.DateTime;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.PhoneNumber;
import model.Record;
import model.RecordCollection;

public class NumberTrackFormController {

    @FXML
    TableView<Record> tableView;

    @FXML
    Button button;

    @FXML
    TableColumn<Record,String> tableColumnName;

    @FXML
    TableColumn<Record,String> tableColumnFirstName;

    @FXML
    TableColumn<Record, String> tableColumnDateOfBirth;

    @FXML
    TableColumn<Record, String> tableColumnSource;

    PhoneNumber phoneNumber;

    RecordCollection records = new RecordCollection();

    Stage stage;


    @FXML
    private void initialize(){
        this.tableColumnName.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        this.tableColumnFirstName.setCellValueFactory(cellData -> cellData.getValue().getFirstNameProperty());
        this.tableColumnDateOfBirth.setCellValueFactory(cellData -> cellData.getValue().getDateDeNaissanceProperty());
        this.tableColumnSource.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastOccurence() + " - " + cellData.getValue().getSource()));
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.records = PhoneNumber.getIdentities(this.phoneNumber);
        this.tableView.setItems(FXCollections.observableArrayList(this.records));
        if(this.stage != null)
            this.stage.setTitle("Historique du numéro " + this.phoneNumber.toString());
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        this.button.setOnAction(event -> this.stage.close());
    }
}

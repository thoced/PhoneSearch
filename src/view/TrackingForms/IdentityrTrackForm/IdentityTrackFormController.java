package view.TrackingForms.IdentityrTrackForm;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Identity;
import model.PhoneNumber;
import model.Record;
import model.RecordCollection;


public class IdentityTrackFormController {

    @FXML
    TableView<Record> tableView;

    @FXML
    TableColumn<Record,String> tableColumnNumber;

    @FXML
    TableColumn<Record,String> tableColumnSource;

    @FXML
    Button button;

    Identity identity;

    RecordCollection records= new RecordCollection();

    Stage stage;

    @FXML
    private void initialize(){
        this.tableColumnNumber.setCellValueFactory(cellData -> cellData.getValue().getPhoneNumberProperty());
        this.tableColumnSource.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastOccurence() + " - " + cellData.getValue().getSource()));
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
        this.records = Identity.getPhoneNumbers(this.identity);
        this.tableView.setItems(FXCollections.observableArrayList(this.records));

        if(this.stage != null)
            this.stage.setTitle("Historique de " + this.identity.getName() + " " + this.identity.getFirstName());
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        this.button.setOnAction(event -> this.stage.close());
    }
}

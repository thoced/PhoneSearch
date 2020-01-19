package view.ExportersForms.ExcelExportFormForNumberSearch;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

public class ExcelExportFormForNumberSearchController {

    //region binds

    @FXML
    Button creerButton;

    @FXML
    Button annulerButton;

    @FXML
    CheckBox numeroCB;

    @FXML
    CheckBox nomCB;

    @FXML
    CheckBox prenomCB;

    @FXML
    CheckBox dateDeNaissanceCB;

    @FXML
    CheckBox knownForCB;

    @FXML
    CheckBox lastOccurenceCB;

    @FXML
    CheckBox ageCB;

    @FXML
    CheckBox dateDeConvocationCB;

    @FXML
    CheckBox adressCB;

    @FXML
    CheckBox sourceCB;

    @FXML
    CheckBox checkBoxCheckAll;

    //endregion

    ButtonType result = ButtonType.CANCEL;
    Stage stage;

    @FXML
    private void initialize(){
        this.numeroCB.setSelected(true);
        this.nomCB.setSelected(true);
        this.prenomCB.setSelected(true);
        this.dateDeNaissanceCB.setSelected(true);
        this.knownForCB.setSelected(true);
        this.ageCB.setSelected(true);
        this.creerButton.setOnAction(this::creerButton_Click);
        this.annulerButton.setOnAction(this::annulerButton_Click);
        this.checkBoxCheckAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                numeroCB.setSelected(checkBoxCheckAll.isSelected());
                nomCB.setSelected(checkBoxCheckAll.isSelected());
                prenomCB.setSelected(checkBoxCheckAll.isSelected());
                dateDeNaissanceCB.setSelected(checkBoxCheckAll.isSelected());
                dateDeConvocationCB.setSelected(checkBoxCheckAll.isSelected());
                knownForCB.setSelected(checkBoxCheckAll.isSelected());
                lastOccurenceCB.setSelected(checkBoxCheckAll.isSelected());
                ageCB.setSelected(checkBoxCheckAll.isSelected());
                adressCB.setSelected(checkBoxCheckAll.isSelected());
                sourceCB.setSelected(checkBoxCheckAll.isSelected());
            }
        });
    }

    private void creerButton_Click(ActionEvent event){
        if(     !this.getSource() &&
                !this.getDateDeConvocation() &&
                !this.getAdress() &&
                !this.getKnownFor() &&
                !this.getNom() &&
                !this.getPrenom() &&
                !this.getDateDeNaissance() &&
                !this.getAge() &&
                !this.getLastOccurence() &&
                !this.getNumero()
            ) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sélection");
            alert.setContentText("Au moins un élément doit être sélectionné pour l'export");
            alert.showAndWait();
            event.consume();
        }
        else {
            this.result = ButtonType.OK;
            this.stage.close();
        }
    }

    private void annulerButton_Click(ActionEvent event){
        this.stage.close();
    }

    //region Accesseurs

    public boolean getNumero() {
        return numeroCB.isSelected();
    }

    public boolean getNom() {
        return nomCB.isSelected();
    }

    public boolean getPrenom() {
        return prenomCB.isSelected();
    }

    public boolean getDateDeNaissance() {
        return dateDeNaissanceCB.isSelected();
    }

    public boolean getKnownFor() {
        return knownForCB.isSelected();
    }

    public boolean getLastOccurence() {
        return lastOccurenceCB.isSelected();
    }

    public boolean getAge() {
        return ageCB.isSelected();
    }

    public boolean getDateDeConvocation() {
        return dateDeConvocationCB.isSelected();
    }

    public boolean getAdress() {
        return adressCB.isSelected();
    }

    public boolean getSource() {
        return sourceCB.isSelected();
    }


    public ButtonType getResult(){
        return this.result;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    //endregion
}

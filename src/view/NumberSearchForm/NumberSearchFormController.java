package view.NumberSearchForm;

import Factories.Cells.*;
import Factories.VFactory;
import com.Vickx.Biblix.Date.DateTime;
import com.Vickx.Biblix.Helper;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Exporters.ConvocationExporter;
import model.Exporters.NumberSearchFormExcelExporter;
import model.PhoneNumber;
import model.Record;
import model.RecordCollection;
import model.Settings;
import org.apache.poi.ss.usermodel.Workbook;
import view.ExportersForms.ExcelExportFormForNumberSearch.ExcelExportFormForNumberSearchController;
import view.ExportersForms.WordExportForm.WordExportFormController;
import view.TrackingForms.IdentityrTrackForm.IdentityTrackFormController;
import view.TrackingForms.NumberTrackForm.NumberTrackFormController;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class NumberSearchFormController {

    //region FXML binds

    @FXML
    TableView<Record> tableView;

    @FXML
    TableColumn<Record,Boolean> tableColumnChecked;

    @FXML
    TableColumn<Record, String> tableColumnNumber;

    @FXML
    TableColumn<Record, String> tableColumnName;

    @FXML
    TableColumn<Record, String> tableColumnFirsName;

    @FXML
    TableColumn<Record, String> tableColumnDateDeNaissance;

    @FXML
    TableColumn<Record, String> tableColumnKnown;

    @FXML
    TableColumn<Record, String> tableColumnLastOccurence;

    @FXML
    ComboBox<String> comboBoxFilter;

    @FXML
    ComboBox<String> comboBoxDisplay;

    @FXML
    TextArea textArea;

    @FXML
    Button button;

    @FXML
    Label connexionLabel;

    @FXML
    Label resultLabel;

    @FXML
    TextField textFieldFiltering;

    @FXML
    Button buttonExcelExport;

    @FXML
    Button buttonWordExport;

    @FXML
    ToolBar toolBar;

    @FXML
    CheckBox checkBoxCheckAll;

    @FXML
    ProgressBar progressBar;

    @FXML
    AnchorPane scene;

    //endregion

    //region membres

    RecordCollection records = new RecordCollection();

    Stage stage;

    //endregion

    //region Initialisations

    @FXML
    private void initialize() {

        this.initializeComponents();
        this.initializeColumns();
        if(VFactory.getDbo().isConnected())
            this.connexionLabel.setText("Connecté");
        else
            this.connexionLabel.setText("Déconnecté");

        if(VFactory.DEBUG_NUMBER_SEARCH_FORM) {
            this.textArea.setText("32");
            this.button_click();
        }
        if(VFactory.DEBUG_WORD_EXPORT_FORM){
            this.textArea.setText("32");
            this.button_click();
            this.checkBoxCheckAll.setSelected(true);
            this.ckeckAllCheckBox_change_selected(new ActionEvent());
            this.buttonWordExport_click(new ActionEvent());
        }
    }

    private void initializeComponents() {

        //region Comboboxes

        this.comboBoxFilter.getItems().add(0,"Aucun Filtre");
        this.comboBoxFilter.getItems().add(1, "Inconnu");
        this.comboBoxFilter.getItems().add(2,"Crimes");
        this.comboBoxFilter.getItems().add(3,"Ecofin");
        this.comboBoxFilter.getItems().add(4,"Moeurs");
        this.comboBoxFilter.getItems().add(5,"Stups");
        this.comboBoxFilter.getItems().add(6,"Vols");
        this.comboBoxFilter.getSelectionModel().select(0);
        this.comboBoxFilter.setOnAction(this::comboBoxFilter_change);

        this.comboBoxDisplay.getItems().add(0,"Groupé");
        this.comboBoxDisplay.getItems().add(1,"Dégroupé");
        this.comboBoxDisplay.getSelectionModel().select(0);
        this.comboBoxDisplay.setOnAction(this::comboBoxDisplay_change);


        //endregion

        //region Divers

        this.button.setOnAction(actionEvent -> button_click());

        this.resultLabel.setText("aucun élément");

        //endregion

        //region ContextMenu pour TableView

        int menuItemImageSize = 20;

        ContextMenu contextMenu = new ContextMenu();

        MenuItem menuItemCopier= new MenuItem("Copier");
        contextMenu.getItems().add(menuItemCopier);
        menuItemCopier.setOnAction(event -> this.copySelectionToClipboard(this.tableView));

        Menu menuBNG= new Menu("Marquer l'identité comme connue :");
        menuBNG.setDisable(true);
        contextMenu.getItems().add(menuBNG);
        MenuItem crimeItem = new MenuItem("Crime");
        Image openIcon = new Image(getClass().getResourceAsStream("/pictures/crimes.jpg"));
        ImageView openView = new ImageView(openIcon);
        openView.setFitWidth(menuItemImageSize);
        openView.setFitHeight(menuItemImageSize);
        crimeItem.setGraphic(openView);

        menuBNG.getItems().add(crimeItem);
        MenuItem ecofinItem = new MenuItem("Ecofin");
        openIcon = new Image(getClass().getResourceAsStream("/pictures/ecofin.jpg"));
        openView = new ImageView(openIcon);
        openView.setFitWidth(menuItemImageSize);
        openView.setFitHeight(menuItemImageSize);
        ecofinItem.setGraphic(openView);
        menuBNG.getItems().add(ecofinItem);

        MenuItem moeursItem = new MenuItem("Moeurs");
        openIcon = new Image(getClass().getResourceAsStream("/pictures/moeurs.jpg"));
        openView = new ImageView(openIcon);
        openView.setFitWidth(menuItemImageSize);
        openView.setFitHeight(menuItemImageSize);
        moeursItem.setGraphic(openView);
        menuBNG.getItems().add(moeursItem);

        MenuItem stupsItem = new MenuItem("Stups");
        openIcon = new Image(getClass().getResourceAsStream("/pictures/stups.png"));
        openView = new ImageView(openIcon);
        openView.setFitWidth(menuItemImageSize);
        openView.setFitHeight(menuItemImageSize);
        stupsItem.setGraphic(openView);
        menuBNG.getItems().add(stupsItem);

        MenuItem volsItem = new MenuItem("Vols");
        openIcon = new Image(getClass().getResourceAsStream("/pictures/vols.png"));
        openView = new ImageView(openIcon);
        openView.setFitWidth(menuItemImageSize);
        openView.setFitHeight(menuItemImageSize);
        volsItem.setGraphic(openView);
        menuBNG.getItems().add(volsItem);

        MenuItem numberTrack = new MenuItem("Historique du numéro");
        contextMenu.getItems().add(numberTrack);
        numberTrack.setOnAction(this::numberTrack_Click);

        MenuItem idTrack = new MenuItem("Historique de l'identité");
        contextMenu.getItems().add(idTrack);
        idTrack.setOnAction(this::identityTrack_click);

        this.tableView.setContextMenu(contextMenu);

        //endregion

        //region ToolBar

        this.buttonExcelExport.setText("");
        Image exportIcon = new Image(getClass().getResourceAsStream("/pictures/Excel_export.png"));
        ImageView exportView = new ImageView(exportIcon);
        exportView.setFitWidth(20);
        exportView.setFitHeight(20);
        this.buttonExcelExport.setGraphic(exportView);
        this.buttonExcelExport.setStyle("");
        Tooltip tooltipExcelExport = new Tooltip("Exporter pour Excel (CTRL+E)");
        tooltipExcelExport.getStyleClass().add("toolBarTooltips");
        this.buttonExcelExport.setTooltip(tooltipExcelExport);
        this.buttonExcelExport.setOnAction(this::buttonExcelExport_click);
        final KeyCodeCombination keyCodeExcel = new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_ANY);
        this.scene.setOnKeyPressed(event-> {
            if(keyCodeExcel.match(event))
                this.buttonExcelExport_click(new ActionEvent());
        });


        this.buttonWordExport.setText("");
        ImageView wordImageView = new ImageView(new Image(getClass().getResourceAsStream("/pictures/Word_export.png")));
        wordImageView.setFitHeight(20);
        wordImageView.setFitWidth(20);
        this.buttonWordExport.setGraphic(wordImageView);
        this.buttonWordExport.setStyle("");
        Tooltip tooltipWordExport = new Tooltip("Générer des convocations (CTRL+I)");
        tooltipWordExport.getStyleClass().add("toolBarTooltips");
        this.buttonWordExport.setTooltip(tooltipWordExport);
        this.buttonWordExport.setOnAction(this::buttonWordExport_click);
        final KeyCodeCombination keyCodeWord = new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_ANY);
        this.scene.setOnKeyPressed(event-> {
            if(keyCodeWord.match(event))
                this.buttonWordExport_click(new ActionEvent());
        });
        this.buttonWordExport.setDisable(true);

        //endregion

        this.checkBoxCheckAll.setOnAction(this::ckeckAllCheckBox_change_selected);

        this.progressBar.setVisible(false);

    }


    private void initializeColumns() {

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //region Values Factories (détermine les valeurs à afficher)

        this.tableColumnNumber.setCellValueFactory(cellData -> cellData.getValue().getPhoneNumberProperty());
        this.tableColumnName.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        this.tableColumnFirsName.setCellValueFactory(cellData -> cellData.getValue().getFirstNameProperty());
        this.tableColumnDateDeNaissance.setCellValueFactory(cellData -> cellData.getValue().getDateDeNaissanceProperty());
        this.tableColumnKnown.setCellValueFactory(cellData -> cellData.getValue().getKnownForProperty());
        this.tableColumnLastOccurence.setCellValueFactory(cellData -> cellData.getValue().getLastOccurenceProperty());
        this.tableColumnChecked.setCellValueFactory(new PropertyValueFactory<>("selected"));

        //endregion

        //region Cell Factories (détermine comment les valeurs sont affichées

        this.tableColumnChecked.setCellFactory(CheckBoxTableCell.forTableColumn(this.tableColumnChecked));

        this.tableColumnChecked.setCellFactory(CheckBoxTableCell.forTableColumn(this.tableColumnChecked));

        this.tableColumnNumber.setCellFactory(NumberComponentTableCellFactory.forTableColumn(tableView,this.comboBoxDisplay.getSelectionModel().getSelectedIndex() == 1));

        this.tableColumnKnown.setCellFactory(column ->new KnowComponentTableCellFactory<>());

        //endregion

        //region propriétés des colonnes

        this.tableColumnChecked.setSortable(false);
        this.tableColumnDateDeNaissance.setSortable(false);
        this.tableColumnKnown.setSortable(false);
        this.tableColumnLastOccurence.setSortable(false);

        //endregion

        //region Events

        this.tableView.getSortOrder().addListener((Observable o) -> columnSorting_Change());
        final KeyCodeCombination keyCodeCopy = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);
        this.tableView.setOnKeyPressed(event -> {if(keyCodeCopy.match(event))copySelectionToClipboard(this.tableView);});
        this.tableView.setOnMouseClicked(this::tableView_Mouse_Click);

        //endregion


    }

    //endregion

    /**
        Nécessaire car sinon ne dégroupe pas.
     */
    private void updateItems(){

        ObservableList<Record> items;
        boolean grouped = this.comboBoxDisplay.getSelectionModel().getSelectedIndex() == 0;
        if(grouped)
            items = FXCollections.observableArrayList(this.records.getGroupedRecords());
        else
            items = FXCollections.observableArrayList(records.getRecords());

        FilteredList<Record> filteredList = new FilteredList<Record>(items, r->true);

        this.textFieldFiltering.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredList.setPredicate(record -> {
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if(record.getNameProperty().getValue().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                else{
                    if(record.getFirstNameProperty().getValue().toLowerCase().contains(lowerCaseFilter))
                        return true;
                    else{
                        return record.getPhoneNumber().toString().contains(lowerCaseFilter);
                    }
                }
            });
        });

        SortedList<Record> sortedList = new SortedList<>(filteredList);

        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        this.tableColumnName.setSortType(TableColumn.SortType.ASCENDING);
        this.tableView.setItems(sortedList);
    }

    //region EventHandlers

    private void button_click() {


        final KeyCodeCombination keyCodeCopy = new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_ANY);

        ArrayList<String> phoneNumbers = new ArrayList<>();

        if(!VFactory.getDbo().isConnected())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Absence de connexion");
            alert.setContentText("Vous devez vous connecter à une base de données pour effectuer des recherches");
            alert.showAndWait();
            return;
        }

        //region vérifie s'il y a du contenu à chercher

        if(this.textArea.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Absence d'élément à recherhcer");
            alert.setContentText("Veuillez au moins renseigner une identité ou un numéro de téléphone");
            alert.showAndWait();
            return;
        }

        //endregion

        //region vérification du contenu et de la taille de chaque élément

        boolean typeError = false;
        String[] elements = this.textArea.getText().split("\n");
        ArrayList<String> shortElements = new ArrayList<>();

        for(String element : elements) {
            if(element.isEmpty())
                continue;
            if(Helper.isNumeric(element)) {
                phoneNumbers.add(element);
                if (element.length() < 5)
                    shortElements.add(element);
            }
            else{
                typeError = true;
            }
        }
        if(typeError){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Les éléments doivent être des numéros de téléphone ou une partie d'un numéro");
            alert.setContentText("Veuillez n'entrer que des chiffres");
            alert.showAndWait();
            return;
        }

        //endregion

        //region vérification de la taille de l'élément

        if(shortElements.size() > 0 && !VFactory.DEBUG_NUMBER_SEARCH_FORM){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Éléments trop courts");
            alert.setHeaderText("Des éléments renseignés sont courts. Ceci risque de générer un nombre important de résultats. Voulez-vous continuer?");
            String liste = "Éléments impliqués: \n";
            for(String element : shortElements){
                liste = liste.concat( element + "\n");
            }
            alert.setContentText(liste);

            ButtonType oui = new ButtonType("Oui");

            alert.getButtonTypes().clear();
            alert.getButtonTypes().addAll(new ButtonType("Oui"),new ButtonType("Non"));


            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent()) {
                if (result.get().getText().equals("Non"))
                    return;
            }

        }

        //endregion

        this.records.clear();
        this.checkBoxCheckAll.setSelected(false);

        if(phoneNumbers.size() > 0)
            this.records.addAll(PhoneNumber.getIdentities(phoneNumbers));

        this.updateItems();

    }

    private void buttonExcelExport_click(ActionEvent event){

        //region Vérification des données

        RecordCollection selectedRecords = new RecordCollection();

        //Stoque les objets sélectionnés dans une liste
        for(Record r : this.tableView.getItems()){
            if(r.isSelected()) {
                selectedRecords.add(r);
            }
        }

        if(selectedRecords.size() ==0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucun élément sélectionné");
            alert.setContentText("Veuillez sélectionner au moins un élément à exporter");
            alert.showAndWait();
            return;
        }

        //endregion

        //region Affichage de la scene de parametrage de l'export

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ExcelExportFormForNumberSearchController.class.getResource("ExcelExportFormForNumberSearch.fxml"));
        AnchorPane scene = null;
        try{
            scene = (AnchorPane)loader.load();
        }
        catch (IOException e){
            e.printStackTrace();
            return;
        }
        Stage stage = new Stage();
        stage.setIconified(false);
        stage.setResizable(false);

        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Exporter pour Excel..." + selectedRecords.size() + "élément(s) à exporter");
        stage.setScene(new Scene(scene, 400,270));
        ExcelExportFormForNumberSearchController controller = loader.getController();
        controller.setStage(stage);
        stage.showAndWait();

        if(controller.getResult() == ButtonType.CANCEL)
            return;

        //endregion

        //region Définition du fichier cible

        DateTime now = DateTime.Now();

        String fileName = "export_" + now.getYear() + "-" + Helper.AddZerro(now.getMonth()) + "-" + Helper.AddZerro(now.getDay()) + " - " + Helper.AddZerro(now.getHour()) + "." + Helper.AddZerro(now.getMinute()) + "h.xls";

        FileChooser fileChooser =new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("XLS files (*.xls)", "*.txt"));
        fileChooser.setInitialFileName(fileName);
        Settings settings = VFactory.getSettings();
        if(settings == null || settings.getExportersDefaultDirectory().equals(""))
            fileChooser.setInitialDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
        else
            fileChooser.setInitialDirectory(new File(settings.getExportersDefaultDirectory()));
        File file = fileChooser.showSaveDialog(this.stage);
        if(file == null)
            return;

        //endregion

        this.progressBar.setVisible(true);
        this.progressBar.setProgress(0);

        ArrayList<String> columns = new ArrayList<>();

        if(controller.getNumero()) columns.add("number");
        if(controller.getNom()) columns.add("name");
        if(controller.getPrenom()) columns.add("firstName");
        if(controller.getDateDeNaissance()) columns.add("dateOfBirth");
        if(controller.getAge()) columns.add("age");
        if(controller.getLastOccurence()) columns.add("lastOccurence");
        if(controller.getKnownFor()) columns.add("knownFor");
        if(controller.getAdress()) columns.add("adress");
        if(controller.getDateDeConvocation()) columns.add("dateDeConvocation");
        if(controller.getSource()) columns.add("source");

        NumberSearchFormExcelExporter exporter = new NumberSearchFormExcelExporter(fileName, selectedRecords, columns);

        Workbook workbook = exporter.createWorkBookSync();

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            System.out.println("File " + fileName + " written correctly");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void buttonWordExport_click(ActionEvent event) {

        //region Vérifications des données

        Settings settings = VFactory.getSettings();

        RecordCollection selectedRecords = new RecordCollection();

        //Stoque les objets sélectionnés dans une liste
        for(Record r : this.tableView.getItems()){
            if(r.isSelected()) {
                selectedRecords.add(r);
            }
        }

        if(selectedRecords.size() ==0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucun élément sélectionné");
            alert.setContentText("Veuillez sélectionner au moins un élément à exporter");
            alert.showAndWait();
            return;
        }

        //endregion

        //region Affichage de la scene de parametrage de l'export

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(WordExportFormController.class.getResource("WordExportForm.fxml"));
        AnchorPane scene = null;
        try{
            scene = (AnchorPane)loader.load();
        }
        catch (IOException e){
            e.printStackTrace();
            return;
        }
        Stage stage = new Stage();
        stage.setIconified(false);
        stage.setTitle("Exporter pour Word..." + selectedRecords.size() + " élément(s) à exporter");
        stage.setScene(new Scene(scene, 1150,270));
        WordExportFormController controller = loader.getController();
        controller.setStage(stage);
        controller.setRecords(selectedRecords);
        stage.showAndWait();


        if(controller.getResult() == ButtonType.CANCEL)
            return;

        ConvocationExporter exporter = new ConvocationExporter(selectedRecords,controller.getDateTimes());
        exporter.Export();

        //endregion

    }

    private void comboBoxDisplay_change(ActionEvent event){
        this.updateItems();
    }

    private void comboBoxFilter_change(ActionEvent event) {

        switch (this.comboBoxFilter.getSelectionModel().getSelectedIndex()){
            case 0: this.records.setFilter(""); break;
            case 1: this.records.setFilter("U"); break;
            case 2: this.records.setFilter("C"); break;
            case 3: this.records.setFilter("E"); break;
            case 4: this.records.setFilter("M"); break;
            case 5: this.records.setFilter("S"); break;
            case 6: this.records.setFilter("V"); break;
        }
        this.updateItems();
    }

    private void columnSorting_Change() {

        if(this.tableView.getSortOrder().size() == 0) {
            return;
        }

        @SuppressWarnings("unchecked")
        TableColumn<Record,String> sortedColumn = (TableColumn<Record, String>) this.tableView.getSortOrder().get(0);

        if(sortedColumn != this.tableColumnNumber)
            this.comboBoxDisplay.getSelectionModel().select(1);

        this.updateItems();

    }

    private void ckeckAllCheckBox_change_selected(ActionEvent event){

        for(Record r : this.tableView.getItems())
            r.setSelected(this.checkBoxCheckAll.isSelected());
    }

    public void copySelectionToClipboard(final TableView<?> table) {

        final Set<Integer> rows = new TreeSet<>();

        for (final TablePosition<?,?> tablePosition : table.getSelectionModel().getSelectedCells()) {
            rows.add(tablePosition.getRow());
        }

        final StringBuilder strb = new StringBuilder();

        boolean firstRow = true;

        for (final Integer row : rows) {
            if (!firstRow) {
                strb.append('\n');
            }
            firstRow = false;
            boolean firstCol = true;
            for (final TableColumn<?, ?> column : table.getColumns()) {
                if(column.equals(tableColumnChecked))
                    continue;

                if (!firstCol) {
                    strb.append('\t');
                }
                firstCol = false;
                final Object cellData = column.getCellData(row);
                if(column.equals(tableColumnKnown)){
                    if(cellData.toString().equals(""))
                        strb.append("");
                    else {
                        String value = cellData.toString();

                        boolean firstValue = true;

                        if(value.contains("C")){
                            strb.append("Crimes");
                            firstValue =false;
                        }

                        if(value.contains("E")){
                            if (!firstValue)
                                strb.append(' ');
                            strb.append("Ecofin");
                            firstValue =false;
                        }

                        if(value.contains("M")){
                            if (!firstValue)
                                strb.append(' ');
                            strb.append("Moeurs");
                            firstValue =false;
                        }

                        if(value.contains("S")){
                            if (!firstValue)
                                strb.append(' ');
                            strb.append("Stups");
                            firstValue =false;
                        }

                        if(value.contains("V")){
                            if (!firstValue)
                                strb.append(' ');
                            strb.append("Vols");
                            firstValue =false;
                        }

                    }
                }
                else
                    strb.append(cellData == null ? "" : cellData.toString());
            }
        }
        final ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(strb.toString());
        Clipboard.getSystemClipboard().setContent(clipboardContent);
    }

    private void numberTrack_Click(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(NumberTrackFormController.class.getResource("NumberTrackForm.fxml"));
        AnchorPane scene = null;

        try {
            scene = (AnchorPane) loader.load();
        }
        catch (IOException e){
            e.printStackTrace();
            return;
        }
        Stage numberTrackForm = new Stage();
        numberTrackForm.setTitle("Historique du numéro");
        numberTrackForm.setScene(new Scene(scene, 700,500));
        NumberTrackFormController controller = loader.getController();
        TablePosition<?,?> tablePosition = this.tableView.getSelectionModel().getSelectedCells().get(0);
        Record record = this.tableView.getItems().get(tablePosition.getRow());
        controller.setStage(numberTrackForm);
        controller.setPhoneNumber(record.getPhoneNumber());
        numberTrackForm.show();
    }

    private void identityTrack_click(ActionEvent event){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(IdentityTrackFormController.class.getResource("IdentityTrackForm.fxml"));
        AnchorPane scene = null;

        try {
            scene = (AnchorPane) loader.load();
        }
        catch (IOException e){
            e.printStackTrace();
            return;
        }
        Stage idTrackForm = new Stage();
        idTrackForm.setTitle("Historique du numéro");
        idTrackForm.setScene(new Scene(scene, 700,500));
        IdentityTrackFormController controller = loader.getController();
        TablePosition<?,?> tablePosition = this.tableView.getSelectionModel().getSelectedCells().get(0);
        Record record = this.tableView.getItems().get(tablePosition.getRow());
        controller.setStage(idTrackForm);
        controller.setIdentity(record.getIdentity());
        idTrackForm.show();
    }

    private void tableView_Mouse_Click(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() < 2)
            return;
        if(tableView.getSelectionModel().getSelectedCells().size() == 0)
            return;

        TablePosition<?,?> tablePosition = tableView.getSelectionModel().getSelectedCells().get(0);
        int row = tablePosition.getRow();
        int col = tablePosition.getColumn();
        TableColumn<?,?> tableColumn = tablePosition.getTableColumn();
        Record record = this.tableView.getItems().get(row);

        if(col == 1)
            this.numberTrack_Click(new ActionEvent());
        if(col == 2 || col == 3)
            this.identityTrack_click(new ActionEvent());
    }

    //endregion

    //region Accesseurs

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    //endregion
}

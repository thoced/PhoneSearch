package view.NumberSearchForm;

import Factories.Cells.*;
import Factories.VFactory;
import com.Vickx.Biblix.Date.DateTime;
import com.Vickx.Biblix.Helper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.*;
import model.Exporters.ConvocationExporter;
import model.Exporters.NumberSearchFormExcelExporter;
import org.apache.poi.ss.usermodel.Workbook;
import view.ExportersForms.ExcelExportFormForNumberSearch.ExcelExportFormForNumberSearchController;
import view.ExportersForms.WordExportForm.WordExportFormController;
import view.TrackingForms.IdentityrTrackForm.IdentityTrackFormController;
import view.TrackingForms.NumberTrackForm.NumberTrackFormController;

import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class NumberSearchFormController {

    //region FXML binds

    //region TreeTableView

    @FXML
    TreeTableView<Record> treeTableView;

    @FXML
    TreeTableColumn<Record,Boolean> treeTableColumnChecked;

    @FXML
    TreeTableColumn<Record,String> treeTableColumnNumber;

    @FXML
    TreeTableColumn<Record, String> treeTableColumnName;

    @FXML
    TreeTableColumn<Record,String> treeTableColumnFirstName;

    @FXML
    TreeTableColumn<Record,String> treeTableColumnDateOfBirth;
    @FXML
    TreeTableColumn<Record,String> treeTableColumnKnownFor;
    @FXML
    TreeTableColumn<Record,String> treeTableColumnLastOccurence;

    //endregion

    //region Components

    @FXML
    ComboBox<String> comboBoxFilter;

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

    //endregion

    //region membres

    RecordCollection records = new RecordCollection();

    Stage stage;

    int totalItems = 0;

    //endregion

    //region Initialisations

    @FXML
    private void initialize() {

        this.initializeComponents();
        this.initializeTreeTableView();
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
        this.comboBoxFilter.setOnAction(event -> this.updateItems());

        //endregion

        //region Divers

        this.button.setOnAction(actionEvent -> button_click());
        this.textFieldFiltering.textProperty().addListener((event) -> this.updateItems());
        this.resultLabel.setText("aucun élément");

        //endregion

        //region ContextMenu pour TableView

        int menuItemImageSize = 20;

        ContextMenu contextMenu = new ContextMenu();

        MenuItem menuItemCopier= new MenuItem("Copier");
        contextMenu.getItems().add(menuItemCopier);
        menuItemCopier.setOnAction(event -> this.copySelectionToClipboard(this.treeTableView));

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

        this.treeTableView.setContextMenu(contextMenu);

        //endregion

        //region ToolBar

        //region Excel

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

        this.buttonExcelExport.setDisable(!VFactory.Enable_NumSearch_Excel_Export);

        //endregion

        //region Word

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
        this.buttonWordExport.setDisable(!VFactory.Enable_NumSearch_Word_Export);

        //endregion

        //endregion

        this.checkBoxCheckAll.setOnAction(this::ckeckAllCheckBox_change_selected);
        this.treeTableView.setOnMouseClicked(this::treeTableViewMouseClick);

        this.progressBar.setVisible(false);

        final KeyCodeCombination keyCodeCopy = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);
        this.treeTableView.setOnKeyPressed(event -> {if(keyCodeCopy.match(event))copySelectionToClipboard(this.treeTableView);});

    }

    private void initializeTreeTableView() {

        this.treeTableView.setEditable(true);
        this.treeTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TreeItem<Record> rootNode = new TreeItem<>(new Record(new Identity("root","root",DateTime.Now()),new PhoneNumber("")));
        this.treeTableView.setRoot(rootNode);
        this.treeTableView.setShowRoot(false);

        this.treeTableColumnChecked.setCellFactory(CheckBoxTreeTableCell.forTreeTableColumn(this.treeTableColumnChecked));
        this.treeTableColumnChecked.setEditable(true);
        this.treeTableColumnChecked.setCellValueFactory((TreeTableColumn.CellDataFeatures<Record,Boolean> p) -> p.getValue().getValue().getSelectedProperty());

        this.treeTableColumnNumber.setCellValueFactory((TreeTableColumn.CellDataFeatures<Record,String> p) -> p.getValue().getValue().getPhoneNumberProperty());
        this.treeTableColumnNumber.setCellFactory((TreeTableColumn<Record,String> param) -> new TreeTableCell<Record,String>(){
            @Override
            protected void updateItem(String phoneNumber, boolean empty){
                super.updateItem(phoneNumber,empty);
                TreeTableRow<Record> treeTableRow = getTreeTableRow();
                int index = treeTableRow.getIndex();
                if(index%2 == 0)
                    treeTableRow.setStyle("-fx-background-color:#bdd7ee;");
                else
                    treeTableRow.setStyle("-fx-background-color:#ddebf7;");
                setText(phoneNumber);
            }
        });

        this.treeTableColumnName.setCellValueFactory((TreeTableColumn.CellDataFeatures<Record,String> p) -> p.getValue().getValue().getNameProperty());

        this.treeTableColumnFirstName.setCellValueFactory((TreeTableColumn.CellDataFeatures<Record,String> p) -> p.getValue().getValue().getFirstNameProperty());

        this.treeTableColumnDateOfBirth.setCellValueFactory((TreeTableColumn.CellDataFeatures<Record,String> p) -> p.getValue().getValue().getDateDeNaissanceProperty());

        this.treeTableColumnKnownFor.setCellFactory(KnowComponentTableCellFactory.forTreeTableColumn());
        this.treeTableColumnKnownFor.setCellValueFactory((TreeTableColumn.CellDataFeatures<Record,String> p) ->  p.getValue().getValue().getKnownForProperty());

        this.treeTableColumnLastOccurence.setCellValueFactory((TreeTableColumn.CellDataFeatures<Record,String> p) -> p.getValue().getValue().getLastOccurenceProperty());

    }

    //endregion

    private void updateItems(){
        boolean nofilter = this.textFieldFiltering.getText().equals("") && this.comboBoxFilter.getSelectionModel().getSelectedIndex()==0;

        if(nofilter)
            this.totalItems = 0;

        TreeItem<Record> root = this.treeTableView.getRoot();
        root.getChildren().clear();

        TreeItem<Record> previous = null;

        String lowerCasFilter = this.textFieldFiltering.getText().toLowerCase();

        for(Record record : this.records){

            //Filtre sur le textField
            if (!record.getName().toLowerCase().contains(lowerCasFilter) && !record.getFirstName().toLowerCase().contains(lowerCasFilter) && !record.getPhoneNumber().toString().contains(lowerCasFilter))
                continue;

            //region Filtre sur "connu pour"

            if(this.comboBoxFilter.getSelectionModel().getSelectedIndex() !=0){
                switch (this.comboBoxFilter.getSelectionModel().getSelectedIndex()) {
                    case 1:
                        if(!record.getKnownFor().equals(""))
                            continue;
                        break;
                    case 2:
                        if(!record.getKnownFor().contains("C"))
                            continue;
                        break;
                    case 3:
                        if(!record.getKnownFor().contains("E"))
                            continue;
                        break;
                    case 4:
                        if(!record.getKnownFor().contains("M"))
                            continue;
                        break;
                    case 5:
                        if(!record.getKnownFor().contains("S"))
                            continue;
                        break;
                    case 6:
                        if(!record.getKnownFor().contains("V"))
                            continue;
                        break;
                }
            }

            //endregion

            TreeItem<Record> item = new TreeItem<>(record);

            if(previous == null || !previous.getValue().getPhoneNumber().equals(item.getValue().getPhoneNumber())) {
                root.getChildren().add(item);
                previous = item;
                if(nofilter)
                    this.totalItems++;
            }
            else
                previous.getChildren().add(item);
        }
        int resultToShow = this.treeTableView.getRoot().getChildren().size();

        if(resultToShow == this.records.size())
            this.resultLabel.setText( resultToShow + " résultat" + (resultToShow>1?"s":""));
        else
            this.resultLabel.setText(resultToShow + " résultat" + (resultToShow>1?"s":"") + " sur " + this.totalItems);

        //this.resultLabel.setText(this.resultLabel.getText() + " (" + this.records.size() + " identité" + (this.records.size()>1?"s":"") + ")");
    }

    //region EventHandlers

    private void button_click() {

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

        if(phoneNumbers.size() > 0) {
            this.records.addAll(PhoneNumber.getIdentities(phoneNumbers));
            this.records.sort();
        }

        this.updateItems();
    }

    private void buttonExcelExport_click(ActionEvent event){

        //region Vérification des données

        RecordCollection selectedRecords = new RecordCollection();

        //region Stoque les objets sélectionnés dans une liste

        for(TreeItem<Record> item: this.treeTableView.getRoot().getChildren()){

            if(item.getValue().isSelected())
                selectedRecords.add(item.getValue());

            if(item.getChildren().size() !=0){
                for(TreeItem<Record> child: item.getChildren()){
                    if(child.getValue().isSelected())
                        selectedRecords.add(child.getValue());
                }
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
        AnchorPane scene ;
        try{
            scene = loader.load();
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
            System.out.println("File \"" + fileName + "\" written correctly");
            if(settings!=null && settings.getOpenAppOnExport()) {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void buttonWordExport_click(ActionEvent event) {

        //region Vérifications des données

        Settings settings = VFactory.getSettings();

        RecordCollection selectedRecords = new RecordCollection();

        //Stoque les objets sélectionnés dans une liste

        for(TreeItem<Record> treeItem : this.treeTableView.getRoot().getChildren()){
            Record record = treeItem.getValue();
            if(record.isSelected())
                selectedRecords.add(record);

            if(treeItem.getChildren().size() > 0){
                for(TreeItem<Record> child : treeItem.getChildren()){
                    if(record.isSelected())
                        selectedRecords.add(record);
                }
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
        AnchorPane scene;
        try{
            scene = loader.load();
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

    private void ckeckAllCheckBox_change_selected(ActionEvent event){

        for(TreeItem<Record> treeItem : this.treeTableView.getRoot().getChildren()){

            treeItem.getValue().setSelected(this.checkBoxCheckAll.isSelected());

            if((treeItem.getChildren().size() != 0 && treeItem.isExpanded()) || !this.checkBoxCheckAll.isSelected()) {
                for(TreeItem<Record> child : treeItem.getChildren()){
                    child.getValue().setSelected(this.checkBoxCheckAll.isSelected());
                }
            }

        }
    }

    public void copySelectionToClipboard(final TreeTableView<?> table) {

        final Set<Integer> rows = new TreeSet<>();

        for (final TreeTablePosition<?,?> tablePosition : table.getSelectionModel().getSelectedCells()) {
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
            for (final TreeTableColumn<?, ?> column : table.getColumns()) {
                if(column.equals(treeTableColumnChecked))
                    continue;

                if (!firstCol) {
                    strb.append('\t');
                }
                firstCol = false;
                final Object cellData = column.getCellData(row);
                if(column.equals(treeTableColumnKnownFor)){
                    if(!cellData.toString().equals("")) {
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
        AnchorPane scene;

        try {
            scene = loader.load();
        }
        catch (IOException e){
            e.printStackTrace();
            return;
        }
        Stage numberTrackForm = new Stage();
        numberTrackForm.setTitle("Historique du numéro");
        numberTrackForm.setScene(new Scene(scene, 700,500));
        NumberTrackFormController controller = loader.getController();
        Record record = this.treeTableView.getSelectionModel().getSelectedItem().getValue();
        controller.setStage(numberTrackForm);
        controller.setPhoneNumber(record.getPhoneNumber());
        numberTrackForm.show();
    }

    private void treeTableViewMouseClick(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() < 2)
            return;
        if(treeTableView.getSelectionModel().getSelectedCells().size() == 0)
            return;

        TreeTablePosition<?,?> tablePosition = this.treeTableView.getSelectionModel().getSelectedCells().get(0);
        int col = tablePosition.getColumn();

        if(col == 1)
            this.numberTrack_Click(new ActionEvent());
        if(col == 2 || col == 3)
            this.identityTrack_click(new ActionEvent());
    }

    private void identityTrack_click(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(IdentityTrackFormController.class.getResource("IdentityTrackForm.fxml"));
        AnchorPane scene;

        try {
            scene = loader.load();
        }
        catch (IOException e){
            e.printStackTrace();
            return;
        }
        Stage idTrackForm = new Stage();
        idTrackForm.setTitle("Historique du numéro");
        idTrackForm.setScene(new Scene(scene, 700,500));
        IdentityTrackFormController controller = loader.getController();
        Record record = this.treeTableView.getSelectionModel().getSelectedItem().getValue();
        controller.setStage(idTrackForm);
        controller.setIdentity(record.getIdentity());
        idTrackForm.show();
    }

    //endregion

    //region Accesseurs

    public void setStage (Stage stage){
        this.stage = stage;
    }

    //endregion
}

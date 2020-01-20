package view.MainForm;

import Factories.VFactory;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Section;
import model.Dossier;
import view.NumberSearchForm.NumberSearchFormController;
import view.UtilitiesForms.SettingsForm.SettingsFormController;

import java.io.IOException;
import java.util.ArrayList;

public class MainFormController {

    @FXML
    TreeView<Dossier> treeView;

    @FXML
    MenuItem menuItemClose;

    @FXML
    MenuItem menuItemFindNumbers;

    @FXML
    MenuItem menuItemPreferences;

    @FXML
    Label connectionLabel;

    ObservableList<Dossier> dossiers;

    public MainFormController(){

    }

    @FXML
    private void initialize(){

        //Initialisation du TreeView
        
        ArrayList<Section> sections = Section.getSections();
        TreeItem<Dossier> root = new TreeItem<>(new Dossier("SLR - ZP Seraing/Neupré"));
        root.setExpanded(true);

        for(Section section : sections){
            TreeItem<Dossier> departementItem = new TreeItem<>(new Dossier(section.getName()));
            root.getChildren().add(departementItem);
            ArrayList<Dossier> dossiers = section.getDossiers();
            dossiers.forEach((d)->departementItem.getChildren().add(new TreeItem<>(d)));
        }

        menuItemClose.setOnAction(this::menuItemClose_action);
        menuItemFindNumbers.setOnAction(this::menuItemFindSomeNumbers_action);
        menuItemPreferences.setOnAction(this::menuItemPreferences_action);

        this.treeView.setRoot(root);
        this.updateConnectionLabel();

        if(VFactory.DEBUG_NUMBER_SEARCH_FORM)
            this.menuItemFindSomeNumbers_action(null);

    }

    private void menuItemClose_action(ActionEvent event){
        System.exit(1);
    }

    private void menuItemFindSomeNumbers_action(ActionEvent event){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(NumberSearchFormController.class.getResource("numberSearchForm.fxml"));
        AnchorPane scene;
        try {
            scene = loader.load();
        }
        catch (IOException e){

            e.printStackTrace();
            return;
        }

        NumberSearchFormController controller = loader.getController();

        Stage stage = new Stage();
        stage.setTitle("Rechercher des numéros");
        stage.setScene(new Scene(scene,1150,750));
        stage.setMinHeight(400);
        stage.setMinWidth(1060);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/pictures/islpSearch.png")));
        controller.setStage(stage);
        stage.showAndWait();
        if(VFactory.DEBUG_NUMBER_SEARCH_FORM)
            System.exit(1);
    }

    private void menuItemPreferences_action(ActionEvent event){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SettingsFormController.class.getResource("SettingsForm.fxml"));
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
        stage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Paramètres de l'application");
        stage.setScene(new Scene(scene, 540,600));
        SettingsFormController controller = loader.getController();
        controller.setStage(stage);
        stage.showAndWait();
        this.updateConnectionLabel();
    }

    private void updateConnectionLabel(){
        if(VFactory.getDbo().isConnected())
            this.connectionLabel.setText("Connecté");
        else
            this.connectionLabel.setText("Déconnecté");
    }

}

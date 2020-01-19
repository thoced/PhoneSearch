import Factories.VFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Settings;
import view.MainForm.MainFormController;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        Settings settings = VFactory.getSettings();
        VFactory.initDbo(settings.getHost(),settings.getSocket(),settings.getLogin(),settings.getPassWord(),settings.getBase());
        FXMLLoader loader = new FXMLLoader();
        //loader.setLocation(getClass().getResource("/view/MainForm/MainForm.fxml"));
        loader.setLocation(MainFormController.class.getResource("MainForm.fxml"));
        Parent root = loader.load();
        MainFormController controller = loader.getController();
        primaryStage.setTitle("PhoneSearch 1.0");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

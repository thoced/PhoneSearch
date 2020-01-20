package Factories.Cells;

import com.Vickx.Biblix.Images.ImageUtils;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import model.Record;
import sun.plugin2.jvm.RemoteJVMLauncher;

import java.util.ArrayList;


public final class KnowComponentTableCellFactory extends TreeTableCell<Record, String> {

    @Override
    protected void updateItem(String value, boolean empty){
        super.updateItem(value, empty);

        //Si l'objet est vide, ne rien afficher
        if(empty || value.trim().equals("")) {
            setText("");
            setGraphic(null);
            return;
        }

        ImageView image = null;

        String fileName01 = "/pictures/crimes_20.png";
        String fileName02 = "/pictures/ecofin_20.png";
        String fileName03 = "/pictures/moeurs_20.png";
        String fileName04 = "/pictures/stups_20.png";
        String fileName05 = "/pictures/vols_20.png";
        Image crimesImages = new Image(fileName01);
        Image ecofinImage = new Image(fileName02);
        Image moeursImage = new Image(fileName03);
        Image stupsImage = new Image(fileName04);
        Image volsImage = new Image(fileName05);

        ArrayList<Image> images = new ArrayList<>();

        if (value.contains("C")) {
            images.add(crimesImages);
            image = new ImageView(crimesImages);
        }
        if (value.contains("E")) {
            images.add(ecofinImage);
            image = new ImageView(ecofinImage);
        }
        if (value.contains("M")) {
            images.add(moeursImage);
            image = new ImageView(moeursImage);
        }
        if (value.contains("S")) {
            images.add(stupsImage);
            image = new ImageView(stupsImage);
        }
        if (value.contains("V")) {
            images.add(volsImage);
            image = new ImageView(volsImage);
        }

        if (images.size() > 1) {

            for (int i = 0; i < images.size()-1; i++)

                image = ImageUtils.appendRightBufferedImage(SwingFXUtils.fromFXImage(image.getImage(), null), SwingFXUtils.fromFXImage(images.get(i), null));

        }
        this.setStyle("-fx-alignment: BASELINE_CENTER;");
        setGraphic(image);
    }

    public static Callback<TreeTableColumn<Record,String>,TreeTableCell<Record,String>> forTreeTableColumn(){
        return (TreeTableColumn<Record,String> p)-> new KnowComponentTableCellFactory();
    }

}

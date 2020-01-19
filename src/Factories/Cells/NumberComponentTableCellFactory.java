package Factories.Cells;

import javafx.scene.control.*;
import javafx.util.Callback;
import model.Record;

public class NumberComponentTableCellFactory<T> extends TableCell<T,String> {

    private boolean grouped;
    private TableView<Record> tableView;

    public NumberComponentTableCellFactory(TableView<Record> tableView, boolean grouped){
        this.tableView = tableView;
        this.grouped = grouped;
    }

    @Override
    protected void updateItem(String item, boolean empty){

        super.updateItem(item,empty);

        @SuppressWarnings("unchecked")
        TableRow<Record> tableRow = getTableRow();

        setGraphic(null);

        //Si l'objet est vide, ne rien afficher
        if(empty) {
            tableRow.setStyle("");
            setText("");
            return;
        }

        int index = getIndex();


        //Si dégroupé, juste afficher
        if(grouped)
            tableRow.setStyle("");
            //Sinon traiter
        else {
            int nbItems = tableView.getItems().size();
            //Si l'index est dans les limites du tableau
            if(index >= 0 && index < nbItems) {
                Record previous = null;
                Record next = null;

                if(index < tableView.getItems().size()-1)
                    next = tableView.getItems().get(index+1);
                if(index != 0)
                    previous = tableView.getItems().get(index-1);


                if(previous != null && previous.getPhoneNumber().toString().equals(item)) {
                    if(selectedProperty().get())
                        setText(item);
                    else
                        setText("\t-->");
                    return;
                }


            }
        }
        setText(item);

    }

    /**
     * Fabrique statique.
     * @param tableView Le composant TableView dans lequel sera affiché la cellule
     * @param grouped Détermine si les numéros seront groupés par numéros similaires
     */
    public static <T> Callback<TableColumn<T, String>, TableCell<T, String>> forTableColumn(TableView<Record> tableView, boolean grouped) {
        return (TableColumn<T, String> tableColumn) -> new NumberComponentTableCellFactory<>(tableView, grouped);
    }
}

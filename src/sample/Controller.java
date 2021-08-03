package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sample.datamodel.Word;
import sample.datamodel.WordsData;

public class Controller {

    @FXML
    private TableView<Word> dictionaryTable;

    private WordsData data;

    public void initialize() {
        data = new WordsData();
        data.loadDict();
        dictionaryTable.setItems(data.getWords());
        dictionaryTable.setStyle("-fx-font-size:22px;");
        //dictionaryTable.setStyle("-fx-background-color: #1d1d1d;");

    }

    // Get TableColumnByName :)
    private <T> TableColumn<T, ?> getTableColumnByName(TableView<T> tableView, String name) {
        for (TableColumn<T, ?> col : tableView.getColumns())
            if (col.getText().equals(name)) return col ;
        return null ;
    }


}

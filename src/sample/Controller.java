package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import sample.datamodel.Word;
import sample.datamodel.WordsData;

public class Controller {

    @FXML
    private TableView<Word> dictionaryTable;

    private WordsData data;

    public void initialize(){
        data = new WordsData();
        data.loadDict();
        dictionaryTable.setItems(data.getWords());
        dictionaryTable.setStyle("-fx-font-size:22px;");
    }

}

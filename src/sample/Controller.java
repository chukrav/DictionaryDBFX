package sample;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import sample.datamodel.Word;
import sample.datamodel.WordsData;

import java.io.IOException;
import java.util.Optional;

public class Controller {

    @FXML
    private TableView<Word> dictionaryTable;

    @FXML
    private BorderPane mainBoardPane;

    private WordsData data;
    private Word selectedWord = new Word();

    public void initialize() {
        data = new WordsData();
        data.loadDict();
        dictionaryTable.setItems(data.getWords());
        dictionaryTable.setStyle("-fx-font-size:22px;");
        //dictionaryTable.setStyle("-fx-background-color: #1d1d1d;");

        setMouseDoubleClickResponse();

    }

    public void setMouseDoubleClickResponse() {
        dictionaryTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Word word = dictionaryTable.getSelectionModel().getSelectedItem();
                System.out.println(word.toString());
                selectedWord = new Word(word);
                showDialog();
            }
        });
    }

    // Get TableColumnByName :)
    private <T> TableColumn<T, ?> getTableColumnByName(TableView<T> tableView, String name) {
        for (TableColumn<T, ?> col : tableView.getColumns())
            if (col.getText().equals(name)) return col;
        return null;
    }


    private void bindColumnToRowNum(TableColumn<Word, Integer> indexColumn) {
        indexColumn.setCellFactory(col -> {
            TableCell<Word, Integer> indexCell = new TableCell<>();
            ReadOnlyObjectProperty<TableRow> rowProperty = indexCell.tableRowProperty();
            ObjectBinding<String> rowBinding = Bindings.createObjectBinding(() -> {
                TableRow<String> row = rowProperty.get();
                if (row != null) {
                    int rowIndex = row.getIndex();
                    if (rowIndex < row.getTableView().getItems().size()) {
                        return Integer.toString(rowIndex);
                    }
                }
                return null;
            }, rowProperty);
            indexCell.textProperty().bind(rowBinding);
            return indexCell;
        });

    }

    @FXML
    public void showDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBoardPane.getScene().getWindow());
        dialog.setTitle("The word you clicked");
     /*   try {
            Parent root = FXMLLoader.load(getClass()
                    .getResource("singleWordDialog.fxml"));
            dialog.getDialogPane().setContent(root);

        } catch (IOException e){
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
        }

      */
        dialog.setTitle("Edit Contact");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("singleWordDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        DialogSingleWord wcontroller = fxmlLoader.getController();
        wcontroller.presentWord(selectedWord);


        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Ok was pressed");
        }
    }

    @FXML
    public void handleClickTableView() {
        Word word = dictionaryTable.getSelectionModel().getSelectedItem();
        System.out.println(word.toString());
    }


}

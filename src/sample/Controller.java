package sample;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import sample.datamodel.Word;
import sample.datamodel.WordsData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Controller {

    @FXML
    private TableView<Word> dictionaryTable;

    @FXML
    private Menu dictionaries;

    @FXML
    private BorderPane mainBoardPane;

    @FXML
    Label dictionaryLabel;

    private WordsData data;
    private Word selectedWord = new Word();

    public void initialize() {
        setMyRawFactory();
        data = new WordsData();
//        data.loadDict();
        data.loadDictDB();
        dictionaryTable.setItems(data.getWords());
        dictionaryTable.setStyle("-fx-font-size:22px;");
        //dictionaryTable.setStyle("-fx-background-color: #1d1d1d;");

        setMouseDoubleClickResponse();
        addDictNamesMenuItems();
//        setMyRawFactory();

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

    public void setMyRawFactory() {
        dictionaryTable.setRowFactory(tv -> new TableRow<Word>() {
            @Override
            protected void updateItem(Word item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else if (item.getCounter() > 0) {
                    setStyle("-fx-background-color: tomato;");
                } else {
                    setStyle("");
                }
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


        dialog.setTitle("Edit Contact");
         */
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
            // got action if checked ! ------------------
        }
    }

    @FXML
    public void handleClickTableView() {
        Word word = dictionaryTable.getSelectionModel().getSelectedItem();
        System.out.println(word.toString());
    }

    public void addDictNamesMenuItems() {
        SplitMenuButton splitMenuButton = new SplitMenuButton();
        splitMenuButton.setPrefWidth(800);

        List<String> dictItems = new ArrayList<>();
        dictItems = DBDealer.getInstance().getDictNames();
        for (int i = 0; i < dictItems.size(); ++i) {
            String dictNam = dictItems.get(i);
            dictNam = dictNam.replaceAll("_", "-");
            MenuItem item = new MenuItem(dictNam);
            item.setOnAction(e -> {
//                System.out.println(item.getText() + " clicked.");
                String itemText = item.getText();
                itemText = itemText.replaceAll("-", "_");
                System.out.println(itemText + " clicked.");
                data.loadDictDB(itemText);
                String currDictName = DBDealer.getInstance().selectFullDictName(itemText);
                dictionaryLabel.setText(currDictName);
            });

            dictionaries.getItems().add(item);
        }

    }

    @FXML
    public void dlgNewWord() {
//        System.out.println("Dict Labels clicked ....");
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBoardPane.getScene().getWindow());
        dialog.setTitle("Add a new word to the collection");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("wordAddEdit.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
//        DialogSingleWord wcontroller = fxmlLoader.getController();
//        wcontroller.presentWord(selectedWord);


        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Ok was pressed");
            ControllerAddEdit addEdit = fxmlLoader.getController();
            addEdit.insertNewWord();
        }
    }

    @FXML
    public void runSQLStatement() {
        DBDealer.getInstance().setWorkDictionary("HP3_16_22");
        //DBDealer.getInstance().insertNewWordToStatus();
        Word word = new Word();
        word.setWord("chauffeur");
        word.setTranslate("fr. водитель, шофёр возить  (кого-л.)  на автомобиле");
        word.setTranscript("[''ʃəufə]");  // ***** !!!!!!!!!!!! ********** Here protected with another ', and in
        // DBDiller - with \' ------ !!!!!
        //word.setTranscript("[]");
        //DBDealer.getInstance().insertNewWordToDict(word);
        DBDealer.getInstance().updateWord(2840, word);
    }


}

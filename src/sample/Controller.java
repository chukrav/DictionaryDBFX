package sample;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
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

//        ObservableList<TableColumn<Word, ?>> columns = dictionaryTable.getColumns();
//        for (int i=0;i<columns.size();++i){
//            System.out.println(columns.get(i).getText());
//        }
//        TableColumn<Word,Integer> indexColumn = (TableColumn<Word, Integer>) dictionaryTable.getColumns().get(0);
//        bindColumnToRowNum(indexColumn);

    }

    // Get TableColumnByName :)
    private <T> TableColumn<T, ?> getTableColumnByName(TableView<T> tableView, String name) {
        for (TableColumn<T, ?> col : tableView.getColumns())
            if (col.getText().equals(name)) return col ;
        return null ;
    }


    private void bindColumnToRowNum(TableColumn<Word,Integer> indexColumn){
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








}

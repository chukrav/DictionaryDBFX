package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class DictLabels {

    @FXML
    private GridPane dictionaryGrid;

    public void initialize(){
        int SIZE = 22;
        Font labelFnt = Font.font("Amble CN", FontWeight.NORMAL, SIZE);
        Label lbl = new Label("VBox-1");
        lbl.setFont(labelFnt);
        dictionaryGrid.add(lbl,0,0,1,1);


        lbl = new Label("VBox-2");
        dictionaryGrid.add(lbl,0,1,1,1);
        lbl = new Label("VBox-3");
        dictionaryGrid.add(lbl,0,2,1,1);
        lbl = new Label("VBox-4");
        dictionaryGrid.add(lbl,1,0,1,1);
        lbl = new Label("VBox-5");
        dictionaryGrid.add(lbl,1,1,1,1);
        lbl = new Label("VBox-6");
        dictionaryGrid.add(lbl,1,2,1,1);
        lbl = new Label("VBox-7");
        dictionaryGrid.add(lbl,2,0,1,1);
        lbl = new Label("VBox-8");
        dictionaryGrid.add(lbl,2,1,1,1);
        lbl = new Label("VBox-9");
        dictionaryGrid.add(lbl,2,2,1,1);

    }




}

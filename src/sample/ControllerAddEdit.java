package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;



public class ControllerAddEdit {

    @FXML
    private GridPane dictionaryGrid;

    @FXML
    private TextField wordField;

    @FXML
    private TextArea translateField;

    @FXML
    private TextField transcriptField;

    public void initialize(){


    }

    @FXML
    public void onEnterWord(){
        String word = wordField.getText();
//        System.out.println("A new word entered: "+word);
        DBDealer dealer = DBDealer.getInstance();
        dealer.selectWordFromCollection(word);
        if (!dealer.getWord().getTranslate().isEmpty()){
            translateField.setText(dealer.getWord().getTranslate());
            transcriptField.setText(dealer.getWord().getTranscript());
        }
    }




}

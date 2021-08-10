package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import sample.datamodel.Word;

public class DialogSingleWord {

    @FXML
    private Label wordField;

    @FXML
    private Label translateField;

    @FXML
    private Label transcriptField;

    public void presentWord(Word word){
        wordField.setText(word.getWord());
        translateField.setText(word.getTranslate());
        transcriptField.setText(word.getTranscript());
    }



}

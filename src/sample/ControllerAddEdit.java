package sample;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import sample.datamodel.Word;


public class ControllerAddEdit {

    @FXML
    private GridPane dictionaryGrid;

    @FXML
    private TextField wordField;

    @FXML
    private TextArea translateField;

    @FXML
    private TextField transcriptField;

    @FXML
    private CheckBox chIshard;

    private boolean isWordExist = true;

    public void initialize() {


    }

    @FXML
    public void onEnterWord() {
        String word = wordField.getText();
//        System.out.println("A new word entered: "+word);
        DBDealer dealer = DBDealer.getInstance();
        dealer.selectWordFromCollection(word);
        if (!dealer.getWord().getTranslate().isEmpty()) {
            Word wordObj = dealer.getWord();
            isWordExist = true;
//            translateField.setText(dealer.getWord().getTranslate());
//            transcriptField.setText(dealer.getWord().getTranscript());
            translateField.setText(wordObj.getTranslate());
            transcriptField.setText(wordObj.getTranscript());
            chIshard.setSelected(wordObj.getRating() > 0);
        } else {
            isWordExist = false;
            translateField.setText("");
            transcriptField.setText("");
        }
    }

    public void insertNewWord() {
        if (!checkWordIncluded()) {
//            DBDealer.getInstance().setWorkDictionary("HP3_16_22");
//            DBDealer.getInstance().getWorkDictionary();
            Word word = new Word();
            String checkWord = wordField.getText();
            checkWord = checkWord.replaceAll("'","''");
            word.setWord(checkWord);
            checkWord = translateField.getText();
            checkWord = checkWord.replaceAll("'","''");
            word.setTranslate(checkWord);
            String transcriptTxt = transcriptField.getText();
            transcriptTxt = transcriptTxt.replaceAll("'","''");
            word.setTranscript(transcriptTxt);
            word.setRating(chIshard.isSelected() ? 1: 0);
            DBDealer.getInstance().insertNewWordToDict(word);
        } else {
            DBDealer.getInstance().insertNewWordToStatus();
        }
    }

    private boolean checkWordIncluded() {
        String word = wordField.getText();
        DBDealer dealer = DBDealer.getInstance();
        dealer.selectWordFromCollection(word);
        if (!dealer.getWord().getTranslate().isEmpty()) {
            isWordExist = true;
            return true;
        }
        return false;
    }


}

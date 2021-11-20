package sample;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import sample.datamodel.Word;
import sample.datamodel.WordsData;

public class DialogSingleWord {

    @FXML
    private Label wordField;

    @FXML
    private Label translateField;

    @FXML
    private Label transcriptField;

    @FXML
    private CheckBox chIshard;

    private boolean ratingInit = false;

    public void presentWord(Word word){
        wordField.setText(word.getWord());
        translateField.setText(word.getTranslate());
        transcriptField.setText(word.getTranscript());
        ratingInit = word.getRating()>0;
        chIshard.setSelected(ratingInit);
    }

    public boolean isHardStatus(){  // Is selected
        return chIshard.isSelected();
    }

    public void selectIshard(){
        chIshard.setSelected(true);
    }

    public void checkUpdateRating(WordsData data){
        boolean outState = chIshard.isSelected();
        if (ratingInit != outState){
            int rating = outState ? 1: 0;
            String wordRated = wordField.getText();
            DBDealer.getInstance().updateWordRating(wordRated,rating);
            System.out.println("Rating changed: " + wordRated+" "+rating);
            Word mword = data.findWord(wordRated);
            mword.setRating(rating);
        } else {
            System.out.println("Nothing changed ...");
        }
    }
}

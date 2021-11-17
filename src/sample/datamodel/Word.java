package sample.datamodel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by timbuchalka on 2/11/16.
 */
public class Word {
    private SimpleStringProperty word = new SimpleStringProperty("");
    private SimpleStringProperty translate = new SimpleStringProperty("");
    private SimpleStringProperty transcript = new SimpleStringProperty("");
    //private SimpleStringProperty notes = new SimpleStringProperty("");
    private SimpleIntegerProperty counter = new SimpleIntegerProperty();
    private SimpleIntegerProperty rating = new SimpleIntegerProperty();

    public Word() {
    }

    public Word(String word, String translate, String transcript) {
        this.word.set(word);
        this.translate.set(translate);
        this.transcript.set(transcript);
        this.counter.set(0);
        this.rating.set(0);

        //this.notes.set(notes);
    }

    public Word(String word, String translate, String transcript,int rating) {
        this.word.set(word);
        this.translate.set(translate);
        this.transcript.set(transcript);
        this.counter.set(0);
        this.rating.set(rating);

        //this.notes.set(notes);
    }

    public Word(Word word){
        this.word.set(word.getWord());
        this.setTranslate(word.getTranslate());
        this.setTranscript(word.getTranscript());
        this.counter.set(word.getCounter());
        this.rating.set(word.getRating());
    }

    public void setRating(int rating) {
        this.rating.set(rating);
    }

    public int getRating() {
        return rating.get();
    }

    public SimpleIntegerProperty ratingProperty() {
        return rating;
    }

    public String getWord() {
        return word.get();
    }

    public SimpleStringProperty wordProperty() {
        return word;
    }

    public String getTranslate() {
        return translate.get();
    }

    public SimpleStringProperty translateProperty() {
        return translate;
    }

    public String getTranscript() {
        return transcript.get();
    }

    public SimpleStringProperty transcriptProperty() {
        return transcript;
    }

    public void setWord(String word) {
        this.word.set(word);
    }

    public void setTranslate(String translate) {
        this.translate.set(translate);
    }

    public void setTranscript(String transcript) {
        this.transcript.set(transcript);
    }

    public int getCounter() {
        return counter.get();
    }

    public SimpleIntegerProperty counterProperty() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter.set(counter);
    }

    @Override
    public String toString() {
        return  word.get() +
                "\t" + translate.get() +
                " " + transcript.get()+" "+rating.get();
    }
}

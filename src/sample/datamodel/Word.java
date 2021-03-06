package sample.datamodel;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by timbuchalka on 2/11/16.
 */
public class Word {
    private SimpleStringProperty word = new SimpleStringProperty("");
    private SimpleStringProperty translate = new SimpleStringProperty("");
    private SimpleStringProperty transcript = new SimpleStringProperty("");
    //private SimpleStringProperty notes = new SimpleStringProperty("");

    public Word() {
    }

    public Word(String word, String translate, String transcript) {
        this.word.set(word);
        this.translate.set(translate);
        this.transcript.set(transcript);

        //this.notes.set(notes);
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

    @Override
    public String toString() {
        return  word +
                "\t" + translate +
                " " + transcript;
    }
}

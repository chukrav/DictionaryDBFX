package sample.writeHtml;

public class MRecord {
    public String getWord() {
        return word;
    }

    public String getTranslate() {
        return translate;
    }

    private String word;
    private String translate;

    public MRecord(String word, String translate) {
        this.word = word;
        this.translate = translate;
    }
}

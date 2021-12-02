package sample.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.DBDealer;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class WordsData {

    private static final String CONTACTS_FILE = "contacts.xml";
    private static final String DICT_FILE = "testDict.csv";

//    private static final String WORDSDATA = "contact";
//    private static final String WORD = "first_name";
//    private static final String TRANSLATE = "last_name";
//    private static final String TRANSCRIPT = "phone_number";
    //private static final String NOTES = "notes";

    private ObservableList<Word> words;
    private DBDealer dealer;
    private String currDictionaryName = "";

    public WordsData() {
        words = FXCollections.observableArrayList();
    }

    public ObservableList<Word> getWords() {
        return words;
    }

    public void addWord(Word item) {
        words.add(item);
    }

    public void deleteContact(Word item) {
        words.remove(item);
    }

    public void loadDict() {
        List<String> dictLines = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(DICT_FILE));
            while (scanner.hasNextLine()) {
                dictLines.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        if (!dictLines.isEmpty()) {
//            for (int i = 0; i < dictLines.size(); ++i) {
//                System.out.println(dictLines.get(i));
//            }
//        }

        if (!dictLines.isEmpty()) {
            for (int i = 0; i < dictLines.size(); ++i) {
                String line = dictLines.get(i);
                String[] parts = line.split("\t");
                String word = parts[1];

                String transcript = "[]";
                String transl = parts[2];
                int firstSq = parts[2].indexOf("[");
                if (firstSq > 0) {
                    transl = parts[2].substring(0, firstSq);
                    transcript = parts[2].substring(firstSq).trim();
                }
//                System.out.println(transcript);
                words.add(new Word(word, transl, transcript));
            }
            Collections.sort(words, (w1, w2) -> w1.getWord().compareTo(w2.getWord()));
            initCounters();
        }


    }

    public void loadDictDB() {
        try {
            dealer = DBDealer.getInstance();
            dealer.connectDB();

            ResultSet results = dealer.getResults();
            //String dictName = dealer.getWorkDictionary();
            updateWords(results);

        } catch (SQLException e) {
            System.out.println("SQL exeption: " + e.getMessage());
        }
    }

    public void loadDictDB(String dictName) {
        try {
            if (dealer == null) {
                dealer = DBDealer.getInstance();
                dealer.connectDB();
            }
            dealer.buildQuery(dictName);

            dealer.makeQuery();
            ResultSet results = dealer.getResults();
            updateWords(results);

        } catch (SQLException e) {
            System.out.println("SQL exeption: " + e.getMessage());
        }
    }

    public void refreshDictDB(){
        try {
            if (dealer == null) {
                dealer = DBDealer.getInstance();
                dealer.connectDB();
            }
            dealer.buildQuery(currDictionaryName);

            dealer.makeQuery();
            ResultSet results = dealer.getResults();
            updateWords(results);

        } catch (SQLException e) {
            System.out.println("SQL exeption: " + e.getMessage());
        }
    }

    public void showHardWords(){
        try {
            if (dealer == null) {
                dealer = DBDealer.getInstance();
                dealer.connectDB();
            }
            dealer.selectHardWords();
            ResultSet results = dealer.getResults();
            updateWords(results);

        } catch (SQLException e) {
            System.out.println("SQL exeption: " + e.getMessage());
        }

    }

    public void updateWords(ResultSet results) throws SQLException {
        words.clear();

        while (results.next()) {
            String word = results.getString("word");
            String transl = results.getString("translation");
            int rating = results.getInt("rating");
            String transcript = "[]";
            int firstSq = transl.indexOf("[");
            if (firstSq > 0) {
                transcript = transl.substring(firstSq).trim();
                transl = transl.substring(0, firstSq);
            }
            words.add(new Word(word, transl, transcript, rating));
        }

        Collections.sort(words, (w1, w2) -> w1.getWord().compareTo(w2.getWord()));
        initCounters();
    }

    private void initCounters() {
        for (int i = 0; i < words.size(); ++i) {
            words.get(i).setCounter(i);
        }
    }


    public void saveDict() throws IOException {
        PrintWriter fd = new PrintWriter(new FileWriter("dictionary.csv"));
        fd.println("word\ttranslation transcription");
        for (Word word : words) {
            fd.println(word.toString());
        }
        fd.close();
    }

    public Word findWord(String sword) {
        return words.stream().filter(mword -> sword.equals(mword.getWord())).findFirst().orElse(null);
    }

    public String getCurrDictionaryName() {
        return currDictionaryName;
    }

    public void setCurrDictionaryName(String currDictionaryName) {
        this.currDictionaryName = currDictionaryName;
    }

    public void createNewDict(String dictName, String dictTitle, String startDate){
//        dictName = 'HP3_22_27' <- only with underscores!
//        dictTitle = '3. Harry Potter and the prisoner of Azkaban, Parts 16-22' <- split by ,
        dealer.createNewDictionary(dictName, dictTitle, startDate);
        currDictionaryName = dictName;
        loadDictDB(dictName);
    }
}

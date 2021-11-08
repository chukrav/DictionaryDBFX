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
//            dealer = new DBDealer();
            dealer = DBDealer.getInstance();
            dealer.connectDB();

            ResultSet results = dealer.getResults();
            String dictName = dealer.getWorkDictionary();
            while (results.next()) {
                int counter = results.getInt(dictName);
                String word = results.getString("word");
                String transl = results.getString("translation");
                String transcript = "[]";
                int firstSq = transl.indexOf("[");
                if (firstSq > 0) {
                    transcript = transl.substring(firstSq).trim();
                    transl = transl.substring(0, firstSq);
                }
                words.add(new Word(word, transl, transcript));
            }


            Collections.sort(words, (w1, w2) -> w1.getWord().compareTo(w2.getWord()));
            initCounters();

            //dealer.getInstance().getDictNames();

        } catch (SQLException e) {
            System.out.println("SQL exeption: " + e.getMessage());
        }
    }

    public void loadDictDB(String dictName) {
        words.clear();
        try {
//            dealer = new DBDealer();
            if (dealer == null){
                dealer = DBDealer.getInstance();
                dealer.connectDB();
            }
            dealer.buildQuery(dictName);

            dealer.makeQuery();
            ResultSet results = dealer.getResults();
            while (results.next()) {
                String word = results.getString("word");
                String transl = results.getString("translation");
                String transcript = "[]";
                int firstSq = transl.indexOf("[");
                if (firstSq > 0) {
                    transcript = transl.substring(firstSq).trim();
                    transl = transl.substring(0, firstSq);
                }
                words.add(new Word(word, transl, transcript));
            }

            Collections.sort(words, (w1, w2) -> w1.getWord().compareTo(w2.getWord()));
            initCounters();

        } catch (SQLException e) {
            System.out.println("SQL exeption: " + e.getMessage());
        }


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




}

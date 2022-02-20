package sample;


import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WriteDictName {
    private String connectionStatementStr = "jdbc:sqlite:data/dictionaryNZ.db";
    //    private String connectionStatementStr = "jdbc:sqlite:C:/Users/Lesha/Udemy/JMaserClass/JavaFXStarts/DictionaryDBFX/data/dictionaryNZ.db";
    //    private String selectHP1p1_4Statement = "SELECT a.id, a.word,a.translation, b.HP1_1_4 FROM " +
//            "dictionary a, tableStatus b WHERE a.id = b.id AND b.HP1_1_4 > 0;";
//    SELECT a.word,a.translation FROM dictionary a, tableStatus b WHERE a.id = b.id AND b.IELTS_03 > 0;
    private String selectDictNames = "select name from pragma_table_info(\"tableStatus\");";
    private String selectAIDWordTranslateB = "SELECT a.word,a.translation"; // dict name "HP1_1_4"
    private String dictName = "IELTS_03";
    private String fromWhereAndCondition = " FROM dictionary a, tableStatus b WHERE a.id = b.id AND b."; // "b.HP1_1_4"
    private String conditioBody = " > 0;";
    private String selectHP1p1_4Statement = selectAIDWordTranslateB + fromWhereAndCondition
            + dictName + conditioBody;
    private ResultSet results;

    private static WriteDictName instance = null;
    private Connection conn;
    private Statement statement;
    //    private String workDictionary = "HP3_16_22";
    private String workDictionary = dictName;

    private List<MRecord> records = new ArrayList<>();
//    private Word word = new Word();

    private String shortDName = "";

    private WriteDictName() {

    }

    public static WriteDictName getInstance() {
        if (instance == null) {
            instance = new WriteDictName();
        }
        return instance;
    }

    public static void main(String[] args) {
        WriteDictName dictionary = WriteDictName.getInstance();
        dictionary.connectDB();

    }

    public void connectDB() {

        try {
            conn = DriverManager.getConnection(connectionStatementStr);
            statement = conn.createStatement();
//            statement.execute(selectHP1p1_4Statement);
            results = statement.executeQuery(selectHP1p1_4Statement);
            while (results.next()) {
//                System.out.println(results.getString("word") + ", "
//                        + results.getString("translation"));
                records.add(new MRecord(results.getString("word"),
                        results.getString("translation")));
            }

            makeQueryDictData();

            results.close();
            statement.close();
            conn.close();

            Collections.sort(records, (w1, w2) -> w1.getWord().compareTo(w2.getWord()));
            printDictionary();

        } catch (SQLException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }

    }

    public void makeQueryDictData() {
//        SELECT * FROM namesDicts WHERE shortName = 'IELTS_03';
        String mQuery = "SELECT * FROM namesDicts WHERE shortName = 'IELTS_03';";
        String longName = "";
        String parts = "";
        String startDate = "";
        try {
            results = statement.executeQuery(mQuery);
            longName = results.getString("longName");
            parts = results.getString("parts");
            startDate = results.getString("startDate");
            System.out.println(longName + ", " + parts+", " + startDate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void printDictionary() {
        for (MRecord item : records) {
            System.out.println(item.getWord() + ", "
                    + item.getTranslate());
        }
    }

    private class MRecord {
        public String getWord() {
            return word;
        }

        public String getTranslate() {
            return translate;
        }

        private String word;
        private String translate;

        MRecord(String word, String translate) {
            this.word = word;
            this.translate = translate;
        }
    }
}

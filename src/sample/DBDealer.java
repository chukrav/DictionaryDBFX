package sample;

import sample.datamodel.Word;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBDealer {

    private String connectionStatementStr = "jdbc:sqlite:data/dictionaryNZ.db";
    //    private String selectHP1p1_4Statement = "SELECT a.id, a.word,a.translation, b.HP1_1_4 FROM " +
//            "dictionary a, tableStatus b WHERE a.id = b.id AND b.HP1_1_4 > 0;";
    private String selectDictNames = "select name from pragma_table_info(\"tableStatus\");";
    private String selectAIDWordTranslateB = "SELECT a.id, a.word,a.translation, b."; // dict name "HP1_1_4"
    private String dictName = "HP1_1_4";
    private String fromWhereAndCondition = " FROM dictionary a, tableStatus b WHERE a.id = b.id AND b."; // "b.HP1_1_4"
    private String conditioBody = " > 0;";
    private String selectHP1p1_4Statement = selectAIDWordTranslateB + dictName + fromWhereAndCondition
            + dictName + conditioBody;
    private String selectDictionaryStatement = selectHP1p1_4Statement;
    //private String selectWordFromCollection =


    private ResultSet results;

    private static DBDealer instance = null;
    private Connection conn;
    private Statement statement;
    private String workDictionary = "HP3_16_22";

    private List<String> dictNames = new ArrayList<>();
    private Word word = new Word();
    private int wordID = -1;


    private DBDealer() {

    }

    public static DBDealer getInstance() {
        if (instance == null) {
            instance = new DBDealer();
        }
        return instance;
    }

    public void connectDB() {

        try {
            conn = DriverManager.getConnection(connectionStatementStr);
            statement = conn.createStatement();
//            statement.execute(selectHP1p1_4Statement);
            results = statement.executeQuery(selectHP1p1_4Statement);
//            while (results.next()) {
//                System.out.println(results.getInt("id") + ", "
//                        + results.getString("word") + ", "
//                        + results.getString("translation"));
//            }

            //results.close();
            //statement.close();
            //conn.close();

        } catch (SQLException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }

    }

    public void makeQuery() {
        try {
//            results.close();
            if (conn == null && statement == null) {
                conn = DriverManager.getConnection(connectionStatementStr);
                statement = conn.createStatement();
            }
            results = statement.executeQuery(selectDictionaryStatement);


        } catch (SQLException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }

    public ResultSet getResults() {
        // System.out.println(results.getFetchSize());
        return results;
    }

    public List<String> getDictNames() {
        try {
            dictNames = new ArrayList<>();
            results = statement.executeQuery(selectDictNames);
            while (results.next()) {
                String name = results.getString("name");
                //System.out.println(name);
                dictNames.add(name);
            }
            dictNames.remove(0);
            return dictNames;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void closeAll() {
        try {
            results.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void buildQuery(String dictStr) {
        selectDictionaryStatement = selectAIDWordTranslateB + dictStr + fromWhereAndCondition
                + dictStr + conditioBody;
//        System.out.println(selectDictionaryStatement);
    }

    public String getSelectDictionaryStatement() {
        return selectDictionaryStatement;
    }

    public String getWorkDictionary() {
        return workDictionary;
    }

    public void setWorkDictionary(String workDictionary) {
        this.workDictionary = workDictionary;
    }

    public void makeInsert() {
//        String string = String.format("A String %s %2d", aStringVar, anIntVar);
    }

    public void selectWordFromCollection(String inword) {
        //String string = String.format("A String %s %2d", aStringVar, anIntVar);
        //"SELECT a.id, a.word,a.translation, b.HP1_1_4 FROM " +
//            "dictionary a, tableStatus b WHERE a.id = b.id AND b.HP1_1_4 > 0;";
        try {
            String selectWordQuery = String.format("SELECT * from dictionary WHERE word = \"%s\";", inword);
            results = statement.executeQuery(selectWordQuery);
            String sword = "";
            String stranslate = "";
            String stranscript = "[]";
            wordID = -1;
            while (results.next()) {
                wordID = results.getInt("id");
                System.out.println(results.getString("word") + ", "
                        + results.getString("translation"));
                sword = results.getString("word");
                stranslate = results.getString("translation");
                int firstSq = stranslate.indexOf("[");
                if (firstSq > 0) {
                    stranscript = stranslate.substring(firstSq).trim();
                    stranslate = stranslate.substring(0, firstSq);
                }
            }
            word = new Word(sword, stranslate, stranscript);
            //results.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Word getWord() {
        return word;
    }

    public void insertNewWord(){
        String selectMaxID = "SELECT max(ID) FROM dictionary;";
        String insertInTableStatus = "";
        if (wordID < 0){
            try {
                results = statement.executeQuery(selectMaxID);
                wordID = results.getInt("id");
                String insertQueryString = String.format("INSERT INTO dictionary (ID, word,translation)"+
                        "VALUES(%d, '%s','%s');",wordID,word.getWord(),word.getTranslate()+", "+word.getTranscript());
                statement.execute(insertQueryString);



            } catch (SQLException e) {
                System.out.println("Can't insert a word.");
                e.printStackTrace();
            }
        } else {
            System.out.println("The word is already exists.");
        }

    }

    public int getDBSize(){
        String selectMaxID = "SELECT max(ID) FROM dictionary;";
        try {
            //results = statement.executeQuery(selectMAXID);
            results = statement.executeQuery(selectMaxID);
            wordID = results.getInt("max(ID)");
            return wordID;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  -1;
    }
}

//    INSERT INTO destination_table(id,name)
//    SELECT id, name
//    FROM source_table s
//        WHERE NOT EXISTS (
//        SELECT 1
//        FROM destination_table d
//        WHERE d.id = s.id
//        );

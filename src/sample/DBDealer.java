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
    private String selectAIDWordTranslateB = "SELECT a.id, a.word,a.translation,a.rating, b."; // dict name "HP1_1_4"
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
    //    private String workDictionary = "HP3_16_22";
    private String workDictionary = dictName;

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
            int rating = 0;
            while (results.next()) {
                wordID = results.getInt("id");
//                System.out.println(results.getString("word") + ", "
//                        + results.getString("translation"));
                rating = results.getInt("rating");
                sword = results.getString("word");
                stranslate = results.getString("translation");
                rating = results.getInt("rating");
                int firstSq = stranslate.indexOf("[");
                if (firstSq > 0) {
                    stranscript = stranslate.substring(firstSq).trim();
                    stranslate = stranslate.substring(0, firstSq);
                }
            }

            word = new Word(sword, stranslate, stranscript, rating);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Word getWord() {
        return word;
    }

    public void insertNewWordToDict() {
        if (wordID < 0) {
            try {
                wordID = getDBSize();
                ++wordID;
                String insertQueryString = String.format("INSERT INTO dictionary (ID, word,translation,rating)" +
                                "VALUES(%d, '%s','%s',%d);", wordID, word.getWord(), word.getTranslate() + ", " + word.getTranscript(),
                        word.getRating());

                statement.execute(insertQueryString);
//                String insertStatusTable = String.format("select name from pragma_table_info('tableStatus') WHERE name NOT like 'id';");
//                results = statement.executeQuery(insertStatusTable);
                insertNewWordToStatus();
            } catch (SQLException e) {
                System.out.println("Can't insert a word.");
                e.printStackTrace();
            }
        } else {
            System.out.println("The word is already exists.");
        }

    }

    public void insertNewWordToDict(Word newWord) {
        word = new Word(newWord);
        insertNewWordToDict();
    }

    public void insertNewWordToStatus() {
        int maxID = getDBSize();
        //maxID++;
        StringBuilder insertZeroRowColumns = new StringBuilder();
        StringBuilder insertZeroRowValues = new StringBuilder();
        insertZeroRowValues.append("VALUES(" + maxID);
        insertZeroRowColumns.append("INSERT INTO tableStatus(ID");
        List<String> list = new ArrayList<>();
        list = getDictNames();
        for (int i = 0; i < list.size(); ++i) {
            insertZeroRowColumns.append("," + list.get(i));
            insertZeroRowValues.append("," + 0);
            System.out.println(list.get(i));
        }
        insertZeroRowColumns.append(") " + insertZeroRowValues + ");");
        String updateStatusTable = String.format("UPDATE tableStatus SET %s = 1 WHERE id = %d;", workDictionary, maxID);
        //System.out.println(insertZeroRowColumns);
        try {
            statement.execute(insertZeroRowColumns.toString());
            statement.execute(updateStatusTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertNewWordToStatus(String dictName) {
        workDictionary = dictName;
        insertNewWordToStatus();
    }

    public void updateWordStatus(int id) {
        String updateWordStatus = String.format("UPDATE tableStatus SET %s = 1 WHERE id = %d", workDictionary, id);
        try {
            statement.execute(updateWordStatus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateWordStatus(String dictName, int id) {
        workDictionary = dictName;
        updateWordStatus(id);
    }

    //fr. водитель, шофёр возить  (кого-л.)  на автомобиле , [ʃəufə ]
//    public void updateWord(int id, Word updateWord) {
    public void updateWord(Word updateWord) {
        int id = updateWord.getCounter();
        String updateQuery = String.format("UPDATE dictionary SET word = \'%s\', translation = \'%s\', rating = %d WHERE id = %d;",
                updateWord.getWord(), updateWord.getTranslate() + updateWord.getTranscript(),
                updateWord.getRating(), id);
        try {
            statement.execute(updateQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //    public void updateRating(int id, Word updateWord){
    public void updateRating(Word updateWord) {
        int id = updateWord.getCounter();
        String updateQuery = String.format("UPDATE dictionary SET rating = %d WHERE id = %d;",
                updateWord.getRating(), id);
        try {
            statement.execute(updateQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int getDBSize() {
        String selectMaxID = "SELECT max(ID) FROM dictionary;";
        try {
            //results = statement.executeQuery(selectMAXID);
            results = statement.executeQuery(selectMaxID);
            wordID = results.getInt("max(ID)");
            return wordID;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String selectFullDictName(String shortName) {
        String selectLongName = String.format("SELECT longName, parts FROM namesDicts WHERE shortName = '%s';", shortName);
        try {
            //results = statement.executeQuery(selectMAXID);
            results = statement.executeQuery(selectLongName);
            String longName = results.getString("longName");
            String parts = results.getString("parts");
            return longName + "," + parts;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

// Insert if not exist ...
//   INSERT INTO memos(id,text)
//   SELECT 5, 'text to insert'
//   WHERE NOT EXISTS(SELECT 1 FROM memos WHERE id = 5 AND text = 'text to insert');

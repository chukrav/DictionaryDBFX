package sample;

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


    private ResultSet results;

    private static DBDealer instance = null;
    private Connection conn;
    private Statement statement;

    private List<String> dictNames = new ArrayList<>();


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

    public void makeQuery(){
        try {
            results.close();
            if (conn == null && statement == null){
                conn = DriverManager.getConnection(connectionStatementStr);
                statement = conn.createStatement();
            }
            results = statement.executeQuery(selectDictionaryStatement);


        }catch (SQLException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }

    public ResultSet getResults() {
       // System.out.println(results.getFetchSize());
        return results;
    }

    public List<String> getDictNames() {
        try {
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
    public void buildQuery(String dictStr){
        selectDictionaryStatement = selectAIDWordTranslateB + dictStr + fromWhereAndCondition
                + dictStr + conditioBody;
//        System.out.println(selectDictionaryStatement);
    }

    public String getSelectDictionaryStatement() {
        return selectDictionaryStatement;
    }
}

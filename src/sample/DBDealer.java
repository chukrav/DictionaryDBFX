package sample;

import java.sql.*;

public class DBDealer {

    private String connectionStatementStr = "jdbc:sqlite:data/dictionaryNZ.db";
    private String selectHP1p1_4Statement = "SELECT a.id, a.word,a.translation, b.HP1_1_4 FROM " +
            "dictionary a, tableStatus b WHERE a.id = b.id AND b.HP1_1_4 > 0;";
    private String selectDictNames = "select name from pragma_table_info(\"tableStatus\");";


    private ResultSet results;

    private static DBDealer instance = null;
    private Connection conn;
    private Statement statement;

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

    public ResultSet getResults() {
        return results;
    }

    public void getDictNames() {
        try {
            results = statement.executeQuery(selectDictNames);
            while (results.next()) {
                System.out.println(results.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
}

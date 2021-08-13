package sample;

import java.sql.*;

public class DBDealer {

    private String connectionStatementStr = "jdbc:sqlite:data/dictionaryNZ.db";
    private String selectHP1p1_4Statement = "SELECT a.id, a.word,a.translation, b.HP1_1_4 FROM " +
            "dictionary a, tableStatus b WHERE a.id = b.id AND b.HP1_1_4 > 0;";
    private ResultSet results;

    public void makeSelect() {

        try {
            Connection conn = DriverManager.getConnection(connectionStatementStr);
            Statement statement = conn.createStatement();
//            statement.execute(selectHP1p1_4Statement);
//            ResultSet
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
}

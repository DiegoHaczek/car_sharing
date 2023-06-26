package carsharing;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static carsharing.Main.DATABASE_URL;

public class DBClient {
    private String dbName;

    public DBClient(String dbName) {
        this.dbName = dbName;
    }

    public void run(String query) {
        try (Connection con = DriverManager.getConnection (DATABASE_URL + dbName);
             Statement statement = con.createStatement()
        ) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String,String> select(String query, String[] params){
        try (Connection con = DriverManager.getConnection (DATABASE_URL + dbName);
             Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery(query);
        ) {
            if(rs.next()){
               return Arrays.stream(params)
                       .collect(Collectors.toMap(
                               String::toString, GetValue(rs)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Function<String, String> GetValue(ResultSet rs) {
        return e -> {
            try {
                return rs.getString(e);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    public List<Map<String, String>> selectAll(String query){
        try (Connection con = DriverManager.getConnection (DATABASE_URL + dbName);
             Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery(query);
        ) {
            List <Map<String, String>> queryResult = new ArrayList<>();
            while (rs.next()){
                queryResult.add(Map.of("ID",rs.getString("ID"),
                        "NAME",rs.getString("NAME")));
            }
            return queryResult;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

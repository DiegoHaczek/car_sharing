package carsharing.Company;

import carsharing.Company.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static carsharing.Main.DATABASE_URL;

public class CompanyDAO {

    private String databaseName;

    public CompanyDAO(String fileName) {
     this.databaseName = fileName;
    }

    public void createTableCompany () throws SQLException {
        Connection conn = DriverManager.getConnection (DATABASE_URL + databaseName);
        conn.setAutoCommit(true);
        Statement st = conn.createStatement();
        st.executeUpdate("create table if not exists COMPANY(" +
                "ID int not null auto_increment," +
                "NAME varchar unique not null," +
                "primary key (id))");
        st.close();
        conn.close();
    }

    public void dropTableCompany () throws SQLException {
        Connection conn = DriverManager.getConnection (DATABASE_URL + databaseName);
        conn.setAutoCommit(true);
        Statement st = conn.createStatement();
        st.executeUpdate("drop table company");
        st.close();
        conn.close();
    }

    public void createCompany(String companyName) throws SQLException {
        Connection conn = DriverManager.getConnection (DATABASE_URL + databaseName);
        conn.setAutoCommit(true);
        Statement st = conn.createStatement();
        st.executeUpdate("insert into COMPANY (NAME) values ('"+companyName+"');");
        st.close();
        conn.close();
    }

    public Optional<List<Company>> listCompanies() throws SQLException {
        Connection conn = DriverManager.getConnection (DATABASE_URL + databaseName);
        conn.setAutoCommit(true);
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select * from COMPANY");
        List<Company> companyList = new ArrayList<>();
        while (rs.next()){
            companyList.add(new Company(rs.getInt("ID"),rs.getString("NAME")));
        }
        st.close();
        conn.close();
        return (companyList.isEmpty()) ? Optional.empty() : Optional.of(companyList);
    }

    public String getCompanyName(int companyId) throws SQLException {
        Connection conn = DriverManager.getConnection (DATABASE_URL + databaseName);
        conn.setAutoCommit(true);
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select NAME from COMPANY where ID = " + companyId + ";");
        String companyName = "";
        if (rs.next()){
            companyName = rs.getString("NAME");
        }
        st.close();
        conn.close();
        return companyName;
    }


}

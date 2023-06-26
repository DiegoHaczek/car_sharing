package carsharing.Company;

import carsharing.DBClient;

import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CompanyDAO {
    private final DBClient dbClient;
    private static final String INSERT = "insert into COMPANY (NAME) values ('%s');";
    private static final String SELECT_ALL = "select * from COMPANY";
    private static final String SELECT_NAME_BY_ID = "select NAME from COMPANY where ID = %d ;";
    private static final String DROP_TABLE = "drop table COMPANY";
    private static final String CREATE_TABLE = "create table if not exists COMPANY(" +
            "ID int not null auto_increment, " +
            "NAME varchar unique not null, " +
            "primary key (id))";

    public CompanyDAO(DBClient dbClient) {
     this.dbClient = dbClient;
    }

    public void createTableCompany (){
        dbClient.run(CREATE_TABLE);
    }

    public void dropTableCompany () {
        dbClient.run(DROP_TABLE);
    }

    public void createCompany(String companyName) throws SQLException {
        dbClient.run(String.format(INSERT,companyName));
    }

    public Optional<List<Company>> listCompanies() throws SQLException {
        List <Company> companyList = dbClient.selectAll(SELECT_ALL)
                .stream()
                .map(this::mapCompanyFromQueryResult)
                .toList();
        return (companyList.isEmpty()) ? Optional.empty() : Optional.of(companyList);
    }

    private Company mapCompanyFromQueryResult(Map<String,String> queryRow) {
        return new Company(Integer.parseInt(queryRow.get("ID")), queryRow.get("NAME"));
    }

    public String getCompanyName(int companyId) throws SQLException {
        String query = String.format(SELECT_NAME_BY_ID,companyId);
        return dbClient.select(query,new String[]{"NAME"}).get("NAME");
    }


}

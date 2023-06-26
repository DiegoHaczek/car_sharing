package carsharing.Customer;

import carsharing.Car.Car;
import carsharing.DBClient;

import java.sql.*;
import java.util.*;

import static carsharing.Main.DATABASE_URL;

public class CustomerDAO {
    private final DBClient dbClient;
    private final String CREATE_TABLE = "create table if not exists CUSTOMER(" +
            "ID int not null auto_increment, " +
            "NAME varchar unique not null, " +
            "RENTED_CAR_ID int default null, " +
            "primary key (ID), " +
            "foreign key (RENTED_CAR_ID) references CAR(ID))";
    private final String SELECT_ALL = "select * from CUSTOMER";
    private final String SELECT_RENTED_CAR = "SELECT CAR.NAME as NAME, COMPANY.NAME as COMPANY " +
            "from CUSTOMER " +
            "inner join CAR on CUSTOMER.RENTED_CAR_ID = CAR.ID " +
            "inner join COMPANY on CAR.COMPANY_ID = COMPANY.ID " +
            "where CUSTOMER.ID = %d ;";
    private final String SELECT_CAR_NAME_BY_ID = "select NAME from " +
            " (select *, ROW_NUMBER() over(order by id) as NEWID from CAR where COMPANY_ID = %d ) " +
            " where NEWID = %d ;";
    private final String SELECT_CAR_ID_BY_CUSTOMER = "select ifnull(RENTED_CAR_ID, 0) as RENTED_CAR_ID" +
            " from CUSTOMER where ID = %d ;";
    private final String DROP_TABLE = "drop table CUSTOMER";
    private final String INSERT = "insert into CUSTOMER (NAME) values ('%s');";
    private final String RENT_CAR = "update CUSTOMER set RENTED_CAR_ID = " +
            "(select ID from CAR where NAME = '%s') " +
            "where ID = %d ;";
    private final String RETURN_CAR = "update CUSTOMER set RENTED_CAR_ID = null where ID = %d;";
    public CustomerDAO(DBClient dbClient) {
        this.dbClient = dbClient;
    }

    public void createTableCustomer () throws SQLException {
        dbClient.run(CREATE_TABLE);
    }

    public void dropTableCustomer () throws SQLException {
        dbClient.run(DROP_TABLE);
    }

    public void createCustomer(String name) throws SQLException {
        dbClient.run(String.format(INSERT,name));
    }

    public void rentCar(int customerId, String carName) throws SQLException {
        dbClient.run(String.format(RENT_CAR,carName,customerId));
    }
    public void returnCar(int customerId) throws SQLException {
        dbClient.run(String.format(RETURN_CAR,customerId));
    }
    public Map<String,String> getRentedCar(int customerId) throws SQLException {
        return dbClient.select(String.format(SELECT_RENTED_CAR,customerId),
                new String[]{"COMPANY","NAME"});
    }

    public String getCarNameByMenuId(int companyId,int carId) throws SQLException {
        return dbClient.select(String.format(SELECT_CAR_NAME_BY_ID,companyId,carId),
                new String[]{"NAME"}).get("NAME");
    }

    public int checkIfHasRentedCar(int customerId) throws SQLException {
        return Integer.parseInt(
                dbClient.select(String.format(SELECT_CAR_ID_BY_CUSTOMER,customerId),
                new String[]{"RENTED_CAR_ID"}).get("RENTED_CAR_ID")
        );
    }

    public Optional<List<Customer>> listCustomers() throws SQLException {
        List <Customer> customerList = dbClient.selectAll(SELECT_ALL)
                .stream()
                .map(this::mapCustomerFromQueryResult)
                .toList();
        return (customerList.isEmpty()) ? Optional.empty() : Optional.of(customerList);
    }

    private Customer mapCustomerFromQueryResult(Map<String,String> queryRow) {
        return new Customer(Integer.parseInt(queryRow.get("ID")), queryRow.get("NAME"));
    }
}

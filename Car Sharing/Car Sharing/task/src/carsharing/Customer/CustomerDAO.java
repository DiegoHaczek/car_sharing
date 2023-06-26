package carsharing.Customer;

import carsharing.Car.Car;

import java.sql.*;
import java.util.*;

import static carsharing.Main.DATABASE_URL;

public class CustomerDAO {
    private String databaseName;
    public CustomerDAO(String fileName) {
        this.databaseName = fileName;
    }

    public void createTableCustomer () throws SQLException {
        Connection conn = DriverManager.getConnection (DATABASE_URL + databaseName);
        conn.setAutoCommit(true);
        Statement st = conn.createStatement();
        st.executeUpdate("create table if not exists CUSTOMER(" +
                "ID int not null auto_increment," +
                "NAME varchar unique not null," +
                "RENTED_CAR_ID int default null," +
                "primary key (ID)," +
                "foreign key (RENTED_CAR_ID) references CAR(ID))");
        st.close();
        conn.close();
    }

    public void dropTableCustomer () throws SQLException {
        Connection conn = DriverManager.getConnection (DATABASE_URL + databaseName);
        conn.setAutoCommit(true);
        Statement st = conn.createStatement();
        st.executeUpdate("drop table CUSTOMER");
        st.close();
        conn.close();
    }

    public void createCustomer(String name) throws SQLException {
        Connection conn = DriverManager.getConnection (DATABASE_URL + databaseName);
        conn.setAutoCommit(true);
        Statement st = conn.createStatement();
        st.executeUpdate("insert into CUSTOMER (NAME) " +
                "values ('" + name + "');");
        st.close();
        conn.close();
    }

    public Optional<List<Customer>> listCustomers() throws SQLException {
        Connection conn = DriverManager.getConnection (DATABASE_URL + databaseName);
        conn.setAutoCommit(true);
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select * from CUSTOMER ;");
        List<Customer> customerList = new ArrayList<>();
        while (rs.next()){
            customerList.add(new Customer(rs.getInt("ID"),rs.getString("NAME")));
        }
        st.close();
        conn.close();
        return (customerList.isEmpty()) ? Optional.empty() : Optional.of(customerList);
    }

    public String getCarNameByMenuId(int companyId,int carId) throws SQLException {
        Connection conn = DriverManager.getConnection (DATABASE_URL + databaseName);
        conn.setAutoCommit(true);
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select NAME from " +
                        "(select *, ROW_NUMBER() over(order by id) as NEWID from CAR where COMPANY_ID = "+companyId+" )" +
                        " where NEWID = "+carId+" ;");
        String carName = "";
        if(rs.next()){
            carName = rs.getString("NAME");
        }
        st.close();
        conn.close();
        return carName;
    }

    public void rentCar(int customerId, String carName) throws SQLException {
        Connection conn = DriverManager.getConnection (DATABASE_URL + databaseName);
        conn.setAutoCommit(true);
        Statement st = conn.createStatement();
        st.executeUpdate("update CUSTOMER set RENTED_CAR_ID = (select ID from CAR where NAME = '"+carName+"') where ID = "+customerId+" ;");
        st.close();
        conn.close();
    }

    public int checkIfHasRentedCar(int customerId) throws SQLException {
        Connection conn = DriverManager.getConnection (DATABASE_URL + databaseName);
        conn.setAutoCommit(true);
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select RENTED_CAR_ID from CUSTOMER where ID = "+customerId+" ;");
        int exists = 0;
        if(rs.next()){
            exists = rs.getInt("RENTED_CAR_ID");
        }
        st.close();
        conn.close();
        return exists;
    }

    public void returnCar(int customerId) throws SQLException {
        Connection conn = DriverManager.getConnection (DATABASE_URL + databaseName);
        conn.setAutoCommit(true);
        Statement st = conn.createStatement();
        st.executeUpdate("update CUSTOMER set RENTED_CAR_ID = null where ID = "+customerId+" ;");
        st.close();
        conn.close();
    }

    public Map<String,String> getRentedCar(int customerId) throws SQLException {
        Connection conn = DriverManager.getConnection (DATABASE_URL + databaseName);
        conn.setAutoCommit(true);
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT CAR.NAME as NAME, COMPANY.NAME as COMPANY " +
                "from CUSTOMER " +
                "inner join CAR on CUSTOMER.RENTED_CAR_ID = CAR.ID " +
                "inner join COMPANY on CAR.COMPANY_ID = COMPANY.ID ;");
       Map<String,String> carInfo = new HashMap<>();
        if(rs.next()){
            //carInfo = rs.getString("NAME");
            carInfo.put("company",rs.getString("COMPANY"));
            carInfo.put("car_name",rs.getString("NAME"));
        }
        st.close();
        conn.close();
        return carInfo;
    }
}

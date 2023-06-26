package carsharing.Car;

import carsharing.Company.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static carsharing.Main.DATABASE_URL;

public class CarDAO {

    private String databaseName;
    public CarDAO(String fileName) {
        this.databaseName = fileName;
    }

    public void createTableCar () throws SQLException {
        Connection conn = DriverManager.getConnection (DATABASE_URL + databaseName);
        conn.setAutoCommit(true);
        Statement st = conn.createStatement();
        st.executeUpdate("create table if not exists CAR(" +
                "ID int not null auto_increment," +
                "NAME varchar unique not null," +
                "COMPANY_ID int not null," +
                "primary key (ID)," +
                "foreign key (COMPANY_ID) references COMPANY(ID))");
        st.close();
        conn.close();
    }

    public void dropTableCar () throws SQLException {
        Connection conn = DriverManager.getConnection (DATABASE_URL + databaseName);
        conn.setAutoCommit(true);
        Statement st = conn.createStatement();
        st.executeUpdate("drop table car");
        st.close();
        conn.close();
    }

    public void createCar(String carName, int companyId) throws SQLException {
        Connection conn = DriverManager.getConnection (DATABASE_URL + databaseName);
        conn.setAutoCommit(true);
        Statement st = conn.createStatement();
        st.executeUpdate("insert into CAR (NAME,COMPANY_ID) " +
                "values ('" + carName + "'," + companyId + ");");
        st.close();
        conn.close();
    }

    public Optional<List<Car>> listCars(int companyId) throws SQLException {
        Connection conn = DriverManager.getConnection (DATABASE_URL + databaseName);
        conn.setAutoCommit(true);
        Statement st = conn.createStatement();
        //ResultSet rs = st.executeQuery("select * from CAR where COMPANY_ID = " + companyId + ";");
        ResultSet rs = st.executeQuery("select *, " +
                "ROW_NUMBER() over(order by id) as NEWID " +
                "from CAR " +
                "where COMPANY_ID = " + companyId + ";");
        List<Car> carList = new ArrayList<>();
        while (rs.next()){
            carList.add(new Car(rs.getInt("NEWID"),rs.getString("NAME")));
        }
        st.close();
        conn.close();
        return (carList.isEmpty()) ? Optional.empty() : Optional.of(carList);
    }

    public Optional<List<Car>> listAvailableCars(int companyId) throws SQLException {
        Connection conn = DriverManager.getConnection (DATABASE_URL + databaseName);
        conn.setAutoCommit(true);
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select *, ROW_NUMBER() over(order by CAR.ID) as NEWID from CAR " +
                "left join CUSTOMER on CAR.ID = CUSTOMER.RENTED_CAR_ID " +
                "where (CUSTOMER.NAME is null) and (CAR.COMPANY_ID = "+companyId+");");
        List<Car> carList = new ArrayList<>();
        while (rs.next()){
            carList.add(new Car(rs.getInt("NEWID"),rs.getString("NAME")));
        }
        st.close();
        conn.close();
        return (carList.isEmpty()) ? Optional.empty() : Optional.of(carList);
    }


}

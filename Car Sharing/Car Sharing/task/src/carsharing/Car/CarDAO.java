package carsharing.Car;

import carsharing.Company.Company;
import carsharing.DBClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static carsharing.Main.DATABASE_URL;

public class CarDAO {

    private final DBClient dbClient;
    private final String CREATE_TABLE = "create table if not exists CAR(" +
            " ID int not null auto_increment, " +
            " NAME varchar unique not null, " +
            " COMPANY_ID int not null, " +
            " primary key (ID), " +
            " foreign key (COMPANY_ID) references COMPANY(ID))";

    private final String SELECT_ALL = "select *, " +
            "ROW_NUMBER() over(order by id) as NEWID " +
            "from CAR " +
            "where COMPANY_ID = %d ;";

    private final String SELECT_AVAILABLE = "select CAR.NAME as NAME, ROW_NUMBER() over(order by CAR.ID) as ID from CAR " +
            "left join CUSTOMER on CAR.ID = CUSTOMER.RENTED_CAR_ID " +
            "where (CUSTOMER.NAME is null) and (CAR.COMPANY_ID = %d );";

    private final String DROP_TABLE = "drop table Car";

    private final String INSERT = "insert into CAR (NAME,COMPANY_ID) " +
            " values ('%s',%d);";

    public CarDAO(DBClient dbClient) {
        this.dbClient = dbClient;
    }

    public void createTableCar () {
        dbClient.run(CREATE_TABLE);
    }

    public void dropTableCar () {
        dbClient.run(DROP_TABLE);
    }

    public void createCar(String carName, int companyId) {
        dbClient.run(String.format(INSERT,carName,companyId));
    }

    public Optional<List<Car>> listCars(int companyId, String query) {
        List <Car> carList = dbClient.selectAll(String.format(query,companyId))
                .stream()
                .map(this::mapCarFromQueryResult)
                .toList();
        return (carList.isEmpty()) ? Optional.empty() : Optional.of(carList);
    }
    public Optional<List<Car>> listAllCars(int companyId) {
        return this.listCars(companyId,SELECT_ALL);
    }

    public Optional<List<Car>> listAvailableCars(int companyId) {
        return this.listCars(companyId,SELECT_AVAILABLE);
    }

    private Car mapCarFromQueryResult(Map<String,String> queryRow) {
        return new Car(Integer.parseInt(queryRow.get("ID")), queryRow.get("NAME"));
    }

}

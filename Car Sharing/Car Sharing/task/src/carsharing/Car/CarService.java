package carsharing.Car;


import carsharing.Company.CompanyDAO;
import carsharing.DBClient;

import java.sql.SQLException;
import java.util.Scanner;

public class CarService {
    private final CarDAO dao;
    private final CompanyDAO companyDao;

    public CarService(DBClient dbClient) {
        this.dao = new CarDAO(dbClient);
        this.companyDao = new CompanyDAO(dbClient);
    }

    public void createCarTable(){
        try{
            dao.createTableCar();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());;
        }
    }

    public void createCar(int companyId){
            System.out.println("Enter the car name:");
            Scanner scan = new Scanner(System.in);
            String carName = scan.nextLine();
            dao.createCar(carName,companyId);
            System.out.println("The car was created");
    }

    public void listCars(int companyId){
        try {
            dao.listAllCars(companyId).ifPresentOrElse(car -> car.forEach(System.out::println),
                    () -> System.out.println("The car list is empty!"));
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public int listAvailableCars(int companyId){
        try {
            dao.listAvailableCars(companyId).ifPresentOrElse(car -> car.forEach(System.out::println),
                    printNotAvailableMessage(companyId)
                    );
            return (int) dao.listAvailableCars(companyId).stream().count();
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    private Runnable printNotAvailableMessage(int companyId) {
        return () -> {
            try {
                System.out.println("No available cars in the " + companyDao.getCompanyName(companyId) + " company");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
    }

}

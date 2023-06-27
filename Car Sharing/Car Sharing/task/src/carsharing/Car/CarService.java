package carsharing.Car;


import carsharing.Company.CompanyDAO;
import carsharing.db.DBClient;

import java.util.Scanner;

public class CarService {
    private final CarDAO dao;
    private final CompanyDAO companyDao;

    public CarService(DBClient dbClient) {
        this.dao = new CarDAO(dbClient);
        this.companyDao = new CompanyDAO(dbClient);
    }

    public void createTable(){
            dao.createTableCar();
    }

    public void dropTable(){
        dao.dropTableCar();
    }

    public void createCar(int companyId){
            System.out.println("Enter the car name:");
            Scanner scan = new Scanner(System.in);
            String carName = scan.nextLine();
            dao.createCar(carName,companyId);
            System.out.println("The car was created");
    }

    public void listCars(int companyId){
            dao.listAllCars(companyId).ifPresentOrElse(car -> car.forEach(System.out::println),
                    () -> System.out.println("The car list is empty!"));
    }

    public int listAvailableCars(int companyId){
            dao.listAvailableCars(companyId).ifPresentOrElse(car -> car.forEach(System.out::println),
                    printNotAvailableMessage(companyId));
            return (int) dao.listAvailableCars(companyId).stream().count();
    }

    private Runnable printNotAvailableMessage(int companyId) {
        return () -> {
                System.out.println("No available cars in the " +
                        companyDao.getCompanyName(companyId) + " company");
        };
    }

}

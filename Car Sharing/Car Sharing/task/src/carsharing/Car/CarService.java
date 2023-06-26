package carsharing.Car;


import carsharing.Company.CompanyDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class CarService {
    private final CarDAO dao;
    private final CompanyDAO companyDao;

    public CarService(String fileName) {
        this.dao = new CarDAO(fileName);
        this.companyDao = new CompanyDAO(fileName);
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
        try {
            System.out.println("Enter the car name:");
            Scanner scan = new Scanner(System.in);
            String carName = scan.nextLine();
            dao.createCar(carName,companyId);
            System.out.println("The car was created");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void listCars(int companyId){
        try {
            dao.listCars(companyId).ifPresentOrElse(car -> car.forEach(System.out::println),
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

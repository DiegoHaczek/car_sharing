package carsharing;

import carsharing.Car.CarDAO;
import carsharing.Car.CarService;
import carsharing.Company.CompanyDAO;
import carsharing.Company.CompanyService;
import carsharing.Customer.Customer;
import carsharing.Customer.CustomerDAO;
import carsharing.Customer.CustomerService;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static String DATABASE_DRIVER = "org.h2.Driver";
    public static String DATABASE_URL = "jdbc:h2:./src/carsharing/db/";


    public static void main(String[] args){
        String fileName = (args.length>0) ? args[1] : "carsharing";
        CompanyService companyService = new CompanyService(fileName);
        CarService carService = new CarService(fileName);
        CustomerService customerService = new CustomerService(fileName);
        companyService.createCompanyTable();
        carService.createCarTable();
        customerService.createCustomerTable();
        Scanner scan = new Scanner(System.in);
        int menuOption = -1;
        while(menuOption != 0) {
            System.out.println("\n1. Log in as a manager\n2. Log in as a customer\n3. Create a customer\n0. Exit");
            menuOption = scan.nextInt();
            switch (menuOption) {
                case 1:
                    managerMenu(companyService, carService);
                    break;
                case 2:
                    customerMenu(companyService,carService,customerService);
                    break;
                case 3:
                    customerService.createCustomer();
                    break;
                case 0:
                    break;
            }
        }
    }

    private static void customerMenu(CompanyService companyService, CarService carService, CustomerService customerService) {
        System.out.println("\nChoose a customer:");

        int customerOption = -1;
        Scanner scan = new Scanner(System.in);
        while (customerOption != 0) {
            if(customerService.listCustomers()==0){
                break;
            }
            System.out.println("0. Back");
            customerOption = scan.nextInt();
            if (customerOption == 0){break;}
            int subMenuOption = -1;
            while (subMenuOption != 0) {
                System.out.println("\n1. Rent a car\n2. Return a rented car\n3. My rented card\n0. Back");
                subMenuOption = scan.nextInt();
                switch (subMenuOption) {
                    case 1 -> {if (customerService.checkIfHasRentedCar(customerOption)) {
                            System.out.println("You've already rented a car!");
                            break;}
                        System.out.println("\nChoose a company");
                        if (companyService.listCompanies() == 0) {
                            break;}
                        System.out.println("0. Back");
                        int companyOption = scan.nextInt();
                        if (companyOption != 0) {
                            if (carService.listAvailableCars(companyOption) == 0) {
                                break;}
                            System.out.println("0. Back");
                            int carOption = scan.nextInt();
                            if (carOption == 0) {
                                break;}
                            customerService.rentCar(customerOption, companyOption, carOption);
                        }
                    }
                    case 2 -> {
                        if (!customerService.checkIfHasRentedCar(customerOption)) {
                            System.out.println("You didn't rent a car!");
                            break;}
                        customerService.returnCar(customerOption);
                        System.out.println("You've returned a rented car!");
                    }
                    case 3 -> {
                        if (!customerService.checkIfHasRentedCar(customerOption)) {
                            System.out.println("You didn't rent a car!");
                            break;}
                        customerService.printRentedCar(customerOption);
                    }
                }

            }
        }
    }

    private static void managerMenu(CompanyService companyService, CarService carService) {
        int subMenuOption = -1;
        Scanner scan = new Scanner(System.in);
        while (subMenuOption != 0) {
            System.out.println("\n1. Company list\n2. Create a company\n0. Back");
            subMenuOption = scan.nextInt();
            switch (subMenuOption) {
                case 1:
                    System.out.println("Choose the company");
                    int companyOption = -1;
                    while (companyOption !=0){
                        if(companyService.listCompanies()==0){
                            break;
                        }
                        System.out.println("0. Back");
                        companyOption = scan.nextInt();
                        if (companyOption == 0){break;}
                        int carMenuOption = -1;
                        while (carMenuOption!=0){
                            System.out.println("\n'" + companyService.getCompanyName(companyOption) + "' company");
                            System.out.println("1. Car list\n2. Create a car\n0. Back");
                            carMenuOption = scan.nextInt();
                            switch (carMenuOption) {
                                case 1 -> carService.listCars(companyOption);
                                case 2 -> carService.creatCar(companyOption);
                                case 0 -> companyOption = 0;
                            }
                        }
                    }
                    break;
                case 2:
                    companyService.createCompany();
                    break;
                case 0:
                    break;
            }
        }
    }
}
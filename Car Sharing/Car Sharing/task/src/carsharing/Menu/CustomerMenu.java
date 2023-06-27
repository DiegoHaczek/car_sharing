package carsharing.Menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import static carsharing.Main.*;

public class CustomerMenu {

    private final Scanner scanner = new Scanner(System.in);
    public CustomerMenu(){}

    public void start() {
        System.out.println("\nChoose a customer:");
        int customerOption = -1;
        while (customerOption != 0) {
            if(customerService.listCustomers()==0){
                break;
            }
            System.out.println("0. Back");
            customerOption = getMenuOption(customerOption);
            if (customerOption == 0 || customerOption == -1){break;}
            int subMenuOption = -1;
            while (subMenuOption != 0) {
                subMenuOption = subMenu(customerOption);
            }
        }
    }

    private int subMenu(int customerOption) {
        int subMenuOption = 0;
        System.out.println("\n1. Rent a car\n2. Return a rented car\n3. My rented card\n0. Back");
        subMenuOption = getMenuOption(subMenuOption);
        switch (subMenuOption) {
            case 1 -> {
                if (customerService.checkIfHasRentedCar(customerOption)) {
                System.out.println("You've already rented a car!");
                break;}
                System.out.println("\nChoose a company");
                rentCarMenu(customerOption);
            }
            case 2 -> {
                if (hasRentedCar(customerOption)) break;
                customerService.returnCar(customerOption);
                System.out.println("You've returned a rented car!");
            }
            case 3 -> {
                if (hasRentedCar(customerOption)) break;
                customerService.printRentedCar(customerOption);
            }
        }
        return subMenuOption;
    }

    private void rentCarMenu(int customerOption) {
        if (companyService.listCompanies() == 0) {
            return;
        }
        System.out.println("0. Back");
        int companyOption = 0;
        companyOption = getMenuOption(companyOption);
        if (companyOption != 0) {
            if (carService.listAvailableCars(companyOption) == 0) {
                return;
            }
            System.out.println("0. Back");
            int carOption = 0;
            carOption = getMenuOption(carOption);
            if (carOption == 0) {
                return;
            }
            customerService.rentCar(customerOption, companyOption, carOption);
        }
    }

    private static boolean hasRentedCar(int customerOption) {
        if (!customerService.checkIfHasRentedCar(customerOption)) {
            System.out.println("You didn't rent a car!");
            return true;
        }
        return false;
    }

    public int getMenuOption(int menuOption) {
        try{
            menuOption = scanner.nextInt();}
        catch (InputMismatchException e){
            System.out.println("Please select a valid input");
            scanner.next();
        }
        return menuOption;
    }
}

package carsharing.Menu;

import java.util.Scanner;

import static carsharing.Main.carService;
import static carsharing.Main.companyService;

public class ManagerMenu {

    private final Scanner scanner = new Scanner(System.in);
    public ManagerMenu(){}

    public void start() {
        int subMenuOption = -1;
        while (subMenuOption != 0) {
            System.out.println("\n1. Company list\n2. Create a company\n0. Back");
            subMenuOption = scanner.nextInt();
            switch (subMenuOption) {
                case 1:
                    subMenu();
                    break;
                case 2:
                    companyService.createCompany();
                    break;
                case 0:
                    break;
            }
        }
    }

    private void subMenu() {
        System.out.println("Choose the company");
        int companyOption = -1;
        while (companyOption !=0){
            if(companyService.listCompanies()==0){
                break;
            }
            System.out.println("0. Back");
            companyOption = scanner.nextInt();
            if (companyOption == 0){break;}
            int carMenuOption = -1;
            while (carMenuOption!=0){
                System.out.println("\n'" + companyService.getCompanyName(companyOption) + "' company");
                System.out.println("1. Car list\n2. Create a car\n0. Back");
                carMenuOption = scanner.nextInt();
                companyOption = companySubMenu(companyOption, carMenuOption);
            }
        }
    }

    private static int companySubMenu(int companyOption, int carMenuOption) {
        switch (carMenuOption) {
            case 1 -> carService.listCars(companyOption);
            case 2 -> carService.createCar(companyOption);
            case 0 -> companyOption = 0;
        }
        return companyOption;
    }
}

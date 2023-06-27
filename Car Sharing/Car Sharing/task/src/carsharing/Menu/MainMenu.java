package carsharing.Menu;

import java.util.Scanner;

import static carsharing.Main.customerService;


public class MainMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final ManagerMenu managerMenu = new ManagerMenu();
    private final CustomerMenu customerMenu = new CustomerMenu();
    public MainMenu(){}

    public void start() {
        int menuOption = -1;
        while(menuOption != 0) {
            System.out.println("\n1. Log in as a manager\n2. Log in as a customer\n3. Create a customer\n0. Exit");
            menuOption = scanner.nextInt();
            switch (menuOption) {
                case 1:
                    managerMenu.start();
                    break;
                case 2:
                    customerMenu.start();
                    break;
                case 3:
                    customerService.createCustomer();
                    break;
                case 0:
                    break;
            }
        }
    }
}

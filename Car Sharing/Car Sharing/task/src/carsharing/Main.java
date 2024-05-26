package carsharing;

import carsharing.Car.CarService;
import carsharing.Company.CompanyService;
import carsharing.Customer.CustomerService;
import carsharing.Menu.MainMenu;
import carsharing.db.DBClient;

public class Main {

    public static String DATABASE_URL = "jdbc:h2:./src/carsharing/db/";
    private static final DBClient dbClient = new DBClient("carsharing");
    public final static CompanyService companyService = new CompanyService(dbClient);
    public final static CarService carService = new CarService(dbClient);
    public final static CustomerService customerService = new CustomerService(dbClient);

    static {
        companyService.createTable();
        carService.createTable();
        customerService.createTable();
        //asdasdasd
    }

    public static void main(String[] args) {
        if (args.length>0){
           dbClient.setDbName(args[1]);
        }
        MainMenu menu = new MainMenu();
        menu.start();
    }
}
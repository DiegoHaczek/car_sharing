package carsharing.Customer;

import carsharing.DBClient;
import java.util.Map;
import java.util.Scanner;

public class CustomerService {
    private final CustomerDAO dao;

    public CustomerService(DBClient dbClient) {
        this.dao = new CustomerDAO(dbClient);
    }

    public void createCustomerTable(){
        dao.createTableCustomer();
    }

    public void dropTable(){
        dao.dropTableCustomer();
    }

    public void createCustomer(){
            System.out.println("Enter the customer name:");
            Scanner scan = new Scanner(System.in);
            String customerName = scan.nextLine();
            dao.createCustomer(customerName);
            System.out.println("The customer was created");
    }

    public int listCustomers() {
        dao.listCustomers().ifPresentOrElse(customer -> customer.forEach(System.out::println),
                () -> System.out.println("The customer list is empty!"));
        return (int) dao.listCustomers().stream().count();
    }

    public void rentCar(int customerId, int companyId, int carId) {
           String carName = dao.getCarNameByMenuId(companyId, carId);
           dao.rentCar(customerId,carName);
           System.out.println("You rented '" + carName + "'");
    }

    public boolean checkIfHasRentedCar(int customerId) {
           return dao.checkIfHasRentedCar(customerId) != 0;
    }

    public void returnCar(int customerId) {
            dao.returnCar(customerId);
    }

    public void printRentedCar(int customerId) {
            Map<String,String> rentedCarInfo = dao.getRentedCar(customerId);
            System.out.println("\nYour rented car:\n"+rentedCarInfo.get("NAME")+
                    "\nCompany:\n" + rentedCarInfo.get("COMPANY") + "\n");
    }
}


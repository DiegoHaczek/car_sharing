package carsharing.Customer;

import carsharing.Company.CompanyDAO;
import carsharing.DBClient;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public class CustomerService {
    private final CustomerDAO dao;

    public CustomerService(DBClient dbClient) {
        this.dao = new CustomerDAO(dbClient);
    }

    public void createCustomerTable(){
        try{
            dao.createTableCustomer();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());;
        }
    }

    public void createCustomer(){
        try {
            System.out.println("Enter the customer name:");
            Scanner scan = new Scanner(System.in);
            String customerName = scan.nextLine();
            dao.createCustomer(customerName);
            System.out.println("The customer was created");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }


    public int listCustomers(){
        try {
            dao.listCustomers().ifPresentOrElse(customer -> customer.forEach(System.out::println),
                    () -> System.out.println("The customer list is empty!"));
            return (int) dao.listCustomers().stream().count();
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public void rentCar(int customerId, int companyId, int carId) {
       try {
           String carName = dao.getCarNameByMenuId(companyId, carId);
           dao.rentCar(customerId,carName);
           System.out.println("You rented '" + carName + "'");
       }catch (SQLException e){
           System.out.println(Arrays.toString(e.getStackTrace()));
           System.out.println(e.getMessage());
       }
    }

    public boolean checkIfHasRentedCar(int customerId) {
       try{
           return dao.checkIfHasRentedCar(customerId) != 0;
       }catch(SQLException e){
           System.out.println(Arrays.toString(e.getStackTrace()) +"\n"+ e.getMessage());
       }
        return false;
    }

    public void returnCar(int customerId) {
        try{
            dao.returnCar(customerId);
        }catch(SQLException e){
            System.out.println(Arrays.toString(e.getStackTrace()) +"\n"+ e.getMessage());
        }
    }

    public void printRentedCar(int customerId) {
        try{
            Map<String,String> rentedCarInfo = dao.getRentedCar(customerId);
            System.out.println("\nYour rented car:\n"+rentedCarInfo.get("NAME")+
                    "\nCompany:\n" + rentedCarInfo.get("COMPANY") + "\n");
        }catch(SQLException e){
            System.out.println(Arrays.toString(e.getStackTrace()) +"\n"+ e.getMessage());
        }
    }
}


package carsharing.Company;

import java.sql.SQLException;
import java.util.Scanner;

public class CompanyService {
    private final CompanyDAO dao;

    public CompanyService(String fileName) {
        this.dao = new CompanyDAO(fileName);
    }

    public void createCompanyTable(){
        try{
            dao.createTableCompany();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());;
        }
    }

    public void createCompany(){
        try {
            System.out.println("Enter the company name:");
            Scanner scan = new Scanner(System.in);
            String companyName = scan.nextLine();
            dao.createCompany(companyName);
            System.out.println("The company was created");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public String getCompanyName(int companyId){
       try{
           return dao.getCompanyName(companyId);
       }catch (SQLException e){
           System.out.println(e.getMessage());
       }
        return null;
    }

    public int listCompanies(){
        try {
            dao.listCompanies().ifPresentOrElse(company -> company.forEach(System.out::println),
                    () -> System.out.println("The company list is empty!"));
            return (int) dao.listCompanies().stream().count();
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return 0;
    }
}

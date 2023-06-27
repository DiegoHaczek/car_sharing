package carsharing.Company;

import carsharing.db.DBClient;

import java.util.Scanner;

public class CompanyService {
    private final CompanyDAO dao;
    public CompanyService(DBClient dbClient) {
        this.dao = new CompanyDAO(dbClient);
    }

    public void createTable(){
        dao.createTableCompany();
    }

    public void dropTable(){
        dao.dropTableCompany();
    }

    public void createCompany(){
            System.out.println("Enter the company name:");
            Scanner scan = new Scanner(System.in);
            String companyName = scan.nextLine();
            dao.createCompany(companyName);
            System.out.println("The company was created");
    }

    public String getCompanyName(int companyId){
           return dao.getCompanyName(companyId);
    }

    public int listCompanies(){
            dao.listCompanies().ifPresentOrElse(company -> company.forEach(System.out::println),
                    () -> System.out.println("The company list is empty!"));
            return (int) dao.listCompanies().stream().count();
    }
}

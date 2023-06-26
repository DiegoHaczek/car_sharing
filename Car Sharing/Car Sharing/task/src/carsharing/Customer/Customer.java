package carsharing.Customer;
public record Customer(int id, String name){
    @Override
    public String toString() {return id + ". " + name;}
}

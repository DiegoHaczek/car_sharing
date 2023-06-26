package carsharing.Company;

public record Company(int id, String name) {
    @Override
    public String toString() {
        return id + ". " + name;
    }
}

package carsharing.Car;

public record Car (int id, String name) {
    @Override
    public String toString() {
        return id + ". " + name;
    }
}

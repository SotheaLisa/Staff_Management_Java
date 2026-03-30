package Model;

public class Volunteer extends StaffMember {
    private double salary;

    public Volunteer(int id, String name, String address, double salary) {
        super(id, name, address);
        this.salary = salary;
    }

    public double getSalary()         { return salary; }
    public void   setSalary(double s) { this.salary = s; }

    @Override public double pay() { return salary; }
}
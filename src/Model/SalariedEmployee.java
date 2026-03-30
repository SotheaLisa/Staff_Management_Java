package Model;

public class SalariedEmployee extends StaffMember {
    private double salary, bonus;

    public SalariedEmployee(int id, String name, String address, double salary, double bonus) {
        super(id, name, address);
        this.salary = salary; this.bonus = bonus;
    }

    public double getSalary()         { return salary; }
    public void   setSalary(double s) { this.salary = s; }
    public double getBonus()          { return bonus; }
    public void   setBonus(double b)  { this.bonus = b; }

    @Override public double pay() { return salary + bonus; }
}
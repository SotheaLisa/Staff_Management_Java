package Model;

public class HourlySalaryEmployee extends StaffMember {
    private int    hoursWorked;
    private double rate;

    public HourlySalaryEmployee(int id, String name, String address, int hoursWorked, double rate) {
        super(id, name, address);
        this.hoursWorked = hoursWorked; this.rate = rate;
    }

    public int    getHoursWorked()        { return hoursWorked; }
    public void   setHoursWorked(int h)   { this.hoursWorked = h; }
    public double getRate()               { return rate; }
    public void   setRate(double r)       { this.rate = r; }

    @Override public double pay() { return hoursWorked * rate; }
}
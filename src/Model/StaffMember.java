package Model;

public abstract class StaffMember {
    protected int    id;
    protected String name;
    protected String address;

    public StaffMember(int id, String name, String address) {
        this.id = id; this.name = name; this.address = address;
    }

    public int    getId()      { return id; }
    public String getName()    { return name; }
    public String getAddress() { return address; }

    public void setName(String name)       { this.name = name; }
    public void setAddress(String address) { this.address = address; }

    public abstract double pay();
}
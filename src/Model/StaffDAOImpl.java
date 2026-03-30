package Model;

import Control.DBConnection;
import java.sql.*;
import java.util.*;

public class StaffDAOImpl implements StaffDao {

    private final Connection conn = DBConnection.get();

    // INSERT
    @Override
    public void insert(StaffMember s) {
        String sql = "INSERT INTO staff(type,name,address,salary,bonus,hours_worked,rate) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getClass().getSimpleName());
            ps.setString(2, s.getName());
            ps.setString(3, s.getAddress());

            if (s instanceof Volunteer v) {
                ps.setDouble(4, v.getSalary());
                ps.setNull(5, Types.DOUBLE);
                ps.setNull(6, Types.INTEGER);
                ps.setNull(7, Types.DOUBLE);
            } else if (s instanceof SalariedEmployee se) {
                ps.setDouble(4, se.getSalary());
                ps.setDouble(5, se.getBonus());
                ps.setNull(6, Types.INTEGER);
                ps.setNull(7, Types.DOUBLE);
            } else if (s instanceof HourlySalaryEmployee he) {
                ps.setNull(4, Types.DOUBLE);
                ps.setNull(5, Types.DOUBLE);
                ps.setInt(6, he.getHoursWorked());
                ps.setDouble(7, he.getRate());
            }

            ps.executeUpdate();
            System.out.println(" Inserted successfully!");
        } catch (SQLException e) { System.out.println("Insert failed: " + e.getMessage()); }
    }

    // SELECT ALL
    @Override
    public List<StaffMember> getAll() {
        List<StaffMember> list = new ArrayList<>();
        try (ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM staff ORDER BY id")) {
            while (rs.next()) list.add(toObject(rs));
        } catch (SQLException e) { System.out.println("Load failed: " + e.getMessage()); }
        return list;
    }

    // SELECT BY ID
    @Override
    public StaffMember getById(int id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM staff WHERE id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return toObject(rs);
        } catch (SQLException e) { System.out.println("Find failed: " + e.getMessage()); }
        return null;
    }

    // UPDATE
    @Override
    public void update(StaffMember s) {
        String sql = "UPDATE staff SET name=?,address=?,salary=?,bonus=?,hours_worked=?,rate=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getAddress());

            if (s instanceof Volunteer v) {
                ps.setDouble(3, v.getSalary());
                ps.setNull(4, Types.DOUBLE);
                ps.setNull(5, Types.INTEGER);
                ps.setNull(6, Types.DOUBLE);
            } else if (s instanceof SalariedEmployee se) {
                ps.setDouble(3, se.getSalary());
                ps.setDouble(4, se.getBonus());
                ps.setNull(5, Types.INTEGER);
                ps.setNull(6, Types.DOUBLE);
            } else if (s instanceof HourlySalaryEmployee he) {
                ps.setNull(3, Types.DOUBLE);
                ps.setNull(4, Types.DOUBLE);
                ps.setInt(5, he.getHoursWorked());
                ps.setDouble(6, he.getRate());
            }

            ps.setInt(7, s.getId());
            ps.executeUpdate();
            System.out.println("Updated!");
        } catch (SQLException e) { System.out.println("Update failed: " + e.getMessage()); }
    }

    // DELETE
    @Override
    public void delete(int id) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM staff WHERE id=?")) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Deleted!" : " ID not found.");
        } catch (SQLException e) { System.out.println("Delete failed: " + e.getMessage()); }
    }

    // NEXT ID (max + 1)
    @Override
    public int nextId() {
        return getAll().stream().mapToInt(StaffMember::getId).max().orElse(0) + 1;
    }

    // Convert DB row → Java object
    private StaffMember toObject(ResultSet rs) throws SQLException {
        int    id   = rs.getInt("id");
        String name = rs.getString("name");
        String addr = rs.getString("address");
        return switch (rs.getString("type")) {
            case "Volunteer"            -> new Volunteer(id, name, addr, rs.getDouble("salary"));
            case "SalariedEmployee"     -> new SalariedEmployee(id, name, addr, rs.getDouble("salary"), rs.getDouble("bonus"));
            case "HourlySalaryEmployee" -> new HourlySalaryEmployee(id, name, addr, rs.getInt("hours_worked"), rs.getDouble("rate"));
            default -> null;
        };
    }
}
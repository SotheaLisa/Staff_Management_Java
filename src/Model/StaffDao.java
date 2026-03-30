package Model;

import java.util.List;

public interface StaffDao {
    void             insert(StaffMember s);
    List<StaffMember> getAll();
    StaffMember      getById(int id);
    void             update(StaffMember s);
    void             delete(int id);
    int              nextId();
}
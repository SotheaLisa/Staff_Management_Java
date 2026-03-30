package Control;

import Model.StaffDao;
import Model.StaffDAOImpl;
import Model.StaffMember;
import java.util.*;

public class StaffController {

    private final StaffDao dao = new StaffDAOImpl();

    public void            add(StaffMember s)     { dao.insert(s); }
    public List<StaffMember> getAll()             { return dao.getAll(); }
    public Optional<StaffMember> findById(int id) { return Optional.ofNullable(dao.getById(id)); }
    public void            update(StaffMember s)  { dao.update(s); }
    public void            remove(int id)         { dao.delete(id); }
    public int             nextId()               { return dao.nextId(); }
}
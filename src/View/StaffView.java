package View;

import Control.StaffController;
import Model.*;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.Table;

import java.util.*;

public class StaffView {

    private final StaffController ctrl = new StaffController();
    private final Scanner sc = new Scanner(System.in);

    // ===== START =====
    public void start() {
        while (true) {
            printMenu();
            System.out.print("==> Enter option: ");
            switch (Validation.option(1, 5, sc)) {
                case 1 -> insert();
                case 2 -> display();
                case 3 -> update();
                case 4 -> remove();
                case 5 -> {
                    System.out.println("Goodbye!");
                    return;
                }
            }
        }
    }

    // ===== MENU =====
    private void printMenu() {
        Table t = new Table(1, BorderStyle.UNICODE_BOX);
        t.addCell(" Welcome to Staff Management System");
        t.addCell("  1. Insert Employee");
        t.addCell("  2. Display Employee");
        t.addCell("  3. Update Employee");
        t.addCell("  4. Remove Employee");
        t.addCell("  5. Exit");
        System.out.println(t.render());
    }

    // ===== INSERT =====
    private void insert() {
        Table t = new Table(4, BorderStyle.UNICODE_BOX);
        t.addCell("1. Volunteer");
        t.addCell("2. Salaried");
        t.addCell("3. Hourly");
        t.addCell("0. Back");
        System.out.println(t.render());

        System.out.print("Choose Type: ");
        int type = Validation.option(0, 3, sc);
        if (type == 0) return;

        String name = Validation.name(sc);
        String address = Validation.address(sc);
        int id = ctrl.nextId();

        switch (type) {
            case 1 -> ctrl.add(new Volunteer(id, name, address,
                    Validation.positiveDouble("Salary", sc)));

            case 2 -> ctrl.add(new SalariedEmployee(id, name, address,
                    Validation.positiveDouble("Salary", sc),
                    Validation.positiveDouble("Bonus", sc)));

            case 3 -> ctrl.add(new HourlySalaryEmployee(id, name, address,
                    Validation.positiveInt("Hours", sc),
                    Validation.positiveDouble("Rate", sc)));
        }
    }

    // ===== DISPLAY (5 rows per page) =====
    private void display() {
        List<StaffMember> list = ctrl.getAll();
        if (list.isEmpty()) {
            System.out.println("No data.");
            return;
        }

        int pageSize = 5;
        int totalPages = (int) Math.ceil((double) list.size() / pageSize);
        int page = 1;

        while (true) {
            int start = (page - 1) * pageSize;
            int end = Math.min(start + pageSize, list.size());

            Table t = new Table(9, BorderStyle.UNICODE_BOX);
            t.addCell("Type"); t.addCell("ID"); t.addCell("Name"); t.addCell("Address");
            t.addCell("Salary"); t.addCell("Bonus"); t.addCell("Hours"); t.addCell("Rate"); t.addCell("Pay");

            for (int i = start; i < end; i++) {
                StaffMember s = list.get(i);

                String sal = "---", bon = "---", hrs = "---", rat = "---";

                if (s instanceof Volunteer v) {
                    sal = "" + v.getSalary();
                } else if (s instanceof SalariedEmployee se) {
                    sal = "" + se.getSalary();
                    bon = "" + se.getBonus();
                } else if (s instanceof HourlySalaryEmployee h) {
                    hrs = "" + h.getHoursWorked();
                    rat = "" + h.getRate();
                }

                t.addCell(s.getClass().getSimpleName());
                t.addCell("" + s.getId());
                t.addCell(s.getName());
                t.addCell(s.getAddress());
                t.addCell(sal);
                t.addCell(bon);
                t.addCell(hrs);
                t.addCell(rat);
                t.addCell(String.format("%.1f", s.pay()));
            }

            System.out.println("Page " + page + "/" + totalPages);
            System.out.println(t.render());

            Table nav = new Table(3, BorderStyle.UNICODE_BOX);
            nav.addCell("[P] Prev");
            nav.addCell("[N] Next");
            nav.addCell("[0] Back");
            System.out.println(nav.render());

            System.out.print("Choice: ");
            String choice = sc.nextLine().trim().toUpperCase();

            switch (choice) {
                case "N" -> {
                    if (page < totalPages) page++;
                    else System.out.println("Last page!");
                }
                case "P" -> {
                    if (page > 1) page--;
                    else System.out.println("First page!");
                }
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // ===== UPDATE (input ID only) =====
    private void update() {
        int id = Validation.id(sc);

        Optional<StaffMember> found = ctrl.findById(id);
        if (found.isEmpty()) {
            System.out.println(" Not found!");
            return;
        }

        StaffMember emp = found.get();

        boolean go = true;
        while (go) {
            Table t = new Table(1, BorderStyle.UNICODE_BOX);
            t.addCell(" Update: " + emp.getClass().getSimpleName() + " — " + emp.getName());
            t.addCell("  1. Name");
            t.addCell("  2. Address");

            if (emp instanceof Volunteer) t.addCell("  3. Salary");

            if (emp instanceof SalariedEmployee) {
                t.addCell("  3. Salary");
                t.addCell("  4. Bonus");
            }

            if (emp instanceof HourlySalaryEmployee) {
                t.addCell("  3. Hours");
                t.addCell("  4. Rate");
            }

            t.addCell("  0. Save & Done");

            System.out.println(t.render());
            System.out.print("Choose: ");

            int max = (emp instanceof Volunteer) ? 3 : 4;

            switch (Validation.option(0, max, sc)) {
                case 0 -> go = false;
                case 1 -> emp.setName(Validation.name(sc));
                case 2 -> emp.setAddress(Validation.address(sc));
                case 3 -> {
                    if (emp instanceof Volunteer v)
                        v.setSalary(Validation.positiveDouble("Salary", sc));
                    else if (emp instanceof SalariedEmployee se)
                        se.setSalary(Validation.positiveDouble("Salary", sc));
                    else if (emp instanceof HourlySalaryEmployee he)
                        he.setHoursWorked(Validation.positiveInt("Hours", sc));
                }
                case 4 -> {
                    if (emp instanceof SalariedEmployee se)
                        se.setBonus(Validation.positiveDouble("Bonus", sc));
                    else if (emp instanceof HourlySalaryEmployee he)
                        he.setRate(Validation.positiveDouble("Rate", sc));
                }
            }
        }

        ctrl.update(emp);
    }

    // ===== REMOVE (input ID only) =====
    private void remove() {
        int id = Validation.id(sc);

        Optional<StaffMember> found = ctrl.findById(id);
        if (found.isEmpty()) {
            System.out.println("Not found!");
            return;
        }

        if (Validation.yesNo("Remove " + found.get().getName() + "?", sc)) {
            ctrl.remove(id);
            System.out.println("Removed successfully!");
        } else {
            System.out.println("Cancelled.");
        }
    }
}
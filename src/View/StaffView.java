package View;

import Control.StaffController;
import Model.*;
import Model.Validation;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.CellStyle.HorizontalAlign;
import org.nocrala.tools.texttablefmt.Table;

import java.util.*;

public class StaffView {

    private final StaffController ctrl = new StaffController();
    private final Scanner sc = new Scanner(System.in);

    private static final CellStyle CENTER = new CellStyle(HorizontalAlign.center);
    private static final CellStyle LEFT = new CellStyle(HorizontalAlign.left);

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
        t.addCell("  Welcome to Staff Management System  ", CENTER);
        t.addCell("  1. Insert Employee                  ", LEFT);
        t.addCell("  2. Display Employee                 ", LEFT);
        t.addCell("  3. Update Employee                  ", LEFT);
        t.addCell("  4. Remove Employee                  ", LEFT);
        t.addCell("  5. Exit                             ", LEFT);
        System.out.println(t.render());
    }

    // ===== INSERT =====
    private void insert() {

        Table typeTable = new Table(4, BorderStyle.UNICODE_BOX);
        typeTable.addCell(" 1. Volunteer ", CENTER);
        typeTable.addCell(" 2. Salaried Employee ", CENTER);
        typeTable.addCell(" 3. Hourly Employee ", CENTER);
        typeTable.addCell(" 0. Back ", CENTER);
        System.out.println(typeTable.render());

        System.out.print("Choose Type: ");
        int type = Validation.option(0, 3, sc);
        if (type == 0) return;

        int id = ctrl.nextId();
        System.out.println("Enter ID: " + id);

        String name = Validation.name(sc);
        String address = Validation.address(sc);

        switch (type) {

            case 1 -> {
                double salary = Validation.positiveDouble("Salary", sc);
                ctrl.add(new Volunteer(id, name, address, salary));

                printSummary(id, "Volunteer", name, address,
                        new String[]{"Salary", "Pay"},
                        new String[]{fmt(salary), fmt(salary)});
            }

            case 2 -> {
                double salary = Validation.positiveDouble("Salary", sc);
                double bonus = Validation.positiveDouble("Bonus ", sc);
                double pay = salary + bonus;

                System.out.println("  >> Payment (Salary + Bonus) = " + fmt(pay));

                ctrl.add(new SalariedEmployee(id, name, address, salary, bonus));

                printSummary(id, "SalariedEmployee", name, address,
                        new String[]{"Salary", "Bonus", "Pay"},
                        new String[]{fmt(salary), fmt(bonus), fmt(pay)});
            }

            case 3 -> {
                int hours = Validation.positiveInt("Hours", sc);
                double rate = Validation.positiveDouble("Rate  ", sc);
                double pay = hours * rate;

                System.out.println("  >> Payment (Hours x Rate) = " + fmt(pay));

                ctrl.add(new HourlySalaryEmployee(id, name, address, hours, rate));

                printSummary(id, "HourlySalaryEmployee", name, address,
                        new String[]{"Hours", "Rate", "Pay"},
                        new String[]{String.valueOf(hours), fmt(rate), fmt(pay)});
            }
        }
    }

    // ===== DISPLAY (UNCHANGED PAGINATION, 5 ROWS PER PAGE) =====
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

            t.addCell("Type", CENTER);
            t.addCell("ID", CENTER);
            t.addCell("Name", CENTER);
            t.addCell("Address", CENTER);
            t.addCell("Salary", CENTER);
            t.addCell("Bonus", CENTER);
            t.addCell("Hours", CENTER);
            t.addCell("Rate", CENTER);
            t.addCell("Pay", CENTER);

            for (int i = start; i < end; i++) {
                StaffMember s = list.get(i);
                String sal = "---", bon = "---", hrs = "---", rat = "---";

                if (s instanceof Volunteer v) sal = fmt(v.getSalary());
                else if (s instanceof SalariedEmployee se) {
                    sal = fmt(se.getSalary());
                    bon = fmt(se.getBonus());
                } else if (s instanceof HourlySalaryEmployee h) {
                    hrs = String.valueOf(h.getHoursWorked());
                    rat = fmt(h.getRate());
                }

                t.addCell(s.getClass().getSimpleName(), LEFT);
                t.addCell(String.valueOf(s.getId()), CENTER);
                t.addCell(s.getName(), LEFT);
                t.addCell(s.getAddress(), LEFT);
                t.addCell(sal, CENTER);
                t.addCell(bon, CENTER);
                t.addCell(hrs, CENTER);
                t.addCell(rat, CENTER);
                t.addCell(fmt(s.pay()), CENTER);
            }

            System.out.println("\nPage " + page + " / " + totalPages);
            System.out.println(t.render());

            Table nav = new Table(3, BorderStyle.UNICODE_BOX);
            nav.addCell(" [P] Prev ", CENTER);
            nav.addCell(" [N] Next ", CENTER);
            nav.addCell(" [0] Back ", CENTER);
            System.out.println(nav.render());

            System.out.print("Choice: ");
            switch (sc.nextLine().trim().toUpperCase()) {
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

    private void printSummary(int id, String type, String name, String address,
                              String[] fields, String[] values) {

        Table s = new Table(2, BorderStyle.UNICODE_BOX);

        s.addCell("Field", CENTER);
        s.addCell("Value", CENTER);

        s.addCell("ID", CENTER);
        s.addCell(String.valueOf(id), LEFT);

        s.addCell("Type", CENTER);
        s.addCell(type, LEFT);

        s.addCell("Name", CENTER);
        s.addCell(name, LEFT);

        s.addCell("Address", CENTER);
        s.addCell(address, LEFT);

        for (int i = 0; i < fields.length; i++) {
            s.addCell(fields[i], CENTER);
            s.addCell(values[i], LEFT);
        }

        System.out.println("Added successfully!");
    }

    private String fmt(double v) {
        return String.format("%.1f", v);
    }
}

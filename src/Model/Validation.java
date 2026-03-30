package Model;

import java.util.Scanner;

public class Validation {


    public static int option(int min, int max, Scanner sc) {
        int op;
        while (true) {
            try {
                op = Integer.parseInt(sc.nextLine());
                if (op >= min && op <= max) return op;
                else System.out.print("Enter between " + min + " - " + max + ": ");
            } catch (Exception e) {
                System.out.print("Invalid number! Try again: ");
            }
        }
    }


    public static String name(Scanner sc) {
        String name;
        String regex = "^[A-Za-z]+( [A-Za-z]+)*$";

        while (true) {
            System.out.print("Enter name: ");
            name = sc.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("Name cannot be empty!");
            } else if (!name.matches(regex)) {
                System.out.println("Invalid name! (Only letters, single space between words)");
            } else {
                return name;
            }
        }
    }


    public static String address(Scanner sc) {
        String address;
        while (true) {
            System.out.print("Enter address: ");
            address = sc.nextLine().trim();

            if (address.isEmpty()) {
                System.out.println("Address cannot be empty!");
            } else {
                return address;
            }
        }
    }


    public static double positiveDouble(String field, Scanner sc) {
        double value;
        while (true) {
            try {
                System.out.print("Enter " + field + ": ");
                value = Double.parseDouble(sc.nextLine());

                if (value > 0) return value;
                else System.out.println(field + " must be > 0!");

            } catch (Exception e) {
                System.out.println("Invalid number!");
            }
        }
    }


    public static int positiveInt(String field, Scanner sc) {
        int value;
        while (true) {
            try {
                System.out.print("Enter " + field + ": ");
                value = Integer.parseInt(sc.nextLine());

                if (value > 0) return value;
                else System.out.println(field + " must be > 0!");

            } catch (Exception e) {
                System.out.println("Invalid number!");
            }
        }
    }

    public static int id(Scanner sc) {
        int id;
        while (true) {
            try {
                System.out.print("Enter ID: ");
                id = Integer.parseInt(sc.nextLine());

                if (id > 0) return id;
                else System.out.println("ID must be > 0!");

            } catch (Exception e) {
                System.out.println("Invalid ID!");
            }
        }
    }

    public static boolean yesNo(String msg, Scanner sc) {
        while (true) {
            System.out.print(msg + " (Y/N): ");
            String input = sc.nextLine().trim().toUpperCase();

            if (input.equals("Y")) return true;
            if (input.equals("N")) return false;

            System.out.println("Please enter Y or N!");
        }
    }
}
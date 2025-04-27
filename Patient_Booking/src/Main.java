import java.util.*;
import java.time.*;

public class Main {
    List<Physiotherapist> physiotherapists = new ArrayList<>();
    List<Patient> patients = new ArrayList<>();
    List<Appointment> appointments = new ArrayList<>();
    Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Main system = new Main();
        system.initializeSampleData();
        system.mainMenu();
    }

    void mainMenu() {
        while (true) {
            System.out.println("\n=== Boost Physio Clinic Booking System ===");
            System.out.println("1. Add Patient");
            System.out.println("2. Remove Patient");
            System.out.println("3. Book Appointment by Expertise");
            System.out.println("4. Book Appointment by Physiotherapist");
            System.out.println("5. Cancel Appointment");
            System.out.println("6. Change Appointment");
            System.out.println("7. Generate Report");
            System.out.println("8. Print All Patients");
            System.out.println("9. Exit");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> addPatient();
                case 2 -> removePatient();
                case 3 -> bookByExpertise();
                case 4 -> bookByPhysiotherapist();
                case 5 -> cancelAppointment();
                case 6 -> changeAppointment();
                case 7 -> generateReport();
                case 8 -> printAllPatients();
                case 9 -> {
                    System.out.println("Exiting... Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    void addPatient() {
        System.out.print("Enter patient ID: ");
        String id = sc.nextLine();
        System.out.print("Enter full name: ");
        String fullName = sc.nextLine();
        System.out.print("Enter address: ");
        String address = sc.nextLine();
        System.out.print("Enter phone number: ");
        String phoneNumber = sc.nextLine();

        patients.add(new Patient(id, fullName, address, phoneNumber));
        System.out.println("Patient added successfully.");
    }

    void removePatient() {
        System.out.print("Enter patient ID to remove: ");
        String id = sc.nextLine();
        patients.removeIf(p -> p.id.equals(id));
        System.out.println("Patient removed if found.");
    }

    void bookByExpertise() {
        System.out.print("Enter area of expertise (e.g., Physiotherapy): ");
        String expertise = sc.nextLine();
        List<Physiotherapist> filtered = physiotherapists.stream()
                .filter(p -> p.areasOfExpertise.contains(expertise))
                .toList();

        if (filtered.isEmpty()) {
            System.out.println("No physiotherapists found with that expertise.");
            return;
        }

        for (int i = 0; i < filtered.size(); i++) {
            System.out.println((i + 1) + ". " + filtered.get(i).fullName);
        }
        System.out.print("Select physiotherapist by number: ");
        int choice = Integer.parseInt(sc.nextLine()) - 1;

        Physiotherapist selected = filtered.get(choice);
        bookTreatment(selected);
    }

    void bookByPhysiotherapist() {
        System.out.print("Enter physiotherapist full name: ");
        String name = sc.nextLine();
        Optional<Physiotherapist> optional = physiotherapists.stream()
                .filter(p -> p.fullName.equalsIgnoreCase(name))
                .findFirst();

        if (optional.isEmpty()) {
            System.out.println("Physiotherapist not found.");
            return;
        }

        bookTreatment(optional.get());
    }

    void bookTreatment(Physiotherapist physio) {
        List<Treatment> availableTreatments = physio.timetable.values().stream()
                .filter(t -> appointments.stream().noneMatch(a -> a.treatment.equals(t) && a.status.equals("Booked")))
                .toList();

        if (availableTreatments.isEmpty()) {
            System.out.println("No available treatments.");
            return;
        }

        for (int i = 0; i < availableTreatments.size(); i++) {
            Treatment t = availableTreatments.get(i);
            System.out.println((i + 1) + ". " + t.name + " at " + t.dateTime);
        }
        System.out.print("Select treatment by number: ");
        int tChoice = Integer.parseInt(sc.nextLine()) - 1;

        System.out.print("Enter patient ID: ");
        String pId = sc.nextLine();
        Optional<Patient> patientOpt = patients.stream()
                .filter(p -> p.id.equals(pId))
                .findFirst();

        if (patientOpt.isEmpty()) {
            System.out.println("Patient not found.");
            return;
        }

        Appointment appointment = new Appointment(availableTreatments.get(tChoice), patientOpt.get());
        appointments.add(appointment);
        System.out.println("Appointment booked successfully.");
    }

    void cancelAppointment() {
        System.out.print("Enter patient ID: ");
        String pId = sc.nextLine();
        List<Appointment> patientAppointments = appointments.stream()
                .filter(a -> a.patient.id.equals(pId) && a.status.equals("Booked"))
                .toList();

        if (patientAppointments.isEmpty()) {
            System.out.println("No booked appointments found.");
            return;
        }

        for (int i = 0; i < patientAppointments.size(); i++) {
            Appointment a = patientAppointments.get(i);
            System.out.println((i + 1) + ". " + a.treatment.name + " at " + a.treatment.dateTime);
        }
        System.out.print("Select appointment to cancel: ");
        int choice = Integer.parseInt(sc.nextLine()) - 1;

        patientAppointments.get(choice).status = "Cancelled";
        System.out.println("Appointment cancelled.");
    }

    void changeAppointment() {
        cancelAppointment();
        System.out.println("Now book a new appointment:");
        bookByExpertise();
    }

    void generateReport() {
        System.out.println("\n=== Treatment Appointments Report ===");
        for (Physiotherapist p : physiotherapists) {
            System.out.println("\nPhysiotherapist: " + p.fullName);
            for (Appointment a : appointments) {
                if (a.treatment.physiotherapist.equals(p)) {
                    System.out.println("  - " + a.treatment.name + " | Patient: " + a.patient.fullName +
                            " | Time: " + a.treatment.dateTime + " | Status: " + a.status);
                }
            }
        }
    }

    void printAllPatients() {
        System.out.println("\n=== List of All Patients ===");
        for (Patient p : patients) {
            System.out.println("ID: " + p.id + " | Name: " + p.fullName + " | Address: " + p.address + " | Phone: " + p.phoneNumber);
        }
    }

    void initializeSampleData() {
        // Physiotherapists
        Physiotherapist p1 = new Physiotherapist("P001", "Alice Smith", "10 Main St", "1234567890", Arrays.asList("Physiotherapy", "Rehabilitation"));
        Physiotherapist p2 = new Physiotherapist("P002", "Bob Johnson", "20 Oak St", "2345678901", Arrays.asList("Osteopathy", "Rehabilitation"));
        Physiotherapist p3 = new Physiotherapist("P003", "Carol White", "30 Pine St", "3456789012", Arrays.asList("Physiotherapy", "Osteopathy"));

        physiotherapists.addAll(Arrays.asList(p1, p2, p3));

        // Patients (12 Indian names)
        patients.add(new Patient("PT001", "Arun Kumar", "Delhi, India", "9988776655"));
        patients.add(new Patient("PT002", "Priya Sharma", "Mumbai, India", "9876543210"));
        patients.add(new Patient("PT003", "Rajesh Gupta", "Bangalore, India", "9556677889"));
        patients.add(new Patient("PT004", "Neha Verma", "Kolkata, India", "9667888999"));
        patients.add(new Patient("PT005", "Vikram Singh", "Chennai, India", "9444556677"));
        patients.add(new Patient("PT006", "Sanya Patel", "Ahmedabad, India", "9123456789"));
        patients.add(new Patient("PT007", "Ravi Mehta", "Pune, India", "9034567890"));
        patients.add(new Patient("PT008", "Isha Desai", "Hyderabad, India", "9345678901"));
        patients.add(new Patient("PT009", "Manish Yadav", "Lucknow, India", "9467678899"));
        patients.add(new Patient("PT010", "Aditi Reddy", "Mumbai, India", "9822334455"));
        patients.add(new Patient("PT011", "Karan Agarwal", "Jaipur, India", "9008771234"));
        patients.add(new Patient("PT012", "Siddhi Joshi", "Surat, India", "9033123412"));

        // Sample 4-week timetable
        LocalDate baseDate = LocalDate.of(2025, 5, 1); // May 2025
        for (int week = 0; week < 4; week++) {
            p1.timetable.put(baseDate.plusDays(week * 7).atTime(10, 0), new Treatment("Neural Mobilisation", baseDate.plusDays(week * 7).atTime(10, 0), p1));
            p1.timetable.put(baseDate.plusDays(week * 7).atTime(14, 0), new Treatment("Acupuncture", baseDate.plusDays(week * 7).atTime(14, 0), p1));

            p2.timetable.put(baseDate.plusDays(week * 7 + 1).atTime(11, 0), new Treatment("Massage", baseDate.plusDays(week * 7 + 1).atTime(11, 0), p2));
            p2.timetable.put(baseDate.plusDays(week * 7 + 1).atTime(15, 0), new Treatment("Pool Rehabilitation", baseDate.plusDays(week * 7 + 1).atTime(15, 0), p2));

            p3.timetable.put(baseDate.plusDays(week * 7 + 2).atTime(12, 0), new Treatment("Spine Mobilisation", baseDate.plusDays(week * 7 + 2).atTime(12, 0), p3));
        }

        // Book random appointments for patients
        Random rand = new Random();
        for (Patient patient : patients) {
            Physiotherapist randomPhysio = physiotherapists.get(rand.nextInt(physiotherapists.size()));
            Treatment randomTreatment = randomPhysio.timetable.values().stream().skip(rand.nextInt(randomPhysio.timetable.size())).findFirst().orElse(null);
            if (randomTreatment != null) {
                Appointment appointment = new Appointment(randomTreatment, patient);
                appointments.add(appointment);
            }
        }
    }

}

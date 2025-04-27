public class Appointment {
    Treatment treatment;
    Patient patient;
    String status; // Booked, Cancelled, Attended

    public Appointment(Treatment treatment, Patient patient) {
        this.treatment = treatment;
        this.patient = patient;
        this.status = "Booked";
    }
}

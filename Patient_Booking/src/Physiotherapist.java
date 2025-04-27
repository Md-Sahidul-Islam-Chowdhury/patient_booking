import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;

public class Physiotherapist {
    String id;
    String fullName;
    String address;
    String phoneNumber;
    List<String> areasOfExpertise;
    Map<LocalDateTime, Treatment> timetable = new HashMap<>();

    public Physiotherapist(String id, String fullName, String address, String phoneNumber, List<String> areasOfExpertise) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.areasOfExpertise = areasOfExpertise;
    }
}

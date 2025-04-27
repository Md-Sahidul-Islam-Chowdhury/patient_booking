import java.time.LocalDateTime;

public class Treatment {
    String name;
    LocalDateTime dateTime;
    Physiotherapist physiotherapist;

    public Treatment(String name, LocalDateTime dateTime, Physiotherapist physiotherapist) {
        this.name = name;
        this.dateTime = dateTime;
        this.physiotherapist = physiotherapist;
    }
}

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Log4j2
public class Main {
    public static void main(String[] args) {
        log.info("Hello World");
    }
}

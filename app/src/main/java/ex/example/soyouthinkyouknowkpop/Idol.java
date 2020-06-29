package ex.example.soyouthinkyouknowkpop;

import java.net.URL;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class Idol {
    private String name;
    private String group;

    @EqualsAndHashCode.Exclude private URL photoSource;

    public String getCompositeIdentity() {

        if (group.equalsIgnoreCase("solo") || group.equalsIgnoreCase("Solo Artist")) {
            return String.format("%s, a Solo Artist", name);
        }
        return String.format("%s, part of %s", name, group);
    }
}

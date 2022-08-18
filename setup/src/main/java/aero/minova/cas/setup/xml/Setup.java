package aero.minova.cas.setup.xml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Setup {
    private final static XmlMapper XML_MAPPER = new XmlMapper();
    private String name;
    List<Tableschema> schema;
    @JsonProperty("sql-code")
    List<Script> sqlCode;

    public static Setup parse(Path file) {
        try {
            return parse(Files.readString(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Setup parse(String document) {
        try {
            return XML_MAPPER.readValue(document, Setup.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

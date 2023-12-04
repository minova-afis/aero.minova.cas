package aero.minova.cas.setup.xml.setup;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "setup")
public class SetupType {
    private final static XmlMapper XML_MAPPER = new XmlMapper();
    private String name;
    List<TableschemaType> schema;
    @JsonProperty("sql-code")
    List<ScriptType> sqlCode;

    public static SetupType parse(Path file) {
        try {
            return parse(Files.readString(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static SetupType parse(String document) {
        try {
            return XML_MAPPER.readValue(document, SetupType.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

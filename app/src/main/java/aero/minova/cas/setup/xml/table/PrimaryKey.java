package aero.minova.cas.setup.xml.table;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrimaryKey {
    @JacksonXmlElementWrapper(useWrapping = false)
    List<String> column = new ArrayList<>();
}

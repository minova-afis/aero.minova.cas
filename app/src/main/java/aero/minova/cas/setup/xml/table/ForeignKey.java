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
public class ForeignKey {
    private String refid;
    private String table;

    @JacksonXmlElementWrapper(useWrapping = false)
    List<Column> column = new ArrayList<>();
}

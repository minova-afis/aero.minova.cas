package aero.minova.cas.setup.xml.setup;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Script {
    public static final String TYPE_SCRIPT = "script";
    public static final String TYPE_TABLE = "table";
    public static final String TYPE_PROCEDURE = "procedure";
    public static final String TYPE_VIEW = "view";
    public static final String TYPE_FUNCTION = "function";
    private String name;
    private String type;
}

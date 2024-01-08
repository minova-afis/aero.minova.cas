package aero.minova.cas.setup.xml.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ColumnInteger {
    private boolean identity;
    private boolean nullable = true;
    public boolean getIdentity() {
        return identity;
    }
    public boolean getNullable() {
        return nullable;
    }
}

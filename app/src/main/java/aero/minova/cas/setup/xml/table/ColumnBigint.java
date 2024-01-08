package aero.minova.cas.setup.xml.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ColumnBigint {
    private boolean nullable = true;

    public boolean getNullable() {
        return nullable;
    }
}

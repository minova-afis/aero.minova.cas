package aero.minova.core.application.system.covid.test.print.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class TestStrecke {
    int keyLong;
    String keyText;
    public TestStrecke(String keyText, int keyLong) {
        this.keyText = keyText;
        this.keyLong = keyLong;
    }
}

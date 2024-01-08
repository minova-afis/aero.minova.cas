package aero.minova.cas.setup.xml.table;

import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Scanner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TableTest {
    @Test
    public void testParsing() {
        final String tFuellingType = new Scanner(Objects.requireNonNull(getClass()//
                .getClassLoader()//
                .getResourceAsStream("tFuellingType.table.xml")), "UTF-8")//
                .useDelimiter("\\A")//
                .next();
        final var testSubject = Table.parse(tFuellingType);
        assertThat(testSubject.getName()).isEqualTo("tFuellingType");
        assertThat(testSubject.getColumn().size()).isEqualTo(14);

        assertThat(testSubject.getColumn().get(2).getName()).isEqualTo("Description");
        assertThat(testSubject.getColumn().get(2).getDefault()).isEqualTo("getdate()");
        assertThat(testSubject.getColumn().get(2).getVarchar().getLength()).isEqualTo(50);
        assertThat(testSubject.getColumn().get(2).getVarchar().getNullable()).isTrue();
        assertThat(testSubject.getColumn().get(2).getVarchar().getIdentity()).isTrue();
        assertThat(testSubject.getColumn().get(4).getDatetime().getNullable()).isEqualTo(true);
        assertThat(testSubject.getColumn().get(4).getDatetime().getIdentity()).isEqualTo(true);
        assertThat(testSubject.getColumn().get(5).getName()).isEqualTo("LastAction");
        assertThat(testSubject.getColumn().get(5).getInteger().getNullable()).isEqualTo(true);
        assertThat(testSubject.getColumn().get(5).getInteger().getIdentity()).isEqualTo(true);
        assertThat(testSubject.getColumn().get(6).getName()).isEqualTo("Refuelling");
        assertThat(testSubject.getColumn().get(6).getBoolean().getNullable()).isEqualTo(true);
        assertThat(testSubject.getColumn().get(6).getBoolean().getIdentity()).isEqualTo(true);
        assertThat(testSubject.getColumn().get(7).getName()).isEqualTo("StartWeight");
        assertThat(testSubject.getColumn().get(7).getFloat().getNullable()).isEqualTo(true);
        assertThat(testSubject.getColumn().get(7).getFloat().getIdentity()).isEqualTo(true);
        assertThat(testSubject.getColumn().get(7).getFloat().getDecimals()).isEqualTo(2);
        assertThat(testSubject.getColumn().get(8).getName()).isEqualTo("DischargeVolume1");
        assertThat(testSubject.getColumn().get(8).getMoney().getNullable()).isEqualTo(true);
        assertThat(testSubject.getColumn().get(8).getMoney().getIdentity()).isEqualTo(true);
        assertThat(testSubject.getColumn().get(9).getName()).isEqualTo("AmbientVolume");
        assertThat(testSubject.getColumn().get(9).getBigint().getNullable()).isEqualTo(true);

        assertThat(testSubject.getPrimarykey().getColumn().get(0)).isEqualTo("KeyLong");
        assertThat(testSubject.getPrimarykey().getColumn().size()).isEqualTo(1);
        assertThat(testSubject.getForeignkey().size()).isEqualTo(2);
        assertThat(testSubject.getForeignkey().get(0).getColumn().size()).isEqualTo(1);
        assertThat(testSubject.getForeignkey().get(0).getColumn().get(0).getName()).isEqualTo("ShipKey");
        assertThat(testSubject.getForeignkey().get(0).getColumn().get(0).getRefid()).isEqualTo("VehicleKey");
        assertThat(testSubject.getForeignkey().get(0).getColumn().size()).isEqualTo(1);
        assertThat(testSubject.getForeignkey().get(1).getRefid()).isEqualTo("ShiftClosingKey");
        assertThat(testSubject.getForeignkey().get(1).getTable()).isEqualTo("tShiftClosing");
        assertThat(testSubject.getUniquekey().size()).isEqualTo(2);
        assertThat(testSubject.getUniquekey().get(1).getName()).isEqualTo("UQ_xtcasUserGroupTest");
        assertThat(testSubject.getUniquekey().get(1).getColumn().size()).isEqualTo(1);
        assertThat(testSubject.getUniquekey().get(1).getColumn().get(0)).isEqualTo("KeyLong");

        assertThat(testSubject.getValues().getColumn().size()).isEqualTo(7);
        assertThat(testSubject.getValues().getColumn().get(5).getRefid()).isEqualTo("NightFuelling");
        assertThat(testSubject.getValues().getRow().size()).isEqualTo(12);
        assertThat(testSubject.getValues().getRow().get(11).getValue().size()).isEqualTo(7);
        assertThat(testSubject.getValues().getRow().get(11).getValue().get(2).getContent()).isEqualTo("Ausgleichsbuchung");
    }
}

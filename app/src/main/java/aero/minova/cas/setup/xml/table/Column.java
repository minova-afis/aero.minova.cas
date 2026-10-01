package aero.minova.cas.setup.xml.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(
        ignoreUnknown = true,
        value = {"tableName"})
public class Column {
    private String name;
    private String defaultValue;
    private String tableName;
    private ColumnBoolean Boolean;
    private ColumnDatetime datetime;
    private ColumnFloat Float;
    private ColumnInteger Integer;
    private ColumnMoney money;
    private ColumnVarchar varchar;
    private ColumnBigint bigint;
    private String Default;
    private String refid;

    public ColumnBigint addNewBigint() {
        final ColumnBigint newBigint = new ColumnBigint();
        return newBigint;
    }

    public ColumnBoolean addNewBoolean() {
        final ColumnBoolean newBoolean = new ColumnBoolean();
        return newBoolean;
    }

    public ColumnDatetime addNewDatetime() {
        final ColumnDatetime newDatetime = new ColumnDatetime();
        return newDatetime;
    }

    public ColumnFloat addNewFloat() {
        final ColumnFloat newFloat = new ColumnFloat();
        return newFloat;
    }

    public ColumnInteger addNewInteger() {
        final ColumnInteger newInteger = new ColumnInteger();
        return newInteger;
    }

    public ColumnMoney addNewMoney() {
        final ColumnMoney newMoney = new ColumnMoney();
        return newMoney;
    }

    public ColumnVarchar addNewVarchar() {
        final ColumnVarchar newVarchar = new ColumnVarchar();
        return newVarchar;
    }
}

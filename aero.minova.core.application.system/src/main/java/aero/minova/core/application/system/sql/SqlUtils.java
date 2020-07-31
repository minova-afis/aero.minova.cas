package aero.minova.core.application.system.sql;

import aero.minova.core.application.system.domain.DataType;
import aero.minova.core.application.system.domain.Value;

public class SqlUtils {

	private SqlUtils() {
		throw new UnsupportedOperationException();
	}

	public static String toSqlString(Value value) {
		if (value.getType() == DataType.BOOLEAN) {
			if (value.getBooleanValue()) {
				return "1";
			} else {
				return "0";
			}
		} else {
			return value.getValue().toString();
		}
	}

}

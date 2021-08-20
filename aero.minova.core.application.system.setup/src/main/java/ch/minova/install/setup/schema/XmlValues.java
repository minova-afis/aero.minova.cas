package ch.minova.install.setup.schema;

import java.util.ArrayList;
import java.util.Vector;

import ch.minova.core.xml.tables.TableDocument.Table.Values.Column;

public class XmlValues {
	// Spalten für die Rows, in die werden die Werte eingegeben.
	public Column[] valuecolumns;
	public String tablename;
	// Inhalt der einzelnen Rows.
	public ArrayList<String[]> rows = new ArrayList<String[]>();

	public XmlValues(final Column[] columnArray, final String tname) {
		this.valuecolumns = columnArray;
		this.tablename = tname;
	}

	public void addValueRow(final String[] data) {
		this.rows.add(data);
	}

	/*
	 * IF NOT EXISTS (SELECT * FROM dbo.[tColor] WHERE ([KeyLong] = '1') ) BEGIN INSERT INTO dbo.[tColor] ( [KeyLong], [KeyText], [Description], [RGB])
	 * VALUES('1', 'SCHWARZ', 'Schwarz', '-16777216' ) END ;
	 */
	public String generateSqlCode() {
		String sqlValues = "";
		for (final String[] s : this.rows) {
			sqlValues += generateOneValue(s);
			sqlValues += ";\n";
		}
		return sqlValues;
	}

	private String generateOneValue(final String[] row) {
		// nicht bei allen ist es der Keylong siehe [tFmcDialog] desshalb immer der erste Wert
		String begin = "IF NOT EXISTS (SELECT * FROM dbo.[" + this.tablename + "] WHERE ([" + this.valuecolumns[0].getRefid() + "] = '" + row[0]
				+ "') ) BEGIN ";
		String insert = "INSERT INTO dbo.[" + this.tablename + "] (";
		// erstellen des Insert
		for (int i = 0; i < this.valuecolumns.length; i++) {
			insert += " [" + this.valuecolumns[i].getRefid() + "]";
			if ((i + 1) < this.valuecolumns.length) {
				insert += ",";
			}
		}
		insert += ") Values(";
		for (int i = 0; i < row.length; i++) {
			// aufpassen bei Hochkommata
			if (row[i].contains("'")) {
				row[i] = row[i].replace("'", "''");
			}
			insert += " '" + row[i] + "'";
			if ((i + 1) < row.length) {
				insert += ",";
			}
		}
		insert += ") End";
		begin += insert;
		return begin;
	}

	public String generateSqlCode(final XmlPrimaryKeyConstraint primaryKeyConstraint) {
		String sqlValues = "";
		for (final String[] s : this.rows) {
			sqlValues += generateOneValue(s, primaryKeyConstraint);
			sqlValues += ";\n";
		}
		return sqlValues;
	}

	private String generateOneValue(final String[] row, final XmlPrimaryKeyConstraint primaryKeyConstraint) {
		// nicht bei allen ist es der Keylong siehe [tFmcDialog] desshalb immer der erste Wert
		String begin = "IF NOT EXISTS (SELECT * FROM dbo.[" + this.tablename + "] WHERE ";
		Boolean firsttime = true;
		for (int i = 0; i < row.length; i++) {
			if (isColumnInPK(this.valuecolumns[i].getRefid(), primaryKeyConstraint)) {
				if (firsttime) {
					firsttime = false;
				} else {
					begin += " and ";
				}
				begin += "([" + this.valuecolumns[i].getRefid() + "] = '" + row[i] + "')";
			}
		}
		begin += ") BEGIN ";

		String insert = "INSERT INTO dbo.[" + this.tablename + "] (";
		// erstellen des Insert
		for (int i = 0; i < this.valuecolumns.length; i++) {
			insert += " [" + this.valuecolumns[i].getRefid() + "]";
			if ((i + 1) < this.valuecolumns.length) {
				insert += ",";
			}
		}
		insert += ") Values(";
		for (int i = 0; i < row.length; i++) {
			// aufpassen bei Hochkommata
			if (row[i].contains("'")) {
				row[i] = row[i].replace("'", "''");
			}
			insert += " '" + row[i] + "'";
			if ((i + 1) < row.length) {
				insert += ",";
			}
		}
		insert += ") End";
		begin += insert;
		return begin;
	}

	/**
	 * Überprüft, ob die angegebende Spalte ein Teil des PK ist. Wenn ja wird true zurückgegeben ansonten false;
	 * 
	 * @param refid
	 * @param primaryKeyConstraint
	 * @return
	 */
	private boolean isColumnInPK(final String refid, final XmlPrimaryKeyConstraint primaryKeyConstraint) {
		final Vector<String> cnames = primaryKeyConstraint.getColumnNames();
		if (cnames.contains(refid)) {
			return true;
		}

		return false;
	}
}
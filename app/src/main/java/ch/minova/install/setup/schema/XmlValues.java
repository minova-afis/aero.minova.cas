package ch.minova.install.setup.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import aero.minova.cas.setup.xml.table.ColumnReference;
import aero.minova.cas.setup.xml.table.Value;

public class XmlValues {
	// Spalten für die Rows, in die werden die Werte eingegeben.
	public List<ColumnReference> valuecolumns;
	public String tablename;
	// Inhalt der einzelnen Rows.
	public ArrayList<List<Value>> rows = new ArrayList<>();

	public XmlValues(final List<ColumnReference> columnArray, final String tname) {
		this.valuecolumns = columnArray;
		this.tablename = tname;
	}

	public void addValueRow(final List<Value> data) {
		this.rows.add(data);
	}

	/*
	 * IF NOT EXISTS (SELECT * FROM dbo.[tColor] WHERE ([KeyLong] = '1') ) BEGIN INSERT INTO dbo.[tColor] ( [KeyLong], [KeyText], [Description], [RGB])
	 * VALUES('1', 'SCHWARZ', 'Schwarz', '-16777216' ) END ;
	 */
	public String generateSqlCode() {
		String sqlValues = "";
		for (final List<Value> s : this.rows) {
			sqlValues += generateOneValue(s);
			sqlValues += ";\n";
		}
		return sqlValues;
	}

	private String generateOneValue(final List<Value> row) {
		// nicht bei allen ist es der Keylong siehe [tFmcDialog] desshalb immer der erste Wert
		String begin = "IF NOT EXISTS (SELECT * FROM dbo.[" + this.tablename + "] WHERE ([" + this.valuecolumns.get(0).getRefid() + "] = '" + row.get(0)
				+ "') ) BEGIN ";
		String insert = "INSERT INTO dbo.[" + this.tablename + "] (";
		// erstellen des Insert
		for (int i = 0; i < this.valuecolumns.size(); i++) {
			insert += " [" + this.valuecolumns.get(i).getRefid() + "]";
			if ((i + 1) < this.valuecolumns.size()) {
				insert += ",";
			}
		}
		insert += ") Values(";
		for (int i = 0; i < row.size(); i++) {
			// aufpassen bei Hochkommata
			if (row.get(i).getContent().contains("'")) {
				row.get(i).setContent(row.get(i).getContent().replace("'", "''"));
			}
			insert += " '" + row.get(i).getContent() + "'";
			if ((i + 1) < row.size()) {
				insert += ",";
			}
		}
		insert += ") End";
		begin += insert;
		return begin;
	}

	public String generateSqlCode(final XmlPrimaryKeyConstraint primaryKeyConstraint) {
		String sqlValues = "";
		for (final List<Value> s : this.rows) {
			sqlValues += generateOneValue(s, primaryKeyConstraint);
			sqlValues += ";\n";
		}
		return sqlValues;
	}

	private String generateOneValue(final List<Value> row, final XmlPrimaryKeyConstraint primaryKeyConstraint) {
		// nicht bei allen ist es der Keylong siehe [tFmcDialog] desshalb immer der erste Wert
		String begin = "IF NOT EXISTS (SELECT * FROM dbo.[" + this.tablename + "] WHERE ";
		Boolean firsttime = true;
		for (int i = 0; i < row.size(); i++) {
			if (isColumnInPK(this.valuecolumns.get(i).getRefid(), primaryKeyConstraint)) {
				if (firsttime) {
					firsttime = false;
				} else {
					begin += " and ";
				}
				begin += "([" + this.valuecolumns.get(i).getRefid() + "] = '" + row.get(i).getContent() + "')";
			}
		}
		begin += ") BEGIN ";

		String insert = "INSERT INTO dbo.[" + this.tablename + "] (";
		// erstellen des Insert
		for (int i = 0; i < this.valuecolumns.size(); i++) {
			insert += " [" + this.valuecolumns.get(i).getRefid() + "]";
			if ((i + 1) < this.valuecolumns.size()) {
				insert += ",";
			}
		}
		insert += ") Values(";
		for (int i = 0; i < row.size(); i++) {
			if (row.get(i).getContent() == null) {
				// Hiermit wird der Default-Value für die Spalte verwendet, so wie es auch im alten Install-Tool gemacht wurde.
				insert += " ''";
			} else {
				// aufpassen bei Hochkommata
				if (row.get(i).getContent().contains("'")) {
					row.get(i).setContent(row.get(i).getContent().replace("'", "''"));
				}
				insert += " '" + row.get(i).getContent() + "'";
			}
			if ((i + 1) < row.size()) {
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
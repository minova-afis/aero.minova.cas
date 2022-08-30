package ch.minova.install.setup.schema;

import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import aero.minova.cas.setup.xml.table.*;

public class XmlDatabaseTable {
    private String name;
    private Hashtable<String, XmlDatabaseColumn> columnTable = new Hashtable<String, XmlDatabaseColumn>();
    private Vector<XmlDatabaseColumn> columnVector = new Vector<XmlDatabaseColumn>();
    private XmlPrimaryKeyConstraint primaryKeyConstraint;
    private Vector<XmlForeignKeyContraint> foreignKeyContraint = new Vector<XmlForeignKeyContraint>();
    private Vector<XmlUniqueKeyConstraint> uniqueKeyConstraint = new Vector<XmlUniqueKeyConstraint>();
    private XmlValues xmlvalues = null;
    private boolean execsqlbefore = false;
    private boolean identity = false;
    private final String setIdentitiy = "SET IDENTITY_INSERT";
    private String DBCollation = "null";
    private final String ifexistsconstrain = "select * from INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE where CONSTRAINT_NAME = '";
    private final String ifexistsconstrainend = "'";

    public boolean isIdentity() {
        return this.identity;
    }

    public void setIdentity(final boolean identity) {
        this.identity = identity;
    }

    public XmlValues getXmlvalues() {
        return this.xmlvalues;
    }

    public void setXmlvalues(final XmlValues xmlvalues) {
        this.xmlvalues = xmlvalues;
    }

    public XmlDatabaseTable(final InputStream is, final String dbcollation) {
        setDBCollation(dbcollation);
        read(is);
    }

    public XmlDatabaseTable(final InputStream is) {
        read(is);
    }

    public boolean isExecsqlbefore() {
        return this.execsqlbefore;
    }

    public void setExecsqlbefore(final boolean execsqlbefore) {
        this.execsqlbefore = execsqlbefore;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Hashtable<String, XmlDatabaseColumn> getColumnTable() {
        return this.columnTable;
    }

    public void setColumnTable(final Hashtable<String, XmlDatabaseColumn> columnTable) {
        this.columnTable = columnTable;
    }

    public Vector<XmlDatabaseColumn> getColumnVector() {
        return this.columnVector;
    }

    public void setColumnVector(final Vector<XmlDatabaseColumn> columnVector) {
        this.columnVector = columnVector;
    }

    public XmlPrimaryKeyConstraint getPrimaryKeyConstraint() {
        return this.primaryKeyConstraint;
    }

    public void setPrimaryKeyConstraint(final XmlPrimaryKeyConstraint primaryKeyConstraint) {
        this.primaryKeyConstraint = primaryKeyConstraint;
    }

    public Vector<XmlForeignKeyContraint> getforeignKeyContraint() {
        return this.foreignKeyContraint;
    }

    public void setforeignKeyContraint(final Vector<XmlForeignKeyContraint> foreignKeyContraint) {
        this.foreignKeyContraint = foreignKeyContraint;
    }

    public Vector<XmlUniqueKeyConstraint> getuniqueKeyConstraint() {
        return this.uniqueKeyConstraint;
    }

    public void setuniqueKeyConstraint(final Vector<XmlUniqueKeyConstraint> uniqueKeyConstraint) {
        this.uniqueKeyConstraint = uniqueKeyConstraint;
    }

    private void read(final InputStream is) {
        final Table t = Table.parse(is);

        // der angegeben Name aus der xml-Datei wird hier in "name"
        // geschrieben
        this.name = t.getName();
        for (int i = 0; i < t.getColumn().size(); i++) {
            final XmlDatabaseColumn dbc = new XmlDatabaseColumn(t, t.getColumn().get(i), this.DBCollation);
            this.columnTable.put(dbc.getName(), dbc);
            if (t.getColumn().get(i).getName().equalsIgnoreCase("Keylong")) {
                // nicht immer auf identitiy setzen
                if (t.getColumn().get(i).getInteger().getIdentity() == true) {
                    setIdentity(true);
                }
            }
            this.columnVector.add(dbc);
        }
        if (t.getPrimarykey() != null) {
            this.primaryKeyConstraint = new XmlPrimaryKeyConstraint(t);
            final PrimaryKey primaryKey = t.getPrimarykey();
            final List<String> colNames = primaryKey.getColumn();
            for (int i = 0; i < colNames.size(); i++) {
                this.primaryKeyConstraint.addColumnName(colNames.get(i));
            }
        }
        final List<ForeignKey> fkeys = t.getForeignkey();
        for (int i = 0; i < fkeys.size(); i++) {
            final XmlForeignKeyContraint fkc = new XmlForeignKeyContraint(t, fkeys.get(i), fkeys.get(i).getColumn());
            this.foreignKeyContraint.add(fkc);
        }
        final List<UniqueKey> ukeys = t.getUniquekey();
        for (int i = 0; i < ukeys.size(); i++) {
            final XmlUniqueKeyConstraint ukc = new XmlUniqueKeyConstraint(t, ukeys.get(i), ukeys.get(i).getColumn());
            this.uniqueKeyConstraint.add(ukc);
        }
        // auslesen wie viele Rows,
        // 2 Dimensionales Array!
        final Values v = t.getValues();
        if (v != null) {
            final XmlValues xmlv = new XmlValues(v.getColumn(), this.name);
            setXmlvalues(xmlv);
            for (int i = 0; i < v.getRow().size(); i++) {
                this.xmlvalues.addValueRow(v.getRow().get(i).getValue());
            }
        }
    }

    public void saveXML(StringWriter sw) {
        throw new RuntimeException("XmlDatabaseTable#saveXml is not implemented version 12.");
    }

    public void saveSQL(final StringWriter sw) {
        sw.write("create table " + "[" + this.name + "]" + "(");
        boolean firstColumn = true;
        for (final XmlDatabaseColumn dbc : this.columnVector) {
            dbc.generateSql(sw, firstColumn);
            firstColumn = false;

        }
        sw.write(")");
    }

    public void saveSQLLogDB(final StringWriter sw, final boolean LogDB) {
        sw.write("create table " + "[" + this.name + "]" + "(");
        boolean firstColumn = true;
        for (final XmlDatabaseColumn dbc : this.columnVector) {
            dbc.generateSql(sw, firstColumn);
            firstColumn = false;
        }

        if (LogDB == true) {
            // An dieser Stelle führen wir ein, dass die OriginalKeys in der LogDB mit "_Original" benannt werden.

            if (this.primaryKeyConstraint != null) {
                for (final String colums : this.primaryKeyConstraint.getColumnNames()) {
                    for (final XmlDatabaseColumn dbc : this.columnVector) {
                        if (dbc.getColumnName().equalsIgnoreCase(colums)) {
                            sw.write(",[" + colums + "_Original" + "]" + dbc.getTypeText());
                        }
                    }
                }
            } else {
                sw.write(",[KeyLong_Original] int");
                System.out.println(MessageFormat
                        .format("Die angeführte Tabelle {0} hat keinen PrimaryKey desshalb wird angenommen, dass Es einen KeyLong gibt.", this.name));
                // throw new NullPointerException(MessageFormat.format("Die angeführte Tabelle {0} hat keinen PrimaryKey", name));
            }
            sw.write(", [LoggingDate] datetime default (CURRENT_TIMESTAMP), [LoggingUser] nvarchar(100) default (SYSTEM_USER)");
            final String dummy = sw.toString();
            // Wir legen in jeder Loggingtabelle einen Keylong an, im Falle von Tabellen die nicht mit einem solchen ausgestattet sind wird ein neuer angelegt.
            if (!dummy.contains("[KeyLong]") && !dummy.contains("[keylong]")) {
                sw.write(", [KeyLong] int identity (1, 1)");
            }
        }
        sw.write(")");
    }

    public String compareConstrainsFK(final SqlDatabaseTable sqlTable, final Connection connection, final HashMap<String, String> mymap) throws SQLException {
        Vector<SqlConstraint> vSqlConstraint;
        vSqlConstraint = sqlTable.getConstraints();
        final HashMap<String, String> hashConstraints = new HashMap<String, String>();
        String constraintskey = null;
        String constrintsValue = null;
        // ResultSet rs = null;
        String constriants = "";
        // "SqlForeignKeyConstraint"
        for (int h = 0; h < getforeignKeyContraint().size(); h++) {
            constraintskey = "fk_" + getforeignKeyContraint().get(h).getTableName() + "_" + getforeignKeyContraint().get(h).getColumnName();
            constrintsValue = "fk_" + getforeignKeyContraint().get(h).getTableName() + "_" + getforeignKeyContraint().get(h).getColumnName() + "(";
            for (int k = 0; k < getforeignKeyContraint().get(h).getForeignKeyColumns().length; k++) {
                if (k > 0) {
                    constrintsValue += ", ";
                }
                constrintsValue += getforeignKeyContraint().get(h).getForeignKeyColumns()[k].getLocalColumnName();
            }
            if (getforeignKeyContraint().get(h).getForeignKeyColumns().length == 0) {
                constrintsValue += getforeignKeyContraint().get(h).getColumnName();
            }
            constrintsValue += ")->PK_" + getforeignKeyContraint().get(h).getForeignTableName();
            hashConstraints.put(constraintskey.toLowerCase(), constrintsValue);
        }
        for (final SqlConstraint sqlconstraint : vSqlConstraint) {
            if (sqlconstraint.getName().toLowerCase().startsWith("fk")) {
                boolean fk_found = false;
                for (int i = 0; i < getforeignKeyContraint().size(); i++) {
                    if (mymap.containsKey(getforeignKeyContraint().get(i).getName())) {
                        if (sqlconstraint.getName().equalsIgnoreCase(getforeignKeyContraint().get(i).getName())) {
                            getforeignKeyContraint().remove(i);
                            fk_found = true;
                            i = getforeignKeyContraint().size();
                        }
                    } else {
                        fk_found = true;
                    }
                }
                if (!fk_found) {
                    dropConstraint(sqlconstraint.getTable().getName(), sqlconstraint.getName(), connection);
                }
            }
        }
        constriants = getXMl_FK_Constraints(constriants);

        if (constriants.equals("")) {
            return null;
        }
        return constriants;
    }

    public String getXMl_FK_Constraints(String constriants) {
        for (int i = 0; i < getforeignKeyContraint().size(); i++) {
            constriants += "\n";
            constriants += getforeignKeyContraint().get(i).getSQLCode();
        }
        return constriants;
    }

    /**
     * Entscheidet ob der PK oder UK ausgeführt werden muss. Hier muss aus der Datenbank gelesen werden, ob die entsprechten Constrains auch noch vorhanden
     * sind.
     *
     * @param sqlTable
     * @param connection
     * @param mymap
     * @return
     * @throws SQLException
     */
    public String compareConstrainsPK_UK(final SqlDatabaseTable sqlTable, final Connection connection, final HashMap<String, String> mymap)
            throws SQLException {
        Vector<SqlConstraint> vSqlConstraint;
        vSqlConstraint = sqlTable.getConstraints();
        final HashMap<String, String> hashConstraints = new HashMap<String, String>();
        String constraintskey = null;
        String constrintsValue = null;
        String constraintValues = null;
        ResultSet rs = null;
        if (getPrimaryKeyConstraint() != null) {
            for (int k = 0; k < getPrimaryKeyConstraint().getColumnNames().size(); k++) {
                constraintskey = "pk_" + getPrimaryKeyConstraint().getTableName();
                constrintsValue = "pk_" + getPrimaryKeyConstraint().getTableName() + "(";
                if (k > 0) {
                    constrintsValue += ", ";
                }
                constrintsValue += getPrimaryKeyConstraint().getColumnNames().get(k).toString();
                constrintsValue += ")";
                hashConstraints.put(constraintskey.toLowerCase(), constrintsValue);
            }
        }
        for (int j = 0; j < getuniqueKeyConstraint().size(); j++) {
            constraintValues = "(";
            constrintsValue = "uk_" + getuniqueKeyConstraint().get(j).getTableName() + "_";
            constraintskey = "uk_" + getuniqueKeyConstraint().get(j).getTableName() + "_";
            // hier wird die Constraint angegeben und als vergleichskriterium
            // herangezogen

            for (int l = 0; l < getuniqueKeyConstraint().get(j).getUniqueKeyColumns().length; l++) {
                if (l > 0) {
                    constraintskey += "_";
                    constraintValues += ",";
                }
                constraintValues += getuniqueKeyConstraint().get(j).getUniqueKeyColumns()[l].getLocalColumnName();
                constraintskey += getuniqueKeyConstraint().get(j).getUniqueKeyColumns()[l].getLocalColumnName();
            }
            constrintsValue = constraintskey + constraintValues + ")";
            hashConstraints.put(constraintskey.toLowerCase(), constrintsValue);
        }

        String constriants = "";
        for (final SqlConstraint sqlconstraint : vSqlConstraint) {
            rs = null;
            if (sqlconstraint.getName().toLowerCase().startsWith("pk")) {
                // rs =
                // connection.createStatement().executeQuery(ifexistsconstrain +
                // getPrimaryKeyConstraint().getName() + ifexistsconstrainend);
                if (!mymap.containsKey(getPrimaryKeyConstraint().getName())) {
                    // Hier findet der Vergleich der Namen statt
                    if (!sqlconstraint.getName().equalsIgnoreCase(getPrimaryKeyConstraint().getName())) {
                        // Wenn nicht gleich dann lösche die alte falls vorhanden
                        // und spiele die neue ein
                        dropConstraint(sqlconstraint.getTable().getName(), sqlconstraint.getName(), connection);
                        constriants += "\n";
                        constriants += getPrimaryKeyConstraint().getSQLCode();
                    } else {
                        constriants += "\n";
                        constriants += getPrimaryKeyConstraint().getSQLCode();
                    }
                } else {
                    this.primaryKeyConstraint = null;
                }
            } else if (sqlconstraint.getName().toLowerCase().startsWith("uk")) {
                boolean uk_found = false;
                for (int i = 0; i < getuniqueKeyConstraint().size(); i++) {
                    rs = connection.createStatement().executeQuery(
                            this.ifexistsconstrain + getuniqueKeyConstraint().get(i).getNameOfConstriant().toLowerCase() + this.ifexistsconstrainend);
                    if (rs.next()) {
                        // Hier findet der Vergleich der Namen statt

                        if (sqlconstraint.getName().equalsIgnoreCase((getuniqueKeyConstraint().get(i).getNameOfConstriant()))) {
                            getuniqueKeyConstraint().remove(i);
                            uk_found = true;
                            i = getuniqueKeyConstraint().size();
                        }
                    } else {
                        uk_found = true;
                    }
                }
                if (!uk_found) {
                    // Ausführen des Droppens der Constraints
                    // constraints +=
                    // dropConstraint(sqlconstraint.getTable().getName(),sqlconstraint.getName());
                    dropConstraint(sqlconstraint.getTable().getName(), sqlconstraint.getName(), connection);
                }
            }
        }
        // die restlichen
        constriants = getXMl_UK_PK_Constraints(constriants);
        if (constriants.equals("")) {
            return null;
        }
        return constriants;
    }

    /**
     * Übergeben werden TabellenName und Der Name der Constraints damit wird eine Drop Constraints SQL-Anweisung erstellt.
     *
     * @param tableName
     * @param constraintName
     * @return
     */
    private void dropConstraint(final String tableName, final String constraintName, final Connection connection) {
        // Dieser Constraints muss nicht m,ehr überprüft werden er ist bereits vorhanden
        String SQlCodeDrop = "IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE WHERE CONSTRAINT_NAME= '";
        SQlCodeDrop += constraintName;
        SQlCodeDrop += "') BEGIN ALTER TABLE dbo.[" + tableName + "] DROP CONSTRAINT [" + constraintName + "] END";
        try {
            connection.createStatement().execute(SQlCodeDrop);
        } catch (final SQLException e) {
            throw new RuntimeException("DropConstraint: " + e.getMessage());
        }
    }

    public String getXMl_UK_PK_Constraints(String constraints) {
        for (int i = 0; i < getuniqueKeyConstraint().size(); i++) {
            constraints += "\n";
            constraints += getuniqueKeyConstraint().get(i).getSQLCode();
        }
        if (getPrimaryKeyConstraint() != null) {
            constraints += "\n";
            constraints += getPrimaryKeyConstraint().getSQLCode();

        }
        return constraints;
    }

    /**
     * Diese Methode leitet den generierten Sql-Code weiter wenn es das Objekt gibt.
     */
    public String generateUpdateValues() {
        if (this.xmlvalues != null) {
            String sqlcode = "";
            // Einpflegen wenn keylong = identity ON
            if (isIdentity()) {
                sqlcode = sqlcode + this.setIdentitiy + " [" + getName() + "] ON \n";
                if (this.primaryKeyConstraint != null) {
                    sqlcode += this.xmlvalues.generateSqlCode(this.primaryKeyConstraint);
                } else {
                    sqlcode += this.xmlvalues.generateSqlCode();
                }
                sqlcode += this.setIdentitiy + " [" + getName() + "] OFF \n";
            } else {
                if (this.primaryKeyConstraint != null) {
                    sqlcode += this.xmlvalues.generateSqlCode(this.primaryKeyConstraint);
                } else {
                    sqlcode += this.xmlvalues.generateSqlCode();
                }
            }
            // Einpflegen wenn keylong = identity OFF
            return sqlcode;
        }
        return null;
    }

    public String getDBCollation() {
        return this.DBCollation;
    }

    public void setDBCollation(final String dBCollation) {
        if (dBCollation == null) {
            this.DBCollation = "null";
        } else {
            this.DBCollation = dBCollation;
        }
    }
}
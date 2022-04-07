/*
 * An XML document type.
 * Localname: table
 * Namespace: 
 * Java type: ch.minova.core.xml.tables.TableDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.xml.tables;


/**
 * A document containing one table(@) element.
 *
 * This is a complex type.
 */
public interface TableDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(TableDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("table96f5doctype");
    
    /**
     * Gets the "table" element
     */
    ch.minova.core.xml.tables.TableDocument.Table getTable();
    
    /**
     * Sets the "table" element
     */
    void setTable(ch.minova.core.xml.tables.TableDocument.Table table);
    
    /**
     * Appends and returns a new empty "table" element
     */
    ch.minova.core.xml.tables.TableDocument.Table addNewTable();
    
    /**
     * An XML table(@).
     *
     * This is a complex type.
     */
    public interface Table extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Table.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("table333felemtype");
        
        /**
         * Gets the "description" element
         */
        ch.minova.core.xml.tables.TableDocument.Table.Description getDescription();
        
        /**
         * Sets the "description" element
         */
        void setDescription(ch.minova.core.xml.tables.TableDocument.Table.Description description);
        
        /**
         * Appends and returns a new empty "description" element
         */
        ch.minova.core.xml.tables.TableDocument.Table.Description addNewDescription();
        
        /**
         * Gets the "docbook" element
         */
        ch.minova.core.xml.tables.TableDocument.Table.Docbook getDocbook();
        
        /**
         * Sets the "docbook" element
         */
        void setDocbook(ch.minova.core.xml.tables.TableDocument.Table.Docbook docbook);
        
        /**
         * Appends and returns a new empty "docbook" element
         */
        ch.minova.core.xml.tables.TableDocument.Table.Docbook addNewDocbook();
        
        /**
         * Gets array of all "column" elements
         */
        ch.minova.core.xml.tables.TableDocument.Table.Column[] getColumnArray();
        
        /**
         * Gets ith "column" element
         */
        ch.minova.core.xml.tables.TableDocument.Table.Column getColumnArray(int i);
        
        /**
         * Returns number of "column" element
         */
        int sizeOfColumnArray();
        
        /**
         * Sets array of all "column" element
         */
        void setColumnArray(ch.minova.core.xml.tables.TableDocument.Table.Column[] columnArray);
        
        /**
         * Sets ith "column" element
         */
        void setColumnArray(int i, ch.minova.core.xml.tables.TableDocument.Table.Column column);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "column" element
         */
        ch.minova.core.xml.tables.TableDocument.Table.Column insertNewColumn(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "column" element
         */
        ch.minova.core.xml.tables.TableDocument.Table.Column addNewColumn();
        
        /**
         * Removes the ith "column" element
         */
        void removeColumn(int i);
        
        /**
         * Gets the "primarykey" element
         */
        ch.minova.core.xml.tables.TableDocument.Table.Primarykey getPrimarykey();
        
        /**
         * True if has "primarykey" element
         */
        boolean isSetPrimarykey();
        
        /**
         * Sets the "primarykey" element
         */
        void setPrimarykey(ch.minova.core.xml.tables.TableDocument.Table.Primarykey primarykey);
        
        /**
         * Appends and returns a new empty "primarykey" element
         */
        ch.minova.core.xml.tables.TableDocument.Table.Primarykey addNewPrimarykey();
        
        /**
         * Unsets the "primarykey" element
         */
        void unsetPrimarykey();
        
        /**
         * Gets array of all "foreignkey" elements
         */
        ch.minova.core.xml.tables.TableDocument.Table.Foreignkey[] getForeignkeyArray();
        
        /**
         * Gets ith "foreignkey" element
         */
        ch.minova.core.xml.tables.TableDocument.Table.Foreignkey getForeignkeyArray(int i);
        
        /**
         * Returns number of "foreignkey" element
         */
        int sizeOfForeignkeyArray();
        
        /**
         * Sets array of all "foreignkey" element
         */
        void setForeignkeyArray(ch.minova.core.xml.tables.TableDocument.Table.Foreignkey[] foreignkeyArray);
        
        /**
         * Sets ith "foreignkey" element
         */
        void setForeignkeyArray(int i, ch.minova.core.xml.tables.TableDocument.Table.Foreignkey foreignkey);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "foreignkey" element
         */
        ch.minova.core.xml.tables.TableDocument.Table.Foreignkey insertNewForeignkey(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "foreignkey" element
         */
        ch.minova.core.xml.tables.TableDocument.Table.Foreignkey addNewForeignkey();
        
        /**
         * Removes the ith "foreignkey" element
         */
        void removeForeignkey(int i);
        
        /**
         * Gets array of all "uniquekey" elements
         */
        ch.minova.core.xml.tables.TableDocument.Table.Uniquekey[] getUniquekeyArray();
        
        /**
         * Gets ith "uniquekey" element
         */
        ch.minova.core.xml.tables.TableDocument.Table.Uniquekey getUniquekeyArray(int i);
        
        /**
         * Returns number of "uniquekey" element
         */
        int sizeOfUniquekeyArray();
        
        /**
         * Sets array of all "uniquekey" element
         */
        void setUniquekeyArray(ch.minova.core.xml.tables.TableDocument.Table.Uniquekey[] uniquekeyArray);
        
        /**
         * Sets ith "uniquekey" element
         */
        void setUniquekeyArray(int i, ch.minova.core.xml.tables.TableDocument.Table.Uniquekey uniquekey);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "uniquekey" element
         */
        ch.minova.core.xml.tables.TableDocument.Table.Uniquekey insertNewUniquekey(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "uniquekey" element
         */
        ch.minova.core.xml.tables.TableDocument.Table.Uniquekey addNewUniquekey();
        
        /**
         * Removes the ith "uniquekey" element
         */
        void removeUniquekey(int i);
        
        /**
         * Gets array of all "oldconstraint" elements
         */
        ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint[] getOldconstraintArray();
        
        /**
         * Gets ith "oldconstraint" element
         */
        ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint getOldconstraintArray(int i);
        
        /**
         * Returns number of "oldconstraint" element
         */
        int sizeOfOldconstraintArray();
        
        /**
         * Sets array of all "oldconstraint" element
         */
        void setOldconstraintArray(ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint[] oldconstraintArray);
        
        /**
         * Sets ith "oldconstraint" element
         */
        void setOldconstraintArray(int i, ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint oldconstraint);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "oldconstraint" element
         */
        ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint insertNewOldconstraint(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "oldconstraint" element
         */
        ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint addNewOldconstraint();
        
        /**
         * Removes the ith "oldconstraint" element
         */
        void removeOldconstraint(int i);
        
        /**
         * Gets the "exec-sql" element
         */
        ch.minova.core.xml.tables.TableDocument.Table.ExecSql getExecSql();
        
        /**
         * True if has "exec-sql" element
         */
        boolean isSetExecSql();
        
        /**
         * Sets the "exec-sql" element
         */
        void setExecSql(ch.minova.core.xml.tables.TableDocument.Table.ExecSql execSql);
        
        /**
         * Appends and returns a new empty "exec-sql" element
         */
        ch.minova.core.xml.tables.TableDocument.Table.ExecSql addNewExecSql();
        
        /**
         * Unsets the "exec-sql" element
         */
        void unsetExecSql();
        
        /**
         * Gets the "values" element
         */
        ch.minova.core.xml.tables.TableDocument.Table.Values getValues();
        
        /**
         * True if has "values" element
         */
        boolean isSetValues();
        
        /**
         * Sets the "values" element
         */
        void setValues(ch.minova.core.xml.tables.TableDocument.Table.Values values);
        
        /**
         * Appends and returns a new empty "values" element
         */
        ch.minova.core.xml.tables.TableDocument.Table.Values addNewValues();
        
        /**
         * Unsets the "values" element
         */
        void unsetValues();
        
        /**
         * Gets the "name" attribute
         */
        java.lang.String getName();
        
        /**
         * Gets (as xml) the "name" attribute
         */
        org.apache.xmlbeans.XmlName xgetName();
        
        /**
         * Sets the "name" attribute
         */
        void setName(java.lang.String name);
        
        /**
         * Sets (as xml) the "name" attribute
         */
        void xsetName(org.apache.xmlbeans.XmlName name);
        
        /**
         * Gets the "vendor" attribute
         */
        java.lang.String getVendor();
        
        /**
         * Gets (as xml) the "vendor" attribute
         */
        org.apache.xmlbeans.XmlString xgetVendor();
        
        /**
         * True if has "vendor" attribute
         */
        boolean isSetVendor();
        
        /**
         * Sets the "vendor" attribute
         */
        void setVendor(java.lang.String vendor);
        
        /**
         * Sets (as xml) the "vendor" attribute
         */
        void xsetVendor(org.apache.xmlbeans.XmlString vendor);
        
        /**
         * Unsets the "vendor" attribute
         */
        void unsetVendor();
        
        /**
         * Gets the "userlevel" attribute
         */
        java.lang.String getUserlevel();
        
        /**
         * Gets (as xml) the "userlevel" attribute
         */
        org.apache.xmlbeans.XmlString xgetUserlevel();
        
        /**
         * True if has "userlevel" attribute
         */
        boolean isSetUserlevel();
        
        /**
         * Sets the "userlevel" attribute
         */
        void setUserlevel(java.lang.String userlevel);
        
        /**
         * Sets (as xml) the "userlevel" attribute
         */
        void xsetUserlevel(org.apache.xmlbeans.XmlString userlevel);
        
        /**
         * Unsets the "userlevel" attribute
         */
        void unsetUserlevel();
        
        /**
         * Gets the "source" attribute
         */
        java.lang.String getSource();
        
        /**
         * Gets (as xml) the "source" attribute
         */
        org.apache.xmlbeans.XmlString xgetSource();
        
        /**
         * True if has "source" attribute
         */
        boolean isSetSource();
        
        /**
         * Sets the "source" attribute
         */
        void setSource(java.lang.String source);
        
        /**
         * Sets (as xml) the "source" attribute
         */
        void xsetSource(org.apache.xmlbeans.XmlString source);
        
        /**
         * Unsets the "source" attribute
         */
        void unsetSource();
        
        /**
         * Gets the "revision" attribute
         */
        java.lang.String getRevision();
        
        /**
         * Gets (as xml) the "revision" attribute
         */
        org.apache.xmlbeans.XmlString xgetRevision();
        
        /**
         * True if has "revision" attribute
         */
        boolean isSetRevision();
        
        /**
         * Sets the "revision" attribute
         */
        void setRevision(java.lang.String revision);
        
        /**
         * Sets (as xml) the "revision" attribute
         */
        void xsetRevision(org.apache.xmlbeans.XmlString revision);
        
        /**
         * Unsets the "revision" attribute
         */
        void unsetRevision();
        
        /**
         * An XML description(@).
         *
         * This is a complex type.
         */
        public interface Description extends org.apache.xmlbeans.XmlObject
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Description.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("description57b7elemtype");
            
            /**
             * Gets the "de" element
             */
            java.lang.String getDe();
            
            /**
             * Gets (as xml) the "de" element
             */
            org.apache.xmlbeans.XmlString xgetDe();
            
            /**
             * Sets the "de" element
             */
            void setDe(java.lang.String de);
            
            /**
             * Sets (as xml) the "de" element
             */
            void xsetDe(org.apache.xmlbeans.XmlString de);
            
            /**
             * Gets the "en" element
             */
            java.lang.String getEn();
            
            /**
             * Gets (as xml) the "en" element
             */
            org.apache.xmlbeans.XmlString xgetEn();
            
            /**
             * Sets the "en" element
             */
            void setEn(java.lang.String en);
            
            /**
             * Sets (as xml) the "en" element
             */
            void xsetEn(org.apache.xmlbeans.XmlString en);
            
            /**
             * A factory class with static methods for creating instances
             * of this type.
             */
            
            public static final class Factory
            {
                public static ch.minova.core.xml.tables.TableDocument.Table.Description newInstance() {
                  return (ch.minova.core.xml.tables.TableDocument.Table.Description) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static ch.minova.core.xml.tables.TableDocument.Table.Description newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (ch.minova.core.xml.tables.TableDocument.Table.Description) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * An XML docbook(@).
         *
         * This is a complex type.
         */
        public interface Docbook extends org.apache.xmlbeans.XmlObject
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Docbook.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("docbook49dcelemtype");
            
            /**
             * Gets the "de" element
             */
            ch.minova.core.xml.tables.DocbookCode getDe();
            
            /**
             * Sets the "de" element
             */
            void setDe(ch.minova.core.xml.tables.DocbookCode de);
            
            /**
             * Appends and returns a new empty "de" element
             */
            ch.minova.core.xml.tables.DocbookCode addNewDe();
            
            /**
             * Gets the "en" element
             */
            ch.minova.core.xml.tables.DocbookCode getEn();
            
            /**
             * Sets the "en" element
             */
            void setEn(ch.minova.core.xml.tables.DocbookCode en);
            
            /**
             * Appends and returns a new empty "en" element
             */
            ch.minova.core.xml.tables.DocbookCode addNewEn();
            
            /**
             * A factory class with static methods for creating instances
             * of this type.
             */
            
            public static final class Factory
            {
                public static ch.minova.core.xml.tables.TableDocument.Table.Docbook newInstance() {
                  return (ch.minova.core.xml.tables.TableDocument.Table.Docbook) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static ch.minova.core.xml.tables.TableDocument.Table.Docbook newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (ch.minova.core.xml.tables.TableDocument.Table.Docbook) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * An XML column(@).
         *
         * This is a complex type.
         */
        public interface Column extends org.apache.xmlbeans.XmlObject
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Column.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("column0721elemtype");
            
            /**
             * Gets the "integer" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Column.Integer getInteger();
            
            /**
             * True if has "integer" element
             */
            boolean isSetInteger();
            
            /**
             * Sets the "integer" element
             */
            void setInteger(ch.minova.core.xml.tables.TableDocument.Table.Column.Integer integer);
            
            /**
             * Appends and returns a new empty "integer" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Column.Integer addNewInteger();
            
            /**
             * Unsets the "integer" element
             */
            void unsetInteger();
            
            /**
             * Gets the "bigint" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Column.Bigint getBigint();
            
            /**
             * True if has "bigint" element
             */
            boolean isSetBigint();
            
            /**
             * Sets the "bigint" element
             */
            void setBigint(ch.minova.core.xml.tables.TableDocument.Table.Column.Bigint bigint);
            
            /**
             * Appends and returns a new empty "bigint" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Column.Bigint addNewBigint();
            
            /**
             * Unsets the "bigint" element
             */
            void unsetBigint();
            
            /**
             * Gets the "varchar" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Column.Varchar getVarchar();
            
            /**
             * True if has "varchar" element
             */
            boolean isSetVarchar();
            
            /**
             * Sets the "varchar" element
             */
            void setVarchar(ch.minova.core.xml.tables.TableDocument.Table.Column.Varchar varchar);
            
            /**
             * Appends and returns a new empty "varchar" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Column.Varchar addNewVarchar();
            
            /**
             * Unsets the "varchar" element
             */
            void unsetVarchar();
            
            /**
             * Gets the "datetime" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Column.Datetime getDatetime();
            
            /**
             * True if has "datetime" element
             */
            boolean isSetDatetime();
            
            /**
             * Sets the "datetime" element
             */
            void setDatetime(ch.minova.core.xml.tables.TableDocument.Table.Column.Datetime datetime);
            
            /**
             * Appends and returns a new empty "datetime" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Column.Datetime addNewDatetime();
            
            /**
             * Unsets the "datetime" element
             */
            void unsetDatetime();
            
            /**
             * Gets the "float" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Column.Float getFloat();
            
            /**
             * True if has "float" element
             */
            boolean isSetFloat();
            
            /**
             * Sets the "float" element
             */
            void setFloat(ch.minova.core.xml.tables.TableDocument.Table.Column.Float xfloat);
            
            /**
             * Appends and returns a new empty "float" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Column.Float addNewFloat();
            
            /**
             * Unsets the "float" element
             */
            void unsetFloat();
            
            /**
             * Gets the "boolean" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Column.Boolean getBoolean();
            
            /**
             * True if has "boolean" element
             */
            boolean isSetBoolean();
            
            /**
             * Sets the "boolean" element
             */
            void setBoolean(ch.minova.core.xml.tables.TableDocument.Table.Column.Boolean xboolean);
            
            /**
             * Appends and returns a new empty "boolean" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Column.Boolean addNewBoolean();
            
            /**
             * Unsets the "boolean" element
             */
            void unsetBoolean();
            
            /**
             * Gets the "money" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Column.Money getMoney();
            
            /**
             * True if has "money" element
             */
            boolean isSetMoney();
            
            /**
             * Sets the "money" element
             */
            void setMoney(ch.minova.core.xml.tables.TableDocument.Table.Column.Money money);
            
            /**
             * Appends and returns a new empty "money" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Column.Money addNewMoney();
            
            /**
             * Unsets the "money" element
             */
            void unsetMoney();
            
            /**
             * Gets array of all "old-name" elements
             */
            java.lang.String[] getOldNameArray();
            
            /**
             * Gets ith "old-name" element
             */
            java.lang.String getOldNameArray(int i);
            
            /**
             * Gets (as xml) array of all "old-name" elements
             */
            org.apache.xmlbeans.XmlID[] xgetOldNameArray();
            
            /**
             * Gets (as xml) ith "old-name" element
             */
            org.apache.xmlbeans.XmlID xgetOldNameArray(int i);
            
            /**
             * Returns number of "old-name" element
             */
            int sizeOfOldNameArray();
            
            /**
             * Sets array of all "old-name" element
             */
            void setOldNameArray(java.lang.String[] oldNameArray);
            
            /**
             * Sets ith "old-name" element
             */
            void setOldNameArray(int i, java.lang.String oldName);
            
            /**
             * Sets (as xml) array of all "old-name" element
             */
            void xsetOldNameArray(org.apache.xmlbeans.XmlID[] oldNameArray);
            
            /**
             * Sets (as xml) ith "old-name" element
             */
            void xsetOldNameArray(int i, org.apache.xmlbeans.XmlID oldName);
            
            /**
             * Inserts the value as the ith "old-name" element
             */
            void insertOldName(int i, java.lang.String oldName);
            
            /**
             * Appends the value as the last "old-name" element
             */
            void addOldName(java.lang.String oldName);
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "old-name" element
             */
            org.apache.xmlbeans.XmlID insertNewOldName(int i);
            
            /**
             * Appends and returns a new empty value (as xml) as the last "old-name" element
             */
            org.apache.xmlbeans.XmlID addNewOldName();
            
            /**
             * Removes the ith "old-name" element
             */
            void removeOldName(int i);
            
            /**
             * Gets the "description" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Column.Description getDescription();
            
            /**
             * Sets the "description" element
             */
            void setDescription(ch.minova.core.xml.tables.TableDocument.Table.Column.Description description);
            
            /**
             * Appends and returns a new empty "description" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Column.Description addNewDescription();
            
            /**
             * Gets the "docbook" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Column.Docbook getDocbook();
            
            /**
             * Sets the "docbook" element
             */
            void setDocbook(ch.minova.core.xml.tables.TableDocument.Table.Column.Docbook docbook);
            
            /**
             * Appends and returns a new empty "docbook" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Column.Docbook addNewDocbook();
            
            /**
             * Gets the "default" attribute
             */
            java.lang.String getDefault();
            
            /**
             * Gets (as xml) the "default" attribute
             */
            org.apache.xmlbeans.XmlString xgetDefault();
            
            /**
             * True if has "default" attribute
             */
            boolean isSetDefault();
            
            /**
             * Sets the "default" attribute
             */
            void setDefault(java.lang.String xdefault);
            
            /**
             * Sets (as xml) the "default" attribute
             */
            void xsetDefault(org.apache.xmlbeans.XmlString xdefault);
            
            /**
             * Unsets the "default" attribute
             */
            void unsetDefault();
            
            /**
             * Gets the "name" attribute
             */
            java.lang.String getName();
            
            /**
             * Gets (as xml) the "name" attribute
             */
            org.apache.xmlbeans.XmlID xgetName();
            
            /**
             * Sets the "name" attribute
             */
            void setName(java.lang.String name);
            
            /**
             * Sets (as xml) the "name" attribute
             */
            void xsetName(org.apache.xmlbeans.XmlID name);
            
            /**
             * Gets the "userlevel" attribute
             */
            java.lang.String getUserlevel();
            
            /**
             * Gets (as xml) the "userlevel" attribute
             */
            org.apache.xmlbeans.XmlString xgetUserlevel();
            
            /**
             * True if has "userlevel" attribute
             */
            boolean isSetUserlevel();
            
            /**
             * Sets the "userlevel" attribute
             */
            void setUserlevel(java.lang.String userlevel);
            
            /**
             * Sets (as xml) the "userlevel" attribute
             */
            void xsetUserlevel(org.apache.xmlbeans.XmlString userlevel);
            
            /**
             * Unsets the "userlevel" attribute
             */
            void unsetUserlevel();
            
            /**
             * Gets the "vendor" attribute
             */
            java.lang.String getVendor();
            
            /**
             * Gets (as xml) the "vendor" attribute
             */
            org.apache.xmlbeans.XmlString xgetVendor();
            
            /**
             * True if has "vendor" attribute
             */
            boolean isSetVendor();
            
            /**
             * Sets the "vendor" attribute
             */
            void setVendor(java.lang.String vendor);
            
            /**
             * Sets (as xml) the "vendor" attribute
             */
            void xsetVendor(org.apache.xmlbeans.XmlString vendor);
            
            /**
             * Unsets the "vendor" attribute
             */
            void unsetVendor();
            
            /**
             * An XML integer(@).
             *
             * This is a complex type.
             */
            public interface Integer extends org.apache.xmlbeans.XmlObject
            {
                public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                    org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Integer.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("integerfea7elemtype");
                
                /**
                 * Gets the "nullable" attribute
                 */
                boolean getNullable();
                
                /**
                 * Gets (as xml) the "nullable" attribute
                 */
                org.apache.xmlbeans.XmlBoolean xgetNullable();
                
                /**
                 * True if has "nullable" attribute
                 */
                boolean isSetNullable();
                
                /**
                 * Sets the "nullable" attribute
                 */
                void setNullable(boolean nullable);
                
                /**
                 * Sets (as xml) the "nullable" attribute
                 */
                void xsetNullable(org.apache.xmlbeans.XmlBoolean nullable);
                
                /**
                 * Unsets the "nullable" attribute
                 */
                void unsetNullable();
                
                /**
                 * Gets the "identity" attribute
                 */
                boolean getIdentity();
                
                /**
                 * Gets (as xml) the "identity" attribute
                 */
                org.apache.xmlbeans.XmlBoolean xgetIdentity();
                
                /**
                 * True if has "identity" attribute
                 */
                boolean isSetIdentity();
                
                /**
                 * Sets the "identity" attribute
                 */
                void setIdentity(boolean identity);
                
                /**
                 * Sets (as xml) the "identity" attribute
                 */
                void xsetIdentity(org.apache.xmlbeans.XmlBoolean identity);
                
                /**
                 * Unsets the "identity" attribute
                 */
                void unsetIdentity();
                
                /**
                 * A factory class with static methods for creating instances
                 * of this type.
                 */
                
                public static final class Factory
                {
                    public static ch.minova.core.xml.tables.TableDocument.Table.Column.Integer newInstance() {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Column.Integer) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                    
                    public static ch.minova.core.xml.tables.TableDocument.Table.Column.Integer newInstance(org.apache.xmlbeans.XmlOptions options) {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Column.Integer) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                    
                    private Factory() { } // No instance of this class allowed
                }
            }
            
            /**
             * An XML bigint(@).
             *
             * This is a complex type.
             */
            public interface Bigint extends org.apache.xmlbeans.XmlObject
            {
                public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                    org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Bigint.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("bigintd806elemtype");
                
                /**
                 * Gets the "nullable" attribute
                 */
                boolean getNullable();
                
                /**
                 * Gets (as xml) the "nullable" attribute
                 */
                org.apache.xmlbeans.XmlBoolean xgetNullable();
                
                /**
                 * True if has "nullable" attribute
                 */
                boolean isSetNullable();
                
                /**
                 * Sets the "nullable" attribute
                 */
                void setNullable(boolean nullable);
                
                /**
                 * Sets (as xml) the "nullable" attribute
                 */
                void xsetNullable(org.apache.xmlbeans.XmlBoolean nullable);
                
                /**
                 * Unsets the "nullable" attribute
                 */
                void unsetNullable();
                
                /**
                 * A factory class with static methods for creating instances
                 * of this type.
                 */
                
                public static final class Factory
                {
                    public static ch.minova.core.xml.tables.TableDocument.Table.Column.Bigint newInstance() {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Column.Bigint) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                    
                    public static ch.minova.core.xml.tables.TableDocument.Table.Column.Bigint newInstance(org.apache.xmlbeans.XmlOptions options) {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Column.Bigint) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                    
                    private Factory() { } // No instance of this class allowed
                }
            }
            
            /**
             * An XML varchar(@).
             *
             * This is a complex type.
             */
            public interface Varchar extends org.apache.xmlbeans.XmlObject
            {
                public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                    org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Varchar.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("varcharb5c8elemtype");
                
                /**
                 * Gets the "length" attribute
                 */
                java.math.BigInteger getLength();
                
                /**
                 * Gets (as xml) the "length" attribute
                 */
                org.apache.xmlbeans.XmlInteger xgetLength();
                
                /**
                 * Sets the "length" attribute
                 */
                void setLength(java.math.BigInteger length);
                
                /**
                 * Sets (as xml) the "length" attribute
                 */
                void xsetLength(org.apache.xmlbeans.XmlInteger length);
                
                /**
                 * Gets the "nullable" attribute
                 */
                boolean getNullable();
                
                /**
                 * Gets (as xml) the "nullable" attribute
                 */
                org.apache.xmlbeans.XmlBoolean xgetNullable();
                
                /**
                 * True if has "nullable" attribute
                 */
                boolean isSetNullable();
                
                /**
                 * Sets the "nullable" attribute
                 */
                void setNullable(boolean nullable);
                
                /**
                 * Sets (as xml) the "nullable" attribute
                 */
                void xsetNullable(org.apache.xmlbeans.XmlBoolean nullable);
                
                /**
                 * Unsets the "nullable" attribute
                 */
                void unsetNullable();
                
                /**
                 * A factory class with static methods for creating instances
                 * of this type.
                 */
                
                public static final class Factory
                {
                    public static ch.minova.core.xml.tables.TableDocument.Table.Column.Varchar newInstance() {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Column.Varchar) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                    
                    public static ch.minova.core.xml.tables.TableDocument.Table.Column.Varchar newInstance(org.apache.xmlbeans.XmlOptions options) {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Column.Varchar) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                    
                    private Factory() { } // No instance of this class allowed
                }
            }
            
            /**
             * An XML datetime(@).
             *
             * This is a complex type.
             */
            public interface Datetime extends org.apache.xmlbeans.XmlObject
            {
                public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                    org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Datetime.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("datetime4efaelemtype");
                
                /**
                 * Gets the "nullable" attribute
                 */
                boolean getNullable();
                
                /**
                 * Gets (as xml) the "nullable" attribute
                 */
                org.apache.xmlbeans.XmlBoolean xgetNullable();
                
                /**
                 * True if has "nullable" attribute
                 */
                boolean isSetNullable();
                
                /**
                 * Sets the "nullable" attribute
                 */
                void setNullable(boolean nullable);
                
                /**
                 * Sets (as xml) the "nullable" attribute
                 */
                void xsetNullable(org.apache.xmlbeans.XmlBoolean nullable);
                
                /**
                 * Unsets the "nullable" attribute
                 */
                void unsetNullable();
                
                /**
                 * A factory class with static methods for creating instances
                 * of this type.
                 */
                
                public static final class Factory
                {
                    public static ch.minova.core.xml.tables.TableDocument.Table.Column.Datetime newInstance() {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Column.Datetime) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                    
                    public static ch.minova.core.xml.tables.TableDocument.Table.Column.Datetime newInstance(org.apache.xmlbeans.XmlOptions options) {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Column.Datetime) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                    
                    private Factory() { } // No instance of this class allowed
                }
            }
            
            /**
             * An XML float(@).
             *
             * This is a complex type.
             */
            public interface Float extends org.apache.xmlbeans.XmlObject
            {
                public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                    org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Float.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("float3d49elemtype");
                
                /**
                 * Gets the "nullable" attribute
                 */
                boolean getNullable();
                
                /**
                 * Gets (as xml) the "nullable" attribute
                 */
                org.apache.xmlbeans.XmlBoolean xgetNullable();
                
                /**
                 * True if has "nullable" attribute
                 */
                boolean isSetNullable();
                
                /**
                 * Sets the "nullable" attribute
                 */
                void setNullable(boolean nullable);
                
                /**
                 * Sets (as xml) the "nullable" attribute
                 */
                void xsetNullable(org.apache.xmlbeans.XmlBoolean nullable);
                
                /**
                 * Unsets the "nullable" attribute
                 */
                void unsetNullable();
                
                /**
                 * Gets the "decimals" attribute
                 */
                int getDecimals();
                
                /**
                 * Gets (as xml) the "decimals" attribute
                 */
                ch.minova.core.xml.tables.TableDocument.Table.Column.Float.Decimals xgetDecimals();
                
                /**
                 * Sets the "decimals" attribute
                 */
                void setDecimals(int decimals);
                
                /**
                 * Sets (as xml) the "decimals" attribute
                 */
                void xsetDecimals(ch.minova.core.xml.tables.TableDocument.Table.Column.Float.Decimals decimals);
                
                /**
                 * An XML decimals(@).
                 *
                 * This is an atomic type that is a restriction of ch.minova.core.xml.tables.TableDocument$Table$Column$Float$Decimals.
                 */
                public interface Decimals extends org.apache.xmlbeans.XmlInteger
                {
                    int getIntValue();
                    void setIntValue(int i);
                    /** @deprecated */
                    int intValue();
                    /** @deprecated */
                    void set(int i);
                    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                      org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Decimals.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("decimals2e27attrtype");
                    
                    /**
                     * A factory class with static methods for creating instances
                     * of this type.
                     */
                    
                    public static final class Factory
                    {
                      public static ch.minova.core.xml.tables.TableDocument.Table.Column.Float.Decimals newValue(java.lang.Object obj) {
                        return (ch.minova.core.xml.tables.TableDocument.Table.Column.Float.Decimals) type.newValue( obj ); }
                      
                      public static ch.minova.core.xml.tables.TableDocument.Table.Column.Float.Decimals newInstance() {
                        return (ch.minova.core.xml.tables.TableDocument.Table.Column.Float.Decimals) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                      
                      public static ch.minova.core.xml.tables.TableDocument.Table.Column.Float.Decimals newInstance(org.apache.xmlbeans.XmlOptions options) {
                        return (ch.minova.core.xml.tables.TableDocument.Table.Column.Float.Decimals) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                      
                      private Factory() { } // No instance of this class allowed
                    }
                }
                
                /**
                 * A factory class with static methods for creating instances
                 * of this type.
                 */
                
                public static final class Factory
                {
                    public static ch.minova.core.xml.tables.TableDocument.Table.Column.Float newInstance() {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Column.Float) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                    
                    public static ch.minova.core.xml.tables.TableDocument.Table.Column.Float newInstance(org.apache.xmlbeans.XmlOptions options) {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Column.Float) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                    
                    private Factory() { } // No instance of this class allowed
                }
            }
            
            /**
             * An XML boolean(@).
             *
             * This is a complex type.
             */
            public interface Boolean extends org.apache.xmlbeans.XmlObject
            {
                public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                    org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Boolean.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("boolean4efdelemtype");
                
                /**
                 * Gets the "nullable" attribute
                 */
                boolean getNullable();
                
                /**
                 * Gets (as xml) the "nullable" attribute
                 */
                org.apache.xmlbeans.XmlBoolean xgetNullable();
                
                /**
                 * True if has "nullable" attribute
                 */
                boolean isSetNullable();
                
                /**
                 * Sets the "nullable" attribute
                 */
                void setNullable(boolean nullable);
                
                /**
                 * Sets (as xml) the "nullable" attribute
                 */
                void xsetNullable(org.apache.xmlbeans.XmlBoolean nullable);
                
                /**
                 * Unsets the "nullable" attribute
                 */
                void unsetNullable();
                
                /**
                 * A factory class with static methods for creating instances
                 * of this type.
                 */
                
                public static final class Factory
                {
                    public static ch.minova.core.xml.tables.TableDocument.Table.Column.Boolean newInstance() {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Column.Boolean) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                    
                    public static ch.minova.core.xml.tables.TableDocument.Table.Column.Boolean newInstance(org.apache.xmlbeans.XmlOptions options) {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Column.Boolean) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                    
                    private Factory() { } // No instance of this class allowed
                }
            }
            
            /**
             * An XML money(@).
             *
             * This is a complex type.
             */
            public interface Money extends org.apache.xmlbeans.XmlObject
            {
                public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                    org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Money.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("money7765elemtype");
                
                /**
                 * Gets the "nullable" attribute
                 */
                boolean getNullable();
                
                /**
                 * Gets (as xml) the "nullable" attribute
                 */
                org.apache.xmlbeans.XmlBoolean xgetNullable();
                
                /**
                 * True if has "nullable" attribute
                 */
                boolean isSetNullable();
                
                /**
                 * Sets the "nullable" attribute
                 */
                void setNullable(boolean nullable);
                
                /**
                 * Sets (as xml) the "nullable" attribute
                 */
                void xsetNullable(org.apache.xmlbeans.XmlBoolean nullable);
                
                /**
                 * Unsets the "nullable" attribute
                 */
                void unsetNullable();
                
                /**
                 * A factory class with static methods for creating instances
                 * of this type.
                 */
                
                public static final class Factory
                {
                    public static ch.minova.core.xml.tables.TableDocument.Table.Column.Money newInstance() {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Column.Money) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                    
                    public static ch.minova.core.xml.tables.TableDocument.Table.Column.Money newInstance(org.apache.xmlbeans.XmlOptions options) {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Column.Money) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                    
                    private Factory() { } // No instance of this class allowed
                }
            }
            
            /**
             * An XML description(@).
             *
             * This is a complex type.
             */
            public interface Description extends org.apache.xmlbeans.XmlObject
            {
                public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                    org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Description.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("description69a9elemtype");
                
                /**
                 * Gets the "de" element
                 */
                java.lang.String getDe();
                
                /**
                 * Gets (as xml) the "de" element
                 */
                org.apache.xmlbeans.XmlString xgetDe();
                
                /**
                 * Sets the "de" element
                 */
                void setDe(java.lang.String de);
                
                /**
                 * Sets (as xml) the "de" element
                 */
                void xsetDe(org.apache.xmlbeans.XmlString de);
                
                /**
                 * Gets the "en" element
                 */
                java.lang.String getEn();
                
                /**
                 * Gets (as xml) the "en" element
                 */
                org.apache.xmlbeans.XmlString xgetEn();
                
                /**
                 * Sets the "en" element
                 */
                void setEn(java.lang.String en);
                
                /**
                 * Sets (as xml) the "en" element
                 */
                void xsetEn(org.apache.xmlbeans.XmlString en);
                
                /**
                 * A factory class with static methods for creating instances
                 * of this type.
                 */
                
                public static final class Factory
                {
                    public static ch.minova.core.xml.tables.TableDocument.Table.Column.Description newInstance() {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Column.Description) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                    
                    public static ch.minova.core.xml.tables.TableDocument.Table.Column.Description newInstance(org.apache.xmlbeans.XmlOptions options) {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Column.Description) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                    
                    private Factory() { } // No instance of this class allowed
                }
            }
            
            /**
             * An XML docbook(@).
             *
             * This is a complex type.
             */
            public interface Docbook extends org.apache.xmlbeans.XmlObject
            {
                public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                    org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Docbook.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("docbook1124elemtype");
                
                /**
                 * Gets the "de" element
                 */
                ch.minova.core.xml.tables.DocbookCode getDe();
                
                /**
                 * Sets the "de" element
                 */
                void setDe(ch.minova.core.xml.tables.DocbookCode de);
                
                /**
                 * Appends and returns a new empty "de" element
                 */
                ch.minova.core.xml.tables.DocbookCode addNewDe();
                
                /**
                 * Gets the "en" element
                 */
                ch.minova.core.xml.tables.DocbookCode getEn();
                
                /**
                 * Sets the "en" element
                 */
                void setEn(ch.minova.core.xml.tables.DocbookCode en);
                
                /**
                 * Appends and returns a new empty "en" element
                 */
                ch.minova.core.xml.tables.DocbookCode addNewEn();
                
                /**
                 * A factory class with static methods for creating instances
                 * of this type.
                 */
                
                public static final class Factory
                {
                    public static ch.minova.core.xml.tables.TableDocument.Table.Column.Docbook newInstance() {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Column.Docbook) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                    
                    public static ch.minova.core.xml.tables.TableDocument.Table.Column.Docbook newInstance(org.apache.xmlbeans.XmlOptions options) {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Column.Docbook) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                    
                    private Factory() { } // No instance of this class allowed
                }
            }
            
            /**
             * A factory class with static methods for creating instances
             * of this type.
             */
            
            public static final class Factory
            {
                public static ch.minova.core.xml.tables.TableDocument.Table.Column newInstance() {
                  return (ch.minova.core.xml.tables.TableDocument.Table.Column) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static ch.minova.core.xml.tables.TableDocument.Table.Column newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (ch.minova.core.xml.tables.TableDocument.Table.Column) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * An XML primarykey(@).
         *
         * This is a complex type.
         */
        public interface Primarykey extends org.apache.xmlbeans.XmlObject
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Primarykey.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("primarykeyfae8elemtype");
            
            /**
             * Gets array of all "column" elements
             */
            java.lang.String[] getColumnArray();
            
            /**
             * Gets ith "column" element
             */
            java.lang.String getColumnArray(int i);
            
            /**
             * Gets (as xml) array of all "column" elements
             */
            org.apache.xmlbeans.XmlIDREF[] xgetColumnArray();
            
            /**
             * Gets (as xml) ith "column" element
             */
            org.apache.xmlbeans.XmlIDREF xgetColumnArray(int i);
            
            /**
             * Returns number of "column" element
             */
            int sizeOfColumnArray();
            
            /**
             * Sets array of all "column" element
             */
            void setColumnArray(java.lang.String[] columnArray);
            
            /**
             * Sets ith "column" element
             */
            void setColumnArray(int i, java.lang.String column);
            
            /**
             * Sets (as xml) array of all "column" element
             */
            void xsetColumnArray(org.apache.xmlbeans.XmlIDREF[] columnArray);
            
            /**
             * Sets (as xml) ith "column" element
             */
            void xsetColumnArray(int i, org.apache.xmlbeans.XmlIDREF column);
            
            /**
             * Inserts the value as the ith "column" element
             */
            void insertColumn(int i, java.lang.String column);
            
            /**
             * Appends the value as the last "column" element
             */
            void addColumn(java.lang.String column);
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "column" element
             */
            org.apache.xmlbeans.XmlIDREF insertNewColumn(int i);
            
            /**
             * Appends and returns a new empty value (as xml) as the last "column" element
             */
            org.apache.xmlbeans.XmlIDREF addNewColumn();
            
            /**
             * Removes the ith "column" element
             */
            void removeColumn(int i);
            
            /**
             * Gets the "description" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Description getDescription();
            
            /**
             * Sets the "description" element
             */
            void setDescription(ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Description description);
            
            /**
             * Appends and returns a new empty "description" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Description addNewDescription();
            
            /**
             * Gets the "docbook" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Docbook getDocbook();
            
            /**
             * Sets the "docbook" element
             */
            void setDocbook(ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Docbook docbook);
            
            /**
             * Appends and returns a new empty "docbook" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Docbook addNewDocbook();
            
            /**
             * An XML description(@).
             *
             * This is a complex type.
             */
            public interface Description extends org.apache.xmlbeans.XmlObject
            {
                public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                    org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Description.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("descriptiond970elemtype");
                
                /**
                 * Gets the "de" element
                 */
                java.lang.String getDe();
                
                /**
                 * Gets (as xml) the "de" element
                 */
                org.apache.xmlbeans.XmlString xgetDe();
                
                /**
                 * Sets the "de" element
                 */
                void setDe(java.lang.String de);
                
                /**
                 * Sets (as xml) the "de" element
                 */
                void xsetDe(org.apache.xmlbeans.XmlString de);
                
                /**
                 * Gets the "en" element
                 */
                java.lang.String getEn();
                
                /**
                 * Gets (as xml) the "en" element
                 */
                org.apache.xmlbeans.XmlString xgetEn();
                
                /**
                 * Sets the "en" element
                 */
                void setEn(java.lang.String en);
                
                /**
                 * Sets (as xml) the "en" element
                 */
                void xsetEn(org.apache.xmlbeans.XmlString en);
                
                /**
                 * A factory class with static methods for creating instances
                 * of this type.
                 */
                
                public static final class Factory
                {
                    public static ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Description newInstance() {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Description) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                    
                    public static ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Description newInstance(org.apache.xmlbeans.XmlOptions options) {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Description) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                    
                    private Factory() { } // No instance of this class allowed
                }
            }
            
            /**
             * An XML docbook(@).
             *
             * This is a complex type.
             */
            public interface Docbook extends org.apache.xmlbeans.XmlObject
            {
                public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                    org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Docbook.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("docbook4b6belemtype");
                
                /**
                 * Gets the "de" element
                 */
                ch.minova.core.xml.tables.DocbookCode getDe();
                
                /**
                 * Sets the "de" element
                 */
                void setDe(ch.minova.core.xml.tables.DocbookCode de);
                
                /**
                 * Appends and returns a new empty "de" element
                 */
                ch.minova.core.xml.tables.DocbookCode addNewDe();
                
                /**
                 * Gets the "en" element
                 */
                ch.minova.core.xml.tables.DocbookCode getEn();
                
                /**
                 * Sets the "en" element
                 */
                void setEn(ch.minova.core.xml.tables.DocbookCode en);
                
                /**
                 * Appends and returns a new empty "en" element
                 */
                ch.minova.core.xml.tables.DocbookCode addNewEn();
                
                /**
                 * A factory class with static methods for creating instances
                 * of this type.
                 */
                
                public static final class Factory
                {
                    public static ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Docbook newInstance() {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Docbook) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                    
                    public static ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Docbook newInstance(org.apache.xmlbeans.XmlOptions options) {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Docbook) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                    
                    private Factory() { } // No instance of this class allowed
                }
            }
            
            /**
             * A factory class with static methods for creating instances
             * of this type.
             */
            
            public static final class Factory
            {
                public static ch.minova.core.xml.tables.TableDocument.Table.Primarykey newInstance() {
                  return (ch.minova.core.xml.tables.TableDocument.Table.Primarykey) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static ch.minova.core.xml.tables.TableDocument.Table.Primarykey newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (ch.minova.core.xml.tables.TableDocument.Table.Primarykey) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * An XML foreignkey(@).
         *
         * This is a complex type.
         */
        public interface Foreignkey extends org.apache.xmlbeans.XmlObject
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Foreignkey.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("foreignkey22d6elemtype");
            
            /**
             * Gets array of all "column" elements
             */
            ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column[] getColumnArray();
            
            /**
             * Gets ith "column" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column getColumnArray(int i);
            
            /**
             * Returns number of "column" element
             */
            int sizeOfColumnArray();
            
            /**
             * Sets array of all "column" element
             */
            void setColumnArray(ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column[] columnArray);
            
            /**
             * Sets ith "column" element
             */
            void setColumnArray(int i, ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column column);
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "column" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column insertNewColumn(int i);
            
            /**
             * Appends and returns a new empty value (as xml) as the last "column" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column addNewColumn();
            
            /**
             * Removes the ith "column" element
             */
            void removeColumn(int i);
            
            /**
             * Gets the "description" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Description getDescription();
            
            /**
             * Sets the "description" element
             */
            void setDescription(ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Description description);
            
            /**
             * Appends and returns a new empty "description" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Description addNewDescription();
            
            /**
             * Gets the "docbook" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Docbook getDocbook();
            
            /**
             * Sets the "docbook" element
             */
            void setDocbook(ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Docbook docbook);
            
            /**
             * Appends and returns a new empty "docbook" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Docbook addNewDocbook();
            
            /**
             * Gets the "refid" attribute
             */
            org.apache.xmlbeans.XmlAnySimpleType getRefid();
            
            /**
             * Sets the "refid" attribute
             */
            void setRefid(org.apache.xmlbeans.XmlAnySimpleType refid);
            
            /**
             * Appends and returns a new empty "refid" attribute
             */
            org.apache.xmlbeans.XmlAnySimpleType addNewRefid();
            
            /**
             * Gets the "table" attribute
             */
            org.apache.xmlbeans.XmlAnySimpleType getTable();
            
            /**
             * Sets the "table" attribute
             */
            void setTable(org.apache.xmlbeans.XmlAnySimpleType table);
            
            /**
             * Appends and returns a new empty "table" attribute
             */
            org.apache.xmlbeans.XmlAnySimpleType addNewTable();
            
            /**
             * An XML column(@).
             *
             * This is a complex type.
             */
            public interface Column extends org.apache.xmlbeans.XmlObject
            {
                public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                    org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Column.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("column2e34elemtype");
                
                /**
                 * Gets the "name" attribute
                 */
                java.lang.String getName();
                
                /**
                 * Gets (as xml) the "name" attribute
                 */
                org.apache.xmlbeans.XmlString xgetName();
                
                /**
                 * Sets the "name" attribute
                 */
                void setName(java.lang.String name);
                
                /**
                 * Sets (as xml) the "name" attribute
                 */
                void xsetName(org.apache.xmlbeans.XmlString name);
                
                /**
                 * Gets the "refid" attribute
                 */
                java.lang.String getRefid();
                
                /**
                 * Gets (as xml) the "refid" attribute
                 */
                org.apache.xmlbeans.XmlString xgetRefid();
                
                /**
                 * True if has "refid" attribute
                 */
                boolean isSetRefid();
                
                /**
                 * Sets the "refid" attribute
                 */
                void setRefid(java.lang.String refid);
                
                /**
                 * Sets (as xml) the "refid" attribute
                 */
                void xsetRefid(org.apache.xmlbeans.XmlString refid);
                
                /**
                 * Unsets the "refid" attribute
                 */
                void unsetRefid();
                
                /**
                 * A factory class with static methods for creating instances
                 * of this type.
                 */
                
                public static final class Factory
                {
                    public static ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column newInstance() {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                    
                    public static ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column newInstance(org.apache.xmlbeans.XmlOptions options) {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                    
                    private Factory() { } // No instance of this class allowed
                }
            }
            
            /**
             * An XML description(@).
             *
             * This is a complex type.
             */
            public interface Description extends org.apache.xmlbeans.XmlObject
            {
                public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                    org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Description.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("description015eelemtype");
                
                /**
                 * Gets the "de" element
                 */
                java.lang.String getDe();
                
                /**
                 * Gets (as xml) the "de" element
                 */
                org.apache.xmlbeans.XmlString xgetDe();
                
                /**
                 * Sets the "de" element
                 */
                void setDe(java.lang.String de);
                
                /**
                 * Sets (as xml) the "de" element
                 */
                void xsetDe(org.apache.xmlbeans.XmlString de);
                
                /**
                 * Gets the "en" element
                 */
                java.lang.String getEn();
                
                /**
                 * Gets (as xml) the "en" element
                 */
                org.apache.xmlbeans.XmlString xgetEn();
                
                /**
                 * Sets the "en" element
                 */
                void setEn(java.lang.String en);
                
                /**
                 * Sets (as xml) the "en" element
                 */
                void xsetEn(org.apache.xmlbeans.XmlString en);
                
                /**
                 * A factory class with static methods for creating instances
                 * of this type.
                 */
                
                public static final class Factory
                {
                    public static ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Description newInstance() {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Description) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                    
                    public static ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Description newInstance(org.apache.xmlbeans.XmlOptions options) {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Description) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                    
                    private Factory() { } // No instance of this class allowed
                }
            }
            
            /**
             * An XML docbook(@).
             *
             * This is a complex type.
             */
            public interface Docbook extends org.apache.xmlbeans.XmlObject
            {
                public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                    org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Docbook.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("docbook7359elemtype");
                
                /**
                 * Gets the "de" element
                 */
                ch.minova.core.xml.tables.DocbookCode getDe();
                
                /**
                 * Sets the "de" element
                 */
                void setDe(ch.minova.core.xml.tables.DocbookCode de);
                
                /**
                 * Appends and returns a new empty "de" element
                 */
                ch.minova.core.xml.tables.DocbookCode addNewDe();
                
                /**
                 * Gets the "en" element
                 */
                ch.minova.core.xml.tables.DocbookCode getEn();
                
                /**
                 * Sets the "en" element
                 */
                void setEn(ch.minova.core.xml.tables.DocbookCode en);
                
                /**
                 * Appends and returns a new empty "en" element
                 */
                ch.minova.core.xml.tables.DocbookCode addNewEn();
                
                /**
                 * A factory class with static methods for creating instances
                 * of this type.
                 */
                
                public static final class Factory
                {
                    public static ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Docbook newInstance() {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Docbook) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                    
                    public static ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Docbook newInstance(org.apache.xmlbeans.XmlOptions options) {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Docbook) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                    
                    private Factory() { } // No instance of this class allowed
                }
            }
            
            /**
             * A factory class with static methods for creating instances
             * of this type.
             */
            
            public static final class Factory
            {
                public static ch.minova.core.xml.tables.TableDocument.Table.Foreignkey newInstance() {
                  return (ch.minova.core.xml.tables.TableDocument.Table.Foreignkey) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static ch.minova.core.xml.tables.TableDocument.Table.Foreignkey newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (ch.minova.core.xml.tables.TableDocument.Table.Foreignkey) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * An XML uniquekey(@).
         *
         * This is a complex type.
         */
        public interface Uniquekey extends org.apache.xmlbeans.XmlObject
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Uniquekey.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("uniquekeya2a9elemtype");
            
            /**
             * Gets array of all "column" elements
             */
            java.lang.String[] getColumnArray();
            
            /**
             * Gets ith "column" element
             */
            java.lang.String getColumnArray(int i);
            
            /**
             * Gets (as xml) array of all "column" elements
             */
            org.apache.xmlbeans.XmlIDREF[] xgetColumnArray();
            
            /**
             * Gets (as xml) ith "column" element
             */
            org.apache.xmlbeans.XmlIDREF xgetColumnArray(int i);
            
            /**
             * Returns number of "column" element
             */
            int sizeOfColumnArray();
            
            /**
             * Sets array of all "column" element
             */
            void setColumnArray(java.lang.String[] columnArray);
            
            /**
             * Sets ith "column" element
             */
            void setColumnArray(int i, java.lang.String column);
            
            /**
             * Sets (as xml) array of all "column" element
             */
            void xsetColumnArray(org.apache.xmlbeans.XmlIDREF[] columnArray);
            
            /**
             * Sets (as xml) ith "column" element
             */
            void xsetColumnArray(int i, org.apache.xmlbeans.XmlIDREF column);
            
            /**
             * Inserts the value as the ith "column" element
             */
            void insertColumn(int i, java.lang.String column);
            
            /**
             * Appends the value as the last "column" element
             */
            void addColumn(java.lang.String column);
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "column" element
             */
            org.apache.xmlbeans.XmlIDREF insertNewColumn(int i);
            
            /**
             * Appends and returns a new empty value (as xml) as the last "column" element
             */
            org.apache.xmlbeans.XmlIDREF addNewColumn();
            
            /**
             * Removes the ith "column" element
             */
            void removeColumn(int i);
            
            /**
             * Gets the "description" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Description getDescription();
            
            /**
             * Sets the "description" element
             */
            void setDescription(ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Description description);
            
            /**
             * Appends and returns a new empty "description" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Description addNewDescription();
            
            /**
             * Gets the "docbook" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Docbook getDocbook();
            
            /**
             * Sets the "docbook" element
             */
            void setDocbook(ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Docbook docbook);
            
            /**
             * Appends and returns a new empty "docbook" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Docbook addNewDocbook();
            
            /**
             * Gets the "name" attribute
             */
            java.lang.String getName();
            
            /**
             * Gets (as xml) the "name" attribute
             */
            org.apache.xmlbeans.XmlString xgetName();
            
            /**
             * Sets the "name" attribute
             */
            void setName(java.lang.String name);
            
            /**
             * Sets (as xml) the "name" attribute
             */
            void xsetName(org.apache.xmlbeans.XmlString name);
            
            /**
             * An XML description(@).
             *
             * This is a complex type.
             */
            public interface Description extends org.apache.xmlbeans.XmlObject
            {
                public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                    org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Description.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("descriptiond321elemtype");
                
                /**
                 * Gets the "de" element
                 */
                java.lang.String getDe();
                
                /**
                 * Gets (as xml) the "de" element
                 */
                org.apache.xmlbeans.XmlString xgetDe();
                
                /**
                 * Sets the "de" element
                 */
                void setDe(java.lang.String de);
                
                /**
                 * Sets (as xml) the "de" element
                 */
                void xsetDe(org.apache.xmlbeans.XmlString de);
                
                /**
                 * Gets the "en" element
                 */
                java.lang.String getEn();
                
                /**
                 * Gets (as xml) the "en" element
                 */
                org.apache.xmlbeans.XmlString xgetEn();
                
                /**
                 * Sets the "en" element
                 */
                void setEn(java.lang.String en);
                
                /**
                 * Sets (as xml) the "en" element
                 */
                void xsetEn(org.apache.xmlbeans.XmlString en);
                
                /**
                 * A factory class with static methods for creating instances
                 * of this type.
                 */
                
                public static final class Factory
                {
                    public static ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Description newInstance() {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Description) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                    
                    public static ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Description newInstance(org.apache.xmlbeans.XmlOptions options) {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Description) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                    
                    private Factory() { } // No instance of this class allowed
                }
            }
            
            /**
             * An XML docbook(@).
             *
             * This is a complex type.
             */
            public interface Docbook extends org.apache.xmlbeans.XmlObject
            {
                public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                    org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Docbook.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("docbookb5c6elemtype");
                
                /**
                 * Gets the "de" element
                 */
                ch.minova.core.xml.tables.DocbookCode getDe();
                
                /**
                 * Sets the "de" element
                 */
                void setDe(ch.minova.core.xml.tables.DocbookCode de);
                
                /**
                 * Appends and returns a new empty "de" element
                 */
                ch.minova.core.xml.tables.DocbookCode addNewDe();
                
                /**
                 * Gets the "en" element
                 */
                ch.minova.core.xml.tables.DocbookCode getEn();
                
                /**
                 * Sets the "en" element
                 */
                void setEn(ch.minova.core.xml.tables.DocbookCode en);
                
                /**
                 * Appends and returns a new empty "en" element
                 */
                ch.minova.core.xml.tables.DocbookCode addNewEn();
                
                /**
                 * A factory class with static methods for creating instances
                 * of this type.
                 */
                
                public static final class Factory
                {
                    public static ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Docbook newInstance() {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Docbook) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                    
                    public static ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Docbook newInstance(org.apache.xmlbeans.XmlOptions options) {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Docbook) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                    
                    private Factory() { } // No instance of this class allowed
                }
            }
            
            /**
             * A factory class with static methods for creating instances
             * of this type.
             */
            
            public static final class Factory
            {
                public static ch.minova.core.xml.tables.TableDocument.Table.Uniquekey newInstance() {
                  return (ch.minova.core.xml.tables.TableDocument.Table.Uniquekey) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static ch.minova.core.xml.tables.TableDocument.Table.Uniquekey newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (ch.minova.core.xml.tables.TableDocument.Table.Uniquekey) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * An XML oldconstraint(@).
         *
         * This is a complex type.
         */
        public interface Oldconstraint extends org.apache.xmlbeans.XmlObject
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Oldconstraint.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("oldconstraint84ffelemtype");
            
            /**
             * Gets the "description" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Description getDescription();
            
            /**
             * Sets the "description" element
             */
            void setDescription(ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Description description);
            
            /**
             * Appends and returns a new empty "description" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Description addNewDescription();
            
            /**
             * Gets the "docbook" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Docbook getDocbook();
            
            /**
             * Sets the "docbook" element
             */
            void setDocbook(ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Docbook docbook);
            
            /**
             * Appends and returns a new empty "docbook" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Docbook addNewDocbook();
            
            /**
             * Gets the "name" attribute
             */
            java.lang.String getName();
            
            /**
             * Gets (as xml) the "name" attribute
             */
            org.apache.xmlbeans.XmlString xgetName();
            
            /**
             * True if has "name" attribute
             */
            boolean isSetName();
            
            /**
             * Sets the "name" attribute
             */
            void setName(java.lang.String name);
            
            /**
             * Sets (as xml) the "name" attribute
             */
            void xsetName(org.apache.xmlbeans.XmlString name);
            
            /**
             * Unsets the "name" attribute
             */
            void unsetName();
            
            /**
             * An XML description(@).
             *
             * This is a complex type.
             */
            public interface Description extends org.apache.xmlbeans.XmlObject
            {
                public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                    org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Description.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("descriptionb977elemtype");
                
                /**
                 * Gets the "de" element
                 */
                java.lang.String getDe();
                
                /**
                 * Gets (as xml) the "de" element
                 */
                org.apache.xmlbeans.XmlString xgetDe();
                
                /**
                 * Sets the "de" element
                 */
                void setDe(java.lang.String de);
                
                /**
                 * Sets (as xml) the "de" element
                 */
                void xsetDe(org.apache.xmlbeans.XmlString de);
                
                /**
                 * Gets the "en" element
                 */
                java.lang.String getEn();
                
                /**
                 * Gets (as xml) the "en" element
                 */
                org.apache.xmlbeans.XmlString xgetEn();
                
                /**
                 * Sets the "en" element
                 */
                void setEn(java.lang.String en);
                
                /**
                 * Sets (as xml) the "en" element
                 */
                void xsetEn(org.apache.xmlbeans.XmlString en);
                
                /**
                 * A factory class with static methods for creating instances
                 * of this type.
                 */
                
                public static final class Factory
                {
                    public static ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Description newInstance() {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Description) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                    
                    public static ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Description newInstance(org.apache.xmlbeans.XmlOptions options) {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Description) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                    
                    private Factory() { } // No instance of this class allowed
                }
            }
            
            /**
             * An XML docbook(@).
             *
             * This is a complex type.
             */
            public interface Docbook extends org.apache.xmlbeans.XmlObject
            {
                public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                    org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Docbook.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("docbookc19celemtype");
                
                /**
                 * Gets the "de" element
                 */
                ch.minova.core.xml.tables.DocbookCode getDe();
                
                /**
                 * Sets the "de" element
                 */
                void setDe(ch.minova.core.xml.tables.DocbookCode de);
                
                /**
                 * Appends and returns a new empty "de" element
                 */
                ch.minova.core.xml.tables.DocbookCode addNewDe();
                
                /**
                 * Gets the "en" element
                 */
                ch.minova.core.xml.tables.DocbookCode getEn();
                
                /**
                 * Sets the "en" element
                 */
                void setEn(ch.minova.core.xml.tables.DocbookCode en);
                
                /**
                 * Appends and returns a new empty "en" element
                 */
                ch.minova.core.xml.tables.DocbookCode addNewEn();
                
                /**
                 * A factory class with static methods for creating instances
                 * of this type.
                 */
                
                public static final class Factory
                {
                    public static ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Docbook newInstance() {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Docbook) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                    
                    public static ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Docbook newInstance(org.apache.xmlbeans.XmlOptions options) {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Docbook) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                    
                    private Factory() { } // No instance of this class allowed
                }
            }
            
            /**
             * A factory class with static methods for creating instances
             * of this type.
             */
            
            public static final class Factory
            {
                public static ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint newInstance() {
                  return (ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * An XML exec-sql(@).
         *
         * This is a complex type.
         */
        public interface ExecSql extends org.apache.xmlbeans.XmlObject
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ExecSql.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("execsql745delemtype");
            
            /**
             * Gets the "execute" attribute
             */
            ch.minova.core.xml.tables.TableDocument.Table.ExecSql.Execute.Enum getExecute();
            
            /**
             * Gets (as xml) the "execute" attribute
             */
            ch.minova.core.xml.tables.TableDocument.Table.ExecSql.Execute xgetExecute();
            
            /**
             * True if has "execute" attribute
             */
            boolean isSetExecute();
            
            /**
             * Sets the "execute" attribute
             */
            void setExecute(ch.minova.core.xml.tables.TableDocument.Table.ExecSql.Execute.Enum execute);
            
            /**
             * Sets (as xml) the "execute" attribute
             */
            void xsetExecute(ch.minova.core.xml.tables.TableDocument.Table.ExecSql.Execute execute);
            
            /**
             * Unsets the "execute" attribute
             */
            void unsetExecute();
            
            /**
             * An XML execute(@).
             *
             * This is an atomic type that is a restriction of ch.minova.core.xml.tables.TableDocument$Table$ExecSql$Execute.
             */
            public interface Execute extends org.apache.xmlbeans.XmlString
            {
                public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                    org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Execute.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("execute9a60attrtype");
                
                org.apache.xmlbeans.StringEnumAbstractBase enumValue();
                void set(org.apache.xmlbeans.StringEnumAbstractBase e);
                
                static final Enum BEFORE = Enum.forString("before");
                static final Enum AFTER = Enum.forString("after");
                
                static final int INT_BEFORE = Enum.INT_BEFORE;
                static final int INT_AFTER = Enum.INT_AFTER;
                
                /**
                 * Enumeration value class for ch.minova.core.xml.tables.TableDocument$Table$ExecSql$Execute.
                 * These enum values can be used as follows:
                 * <pre>
                 * enum.toString(); // returns the string value of the enum
                 * enum.intValue(); // returns an int value, useful for switches
                 * // e.g., case Enum.INT_BEFORE
                 * Enum.forString(s); // returns the enum value for a string
                 * Enum.forInt(i); // returns the enum value for an int
                 * </pre>
                 * Enumeration objects are immutable singleton objects that
                 * can be compared using == object equality. They have no
                 * public constructor. See the constants defined within this
                 * class for all the valid values.
                 */
                static final class Enum extends org.apache.xmlbeans.StringEnumAbstractBase
                {
                    /**
                     * Returns the enum value for a string, or null if none.
                     */
                    public static Enum forString(java.lang.String s)
                        { return (Enum)table.forString(s); }
                    /**
                     * Returns the enum value corresponding to an int, or null if none.
                     */
                    public static Enum forInt(int i)
                        { return (Enum)table.forInt(i); }
                    
                    private Enum(java.lang.String s, int i)
                        { super(s, i); }
                    
                    static final int INT_BEFORE = 1;
                    static final int INT_AFTER = 2;
                    
                    public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
                        new org.apache.xmlbeans.StringEnumAbstractBase.Table
                    (
                      new Enum[]
                      {
                        new Enum("before", INT_BEFORE),
                        new Enum("after", INT_AFTER),
                      }
                    );
                    private static final long serialVersionUID = 1L;
                    private java.lang.Object readResolve() { return forInt(intValue()); } 
                }
                
                /**
                 * A factory class with static methods for creating instances
                 * of this type.
                 */
                
                public static final class Factory
                {
                    public static ch.minova.core.xml.tables.TableDocument.Table.ExecSql.Execute newValue(java.lang.Object obj) {
                      return (ch.minova.core.xml.tables.TableDocument.Table.ExecSql.Execute) type.newValue( obj ); }
                    
                    public static ch.minova.core.xml.tables.TableDocument.Table.ExecSql.Execute newInstance() {
                      return (ch.minova.core.xml.tables.TableDocument.Table.ExecSql.Execute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                    
                    public static ch.minova.core.xml.tables.TableDocument.Table.ExecSql.Execute newInstance(org.apache.xmlbeans.XmlOptions options) {
                      return (ch.minova.core.xml.tables.TableDocument.Table.ExecSql.Execute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                    
                    private Factory() { } // No instance of this class allowed
                }
            }
            
            /**
             * A factory class with static methods for creating instances
             * of this type.
             */
            
            public static final class Factory
            {
                public static ch.minova.core.xml.tables.TableDocument.Table.ExecSql newInstance() {
                  return (ch.minova.core.xml.tables.TableDocument.Table.ExecSql) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static ch.minova.core.xml.tables.TableDocument.Table.ExecSql newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (ch.minova.core.xml.tables.TableDocument.Table.ExecSql) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * An XML values(@).
         *
         * This is a complex type.
         */
        public interface Values extends org.apache.xmlbeans.XmlObject
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Values.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("valuesb8edelemtype");
            
            /**
             * Gets array of all "column" elements
             */
            ch.minova.core.xml.tables.TableDocument.Table.Values.Column[] getColumnArray();
            
            /**
             * Gets ith "column" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Values.Column getColumnArray(int i);
            
            /**
             * Returns number of "column" element
             */
            int sizeOfColumnArray();
            
            /**
             * Sets array of all "column" element
             */
            void setColumnArray(ch.minova.core.xml.tables.TableDocument.Table.Values.Column[] columnArray);
            
            /**
             * Sets ith "column" element
             */
            void setColumnArray(int i, ch.minova.core.xml.tables.TableDocument.Table.Values.Column column);
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "column" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Values.Column insertNewColumn(int i);
            
            /**
             * Appends and returns a new empty value (as xml) as the last "column" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Values.Column addNewColumn();
            
            /**
             * Removes the ith "column" element
             */
            void removeColumn(int i);
            
            /**
             * Gets array of all "row" elements
             */
            ch.minova.core.xml.tables.TableDocument.Table.Values.Row[] getRowArray();
            
            /**
             * Gets ith "row" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Values.Row getRowArray(int i);
            
            /**
             * Returns number of "row" element
             */
            int sizeOfRowArray();
            
            /**
             * Sets array of all "row" element
             */
            void setRowArray(ch.minova.core.xml.tables.TableDocument.Table.Values.Row[] rowArray);
            
            /**
             * Sets ith "row" element
             */
            void setRowArray(int i, ch.minova.core.xml.tables.TableDocument.Table.Values.Row row);
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "row" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Values.Row insertNewRow(int i);
            
            /**
             * Appends and returns a new empty value (as xml) as the last "row" element
             */
            ch.minova.core.xml.tables.TableDocument.Table.Values.Row addNewRow();
            
            /**
             * Removes the ith "row" element
             */
            void removeRow(int i);
            
            /**
             * An XML column(@).
             *
             * This is a complex type.
             */
            public interface Column extends org.apache.xmlbeans.XmlObject
            {
                public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                    org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Column.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("column234belemtype");
                
                /**
                 * Gets the "refid" attribute
                 */
                java.lang.String getRefid();
                
                /**
                 * Gets (as xml) the "refid" attribute
                 */
                org.apache.xmlbeans.XmlIDREF xgetRefid();
                
                /**
                 * Sets the "refid" attribute
                 */
                void setRefid(java.lang.String refid);
                
                /**
                 * Sets (as xml) the "refid" attribute
                 */
                void xsetRefid(org.apache.xmlbeans.XmlIDREF refid);
                
                /**
                 * A factory class with static methods for creating instances
                 * of this type.
                 */
                
                public static final class Factory
                {
                    public static ch.minova.core.xml.tables.TableDocument.Table.Values.Column newInstance() {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Values.Column) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                    
                    public static ch.minova.core.xml.tables.TableDocument.Table.Values.Column newInstance(org.apache.xmlbeans.XmlOptions options) {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Values.Column) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                    
                    private Factory() { } // No instance of this class allowed
                }
            }
            
            /**
             * An XML row(@).
             *
             * This is a complex type.
             */
            public interface Row extends org.apache.xmlbeans.XmlObject
            {
                public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                    org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Row.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("rowa357elemtype");
                
                /**
                 * Gets array of all "value" elements
                 */
                java.lang.String[] getValueArray();
                
                /**
                 * Gets ith "value" element
                 */
                java.lang.String getValueArray(int i);
                
                /**
                 * Gets (as xml) array of all "value" elements
                 */
                org.apache.xmlbeans.XmlString[] xgetValueArray();
                
                /**
                 * Gets (as xml) ith "value" element
                 */
                org.apache.xmlbeans.XmlString xgetValueArray(int i);
                
                /**
                 * Returns number of "value" element
                 */
                int sizeOfValueArray();
                
                /**
                 * Sets array of all "value" element
                 */
                void setValueArray(java.lang.String[] valueArray);
                
                /**
                 * Sets ith "value" element
                 */
                void setValueArray(int i, java.lang.String value);
                
                /**
                 * Sets (as xml) array of all "value" element
                 */
                void xsetValueArray(org.apache.xmlbeans.XmlString[] valueArray);
                
                /**
                 * Sets (as xml) ith "value" element
                 */
                void xsetValueArray(int i, org.apache.xmlbeans.XmlString value);
                
                /**
                 * Inserts the value as the ith "value" element
                 */
                void insertValue(int i, java.lang.String value);
                
                /**
                 * Appends the value as the last "value" element
                 */
                void addValue(java.lang.String value);
                
                /**
                 * Inserts and returns a new empty value (as xml) as the ith "value" element
                 */
                org.apache.xmlbeans.XmlString insertNewValue(int i);
                
                /**
                 * Appends and returns a new empty value (as xml) as the last "value" element
                 */
                org.apache.xmlbeans.XmlString addNewValue();
                
                /**
                 * Removes the ith "value" element
                 */
                void removeValue(int i);
                
                /**
                 * Gets the "description" element
                 */
                ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Description getDescription();
                
                /**
                 * Sets the "description" element
                 */
                void setDescription(ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Description description);
                
                /**
                 * Appends and returns a new empty "description" element
                 */
                ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Description addNewDescription();
                
                /**
                 * Gets the "docbook" element
                 */
                ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Docbook getDocbook();
                
                /**
                 * Sets the "docbook" element
                 */
                void setDocbook(ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Docbook docbook);
                
                /**
                 * Appends and returns a new empty "docbook" element
                 */
                ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Docbook addNewDocbook();
                
                /**
                 * Gets the "vendor" attribute
                 */
                org.apache.xmlbeans.XmlAnySimpleType getVendor();
                
                /**
                 * True if has "vendor" attribute
                 */
                boolean isSetVendor();
                
                /**
                 * Sets the "vendor" attribute
                 */
                void setVendor(org.apache.xmlbeans.XmlAnySimpleType vendor);
                
                /**
                 * Appends and returns a new empty "vendor" attribute
                 */
                org.apache.xmlbeans.XmlAnySimpleType addNewVendor();
                
                /**
                 * Unsets the "vendor" attribute
                 */
                void unsetVendor();
                
                /**
                 * Gets the "insert" attribute
                 */
                boolean getInsert();
                
                /**
                 * Gets (as xml) the "insert" attribute
                 */
                org.apache.xmlbeans.XmlBoolean xgetInsert();
                
                /**
                 * True if has "insert" attribute
                 */
                boolean isSetInsert();
                
                /**
                 * Sets the "insert" attribute
                 */
                void setInsert(boolean insert);
                
                /**
                 * Sets (as xml) the "insert" attribute
                 */
                void xsetInsert(org.apache.xmlbeans.XmlBoolean insert);
                
                /**
                 * Unsets the "insert" attribute
                 */
                void unsetInsert();
                
                /**
                 * An XML description(@).
                 *
                 * This is a complex type.
                 */
                public interface Description extends org.apache.xmlbeans.XmlObject
                {
                    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                      org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Description.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("descriptionffdfelemtype");
                    
                    /**
                     * Gets the "de" element
                     */
                    java.lang.String getDe();
                    
                    /**
                     * Gets (as xml) the "de" element
                     */
                    org.apache.xmlbeans.XmlString xgetDe();
                    
                    /**
                     * Sets the "de" element
                     */
                    void setDe(java.lang.String de);
                    
                    /**
                     * Sets (as xml) the "de" element
                     */
                    void xsetDe(org.apache.xmlbeans.XmlString de);
                    
                    /**
                     * Gets the "en" element
                     */
                    java.lang.String getEn();
                    
                    /**
                     * Gets (as xml) the "en" element
                     */
                    org.apache.xmlbeans.XmlString xgetEn();
                    
                    /**
                     * Sets the "en" element
                     */
                    void setEn(java.lang.String en);
                    
                    /**
                     * Sets (as xml) the "en" element
                     */
                    void xsetEn(org.apache.xmlbeans.XmlString en);
                    
                    /**
                     * A factory class with static methods for creating instances
                     * of this type.
                     */
                    
                    public static final class Factory
                    {
                      public static ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Description newInstance() {
                        return (ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Description) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                      
                      public static ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Description newInstance(org.apache.xmlbeans.XmlOptions options) {
                        return (ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Description) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                      
                      private Factory() { } // No instance of this class allowed
                    }
                }
                
                /**
                 * An XML docbook(@).
                 *
                 * This is a complex type.
                 */
                public interface Docbook extends org.apache.xmlbeans.XmlObject
                {
                    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                      org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Docbook.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2F4E3689390830430E8C90F5E81EF291").resolveHandle("docbookdf1aelemtype");
                    
                    /**
                     * Gets the "de" element
                     */
                    ch.minova.core.xml.tables.DocbookCode getDe();
                    
                    /**
                     * Sets the "de" element
                     */
                    void setDe(ch.minova.core.xml.tables.DocbookCode de);
                    
                    /**
                     * Appends and returns a new empty "de" element
                     */
                    ch.minova.core.xml.tables.DocbookCode addNewDe();
                    
                    /**
                     * Gets the "en" element
                     */
                    ch.minova.core.xml.tables.DocbookCode getEn();
                    
                    /**
                     * Sets the "en" element
                     */
                    void setEn(ch.minova.core.xml.tables.DocbookCode en);
                    
                    /**
                     * Appends and returns a new empty "en" element
                     */
                    ch.minova.core.xml.tables.DocbookCode addNewEn();
                    
                    /**
                     * A factory class with static methods for creating instances
                     * of this type.
                     */
                    
                    public static final class Factory
                    {
                      public static ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Docbook newInstance() {
                        return (ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Docbook) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                      
                      public static ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Docbook newInstance(org.apache.xmlbeans.XmlOptions options) {
                        return (ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Docbook) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                      
                      private Factory() { } // No instance of this class allowed
                    }
                }
                
                /**
                 * A factory class with static methods for creating instances
                 * of this type.
                 */
                
                public static final class Factory
                {
                    public static ch.minova.core.xml.tables.TableDocument.Table.Values.Row newInstance() {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Values.Row) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                    
                    public static ch.minova.core.xml.tables.TableDocument.Table.Values.Row newInstance(org.apache.xmlbeans.XmlOptions options) {
                      return (ch.minova.core.xml.tables.TableDocument.Table.Values.Row) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                    
                    private Factory() { } // No instance of this class allowed
                }
            }
            
            /**
             * A factory class with static methods for creating instances
             * of this type.
             */
            
            public static final class Factory
            {
                public static ch.minova.core.xml.tables.TableDocument.Table.Values newInstance() {
                  return (ch.minova.core.xml.tables.TableDocument.Table.Values) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static ch.minova.core.xml.tables.TableDocument.Table.Values newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (ch.minova.core.xml.tables.TableDocument.Table.Values) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static ch.minova.core.xml.tables.TableDocument.Table newInstance() {
              return (ch.minova.core.xml.tables.TableDocument.Table) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static ch.minova.core.xml.tables.TableDocument.Table newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (ch.minova.core.xml.tables.TableDocument.Table) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static ch.minova.core.xml.tables.TableDocument newInstance() {
          return (ch.minova.core.xml.tables.TableDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static ch.minova.core.xml.tables.TableDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (ch.minova.core.xml.tables.TableDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static ch.minova.core.xml.tables.TableDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (ch.minova.core.xml.tables.TableDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static ch.minova.core.xml.tables.TableDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ch.minova.core.xml.tables.TableDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static ch.minova.core.xml.tables.TableDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.minova.core.xml.tables.TableDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static ch.minova.core.xml.tables.TableDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.minova.core.xml.tables.TableDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static ch.minova.core.xml.tables.TableDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.minova.core.xml.tables.TableDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static ch.minova.core.xml.tables.TableDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.minova.core.xml.tables.TableDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static ch.minova.core.xml.tables.TableDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.minova.core.xml.tables.TableDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static ch.minova.core.xml.tables.TableDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.minova.core.xml.tables.TableDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static ch.minova.core.xml.tables.TableDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.minova.core.xml.tables.TableDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static ch.minova.core.xml.tables.TableDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.minova.core.xml.tables.TableDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static ch.minova.core.xml.tables.TableDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (ch.minova.core.xml.tables.TableDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static ch.minova.core.xml.tables.TableDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ch.minova.core.xml.tables.TableDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static ch.minova.core.xml.tables.TableDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (ch.minova.core.xml.tables.TableDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static ch.minova.core.xml.tables.TableDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ch.minova.core.xml.tables.TableDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static ch.minova.core.xml.tables.TableDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (ch.minova.core.xml.tables.TableDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }*/
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static ch.minova.core.xml.tables.TableDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (ch.minova.core.xml.tables.TableDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }*/
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }*/
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }*/
        
        private Factory() { } // No instance of this class allowed
    }
}

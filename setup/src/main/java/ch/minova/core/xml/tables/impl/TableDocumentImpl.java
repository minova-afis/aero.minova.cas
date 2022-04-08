/*
 * An XML document type.
 * Localname: table
 * Namespace: 
 * Java type: ch.minova.core.xml.tables.TableDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.xml.tables.impl;
/**
 * A document containing one table(@) element.
 *
 * This is a complex type.
 */
public class TableDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument
{
    private static final long serialVersionUID = 1L;
    
    public TableDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TABLE$0 = 
        new javax.xml.namespace.QName("", "table");
    
    
    /**
     * Gets the "table" element
     */
    public ch.minova.core.xml.tables.TableDocument.Table getTable()
    {
        synchronized (monitor())
        {
            check_orphaned();
            ch.minova.core.xml.tables.TableDocument.Table target = null;
            target = (ch.minova.core.xml.tables.TableDocument.Table)get_store().find_element_user(TABLE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "table" element
     */
    public void setTable(ch.minova.core.xml.tables.TableDocument.Table table)
    {
        synchronized (monitor())
        {
            check_orphaned();
            ch.minova.core.xml.tables.TableDocument.Table target = null;
            target = (ch.minova.core.xml.tables.TableDocument.Table)get_store().find_element_user(TABLE$0, 0);
            if (target == null)
            {
                target = (ch.minova.core.xml.tables.TableDocument.Table)get_store().add_element_user(TABLE$0);
            }
            target.set(table);
        }
    }
    
    /**
     * Appends and returns a new empty "table" element
     */
    public ch.minova.core.xml.tables.TableDocument.Table addNewTable()
    {
        synchronized (monitor())
        {
            check_orphaned();
            ch.minova.core.xml.tables.TableDocument.Table target = null;
            target = (ch.minova.core.xml.tables.TableDocument.Table)get_store().add_element_user(TABLE$0);
            return target;
        }
    }
    /**
     * An XML table(@).
     *
     * This is a complex type.
     */
    public static class TableImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table
    {
        private static final long serialVersionUID = 1L;
        
        public TableImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName DESCRIPTION$0 = 
            new javax.xml.namespace.QName("", "description");
        private static final javax.xml.namespace.QName DOCBOOK$2 = 
            new javax.xml.namespace.QName("", "docbook");
        private static final javax.xml.namespace.QName COLUMN$4 = 
            new javax.xml.namespace.QName("", "column");
        private static final javax.xml.namespace.QName PRIMARYKEY$6 = 
            new javax.xml.namespace.QName("", "primarykey");
        private static final javax.xml.namespace.QName FOREIGNKEY$8 = 
            new javax.xml.namespace.QName("", "foreignkey");
        private static final javax.xml.namespace.QName UNIQUEKEY$10 = 
            new javax.xml.namespace.QName("", "uniquekey");
        private static final javax.xml.namespace.QName OLDCONSTRAINT$12 = 
            new javax.xml.namespace.QName("", "oldconstraint");
        private static final javax.xml.namespace.QName EXECSQL$14 = 
            new javax.xml.namespace.QName("", "exec-sql");
        private static final javax.xml.namespace.QName VALUES$16 = 
            new javax.xml.namespace.QName("", "values");
        private static final javax.xml.namespace.QName NAME$18 = 
            new javax.xml.namespace.QName("", "name");
        private static final javax.xml.namespace.QName VENDOR$20 = 
            new javax.xml.namespace.QName("", "vendor");
        private static final javax.xml.namespace.QName USERLEVEL$22 = 
            new javax.xml.namespace.QName("", "userlevel");
        private static final javax.xml.namespace.QName SOURCE$24 = 
            new javax.xml.namespace.QName("", "source");
        private static final javax.xml.namespace.QName REVISION$26 = 
            new javax.xml.namespace.QName("", "revision");
        
        
        /**
         * Gets the "description" element
         */
        public ch.minova.core.xml.tables.TableDocument.Table.Description getDescription()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Description target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Description)get_store().find_element_user(DESCRIPTION$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * Sets the "description" element
         */
        public void setDescription(ch.minova.core.xml.tables.TableDocument.Table.Description description)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Description target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Description)get_store().find_element_user(DESCRIPTION$0, 0);
                if (target == null)
                {
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Description)get_store().add_element_user(DESCRIPTION$0);
                }
                target.set(description);
            }
        }
        
        /**
         * Appends and returns a new empty "description" element
         */
        public ch.minova.core.xml.tables.TableDocument.Table.Description addNewDescription()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Description target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Description)get_store().add_element_user(DESCRIPTION$0);
                return target;
            }
        }
        
        /**
         * Gets the "docbook" element
         */
        public ch.minova.core.xml.tables.TableDocument.Table.Docbook getDocbook()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Docbook target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Docbook)get_store().find_element_user(DOCBOOK$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * Sets the "docbook" element
         */
        public void setDocbook(ch.minova.core.xml.tables.TableDocument.Table.Docbook docbook)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Docbook target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Docbook)get_store().find_element_user(DOCBOOK$2, 0);
                if (target == null)
                {
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Docbook)get_store().add_element_user(DOCBOOK$2);
                }
                target.set(docbook);
            }
        }
        
        /**
         * Appends and returns a new empty "docbook" element
         */
        public ch.minova.core.xml.tables.TableDocument.Table.Docbook addNewDocbook()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Docbook target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Docbook)get_store().add_element_user(DOCBOOK$2);
                return target;
            }
        }
        
        /**
         * Gets array of all "column" elements
         */
        public ch.minova.core.xml.tables.TableDocument.Table.Column[] getColumnArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(COLUMN$4, targetList);
                ch.minova.core.xml.tables.TableDocument.Table.Column[] result = new ch.minova.core.xml.tables.TableDocument.Table.Column[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "column" element
         */
        public ch.minova.core.xml.tables.TableDocument.Table.Column getColumnArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Column target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Column)get_store().find_element_user(COLUMN$4, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "column" element
         */
        public int sizeOfColumnArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(COLUMN$4);
            }
        }
        
        /**
         * Sets array of all "column" element
         */
        public void setColumnArray(ch.minova.core.xml.tables.TableDocument.Table.Column[] columnArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(columnArray, COLUMN$4);
            }
        }
        
        /**
         * Sets ith "column" element
         */
        public void setColumnArray(int i, ch.minova.core.xml.tables.TableDocument.Table.Column column)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Column target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Column)get_store().find_element_user(COLUMN$4, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(column);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "column" element
         */
        public ch.minova.core.xml.tables.TableDocument.Table.Column insertNewColumn(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Column target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Column)get_store().insert_element_user(COLUMN$4, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "column" element
         */
        public ch.minova.core.xml.tables.TableDocument.Table.Column addNewColumn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Column target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Column)get_store().add_element_user(COLUMN$4);
                return target;
            }
        }
        
        /**
         * Removes the ith "column" element
         */
        public void removeColumn(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(COLUMN$4, i);
            }
        }
        
        /**
         * Gets the "primarykey" element
         */
        public ch.minova.core.xml.tables.TableDocument.Table.Primarykey getPrimarykey()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Primarykey target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Primarykey)get_store().find_element_user(PRIMARYKEY$6, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "primarykey" element
         */
        public boolean isSetPrimarykey()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(PRIMARYKEY$6) != 0;
            }
        }
        
        /**
         * Sets the "primarykey" element
         */
        public void setPrimarykey(ch.minova.core.xml.tables.TableDocument.Table.Primarykey primarykey)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Primarykey target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Primarykey)get_store().find_element_user(PRIMARYKEY$6, 0);
                if (target == null)
                {
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Primarykey)get_store().add_element_user(PRIMARYKEY$6);
                }
                target.set(primarykey);
            }
        }
        
        /**
         * Appends and returns a new empty "primarykey" element
         */
        public ch.minova.core.xml.tables.TableDocument.Table.Primarykey addNewPrimarykey()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Primarykey target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Primarykey)get_store().add_element_user(PRIMARYKEY$6);
                return target;
            }
        }
        
        /**
         * Unsets the "primarykey" element
         */
        public void unsetPrimarykey()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(PRIMARYKEY$6, 0);
            }
        }
        
        /**
         * Gets array of all "foreignkey" elements
         */
        public ch.minova.core.xml.tables.TableDocument.Table.Foreignkey[] getForeignkeyArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(FOREIGNKEY$8, targetList);
                ch.minova.core.xml.tables.TableDocument.Table.Foreignkey[] result = new ch.minova.core.xml.tables.TableDocument.Table.Foreignkey[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "foreignkey" element
         */
        public ch.minova.core.xml.tables.TableDocument.Table.Foreignkey getForeignkeyArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Foreignkey target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Foreignkey)get_store().find_element_user(FOREIGNKEY$8, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "foreignkey" element
         */
        public int sizeOfForeignkeyArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(FOREIGNKEY$8);
            }
        }
        
        /**
         * Sets array of all "foreignkey" element
         */
        public void setForeignkeyArray(ch.minova.core.xml.tables.TableDocument.Table.Foreignkey[] foreignkeyArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(foreignkeyArray, FOREIGNKEY$8);
            }
        }
        
        /**
         * Sets ith "foreignkey" element
         */
        public void setForeignkeyArray(int i, ch.minova.core.xml.tables.TableDocument.Table.Foreignkey foreignkey)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Foreignkey target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Foreignkey)get_store().find_element_user(FOREIGNKEY$8, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(foreignkey);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "foreignkey" element
         */
        public ch.minova.core.xml.tables.TableDocument.Table.Foreignkey insertNewForeignkey(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Foreignkey target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Foreignkey)get_store().insert_element_user(FOREIGNKEY$8, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "foreignkey" element
         */
        public ch.minova.core.xml.tables.TableDocument.Table.Foreignkey addNewForeignkey()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Foreignkey target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Foreignkey)get_store().add_element_user(FOREIGNKEY$8);
                return target;
            }
        }
        
        /**
         * Removes the ith "foreignkey" element
         */
        public void removeForeignkey(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(FOREIGNKEY$8, i);
            }
        }
        
        /**
         * Gets array of all "uniquekey" elements
         */
        public ch.minova.core.xml.tables.TableDocument.Table.Uniquekey[] getUniquekeyArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(UNIQUEKEY$10, targetList);
                ch.minova.core.xml.tables.TableDocument.Table.Uniquekey[] result = new ch.minova.core.xml.tables.TableDocument.Table.Uniquekey[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "uniquekey" element
         */
        public ch.minova.core.xml.tables.TableDocument.Table.Uniquekey getUniquekeyArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Uniquekey target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Uniquekey)get_store().find_element_user(UNIQUEKEY$10, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "uniquekey" element
         */
        public int sizeOfUniquekeyArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(UNIQUEKEY$10);
            }
        }
        
        /**
         * Sets array of all "uniquekey" element
         */
        public void setUniquekeyArray(ch.minova.core.xml.tables.TableDocument.Table.Uniquekey[] uniquekeyArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(uniquekeyArray, UNIQUEKEY$10);
            }
        }
        
        /**
         * Sets ith "uniquekey" element
         */
        public void setUniquekeyArray(int i, ch.minova.core.xml.tables.TableDocument.Table.Uniquekey uniquekey)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Uniquekey target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Uniquekey)get_store().find_element_user(UNIQUEKEY$10, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(uniquekey);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "uniquekey" element
         */
        public ch.minova.core.xml.tables.TableDocument.Table.Uniquekey insertNewUniquekey(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Uniquekey target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Uniquekey)get_store().insert_element_user(UNIQUEKEY$10, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "uniquekey" element
         */
        public ch.minova.core.xml.tables.TableDocument.Table.Uniquekey addNewUniquekey()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Uniquekey target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Uniquekey)get_store().add_element_user(UNIQUEKEY$10);
                return target;
            }
        }
        
        /**
         * Removes the ith "uniquekey" element
         */
        public void removeUniquekey(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(UNIQUEKEY$10, i);
            }
        }
        
        /**
         * Gets array of all "oldconstraint" elements
         */
        public ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint[] getOldconstraintArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(OLDCONSTRAINT$12, targetList);
                ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint[] result = new ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "oldconstraint" element
         */
        public ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint getOldconstraintArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint)get_store().find_element_user(OLDCONSTRAINT$12, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "oldconstraint" element
         */
        public int sizeOfOldconstraintArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(OLDCONSTRAINT$12);
            }
        }
        
        /**
         * Sets array of all "oldconstraint" element
         */
        public void setOldconstraintArray(ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint[] oldconstraintArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(oldconstraintArray, OLDCONSTRAINT$12);
            }
        }
        
        /**
         * Sets ith "oldconstraint" element
         */
        public void setOldconstraintArray(int i, ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint oldconstraint)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint)get_store().find_element_user(OLDCONSTRAINT$12, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(oldconstraint);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "oldconstraint" element
         */
        public ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint insertNewOldconstraint(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint)get_store().insert_element_user(OLDCONSTRAINT$12, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "oldconstraint" element
         */
        public ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint addNewOldconstraint()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint)get_store().add_element_user(OLDCONSTRAINT$12);
                return target;
            }
        }
        
        /**
         * Removes the ith "oldconstraint" element
         */
        public void removeOldconstraint(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(OLDCONSTRAINT$12, i);
            }
        }
        
        /**
         * Gets the "exec-sql" element
         */
        public ch.minova.core.xml.tables.TableDocument.Table.ExecSql getExecSql()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.ExecSql target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.ExecSql)get_store().find_element_user(EXECSQL$14, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "exec-sql" element
         */
        public boolean isSetExecSql()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(EXECSQL$14) != 0;
            }
        }
        
        /**
         * Sets the "exec-sql" element
         */
        public void setExecSql(ch.minova.core.xml.tables.TableDocument.Table.ExecSql execSql)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.ExecSql target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.ExecSql)get_store().find_element_user(EXECSQL$14, 0);
                if (target == null)
                {
                    target = (ch.minova.core.xml.tables.TableDocument.Table.ExecSql)get_store().add_element_user(EXECSQL$14);
                }
                target.set(execSql);
            }
        }
        
        /**
         * Appends and returns a new empty "exec-sql" element
         */
        public ch.minova.core.xml.tables.TableDocument.Table.ExecSql addNewExecSql()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.ExecSql target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.ExecSql)get_store().add_element_user(EXECSQL$14);
                return target;
            }
        }
        
        /**
         * Unsets the "exec-sql" element
         */
        public void unsetExecSql()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(EXECSQL$14, 0);
            }
        }
        
        /**
         * Gets the "values" element
         */
        public ch.minova.core.xml.tables.TableDocument.Table.Values getValues()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Values target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Values)get_store().find_element_user(VALUES$16, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "values" element
         */
        public boolean isSetValues()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(VALUES$16) != 0;
            }
        }
        
        /**
         * Sets the "values" element
         */
        public void setValues(ch.minova.core.xml.tables.TableDocument.Table.Values values)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Values target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Values)get_store().find_element_user(VALUES$16, 0);
                if (target == null)
                {
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Values)get_store().add_element_user(VALUES$16);
                }
                target.set(values);
            }
        }
        
        /**
         * Appends and returns a new empty "values" element
         */
        public ch.minova.core.xml.tables.TableDocument.Table.Values addNewValues()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.xml.tables.TableDocument.Table.Values target = null;
                target = (ch.minova.core.xml.tables.TableDocument.Table.Values)get_store().add_element_user(VALUES$16);
                return target;
            }
        }
        
        /**
         * Unsets the "values" element
         */
        public void unsetValues()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(VALUES$16, 0);
            }
        }
        
        /**
         * Gets the "name" attribute
         */
        public java.lang.String getName()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$18);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "name" attribute
         */
        public org.apache.xmlbeans.XmlName xgetName()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlName target = null;
                target = (org.apache.xmlbeans.XmlName)get_store().find_attribute_user(NAME$18);
                return target;
            }
        }
        
        /**
         * Sets the "name" attribute
         */
        public void setName(java.lang.String name)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$18);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NAME$18);
                }
                target.setStringValue(name);
            }
        }
        
        /**
         * Sets (as xml) the "name" attribute
         */
        public void xsetName(org.apache.xmlbeans.XmlName name)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlName target = null;
                target = (org.apache.xmlbeans.XmlName)get_store().find_attribute_user(NAME$18);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlName)get_store().add_attribute_user(NAME$18);
                }
                target.set(name);
            }
        }
        
        /**
         * Gets the "vendor" attribute
         */
        public java.lang.String getVendor()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VENDOR$20);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "vendor" attribute
         */
        public org.apache.xmlbeans.XmlString xgetVendor()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(VENDOR$20);
                return target;
            }
        }
        
        /**
         * True if has "vendor" attribute
         */
        public boolean isSetVendor()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(VENDOR$20) != null;
            }
        }
        
        /**
         * Sets the "vendor" attribute
         */
        public void setVendor(java.lang.String vendor)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VENDOR$20);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(VENDOR$20);
                }
                target.setStringValue(vendor);
            }
        }
        
        /**
         * Sets (as xml) the "vendor" attribute
         */
        public void xsetVendor(org.apache.xmlbeans.XmlString vendor)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(VENDOR$20);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(VENDOR$20);
                }
                target.set(vendor);
            }
        }
        
        /**
         * Unsets the "vendor" attribute
         */
        public void unsetVendor()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(VENDOR$20);
            }
        }
        
        /**
         * Gets the "userlevel" attribute
         */
        public java.lang.String getUserlevel()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(USERLEVEL$22);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "userlevel" attribute
         */
        public org.apache.xmlbeans.XmlString xgetUserlevel()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(USERLEVEL$22);
                return target;
            }
        }
        
        /**
         * True if has "userlevel" attribute
         */
        public boolean isSetUserlevel()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(USERLEVEL$22) != null;
            }
        }
        
        /**
         * Sets the "userlevel" attribute
         */
        public void setUserlevel(java.lang.String userlevel)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(USERLEVEL$22);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(USERLEVEL$22);
                }
                target.setStringValue(userlevel);
            }
        }
        
        /**
         * Sets (as xml) the "userlevel" attribute
         */
        public void xsetUserlevel(org.apache.xmlbeans.XmlString userlevel)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(USERLEVEL$22);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(USERLEVEL$22);
                }
                target.set(userlevel);
            }
        }
        
        /**
         * Unsets the "userlevel" attribute
         */
        public void unsetUserlevel()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(USERLEVEL$22);
            }
        }
        
        /**
         * Gets the "source" attribute
         */
        public java.lang.String getSource()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SOURCE$24);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "source" attribute
         */
        public org.apache.xmlbeans.XmlString xgetSource()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(SOURCE$24);
                return target;
            }
        }
        
        /**
         * True if has "source" attribute
         */
        public boolean isSetSource()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(SOURCE$24) != null;
            }
        }
        
        /**
         * Sets the "source" attribute
         */
        public void setSource(java.lang.String source)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SOURCE$24);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(SOURCE$24);
                }
                target.setStringValue(source);
            }
        }
        
        /**
         * Sets (as xml) the "source" attribute
         */
        public void xsetSource(org.apache.xmlbeans.XmlString source)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(SOURCE$24);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(SOURCE$24);
                }
                target.set(source);
            }
        }
        
        /**
         * Unsets the "source" attribute
         */
        public void unsetSource()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(SOURCE$24);
            }
        }
        
        /**
         * Gets the "revision" attribute
         */
        public java.lang.String getRevision()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(REVISION$26);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "revision" attribute
         */
        public org.apache.xmlbeans.XmlString xgetRevision()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(REVISION$26);
                return target;
            }
        }
        
        /**
         * True if has "revision" attribute
         */
        public boolean isSetRevision()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(REVISION$26) != null;
            }
        }
        
        /**
         * Sets the "revision" attribute
         */
        public void setRevision(java.lang.String revision)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(REVISION$26);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(REVISION$26);
                }
                target.setStringValue(revision);
            }
        }
        
        /**
         * Sets (as xml) the "revision" attribute
         */
        public void xsetRevision(org.apache.xmlbeans.XmlString revision)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(REVISION$26);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(REVISION$26);
                }
                target.set(revision);
            }
        }
        
        /**
         * Unsets the "revision" attribute
         */
        public void unsetRevision()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(REVISION$26);
            }
        }
        /**
         * An XML description(@).
         *
         * This is a complex type.
         */
        public static class DescriptionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Description
        {
            private static final long serialVersionUID = 1L;
            
            public DescriptionImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType);
            }
            
            private static final javax.xml.namespace.QName DE$0 = 
                new javax.xml.namespace.QName("", "de");
            private static final javax.xml.namespace.QName EN$2 = 
                new javax.xml.namespace.QName("", "en");
            
            
            /**
             * Gets the "de" element
             */
            public java.lang.String getDe()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DE$0, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target.getStringValue();
                }
            }
            
            /**
             * Gets (as xml) the "de" element
             */
            public org.apache.xmlbeans.XmlString xgetDe()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlString target = null;
                    target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DE$0, 0);
                    return target;
                }
            }
            
            /**
             * Sets the "de" element
             */
            public void setDe(java.lang.String de)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DE$0, 0);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DE$0);
                    }
                    target.setStringValue(de);
                }
            }
            
            /**
             * Sets (as xml) the "de" element
             */
            public void xsetDe(org.apache.xmlbeans.XmlString de)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlString target = null;
                    target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DE$0, 0);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DE$0);
                    }
                    target.set(de);
                }
            }
            
            /**
             * Gets the "en" element
             */
            public java.lang.String getEn()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EN$2, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target.getStringValue();
                }
            }
            
            /**
             * Gets (as xml) the "en" element
             */
            public org.apache.xmlbeans.XmlString xgetEn()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlString target = null;
                    target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EN$2, 0);
                    return target;
                }
            }
            
            /**
             * Sets the "en" element
             */
            public void setEn(java.lang.String en)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EN$2, 0);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(EN$2);
                    }
                    target.setStringValue(en);
                }
            }
            
            /**
             * Sets (as xml) the "en" element
             */
            public void xsetEn(org.apache.xmlbeans.XmlString en)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlString target = null;
                    target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EN$2, 0);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(EN$2);
                    }
                    target.set(en);
                }
            }
        }
        /**
         * An XML docbook(@).
         *
         * This is a complex type.
         */
        public static class DocbookImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Docbook
        {
            private static final long serialVersionUID = 1L;
            
            public DocbookImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType);
            }
            
            private static final javax.xml.namespace.QName DE$0 = 
                new javax.xml.namespace.QName("", "de");
            private static final javax.xml.namespace.QName EN$2 = 
                new javax.xml.namespace.QName("", "en");
            
            
            /**
             * Gets the "de" element
             */
            public ch.minova.core.xml.tables.DocbookCode getDe()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.DocbookCode target = null;
                    target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(DE$0, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * Sets the "de" element
             */
            public void setDe(ch.minova.core.xml.tables.DocbookCode de)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.DocbookCode target = null;
                    target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(DE$0, 0);
                    if (target == null)
                    {
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(DE$0);
                    }
                    target.set(de);
                }
            }
            
            /**
             * Appends and returns a new empty "de" element
             */
            public ch.minova.core.xml.tables.DocbookCode addNewDe()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.DocbookCode target = null;
                    target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(DE$0);
                    return target;
                }
            }
            
            /**
             * Gets the "en" element
             */
            public ch.minova.core.xml.tables.DocbookCode getEn()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.DocbookCode target = null;
                    target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(EN$2, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * Sets the "en" element
             */
            public void setEn(ch.minova.core.xml.tables.DocbookCode en)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.DocbookCode target = null;
                    target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(EN$2, 0);
                    if (target == null)
                    {
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(EN$2);
                    }
                    target.set(en);
                }
            }
            
            /**
             * Appends and returns a new empty "en" element
             */
            public ch.minova.core.xml.tables.DocbookCode addNewEn()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.DocbookCode target = null;
                    target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(EN$2);
                    return target;
                }
            }
        }
        /**
         * An XML column(@).
         *
         * This is a complex type.
         */
        public static class ColumnImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Column
        {
            private static final long serialVersionUID = 1L;
            
            public ColumnImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType);
            }
            
            private static final javax.xml.namespace.QName INTEGER$0 = 
                new javax.xml.namespace.QName("", "integer");
            private static final javax.xml.namespace.QName BIGINT$2 = 
                new javax.xml.namespace.QName("", "bigint");
            private static final javax.xml.namespace.QName VARCHAR$4 = 
                new javax.xml.namespace.QName("", "varchar");
            private static final javax.xml.namespace.QName DATETIME$6 = 
                new javax.xml.namespace.QName("", "datetime");
            private static final javax.xml.namespace.QName FLOAT$8 = 
                new javax.xml.namespace.QName("", "float");
            private static final javax.xml.namespace.QName BOOLEAN$10 = 
                new javax.xml.namespace.QName("", "boolean");
            private static final javax.xml.namespace.QName MONEY$12 = 
                new javax.xml.namespace.QName("", "money");
            private static final javax.xml.namespace.QName OLDNAME$14 = 
                new javax.xml.namespace.QName("", "old-name");
            private static final javax.xml.namespace.QName DESCRIPTION$16 = 
                new javax.xml.namespace.QName("", "description");
            private static final javax.xml.namespace.QName DOCBOOK$18 = 
                new javax.xml.namespace.QName("", "docbook");
            private static final javax.xml.namespace.QName DEFAULT$20 = 
                new javax.xml.namespace.QName("", "default");
            private static final javax.xml.namespace.QName NAME$22 = 
                new javax.xml.namespace.QName("", "name");
            private static final javax.xml.namespace.QName USERLEVEL$24 = 
                new javax.xml.namespace.QName("", "userlevel");
            private static final javax.xml.namespace.QName VENDOR$26 = 
                new javax.xml.namespace.QName("", "vendor");
            
            
            /**
             * Gets the "integer" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Column.Integer getInteger()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Integer target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Integer)get_store().find_element_user(INTEGER$0, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * True if has "integer" element
             */
            public boolean isSetInteger()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    return get_store().count_elements(INTEGER$0) != 0;
                }
            }
            
            /**
             * Sets the "integer" element
             */
            public void setInteger(ch.minova.core.xml.tables.TableDocument.Table.Column.Integer integer)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Integer target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Integer)get_store().find_element_user(INTEGER$0, 0);
                    if (target == null)
                    {
                      target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Integer)get_store().add_element_user(INTEGER$0);
                    }
                    target.set(integer);
                }
            }
            
            /**
             * Appends and returns a new empty "integer" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Column.Integer addNewInteger()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Integer target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Integer)get_store().add_element_user(INTEGER$0);
                    return target;
                }
            }
            
            /**
             * Unsets the "integer" element
             */
            public void unsetInteger()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    get_store().remove_element(INTEGER$0, 0);
                }
            }
            
            /**
             * Gets the "bigint" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Column.Bigint getBigint()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Bigint target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Bigint)get_store().find_element_user(BIGINT$2, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * True if has "bigint" element
             */
            public boolean isSetBigint()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    return get_store().count_elements(BIGINT$2) != 0;
                }
            }
            
            /**
             * Sets the "bigint" element
             */
            public void setBigint(ch.minova.core.xml.tables.TableDocument.Table.Column.Bigint bigint)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Bigint target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Bigint)get_store().find_element_user(BIGINT$2, 0);
                    if (target == null)
                    {
                      target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Bigint)get_store().add_element_user(BIGINT$2);
                    }
                    target.set(bigint);
                }
            }
            
            /**
             * Appends and returns a new empty "bigint" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Column.Bigint addNewBigint()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Bigint target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Bigint)get_store().add_element_user(BIGINT$2);
                    return target;
                }
            }
            
            /**
             * Unsets the "bigint" element
             */
            public void unsetBigint()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    get_store().remove_element(BIGINT$2, 0);
                }
            }
            
            /**
             * Gets the "varchar" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Column.Varchar getVarchar()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Varchar target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Varchar)get_store().find_element_user(VARCHAR$4, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * True if has "varchar" element
             */
            public boolean isSetVarchar()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    return get_store().count_elements(VARCHAR$4) != 0;
                }
            }
            
            /**
             * Sets the "varchar" element
             */
            public void setVarchar(ch.minova.core.xml.tables.TableDocument.Table.Column.Varchar varchar)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Varchar target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Varchar)get_store().find_element_user(VARCHAR$4, 0);
                    if (target == null)
                    {
                      target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Varchar)get_store().add_element_user(VARCHAR$4);
                    }
                    target.set(varchar);
                }
            }
            
            /**
             * Appends and returns a new empty "varchar" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Column.Varchar addNewVarchar()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Varchar target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Varchar)get_store().add_element_user(VARCHAR$4);
                    return target;
                }
            }
            
            /**
             * Unsets the "varchar" element
             */
            public void unsetVarchar()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    get_store().remove_element(VARCHAR$4, 0);
                }
            }
            
            /**
             * Gets the "datetime" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Column.Datetime getDatetime()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Datetime target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Datetime)get_store().find_element_user(DATETIME$6, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * True if has "datetime" element
             */
            public boolean isSetDatetime()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    return get_store().count_elements(DATETIME$6) != 0;
                }
            }
            
            /**
             * Sets the "datetime" element
             */
            public void setDatetime(ch.minova.core.xml.tables.TableDocument.Table.Column.Datetime datetime)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Datetime target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Datetime)get_store().find_element_user(DATETIME$6, 0);
                    if (target == null)
                    {
                      target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Datetime)get_store().add_element_user(DATETIME$6);
                    }
                    target.set(datetime);
                }
            }
            
            /**
             * Appends and returns a new empty "datetime" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Column.Datetime addNewDatetime()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Datetime target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Datetime)get_store().add_element_user(DATETIME$6);
                    return target;
                }
            }
            
            /**
             * Unsets the "datetime" element
             */
            public void unsetDatetime()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    get_store().remove_element(DATETIME$6, 0);
                }
            }
            
            /**
             * Gets the "float" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Column.Float getFloat()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Float target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Float)get_store().find_element_user(FLOAT$8, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * True if has "float" element
             */
            public boolean isSetFloat()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    return get_store().count_elements(FLOAT$8) != 0;
                }
            }
            
            /**
             * Sets the "float" element
             */
            public void setFloat(ch.minova.core.xml.tables.TableDocument.Table.Column.Float xfloat)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Float target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Float)get_store().find_element_user(FLOAT$8, 0);
                    if (target == null)
                    {
                      target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Float)get_store().add_element_user(FLOAT$8);
                    }
                    target.set(xfloat);
                }
            }
            
            /**
             * Appends and returns a new empty "float" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Column.Float addNewFloat()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Float target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Float)get_store().add_element_user(FLOAT$8);
                    return target;
                }
            }
            
            /**
             * Unsets the "float" element
             */
            public void unsetFloat()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    get_store().remove_element(FLOAT$8, 0);
                }
            }
            
            /**
             * Gets the "boolean" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Column.Boolean getBoolean()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Boolean target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Boolean)get_store().find_element_user(BOOLEAN$10, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * True if has "boolean" element
             */
            public boolean isSetBoolean()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    return get_store().count_elements(BOOLEAN$10) != 0;
                }
            }
            
            /**
             * Sets the "boolean" element
             */
            public void setBoolean(ch.minova.core.xml.tables.TableDocument.Table.Column.Boolean xboolean)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Boolean target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Boolean)get_store().find_element_user(BOOLEAN$10, 0);
                    if (target == null)
                    {
                      target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Boolean)get_store().add_element_user(BOOLEAN$10);
                    }
                    target.set(xboolean);
                }
            }
            
            /**
             * Appends and returns a new empty "boolean" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Column.Boolean addNewBoolean()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Boolean target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Boolean)get_store().add_element_user(BOOLEAN$10);
                    return target;
                }
            }
            
            /**
             * Unsets the "boolean" element
             */
            public void unsetBoolean()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    get_store().remove_element(BOOLEAN$10, 0);
                }
            }
            
            /**
             * Gets the "money" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Column.Money getMoney()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Money target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Money)get_store().find_element_user(MONEY$12, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * True if has "money" element
             */
            public boolean isSetMoney()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    return get_store().count_elements(MONEY$12) != 0;
                }
            }
            
            /**
             * Sets the "money" element
             */
            public void setMoney(ch.minova.core.xml.tables.TableDocument.Table.Column.Money money)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Money target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Money)get_store().find_element_user(MONEY$12, 0);
                    if (target == null)
                    {
                      target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Money)get_store().add_element_user(MONEY$12);
                    }
                    target.set(money);
                }
            }
            
            /**
             * Appends and returns a new empty "money" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Column.Money addNewMoney()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Money target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Money)get_store().add_element_user(MONEY$12);
                    return target;
                }
            }
            
            /**
             * Unsets the "money" element
             */
            public void unsetMoney()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    get_store().remove_element(MONEY$12, 0);
                }
            }
            
            /**
             * Gets array of all "old-name" elements
             */
            public java.lang.String[] getOldNameArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    java.util.List targetList = new java.util.ArrayList();
                    get_store().find_all_element_users(OLDNAME$14, targetList);
                    java.lang.String[] result = new java.lang.String[targetList.size()];
                    for (int i = 0, len = targetList.size() ; i < len ; i++)
                        result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getStringValue();
                    return result;
                }
            }
            
            /**
             * Gets ith "old-name" element
             */
            public java.lang.String getOldNameArray(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(OLDNAME$14, i);
                    if (target == null)
                    {
                      throw new IndexOutOfBoundsException();
                    }
                    return target.getStringValue();
                }
            }
            
            /**
             * Gets (as xml) array of all "old-name" elements
             */
            public org.apache.xmlbeans.XmlID[] xgetOldNameArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    java.util.List targetList = new java.util.ArrayList();
                    get_store().find_all_element_users(OLDNAME$14, targetList);
                    org.apache.xmlbeans.XmlID[] result = new org.apache.xmlbeans.XmlID[targetList.size()];
                    targetList.toArray(result);
                    return result;
                }
            }
            
            /**
             * Gets (as xml) ith "old-name" element
             */
            public org.apache.xmlbeans.XmlID xgetOldNameArray(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlID target = null;
                    target = (org.apache.xmlbeans.XmlID)get_store().find_element_user(OLDNAME$14, i);
                    if (target == null)
                    {
                      throw new IndexOutOfBoundsException();
                    }
                    return (org.apache.xmlbeans.XmlID)target;
                }
            }
            
            /**
             * Returns number of "old-name" element
             */
            public int sizeOfOldNameArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    return get_store().count_elements(OLDNAME$14);
                }
            }
            
            /**
             * Sets array of all "old-name" element
             */
            public void setOldNameArray(java.lang.String[] oldNameArray)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    arraySetterHelper(oldNameArray, OLDNAME$14);
                }
            }
            
            /**
             * Sets ith "old-name" element
             */
            public void setOldNameArray(int i, java.lang.String oldName)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(OLDNAME$14, i);
                    if (target == null)
                    {
                      throw new IndexOutOfBoundsException();
                    }
                    target.setStringValue(oldName);
                }
            }
            
            /**
             * Sets (as xml) array of all "old-name" element
             */
            public void xsetOldNameArray(org.apache.xmlbeans.XmlID[]oldNameArray)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    arraySetterHelper(oldNameArray, OLDNAME$14);
                }
            }
            
            /**
             * Sets (as xml) ith "old-name" element
             */
            public void xsetOldNameArray(int i, org.apache.xmlbeans.XmlID oldName)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlID target = null;
                    target = (org.apache.xmlbeans.XmlID)get_store().find_element_user(OLDNAME$14, i);
                    if (target == null)
                    {
                      throw new IndexOutOfBoundsException();
                    }
                    target.set(oldName);
                }
            }
            
            /**
             * Inserts the value as the ith "old-name" element
             */
            public void insertOldName(int i, java.lang.String oldName)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = 
                      (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(OLDNAME$14, i);
                    target.setStringValue(oldName);
                }
            }
            
            /**
             * Appends the value as the last "old-name" element
             */
            public void addOldName(java.lang.String oldName)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(OLDNAME$14);
                    target.setStringValue(oldName);
                }
            }
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "old-name" element
             */
            public org.apache.xmlbeans.XmlID insertNewOldName(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlID target = null;
                    target = (org.apache.xmlbeans.XmlID)get_store().insert_element_user(OLDNAME$14, i);
                    return target;
                }
            }
            
            /**
             * Appends and returns a new empty value (as xml) as the last "old-name" element
             */
            public org.apache.xmlbeans.XmlID addNewOldName()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlID target = null;
                    target = (org.apache.xmlbeans.XmlID)get_store().add_element_user(OLDNAME$14);
                    return target;
                }
            }
            
            /**
             * Removes the ith "old-name" element
             */
            public void removeOldName(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    get_store().remove_element(OLDNAME$14, i);
                }
            }
            
            /**
             * Gets the "description" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Column.Description getDescription()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Description target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Description)get_store().find_element_user(DESCRIPTION$16, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * Sets the "description" element
             */
            public void setDescription(ch.minova.core.xml.tables.TableDocument.Table.Column.Description description)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Description target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Description)get_store().find_element_user(DESCRIPTION$16, 0);
                    if (target == null)
                    {
                      target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Description)get_store().add_element_user(DESCRIPTION$16);
                    }
                    target.set(description);
                }
            }
            
            /**
             * Appends and returns a new empty "description" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Column.Description addNewDescription()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Description target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Description)get_store().add_element_user(DESCRIPTION$16);
                    return target;
                }
            }
            
            /**
             * Gets the "docbook" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Column.Docbook getDocbook()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Docbook target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Docbook)get_store().find_element_user(DOCBOOK$18, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * Sets the "docbook" element
             */
            public void setDocbook(ch.minova.core.xml.tables.TableDocument.Table.Column.Docbook docbook)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Docbook target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Docbook)get_store().find_element_user(DOCBOOK$18, 0);
                    if (target == null)
                    {
                      target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Docbook)get_store().add_element_user(DOCBOOK$18);
                    }
                    target.set(docbook);
                }
            }
            
            /**
             * Appends and returns a new empty "docbook" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Column.Docbook addNewDocbook()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Column.Docbook target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Docbook)get_store().add_element_user(DOCBOOK$18);
                    return target;
                }
            }
            
            /**
             * Gets the "default" attribute
             */
            public java.lang.String getDefault()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(DEFAULT$20);
                    if (target == null)
                    {
                      return null;
                    }
                    return target.getStringValue();
                }
            }
            
            /**
             * Gets (as xml) the "default" attribute
             */
            public org.apache.xmlbeans.XmlString xgetDefault()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlString target = null;
                    target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(DEFAULT$20);
                    return target;
                }
            }
            
            /**
             * True if has "default" attribute
             */
            public boolean isSetDefault()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    return get_store().find_attribute_user(DEFAULT$20) != null;
                }
            }
            
            /**
             * Sets the "default" attribute
             */
            public void setDefault(java.lang.String xdefault)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(DEFAULT$20);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(DEFAULT$20);
                    }
                    target.setStringValue(xdefault);
                }
            }
            
            /**
             * Sets (as xml) the "default" attribute
             */
            public void xsetDefault(org.apache.xmlbeans.XmlString xdefault)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlString target = null;
                    target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(DEFAULT$20);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(DEFAULT$20);
                    }
                    target.set(xdefault);
                }
            }
            
            /**
             * Unsets the "default" attribute
             */
            public void unsetDefault()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    get_store().remove_attribute(DEFAULT$20);
                }
            }
            
            /**
             * Gets the "name" attribute
             */
            public java.lang.String getName()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$22);
                    if (target == null)
                    {
                      return null;
                    }
                    return target.getStringValue();
                }
            }
            
            /**
             * Gets (as xml) the "name" attribute
             */
            public org.apache.xmlbeans.XmlID xgetName()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlID target = null;
                    target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(NAME$22);
                    return target;
                }
            }
            
            /**
             * Sets the "name" attribute
             */
            public void setName(java.lang.String name)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$22);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NAME$22);
                    }
                    target.setStringValue(name);
                }
            }
            
            /**
             * Sets (as xml) the "name" attribute
             */
            public void xsetName(org.apache.xmlbeans.XmlID name)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlID target = null;
                    target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(NAME$22);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.XmlID)get_store().add_attribute_user(NAME$22);
                    }
                    target.set(name);
                }
            }
            
            /**
             * Gets the "userlevel" attribute
             */
            public java.lang.String getUserlevel()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(USERLEVEL$24);
                    if (target == null)
                    {
                      return null;
                    }
                    return target.getStringValue();
                }
            }
            
            /**
             * Gets (as xml) the "userlevel" attribute
             */
            public org.apache.xmlbeans.XmlString xgetUserlevel()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlString target = null;
                    target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(USERLEVEL$24);
                    return target;
                }
            }
            
            /**
             * True if has "userlevel" attribute
             */
            public boolean isSetUserlevel()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    return get_store().find_attribute_user(USERLEVEL$24) != null;
                }
            }
            
            /**
             * Sets the "userlevel" attribute
             */
            public void setUserlevel(java.lang.String userlevel)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(USERLEVEL$24);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(USERLEVEL$24);
                    }
                    target.setStringValue(userlevel);
                }
            }
            
            /**
             * Sets (as xml) the "userlevel" attribute
             */
            public void xsetUserlevel(org.apache.xmlbeans.XmlString userlevel)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlString target = null;
                    target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(USERLEVEL$24);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(USERLEVEL$24);
                    }
                    target.set(userlevel);
                }
            }
            
            /**
             * Unsets the "userlevel" attribute
             */
            public void unsetUserlevel()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    get_store().remove_attribute(USERLEVEL$24);
                }
            }
            
            /**
             * Gets the "vendor" attribute
             */
            public java.lang.String getVendor()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VENDOR$26);
                    if (target == null)
                    {
                      return null;
                    }
                    return target.getStringValue();
                }
            }
            
            /**
             * Gets (as xml) the "vendor" attribute
             */
            public org.apache.xmlbeans.XmlString xgetVendor()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlString target = null;
                    target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(VENDOR$26);
                    return target;
                }
            }
            
            /**
             * True if has "vendor" attribute
             */
            public boolean isSetVendor()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    return get_store().find_attribute_user(VENDOR$26) != null;
                }
            }
            
            /**
             * Sets the "vendor" attribute
             */
            public void setVendor(java.lang.String vendor)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VENDOR$26);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(VENDOR$26);
                    }
                    target.setStringValue(vendor);
                }
            }
            
            /**
             * Sets (as xml) the "vendor" attribute
             */
            public void xsetVendor(org.apache.xmlbeans.XmlString vendor)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlString target = null;
                    target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(VENDOR$26);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(VENDOR$26);
                    }
                    target.set(vendor);
                }
            }
            
            /**
             * Unsets the "vendor" attribute
             */
            public void unsetVendor()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    get_store().remove_attribute(VENDOR$26);
                }
            }
            /**
             * An XML integer(@).
             *
             * This is a complex type.
             */
            public static class IntegerImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Column.Integer
            {
                private static final long serialVersionUID = 1L;
                
                public IntegerImpl(org.apache.xmlbeans.SchemaType sType)
                {
                    super(sType);
                }
                
                private static final javax.xml.namespace.QName NULLABLE$0 = 
                    new javax.xml.namespace.QName("", "nullable");
                private static final javax.xml.namespace.QName IDENTITY$2 = 
                    new javax.xml.namespace.QName("", "identity");
                
                
                /**
                 * Gets the "nullable" attribute
                 */
                public boolean getNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NULLABLE$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(NULLABLE$0);
                      }
                      if (target == null)
                      {
                        return false;
                      }
                      return target.getBooleanValue();
                    }
                }
                
                /**
                 * Gets (as xml) the "nullable" attribute
                 */
                public org.apache.xmlbeans.XmlBoolean xgetNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlBoolean target = null;
                      target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(NULLABLE$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(NULLABLE$0);
                      }
                      return target;
                    }
                }
                
                /**
                 * True if has "nullable" attribute
                 */
                public boolean isSetNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      return get_store().find_attribute_user(NULLABLE$0) != null;
                    }
                }
                
                /**
                 * Sets the "nullable" attribute
                 */
                public void setNullable(boolean nullable)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NULLABLE$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NULLABLE$0);
                      }
                      target.setBooleanValue(nullable);
                    }
                }
                
                /**
                 * Sets (as xml) the "nullable" attribute
                 */
                public void xsetNullable(org.apache.xmlbeans.XmlBoolean nullable)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlBoolean target = null;
                      target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(NULLABLE$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(NULLABLE$0);
                      }
                      target.set(nullable);
                    }
                }
                
                /**
                 * Unsets the "nullable" attribute
                 */
                public void unsetNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      get_store().remove_attribute(NULLABLE$0);
                    }
                }
                
                /**
                 * Gets the "identity" attribute
                 */
                public boolean getIdentity()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(IDENTITY$2);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(IDENTITY$2);
                      }
                      if (target == null)
                      {
                        return false;
                      }
                      return target.getBooleanValue();
                    }
                }
                
                /**
                 * Gets (as xml) the "identity" attribute
                 */
                public org.apache.xmlbeans.XmlBoolean xgetIdentity()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlBoolean target = null;
                      target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(IDENTITY$2);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(IDENTITY$2);
                      }
                      return target;
                    }
                }
                
                /**
                 * True if has "identity" attribute
                 */
                public boolean isSetIdentity()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      return get_store().find_attribute_user(IDENTITY$2) != null;
                    }
                }
                
                /**
                 * Sets the "identity" attribute
                 */
                public void setIdentity(boolean identity)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(IDENTITY$2);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(IDENTITY$2);
                      }
                      target.setBooleanValue(identity);
                    }
                }
                
                /**
                 * Sets (as xml) the "identity" attribute
                 */
                public void xsetIdentity(org.apache.xmlbeans.XmlBoolean identity)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlBoolean target = null;
                      target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(IDENTITY$2);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(IDENTITY$2);
                      }
                      target.set(identity);
                    }
                }
                
                /**
                 * Unsets the "identity" attribute
                 */
                public void unsetIdentity()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      get_store().remove_attribute(IDENTITY$2);
                    }
                }
            }
            /**
             * An XML bigint(@).
             *
             * This is a complex type.
             */
            public static class BigintImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Column.Bigint
            {
                private static final long serialVersionUID = 1L;
                
                public BigintImpl(org.apache.xmlbeans.SchemaType sType)
                {
                    super(sType);
                }
                
                private static final javax.xml.namespace.QName NULLABLE$0 = 
                    new javax.xml.namespace.QName("", "nullable");
                
                
                /**
                 * Gets the "nullable" attribute
                 */
                public boolean getNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NULLABLE$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(NULLABLE$0);
                      }
                      if (target == null)
                      {
                        return false;
                      }
                      return target.getBooleanValue();
                    }
                }
                
                /**
                 * Gets (as xml) the "nullable" attribute
                 */
                public org.apache.xmlbeans.XmlBoolean xgetNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlBoolean target = null;
                      target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(NULLABLE$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(NULLABLE$0);
                      }
                      return target;
                    }
                }
                
                /**
                 * True if has "nullable" attribute
                 */
                public boolean isSetNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      return get_store().find_attribute_user(NULLABLE$0) != null;
                    }
                }
                
                /**
                 * Sets the "nullable" attribute
                 */
                public void setNullable(boolean nullable)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NULLABLE$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NULLABLE$0);
                      }
                      target.setBooleanValue(nullable);
                    }
                }
                
                /**
                 * Sets (as xml) the "nullable" attribute
                 */
                public void xsetNullable(org.apache.xmlbeans.XmlBoolean nullable)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlBoolean target = null;
                      target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(NULLABLE$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(NULLABLE$0);
                      }
                      target.set(nullable);
                    }
                }
                
                /**
                 * Unsets the "nullable" attribute
                 */
                public void unsetNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      get_store().remove_attribute(NULLABLE$0);
                    }
                }
            }
            /**
             * An XML varchar(@).
             *
             * This is a complex type.
             */
            public static class VarcharImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Column.Varchar
            {
                private static final long serialVersionUID = 1L;
                
                public VarcharImpl(org.apache.xmlbeans.SchemaType sType)
                {
                    super(sType);
                }
                
                private static final javax.xml.namespace.QName LENGTH$0 = 
                    new javax.xml.namespace.QName("", "length");
                private static final javax.xml.namespace.QName NULLABLE$2 = 
                    new javax.xml.namespace.QName("", "nullable");
                
                
                /**
                 * Gets the "length" attribute
                 */
                public java.math.BigInteger getLength()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(LENGTH$0);
                      if (target == null)
                      {
                        return null;
                      }
                      return target.getBigIntegerValue();
                    }
                }
                
                /**
                 * Gets (as xml) the "length" attribute
                 */
                public org.apache.xmlbeans.XmlInteger xgetLength()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlInteger target = null;
                      target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(LENGTH$0);
                      return target;
                    }
                }
                
                /**
                 * Sets the "length" attribute
                 */
                public void setLength(java.math.BigInteger length)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(LENGTH$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(LENGTH$0);
                      }
                      target.setBigIntegerValue(length);
                    }
                }
                
                /**
                 * Sets (as xml) the "length" attribute
                 */
                public void xsetLength(org.apache.xmlbeans.XmlInteger length)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlInteger target = null;
                      target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(LENGTH$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlInteger)get_store().add_attribute_user(LENGTH$0);
                      }
                      target.set(length);
                    }
                }
                
                /**
                 * Gets the "nullable" attribute
                 */
                public boolean getNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NULLABLE$2);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(NULLABLE$2);
                      }
                      if (target == null)
                      {
                        return false;
                      }
                      return target.getBooleanValue();
                    }
                }
                
                /**
                 * Gets (as xml) the "nullable" attribute
                 */
                public org.apache.xmlbeans.XmlBoolean xgetNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlBoolean target = null;
                      target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(NULLABLE$2);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(NULLABLE$2);
                      }
                      return target;
                    }
                }
                
                /**
                 * True if has "nullable" attribute
                 */
                public boolean isSetNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      return get_store().find_attribute_user(NULLABLE$2) != null;
                    }
                }
                
                /**
                 * Sets the "nullable" attribute
                 */
                public void setNullable(boolean nullable)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NULLABLE$2);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NULLABLE$2);
                      }
                      target.setBooleanValue(nullable);
                    }
                }
                
                /**
                 * Sets (as xml) the "nullable" attribute
                 */
                public void xsetNullable(org.apache.xmlbeans.XmlBoolean nullable)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlBoolean target = null;
                      target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(NULLABLE$2);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(NULLABLE$2);
                      }
                      target.set(nullable);
                    }
                }
                
                /**
                 * Unsets the "nullable" attribute
                 */
                public void unsetNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      get_store().remove_attribute(NULLABLE$2);
                    }
                }
            }
            /**
             * An XML datetime(@).
             *
             * This is a complex type.
             */
            public static class DatetimeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Column.Datetime
            {
                private static final long serialVersionUID = 1L;
                
                public DatetimeImpl(org.apache.xmlbeans.SchemaType sType)
                {
                    super(sType);
                }
                
                private static final javax.xml.namespace.QName NULLABLE$0 = 
                    new javax.xml.namespace.QName("", "nullable");
                
                
                /**
                 * Gets the "nullable" attribute
                 */
                public boolean getNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NULLABLE$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(NULLABLE$0);
                      }
                      if (target == null)
                      {
                        return false;
                      }
                      return target.getBooleanValue();
                    }
                }
                
                /**
                 * Gets (as xml) the "nullable" attribute
                 */
                public org.apache.xmlbeans.XmlBoolean xgetNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlBoolean target = null;
                      target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(NULLABLE$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(NULLABLE$0);
                      }
                      return target;
                    }
                }
                
                /**
                 * True if has "nullable" attribute
                 */
                public boolean isSetNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      return get_store().find_attribute_user(NULLABLE$0) != null;
                    }
                }
                
                /**
                 * Sets the "nullable" attribute
                 */
                public void setNullable(boolean nullable)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NULLABLE$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NULLABLE$0);
                      }
                      target.setBooleanValue(nullable);
                    }
                }
                
                /**
                 * Sets (as xml) the "nullable" attribute
                 */
                public void xsetNullable(org.apache.xmlbeans.XmlBoolean nullable)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlBoolean target = null;
                      target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(NULLABLE$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(NULLABLE$0);
                      }
                      target.set(nullable);
                    }
                }
                
                /**
                 * Unsets the "nullable" attribute
                 */
                public void unsetNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      get_store().remove_attribute(NULLABLE$0);
                    }
                }
            }
            /**
             * An XML float(@).
             *
             * This is a complex type.
             */
            public static class FloatImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Column.Float
            {
                private static final long serialVersionUID = 1L;
                
                public FloatImpl(org.apache.xmlbeans.SchemaType sType)
                {
                    super(sType);
                }
                
                private static final javax.xml.namespace.QName NULLABLE$0 = 
                    new javax.xml.namespace.QName("", "nullable");
                private static final javax.xml.namespace.QName DECIMALS$2 = 
                    new javax.xml.namespace.QName("", "decimals");
                
                
                /**
                 * Gets the "nullable" attribute
                 */
                public boolean getNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NULLABLE$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(NULLABLE$0);
                      }
                      if (target == null)
                      {
                        return false;
                      }
                      return target.getBooleanValue();
                    }
                }
                
                /**
                 * Gets (as xml) the "nullable" attribute
                 */
                public org.apache.xmlbeans.XmlBoolean xgetNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlBoolean target = null;
                      target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(NULLABLE$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(NULLABLE$0);
                      }
                      return target;
                    }
                }
                
                /**
                 * True if has "nullable" attribute
                 */
                public boolean isSetNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      return get_store().find_attribute_user(NULLABLE$0) != null;
                    }
                }
                
                /**
                 * Sets the "nullable" attribute
                 */
                public void setNullable(boolean nullable)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NULLABLE$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NULLABLE$0);
                      }
                      target.setBooleanValue(nullable);
                    }
                }
                
                /**
                 * Sets (as xml) the "nullable" attribute
                 */
                public void xsetNullable(org.apache.xmlbeans.XmlBoolean nullable)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlBoolean target = null;
                      target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(NULLABLE$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(NULLABLE$0);
                      }
                      target.set(nullable);
                    }
                }
                
                /**
                 * Unsets the "nullable" attribute
                 */
                public void unsetNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      get_store().remove_attribute(NULLABLE$0);
                    }
                }
                
                /**
                 * Gets the "decimals" attribute
                 */
                public int getDecimals()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(DECIMALS$2);
                      if (target == null)
                      {
                        return 0;
                      }
                      return target.getIntValue();
                    }
                }
                
                /**
                 * Gets (as xml) the "decimals" attribute
                 */
                public ch.minova.core.xml.tables.TableDocument.Table.Column.Float.Decimals xgetDecimals()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.TableDocument.Table.Column.Float.Decimals target = null;
                      target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Float.Decimals)get_store().find_attribute_user(DECIMALS$2);
                      return target;
                    }
                }
                
                /**
                 * Sets the "decimals" attribute
                 */
                public void setDecimals(int decimals)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(DECIMALS$2);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(DECIMALS$2);
                      }
                      target.setIntValue(decimals);
                    }
                }
                
                /**
                 * Sets (as xml) the "decimals" attribute
                 */
                public void xsetDecimals(ch.minova.core.xml.tables.TableDocument.Table.Column.Float.Decimals decimals)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.TableDocument.Table.Column.Float.Decimals target = null;
                      target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Float.Decimals)get_store().find_attribute_user(DECIMALS$2);
                      if (target == null)
                      {
                        target = (ch.minova.core.xml.tables.TableDocument.Table.Column.Float.Decimals)get_store().add_attribute_user(DECIMALS$2);
                      }
                      target.set(decimals);
                    }
                }
                /**
                 * An XML decimals(@).
                 *
                 * This is an atomic type that is a restriction of ch.minova.core.xml.tables.TableDocument$Table$Column$Float$Decimals.
                 */
                public static class DecimalsImpl extends org.apache.xmlbeans.impl.values.JavaIntHolderEx implements ch.minova.core.xml.tables.TableDocument.Table.Column.Float.Decimals
                {
                    private static final long serialVersionUID = 1L;
                    
                    public DecimalsImpl(org.apache.xmlbeans.SchemaType sType)
                    {
                      super(sType, false);
                    }
                    
                    protected DecimalsImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
                    {
                      super(sType, b);
                    }
                }
            }
            /**
             * An XML boolean(@).
             *
             * This is a complex type.
             */
            public static class BooleanImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Column.Boolean
            {
                private static final long serialVersionUID = 1L;
                
                public BooleanImpl(org.apache.xmlbeans.SchemaType sType)
                {
                    super(sType);
                }
                
                private static final javax.xml.namespace.QName NULLABLE$0 = 
                    new javax.xml.namespace.QName("", "nullable");
                
                
                /**
                 * Gets the "nullable" attribute
                 */
                public boolean getNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NULLABLE$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(NULLABLE$0);
                      }
                      if (target == null)
                      {
                        return false;
                      }
                      return target.getBooleanValue();
                    }
                }
                
                /**
                 * Gets (as xml) the "nullable" attribute
                 */
                public org.apache.xmlbeans.XmlBoolean xgetNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlBoolean target = null;
                      target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(NULLABLE$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(NULLABLE$0);
                      }
                      return target;
                    }
                }
                
                /**
                 * True if has "nullable" attribute
                 */
                public boolean isSetNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      return get_store().find_attribute_user(NULLABLE$0) != null;
                    }
                }
                
                /**
                 * Sets the "nullable" attribute
                 */
                public void setNullable(boolean nullable)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NULLABLE$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NULLABLE$0);
                      }
                      target.setBooleanValue(nullable);
                    }
                }
                
                /**
                 * Sets (as xml) the "nullable" attribute
                 */
                public void xsetNullable(org.apache.xmlbeans.XmlBoolean nullable)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlBoolean target = null;
                      target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(NULLABLE$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(NULLABLE$0);
                      }
                      target.set(nullable);
                    }
                }
                
                /**
                 * Unsets the "nullable" attribute
                 */
                public void unsetNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      get_store().remove_attribute(NULLABLE$0);
                    }
                }
            }
            /**
             * An XML money(@).
             *
             * This is a complex type.
             */
            public static class MoneyImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Column.Money
            {
                private static final long serialVersionUID = 1L;
                
                public MoneyImpl(org.apache.xmlbeans.SchemaType sType)
                {
                    super(sType);
                }
                
                private static final javax.xml.namespace.QName NULLABLE$0 = 
                    new javax.xml.namespace.QName("", "nullable");
                
                
                /**
                 * Gets the "nullable" attribute
                 */
                public boolean getNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NULLABLE$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(NULLABLE$0);
                      }
                      if (target == null)
                      {
                        return false;
                      }
                      return target.getBooleanValue();
                    }
                }
                
                /**
                 * Gets (as xml) the "nullable" attribute
                 */
                public org.apache.xmlbeans.XmlBoolean xgetNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlBoolean target = null;
                      target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(NULLABLE$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(NULLABLE$0);
                      }
                      return target;
                    }
                }
                
                /**
                 * True if has "nullable" attribute
                 */
                public boolean isSetNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      return get_store().find_attribute_user(NULLABLE$0) != null;
                    }
                }
                
                /**
                 * Sets the "nullable" attribute
                 */
                public void setNullable(boolean nullable)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NULLABLE$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NULLABLE$0);
                      }
                      target.setBooleanValue(nullable);
                    }
                }
                
                /**
                 * Sets (as xml) the "nullable" attribute
                 */
                public void xsetNullable(org.apache.xmlbeans.XmlBoolean nullable)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlBoolean target = null;
                      target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(NULLABLE$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(NULLABLE$0);
                      }
                      target.set(nullable);
                    }
                }
                
                /**
                 * Unsets the "nullable" attribute
                 */
                public void unsetNullable()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      get_store().remove_attribute(NULLABLE$0);
                    }
                }
            }
            /**
             * An XML description(@).
             *
             * This is a complex type.
             */
            public static class DescriptionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Column.Description
            {
                private static final long serialVersionUID = 1L;
                
                public DescriptionImpl(org.apache.xmlbeans.SchemaType sType)
                {
                    super(sType);
                }
                
                private static final javax.xml.namespace.QName DE$0 = 
                    new javax.xml.namespace.QName("", "de");
                private static final javax.xml.namespace.QName EN$2 = 
                    new javax.xml.namespace.QName("", "en");
                
                
                /**
                 * Gets the "de" element
                 */
                public java.lang.String getDe()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DE$0, 0);
                      if (target == null)
                      {
                        return null;
                      }
                      return target.getStringValue();
                    }
                }
                
                /**
                 * Gets (as xml) the "de" element
                 */
                public org.apache.xmlbeans.XmlString xgetDe()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DE$0, 0);
                      return target;
                    }
                }
                
                /**
                 * Sets the "de" element
                 */
                public void setDe(java.lang.String de)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DE$0, 0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DE$0);
                      }
                      target.setStringValue(de);
                    }
                }
                
                /**
                 * Sets (as xml) the "de" element
                 */
                public void xsetDe(org.apache.xmlbeans.XmlString de)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DE$0, 0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DE$0);
                      }
                      target.set(de);
                    }
                }
                
                /**
                 * Gets the "en" element
                 */
                public java.lang.String getEn()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EN$2, 0);
                      if (target == null)
                      {
                        return null;
                      }
                      return target.getStringValue();
                    }
                }
                
                /**
                 * Gets (as xml) the "en" element
                 */
                public org.apache.xmlbeans.XmlString xgetEn()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EN$2, 0);
                      return target;
                    }
                }
                
                /**
                 * Sets the "en" element
                 */
                public void setEn(java.lang.String en)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EN$2, 0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(EN$2);
                      }
                      target.setStringValue(en);
                    }
                }
                
                /**
                 * Sets (as xml) the "en" element
                 */
                public void xsetEn(org.apache.xmlbeans.XmlString en)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EN$2, 0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(EN$2);
                      }
                      target.set(en);
                    }
                }
            }
            /**
             * An XML docbook(@).
             *
             * This is a complex type.
             */
            public static class DocbookImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Column.Docbook
            {
                private static final long serialVersionUID = 1L;
                
                public DocbookImpl(org.apache.xmlbeans.SchemaType sType)
                {
                    super(sType);
                }
                
                private static final javax.xml.namespace.QName DE$0 = 
                    new javax.xml.namespace.QName("", "de");
                private static final javax.xml.namespace.QName EN$2 = 
                    new javax.xml.namespace.QName("", "en");
                
                
                /**
                 * Gets the "de" element
                 */
                public ch.minova.core.xml.tables.DocbookCode getDe()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(DE$0, 0);
                      if (target == null)
                      {
                        return null;
                      }
                      return target;
                    }
                }
                
                /**
                 * Sets the "de" element
                 */
                public void setDe(ch.minova.core.xml.tables.DocbookCode de)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(DE$0, 0);
                      if (target == null)
                      {
                        target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(DE$0);
                      }
                      target.set(de);
                    }
                }
                
                /**
                 * Appends and returns a new empty "de" element
                 */
                public ch.minova.core.xml.tables.DocbookCode addNewDe()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(DE$0);
                      return target;
                    }
                }
                
                /**
                 * Gets the "en" element
                 */
                public ch.minova.core.xml.tables.DocbookCode getEn()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(EN$2, 0);
                      if (target == null)
                      {
                        return null;
                      }
                      return target;
                    }
                }
                
                /**
                 * Sets the "en" element
                 */
                public void setEn(ch.minova.core.xml.tables.DocbookCode en)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(EN$2, 0);
                      if (target == null)
                      {
                        target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(EN$2);
                      }
                      target.set(en);
                    }
                }
                
                /**
                 * Appends and returns a new empty "en" element
                 */
                public ch.minova.core.xml.tables.DocbookCode addNewEn()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(EN$2);
                      return target;
                    }
                }
            }
        }
        /**
         * An XML primarykey(@).
         *
         * This is a complex type.
         */
        public static class PrimarykeyImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Primarykey
        {
            private static final long serialVersionUID = 1L;
            
            public PrimarykeyImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType);
            }
            
            private static final javax.xml.namespace.QName COLUMN$0 = 
                new javax.xml.namespace.QName("", "column");
            private static final javax.xml.namespace.QName DESCRIPTION$2 = 
                new javax.xml.namespace.QName("", "description");
            private static final javax.xml.namespace.QName DOCBOOK$4 = 
                new javax.xml.namespace.QName("", "docbook");
            
            
            /**
             * Gets array of all "column" elements
             */
            public java.lang.String[] getColumnArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    java.util.List targetList = new java.util.ArrayList();
                    get_store().find_all_element_users(COLUMN$0, targetList);
                    java.lang.String[] result = new java.lang.String[targetList.size()];
                    for (int i = 0, len = targetList.size() ; i < len ; i++)
                        result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getStringValue();
                    return result;
                }
            }
            
            /**
             * Gets ith "column" element
             */
            public java.lang.String getColumnArray(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(COLUMN$0, i);
                    if (target == null)
                    {
                      throw new IndexOutOfBoundsException();
                    }
                    return target.getStringValue();
                }
            }
            
            /**
             * Gets (as xml) array of all "column" elements
             */
            public org.apache.xmlbeans.XmlIDREF[] xgetColumnArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    java.util.List targetList = new java.util.ArrayList();
                    get_store().find_all_element_users(COLUMN$0, targetList);
                    org.apache.xmlbeans.XmlIDREF[] result = new org.apache.xmlbeans.XmlIDREF[targetList.size()];
                    targetList.toArray(result);
                    return result;
                }
            }
            
            /**
             * Gets (as xml) ith "column" element
             */
            public org.apache.xmlbeans.XmlIDREF xgetColumnArray(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlIDREF target = null;
                    target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(COLUMN$0, i);
                    if (target == null)
                    {
                      throw new IndexOutOfBoundsException();
                    }
                    return (org.apache.xmlbeans.XmlIDREF)target;
                }
            }
            
            /**
             * Returns number of "column" element
             */
            public int sizeOfColumnArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    return get_store().count_elements(COLUMN$0);
                }
            }
            
            /**
             * Sets array of all "column" element
             */
            public void setColumnArray(java.lang.String[] columnArray)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    arraySetterHelper(columnArray, COLUMN$0);
                }
            }
            
            /**
             * Sets ith "column" element
             */
            public void setColumnArray(int i, java.lang.String column)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(COLUMN$0, i);
                    if (target == null)
                    {
                      throw new IndexOutOfBoundsException();
                    }
                    target.setStringValue(column);
                }
            }
            
            /**
             * Sets (as xml) array of all "column" element
             */
            public void xsetColumnArray(org.apache.xmlbeans.XmlIDREF[]columnArray)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    arraySetterHelper(columnArray, COLUMN$0);
                }
            }
            
            /**
             * Sets (as xml) ith "column" element
             */
            public void xsetColumnArray(int i, org.apache.xmlbeans.XmlIDREF column)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlIDREF target = null;
                    target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(COLUMN$0, i);
                    if (target == null)
                    {
                      throw new IndexOutOfBoundsException();
                    }
                    target.set(column);
                }
            }
            
            /**
             * Inserts the value as the ith "column" element
             */
            public void insertColumn(int i, java.lang.String column)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = 
                      (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(COLUMN$0, i);
                    target.setStringValue(column);
                }
            }
            
            /**
             * Appends the value as the last "column" element
             */
            public void addColumn(java.lang.String column)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(COLUMN$0);
                    target.setStringValue(column);
                }
            }
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "column" element
             */
            public org.apache.xmlbeans.XmlIDREF insertNewColumn(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlIDREF target = null;
                    target = (org.apache.xmlbeans.XmlIDREF)get_store().insert_element_user(COLUMN$0, i);
                    return target;
                }
            }
            
            /**
             * Appends and returns a new empty value (as xml) as the last "column" element
             */
            public org.apache.xmlbeans.XmlIDREF addNewColumn()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlIDREF target = null;
                    target = (org.apache.xmlbeans.XmlIDREF)get_store().add_element_user(COLUMN$0);
                    return target;
                }
            }
            
            /**
             * Removes the ith "column" element
             */
            public void removeColumn(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    get_store().remove_element(COLUMN$0, i);
                }
            }
            
            /**
             * Gets the "description" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Description getDescription()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Description target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Description)get_store().find_element_user(DESCRIPTION$2, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * Sets the "description" element
             */
            public void setDescription(ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Description description)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Description target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Description)get_store().find_element_user(DESCRIPTION$2, 0);
                    if (target == null)
                    {
                      target = (ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Description)get_store().add_element_user(DESCRIPTION$2);
                    }
                    target.set(description);
                }
            }
            
            /**
             * Appends and returns a new empty "description" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Description addNewDescription()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Description target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Description)get_store().add_element_user(DESCRIPTION$2);
                    return target;
                }
            }
            
            /**
             * Gets the "docbook" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Docbook getDocbook()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Docbook target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Docbook)get_store().find_element_user(DOCBOOK$4, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * Sets the "docbook" element
             */
            public void setDocbook(ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Docbook docbook)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Docbook target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Docbook)get_store().find_element_user(DOCBOOK$4, 0);
                    if (target == null)
                    {
                      target = (ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Docbook)get_store().add_element_user(DOCBOOK$4);
                    }
                    target.set(docbook);
                }
            }
            
            /**
             * Appends and returns a new empty "docbook" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Docbook addNewDocbook()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Docbook target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Docbook)get_store().add_element_user(DOCBOOK$4);
                    return target;
                }
            }
            /**
             * An XML description(@).
             *
             * This is a complex type.
             */
            public static class DescriptionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Description
            {
                private static final long serialVersionUID = 1L;
                
                public DescriptionImpl(org.apache.xmlbeans.SchemaType sType)
                {
                    super(sType);
                }
                
                private static final javax.xml.namespace.QName DE$0 = 
                    new javax.xml.namespace.QName("", "de");
                private static final javax.xml.namespace.QName EN$2 = 
                    new javax.xml.namespace.QName("", "en");
                
                
                /**
                 * Gets the "de" element
                 */
                public java.lang.String getDe()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DE$0, 0);
                      if (target == null)
                      {
                        return null;
                      }
                      return target.getStringValue();
                    }
                }
                
                /**
                 * Gets (as xml) the "de" element
                 */
                public org.apache.xmlbeans.XmlString xgetDe()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DE$0, 0);
                      return target;
                    }
                }
                
                /**
                 * Sets the "de" element
                 */
                public void setDe(java.lang.String de)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DE$0, 0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DE$0);
                      }
                      target.setStringValue(de);
                    }
                }
                
                /**
                 * Sets (as xml) the "de" element
                 */
                public void xsetDe(org.apache.xmlbeans.XmlString de)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DE$0, 0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DE$0);
                      }
                      target.set(de);
                    }
                }
                
                /**
                 * Gets the "en" element
                 */
                public java.lang.String getEn()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EN$2, 0);
                      if (target == null)
                      {
                        return null;
                      }
                      return target.getStringValue();
                    }
                }
                
                /**
                 * Gets (as xml) the "en" element
                 */
                public org.apache.xmlbeans.XmlString xgetEn()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EN$2, 0);
                      return target;
                    }
                }
                
                /**
                 * Sets the "en" element
                 */
                public void setEn(java.lang.String en)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EN$2, 0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(EN$2);
                      }
                      target.setStringValue(en);
                    }
                }
                
                /**
                 * Sets (as xml) the "en" element
                 */
                public void xsetEn(org.apache.xmlbeans.XmlString en)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EN$2, 0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(EN$2);
                      }
                      target.set(en);
                    }
                }
            }
            /**
             * An XML docbook(@).
             *
             * This is a complex type.
             */
            public static class DocbookImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Primarykey.Docbook
            {
                private static final long serialVersionUID = 1L;
                
                public DocbookImpl(org.apache.xmlbeans.SchemaType sType)
                {
                    super(sType);
                }
                
                private static final javax.xml.namespace.QName DE$0 = 
                    new javax.xml.namespace.QName("", "de");
                private static final javax.xml.namespace.QName EN$2 = 
                    new javax.xml.namespace.QName("", "en");
                
                
                /**
                 * Gets the "de" element
                 */
                public ch.minova.core.xml.tables.DocbookCode getDe()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(DE$0, 0);
                      if (target == null)
                      {
                        return null;
                      }
                      return target;
                    }
                }
                
                /**
                 * Sets the "de" element
                 */
                public void setDe(ch.minova.core.xml.tables.DocbookCode de)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(DE$0, 0);
                      if (target == null)
                      {
                        target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(DE$0);
                      }
                      target.set(de);
                    }
                }
                
                /**
                 * Appends and returns a new empty "de" element
                 */
                public ch.minova.core.xml.tables.DocbookCode addNewDe()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(DE$0);
                      return target;
                    }
                }
                
                /**
                 * Gets the "en" element
                 */
                public ch.minova.core.xml.tables.DocbookCode getEn()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(EN$2, 0);
                      if (target == null)
                      {
                        return null;
                      }
                      return target;
                    }
                }
                
                /**
                 * Sets the "en" element
                 */
                public void setEn(ch.minova.core.xml.tables.DocbookCode en)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(EN$2, 0);
                      if (target == null)
                      {
                        target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(EN$2);
                      }
                      target.set(en);
                    }
                }
                
                /**
                 * Appends and returns a new empty "en" element
                 */
                public ch.minova.core.xml.tables.DocbookCode addNewEn()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(EN$2);
                      return target;
                    }
                }
            }
        }
        /**
         * An XML foreignkey(@).
         *
         * This is a complex type.
         */
        public static class ForeignkeyImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Foreignkey
        {
            private static final long serialVersionUID = 1L;
            
            public ForeignkeyImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType);
            }
            
            private static final javax.xml.namespace.QName COLUMN$0 = 
                new javax.xml.namespace.QName("", "column");
            private static final javax.xml.namespace.QName DESCRIPTION$2 = 
                new javax.xml.namespace.QName("", "description");
            private static final javax.xml.namespace.QName DOCBOOK$4 = 
                new javax.xml.namespace.QName("", "docbook");
            private static final javax.xml.namespace.QName REFID$6 = 
                new javax.xml.namespace.QName("", "refid");
            private static final javax.xml.namespace.QName TABLE$8 = 
                new javax.xml.namespace.QName("", "table");
            
            
            /**
             * Gets array of all "column" elements
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column[] getColumnArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    java.util.List targetList = new java.util.ArrayList();
                    get_store().find_all_element_users(COLUMN$0, targetList);
                    ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column[] result = new ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column[targetList.size()];
                    targetList.toArray(result);
                    return result;
                }
            }
            
            /**
             * Gets ith "column" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column getColumnArray(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column)get_store().find_element_user(COLUMN$0, i);
                    if (target == null)
                    {
                      throw new IndexOutOfBoundsException();
                    }
                    return target;
                }
            }
            
            /**
             * Returns number of "column" element
             */
            public int sizeOfColumnArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    return get_store().count_elements(COLUMN$0);
                }
            }
            
            /**
             * Sets array of all "column" element
             */
            public void setColumnArray(ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column[] columnArray)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    arraySetterHelper(columnArray, COLUMN$0);
                }
            }
            
            /**
             * Sets ith "column" element
             */
            public void setColumnArray(int i, ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column column)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column)get_store().find_element_user(COLUMN$0, i);
                    if (target == null)
                    {
                      throw new IndexOutOfBoundsException();
                    }
                    target.set(column);
                }
            }
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "column" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column insertNewColumn(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column)get_store().insert_element_user(COLUMN$0, i);
                    return target;
                }
            }
            
            /**
             * Appends and returns a new empty value (as xml) as the last "column" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column addNewColumn()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column)get_store().add_element_user(COLUMN$0);
                    return target;
                }
            }
            
            /**
             * Removes the ith "column" element
             */
            public void removeColumn(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    get_store().remove_element(COLUMN$0, i);
                }
            }
            
            /**
             * Gets the "description" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Description getDescription()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Description target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Description)get_store().find_element_user(DESCRIPTION$2, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * Sets the "description" element
             */
            public void setDescription(ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Description description)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Description target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Description)get_store().find_element_user(DESCRIPTION$2, 0);
                    if (target == null)
                    {
                      target = (ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Description)get_store().add_element_user(DESCRIPTION$2);
                    }
                    target.set(description);
                }
            }
            
            /**
             * Appends and returns a new empty "description" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Description addNewDescription()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Description target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Description)get_store().add_element_user(DESCRIPTION$2);
                    return target;
                }
            }
            
            /**
             * Gets the "docbook" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Docbook getDocbook()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Docbook target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Docbook)get_store().find_element_user(DOCBOOK$4, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * Sets the "docbook" element
             */
            public void setDocbook(ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Docbook docbook)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Docbook target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Docbook)get_store().find_element_user(DOCBOOK$4, 0);
                    if (target == null)
                    {
                      target = (ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Docbook)get_store().add_element_user(DOCBOOK$4);
                    }
                    target.set(docbook);
                }
            }
            
            /**
             * Appends and returns a new empty "docbook" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Docbook addNewDocbook()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Docbook target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Docbook)get_store().add_element_user(DOCBOOK$4);
                    return target;
                }
            }
            
            /**
             * Gets the "refid" attribute
             */
            public org.apache.xmlbeans.XmlAnySimpleType getRefid()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlAnySimpleType target = null;
                    target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(REFID$6);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * Sets the "refid" attribute
             */
            public void setRefid(org.apache.xmlbeans.XmlAnySimpleType refid)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlAnySimpleType target = null;
                    target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(REFID$6);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(REFID$6);
                    }
                    target.set(refid);
                }
            }
            
            /**
             * Appends and returns a new empty "refid" attribute
             */
            public org.apache.xmlbeans.XmlAnySimpleType addNewRefid()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlAnySimpleType target = null;
                    target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(REFID$6);
                    return target;
                }
            }
            
            /**
             * Gets the "table" attribute
             */
            public org.apache.xmlbeans.XmlAnySimpleType getTable()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlAnySimpleType target = null;
                    target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(TABLE$8);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * Sets the "table" attribute
             */
            public void setTable(org.apache.xmlbeans.XmlAnySimpleType table)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlAnySimpleType target = null;
                    target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(TABLE$8);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(TABLE$8);
                    }
                    target.set(table);
                }
            }
            
            /**
             * Appends and returns a new empty "table" attribute
             */
            public org.apache.xmlbeans.XmlAnySimpleType addNewTable()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlAnySimpleType target = null;
                    target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(TABLE$8);
                    return target;
                }
            }
            /**
             * An XML column(@).
             *
             * This is a complex type.
             */
            public static class ColumnImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Column
            {
                private static final long serialVersionUID = 1L;
                
                public ColumnImpl(org.apache.xmlbeans.SchemaType sType)
                {
                    super(sType);
                }
                
                private static final javax.xml.namespace.QName NAME$0 = 
                    new javax.xml.namespace.QName("", "name");
                private static final javax.xml.namespace.QName REFID$2 = 
                    new javax.xml.namespace.QName("", "refid");
                
                
                /**
                 * Gets the "name" attribute
                 */
                public java.lang.String getName()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$0);
                      if (target == null)
                      {
                        return null;
                      }
                      return target.getStringValue();
                    }
                }
                
                /**
                 * Gets (as xml) the "name" attribute
                 */
                public org.apache.xmlbeans.XmlString xgetName()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$0);
                      return target;
                    }
                }
                
                /**
                 * Sets the "name" attribute
                 */
                public void setName(java.lang.String name)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NAME$0);
                      }
                      target.setStringValue(name);
                    }
                }
                
                /**
                 * Sets (as xml) the "name" attribute
                 */
                public void xsetName(org.apache.xmlbeans.XmlString name)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(NAME$0);
                      }
                      target.set(name);
                    }
                }
                
                /**
                 * Gets the "refid" attribute
                 */
                public java.lang.String getRefid()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(REFID$2);
                      if (target == null)
                      {
                        return null;
                      }
                      return target.getStringValue();
                    }
                }
                
                /**
                 * Gets (as xml) the "refid" attribute
                 */
                public org.apache.xmlbeans.XmlString xgetRefid()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(REFID$2);
                      return target;
                    }
                }
                
                /**
                 * True if has "refid" attribute
                 */
                public boolean isSetRefid()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      return get_store().find_attribute_user(REFID$2) != null;
                    }
                }
                
                /**
                 * Sets the "refid" attribute
                 */
                public void setRefid(java.lang.String refid)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(REFID$2);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(REFID$2);
                      }
                      target.setStringValue(refid);
                    }
                }
                
                /**
                 * Sets (as xml) the "refid" attribute
                 */
                public void xsetRefid(org.apache.xmlbeans.XmlString refid)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(REFID$2);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(REFID$2);
                      }
                      target.set(refid);
                    }
                }
                
                /**
                 * Unsets the "refid" attribute
                 */
                public void unsetRefid()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      get_store().remove_attribute(REFID$2);
                    }
                }
            }
            /**
             * An XML description(@).
             *
             * This is a complex type.
             */
            public static class DescriptionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Description
            {
                private static final long serialVersionUID = 1L;
                
                public DescriptionImpl(org.apache.xmlbeans.SchemaType sType)
                {
                    super(sType);
                }
                
                private static final javax.xml.namespace.QName DE$0 = 
                    new javax.xml.namespace.QName("", "de");
                private static final javax.xml.namespace.QName EN$2 = 
                    new javax.xml.namespace.QName("", "en");
                
                
                /**
                 * Gets the "de" element
                 */
                public java.lang.String getDe()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DE$0, 0);
                      if (target == null)
                      {
                        return null;
                      }
                      return target.getStringValue();
                    }
                }
                
                /**
                 * Gets (as xml) the "de" element
                 */
                public org.apache.xmlbeans.XmlString xgetDe()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DE$0, 0);
                      return target;
                    }
                }
                
                /**
                 * Sets the "de" element
                 */
                public void setDe(java.lang.String de)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DE$0, 0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DE$0);
                      }
                      target.setStringValue(de);
                    }
                }
                
                /**
                 * Sets (as xml) the "de" element
                 */
                public void xsetDe(org.apache.xmlbeans.XmlString de)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DE$0, 0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DE$0);
                      }
                      target.set(de);
                    }
                }
                
                /**
                 * Gets the "en" element
                 */
                public java.lang.String getEn()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EN$2, 0);
                      if (target == null)
                      {
                        return null;
                      }
                      return target.getStringValue();
                    }
                }
                
                /**
                 * Gets (as xml) the "en" element
                 */
                public org.apache.xmlbeans.XmlString xgetEn()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EN$2, 0);
                      return target;
                    }
                }
                
                /**
                 * Sets the "en" element
                 */
                public void setEn(java.lang.String en)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EN$2, 0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(EN$2);
                      }
                      target.setStringValue(en);
                    }
                }
                
                /**
                 * Sets (as xml) the "en" element
                 */
                public void xsetEn(org.apache.xmlbeans.XmlString en)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EN$2, 0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(EN$2);
                      }
                      target.set(en);
                    }
                }
            }
            /**
             * An XML docbook(@).
             *
             * This is a complex type.
             */
            public static class DocbookImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Foreignkey.Docbook
            {
                private static final long serialVersionUID = 1L;
                
                public DocbookImpl(org.apache.xmlbeans.SchemaType sType)
                {
                    super(sType);
                }
                
                private static final javax.xml.namespace.QName DE$0 = 
                    new javax.xml.namespace.QName("", "de");
                private static final javax.xml.namespace.QName EN$2 = 
                    new javax.xml.namespace.QName("", "en");
                
                
                /**
                 * Gets the "de" element
                 */
                public ch.minova.core.xml.tables.DocbookCode getDe()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(DE$0, 0);
                      if (target == null)
                      {
                        return null;
                      }
                      return target;
                    }
                }
                
                /**
                 * Sets the "de" element
                 */
                public void setDe(ch.minova.core.xml.tables.DocbookCode de)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(DE$0, 0);
                      if (target == null)
                      {
                        target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(DE$0);
                      }
                      target.set(de);
                    }
                }
                
                /**
                 * Appends and returns a new empty "de" element
                 */
                public ch.minova.core.xml.tables.DocbookCode addNewDe()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(DE$0);
                      return target;
                    }
                }
                
                /**
                 * Gets the "en" element
                 */
                public ch.minova.core.xml.tables.DocbookCode getEn()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(EN$2, 0);
                      if (target == null)
                      {
                        return null;
                      }
                      return target;
                    }
                }
                
                /**
                 * Sets the "en" element
                 */
                public void setEn(ch.minova.core.xml.tables.DocbookCode en)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(EN$2, 0);
                      if (target == null)
                      {
                        target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(EN$2);
                      }
                      target.set(en);
                    }
                }
                
                /**
                 * Appends and returns a new empty "en" element
                 */
                public ch.minova.core.xml.tables.DocbookCode addNewEn()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(EN$2);
                      return target;
                    }
                }
            }
        }
        /**
         * An XML uniquekey(@).
         *
         * This is a complex type.
         */
        public static class UniquekeyImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Uniquekey
        {
            private static final long serialVersionUID = 1L;
            
            public UniquekeyImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType);
            }
            
            private static final javax.xml.namespace.QName COLUMN$0 = 
                new javax.xml.namespace.QName("", "column");
            private static final javax.xml.namespace.QName DESCRIPTION$2 = 
                new javax.xml.namespace.QName("", "description");
            private static final javax.xml.namespace.QName DOCBOOK$4 = 
                new javax.xml.namespace.QName("", "docbook");
            private static final javax.xml.namespace.QName NAME$6 = 
                new javax.xml.namespace.QName("", "name");
            
            
            /**
             * Gets array of all "column" elements
             */
            public java.lang.String[] getColumnArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    java.util.List targetList = new java.util.ArrayList();
                    get_store().find_all_element_users(COLUMN$0, targetList);
                    java.lang.String[] result = new java.lang.String[targetList.size()];
                    for (int i = 0, len = targetList.size() ; i < len ; i++)
                        result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getStringValue();
                    return result;
                }
            }
            
            /**
             * Gets ith "column" element
             */
            public java.lang.String getColumnArray(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(COLUMN$0, i);
                    if (target == null)
                    {
                      throw new IndexOutOfBoundsException();
                    }
                    return target.getStringValue();
                }
            }
            
            /**
             * Gets (as xml) array of all "column" elements
             */
            public org.apache.xmlbeans.XmlIDREF[] xgetColumnArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    java.util.List targetList = new java.util.ArrayList();
                    get_store().find_all_element_users(COLUMN$0, targetList);
                    org.apache.xmlbeans.XmlIDREF[] result = new org.apache.xmlbeans.XmlIDREF[targetList.size()];
                    targetList.toArray(result);
                    return result;
                }
            }
            
            /**
             * Gets (as xml) ith "column" element
             */
            public org.apache.xmlbeans.XmlIDREF xgetColumnArray(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlIDREF target = null;
                    target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(COLUMN$0, i);
                    if (target == null)
                    {
                      throw new IndexOutOfBoundsException();
                    }
                    return (org.apache.xmlbeans.XmlIDREF)target;
                }
            }
            
            /**
             * Returns number of "column" element
             */
            public int sizeOfColumnArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    return get_store().count_elements(COLUMN$0);
                }
            }
            
            /**
             * Sets array of all "column" element
             */
            public void setColumnArray(java.lang.String[] columnArray)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    arraySetterHelper(columnArray, COLUMN$0);
                }
            }
            
            /**
             * Sets ith "column" element
             */
            public void setColumnArray(int i, java.lang.String column)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(COLUMN$0, i);
                    if (target == null)
                    {
                      throw new IndexOutOfBoundsException();
                    }
                    target.setStringValue(column);
                }
            }
            
            /**
             * Sets (as xml) array of all "column" element
             */
            public void xsetColumnArray(org.apache.xmlbeans.XmlIDREF[]columnArray)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    arraySetterHelper(columnArray, COLUMN$0);
                }
            }
            
            /**
             * Sets (as xml) ith "column" element
             */
            public void xsetColumnArray(int i, org.apache.xmlbeans.XmlIDREF column)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlIDREF target = null;
                    target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(COLUMN$0, i);
                    if (target == null)
                    {
                      throw new IndexOutOfBoundsException();
                    }
                    target.set(column);
                }
            }
            
            /**
             * Inserts the value as the ith "column" element
             */
            public void insertColumn(int i, java.lang.String column)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = 
                      (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(COLUMN$0, i);
                    target.setStringValue(column);
                }
            }
            
            /**
             * Appends the value as the last "column" element
             */
            public void addColumn(java.lang.String column)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(COLUMN$0);
                    target.setStringValue(column);
                }
            }
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "column" element
             */
            public org.apache.xmlbeans.XmlIDREF insertNewColumn(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlIDREF target = null;
                    target = (org.apache.xmlbeans.XmlIDREF)get_store().insert_element_user(COLUMN$0, i);
                    return target;
                }
            }
            
            /**
             * Appends and returns a new empty value (as xml) as the last "column" element
             */
            public org.apache.xmlbeans.XmlIDREF addNewColumn()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlIDREF target = null;
                    target = (org.apache.xmlbeans.XmlIDREF)get_store().add_element_user(COLUMN$0);
                    return target;
                }
            }
            
            /**
             * Removes the ith "column" element
             */
            public void removeColumn(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    get_store().remove_element(COLUMN$0, i);
                }
            }
            
            /**
             * Gets the "description" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Description getDescription()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Description target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Description)get_store().find_element_user(DESCRIPTION$2, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * Sets the "description" element
             */
            public void setDescription(ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Description description)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Description target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Description)get_store().find_element_user(DESCRIPTION$2, 0);
                    if (target == null)
                    {
                      target = (ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Description)get_store().add_element_user(DESCRIPTION$2);
                    }
                    target.set(description);
                }
            }
            
            /**
             * Appends and returns a new empty "description" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Description addNewDescription()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Description target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Description)get_store().add_element_user(DESCRIPTION$2);
                    return target;
                }
            }
            
            /**
             * Gets the "docbook" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Docbook getDocbook()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Docbook target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Docbook)get_store().find_element_user(DOCBOOK$4, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * Sets the "docbook" element
             */
            public void setDocbook(ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Docbook docbook)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Docbook target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Docbook)get_store().find_element_user(DOCBOOK$4, 0);
                    if (target == null)
                    {
                      target = (ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Docbook)get_store().add_element_user(DOCBOOK$4);
                    }
                    target.set(docbook);
                }
            }
            
            /**
             * Appends and returns a new empty "docbook" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Docbook addNewDocbook()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Docbook target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Docbook)get_store().add_element_user(DOCBOOK$4);
                    return target;
                }
            }
            
            /**
             * Gets the "name" attribute
             */
            public java.lang.String getName()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$6);
                    if (target == null)
                    {
                      return null;
                    }
                    return target.getStringValue();
                }
            }
            
            /**
             * Gets (as xml) the "name" attribute
             */
            public org.apache.xmlbeans.XmlString xgetName()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlString target = null;
                    target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$6);
                    return target;
                }
            }
            
            /**
             * Sets the "name" attribute
             */
            public void setName(java.lang.String name)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$6);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NAME$6);
                    }
                    target.setStringValue(name);
                }
            }
            
            /**
             * Sets (as xml) the "name" attribute
             */
            public void xsetName(org.apache.xmlbeans.XmlString name)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlString target = null;
                    target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$6);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(NAME$6);
                    }
                    target.set(name);
                }
            }
            /**
             * An XML description(@).
             *
             * This is a complex type.
             */
            public static class DescriptionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Description
            {
                private static final long serialVersionUID = 1L;
                
                public DescriptionImpl(org.apache.xmlbeans.SchemaType sType)
                {
                    super(sType);
                }
                
                private static final javax.xml.namespace.QName DE$0 = 
                    new javax.xml.namespace.QName("", "de");
                private static final javax.xml.namespace.QName EN$2 = 
                    new javax.xml.namespace.QName("", "en");
                
                
                /**
                 * Gets the "de" element
                 */
                public java.lang.String getDe()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DE$0, 0);
                      if (target == null)
                      {
                        return null;
                      }
                      return target.getStringValue();
                    }
                }
                
                /**
                 * Gets (as xml) the "de" element
                 */
                public org.apache.xmlbeans.XmlString xgetDe()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DE$0, 0);
                      return target;
                    }
                }
                
                /**
                 * Sets the "de" element
                 */
                public void setDe(java.lang.String de)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DE$0, 0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DE$0);
                      }
                      target.setStringValue(de);
                    }
                }
                
                /**
                 * Sets (as xml) the "de" element
                 */
                public void xsetDe(org.apache.xmlbeans.XmlString de)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DE$0, 0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DE$0);
                      }
                      target.set(de);
                    }
                }
                
                /**
                 * Gets the "en" element
                 */
                public java.lang.String getEn()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EN$2, 0);
                      if (target == null)
                      {
                        return null;
                      }
                      return target.getStringValue();
                    }
                }
                
                /**
                 * Gets (as xml) the "en" element
                 */
                public org.apache.xmlbeans.XmlString xgetEn()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EN$2, 0);
                      return target;
                    }
                }
                
                /**
                 * Sets the "en" element
                 */
                public void setEn(java.lang.String en)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EN$2, 0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(EN$2);
                      }
                      target.setStringValue(en);
                    }
                }
                
                /**
                 * Sets (as xml) the "en" element
                 */
                public void xsetEn(org.apache.xmlbeans.XmlString en)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EN$2, 0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(EN$2);
                      }
                      target.set(en);
                    }
                }
            }
            /**
             * An XML docbook(@).
             *
             * This is a complex type.
             */
            public static class DocbookImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Uniquekey.Docbook
            {
                private static final long serialVersionUID = 1L;
                
                public DocbookImpl(org.apache.xmlbeans.SchemaType sType)
                {
                    super(sType);
                }
                
                private static final javax.xml.namespace.QName DE$0 = 
                    new javax.xml.namespace.QName("", "de");
                private static final javax.xml.namespace.QName EN$2 = 
                    new javax.xml.namespace.QName("", "en");
                
                
                /**
                 * Gets the "de" element
                 */
                public ch.minova.core.xml.tables.DocbookCode getDe()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(DE$0, 0);
                      if (target == null)
                      {
                        return null;
                      }
                      return target;
                    }
                }
                
                /**
                 * Sets the "de" element
                 */
                public void setDe(ch.minova.core.xml.tables.DocbookCode de)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(DE$0, 0);
                      if (target == null)
                      {
                        target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(DE$0);
                      }
                      target.set(de);
                    }
                }
                
                /**
                 * Appends and returns a new empty "de" element
                 */
                public ch.minova.core.xml.tables.DocbookCode addNewDe()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(DE$0);
                      return target;
                    }
                }
                
                /**
                 * Gets the "en" element
                 */
                public ch.minova.core.xml.tables.DocbookCode getEn()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(EN$2, 0);
                      if (target == null)
                      {
                        return null;
                      }
                      return target;
                    }
                }
                
                /**
                 * Sets the "en" element
                 */
                public void setEn(ch.minova.core.xml.tables.DocbookCode en)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(EN$2, 0);
                      if (target == null)
                      {
                        target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(EN$2);
                      }
                      target.set(en);
                    }
                }
                
                /**
                 * Appends and returns a new empty "en" element
                 */
                public ch.minova.core.xml.tables.DocbookCode addNewEn()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(EN$2);
                      return target;
                    }
                }
            }
        }
        /**
         * An XML oldconstraint(@).
         *
         * This is a complex type.
         */
        public static class OldconstraintImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint
        {
            private static final long serialVersionUID = 1L;
            
            public OldconstraintImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType);
            }
            
            private static final javax.xml.namespace.QName DESCRIPTION$0 = 
                new javax.xml.namespace.QName("", "description");
            private static final javax.xml.namespace.QName DOCBOOK$2 = 
                new javax.xml.namespace.QName("", "docbook");
            private static final javax.xml.namespace.QName NAME$4 = 
                new javax.xml.namespace.QName("", "name");
            
            
            /**
             * Gets the "description" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Description getDescription()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Description target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Description)get_store().find_element_user(DESCRIPTION$0, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * Sets the "description" element
             */
            public void setDescription(ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Description description)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Description target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Description)get_store().find_element_user(DESCRIPTION$0, 0);
                    if (target == null)
                    {
                      target = (ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Description)get_store().add_element_user(DESCRIPTION$0);
                    }
                    target.set(description);
                }
            }
            
            /**
             * Appends and returns a new empty "description" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Description addNewDescription()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Description target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Description)get_store().add_element_user(DESCRIPTION$0);
                    return target;
                }
            }
            
            /**
             * Gets the "docbook" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Docbook getDocbook()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Docbook target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Docbook)get_store().find_element_user(DOCBOOK$2, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * Sets the "docbook" element
             */
            public void setDocbook(ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Docbook docbook)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Docbook target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Docbook)get_store().find_element_user(DOCBOOK$2, 0);
                    if (target == null)
                    {
                      target = (ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Docbook)get_store().add_element_user(DOCBOOK$2);
                    }
                    target.set(docbook);
                }
            }
            
            /**
             * Appends and returns a new empty "docbook" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Docbook addNewDocbook()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Docbook target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Docbook)get_store().add_element_user(DOCBOOK$2);
                    return target;
                }
            }
            
            /**
             * Gets the "name" attribute
             */
            public java.lang.String getName()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$4);
                    if (target == null)
                    {
                      return null;
                    }
                    return target.getStringValue();
                }
            }
            
            /**
             * Gets (as xml) the "name" attribute
             */
            public org.apache.xmlbeans.XmlString xgetName()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlString target = null;
                    target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$4);
                    return target;
                }
            }
            
            /**
             * True if has "name" attribute
             */
            public boolean isSetName()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    return get_store().find_attribute_user(NAME$4) != null;
                }
            }
            
            /**
             * Sets the "name" attribute
             */
            public void setName(java.lang.String name)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$4);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NAME$4);
                    }
                    target.setStringValue(name);
                }
            }
            
            /**
             * Sets (as xml) the "name" attribute
             */
            public void xsetName(org.apache.xmlbeans.XmlString name)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlString target = null;
                    target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$4);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(NAME$4);
                    }
                    target.set(name);
                }
            }
            
            /**
             * Unsets the "name" attribute
             */
            public void unsetName()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    get_store().remove_attribute(NAME$4);
                }
            }
            /**
             * An XML description(@).
             *
             * This is a complex type.
             */
            public static class DescriptionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Description
            {
                private static final long serialVersionUID = 1L;
                
                public DescriptionImpl(org.apache.xmlbeans.SchemaType sType)
                {
                    super(sType);
                }
                
                private static final javax.xml.namespace.QName DE$0 = 
                    new javax.xml.namespace.QName("", "de");
                private static final javax.xml.namespace.QName EN$2 = 
                    new javax.xml.namespace.QName("", "en");
                
                
                /**
                 * Gets the "de" element
                 */
                public java.lang.String getDe()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DE$0, 0);
                      if (target == null)
                      {
                        return null;
                      }
                      return target.getStringValue();
                    }
                }
                
                /**
                 * Gets (as xml) the "de" element
                 */
                public org.apache.xmlbeans.XmlString xgetDe()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DE$0, 0);
                      return target;
                    }
                }
                
                /**
                 * Sets the "de" element
                 */
                public void setDe(java.lang.String de)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DE$0, 0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DE$0);
                      }
                      target.setStringValue(de);
                    }
                }
                
                /**
                 * Sets (as xml) the "de" element
                 */
                public void xsetDe(org.apache.xmlbeans.XmlString de)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DE$0, 0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DE$0);
                      }
                      target.set(de);
                    }
                }
                
                /**
                 * Gets the "en" element
                 */
                public java.lang.String getEn()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EN$2, 0);
                      if (target == null)
                      {
                        return null;
                      }
                      return target.getStringValue();
                    }
                }
                
                /**
                 * Gets (as xml) the "en" element
                 */
                public org.apache.xmlbeans.XmlString xgetEn()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EN$2, 0);
                      return target;
                    }
                }
                
                /**
                 * Sets the "en" element
                 */
                public void setEn(java.lang.String en)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EN$2, 0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(EN$2);
                      }
                      target.setStringValue(en);
                    }
                }
                
                /**
                 * Sets (as xml) the "en" element
                 */
                public void xsetEn(org.apache.xmlbeans.XmlString en)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EN$2, 0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(EN$2);
                      }
                      target.set(en);
                    }
                }
            }
            /**
             * An XML docbook(@).
             *
             * This is a complex type.
             */
            public static class DocbookImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Oldconstraint.Docbook
            {
                private static final long serialVersionUID = 1L;
                
                public DocbookImpl(org.apache.xmlbeans.SchemaType sType)
                {
                    super(sType);
                }
                
                private static final javax.xml.namespace.QName DE$0 = 
                    new javax.xml.namespace.QName("", "de");
                private static final javax.xml.namespace.QName EN$2 = 
                    new javax.xml.namespace.QName("", "en");
                
                
                /**
                 * Gets the "de" element
                 */
                public ch.minova.core.xml.tables.DocbookCode getDe()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(DE$0, 0);
                      if (target == null)
                      {
                        return null;
                      }
                      return target;
                    }
                }
                
                /**
                 * Sets the "de" element
                 */
                public void setDe(ch.minova.core.xml.tables.DocbookCode de)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(DE$0, 0);
                      if (target == null)
                      {
                        target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(DE$0);
                      }
                      target.set(de);
                    }
                }
                
                /**
                 * Appends and returns a new empty "de" element
                 */
                public ch.minova.core.xml.tables.DocbookCode addNewDe()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(DE$0);
                      return target;
                    }
                }
                
                /**
                 * Gets the "en" element
                 */
                public ch.minova.core.xml.tables.DocbookCode getEn()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(EN$2, 0);
                      if (target == null)
                      {
                        return null;
                      }
                      return target;
                    }
                }
                
                /**
                 * Sets the "en" element
                 */
                public void setEn(ch.minova.core.xml.tables.DocbookCode en)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(EN$2, 0);
                      if (target == null)
                      {
                        target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(EN$2);
                      }
                      target.set(en);
                    }
                }
                
                /**
                 * Appends and returns a new empty "en" element
                 */
                public ch.minova.core.xml.tables.DocbookCode addNewEn()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.DocbookCode target = null;
                      target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(EN$2);
                      return target;
                    }
                }
            }
        }
        /**
         * An XML exec-sql(@).
         *
         * This is a complex type.
         */
        public static class ExecSqlImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.ExecSql
        {
            private static final long serialVersionUID = 1L;
            
            public ExecSqlImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType);
            }
            
            private static final javax.xml.namespace.QName EXECUTE$0 = 
                new javax.xml.namespace.QName("", "execute");
            
            
            /**
             * Gets the "execute" attribute
             */
            public ch.minova.core.xml.tables.TableDocument.Table.ExecSql.Execute.Enum getExecute()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(EXECUTE$0);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(EXECUTE$0);
                    }
                    if (target == null)
                    {
                      return null;
                    }
                    return (ch.minova.core.xml.tables.TableDocument.Table.ExecSql.Execute.Enum)target.getEnumValue();
                }
            }
            
            /**
             * Gets (as xml) the "execute" attribute
             */
            public ch.minova.core.xml.tables.TableDocument.Table.ExecSql.Execute xgetExecute()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.ExecSql.Execute target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.ExecSql.Execute)get_store().find_attribute_user(EXECUTE$0);
                    if (target == null)
                    {
                      target = (ch.minova.core.xml.tables.TableDocument.Table.ExecSql.Execute)get_default_attribute_value(EXECUTE$0);
                    }
                    return target;
                }
            }
            
            /**
             * True if has "execute" attribute
             */
            public boolean isSetExecute()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    return get_store().find_attribute_user(EXECUTE$0) != null;
                }
            }
            
            /**
             * Sets the "execute" attribute
             */
            public void setExecute(ch.minova.core.xml.tables.TableDocument.Table.ExecSql.Execute.Enum execute)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(EXECUTE$0);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(EXECUTE$0);
                    }
                    target.setEnumValue(execute);
                }
            }
            
            /**
             * Sets (as xml) the "execute" attribute
             */
            public void xsetExecute(ch.minova.core.xml.tables.TableDocument.Table.ExecSql.Execute execute)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.ExecSql.Execute target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.ExecSql.Execute)get_store().find_attribute_user(EXECUTE$0);
                    if (target == null)
                    {
                      target = (ch.minova.core.xml.tables.TableDocument.Table.ExecSql.Execute)get_store().add_attribute_user(EXECUTE$0);
                    }
                    target.set(execute);
                }
            }
            
            /**
             * Unsets the "execute" attribute
             */
            public void unsetExecute()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    get_store().remove_attribute(EXECUTE$0);
                }
            }
            /**
             * An XML execute(@).
             *
             * This is an atomic type that is a restriction of ch.minova.core.xml.tables.TableDocument$Table$ExecSql$Execute.
             */
            public static class ExecuteImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements ch.minova.core.xml.tables.TableDocument.Table.ExecSql.Execute
            {
                private static final long serialVersionUID = 1L;
                
                public ExecuteImpl(org.apache.xmlbeans.SchemaType sType)
                {
                    super(sType, false);
                }
                
                protected ExecuteImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
                {
                    super(sType, b);
                }
            }
        }
        /**
         * An XML values(@).
         *
         * This is a complex type.
         */
        public static class ValuesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Values
        {
            private static final long serialVersionUID = 1L;
            
            public ValuesImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType);
            }
            
            private static final javax.xml.namespace.QName COLUMN$0 = 
                new javax.xml.namespace.QName("", "column");
            private static final javax.xml.namespace.QName ROW$2 = 
                new javax.xml.namespace.QName("", "row");
            
            
            /**
             * Gets array of all "column" elements
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Values.Column[] getColumnArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    java.util.List targetList = new java.util.ArrayList();
                    get_store().find_all_element_users(COLUMN$0, targetList);
                    ch.minova.core.xml.tables.TableDocument.Table.Values.Column[] result = new ch.minova.core.xml.tables.TableDocument.Table.Values.Column[targetList.size()];
                    targetList.toArray(result);
                    return result;
                }
            }
            
            /**
             * Gets ith "column" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Values.Column getColumnArray(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Values.Column target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Values.Column)get_store().find_element_user(COLUMN$0, i);
                    if (target == null)
                    {
                      throw new IndexOutOfBoundsException();
                    }
                    return target;
                }
            }
            
            /**
             * Returns number of "column" element
             */
            public int sizeOfColumnArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    return get_store().count_elements(COLUMN$0);
                }
            }
            
            /**
             * Sets array of all "column" element
             */
            public void setColumnArray(ch.minova.core.xml.tables.TableDocument.Table.Values.Column[] columnArray)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    arraySetterHelper(columnArray, COLUMN$0);
                }
            }
            
            /**
             * Sets ith "column" element
             */
            public void setColumnArray(int i, ch.minova.core.xml.tables.TableDocument.Table.Values.Column column)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Values.Column target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Values.Column)get_store().find_element_user(COLUMN$0, i);
                    if (target == null)
                    {
                      throw new IndexOutOfBoundsException();
                    }
                    target.set(column);
                }
            }
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "column" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Values.Column insertNewColumn(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Values.Column target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Values.Column)get_store().insert_element_user(COLUMN$0, i);
                    return target;
                }
            }
            
            /**
             * Appends and returns a new empty value (as xml) as the last "column" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Values.Column addNewColumn()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Values.Column target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Values.Column)get_store().add_element_user(COLUMN$0);
                    return target;
                }
            }
            
            /**
             * Removes the ith "column" element
             */
            public void removeColumn(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    get_store().remove_element(COLUMN$0, i);
                }
            }
            
            /**
             * Gets array of all "row" elements
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Values.Row[] getRowArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    java.util.List targetList = new java.util.ArrayList();
                    get_store().find_all_element_users(ROW$2, targetList);
                    ch.minova.core.xml.tables.TableDocument.Table.Values.Row[] result = new ch.minova.core.xml.tables.TableDocument.Table.Values.Row[targetList.size()];
                    targetList.toArray(result);
                    return result;
                }
            }
            
            /**
             * Gets ith "row" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Values.Row getRowArray(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Values.Row target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Values.Row)get_store().find_element_user(ROW$2, i);
                    if (target == null)
                    {
                      throw new IndexOutOfBoundsException();
                    }
                    return target;
                }
            }
            
            /**
             * Returns number of "row" element
             */
            public int sizeOfRowArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    return get_store().count_elements(ROW$2);
                }
            }
            
            /**
             * Sets array of all "row" element
             */
            public void setRowArray(ch.minova.core.xml.tables.TableDocument.Table.Values.Row[] rowArray)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    arraySetterHelper(rowArray, ROW$2);
                }
            }
            
            /**
             * Sets ith "row" element
             */
            public void setRowArray(int i, ch.minova.core.xml.tables.TableDocument.Table.Values.Row row)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Values.Row target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Values.Row)get_store().find_element_user(ROW$2, i);
                    if (target == null)
                    {
                      throw new IndexOutOfBoundsException();
                    }
                    target.set(row);
                }
            }
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "row" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Values.Row insertNewRow(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Values.Row target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Values.Row)get_store().insert_element_user(ROW$2, i);
                    return target;
                }
            }
            
            /**
             * Appends and returns a new empty value (as xml) as the last "row" element
             */
            public ch.minova.core.xml.tables.TableDocument.Table.Values.Row addNewRow()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.xml.tables.TableDocument.Table.Values.Row target = null;
                    target = (ch.minova.core.xml.tables.TableDocument.Table.Values.Row)get_store().add_element_user(ROW$2);
                    return target;
                }
            }
            
            /**
             * Removes the ith "row" element
             */
            public void removeRow(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    get_store().remove_element(ROW$2, i);
                }
            }
            /**
             * An XML column(@).
             *
             * This is a complex type.
             */
            public static class ColumnImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Values.Column
            {
                private static final long serialVersionUID = 1L;
                
                public ColumnImpl(org.apache.xmlbeans.SchemaType sType)
                {
                    super(sType);
                }
                
                private static final javax.xml.namespace.QName REFID$0 = 
                    new javax.xml.namespace.QName("", "refid");
                
                
                /**
                 * Gets the "refid" attribute
                 */
                public java.lang.String getRefid()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(REFID$0);
                      if (target == null)
                      {
                        return null;
                      }
                      return target.getStringValue();
                    }
                }
                
                /**
                 * Gets (as xml) the "refid" attribute
                 */
                public org.apache.xmlbeans.XmlIDREF xgetRefid()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlIDREF target = null;
                      target = (org.apache.xmlbeans.XmlIDREF)get_store().find_attribute_user(REFID$0);
                      return target;
                    }
                }
                
                /**
                 * Sets the "refid" attribute
                 */
                public void setRefid(java.lang.String refid)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(REFID$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(REFID$0);
                      }
                      target.setStringValue(refid);
                    }
                }
                
                /**
                 * Sets (as xml) the "refid" attribute
                 */
                public void xsetRefid(org.apache.xmlbeans.XmlIDREF refid)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlIDREF target = null;
                      target = (org.apache.xmlbeans.XmlIDREF)get_store().find_attribute_user(REFID$0);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlIDREF)get_store().add_attribute_user(REFID$0);
                      }
                      target.set(refid);
                    }
                }
            }
            /**
             * An XML row(@).
             *
             * This is a complex type.
             */
            public static class RowImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Values.Row
            {
                private static final long serialVersionUID = 1L;
                
                public RowImpl(org.apache.xmlbeans.SchemaType sType)
                {
                    super(sType);
                }
                
                private static final javax.xml.namespace.QName VALUE$0 = 
                    new javax.xml.namespace.QName("", "value");
                private static final javax.xml.namespace.QName DESCRIPTION$2 = 
                    new javax.xml.namespace.QName("", "description");
                private static final javax.xml.namespace.QName DOCBOOK$4 = 
                    new javax.xml.namespace.QName("", "docbook");
                private static final javax.xml.namespace.QName VENDOR$6 = 
                    new javax.xml.namespace.QName("", "vendor");
                private static final javax.xml.namespace.QName INSERT$8 = 
                    new javax.xml.namespace.QName("", "insert");
                
                
                /**
                 * Gets array of all "value" elements
                 */
                public java.lang.String[] getValueArray()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      java.util.List targetList = new java.util.ArrayList();
                      get_store().find_all_element_users(VALUE$0, targetList);
                      java.lang.String[] result = new java.lang.String[targetList.size()];
                      for (int i = 0, len = targetList.size() ; i < len ; i++)
                          result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getStringValue();
                      return result;
                    }
                }
                
                /**
                 * Gets ith "value" element
                 */
                public java.lang.String getValueArray(int i)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUE$0, i);
                      if (target == null)
                      {
                        throw new IndexOutOfBoundsException();
                      }
                      return target.getStringValue();
                    }
                }
                
                /**
                 * Gets (as xml) array of all "value" elements
                 */
                public org.apache.xmlbeans.XmlString[] xgetValueArray()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      java.util.List targetList = new java.util.ArrayList();
                      get_store().find_all_element_users(VALUE$0, targetList);
                      org.apache.xmlbeans.XmlString[] result = new org.apache.xmlbeans.XmlString[targetList.size()];
                      targetList.toArray(result);
                      return result;
                    }
                }
                
                /**
                 * Gets (as xml) ith "value" element
                 */
                public org.apache.xmlbeans.XmlString xgetValueArray(int i)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(VALUE$0, i);
                      if (target == null)
                      {
                        throw new IndexOutOfBoundsException();
                      }
                      return (org.apache.xmlbeans.XmlString)target;
                    }
                }
                
                /**
                 * Returns number of "value" element
                 */
                public int sizeOfValueArray()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      return get_store().count_elements(VALUE$0);
                    }
                }
                
                /**
                 * Sets array of all "value" element
                 */
                public void setValueArray(java.lang.String[] valueArray)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      arraySetterHelper(valueArray, VALUE$0);
                    }
                }
                
                /**
                 * Sets ith "value" element
                 */
                public void setValueArray(int i, java.lang.String value)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUE$0, i);
                      if (target == null)
                      {
                        throw new IndexOutOfBoundsException();
                      }
                      target.setStringValue(value);
                    }
                }
                
                /**
                 * Sets (as xml) array of all "value" element
                 */
                public void xsetValueArray(org.apache.xmlbeans.XmlString[]valueArray)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      arraySetterHelper(valueArray, VALUE$0);
                    }
                }
                
                /**
                 * Sets (as xml) ith "value" element
                 */
                public void xsetValueArray(int i, org.apache.xmlbeans.XmlString value)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(VALUE$0, i);
                      if (target == null)
                      {
                        throw new IndexOutOfBoundsException();
                      }
                      target.set(value);
                    }
                }
                
                /**
                 * Inserts the value as the ith "value" element
                 */
                public void insertValue(int i, java.lang.String value)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = 
                        (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(VALUE$0, i);
                      target.setStringValue(value);
                    }
                }
                
                /**
                 * Appends the value as the last "value" element
                 */
                public void addValue(java.lang.String value)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(VALUE$0);
                      target.setStringValue(value);
                    }
                }
                
                /**
                 * Inserts and returns a new empty value (as xml) as the ith "value" element
                 */
                public org.apache.xmlbeans.XmlString insertNewValue(int i)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().insert_element_user(VALUE$0, i);
                      return target;
                    }
                }
                
                /**
                 * Appends and returns a new empty value (as xml) as the last "value" element
                 */
                public org.apache.xmlbeans.XmlString addNewValue()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlString target = null;
                      target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(VALUE$0);
                      return target;
                    }
                }
                
                /**
                 * Removes the ith "value" element
                 */
                public void removeValue(int i)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      get_store().remove_element(VALUE$0, i);
                    }
                }
                
                /**
                 * Gets the "description" element
                 */
                public ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Description getDescription()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Description target = null;
                      target = (ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Description)get_store().find_element_user(DESCRIPTION$2, 0);
                      if (target == null)
                      {
                        return null;
                      }
                      return target;
                    }
                }
                
                /**
                 * Sets the "description" element
                 */
                public void setDescription(ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Description description)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Description target = null;
                      target = (ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Description)get_store().find_element_user(DESCRIPTION$2, 0);
                      if (target == null)
                      {
                        target = (ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Description)get_store().add_element_user(DESCRIPTION$2);
                      }
                      target.set(description);
                    }
                }
                
                /**
                 * Appends and returns a new empty "description" element
                 */
                public ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Description addNewDescription()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Description target = null;
                      target = (ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Description)get_store().add_element_user(DESCRIPTION$2);
                      return target;
                    }
                }
                
                /**
                 * Gets the "docbook" element
                 */
                public ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Docbook getDocbook()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Docbook target = null;
                      target = (ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Docbook)get_store().find_element_user(DOCBOOK$4, 0);
                      if (target == null)
                      {
                        return null;
                      }
                      return target;
                    }
                }
                
                /**
                 * Sets the "docbook" element
                 */
                public void setDocbook(ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Docbook docbook)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Docbook target = null;
                      target = (ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Docbook)get_store().find_element_user(DOCBOOK$4, 0);
                      if (target == null)
                      {
                        target = (ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Docbook)get_store().add_element_user(DOCBOOK$4);
                      }
                      target.set(docbook);
                    }
                }
                
                /**
                 * Appends and returns a new empty "docbook" element
                 */
                public ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Docbook addNewDocbook()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Docbook target = null;
                      target = (ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Docbook)get_store().add_element_user(DOCBOOK$4);
                      return target;
                    }
                }
                
                /**
                 * Gets the "vendor" attribute
                 */
                public org.apache.xmlbeans.XmlAnySimpleType getVendor()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlAnySimpleType target = null;
                      target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(VENDOR$6);
                      if (target == null)
                      {
                        return null;
                      }
                      return target;
                    }
                }
                
                /**
                 * True if has "vendor" attribute
                 */
                public boolean isSetVendor()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      return get_store().find_attribute_user(VENDOR$6) != null;
                    }
                }
                
                /**
                 * Sets the "vendor" attribute
                 */
                public void setVendor(org.apache.xmlbeans.XmlAnySimpleType vendor)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlAnySimpleType target = null;
                      target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().find_attribute_user(VENDOR$6);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(VENDOR$6);
                      }
                      target.set(vendor);
                    }
                }
                
                /**
                 * Appends and returns a new empty "vendor" attribute
                 */
                public org.apache.xmlbeans.XmlAnySimpleType addNewVendor()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlAnySimpleType target = null;
                      target = (org.apache.xmlbeans.XmlAnySimpleType)get_store().add_attribute_user(VENDOR$6);
                      return target;
                    }
                }
                
                /**
                 * Unsets the "vendor" attribute
                 */
                public void unsetVendor()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      get_store().remove_attribute(VENDOR$6);
                    }
                }
                
                /**
                 * Gets the "insert" attribute
                 */
                public boolean getInsert()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(INSERT$8);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(INSERT$8);
                      }
                      if (target == null)
                      {
                        return false;
                      }
                      return target.getBooleanValue();
                    }
                }
                
                /**
                 * Gets (as xml) the "insert" attribute
                 */
                public org.apache.xmlbeans.XmlBoolean xgetInsert()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlBoolean target = null;
                      target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(INSERT$8);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(INSERT$8);
                      }
                      return target;
                    }
                }
                
                /**
                 * True if has "insert" attribute
                 */
                public boolean isSetInsert()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      return get_store().find_attribute_user(INSERT$8) != null;
                    }
                }
                
                /**
                 * Sets the "insert" attribute
                 */
                public void setInsert(boolean insert)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.SimpleValue target = null;
                      target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(INSERT$8);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(INSERT$8);
                      }
                      target.setBooleanValue(insert);
                    }
                }
                
                /**
                 * Sets (as xml) the "insert" attribute
                 */
                public void xsetInsert(org.apache.xmlbeans.XmlBoolean insert)
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      org.apache.xmlbeans.XmlBoolean target = null;
                      target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(INSERT$8);
                      if (target == null)
                      {
                        target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(INSERT$8);
                      }
                      target.set(insert);
                    }
                }
                
                /**
                 * Unsets the "insert" attribute
                 */
                public void unsetInsert()
                {
                    synchronized (monitor())
                    {
                      check_orphaned();
                      get_store().remove_attribute(INSERT$8);
                    }
                }
                /**
                 * An XML description(@).
                 *
                 * This is a complex type.
                 */
                public static class DescriptionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Description
                {
                    private static final long serialVersionUID = 1L;
                    
                    public DescriptionImpl(org.apache.xmlbeans.SchemaType sType)
                    {
                      super(sType);
                    }
                    
                    private static final javax.xml.namespace.QName DE$0 = 
                      new javax.xml.namespace.QName("", "de");
                    private static final javax.xml.namespace.QName EN$2 = 
                      new javax.xml.namespace.QName("", "en");
                    
                    
                    /**
                     * Gets the "de" element
                     */
                    public java.lang.String getDe()
                    {
                      synchronized (monitor())
                      {
                        check_orphaned();
                        org.apache.xmlbeans.SimpleValue target = null;
                        target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DE$0, 0);
                        if (target == null)
                        {
                          return null;
                        }
                        return target.getStringValue();
                      }
                    }
                    
                    /**
                     * Gets (as xml) the "de" element
                     */
                    public org.apache.xmlbeans.XmlString xgetDe()
                    {
                      synchronized (monitor())
                      {
                        check_orphaned();
                        org.apache.xmlbeans.XmlString target = null;
                        target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DE$0, 0);
                        return target;
                      }
                    }
                    
                    /**
                     * Sets the "de" element
                     */
                    public void setDe(java.lang.String de)
                    {
                      synchronized (monitor())
                      {
                        check_orphaned();
                        org.apache.xmlbeans.SimpleValue target = null;
                        target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DE$0, 0);
                        if (target == null)
                        {
                          target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DE$0);
                        }
                        target.setStringValue(de);
                      }
                    }
                    
                    /**
                     * Sets (as xml) the "de" element
                     */
                    public void xsetDe(org.apache.xmlbeans.XmlString de)
                    {
                      synchronized (monitor())
                      {
                        check_orphaned();
                        org.apache.xmlbeans.XmlString target = null;
                        target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DE$0, 0);
                        if (target == null)
                        {
                          target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DE$0);
                        }
                        target.set(de);
                      }
                    }
                    
                    /**
                     * Gets the "en" element
                     */
                    public java.lang.String getEn()
                    {
                      synchronized (monitor())
                      {
                        check_orphaned();
                        org.apache.xmlbeans.SimpleValue target = null;
                        target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EN$2, 0);
                        if (target == null)
                        {
                          return null;
                        }
                        return target.getStringValue();
                      }
                    }
                    
                    /**
                     * Gets (as xml) the "en" element
                     */
                    public org.apache.xmlbeans.XmlString xgetEn()
                    {
                      synchronized (monitor())
                      {
                        check_orphaned();
                        org.apache.xmlbeans.XmlString target = null;
                        target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EN$2, 0);
                        return target;
                      }
                    }
                    
                    /**
                     * Sets the "en" element
                     */
                    public void setEn(java.lang.String en)
                    {
                      synchronized (monitor())
                      {
                        check_orphaned();
                        org.apache.xmlbeans.SimpleValue target = null;
                        target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EN$2, 0);
                        if (target == null)
                        {
                          target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(EN$2);
                        }
                        target.setStringValue(en);
                      }
                    }
                    
                    /**
                     * Sets (as xml) the "en" element
                     */
                    public void xsetEn(org.apache.xmlbeans.XmlString en)
                    {
                      synchronized (monitor())
                      {
                        check_orphaned();
                        org.apache.xmlbeans.XmlString target = null;
                        target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EN$2, 0);
                        if (target == null)
                        {
                          target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(EN$2);
                        }
                        target.set(en);
                      }
                    }
                }
                /**
                 * An XML docbook(@).
                 *
                 * This is a complex type.
                 */
                public static class DocbookImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.TableDocument.Table.Values.Row.Docbook
                {
                    private static final long serialVersionUID = 1L;
                    
                    public DocbookImpl(org.apache.xmlbeans.SchemaType sType)
                    {
                      super(sType);
                    }
                    
                    private static final javax.xml.namespace.QName DE$0 = 
                      new javax.xml.namespace.QName("", "de");
                    private static final javax.xml.namespace.QName EN$2 = 
                      new javax.xml.namespace.QName("", "en");
                    
                    
                    /**
                     * Gets the "de" element
                     */
                    public ch.minova.core.xml.tables.DocbookCode getDe()
                    {
                      synchronized (monitor())
                      {
                        check_orphaned();
                        ch.minova.core.xml.tables.DocbookCode target = null;
                        target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(DE$0, 0);
                        if (target == null)
                        {
                          return null;
                        }
                        return target;
                      }
                    }
                    
                    /**
                     * Sets the "de" element
                     */
                    public void setDe(ch.minova.core.xml.tables.DocbookCode de)
                    {
                      synchronized (monitor())
                      {
                        check_orphaned();
                        ch.minova.core.xml.tables.DocbookCode target = null;
                        target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(DE$0, 0);
                        if (target == null)
                        {
                          target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(DE$0);
                        }
                        target.set(de);
                      }
                    }
                    
                    /**
                     * Appends and returns a new empty "de" element
                     */
                    public ch.minova.core.xml.tables.DocbookCode addNewDe()
                    {
                      synchronized (monitor())
                      {
                        check_orphaned();
                        ch.minova.core.xml.tables.DocbookCode target = null;
                        target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(DE$0);
                        return target;
                      }
                    }
                    
                    /**
                     * Gets the "en" element
                     */
                    public ch.minova.core.xml.tables.DocbookCode getEn()
                    {
                      synchronized (monitor())
                      {
                        check_orphaned();
                        ch.minova.core.xml.tables.DocbookCode target = null;
                        target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(EN$2, 0);
                        if (target == null)
                        {
                          return null;
                        }
                        return target;
                      }
                    }
                    
                    /**
                     * Sets the "en" element
                     */
                    public void setEn(ch.minova.core.xml.tables.DocbookCode en)
                    {
                      synchronized (monitor())
                      {
                        check_orphaned();
                        ch.minova.core.xml.tables.DocbookCode target = null;
                        target = (ch.minova.core.xml.tables.DocbookCode)get_store().find_element_user(EN$2, 0);
                        if (target == null)
                        {
                          target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(EN$2);
                        }
                        target.set(en);
                      }
                    }
                    
                    /**
                     * Appends and returns a new empty "en" element
                     */
                    public ch.minova.core.xml.tables.DocbookCode addNewEn()
                    {
                      synchronized (monitor())
                      {
                        check_orphaned();
                        ch.minova.core.xml.tables.DocbookCode target = null;
                        target = (ch.minova.core.xml.tables.DocbookCode)get_store().add_element_user(EN$2);
                        return target;
                      }
                    }
                }
            }
        }
    }
}

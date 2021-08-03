/*
 * An XML document type.
 * Localname: sql-code
 * Namespace: 
 * Java type: ch.minova.core.install.SqlCodeDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one sql-code(@) element.
 *
 * This is a complex type.
 */
public class SqlCodeDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.SqlCodeDocument
{
    private static final long serialVersionUID = 1L;
    
    public SqlCodeDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SQLCODE$0 = 
        new javax.xml.namespace.QName("", "sql-code");
    
    
    /**
     * Gets the "sql-code" element
     */
    public SqlCode getSqlCode()
    {
        synchronized (monitor())
        {
            check_orphaned();
            SqlCode target = null;
            target = (SqlCode)get_store().find_element_user(SQLCODE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "sql-code" element
     */
    public void setSqlCode(SqlCode sqlCode)
    {
        synchronized (monitor())
        {
            check_orphaned();
            SqlCode target = null;
            target = (SqlCode)get_store().find_element_user(SQLCODE$0, 0);
            if (target == null)
            {
                target = (SqlCode)get_store().add_element_user(SQLCODE$0);
            }
            target.set(sqlCode);
        }
    }
    
    /**
     * Appends and returns a new empty "sql-code" element
     */
    public SqlCode addNewSqlCode()
    {
        synchronized (monitor())
        {
            check_orphaned();
            SqlCode target = null;
            target = (SqlCode)get_store().add_element_user(SQLCODE$0);
            return target;
        }
    }
    /**
     * An XML sql-code(@).
     *
     * This is a complex type.
     */
    public static class SqlCodeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements SqlCode
    {
        private static final long serialVersionUID = 1L;
        
        public SqlCodeImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName SCRIPT$0 = 
            new javax.xml.namespace.QName("", "script");
        
        
        /**
         * Gets array of all "script" elements
         */
        public ch.minova.core.install.ScriptDocument.Script[] getScriptArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(SCRIPT$0, targetList);
                ch.minova.core.install.ScriptDocument.Script[] result = new ch.minova.core.install.ScriptDocument.Script[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "script" element
         */
        public ch.minova.core.install.ScriptDocument.Script getScriptArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.ScriptDocument.Script target = null;
                target = (ch.minova.core.install.ScriptDocument.Script)get_store().find_element_user(SCRIPT$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "script" element
         */
        public int sizeOfScriptArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(SCRIPT$0);
            }
        }
        
        /**
         * Sets array of all "script" element
         */
        public void setScriptArray(ch.minova.core.install.ScriptDocument.Script[] scriptArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(scriptArray, SCRIPT$0);
            }
        }
        
        /**
         * Sets ith "script" element
         */
        public void setScriptArray(int i, ch.minova.core.install.ScriptDocument.Script script)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.ScriptDocument.Script target = null;
                target = (ch.minova.core.install.ScriptDocument.Script)get_store().find_element_user(SCRIPT$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(script);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "script" element
         */
        public ch.minova.core.install.ScriptDocument.Script insertNewScript(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.ScriptDocument.Script target = null;
                target = (ch.minova.core.install.ScriptDocument.Script)get_store().insert_element_user(SCRIPT$0, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "script" element
         */
        public ch.minova.core.install.ScriptDocument.Script addNewScript()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.ScriptDocument.Script target = null;
                target = (ch.minova.core.install.ScriptDocument.Script)get_store().add_element_user(SCRIPT$0);
                return target;
            }
        }
        
        /**
         * Removes the ith "script" element
         */
        public void removeScript(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(SCRIPT$0, i);
            }
        }
    }
}

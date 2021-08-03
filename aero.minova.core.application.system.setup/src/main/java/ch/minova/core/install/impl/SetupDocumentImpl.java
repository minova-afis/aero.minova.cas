/*
 * An XML document type.
 * Localname: setup
 * Namespace: 
 * Java type: ch.minova.core.install.SetupDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one setup(@) element.
 *
 * This is a complex type.
 */
public class SetupDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.SetupDocument
{
    private static final long serialVersionUID = 1L;
    
    public SetupDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SETUP$0 = 
        new javax.xml.namespace.QName("", "setup");
    
    
    /**
     * Gets the "setup" element
     */
    public Setup getSetup()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Setup target = null;
            target = (Setup)get_store().find_element_user(SETUP$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "setup" element
     */
    public void setSetup(Setup setup)
    {
        synchronized (monitor())
        {
            check_orphaned();
            Setup target = null;
            target = (Setup)get_store().find_element_user(SETUP$0, 0);
            if (target == null)
            {
                target = (Setup)get_store().add_element_user(SETUP$0);
            }
            target.set(setup);
        }
    }
    
    /**
     * Appends and returns a new empty "setup" element
     */
    public Setup addNewSetup()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Setup target = null;
            target = (Setup)get_store().add_element_user(SETUP$0);
            return target;
        }
    }
    /**
     * An XML setup(@).
     *
     * This is a complex type.
     */
    public static class SetupImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements Setup
    {
        private static final long serialVersionUID = 1L;
        
        public SetupImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName REQUIREDMODULES$0 = 
            new javax.xml.namespace.QName("", "required-modules");
        private static final javax.xml.namespace.QName REQUIREDSERVICE$2 = 
            new javax.xml.namespace.QName("", "required-service");
        private static final javax.xml.namespace.QName SQLCODE$4 = 
            new javax.xml.namespace.QName("", "sql-code");
        private static final javax.xml.namespace.QName MDICODE$6 = 
            new javax.xml.namespace.QName("", "mdi-code");
        private static final javax.xml.namespace.QName XBSCODE$8 = 
            new javax.xml.namespace.QName("", "xbs-code");
        private static final javax.xml.namespace.QName STATICCODE$10 = 
            new javax.xml.namespace.QName("", "static-code");
        private static final javax.xml.namespace.QName COPYFILE$12 = 
            new javax.xml.namespace.QName("", "copy-file");
        private static final javax.xml.namespace.QName SCHEMA$14 = 
            new javax.xml.namespace.QName("", "schema");
        private static final javax.xml.namespace.QName EXECUTEJAVA$16 = 
            new javax.xml.namespace.QName("", "execute-java");
        private static final javax.xml.namespace.QName NAME$18 = 
            new javax.xml.namespace.QName("", "name");
        
        
        /**
         * Gets the "required-modules" element
         */
        public ch.minova.core.install.RequiredModulesDocument.RequiredModules getRequiredModules()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.RequiredModulesDocument.RequiredModules target = null;
                target = (ch.minova.core.install.RequiredModulesDocument.RequiredModules)get_store().find_element_user(REQUIREDMODULES$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "required-modules" element
         */
        public boolean isSetRequiredModules()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(REQUIREDMODULES$0) != 0;
            }
        }
        
        /**
         * Sets the "required-modules" element
         */
        public void setRequiredModules(ch.minova.core.install.RequiredModulesDocument.RequiredModules requiredModules)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.RequiredModulesDocument.RequiredModules target = null;
                target = (ch.minova.core.install.RequiredModulesDocument.RequiredModules)get_store().find_element_user(REQUIREDMODULES$0, 0);
                if (target == null)
                {
                    target = (ch.minova.core.install.RequiredModulesDocument.RequiredModules)get_store().add_element_user(REQUIREDMODULES$0);
                }
                target.set(requiredModules);
            }
        }
        
        /**
         * Appends and returns a new empty "required-modules" element
         */
        public ch.minova.core.install.RequiredModulesDocument.RequiredModules addNewRequiredModules()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.RequiredModulesDocument.RequiredModules target = null;
                target = (ch.minova.core.install.RequiredModulesDocument.RequiredModules)get_store().add_element_user(REQUIREDMODULES$0);
                return target;
            }
        }
        
        /**
         * Unsets the "required-modules" element
         */
        public void unsetRequiredModules()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(REQUIREDMODULES$0, 0);
            }
        }
        
        /**
         * Gets the "required-service" element
         */
        public ch.minova.core.install.RequiredServiceDocument.RequiredService getRequiredService()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.RequiredServiceDocument.RequiredService target = null;
                target = (ch.minova.core.install.RequiredServiceDocument.RequiredService)get_store().find_element_user(REQUIREDSERVICE$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "required-service" element
         */
        public boolean isSetRequiredService()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(REQUIREDSERVICE$2) != 0;
            }
        }
        
        /**
         * Sets the "required-service" element
         */
        public void setRequiredService(ch.minova.core.install.RequiredServiceDocument.RequiredService requiredService)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.RequiredServiceDocument.RequiredService target = null;
                target = (ch.minova.core.install.RequiredServiceDocument.RequiredService)get_store().find_element_user(REQUIREDSERVICE$2, 0);
                if (target == null)
                {
                    target = (ch.minova.core.install.RequiredServiceDocument.RequiredService)get_store().add_element_user(REQUIREDSERVICE$2);
                }
                target.set(requiredService);
            }
        }
        
        /**
         * Appends and returns a new empty "required-service" element
         */
        public ch.minova.core.install.RequiredServiceDocument.RequiredService addNewRequiredService()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.RequiredServiceDocument.RequiredService target = null;
                target = (ch.minova.core.install.RequiredServiceDocument.RequiredService)get_store().add_element_user(REQUIREDSERVICE$2);
                return target;
            }
        }
        
        /**
         * Unsets the "required-service" element
         */
        public void unsetRequiredService()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(REQUIREDSERVICE$2, 0);
            }
        }
        
        /**
         * Gets the "sql-code" element
         */
        public ch.minova.core.install.SqlCodeDocument.SqlCode getSqlCode()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.SqlCodeDocument.SqlCode target = null;
                target = (ch.minova.core.install.SqlCodeDocument.SqlCode)get_store().find_element_user(SQLCODE$4, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "sql-code" element
         */
        public boolean isSetSqlCode()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(SQLCODE$4) != 0;
            }
        }
        
        /**
         * Sets the "sql-code" element
         */
        public void setSqlCode(ch.minova.core.install.SqlCodeDocument.SqlCode sqlCode)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.SqlCodeDocument.SqlCode target = null;
                target = (ch.minova.core.install.SqlCodeDocument.SqlCode)get_store().find_element_user(SQLCODE$4, 0);
                if (target == null)
                {
                    target = (ch.minova.core.install.SqlCodeDocument.SqlCode)get_store().add_element_user(SQLCODE$4);
                }
                target.set(sqlCode);
            }
        }
        
        /**
         * Appends and returns a new empty "sql-code" element
         */
        public ch.minova.core.install.SqlCodeDocument.SqlCode addNewSqlCode()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.SqlCodeDocument.SqlCode target = null;
                target = (ch.minova.core.install.SqlCodeDocument.SqlCode)get_store().add_element_user(SQLCODE$4);
                return target;
            }
        }
        
        /**
         * Unsets the "sql-code" element
         */
        public void unsetSqlCode()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(SQLCODE$4, 0);
            }
        }
        
        /**
         * Gets the "mdi-code" element
         */
        public ch.minova.core.install.MdiCodeDocument.MdiCode getMdiCode()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.MdiCodeDocument.MdiCode target = null;
                target = (ch.minova.core.install.MdiCodeDocument.MdiCode)get_store().find_element_user(MDICODE$6, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "mdi-code" element
         */
        public boolean isSetMdiCode()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(MDICODE$6) != 0;
            }
        }
        
        /**
         * Sets the "mdi-code" element
         */
        public void setMdiCode(ch.minova.core.install.MdiCodeDocument.MdiCode mdiCode)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.MdiCodeDocument.MdiCode target = null;
                target = (ch.minova.core.install.MdiCodeDocument.MdiCode)get_store().find_element_user(MDICODE$6, 0);
                if (target == null)
                {
                    target = (ch.minova.core.install.MdiCodeDocument.MdiCode)get_store().add_element_user(MDICODE$6);
                }
                target.set(mdiCode);
            }
        }
        
        /**
         * Appends and returns a new empty "mdi-code" element
         */
        public ch.minova.core.install.MdiCodeDocument.MdiCode addNewMdiCode()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.MdiCodeDocument.MdiCode target = null;
                target = (ch.minova.core.install.MdiCodeDocument.MdiCode)get_store().add_element_user(MDICODE$6);
                return target;
            }
        }
        
        /**
         * Unsets the "mdi-code" element
         */
        public void unsetMdiCode()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(MDICODE$6, 0);
            }
        }
        
        /**
         * Gets the "xbs-code" element
         */
        public ch.minova.core.install.XbsCodeDocument.XbsCode getXbsCode()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.XbsCodeDocument.XbsCode target = null;
                target = (ch.minova.core.install.XbsCodeDocument.XbsCode)get_store().find_element_user(XBSCODE$8, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "xbs-code" element
         */
        public boolean isSetXbsCode()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(XBSCODE$8) != 0;
            }
        }
        
        /**
         * Sets the "xbs-code" element
         */
        public void setXbsCode(ch.minova.core.install.XbsCodeDocument.XbsCode xbsCode)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.XbsCodeDocument.XbsCode target = null;
                target = (ch.minova.core.install.XbsCodeDocument.XbsCode)get_store().find_element_user(XBSCODE$8, 0);
                if (target == null)
                {
                    target = (ch.minova.core.install.XbsCodeDocument.XbsCode)get_store().add_element_user(XBSCODE$8);
                }
                target.set(xbsCode);
            }
        }
        
        /**
         * Appends and returns a new empty "xbs-code" element
         */
        public ch.minova.core.install.XbsCodeDocument.XbsCode addNewXbsCode()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.XbsCodeDocument.XbsCode target = null;
                target = (ch.minova.core.install.XbsCodeDocument.XbsCode)get_store().add_element_user(XBSCODE$8);
                return target;
            }
        }
        
        /**
         * Unsets the "xbs-code" element
         */
        public void unsetXbsCode()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(XBSCODE$8, 0);
            }
        }
        
        /**
         * Gets the "static-code" element
         */
        public ch.minova.core.install.StaticCodeDocument.StaticCode getStaticCode()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.StaticCodeDocument.StaticCode target = null;
                target = (ch.minova.core.install.StaticCodeDocument.StaticCode)get_store().find_element_user(STATICCODE$10, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "static-code" element
         */
        public boolean isSetStaticCode()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(STATICCODE$10) != 0;
            }
        }
        
        /**
         * Sets the "static-code" element
         */
        public void setStaticCode(ch.minova.core.install.StaticCodeDocument.StaticCode staticCode)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.StaticCodeDocument.StaticCode target = null;
                target = (ch.minova.core.install.StaticCodeDocument.StaticCode)get_store().find_element_user(STATICCODE$10, 0);
                if (target == null)
                {
                    target = (ch.minova.core.install.StaticCodeDocument.StaticCode)get_store().add_element_user(STATICCODE$10);
                }
                target.set(staticCode);
            }
        }
        
        /**
         * Appends and returns a new empty "static-code" element
         */
        public ch.minova.core.install.StaticCodeDocument.StaticCode addNewStaticCode()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.StaticCodeDocument.StaticCode target = null;
                target = (ch.minova.core.install.StaticCodeDocument.StaticCode)get_store().add_element_user(STATICCODE$10);
                return target;
            }
        }
        
        /**
         * Unsets the "static-code" element
         */
        public void unsetStaticCode()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(STATICCODE$10, 0);
            }
        }
        
        /**
         * Gets the "copy-file" element
         */
        public ch.minova.core.install.CopyFileDocument.CopyFile getCopyFile()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.CopyFileDocument.CopyFile target = null;
                target = (ch.minova.core.install.CopyFileDocument.CopyFile)get_store().find_element_user(COPYFILE$12, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "copy-file" element
         */
        public boolean isSetCopyFile()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(COPYFILE$12) != 0;
            }
        }
        
        /**
         * Sets the "copy-file" element
         */
        public void setCopyFile(ch.minova.core.install.CopyFileDocument.CopyFile copyFile)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.CopyFileDocument.CopyFile target = null;
                target = (ch.minova.core.install.CopyFileDocument.CopyFile)get_store().find_element_user(COPYFILE$12, 0);
                if (target == null)
                {
                    target = (ch.minova.core.install.CopyFileDocument.CopyFile)get_store().add_element_user(COPYFILE$12);
                }
                target.set(copyFile);
            }
        }
        
        /**
         * Appends and returns a new empty "copy-file" element
         */
        public ch.minova.core.install.CopyFileDocument.CopyFile addNewCopyFile()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.CopyFileDocument.CopyFile target = null;
                target = (ch.minova.core.install.CopyFileDocument.CopyFile)get_store().add_element_user(COPYFILE$12);
                return target;
            }
        }
        
        /**
         * Unsets the "copy-file" element
         */
        public void unsetCopyFile()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(COPYFILE$12, 0);
            }
        }
        
        /**
         * Gets the "schema" element
         */
        public ch.minova.core.install.SchemaDocument.Schema getSchema()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.SchemaDocument.Schema target = null;
                target = (ch.minova.core.install.SchemaDocument.Schema)get_store().find_element_user(SCHEMA$14, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "schema" element
         */
        public boolean isSetSchema()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(SCHEMA$14) != 0;
            }
        }
        
        /**
         * Sets the "schema" element
         */
        public void setSchema(ch.minova.core.install.SchemaDocument.Schema schema)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.SchemaDocument.Schema target = null;
                target = (ch.minova.core.install.SchemaDocument.Schema)get_store().find_element_user(SCHEMA$14, 0);
                if (target == null)
                {
                    target = (ch.minova.core.install.SchemaDocument.Schema)get_store().add_element_user(SCHEMA$14);
                }
                target.set(schema);
            }
        }
        
        /**
         * Appends and returns a new empty "schema" element
         */
        public ch.minova.core.install.SchemaDocument.Schema addNewSchema()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.SchemaDocument.Schema target = null;
                target = (ch.minova.core.install.SchemaDocument.Schema)get_store().add_element_user(SCHEMA$14);
                return target;
            }
        }
        
        /**
         * Unsets the "schema" element
         */
        public void unsetSchema()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(SCHEMA$14, 0);
            }
        }
        
        /**
         * Gets array of all "execute-java" elements
         */
        public ch.minova.core.install.ExecuteJavaDocument.ExecuteJava[] getExecuteJavaArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(EXECUTEJAVA$16, targetList);
                ch.minova.core.install.ExecuteJavaDocument.ExecuteJava[] result = new ch.minova.core.install.ExecuteJavaDocument.ExecuteJava[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "execute-java" element
         */
        public ch.minova.core.install.ExecuteJavaDocument.ExecuteJava getExecuteJavaArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.ExecuteJavaDocument.ExecuteJava target = null;
                target = (ch.minova.core.install.ExecuteJavaDocument.ExecuteJava)get_store().find_element_user(EXECUTEJAVA$16, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "execute-java" element
         */
        public int sizeOfExecuteJavaArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(EXECUTEJAVA$16);
            }
        }
        
        /**
         * Sets array of all "execute-java" element
         */
        public void setExecuteJavaArray(ch.minova.core.install.ExecuteJavaDocument.ExecuteJava[] executeJavaArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(executeJavaArray, EXECUTEJAVA$16);
            }
        }
        
        /**
         * Sets ith "execute-java" element
         */
        public void setExecuteJavaArray(int i, ch.minova.core.install.ExecuteJavaDocument.ExecuteJava executeJava)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.ExecuteJavaDocument.ExecuteJava target = null;
                target = (ch.minova.core.install.ExecuteJavaDocument.ExecuteJava)get_store().find_element_user(EXECUTEJAVA$16, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(executeJava);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "execute-java" element
         */
        public ch.minova.core.install.ExecuteJavaDocument.ExecuteJava insertNewExecuteJava(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.ExecuteJavaDocument.ExecuteJava target = null;
                target = (ch.minova.core.install.ExecuteJavaDocument.ExecuteJava)get_store().insert_element_user(EXECUTEJAVA$16, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "execute-java" element
         */
        public ch.minova.core.install.ExecuteJavaDocument.ExecuteJava addNewExecuteJava()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.ExecuteJavaDocument.ExecuteJava target = null;
                target = (ch.minova.core.install.ExecuteJavaDocument.ExecuteJava)get_store().add_element_user(EXECUTEJAVA$16);
                return target;
            }
        }
        
        /**
         * Removes the ith "execute-java" element
         */
        public void removeExecuteJava(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(EXECUTEJAVA$16, i);
            }
        }
        
        /**
         * Gets the "name" attribute
         */
        public String getName()
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
        public org.apache.xmlbeans.XmlNCName xgetName()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(NAME$18);
                return target;
            }
        }
        
        /**
         * Sets the "name" attribute
         */
        public void setName(String name)
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
        public void xsetName(org.apache.xmlbeans.XmlNCName name)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(NAME$18);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlNCName)get_store().add_attribute_user(NAME$18);
                }
                target.set(name);
            }
        }
    }
}

/*
 * An XML document type.
 * Localname: tableschema
 * Namespace: 
 * Java type: ch.minova.core.install.TableschemaDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one tableschema(@) element.
 *
 * This is a complex type.
 */
public class TableschemaDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.TableschemaDocument
{
    private static final long serialVersionUID = 1L;
    
    public TableschemaDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TABLESCHEMA$0 = 
        new javax.xml.namespace.QName("", "tableschema");
    
    
    /**
     * Gets the "tableschema" element
     */
    public Tableschema getTableschema()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Tableschema target = null;
            target = (Tableschema)get_store().find_element_user(TABLESCHEMA$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "tableschema" element
     */
    public void setTableschema(Tableschema tableschema)
    {
        synchronized (monitor())
        {
            check_orphaned();
            Tableschema target = null;
            target = (Tableschema)get_store().find_element_user(TABLESCHEMA$0, 0);
            if (target == null)
            {
                target = (Tableschema)get_store().add_element_user(TABLESCHEMA$0);
            }
            target.set(tableschema);
        }
    }
    
    /**
     * Appends and returns a new empty "tableschema" element
     */
    public Tableschema addNewTableschema()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Tableschema target = null;
            target = (Tableschema)get_store().add_element_user(TABLESCHEMA$0);
            return target;
        }
    }
    /**
     * An XML tableschema(@).
     *
     * This is a complex type.
     */
    public static class TableschemaImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements Tableschema
    {
        private static final long serialVersionUID = 1L;
        
        public TableschemaImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName TABLE$0 = 
            new javax.xml.namespace.QName("", "table");
        private static final javax.xml.namespace.QName NAME$2 = 
            new javax.xml.namespace.QName("", "name");
        private static final javax.xml.namespace.QName TYPE$4 = 
            new javax.xml.namespace.QName("", "type");
        private static final javax.xml.namespace.QName EXECUTE$6 = 
            new javax.xml.namespace.QName("", "execute");
        
        
        /**
         * Gets array of all "table" elements
         */
        public org.apache.xmlbeans.XmlObject[] getTableArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(TABLE$0, targetList);
                org.apache.xmlbeans.XmlObject[] result = new org.apache.xmlbeans.XmlObject[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "table" element
         */
        public org.apache.xmlbeans.XmlObject getTableArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlObject target = null;
                target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(TABLE$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "table" element
         */
        public int sizeOfTableArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(TABLE$0);
            }
        }
        
        /**
         * Sets array of all "table" element
         */
        public void setTableArray(org.apache.xmlbeans.XmlObject[] tableArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(tableArray, TABLE$0);
            }
        }
        
        /**
         * Sets ith "table" element
         */
        public void setTableArray(int i, org.apache.xmlbeans.XmlObject table)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlObject target = null;
                target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(TABLE$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(table);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "table" element
         */
        public org.apache.xmlbeans.XmlObject insertNewTable(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlObject target = null;
                target = (org.apache.xmlbeans.XmlObject)get_store().insert_element_user(TABLE$0, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "table" element
         */
        public org.apache.xmlbeans.XmlObject addNewTable()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlObject target = null;
                target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(TABLE$0);
                return target;
            }
        }
        
        /**
         * Removes the ith "table" element
         */
        public void removeTable(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(TABLE$0, i);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$2);
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
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(NAME$2);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NAME$2);
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
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(NAME$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlNCName)get_store().add_attribute_user(NAME$2);
                }
                target.set(name);
            }
        }
        
        /**
         * Gets the "type" attribute
         */
        public Type.Enum getType()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TYPE$4);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(TYPE$4);
                }
                if (target == null)
                {
                    return null;
                }
                return (Type.Enum)target.getEnumValue();
            }
        }
        
        /**
         * Gets (as xml) the "type" attribute
         */
        public Type xgetType()
        {
            synchronized (monitor())
            {
                check_orphaned();
                Type target = null;
                target = (Type)get_store().find_attribute_user(TYPE$4);
                if (target == null)
                {
                    target = (Type)get_default_attribute_value(TYPE$4);
                }
                return target;
            }
        }
        
        /**
         * True if has "type" attribute
         */
        public boolean isSetType()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(TYPE$4) != null;
            }
        }
        
        /**
         * Sets the "type" attribute
         */
        public void setType(Type.Enum type)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TYPE$4);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(TYPE$4);
                }
                target.setEnumValue(type);
            }
        }
        
        /**
         * Sets (as xml) the "type" attribute
         */
        public void xsetType(Type type)
        {
            synchronized (monitor())
            {
                check_orphaned();
                Type target = null;
                target = (Type)get_store().find_attribute_user(TYPE$4);
                if (target == null)
                {
                    target = (Type)get_store().add_attribute_user(TYPE$4);
                }
                target.set(type);
            }
        }
        
        /**
         * Unsets the "type" attribute
         */
        public void unsetType()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(TYPE$4);
            }
        }
        
        /**
         * Gets the "execute" attribute
         */
        public Execute.Enum getExecute()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(EXECUTE$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(EXECUTE$6);
                }
                if (target == null)
                {
                    return null;
                }
                return (Execute.Enum)target.getEnumValue();
            }
        }
        
        /**
         * Gets (as xml) the "execute" attribute
         */
        public Execute xgetExecute()
        {
            synchronized (monitor())
            {
                check_orphaned();
                Execute target = null;
                target = (Execute)get_store().find_attribute_user(EXECUTE$6);
                if (target == null)
                {
                    target = (Execute)get_default_attribute_value(EXECUTE$6);
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
                return get_store().find_attribute_user(EXECUTE$6) != null;
            }
        }
        
        /**
         * Sets the "execute" attribute
         */
        public void setExecute(Execute.Enum execute)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(EXECUTE$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(EXECUTE$6);
                }
                target.setEnumValue(execute);
            }
        }
        
        /**
         * Sets (as xml) the "execute" attribute
         */
        public void xsetExecute(Execute execute)
        {
            synchronized (monitor())
            {
                check_orphaned();
                Execute target = null;
                target = (Execute)get_store().find_attribute_user(EXECUTE$6);
                if (target == null)
                {
                    target = (Execute)get_store().add_attribute_user(EXECUTE$6);
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
                get_store().remove_attribute(EXECUTE$6);
            }
        }
        /**
         * An XML type(@).
         *
         * This is an atomic type that is a restriction of ch.minova.core.install.TableschemaDocument$Tableschema$Type.
         */
        public static class TypeImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements Type
        {
            private static final long serialVersionUID = 1L;
            
            public TypeImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType, false);
            }
            
            protected TypeImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
            {
                super(sType, b);
            }
        }
        /**
         * An XML execute(@).
         *
         * This is an atomic type that is a restriction of ch.minova.core.install.TableschemaDocument$Tableschema$Execute.
         */
        public static class ExecuteImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements Execute
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
}

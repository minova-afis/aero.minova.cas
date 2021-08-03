/*
 * An XML document type.
 * Localname: script
 * Namespace: 
 * Java type: ch.minova.core.install.ScriptDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one script(@) element.
 *
 * This is a complex type.
 */
public class ScriptDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.ScriptDocument
{
    private static final long serialVersionUID = 1L;
    
    public ScriptDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SCRIPT$0 = 
        new javax.xml.namespace.QName("", "script");
    
    
    /**
     * Gets the "script" element
     */
    public Script getScript()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Script target = null;
            target = (Script)get_store().find_element_user(SCRIPT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "script" element
     */
    public void setScript(Script script)
    {
        synchronized (monitor())
        {
            check_orphaned();
            Script target = null;
            target = (Script)get_store().find_element_user(SCRIPT$0, 0);
            if (target == null)
            {
                target = (Script)get_store().add_element_user(SCRIPT$0);
            }
            target.set(script);
        }
    }
    
    /**
     * Appends and returns a new empty "script" element
     */
    public Script addNewScript()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Script target = null;
            target = (Script)get_store().add_element_user(SCRIPT$0);
            return target;
        }
    }
    /**
     * An XML script(@).
     *
     * This is a complex type.
     */
    public static class ScriptImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements Script
    {
        private static final long serialVersionUID = 1L;
        
        public ScriptImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName VERSION$0 = 
            new javax.xml.namespace.QName("", "version");
        private static final javax.xml.namespace.QName NAME$2 = 
            new javax.xml.namespace.QName("", "name");
        private static final javax.xml.namespace.QName TYPE$4 = 
            new javax.xml.namespace.QName("", "type");
        
        
        /**
         * Gets array of all "version" elements
         */
        public ch.minova.core.install.VersionDocument.Version[] getVersionArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(VERSION$0, targetList);
                ch.minova.core.install.VersionDocument.Version[] result = new ch.minova.core.install.VersionDocument.Version[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "version" element
         */
        public ch.minova.core.install.VersionDocument.Version getVersionArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.VersionDocument.Version target = null;
                target = (ch.minova.core.install.VersionDocument.Version)get_store().find_element_user(VERSION$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "version" element
         */
        public int sizeOfVersionArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(VERSION$0);
            }
        }
        
        /**
         * Sets array of all "version" element
         */
        public void setVersionArray(ch.minova.core.install.VersionDocument.Version[] versionArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(versionArray, VERSION$0);
            }
        }
        
        /**
         * Sets ith "version" element
         */
        public void setVersionArray(int i, ch.minova.core.install.VersionDocument.Version version)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.VersionDocument.Version target = null;
                target = (ch.minova.core.install.VersionDocument.Version)get_store().find_element_user(VERSION$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(version);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "version" element
         */
        public ch.minova.core.install.VersionDocument.Version insertNewVersion(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.VersionDocument.Version target = null;
                target = (ch.minova.core.install.VersionDocument.Version)get_store().insert_element_user(VERSION$0, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "version" element
         */
        public ch.minova.core.install.VersionDocument.Version addNewVersion()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.VersionDocument.Version target = null;
                target = (ch.minova.core.install.VersionDocument.Version)get_store().add_element_user(VERSION$0);
                return target;
            }
        }
        
        /**
         * Removes the ith "version" element
         */
        public void removeVersion(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(VERSION$0, i);
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
                return target;
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
         * An XML type(@).
         *
         * This is an atomic type that is a restriction of ch.minova.core.install.ScriptDocument$Script$Type.
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
    }
}

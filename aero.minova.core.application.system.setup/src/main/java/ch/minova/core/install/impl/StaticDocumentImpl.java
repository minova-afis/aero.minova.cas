/*
 * An XML document type.
 * Localname: static
 * Namespace: 
 * Java type: ch.minova.core.install.StaticDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one static(@) element.
 *
 * This is a complex type.
 */
public class StaticDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.StaticDocument
{
    private static final long serialVersionUID = 1L;
    
    public StaticDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName STATIC$0 = 
        new javax.xml.namespace.QName("", "static");
    
    
    /**
     * Gets the "static" element
     */
    public Static getStatic()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Static target = null;
            target = (Static)get_store().find_element_user(STATIC$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "static" element
     */
    public void setStatic(Static xstatic)
    {
        synchronized (monitor())
        {
            check_orphaned();
            Static target = null;
            target = (Static)get_store().find_element_user(STATIC$0, 0);
            if (target == null)
            {
                target = (Static)get_store().add_element_user(STATIC$0);
            }
            target.set(xstatic);
        }
    }
    
    /**
     * Appends and returns a new empty "static" element
     */
    public Static addNewStatic()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Static target = null;
            target = (Static)get_store().add_element_user(STATIC$0);
            return target;
        }
    }
    /**
     * An XML static(@).
     *
     * This is a complex type.
     */
    public static class StaticImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements Static
    {
        private static final long serialVersionUID = 1L;
        
        public StaticImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName VERSION$0 = 
            new javax.xml.namespace.QName("", "version");
        private static final javax.xml.namespace.QName NAME$2 = 
            new javax.xml.namespace.QName("", "name");
        private static final javax.xml.namespace.QName MODE$4 = 
            new javax.xml.namespace.QName("", "mode");
        private static final javax.xml.namespace.QName COUNTRY$6 = 
            new javax.xml.namespace.QName("", "country");
        private static final javax.xml.namespace.QName LANG$8 = 
            new javax.xml.namespace.QName("", "lang");
        
        
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
         * Gets the "mode" attribute
         */
        public java.math.BigInteger getMode()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(MODE$4);
                if (target == null)
                {
                    return null;
                }
                return target.getBigIntegerValue();
            }
        }
        
        /**
         * Gets (as xml) the "mode" attribute
         */
        public org.apache.xmlbeans.XmlInteger xgetMode()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInteger target = null;
                target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(MODE$4);
                return target;
            }
        }
        
        /**
         * Sets the "mode" attribute
         */
        public void setMode(java.math.BigInteger mode)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(MODE$4);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(MODE$4);
                }
                target.setBigIntegerValue(mode);
            }
        }
        
        /**
         * Sets (as xml) the "mode" attribute
         */
        public void xsetMode(org.apache.xmlbeans.XmlInteger mode)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInteger target = null;
                target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(MODE$4);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInteger)get_store().add_attribute_user(MODE$4);
                }
                target.set(mode);
            }
        }
        
        /**
         * Gets the "country" attribute
         */
        public Country.Enum getCountry()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(COUNTRY$6);
                if (target == null)
                {
                    return null;
                }
                return (Country.Enum)target.getEnumValue();
            }
        }
        
        /**
         * Gets (as xml) the "country" attribute
         */
        public Country xgetCountry()
        {
            synchronized (monitor())
            {
                check_orphaned();
                Country target = null;
                target = (Country)get_store().find_attribute_user(COUNTRY$6);
                return target;
            }
        }
        
        /**
         * True if has "country" attribute
         */
        public boolean isSetCountry()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(COUNTRY$6) != null;
            }
        }
        
        /**
         * Sets the "country" attribute
         */
        public void setCountry(Country.Enum country)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(COUNTRY$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(COUNTRY$6);
                }
                target.setEnumValue(country);
            }
        }
        
        /**
         * Sets (as xml) the "country" attribute
         */
        public void xsetCountry(Country country)
        {
            synchronized (monitor())
            {
                check_orphaned();
                Country target = null;
                target = (Country)get_store().find_attribute_user(COUNTRY$6);
                if (target == null)
                {
                    target = (Country)get_store().add_attribute_user(COUNTRY$6);
                }
                target.set(country);
            }
        }
        
        /**
         * Unsets the "country" attribute
         */
        public void unsetCountry()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(COUNTRY$6);
            }
        }
        
        /**
         * Gets the "lang" attribute
         */
        public Lang.Enum getLang()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(LANG$8);
                if (target == null)
                {
                    return null;
                }
                return (Lang.Enum)target.getEnumValue();
            }
        }
        
        /**
         * Gets (as xml) the "lang" attribute
         */
        public Lang xgetLang()
        {
            synchronized (monitor())
            {
                check_orphaned();
                Lang target = null;
                target = (Lang)get_store().find_attribute_user(LANG$8);
                return target;
            }
        }
        
        /**
         * Sets the "lang" attribute
         */
        public void setLang(Lang.Enum lang)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(LANG$8);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(LANG$8);
                }
                target.setEnumValue(lang);
            }
        }
        
        /**
         * Sets (as xml) the "lang" attribute
         */
        public void xsetLang(Lang lang)
        {
            synchronized (monitor())
            {
                check_orphaned();
                Lang target = null;
                target = (Lang)get_store().find_attribute_user(LANG$8);
                if (target == null)
                {
                    target = (Lang)get_store().add_attribute_user(LANG$8);
                }
                target.set(lang);
            }
        }
        /**
         * An XML country(@).
         *
         * This is an atomic type that is a restriction of ch.minova.core.install.StaticDocument$Static$Country.
         */
        public static class CountryImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements Country
        {
            private static final long serialVersionUID = 1L;
            
            public CountryImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType, false);
            }
            
            protected CountryImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
            {
                super(sType, b);
            }
        }
        /**
         * An XML lang(@).
         *
         * This is an atomic type that is a restriction of ch.minova.core.install.StaticDocument$Static$Lang.
         */
        public static class LangImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements Lang
        {
            private static final long serialVersionUID = 1L;
            
            public LangImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType, false);
            }
            
            protected LangImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
            {
                super(sType, b);
            }
        }
    }
}

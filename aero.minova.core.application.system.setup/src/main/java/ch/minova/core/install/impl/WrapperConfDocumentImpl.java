/*
 * An XML document type.
 * Localname: wrapper-conf
 * Namespace: 
 * Java type: ch.minova.core.install.WrapperConfDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one wrapper-conf(@) element.
 *
 * This is a complex type.
 */
public class WrapperConfDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.WrapperConfDocument
{
    private static final long serialVersionUID = 1L;
    
    public WrapperConfDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName WRAPPERCONF$0 = 
        new javax.xml.namespace.QName("", "wrapper-conf");
    
    
    /**
     * Gets the "wrapper-conf" element
     */
    public WrapperConf getWrapperConf()
    {
        synchronized (monitor())
        {
            check_orphaned();
            WrapperConf target = null;
            target = (WrapperConf)get_store().find_element_user(WRAPPERCONF$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "wrapper-conf" element
     */
    public void setWrapperConf(WrapperConf wrapperConf)
    {
        synchronized (monitor())
        {
            check_orphaned();
            WrapperConf target = null;
            target = (WrapperConf)get_store().find_element_user(WRAPPERCONF$0, 0);
            if (target == null)
            {
                target = (WrapperConf)get_store().add_element_user(WRAPPERCONF$0);
            }
            target.set(wrapperConf);
        }
    }
    
    /**
     * Appends and returns a new empty "wrapper-conf" element
     */
    public WrapperConf addNewWrapperConf()
    {
        synchronized (monitor())
        {
            check_orphaned();
            WrapperConf target = null;
            target = (WrapperConf)get_store().add_element_user(WRAPPERCONF$0);
            return target;
        }
    }
    /**
     * An XML wrapper-conf(@).
     *
     * This is a complex type.
     */
    public static class WrapperConfImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements WrapperConf
    {
        private static final long serialVersionUID = 1L;
        
        public WrapperConfImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName ENTRY$0 = 
            new javax.xml.namespace.QName("", "entry");
        private static final javax.xml.namespace.QName FLAT$2 = 
            new javax.xml.namespace.QName("", "flat");
        
        
        /**
         * Gets array of all "entry" elements
         */
        public Entry[] getEntryArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(ENTRY$0, targetList);
                Entry[] result = new Entry[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "entry" element
         */
        public Entry getEntryArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                Entry target = null;
                target = (Entry)get_store().find_element_user(ENTRY$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "entry" element
         */
        public int sizeOfEntryArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(ENTRY$0);
            }
        }
        
        /**
         * Sets array of all "entry" element
         */
        public void setEntryArray(Entry[] entryArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(entryArray, ENTRY$0);
            }
        }
        
        /**
         * Sets ith "entry" element
         */
        public void setEntryArray(int i, Entry entry)
        {
            synchronized (monitor())
            {
                check_orphaned();
                Entry target = null;
                target = (Entry)get_store().find_element_user(ENTRY$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(entry);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "entry" element
         */
        public Entry insertNewEntry(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                Entry target = null;
                target = (Entry)get_store().insert_element_user(ENTRY$0, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "entry" element
         */
        public Entry addNewEntry()
        {
            synchronized (monitor())
            {
                check_orphaned();
                Entry target = null;
                target = (Entry)get_store().add_element_user(ENTRY$0);
                return target;
            }
        }
        
        /**
         * Removes the ith "entry" element
         */
        public void removeEntry(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(ENTRY$0, i);
            }
        }
        
        /**
         * Gets the "flat" attribute
         */
        public boolean getFlat()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FLAT$2);
                if (target == null)
                {
                    return false;
                }
                return target.getBooleanValue();
            }
        }
        
        /**
         * Gets (as xml) the "flat" attribute
         */
        public org.apache.xmlbeans.XmlBoolean xgetFlat()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlBoolean target = null;
                target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(FLAT$2);
                return target;
            }
        }
        
        /**
         * True if has "flat" attribute
         */
        public boolean isSetFlat()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(FLAT$2) != null;
            }
        }
        
        /**
         * Sets the "flat" attribute
         */
        public void setFlat(boolean flat)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FLAT$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(FLAT$2);
                }
                target.setBooleanValue(flat);
            }
        }
        
        /**
         * Sets (as xml) the "flat" attribute
         */
        public void xsetFlat(org.apache.xmlbeans.XmlBoolean flat)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlBoolean target = null;
                target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(FLAT$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(FLAT$2);
                }
                target.set(flat);
            }
        }
        
        /**
         * Unsets the "flat" attribute
         */
        public void unsetFlat()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(FLAT$2);
            }
        }
        /**
         * An XML entry(@).
         *
         * This is a complex type.
         */
        public static class EntryImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements Entry
        {
            private static final long serialVersionUID = 1L;
            
            public EntryImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType);
            }
            
            private static final javax.xml.namespace.QName KEY$0 = 
                new javax.xml.namespace.QName("", "key");
            private static final javax.xml.namespace.QName VALUE$2 = 
                new javax.xml.namespace.QName("", "value");
            
            
            /**
             * Gets the "key" attribute
             */
            public String getKey()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(KEY$0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target.getStringValue();
                }
            }
            
            /**
             * Gets (as xml) the "key" attribute
             */
            public org.apache.xmlbeans.XmlString xgetKey()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlString target = null;
                    target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(KEY$0);
                    return target;
                }
            }
            
            /**
             * Sets the "key" attribute
             */
            public void setKey(String key)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(KEY$0);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(KEY$0);
                    }
                    target.setStringValue(key);
                }
            }
            
            /**
             * Sets (as xml) the "key" attribute
             */
            public void xsetKey(org.apache.xmlbeans.XmlString key)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlString target = null;
                    target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(KEY$0);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(KEY$0);
                    }
                    target.set(key);
                }
            }
            
            /**
             * Gets the "value" attribute
             */
            public String getValue()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VALUE$2);
                    if (target == null)
                    {
                      return null;
                    }
                    return target.getStringValue();
                }
            }
            
            /**
             * Gets (as xml) the "value" attribute
             */
            public org.apache.xmlbeans.XmlString xgetValue()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlString target = null;
                    target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(VALUE$2);
                    return target;
                }
            }
            
            /**
             * Sets the "value" attribute
             */
            public void setValue(String value)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VALUE$2);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(VALUE$2);
                    }
                    target.setStringValue(value);
                }
            }
            
            /**
             * Sets (as xml) the "value" attribute
             */
            public void xsetValue(org.apache.xmlbeans.XmlString value)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlString target = null;
                    target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(VALUE$2);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(VALUE$2);
                    }
                    target.set(value);
                }
            }
        }
    }
}

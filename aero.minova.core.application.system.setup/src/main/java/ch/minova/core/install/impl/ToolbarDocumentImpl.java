/*
 * An XML document type.
 * Localname: toolbar
 * Namespace: 
 * Java type: ch.minova.core.install.ToolbarDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one toolbar(@) element.
 *
 * This is a complex type.
 */
public class ToolbarDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.ToolbarDocument
{
    private static final long serialVersionUID = 1L;
    
    public ToolbarDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TOOLBAR$0 = 
        new javax.xml.namespace.QName("", "toolbar");
    
    
    /**
     * Gets the "toolbar" element
     */
    public Toolbar getToolbar()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Toolbar target = null;
            target = (Toolbar)get_store().find_element_user(TOOLBAR$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "toolbar" element
     */
    public void setToolbar(Toolbar toolbar)
    {
        synchronized (monitor())
        {
            check_orphaned();
            Toolbar target = null;
            target = (Toolbar)get_store().find_element_user(TOOLBAR$0, 0);
            if (target == null)
            {
                target = (Toolbar)get_store().add_element_user(TOOLBAR$0);
            }
            target.set(toolbar);
        }
    }
    
    /**
     * Appends and returns a new empty "toolbar" element
     */
    public Toolbar addNewToolbar()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Toolbar target = null;
            target = (Toolbar)get_store().add_element_user(TOOLBAR$0);
            return target;
        }
    }
    /**
     * An XML toolbar(@).
     *
     * This is a complex type.
     */
    public static class ToolbarImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements Toolbar
    {
        private static final long serialVersionUID = 1L;
        
        public ToolbarImpl(org.apache.xmlbeans.SchemaType sType)
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
        public ch.minova.core.install.EntryDocument.Entry[] getEntryArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(ENTRY$0, targetList);
                ch.minova.core.install.EntryDocument.Entry[] result = new ch.minova.core.install.EntryDocument.Entry[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "entry" element
         */
        public ch.minova.core.install.EntryDocument.Entry getEntryArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.EntryDocument.Entry target = null;
                target = (ch.minova.core.install.EntryDocument.Entry)get_store().find_element_user(ENTRY$0, i);
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
        public void setEntryArray(ch.minova.core.install.EntryDocument.Entry[] entryArray)
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
        public void setEntryArray(int i, ch.minova.core.install.EntryDocument.Entry entry)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.EntryDocument.Entry target = null;
                target = (ch.minova.core.install.EntryDocument.Entry)get_store().find_element_user(ENTRY$0, i);
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
        public ch.minova.core.install.EntryDocument.Entry insertNewEntry(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.EntryDocument.Entry target = null;
                target = (ch.minova.core.install.EntryDocument.Entry)get_store().insert_element_user(ENTRY$0, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "entry" element
         */
        public ch.minova.core.install.EntryDocument.Entry addNewEntry()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.EntryDocument.Entry target = null;
                target = (ch.minova.core.install.EntryDocument.Entry)get_store().add_element_user(ENTRY$0);
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
    }
}

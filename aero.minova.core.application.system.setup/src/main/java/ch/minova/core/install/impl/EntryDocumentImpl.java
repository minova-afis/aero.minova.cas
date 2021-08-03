/*
 * An XML document type.
 * Localname: entry
 * Namespace: 
 * Java type: ch.minova.core.install.EntryDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one entry(@) element.
 *
 * This is a complex type.
 */
public class EntryDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.EntryDocument
{
    private static final long serialVersionUID = 1L;
    
    public EntryDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ENTRY$0 = 
        new javax.xml.namespace.QName("", "entry");
    
    
    /**
     * Gets the "entry" element
     */
    public Entry getEntry()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Entry target = null;
            target = (Entry)get_store().find_element_user(ENTRY$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "entry" element
     */
    public void setEntry(Entry entry)
    {
        synchronized (monitor())
        {
            check_orphaned();
            Entry target = null;
            target = (Entry)get_store().find_element_user(ENTRY$0, 0);
            if (target == null)
            {
                target = (Entry)get_store().add_element_user(ENTRY$0);
            }
            target.set(entry);
        }
    }
    
    /**
     * Appends and returns a new empty "entry" element
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
        
        private static final javax.xml.namespace.QName ID$0 = 
            new javax.xml.namespace.QName("", "id");
        private static final javax.xml.namespace.QName POSITION$2 = 
            new javax.xml.namespace.QName("", "position");
        private static final javax.xml.namespace.QName SEPARATORAFTER$4 = 
            new javax.xml.namespace.QName("", "separator-after");
        private static final javax.xml.namespace.QName SEPARATORBEFORE$6 = 
            new javax.xml.namespace.QName("", "separator-before");
        private static final javax.xml.namespace.QName TYPE$8 = 
            new javax.xml.namespace.QName("", "type");
        private static final javax.xml.namespace.QName OVERRIDE$10 = 
            new javax.xml.namespace.QName("", "override");
        
        
        /**
         * Gets the "id" attribute
         */
        public String getId()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ID$0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "id" attribute
         */
        public org.apache.xmlbeans.XmlNCName xgetId()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(ID$0);
                return target;
            }
        }
        
        /**
         * Sets the "id" attribute
         */
        public void setId(String id)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ID$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(ID$0);
                }
                target.setStringValue(id);
            }
        }
        
        /**
         * Sets (as xml) the "id" attribute
         */
        public void xsetId(org.apache.xmlbeans.XmlNCName id)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(ID$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlNCName)get_store().add_attribute_user(ID$0);
                }
                target.set(id);
            }
        }
        
        /**
         * Gets the "position" attribute
         */
        public float getPosition()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(POSITION$2);
                if (target == null)
                {
                    return 0.0f;
                }
                return target.getFloatValue();
            }
        }
        
        /**
         * Gets (as xml) the "position" attribute
         */
        public org.apache.xmlbeans.XmlFloat xgetPosition()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlFloat target = null;
                target = (org.apache.xmlbeans.XmlFloat)get_store().find_attribute_user(POSITION$2);
                return target;
            }
        }
        
        /**
         * Sets the "position" attribute
         */
        public void setPosition(float position)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(POSITION$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(POSITION$2);
                }
                target.setFloatValue(position);
            }
        }
        
        /**
         * Sets (as xml) the "position" attribute
         */
        public void xsetPosition(org.apache.xmlbeans.XmlFloat position)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlFloat target = null;
                target = (org.apache.xmlbeans.XmlFloat)get_store().find_attribute_user(POSITION$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlFloat)get_store().add_attribute_user(POSITION$2);
                }
                target.set(position);
            }
        }
        
        /**
         * Gets the "separator-after" attribute
         */
        public boolean getSeparatorAfter()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SEPARATORAFTER$4);
                if (target == null)
                {
                    return false;
                }
                return target.getBooleanValue();
            }
        }
        
        /**
         * Gets (as xml) the "separator-after" attribute
         */
        public org.apache.xmlbeans.XmlBoolean xgetSeparatorAfter()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlBoolean target = null;
                target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(SEPARATORAFTER$4);
                return target;
            }
        }
        
        /**
         * True if has "separator-after" attribute
         */
        public boolean isSetSeparatorAfter()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(SEPARATORAFTER$4) != null;
            }
        }
        
        /**
         * Sets the "separator-after" attribute
         */
        public void setSeparatorAfter(boolean separatorAfter)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SEPARATORAFTER$4);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(SEPARATORAFTER$4);
                }
                target.setBooleanValue(separatorAfter);
            }
        }
        
        /**
         * Sets (as xml) the "separator-after" attribute
         */
        public void xsetSeparatorAfter(org.apache.xmlbeans.XmlBoolean separatorAfter)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlBoolean target = null;
                target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(SEPARATORAFTER$4);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(SEPARATORAFTER$4);
                }
                target.set(separatorAfter);
            }
        }
        
        /**
         * Unsets the "separator-after" attribute
         */
        public void unsetSeparatorAfter()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(SEPARATORAFTER$4);
            }
        }
        
        /**
         * Gets the "separator-before" attribute
         */
        public boolean getSeparatorBefore()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SEPARATORBEFORE$6);
                if (target == null)
                {
                    return false;
                }
                return target.getBooleanValue();
            }
        }
        
        /**
         * Gets (as xml) the "separator-before" attribute
         */
        public org.apache.xmlbeans.XmlBoolean xgetSeparatorBefore()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlBoolean target = null;
                target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(SEPARATORBEFORE$6);
                return target;
            }
        }
        
        /**
         * True if has "separator-before" attribute
         */
        public boolean isSetSeparatorBefore()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(SEPARATORBEFORE$6) != null;
            }
        }
        
        /**
         * Sets the "separator-before" attribute
         */
        public void setSeparatorBefore(boolean separatorBefore)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SEPARATORBEFORE$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(SEPARATORBEFORE$6);
                }
                target.setBooleanValue(separatorBefore);
            }
        }
        
        /**
         * Sets (as xml) the "separator-before" attribute
         */
        public void xsetSeparatorBefore(org.apache.xmlbeans.XmlBoolean separatorBefore)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlBoolean target = null;
                target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(SEPARATORBEFORE$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(SEPARATORBEFORE$6);
                }
                target.set(separatorBefore);
            }
        }
        
        /**
         * Unsets the "separator-before" attribute
         */
        public void unsetSeparatorBefore()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(SEPARATORBEFORE$6);
            }
        }
        
        /**
         * Gets the "type" attribute
         */
        public String getType()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TYPE$8);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "type" attribute
         */
        public org.apache.xmlbeans.XmlNCName xgetType()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(TYPE$8);
                return target;
            }
        }
        
        /**
         * Sets the "type" attribute
         */
        public void setType(String type)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TYPE$8);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(TYPE$8);
                }
                target.setStringValue(type);
            }
        }
        
        /**
         * Sets (as xml) the "type" attribute
         */
        public void xsetType(org.apache.xmlbeans.XmlNCName type)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(TYPE$8);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlNCName)get_store().add_attribute_user(TYPE$8);
                }
                target.set(type);
            }
        }
        
        /**
         * Gets the "override" attribute
         */
        public String getOverride()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OVERRIDE$10);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(OVERRIDE$10);
                }
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "override" attribute
         */
        public org.apache.xmlbeans.XmlNCName xgetOverride()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(OVERRIDE$10);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlNCName)get_default_attribute_value(OVERRIDE$10);
                }
                return target;
            }
        }
        
        /**
         * True if has "override" attribute
         */
        public boolean isSetOverride()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(OVERRIDE$10) != null;
            }
        }
        
        /**
         * Sets the "override" attribute
         */
        public void setOverride(String override)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OVERRIDE$10);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(OVERRIDE$10);
                }
                target.setStringValue(override);
            }
        }
        
        /**
         * Sets (as xml) the "override" attribute
         */
        public void xsetOverride(org.apache.xmlbeans.XmlNCName override)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(OVERRIDE$10);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlNCName)get_store().add_attribute_user(OVERRIDE$10);
                }
                target.set(override);
            }
        }
        
        /**
         * Unsets the "override" attribute
         */
        public void unsetOverride()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(OVERRIDE$10);
            }
        }
    }
}

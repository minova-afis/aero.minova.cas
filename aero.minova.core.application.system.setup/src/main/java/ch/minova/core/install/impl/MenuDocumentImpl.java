/*
 * An XML document type.
 * Localname: menu
 * Namespace: 
 * Java type: ch.minova.core.install.MenuDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one menu(@) element.
 *
 * This is a complex type.
 */
public class MenuDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.MenuDocument
{
    private static final long serialVersionUID = 1L;
    
    public MenuDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName MENU$0 = 
        new javax.xml.namespace.QName("", "menu");
    
    
    /**
     * Gets the "menu" element
     */
    public Menu getMenu()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Menu target = null;
            target = (Menu)get_store().find_element_user(MENU$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "menu" element
     */
    public void setMenu(Menu menu)
    {
        synchronized (monitor())
        {
            check_orphaned();
            Menu target = null;
            target = (Menu)get_store().find_element_user(MENU$0, 0);
            if (target == null)
            {
                target = (Menu)get_store().add_element_user(MENU$0);
            }
            target.set(menu);
        }
    }
    
    /**
     * Appends and returns a new empty "menu" element
     */
    public Menu addNewMenu()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Menu target = null;
            target = (Menu)get_store().add_element_user(MENU$0);
            return target;
        }
    }
    /**
     * An XML menu(@).
     *
     * This is a complex type.
     */
    public static class MenuImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements Menu
    {
        private static final long serialVersionUID = 1L;
        
        public MenuImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName MENU$0 = 
            new javax.xml.namespace.QName("", "menu");
        private static final javax.xml.namespace.QName ENTRY$2 = 
            new javax.xml.namespace.QName("", "entry");
        private static final javax.xml.namespace.QName POSITION$4 = 
            new javax.xml.namespace.QName("", "position");
        private static final javax.xml.namespace.QName ID$6 = 
            new javax.xml.namespace.QName("", "id");
        private static final javax.xml.namespace.QName TEXT$8 = 
            new javax.xml.namespace.QName("", "text");
        private static final javax.xml.namespace.QName OVERRIDE$10 = 
            new javax.xml.namespace.QName("", "override");
        
        
        /**
         * Gets array of all "menu" elements
         */
        public Menu[] getMenuArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(MENU$0, targetList);
                Menu[] result = new Menu[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "menu" element
         */
        public Menu getMenuArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                Menu target = null;
                target = (Menu)get_store().find_element_user(MENU$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "menu" element
         */
        public int sizeOfMenuArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(MENU$0);
            }
        }
        
        /**
         * Sets array of all "menu" element
         */
        public void setMenuArray(Menu[] menuArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(menuArray, MENU$0);
            }
        }
        
        /**
         * Sets ith "menu" element
         */
        public void setMenuArray(int i, Menu menu)
        {
            synchronized (monitor())
            {
                check_orphaned();
                Menu target = null;
                target = (Menu)get_store().find_element_user(MENU$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(menu);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "menu" element
         */
        public Menu insertNewMenu(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                Menu target = null;
                target = (Menu)get_store().insert_element_user(MENU$0, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "menu" element
         */
        public Menu addNewMenu()
        {
            synchronized (monitor())
            {
                check_orphaned();
                Menu target = null;
                target = (Menu)get_store().add_element_user(MENU$0);
                return target;
            }
        }
        
        /**
         * Removes the ith "menu" element
         */
        public void removeMenu(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(MENU$0, i);
            }
        }
        
        /**
         * Gets array of all "entry" elements
         */
        public Entry[] getEntryArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(ENTRY$2, targetList);
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
                target = (Entry)get_store().find_element_user(ENTRY$2, i);
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
                return get_store().count_elements(ENTRY$2);
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
                arraySetterHelper(entryArray, ENTRY$2);
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
                target = (Entry)get_store().find_element_user(ENTRY$2, i);
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
                target = (Entry)get_store().insert_element_user(ENTRY$2, i);
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
                target = (Entry)get_store().add_element_user(ENTRY$2);
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
                get_store().remove_element(ENTRY$2, i);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(POSITION$4);
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
                target = (org.apache.xmlbeans.XmlFloat)get_store().find_attribute_user(POSITION$4);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(POSITION$4);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(POSITION$4);
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
                target = (org.apache.xmlbeans.XmlFloat)get_store().find_attribute_user(POSITION$4);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlFloat)get_store().add_attribute_user(POSITION$4);
                }
                target.set(position);
            }
        }
        
        /**
         * Gets the "id" attribute
         */
        public String getId()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ID$6);
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
        public org.apache.xmlbeans.XmlID xgetId()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlID target = null;
                target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(ID$6);
                return target;
            }
        }
        
        /**
         * True if has "id" attribute
         */
        public boolean isSetId()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(ID$6) != null;
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ID$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(ID$6);
                }
                target.setStringValue(id);
            }
        }
        
        /**
         * Sets (as xml) the "id" attribute
         */
        public void xsetId(org.apache.xmlbeans.XmlID id)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlID target = null;
                target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(ID$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlID)get_store().add_attribute_user(ID$6);
                }
                target.set(id);
            }
        }
        
        /**
         * Unsets the "id" attribute
         */
        public void unsetId()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(ID$6);
            }
        }
        
        /**
         * Gets the "text" attribute
         */
        public String getText()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TEXT$8);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "text" attribute
         */
        public org.apache.xmlbeans.XmlString xgetText()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(TEXT$8);
                return target;
            }
        }
        
        /**
         * True if has "text" attribute
         */
        public boolean isSetText()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(TEXT$8) != null;
            }
        }
        
        /**
         * Sets the "text" attribute
         */
        public void setText(String text)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TEXT$8);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(TEXT$8);
                }
                target.setStringValue(text);
            }
        }
        
        /**
         * Sets (as xml) the "text" attribute
         */
        public void xsetText(org.apache.xmlbeans.XmlString text)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(TEXT$8);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(TEXT$8);
                }
                target.set(text);
            }
        }
        
        /**
         * Unsets the "text" attribute
         */
        public void unsetText()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(TEXT$8);
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
            private static final javax.xml.namespace.QName OVERRIDE$4 = 
                new javax.xml.namespace.QName("", "override");
            private static final javax.xml.namespace.QName SEPARATORAFTER$6 = 
                new javax.xml.namespace.QName("", "separator-after");
            private static final javax.xml.namespace.QName SEPARATORBEFORE$8 = 
                new javax.xml.namespace.QName("", "separator-before");
            private static final javax.xml.namespace.QName TYPE$10 = 
                new javax.xml.namespace.QName("", "type");
            
            
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
            public org.apache.xmlbeans.XmlIDREF xgetId()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlIDREF target = null;
                    target = (org.apache.xmlbeans.XmlIDREF)get_store().find_attribute_user(ID$0);
                    return target;
                }
            }
            
            /**
             * True if has "id" attribute
             */
            public boolean isSetId()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    return get_store().find_attribute_user(ID$0) != null;
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
            public void xsetId(org.apache.xmlbeans.XmlIDREF id)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlIDREF target = null;
                    target = (org.apache.xmlbeans.XmlIDREF)get_store().find_attribute_user(ID$0);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.XmlIDREF)get_store().add_attribute_user(ID$0);
                    }
                    target.set(id);
                }
            }
            
            /**
             * Unsets the "id" attribute
             */
            public void unsetId()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    get_store().remove_attribute(ID$0);
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
             * Gets the "override" attribute
             */
            public String getOverride()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OVERRIDE$4);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(OVERRIDE$4);
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
                    target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(OVERRIDE$4);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.XmlNCName)get_default_attribute_value(OVERRIDE$4);
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
                    return get_store().find_attribute_user(OVERRIDE$4) != null;
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
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OVERRIDE$4);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(OVERRIDE$4);
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
                    target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(OVERRIDE$4);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.XmlNCName)get_store().add_attribute_user(OVERRIDE$4);
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
                    get_store().remove_attribute(OVERRIDE$4);
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
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SEPARATORAFTER$6);
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
                    target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(SEPARATORAFTER$6);
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
                    return get_store().find_attribute_user(SEPARATORAFTER$6) != null;
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
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SEPARATORAFTER$6);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(SEPARATORAFTER$6);
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
                    target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(SEPARATORAFTER$6);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(SEPARATORAFTER$6);
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
                    get_store().remove_attribute(SEPARATORAFTER$6);
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
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SEPARATORBEFORE$8);
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
                    target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(SEPARATORBEFORE$8);
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
                    return get_store().find_attribute_user(SEPARATORBEFORE$8) != null;
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
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SEPARATORBEFORE$8);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(SEPARATORBEFORE$8);
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
                    target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(SEPARATORBEFORE$8);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(SEPARATORBEFORE$8);
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
                    get_store().remove_attribute(SEPARATORBEFORE$8);
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
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TYPE$10);
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
                    target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(TYPE$10);
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
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TYPE$10);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(TYPE$10);
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
                    target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(TYPE$10);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.XmlNCName)get_store().add_attribute_user(TYPE$10);
                    }
                    target.set(type);
                }
            }
        }
    }
}

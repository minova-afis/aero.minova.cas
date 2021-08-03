/*
 * An XML document type.
 * Localname: main
 * Namespace: 
 * Java type: ch.minova.core.install.MainDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one main(@) element.
 *
 * This is a complex type.
 */
public class MainDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.MainDocument
{
    private static final long serialVersionUID = 1L;
    
    public MainDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName MAIN$0 = 
        new javax.xml.namespace.QName("", "main");
    
    
    /**
     * Gets the "main" element
     */
    public Main getMain()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Main target = null;
            target = (Main)get_store().find_element_user(MAIN$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "main" element
     */
    public void setMain(Main main)
    {
        synchronized (monitor())
        {
            check_orphaned();
            Main target = null;
            target = (Main)get_store().find_element_user(MAIN$0, 0);
            if (target == null)
            {
                target = (Main)get_store().add_element_user(MAIN$0);
            }
            target.set(main);
        }
    }
    
    /**
     * Appends and returns a new empty "main" element
     */
    public Main addNewMain()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Main target = null;
            target = (Main)get_store().add_element_user(MAIN$0);
            return target;
        }
    }
    /**
     * An XML main(@).
     *
     * This is a complex type.
     */
    public static class MainImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements Main
    {
        private static final long serialVersionUID = 1L;
        
        public MainImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName ACTION$0 = 
            new javax.xml.namespace.QName("", "action");
        private static final javax.xml.namespace.QName MENU$2 = 
            new javax.xml.namespace.QName("", "menu");
        private static final javax.xml.namespace.QName TOOLBAR$4 = 
            new javax.xml.namespace.QName("", "toolbar");
        private static final javax.xml.namespace.QName ICON$6 = 
            new javax.xml.namespace.QName("", "icon");
        private static final javax.xml.namespace.QName TITEL$8 = 
            new javax.xml.namespace.QName("", "titel");
        
        
        /**
         * Gets array of all "action" elements
         */
        public ch.minova.core.install.ActionDocument.Action[] getActionArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(ACTION$0, targetList);
                ch.minova.core.install.ActionDocument.Action[] result = new ch.minova.core.install.ActionDocument.Action[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "action" element
         */
        public ch.minova.core.install.ActionDocument.Action getActionArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.ActionDocument.Action target = null;
                target = (ch.minova.core.install.ActionDocument.Action)get_store().find_element_user(ACTION$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "action" element
         */
        public int sizeOfActionArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(ACTION$0);
            }
        }
        
        /**
         * Sets array of all "action" element
         */
        public void setActionArray(ch.minova.core.install.ActionDocument.Action[] actionArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(actionArray, ACTION$0);
            }
        }
        
        /**
         * Sets ith "action" element
         */
        public void setActionArray(int i, ch.minova.core.install.ActionDocument.Action action)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.ActionDocument.Action target = null;
                target = (ch.minova.core.install.ActionDocument.Action)get_store().find_element_user(ACTION$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(action);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "action" element
         */
        public ch.minova.core.install.ActionDocument.Action insertNewAction(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.ActionDocument.Action target = null;
                target = (ch.minova.core.install.ActionDocument.Action)get_store().insert_element_user(ACTION$0, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "action" element
         */
        public ch.minova.core.install.ActionDocument.Action addNewAction()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.ActionDocument.Action target = null;
                target = (ch.minova.core.install.ActionDocument.Action)get_store().add_element_user(ACTION$0);
                return target;
            }
        }
        
        /**
         * Removes the ith "action" element
         */
        public void removeAction(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(ACTION$0, i);
            }
        }
        
        /**
         * Gets the "menu" element
         */
        public ch.minova.core.install.MenuDocument.Menu getMenu()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.MenuDocument.Menu target = null;
                target = (ch.minova.core.install.MenuDocument.Menu)get_store().find_element_user(MENU$2, 0);
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
        public void setMenu(ch.minova.core.install.MenuDocument.Menu menu)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.MenuDocument.Menu target = null;
                target = (ch.minova.core.install.MenuDocument.Menu)get_store().find_element_user(MENU$2, 0);
                if (target == null)
                {
                    target = (ch.minova.core.install.MenuDocument.Menu)get_store().add_element_user(MENU$2);
                }
                target.set(menu);
            }
        }
        
        /**
         * Appends and returns a new empty "menu" element
         */
        public ch.minova.core.install.MenuDocument.Menu addNewMenu()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.MenuDocument.Menu target = null;
                target = (ch.minova.core.install.MenuDocument.Menu)get_store().add_element_user(MENU$2);
                return target;
            }
        }
        
        /**
         * Gets the "toolbar" element
         */
        public ch.minova.core.install.ToolbarDocument.Toolbar getToolbar()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.ToolbarDocument.Toolbar target = null;
                target = (ch.minova.core.install.ToolbarDocument.Toolbar)get_store().find_element_user(TOOLBAR$4, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "toolbar" element
         */
        public boolean isSetToolbar()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(TOOLBAR$4) != 0;
            }
        }
        
        /**
         * Sets the "toolbar" element
         */
        public void setToolbar(ch.minova.core.install.ToolbarDocument.Toolbar toolbar)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.ToolbarDocument.Toolbar target = null;
                target = (ch.minova.core.install.ToolbarDocument.Toolbar)get_store().find_element_user(TOOLBAR$4, 0);
                if (target == null)
                {
                    target = (ch.minova.core.install.ToolbarDocument.Toolbar)get_store().add_element_user(TOOLBAR$4);
                }
                target.set(toolbar);
            }
        }
        
        /**
         * Appends and returns a new empty "toolbar" element
         */
        public ch.minova.core.install.ToolbarDocument.Toolbar addNewToolbar()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.ToolbarDocument.Toolbar target = null;
                target = (ch.minova.core.install.ToolbarDocument.Toolbar)get_store().add_element_user(TOOLBAR$4);
                return target;
            }
        }
        
        /**
         * Unsets the "toolbar" element
         */
        public void unsetToolbar()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(TOOLBAR$4, 0);
            }
        }
        
        /**
         * Gets the "icon" attribute
         */
        public String getIcon()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ICON$6);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "icon" attribute
         */
        public org.apache.xmlbeans.XmlNCName xgetIcon()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(ICON$6);
                return target;
            }
        }
        
        /**
         * True if has "icon" attribute
         */
        public boolean isSetIcon()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(ICON$6) != null;
            }
        }
        
        /**
         * Sets the "icon" attribute
         */
        public void setIcon(String icon)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ICON$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(ICON$6);
                }
                target.setStringValue(icon);
            }
        }
        
        /**
         * Sets (as xml) the "icon" attribute
         */
        public void xsetIcon(org.apache.xmlbeans.XmlNCName icon)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(ICON$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlNCName)get_store().add_attribute_user(ICON$6);
                }
                target.set(icon);
            }
        }
        
        /**
         * Unsets the "icon" attribute
         */
        public void unsetIcon()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(ICON$6);
            }
        }
        
        /**
         * Gets the "titel" attribute
         */
        public String getTitel()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TITEL$8);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "titel" attribute
         */
        public org.apache.xmlbeans.XmlNCName xgetTitel()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(TITEL$8);
                return target;
            }
        }
        
        /**
         * True if has "titel" attribute
         */
        public boolean isSetTitel()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(TITEL$8) != null;
            }
        }
        
        /**
         * Sets the "titel" attribute
         */
        public void setTitel(String titel)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TITEL$8);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(TITEL$8);
                }
                target.setStringValue(titel);
            }
        }
        
        /**
         * Sets (as xml) the "titel" attribute
         */
        public void xsetTitel(org.apache.xmlbeans.XmlNCName titel)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(TITEL$8);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlNCName)get_store().add_attribute_user(TITEL$8);
                }
                target.set(titel);
            }
        }
        
        /**
         * Unsets the "titel" attribute
         */
        public void unsetTitel()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(TITEL$8);
            }
        }
    }
}

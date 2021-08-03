/*
 * An XML document type.
 * Localname: mdi-code
 * Namespace: 
 * Java type: ch.minova.core.install.MdiCodeDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one mdi-code(@) element.
 *
 * This is a complex type.
 */
public class MdiCodeDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.MdiCodeDocument
{
    private static final long serialVersionUID = 1L;
    
    public MdiCodeDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName MDICODE$0 = 
        new javax.xml.namespace.QName("", "mdi-code");
    
    
    /**
     * Gets the "mdi-code" element
     */
    public MdiCode getMdiCode()
    {
        synchronized (monitor())
        {
            check_orphaned();
            MdiCode target = null;
            target = (MdiCode)get_store().find_element_user(MDICODE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "mdi-code" element
     */
    public void setMdiCode(MdiCode mdiCode)
    {
        synchronized (monitor())
        {
            check_orphaned();
            MdiCode target = null;
            target = (MdiCode)get_store().find_element_user(MDICODE$0, 0);
            if (target == null)
            {
                target = (MdiCode)get_store().add_element_user(MDICODE$0);
            }
            target.set(mdiCode);
        }
    }
    
    /**
     * Appends and returns a new empty "mdi-code" element
     */
    public MdiCode addNewMdiCode()
    {
        synchronized (monitor())
        {
            check_orphaned();
            MdiCode target = null;
            target = (MdiCode)get_store().add_element_user(MDICODE$0);
            return target;
        }
    }
    /**
     * An XML mdi-code(@).
     *
     * This is a complex type.
     */
    public static class MdiCodeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements MdiCode
    {
        private static final long serialVersionUID = 1L;
        
        public MdiCodeImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName ACTION$0 = 
            new javax.xml.namespace.QName("", "action");
        private static final javax.xml.namespace.QName MENU$2 = 
            new javax.xml.namespace.QName("", "menu");
        private static final javax.xml.namespace.QName TOOLBAR$4 = 
            new javax.xml.namespace.QName("", "toolbar");
        
        
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
         * Gets array of all "menu" elements
         */
        public ch.minova.core.install.MenuDocument.Menu[] getMenuArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(MENU$2, targetList);
                ch.minova.core.install.MenuDocument.Menu[] result = new ch.minova.core.install.MenuDocument.Menu[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "menu" element
         */
        public ch.minova.core.install.MenuDocument.Menu getMenuArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.MenuDocument.Menu target = null;
                target = (ch.minova.core.install.MenuDocument.Menu)get_store().find_element_user(MENU$2, i);
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
                return get_store().count_elements(MENU$2);
            }
        }
        
        /**
         * Sets array of all "menu" element
         */
        public void setMenuArray(ch.minova.core.install.MenuDocument.Menu[] menuArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(menuArray, MENU$2);
            }
        }
        
        /**
         * Sets ith "menu" element
         */
        public void setMenuArray(int i, ch.minova.core.install.MenuDocument.Menu menu)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.MenuDocument.Menu target = null;
                target = (ch.minova.core.install.MenuDocument.Menu)get_store().find_element_user(MENU$2, i);
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
        public ch.minova.core.install.MenuDocument.Menu insertNewMenu(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.MenuDocument.Menu target = null;
                target = (ch.minova.core.install.MenuDocument.Menu)get_store().insert_element_user(MENU$2, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "menu" element
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
         * Removes the ith "menu" element
         */
        public void removeMenu(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(MENU$2, i);
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
    }
}

/*
 * An XML document type.
 * Localname: action
 * Namespace: 
 * Java type: ch.minova.core.install.ActionDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one action(@) element.
 *
 * This is a complex type.
 */
public class ActionDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.ActionDocument
{
    private static final long serialVersionUID = 1L;
    
    public ActionDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ACTION$0 = 
        new javax.xml.namespace.QName("", "action");
    
    
    /**
     * Gets the "action" element
     */
    public Action getAction()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Action target = null;
            target = (Action)get_store().find_element_user(ACTION$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "action" element
     */
    public void setAction(Action action)
    {
        synchronized (monitor())
        {
            check_orphaned();
            Action target = null;
            target = (Action)get_store().find_element_user(ACTION$0, 0);
            if (target == null)
            {
                target = (Action)get_store().add_element_user(ACTION$0);
            }
            target.set(action);
        }
    }
    
    /**
     * Appends and returns a new empty "action" element
     */
    public Action addNewAction()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Action target = null;
            target = (Action)get_store().add_element_user(ACTION$0);
            return target;
        }
    }
    /**
     * An XML action(@).
     *
     * This is a complex type.
     */
    public static class ActionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements Action
    {
        private static final long serialVersionUID = 1L;
        
        public ActionImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName ACTION$0 = 
            new javax.xml.namespace.QName("", "action");
        private static final javax.xml.namespace.QName GENERIC$2 = 
            new javax.xml.namespace.QName("", "generic");
        private static final javax.xml.namespace.QName ICON$4 = 
            new javax.xml.namespace.QName("", "icon");
        private static final javax.xml.namespace.QName ID$6 = 
            new javax.xml.namespace.QName("", "id");
        private static final javax.xml.namespace.QName TEXT$8 = 
            new javax.xml.namespace.QName("", "text");
        private static final javax.xml.namespace.QName AUTOSTART$10 = 
            new javax.xml.namespace.QName("", "autostart");
        private static final javax.xml.namespace.QName DETAILVISIBLE$12 = 
            new javax.xml.namespace.QName("", "detail-visible");
        private static final javax.xml.namespace.QName DIALOG$14 = 
            new javax.xml.namespace.QName("", "dialog");
        private static final javax.xml.namespace.QName DOCUMENTATION$16 = 
            new javax.xml.namespace.QName("", "documentation");
        private static final javax.xml.namespace.QName PARAM$18 = 
            new javax.xml.namespace.QName("", "param");
        private static final javax.xml.namespace.QName SHORTCUT$20 = 
            new javax.xml.namespace.QName("", "shortcut");
        private static final javax.xml.namespace.QName SUPRESSPRINT$22 = 
            new javax.xml.namespace.QName("", "supress-print");
        private static final javax.xml.namespace.QName VISIBLE$24 = 
            new javax.xml.namespace.QName("", "visible");
        
        
        /**
         * Gets the "action" attribute
         */
        public String getAction()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ACTION$0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "action" attribute
         */
        public org.apache.xmlbeans.XmlNCName xgetAction()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(ACTION$0);
                return target;
            }
        }
        
        /**
         * Sets the "action" attribute
         */
        public void setAction(String action)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ACTION$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(ACTION$0);
                }
                target.setStringValue(action);
            }
        }
        
        /**
         * Sets (as xml) the "action" attribute
         */
        public void xsetAction(org.apache.xmlbeans.XmlNCName action)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(ACTION$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlNCName)get_store().add_attribute_user(ACTION$0);
                }
                target.set(action);
            }
        }
        
        /**
         * Gets the "generic" attribute
         */
        public boolean getGeneric()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(GENERIC$2);
                if (target == null)
                {
                    return false;
                }
                return target.getBooleanValue();
            }
        }
        
        /**
         * Gets (as xml) the "generic" attribute
         */
        public org.apache.xmlbeans.XmlBoolean xgetGeneric()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlBoolean target = null;
                target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(GENERIC$2);
                return target;
            }
        }
        
        /**
         * True if has "generic" attribute
         */
        public boolean isSetGeneric()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(GENERIC$2) != null;
            }
        }
        
        /**
         * Sets the "generic" attribute
         */
        public void setGeneric(boolean generic)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(GENERIC$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(GENERIC$2);
                }
                target.setBooleanValue(generic);
            }
        }
        
        /**
         * Sets (as xml) the "generic" attribute
         */
        public void xsetGeneric(org.apache.xmlbeans.XmlBoolean generic)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlBoolean target = null;
                target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(GENERIC$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(GENERIC$2);
                }
                target.set(generic);
            }
        }
        
        /**
         * Unsets the "generic" attribute
         */
        public void unsetGeneric()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(GENERIC$2);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ICON$4);
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
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(ICON$4);
                return target;
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ICON$4);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(ICON$4);
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
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(ICON$4);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlNCName)get_store().add_attribute_user(ICON$4);
                }
                target.set(icon);
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
         * Gets the "autostart" attribute
         */
        public String getAutostart()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(AUTOSTART$10);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "autostart" attribute
         */
        public org.apache.xmlbeans.XmlNCName xgetAutostart()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(AUTOSTART$10);
                return target;
            }
        }
        
        /**
         * True if has "autostart" attribute
         */
        public boolean isSetAutostart()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(AUTOSTART$10) != null;
            }
        }
        
        /**
         * Sets the "autostart" attribute
         */
        public void setAutostart(String autostart)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(AUTOSTART$10);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(AUTOSTART$10);
                }
                target.setStringValue(autostart);
            }
        }
        
        /**
         * Sets (as xml) the "autostart" attribute
         */
        public void xsetAutostart(org.apache.xmlbeans.XmlNCName autostart)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(AUTOSTART$10);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlNCName)get_store().add_attribute_user(AUTOSTART$10);
                }
                target.set(autostart);
            }
        }
        
        /**
         * Unsets the "autostart" attribute
         */
        public void unsetAutostart()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(AUTOSTART$10);
            }
        }
        
        /**
         * Gets the "detail-visible" attribute
         */
        public String getDetailVisible()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(DETAILVISIBLE$12);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "detail-visible" attribute
         */
        public org.apache.xmlbeans.XmlNCName xgetDetailVisible()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(DETAILVISIBLE$12);
                return target;
            }
        }
        
        /**
         * True if has "detail-visible" attribute
         */
        public boolean isSetDetailVisible()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(DETAILVISIBLE$12) != null;
            }
        }
        
        /**
         * Sets the "detail-visible" attribute
         */
        public void setDetailVisible(String detailVisible)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(DETAILVISIBLE$12);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(DETAILVISIBLE$12);
                }
                target.setStringValue(detailVisible);
            }
        }
        
        /**
         * Sets (as xml) the "detail-visible" attribute
         */
        public void xsetDetailVisible(org.apache.xmlbeans.XmlNCName detailVisible)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(DETAILVISIBLE$12);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlNCName)get_store().add_attribute_user(DETAILVISIBLE$12);
                }
                target.set(detailVisible);
            }
        }
        
        /**
         * Unsets the "detail-visible" attribute
         */
        public void unsetDetailVisible()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(DETAILVISIBLE$12);
            }
        }
        
        /**
         * Gets the "dialog" attribute
         */
        public String getDialog()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(DIALOG$14);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "dialog" attribute
         */
        public org.apache.xmlbeans.XmlNCName xgetDialog()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(DIALOG$14);
                return target;
            }
        }
        
        /**
         * True if has "dialog" attribute
         */
        public boolean isSetDialog()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(DIALOG$14) != null;
            }
        }
        
        /**
         * Sets the "dialog" attribute
         */
        public void setDialog(String dialog)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(DIALOG$14);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(DIALOG$14);
                }
                target.setStringValue(dialog);
            }
        }
        
        /**
         * Sets (as xml) the "dialog" attribute
         */
        public void xsetDialog(org.apache.xmlbeans.XmlNCName dialog)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(DIALOG$14);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlNCName)get_store().add_attribute_user(DIALOG$14);
                }
                target.set(dialog);
            }
        }
        
        /**
         * Unsets the "dialog" attribute
         */
        public void unsetDialog()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(DIALOG$14);
            }
        }
        
        /**
         * Gets the "documentation" attribute
         */
        public String getDocumentation()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(DOCUMENTATION$16);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "documentation" attribute
         */
        public org.apache.xmlbeans.XmlNCName xgetDocumentation()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(DOCUMENTATION$16);
                return target;
            }
        }
        
        /**
         * True if has "documentation" attribute
         */
        public boolean isSetDocumentation()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(DOCUMENTATION$16) != null;
            }
        }
        
        /**
         * Sets the "documentation" attribute
         */
        public void setDocumentation(String documentation)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(DOCUMENTATION$16);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(DOCUMENTATION$16);
                }
                target.setStringValue(documentation);
            }
        }
        
        /**
         * Sets (as xml) the "documentation" attribute
         */
        public void xsetDocumentation(org.apache.xmlbeans.XmlNCName documentation)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(DOCUMENTATION$16);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlNCName)get_store().add_attribute_user(DOCUMENTATION$16);
                }
                target.set(documentation);
            }
        }
        
        /**
         * Unsets the "documentation" attribute
         */
        public void unsetDocumentation()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(DOCUMENTATION$16);
            }
        }
        
        /**
         * Gets the "param" attribute
         */
        public String getParam()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PARAM$18);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "param" attribute
         */
        public org.apache.xmlbeans.XmlNCName xgetParam()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(PARAM$18);
                return target;
            }
        }
        
        /**
         * True if has "param" attribute
         */
        public boolean isSetParam()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(PARAM$18) != null;
            }
        }
        
        /**
         * Sets the "param" attribute
         */
        public void setParam(String param)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PARAM$18);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PARAM$18);
                }
                target.setStringValue(param);
            }
        }
        
        /**
         * Sets (as xml) the "param" attribute
         */
        public void xsetParam(org.apache.xmlbeans.XmlNCName param)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(PARAM$18);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlNCName)get_store().add_attribute_user(PARAM$18);
                }
                target.set(param);
            }
        }
        
        /**
         * Unsets the "param" attribute
         */
        public void unsetParam()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(PARAM$18);
            }
        }
        
        /**
         * Gets the "shortcut" attribute
         */
        public String getShortcut()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SHORTCUT$20);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "shortcut" attribute
         */
        public org.apache.xmlbeans.XmlNCName xgetShortcut()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(SHORTCUT$20);
                return target;
            }
        }
        
        /**
         * True if has "shortcut" attribute
         */
        public boolean isSetShortcut()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(SHORTCUT$20) != null;
            }
        }
        
        /**
         * Sets the "shortcut" attribute
         */
        public void setShortcut(String shortcut)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SHORTCUT$20);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(SHORTCUT$20);
                }
                target.setStringValue(shortcut);
            }
        }
        
        /**
         * Sets (as xml) the "shortcut" attribute
         */
        public void xsetShortcut(org.apache.xmlbeans.XmlNCName shortcut)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(SHORTCUT$20);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlNCName)get_store().add_attribute_user(SHORTCUT$20);
                }
                target.set(shortcut);
            }
        }
        
        /**
         * Unsets the "shortcut" attribute
         */
        public void unsetShortcut()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(SHORTCUT$20);
            }
        }
        
        /**
         * Gets the "supress-print" attribute
         */
        public String getSupressPrint()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SUPRESSPRINT$22);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "supress-print" attribute
         */
        public org.apache.xmlbeans.XmlNCName xgetSupressPrint()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(SUPRESSPRINT$22);
                return target;
            }
        }
        
        /**
         * True if has "supress-print" attribute
         */
        public boolean isSetSupressPrint()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(SUPRESSPRINT$22) != null;
            }
        }
        
        /**
         * Sets the "supress-print" attribute
         */
        public void setSupressPrint(String supressPrint)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SUPRESSPRINT$22);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(SUPRESSPRINT$22);
                }
                target.setStringValue(supressPrint);
            }
        }
        
        /**
         * Sets (as xml) the "supress-print" attribute
         */
        public void xsetSupressPrint(org.apache.xmlbeans.XmlNCName supressPrint)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(SUPRESSPRINT$22);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlNCName)get_store().add_attribute_user(SUPRESSPRINT$22);
                }
                target.set(supressPrint);
            }
        }
        
        /**
         * Unsets the "supress-print" attribute
         */
        public void unsetSupressPrint()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(SUPRESSPRINT$22);
            }
        }
        
        /**
         * Gets the "visible" attribute
         */
        public String getVisible()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VISIBLE$24);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "visible" attribute
         */
        public org.apache.xmlbeans.XmlNCName xgetVisible()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(VISIBLE$24);
                return target;
            }
        }
        
        /**
         * True if has "visible" attribute
         */
        public boolean isSetVisible()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(VISIBLE$24) != null;
            }
        }
        
        /**
         * Sets the "visible" attribute
         */
        public void setVisible(String visible)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VISIBLE$24);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(VISIBLE$24);
                }
                target.setStringValue(visible);
            }
        }
        
        /**
         * Sets (as xml) the "visible" attribute
         */
        public void xsetVisible(org.apache.xmlbeans.XmlNCName visible)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(VISIBLE$24);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlNCName)get_store().add_attribute_user(VISIBLE$24);
                }
                target.set(visible);
            }
        }
        
        /**
         * Unsets the "visible" attribute
         */
        public void unsetVisible()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(VISIBLE$24);
            }
        }
    }
}

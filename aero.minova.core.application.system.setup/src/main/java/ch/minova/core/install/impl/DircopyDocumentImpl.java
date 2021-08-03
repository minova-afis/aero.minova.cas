/*
 * An XML document type.
 * Localname: dircopy
 * Namespace: 
 * Java type: ch.minova.core.install.DircopyDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one dircopy(@) element.
 *
 * This is a complex type.
 */
public class DircopyDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.DircopyDocument
{
    private static final long serialVersionUID = 1L;
    
    public DircopyDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DIRCOPY$0 = 
        new javax.xml.namespace.QName("", "dircopy");
    
    
    /**
     * Gets the "dircopy" element
     */
    public Dircopy getDircopy()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Dircopy target = null;
            target = (Dircopy)get_store().find_element_user(DIRCOPY$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "dircopy" element
     */
    public void setDircopy(Dircopy dircopy)
    {
        synchronized (monitor())
        {
            check_orphaned();
            Dircopy target = null;
            target = (Dircopy)get_store().find_element_user(DIRCOPY$0, 0);
            if (target == null)
            {
                target = (Dircopy)get_store().add_element_user(DIRCOPY$0);
            }
            target.set(dircopy);
        }
    }
    
    /**
     * Appends and returns a new empty "dircopy" element
     */
    public Dircopy addNewDircopy()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Dircopy target = null;
            target = (Dircopy)get_store().add_element_user(DIRCOPY$0);
            return target;
        }
    }
    /**
     * An XML dircopy(@).
     *
     * This is a complex type.
     */
    public static class DircopyImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements Dircopy
    {
        private static final long serialVersionUID = 1L;
        
        public DircopyImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName FROMDIR$0 = 
            new javax.xml.namespace.QName("", "fromdir");
        private static final javax.xml.namespace.QName TODIR$2 = 
            new javax.xml.namespace.QName("", "todir");
        
        
        /**
         * Gets the "fromdir" attribute
         */
        public String getFromdir()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FROMDIR$0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "fromdir" attribute
         */
        public org.apache.xmlbeans.XmlString xgetFromdir()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(FROMDIR$0);
                return target;
            }
        }
        
        /**
         * Sets the "fromdir" attribute
         */
        public void setFromdir(String fromdir)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FROMDIR$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(FROMDIR$0);
                }
                target.setStringValue(fromdir);
            }
        }
        
        /**
         * Sets (as xml) the "fromdir" attribute
         */
        public void xsetFromdir(org.apache.xmlbeans.XmlString fromdir)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(FROMDIR$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(FROMDIR$0);
                }
                target.set(fromdir);
            }
        }
        
        /**
         * Gets the "todir" attribute
         */
        public String getTodir()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TODIR$2);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "todir" attribute
         */
        public org.apache.xmlbeans.XmlString xgetTodir()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(TODIR$2);
                return target;
            }
        }
        
        /**
         * Sets the "todir" attribute
         */
        public void setTodir(String todir)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TODIR$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(TODIR$2);
                }
                target.setStringValue(todir);
            }
        }
        
        /**
         * Sets (as xml) the "todir" attribute
         */
        public void xsetTodir(org.apache.xmlbeans.XmlString todir)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(TODIR$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(TODIR$2);
                }
                target.set(todir);
            }
        }
    }
}

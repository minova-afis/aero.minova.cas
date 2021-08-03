/*
 * An XML document type.
 * Localname: version
 * Namespace: 
 * Java type: ch.minova.core.install.VersionDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one version(@) element.
 *
 * This is a complex type.
 */
public class VersionDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.VersionDocument
{
    private static final long serialVersionUID = 1L;
    
    public VersionDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName VERSION$0 = 
        new javax.xml.namespace.QName("", "version");
    
    
    /**
     * Gets the "version" element
     */
    public Version getVersion()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Version target = null;
            target = (Version)get_store().find_element_user(VERSION$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "version" element
     */
    public void setVersion(Version version)
    {
        synchronized (monitor())
        {
            check_orphaned();
            Version target = null;
            target = (Version)get_store().find_element_user(VERSION$0, 0);
            if (target == null)
            {
                target = (Version)get_store().add_element_user(VERSION$0);
            }
            target.set(version);
        }
    }
    
    /**
     * Appends and returns a new empty "version" element
     */
    public Version addNewVersion()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Version target = null;
            target = (Version)get_store().add_element_user(VERSION$0);
            return target;
        }
    }
    /**
     * An XML version(@).
     *
     * This is a complex type.
     */
    public static class VersionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements Version
    {
        private static final long serialVersionUID = 1L;
        
        public VersionImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName MAJOR$0 = 
            new javax.xml.namespace.QName("", "major");
        private static final javax.xml.namespace.QName MINOR$2 = 
            new javax.xml.namespace.QName("", "minor");
        private static final javax.xml.namespace.QName PATCH$4 = 
            new javax.xml.namespace.QName("", "patch");
        
        
        /**
         * Gets the "major" attribute
         */
        public java.math.BigInteger getMajor()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(MAJOR$0);
                if (target == null)
                {
                    return null;
                }
                return target.getBigIntegerValue();
            }
        }
        
        /**
         * Gets (as xml) the "major" attribute
         */
        public org.apache.xmlbeans.XmlInteger xgetMajor()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInteger target = null;
                target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(MAJOR$0);
                return target;
            }
        }
        
        /**
         * Sets the "major" attribute
         */
        public void setMajor(java.math.BigInteger major)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(MAJOR$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(MAJOR$0);
                }
                target.setBigIntegerValue(major);
            }
        }
        
        /**
         * Sets (as xml) the "major" attribute
         */
        public void xsetMajor(org.apache.xmlbeans.XmlInteger major)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInteger target = null;
                target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(MAJOR$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInteger)get_store().add_attribute_user(MAJOR$0);
                }
                target.set(major);
            }
        }
        
        /**
         * Gets the "minor" attribute
         */
        public java.math.BigInteger getMinor()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(MINOR$2);
                if (target == null)
                {
                    return null;
                }
                return target.getBigIntegerValue();
            }
        }
        
        /**
         * Gets (as xml) the "minor" attribute
         */
        public org.apache.xmlbeans.XmlInteger xgetMinor()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInteger target = null;
                target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(MINOR$2);
                return target;
            }
        }
        
        /**
         * Sets the "minor" attribute
         */
        public void setMinor(java.math.BigInteger minor)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(MINOR$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(MINOR$2);
                }
                target.setBigIntegerValue(minor);
            }
        }
        
        /**
         * Sets (as xml) the "minor" attribute
         */
        public void xsetMinor(org.apache.xmlbeans.XmlInteger minor)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInteger target = null;
                target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(MINOR$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInteger)get_store().add_attribute_user(MINOR$2);
                }
                target.set(minor);
            }
        }
        
        /**
         * Gets the "patch" attribute
         */
        public java.math.BigInteger getPatch()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PATCH$4);
                if (target == null)
                {
                    return null;
                }
                return target.getBigIntegerValue();
            }
        }
        
        /**
         * Gets (as xml) the "patch" attribute
         */
        public org.apache.xmlbeans.XmlInteger xgetPatch()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInteger target = null;
                target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(PATCH$4);
                return target;
            }
        }
        
        /**
         * Sets the "patch" attribute
         */
        public void setPatch(java.math.BigInteger patch)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PATCH$4);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PATCH$4);
                }
                target.setBigIntegerValue(patch);
            }
        }
        
        /**
         * Sets (as xml) the "patch" attribute
         */
        public void xsetPatch(org.apache.xmlbeans.XmlInteger patch)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInteger target = null;
                target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(PATCH$4);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInteger)get_store().add_attribute_user(PATCH$4);
                }
                target.set(patch);
            }
        }
    }
}

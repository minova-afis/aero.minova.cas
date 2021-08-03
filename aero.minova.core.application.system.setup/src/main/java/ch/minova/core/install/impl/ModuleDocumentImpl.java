/*
 * An XML document type.
 * Localname: module
 * Namespace: 
 * Java type: ch.minova.core.install.ModuleDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one module(@) element.
 *
 * This is a complex type.
 */
public class ModuleDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.ModuleDocument
{
    private static final long serialVersionUID = 1L;
    
    public ModuleDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName MODULE$0 = 
        new javax.xml.namespace.QName("", "module");
    
    
    /**
     * Gets the "module" element
     */
    public Module getModule()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Module target = null;
            target = (Module)get_store().find_element_user(MODULE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "module" element
     */
    public void setModule(Module module)
    {
        synchronized (monitor())
        {
            check_orphaned();
            Module target = null;
            target = (Module)get_store().find_element_user(MODULE$0, 0);
            if (target == null)
            {
                target = (Module)get_store().add_element_user(MODULE$0);
            }
            target.set(module);
        }
    }
    
    /**
     * Appends and returns a new empty "module" element
     */
    public Module addNewModule()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Module target = null;
            target = (Module)get_store().add_element_user(MODULE$0);
            return target;
        }
    }
    /**
     * An XML module(@).
     *
     * This is a complex type.
     */
    public static class ModuleImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements Module
    {
        private static final long serialVersionUID = 1L;
        
        public ModuleImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName VERSION$0 = 
            new javax.xml.namespace.QName("", "version");
        private static final javax.xml.namespace.QName MAJOR$2 = 
            new javax.xml.namespace.QName("", "major");
        private static final javax.xml.namespace.QName MINOR$4 = 
            new javax.xml.namespace.QName("", "minor");
        private static final javax.xml.namespace.QName PATCH$6 = 
            new javax.xml.namespace.QName("", "patch");
        private static final javax.xml.namespace.QName NAME$8 = 
            new javax.xml.namespace.QName("", "name");
        private static final javax.xml.namespace.QName BUILDNUMBER$10 = 
            new javax.xml.namespace.QName("", "buildnumber");
        
        
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
         * Gets the "major" attribute
         */
        public java.math.BigInteger getMajor()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(MAJOR$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(MAJOR$2);
                }
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
                target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(MAJOR$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInteger)get_default_attribute_value(MAJOR$2);
                }
                return target;
            }
        }
        
        /**
         * True if has "major" attribute
         */
        public boolean isSetMajor()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(MAJOR$2) != null;
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(MAJOR$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(MAJOR$2);
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
                target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(MAJOR$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInteger)get_store().add_attribute_user(MAJOR$2);
                }
                target.set(major);
            }
        }
        
        /**
         * Unsets the "major" attribute
         */
        public void unsetMajor()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(MAJOR$2);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(MINOR$4);
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
                target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(MINOR$4);
                return target;
            }
        }
        
        /**
         * True if has "minor" attribute
         */
        public boolean isSetMinor()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(MINOR$4) != null;
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(MINOR$4);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(MINOR$4);
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
                target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(MINOR$4);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInteger)get_store().add_attribute_user(MINOR$4);
                }
                target.set(minor);
            }
        }
        
        /**
         * Unsets the "minor" attribute
         */
        public void unsetMinor()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(MINOR$4);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PATCH$6);
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
                target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(PATCH$6);
                return target;
            }
        }
        
        /**
         * True if has "patch" attribute
         */
        public boolean isSetPatch()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(PATCH$6) != null;
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PATCH$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PATCH$6);
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
                target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(PATCH$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInteger)get_store().add_attribute_user(PATCH$6);
                }
                target.set(patch);
            }
        }
        
        /**
         * Unsets the "patch" attribute
         */
        public void unsetPatch()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(PATCH$6);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$8);
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
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(NAME$8);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$8);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NAME$8);
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
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(NAME$8);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlNCName)get_store().add_attribute_user(NAME$8);
                }
                target.set(name);
            }
        }
        
        /**
         * Gets the "buildnumber" attribute
         */
        public java.math.BigInteger getBuildnumber()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(BUILDNUMBER$10);
                if (target == null)
                {
                    return null;
                }
                return target.getBigIntegerValue();
            }
        }
        
        /**
         * Gets (as xml) the "buildnumber" attribute
         */
        public org.apache.xmlbeans.XmlInteger xgetBuildnumber()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInteger target = null;
                target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(BUILDNUMBER$10);
                return target;
            }
        }
        
        /**
         * True if has "buildnumber" attribute
         */
        public boolean isSetBuildnumber()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(BUILDNUMBER$10) != null;
            }
        }
        
        /**
         * Sets the "buildnumber" attribute
         */
        public void setBuildnumber(java.math.BigInteger buildnumber)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(BUILDNUMBER$10);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(BUILDNUMBER$10);
                }
                target.setBigIntegerValue(buildnumber);
            }
        }
        
        /**
         * Sets (as xml) the "buildnumber" attribute
         */
        public void xsetBuildnumber(org.apache.xmlbeans.XmlInteger buildnumber)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInteger target = null;
                target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(BUILDNUMBER$10);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInteger)get_store().add_attribute_user(BUILDNUMBER$10);
                }
                target.set(buildnumber);
            }
        }
        
        /**
         * Unsets the "buildnumber" attribute
         */
        public void unsetBuildnumber()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(BUILDNUMBER$10);
            }
        }
    }
}

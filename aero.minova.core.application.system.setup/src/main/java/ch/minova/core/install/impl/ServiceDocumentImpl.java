/*
 * An XML document type.
 * Localname: service
 * Namespace: 
 * Java type: ch.minova.core.install.ServiceDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one service(@) element.
 *
 * This is a complex type.
 */
public class ServiceDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.ServiceDocument
{
    private static final long serialVersionUID = 1L;
    
    public ServiceDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SERVICE$0 = 
        new javax.xml.namespace.QName("", "service");
    
    
    /**
     * Gets the "service" element
     */
    public Service getService()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Service target = null;
            target = (Service)get_store().find_element_user(SERVICE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "service" element
     */
    public void setService(Service service)
    {
        synchronized (monitor())
        {
            check_orphaned();
            Service target = null;
            target = (Service)get_store().find_element_user(SERVICE$0, 0);
            if (target == null)
            {
                target = (Service)get_store().add_element_user(SERVICE$0);
            }
            target.set(service);
        }
    }
    
    /**
     * Appends and returns a new empty "service" element
     */
    public Service addNewService()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Service target = null;
            target = (Service)get_store().add_element_user(SERVICE$0);
            return target;
        }
    }
    /**
     * An XML service(@).
     *
     * This is a complex type.
     */
    public static class ServiceImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements Service
    {
        private static final long serialVersionUID = 1L;
        
        public ServiceImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName WRAPPERCONF$0 = 
            new javax.xml.namespace.QName("", "wrapper-conf");
        private static final javax.xml.namespace.QName LOG4JCONF$2 = 
            new javax.xml.namespace.QName("", "log4j-conf");
        private static final javax.xml.namespace.QName VERSION$4 = 
            new javax.xml.namespace.QName("", "version");
        private static final javax.xml.namespace.QName MAJOR$6 = 
            new javax.xml.namespace.QName("", "major");
        private static final javax.xml.namespace.QName MINOR$8 = 
            new javax.xml.namespace.QName("", "minor");
        private static final javax.xml.namespace.QName NAME$10 = 
            new javax.xml.namespace.QName("", "name");
        private static final javax.xml.namespace.QName SERVICENAME$12 = 
            new javax.xml.namespace.QName("", "service-name");
        private static final javax.xml.namespace.QName PATCH$14 = 
            new javax.xml.namespace.QName("", "patch");
        private static final javax.xml.namespace.QName BUILDNUMBER$16 = 
            new javax.xml.namespace.QName("", "buildnumber");
        
        
        /**
         * Gets the "wrapper-conf" element
         */
        public ch.minova.core.install.WrapperConfDocument.WrapperConf getWrapperConf()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.WrapperConfDocument.WrapperConf target = null;
                target = (ch.minova.core.install.WrapperConfDocument.WrapperConf)get_store().find_element_user(WRAPPERCONF$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "wrapper-conf" element
         */
        public boolean isSetWrapperConf()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(WRAPPERCONF$0) != 0;
            }
        }
        
        /**
         * Sets the "wrapper-conf" element
         */
        public void setWrapperConf(ch.minova.core.install.WrapperConfDocument.WrapperConf wrapperConf)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.WrapperConfDocument.WrapperConf target = null;
                target = (ch.minova.core.install.WrapperConfDocument.WrapperConf)get_store().find_element_user(WRAPPERCONF$0, 0);
                if (target == null)
                {
                    target = (ch.minova.core.install.WrapperConfDocument.WrapperConf)get_store().add_element_user(WRAPPERCONF$0);
                }
                target.set(wrapperConf);
            }
        }
        
        /**
         * Appends and returns a new empty "wrapper-conf" element
         */
        public ch.minova.core.install.WrapperConfDocument.WrapperConf addNewWrapperConf()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.WrapperConfDocument.WrapperConf target = null;
                target = (ch.minova.core.install.WrapperConfDocument.WrapperConf)get_store().add_element_user(WRAPPERCONF$0);
                return target;
            }
        }
        
        /**
         * Unsets the "wrapper-conf" element
         */
        public void unsetWrapperConf()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(WRAPPERCONF$0, 0);
            }
        }
        
        /**
         * Gets the "log4j-conf" element
         */
        public ch.minova.core.install.Log4JConfDocument.Log4JConf getLog4JConf()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.Log4JConfDocument.Log4JConf target = null;
                target = (ch.minova.core.install.Log4JConfDocument.Log4JConf)get_store().find_element_user(LOG4JCONF$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "log4j-conf" element
         */
        public boolean isSetLog4JConf()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(LOG4JCONF$2) != 0;
            }
        }
        
        /**
         * Sets the "log4j-conf" element
         */
        public void setLog4JConf(ch.minova.core.install.Log4JConfDocument.Log4JConf log4JConf)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.Log4JConfDocument.Log4JConf target = null;
                target = (ch.minova.core.install.Log4JConfDocument.Log4JConf)get_store().find_element_user(LOG4JCONF$2, 0);
                if (target == null)
                {
                    target = (ch.minova.core.install.Log4JConfDocument.Log4JConf)get_store().add_element_user(LOG4JCONF$2);
                }
                target.set(log4JConf);
            }
        }
        
        /**
         * Appends and returns a new empty "log4j-conf" element
         */
        public ch.minova.core.install.Log4JConfDocument.Log4JConf addNewLog4JConf()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.Log4JConfDocument.Log4JConf target = null;
                target = (ch.minova.core.install.Log4JConfDocument.Log4JConf)get_store().add_element_user(LOG4JCONF$2);
                return target;
            }
        }
        
        /**
         * Unsets the "log4j-conf" element
         */
        public void unsetLog4JConf()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(LOG4JCONF$2, 0);
            }
        }
        
        /**
         * Gets array of all "version" elements
         */
        public ch.minova.core.install.VersionDocument.Version[] getVersionArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(VERSION$4, targetList);
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
                target = (ch.minova.core.install.VersionDocument.Version)get_store().find_element_user(VERSION$4, i);
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
                return get_store().count_elements(VERSION$4);
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
                arraySetterHelper(versionArray, VERSION$4);
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
                target = (ch.minova.core.install.VersionDocument.Version)get_store().find_element_user(VERSION$4, i);
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
                target = (ch.minova.core.install.VersionDocument.Version)get_store().insert_element_user(VERSION$4, i);
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
                target = (ch.minova.core.install.VersionDocument.Version)get_store().add_element_user(VERSION$4);
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
                get_store().remove_element(VERSION$4, i);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(MAJOR$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(MAJOR$6);
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
                target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(MAJOR$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInteger)get_default_attribute_value(MAJOR$6);
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
                return get_store().find_attribute_user(MAJOR$6) != null;
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(MAJOR$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(MAJOR$6);
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
                target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(MAJOR$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInteger)get_store().add_attribute_user(MAJOR$6);
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
                get_store().remove_attribute(MAJOR$6);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(MINOR$8);
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
                target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(MINOR$8);
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
                return get_store().find_attribute_user(MINOR$8) != null;
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(MINOR$8);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(MINOR$8);
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
                target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(MINOR$8);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInteger)get_store().add_attribute_user(MINOR$8);
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
                get_store().remove_attribute(MINOR$8);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$10);
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
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(NAME$10);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$10);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NAME$10);
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
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(NAME$10);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlNCName)get_store().add_attribute_user(NAME$10);
                }
                target.set(name);
            }
        }
        
        /**
         * Gets the "service-name" attribute
         */
        public String getServiceName()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SERVICENAME$12);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "service-name" attribute
         */
        public org.apache.xmlbeans.XmlNCName xgetServiceName()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(SERVICENAME$12);
                return target;
            }
        }
        
        /**
         * Sets the "service-name" attribute
         */
        public void setServiceName(String serviceName)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SERVICENAME$12);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(SERVICENAME$12);
                }
                target.setStringValue(serviceName);
            }
        }
        
        /**
         * Sets (as xml) the "service-name" attribute
         */
        public void xsetServiceName(org.apache.xmlbeans.XmlNCName serviceName)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(SERVICENAME$12);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlNCName)get_store().add_attribute_user(SERVICENAME$12);
                }
                target.set(serviceName);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PATCH$14);
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
                target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(PATCH$14);
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
                return get_store().find_attribute_user(PATCH$14) != null;
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PATCH$14);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PATCH$14);
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
                target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(PATCH$14);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInteger)get_store().add_attribute_user(PATCH$14);
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
                get_store().remove_attribute(PATCH$14);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(BUILDNUMBER$16);
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
                target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(BUILDNUMBER$16);
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
                return get_store().find_attribute_user(BUILDNUMBER$16) != null;
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(BUILDNUMBER$16);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(BUILDNUMBER$16);
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
                target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(BUILDNUMBER$16);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInteger)get_store().add_attribute_user(BUILDNUMBER$16);
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
                get_store().remove_attribute(BUILDNUMBER$16);
            }
        }
    }
}

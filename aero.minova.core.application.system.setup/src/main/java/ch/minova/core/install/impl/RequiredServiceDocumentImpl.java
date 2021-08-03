/*
 * An XML document type.
 * Localname: required-service
 * Namespace: 
 * Java type: ch.minova.core.install.RequiredServiceDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one required-service(@) element.
 *
 * This is a complex type.
 */
public class RequiredServiceDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.RequiredServiceDocument
{
    private static final long serialVersionUID = 1L;
    
    public RequiredServiceDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName REQUIREDSERVICE$0 = 
        new javax.xml.namespace.QName("", "required-service");
    
    
    /**
     * Gets the "required-service" element
     */
    public RequiredService getRequiredService()
    {
        synchronized (monitor())
        {
            check_orphaned();
            RequiredService target = null;
            target = (RequiredService)get_store().find_element_user(REQUIREDSERVICE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "required-service" element
     */
    public void setRequiredService(RequiredService requiredService)
    {
        synchronized (monitor())
        {
            check_orphaned();
            RequiredService target = null;
            target = (RequiredService)get_store().find_element_user(REQUIREDSERVICE$0, 0);
            if (target == null)
            {
                target = (RequiredService)get_store().add_element_user(REQUIREDSERVICE$0);
            }
            target.set(requiredService);
        }
    }
    
    /**
     * Appends and returns a new empty "required-service" element
     */
    public RequiredService addNewRequiredService()
    {
        synchronized (monitor())
        {
            check_orphaned();
            RequiredService target = null;
            target = (RequiredService)get_store().add_element_user(REQUIREDSERVICE$0);
            return target;
        }
    }
    /**
     * An XML required-service(@).
     *
     * This is a complex type.
     */
    public static class RequiredServiceImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements RequiredService
    {
        private static final long serialVersionUID = 1L;
        
        public RequiredServiceImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName SERVICE$0 = 
            new javax.xml.namespace.QName("", "service");
        
        
        /**
         * Gets array of all "service" elements
         */
        public ch.minova.core.install.ServiceDocument.Service[] getServiceArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(SERVICE$0, targetList);
                ch.minova.core.install.ServiceDocument.Service[] result = new ch.minova.core.install.ServiceDocument.Service[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "service" element
         */
        public ch.minova.core.install.ServiceDocument.Service getServiceArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.ServiceDocument.Service target = null;
                target = (ch.minova.core.install.ServiceDocument.Service)get_store().find_element_user(SERVICE$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "service" element
         */
        public int sizeOfServiceArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(SERVICE$0);
            }
        }
        
        /**
         * Sets array of all "service" element
         */
        public void setServiceArray(ch.minova.core.install.ServiceDocument.Service[] serviceArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(serviceArray, SERVICE$0);
            }
        }
        
        /**
         * Sets ith "service" element
         */
        public void setServiceArray(int i, ch.minova.core.install.ServiceDocument.Service service)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.ServiceDocument.Service target = null;
                target = (ch.minova.core.install.ServiceDocument.Service)get_store().find_element_user(SERVICE$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(service);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "service" element
         */
        public ch.minova.core.install.ServiceDocument.Service insertNewService(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.ServiceDocument.Service target = null;
                target = (ch.minova.core.install.ServiceDocument.Service)get_store().insert_element_user(SERVICE$0, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "service" element
         */
        public ch.minova.core.install.ServiceDocument.Service addNewService()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.ServiceDocument.Service target = null;
                target = (ch.minova.core.install.ServiceDocument.Service)get_store().add_element_user(SERVICE$0);
                return target;
            }
        }
        
        /**
         * Removes the ith "service" element
         */
        public void removeService(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(SERVICE$0, i);
            }
        }
    }
}

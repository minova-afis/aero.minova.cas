/*
 * An XML document type.
 * Localname: min-required
 * Namespace: 
 * Java type: ch.minova.core.install.MinRequiredDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one min-required(@) element.
 *
 * This is a complex type.
 */
public class MinRequiredDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.MinRequiredDocument
{
    private static final long serialVersionUID = 1L;
    
    public MinRequiredDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName MINREQUIRED$0 = 
        new javax.xml.namespace.QName("", "min-required");
    
    
    /**
     * Gets the "min-required" element
     */
    public MinRequired getMinRequired()
    {
        synchronized (monitor())
        {
            check_orphaned();
            MinRequired target = null;
            target = (MinRequired)get_store().find_element_user(MINREQUIRED$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "min-required" element
     */
    public void setMinRequired(MinRequired minRequired)
    {
        synchronized (monitor())
        {
            check_orphaned();
            MinRequired target = null;
            target = (MinRequired)get_store().find_element_user(MINREQUIRED$0, 0);
            if (target == null)
            {
                target = (MinRequired)get_store().add_element_user(MINREQUIRED$0);
            }
            target.set(minRequired);
        }
    }
    
    /**
     * Appends and returns a new empty "min-required" element
     */
    public MinRequired addNewMinRequired()
    {
        synchronized (monitor())
        {
            check_orphaned();
            MinRequired target = null;
            target = (MinRequired)get_store().add_element_user(MINREQUIRED$0);
            return target;
        }
    }
    /**
     * An XML min-required(@).
     *
     * This is a complex type.
     */
    public static class MinRequiredImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements MinRequired
    {
        private static final long serialVersionUID = 1L;
        
        public MinRequiredImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName MODULE$0 = 
            new javax.xml.namespace.QName("", "module");
        
        
        /**
         * Gets array of all "module" elements
         */
        public ch.minova.core.install.ModuleDocument.Module[] getModuleArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(MODULE$0, targetList);
                ch.minova.core.install.ModuleDocument.Module[] result = new ch.minova.core.install.ModuleDocument.Module[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "module" element
         */
        public ch.minova.core.install.ModuleDocument.Module getModuleArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.ModuleDocument.Module target = null;
                target = (ch.minova.core.install.ModuleDocument.Module)get_store().find_element_user(MODULE$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "module" element
         */
        public int sizeOfModuleArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(MODULE$0);
            }
        }
        
        /**
         * Sets array of all "module" element
         */
        public void setModuleArray(ch.minova.core.install.ModuleDocument.Module[] moduleArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(moduleArray, MODULE$0);
            }
        }
        
        /**
         * Sets ith "module" element
         */
        public void setModuleArray(int i, ch.minova.core.install.ModuleDocument.Module module)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.ModuleDocument.Module target = null;
                target = (ch.minova.core.install.ModuleDocument.Module)get_store().find_element_user(MODULE$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(module);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "module" element
         */
        public ch.minova.core.install.ModuleDocument.Module insertNewModule(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.ModuleDocument.Module target = null;
                target = (ch.minova.core.install.ModuleDocument.Module)get_store().insert_element_user(MODULE$0, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "module" element
         */
        public ch.minova.core.install.ModuleDocument.Module addNewModule()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.ModuleDocument.Module target = null;
                target = (ch.minova.core.install.ModuleDocument.Module)get_store().add_element_user(MODULE$0);
                return target;
            }
        }
        
        /**
         * Removes the ith "module" element
         */
        public void removeModule(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(MODULE$0, i);
            }
        }
    }
}

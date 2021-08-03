/*
 * An XML document type.
 * Localname: required-modules
 * Namespace: 
 * Java type: ch.minova.core.install.RequiredModulesDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one required-modules(@) element.
 *
 * This is a complex type.
 */
public class RequiredModulesDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.RequiredModulesDocument
{
    private static final long serialVersionUID = 1L;
    
    public RequiredModulesDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName REQUIREDMODULES$0 = 
        new javax.xml.namespace.QName("", "required-modules");
    
    
    /**
     * Gets the "required-modules" element
     */
    public RequiredModules getRequiredModules()
    {
        synchronized (monitor())
        {
            check_orphaned();
            RequiredModules target = null;
            target = (RequiredModules)get_store().find_element_user(REQUIREDMODULES$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "required-modules" element
     */
    public void setRequiredModules(RequiredModules requiredModules)
    {
        synchronized (monitor())
        {
            check_orphaned();
            RequiredModules target = null;
            target = (RequiredModules)get_store().find_element_user(REQUIREDMODULES$0, 0);
            if (target == null)
            {
                target = (RequiredModules)get_store().add_element_user(REQUIREDMODULES$0);
            }
            target.set(requiredModules);
        }
    }
    
    /**
     * Appends and returns a new empty "required-modules" element
     */
    public RequiredModules addNewRequiredModules()
    {
        synchronized (monitor())
        {
            check_orphaned();
            RequiredModules target = null;
            target = (RequiredModules)get_store().add_element_user(REQUIREDMODULES$0);
            return target;
        }
    }
    /**
     * An XML required-modules(@).
     *
     * This is a complex type.
     */
    public static class RequiredModulesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements RequiredModules
    {
        private static final long serialVersionUID = 1L;
        
        public RequiredModulesImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName MODULE$0 = 
            new javax.xml.namespace.QName("", "module");
        private static final javax.xml.namespace.QName ONEOF$2 = 
            new javax.xml.namespace.QName("", "one-of");
        private static final javax.xml.namespace.QName MINREQUIRED$4 = 
            new javax.xml.namespace.QName("", "min-required");
        
        
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
        
        /**
         * Gets array of all "one-of" elements
         */
        public ch.minova.core.install.OneOfDocument.OneOf[] getOneOfArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(ONEOF$2, targetList);
                ch.minova.core.install.OneOfDocument.OneOf[] result = new ch.minova.core.install.OneOfDocument.OneOf[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "one-of" element
         */
        public ch.minova.core.install.OneOfDocument.OneOf getOneOfArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.OneOfDocument.OneOf target = null;
                target = (ch.minova.core.install.OneOfDocument.OneOf)get_store().find_element_user(ONEOF$2, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "one-of" element
         */
        public int sizeOfOneOfArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(ONEOF$2);
            }
        }
        
        /**
         * Sets array of all "one-of" element
         */
        public void setOneOfArray(ch.minova.core.install.OneOfDocument.OneOf[] oneOfArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(oneOfArray, ONEOF$2);
            }
        }
        
        /**
         * Sets ith "one-of" element
         */
        public void setOneOfArray(int i, ch.minova.core.install.OneOfDocument.OneOf oneOf)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.OneOfDocument.OneOf target = null;
                target = (ch.minova.core.install.OneOfDocument.OneOf)get_store().find_element_user(ONEOF$2, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(oneOf);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "one-of" element
         */
        public ch.minova.core.install.OneOfDocument.OneOf insertNewOneOf(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.OneOfDocument.OneOf target = null;
                target = (ch.minova.core.install.OneOfDocument.OneOf)get_store().insert_element_user(ONEOF$2, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "one-of" element
         */
        public ch.minova.core.install.OneOfDocument.OneOf addNewOneOf()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.OneOfDocument.OneOf target = null;
                target = (ch.minova.core.install.OneOfDocument.OneOf)get_store().add_element_user(ONEOF$2);
                return target;
            }
        }
        
        /**
         * Removes the ith "one-of" element
         */
        public void removeOneOf(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(ONEOF$2, i);
            }
        }
        
        /**
         * Gets array of all "min-required" elements
         */
        public ch.minova.core.install.MinRequiredDocument.MinRequired[] getMinRequiredArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(MINREQUIRED$4, targetList);
                ch.minova.core.install.MinRequiredDocument.MinRequired[] result = new ch.minova.core.install.MinRequiredDocument.MinRequired[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "min-required" element
         */
        public ch.minova.core.install.MinRequiredDocument.MinRequired getMinRequiredArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.MinRequiredDocument.MinRequired target = null;
                target = (ch.minova.core.install.MinRequiredDocument.MinRequired)get_store().find_element_user(MINREQUIRED$4, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "min-required" element
         */
        public int sizeOfMinRequiredArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(MINREQUIRED$4);
            }
        }
        
        /**
         * Sets array of all "min-required" element
         */
        public void setMinRequiredArray(ch.minova.core.install.MinRequiredDocument.MinRequired[] minRequiredArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(minRequiredArray, MINREQUIRED$4);
            }
        }
        
        /**
         * Sets ith "min-required" element
         */
        public void setMinRequiredArray(int i, ch.minova.core.install.MinRequiredDocument.MinRequired minRequired)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.MinRequiredDocument.MinRequired target = null;
                target = (ch.minova.core.install.MinRequiredDocument.MinRequired)get_store().find_element_user(MINREQUIRED$4, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(minRequired);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "min-required" element
         */
        public ch.minova.core.install.MinRequiredDocument.MinRequired insertNewMinRequired(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.MinRequiredDocument.MinRequired target = null;
                target = (ch.minova.core.install.MinRequiredDocument.MinRequired)get_store().insert_element_user(MINREQUIRED$4, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "min-required" element
         */
        public ch.minova.core.install.MinRequiredDocument.MinRequired addNewMinRequired()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.MinRequiredDocument.MinRequired target = null;
                target = (ch.minova.core.install.MinRequiredDocument.MinRequired)get_store().add_element_user(MINREQUIRED$4);
                return target;
            }
        }
        
        /**
         * Removes the ith "min-required" element
         */
        public void removeMinRequired(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(MINREQUIRED$4, i);
            }
        }
    }
}

/*
 * An XML document type.
 * Localname: one-of
 * Namespace: 
 * Java type: ch.minova.core.install.OneOfDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one one-of(@) element.
 *
 * This is a complex type.
 */
public class OneOfDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.OneOfDocument
{
    private static final long serialVersionUID = 1L;
    
    public OneOfDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ONEOF$0 = 
        new javax.xml.namespace.QName("", "one-of");
    
    
    /**
     * Gets the "one-of" element
     */
    public OneOf getOneOf()
    {
        synchronized (monitor())
        {
            check_orphaned();
            OneOf target = null;
            target = (OneOf)get_store().find_element_user(ONEOF$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "one-of" element
     */
    public void setOneOf(OneOf oneOf)
    {
        synchronized (monitor())
        {
            check_orphaned();
            OneOf target = null;
            target = (OneOf)get_store().find_element_user(ONEOF$0, 0);
            if (target == null)
            {
                target = (OneOf)get_store().add_element_user(ONEOF$0);
            }
            target.set(oneOf);
        }
    }
    
    /**
     * Appends and returns a new empty "one-of" element
     */
    public OneOf addNewOneOf()
    {
        synchronized (monitor())
        {
            check_orphaned();
            OneOf target = null;
            target = (OneOf)get_store().add_element_user(ONEOF$0);
            return target;
        }
    }
    /**
     * An XML one-of(@).
     *
     * This is a complex type.
     */
    public static class OneOfImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements OneOf
    {
        private static final long serialVersionUID = 1L;
        
        public OneOfImpl(org.apache.xmlbeans.SchemaType sType)
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

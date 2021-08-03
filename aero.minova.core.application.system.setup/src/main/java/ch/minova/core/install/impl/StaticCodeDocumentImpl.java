/*
 * An XML document type.
 * Localname: static-code
 * Namespace: 
 * Java type: ch.minova.core.install.StaticCodeDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one static-code(@) element.
 *
 * This is a complex type.
 */
public class StaticCodeDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.StaticCodeDocument
{
    private static final long serialVersionUID = 1L;
    
    public StaticCodeDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName STATICCODE$0 = 
        new javax.xml.namespace.QName("", "static-code");
    
    
    /**
     * Gets the "static-code" element
     */
    public StaticCode getStaticCode()
    {
        synchronized (monitor())
        {
            check_orphaned();
            StaticCode target = null;
            target = (StaticCode)get_store().find_element_user(STATICCODE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "static-code" element
     */
    public void setStaticCode(StaticCode staticCode)
    {
        synchronized (monitor())
        {
            check_orphaned();
            StaticCode target = null;
            target = (StaticCode)get_store().find_element_user(STATICCODE$0, 0);
            if (target == null)
            {
                target = (StaticCode)get_store().add_element_user(STATICCODE$0);
            }
            target.set(staticCode);
        }
    }
    
    /**
     * Appends and returns a new empty "static-code" element
     */
    public StaticCode addNewStaticCode()
    {
        synchronized (monitor())
        {
            check_orphaned();
            StaticCode target = null;
            target = (StaticCode)get_store().add_element_user(STATICCODE$0);
            return target;
        }
    }
    /**
     * An XML static-code(@).
     *
     * This is a complex type.
     */
    public static class StaticCodeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements StaticCode
    {
        private static final long serialVersionUID = 1L;
        
        public StaticCodeImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName STATIC$0 = 
            new javax.xml.namespace.QName("", "static");
        
        
        /**
         * Gets array of all "static" elements
         */
        public ch.minova.core.install.StaticDocument.Static[] getStaticArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(STATIC$0, targetList);
                ch.minova.core.install.StaticDocument.Static[] result = new ch.minova.core.install.StaticDocument.Static[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "static" element
         */
        public ch.minova.core.install.StaticDocument.Static getStaticArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.StaticDocument.Static target = null;
                target = (ch.minova.core.install.StaticDocument.Static)get_store().find_element_user(STATIC$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "static" element
         */
        public int sizeOfStaticArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(STATIC$0);
            }
        }
        
        /**
         * Sets array of all "static" element
         */
        public void setStaticArray(ch.minova.core.install.StaticDocument.Static[] xstaticArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(xstaticArray, STATIC$0);
            }
        }
        
        /**
         * Sets ith "static" element
         */
        public void setStaticArray(int i, ch.minova.core.install.StaticDocument.Static xstatic)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.StaticDocument.Static target = null;
                target = (ch.minova.core.install.StaticDocument.Static)get_store().find_element_user(STATIC$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(xstatic);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "static" element
         */
        public ch.minova.core.install.StaticDocument.Static insertNewStatic(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.StaticDocument.Static target = null;
                target = (ch.minova.core.install.StaticDocument.Static)get_store().insert_element_user(STATIC$0, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "static" element
         */
        public ch.minova.core.install.StaticDocument.Static addNewStatic()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.StaticDocument.Static target = null;
                target = (ch.minova.core.install.StaticDocument.Static)get_store().add_element_user(STATIC$0);
                return target;
            }
        }
        
        /**
         * Removes the ith "static" element
         */
        public void removeStatic(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(STATIC$0, i);
            }
        }
    }
}

/*
 * An XML document type.
 * Localname: schema
 * Namespace: 
 * Java type: ch.minova.core.install.SchemaDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one schema(@) element.
 *
 * This is a complex type.
 */
public class SchemaDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.SchemaDocument
{
    private static final long serialVersionUID = 1L;
    
    public SchemaDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SCHEMA$0 = 
        new javax.xml.namespace.QName("", "schema");
    
    
    /**
     * Gets the "schema" element
     */
    public Schema getSchema()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Schema target = null;
            target = (Schema)get_store().find_element_user(SCHEMA$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "schema" element
     */
    public void setSchema(Schema schema)
    {
        synchronized (monitor())
        {
            check_orphaned();
            Schema target = null;
            target = (Schema)get_store().find_element_user(SCHEMA$0, 0);
            if (target == null)
            {
                target = (Schema)get_store().add_element_user(SCHEMA$0);
            }
            target.set(schema);
        }
    }
    
    /**
     * Appends and returns a new empty "schema" element
     */
    public Schema addNewSchema()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Schema target = null;
            target = (Schema)get_store().add_element_user(SCHEMA$0);
            return target;
        }
    }
    /**
     * An XML schema(@).
     *
     * This is a complex type.
     */
    public static class SchemaImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements Schema
    {
        private static final long serialVersionUID = 1L;
        
        public SchemaImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName TABLESCHEMA$0 = 
            new javax.xml.namespace.QName("", "tableschema");
        
        
        /**
         * Gets array of all "tableschema" elements
         */
        public ch.minova.core.install.TableschemaDocument.Tableschema[] getTableschemaArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(TABLESCHEMA$0, targetList);
                ch.minova.core.install.TableschemaDocument.Tableschema[] result = new ch.minova.core.install.TableschemaDocument.Tableschema[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "tableschema" element
         */
        public ch.minova.core.install.TableschemaDocument.Tableschema getTableschemaArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.TableschemaDocument.Tableschema target = null;
                target = (ch.minova.core.install.TableschemaDocument.Tableschema)get_store().find_element_user(TABLESCHEMA$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "tableschema" element
         */
        public int sizeOfTableschemaArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(TABLESCHEMA$0);
            }
        }
        
        /**
         * Sets array of all "tableschema" element
         */
        public void setTableschemaArray(ch.minova.core.install.TableschemaDocument.Tableschema[] tableschemaArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(tableschemaArray, TABLESCHEMA$0);
            }
        }
        
        /**
         * Sets ith "tableschema" element
         */
        public void setTableschemaArray(int i, ch.minova.core.install.TableschemaDocument.Tableschema tableschema)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.TableschemaDocument.Tableschema target = null;
                target = (ch.minova.core.install.TableschemaDocument.Tableschema)get_store().find_element_user(TABLESCHEMA$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(tableschema);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "tableschema" element
         */
        public ch.minova.core.install.TableschemaDocument.Tableschema insertNewTableschema(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.TableschemaDocument.Tableschema target = null;
                target = (ch.minova.core.install.TableschemaDocument.Tableschema)get_store().insert_element_user(TABLESCHEMA$0, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "tableschema" element
         */
        public ch.minova.core.install.TableschemaDocument.Tableschema addNewTableschema()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.TableschemaDocument.Tableschema target = null;
                target = (ch.minova.core.install.TableschemaDocument.Tableschema)get_store().add_element_user(TABLESCHEMA$0);
                return target;
            }
        }
        
        /**
         * Removes the ith "tableschema" element
         */
        public void removeTableschema(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(TABLESCHEMA$0, i);
            }
        }
    }
}

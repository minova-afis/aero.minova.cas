/*
 * An XML attribute type.
 * Localname: nullable
 * Namespace: 
 * Java type: ch.minova.core.xml.tables.NullableAttribute
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.xml.tables.impl;
/**
 * A document containing one nullable(@) attribute.
 *
 * This is a complex type.
 */
public class NullableAttributeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.xml.tables.NullableAttribute
{
    private static final long serialVersionUID = 1L;
    
    public NullableAttributeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NULLABLE$0 = 
        new javax.xml.namespace.QName("", "nullable");
    
    
    /**
     * Gets the "nullable" attribute
     */
    public boolean getNullable()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NULLABLE$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(NULLABLE$0);
            }
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "nullable" attribute
     */
    public org.apache.xmlbeans.XmlBoolean xgetNullable()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(NULLABLE$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(NULLABLE$0);
            }
            return target;
        }
    }
    
    /**
     * True if has "nullable" attribute
     */
    public boolean isSetNullable()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(NULLABLE$0) != null;
        }
    }
    
    /**
     * Sets the "nullable" attribute
     */
    public void setNullable(boolean nullable)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NULLABLE$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NULLABLE$0);
            }
            target.setBooleanValue(nullable);
        }
    }
    
    /**
     * Sets (as xml) the "nullable" attribute
     */
    public void xsetNullable(org.apache.xmlbeans.XmlBoolean nullable)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(NULLABLE$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(NULLABLE$0);
            }
            target.set(nullable);
        }
    }
    
    /**
     * Unsets the "nullable" attribute
     */
    public void unsetNullable()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(NULLABLE$0);
        }
    }
}

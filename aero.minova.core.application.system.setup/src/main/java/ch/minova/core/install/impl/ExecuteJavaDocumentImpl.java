/*
 * An XML document type.
 * Localname: execute-java
 * Namespace: 
 * Java type: ch.minova.core.install.ExecuteJavaDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one execute-java(@) element.
 *
 * This is a complex type.
 */
public class ExecuteJavaDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.ExecuteJavaDocument
{
    private static final long serialVersionUID = 1L;
    
    public ExecuteJavaDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName EXECUTEJAVA$0 = 
        new javax.xml.namespace.QName("", "execute-java");
    
    
    /**
     * Gets the "execute-java" element
     */
    public ExecuteJava getExecuteJava()
    {
        synchronized (monitor())
        {
            check_orphaned();
            ExecuteJava target = null;
            target = (ExecuteJava)get_store().find_element_user(EXECUTEJAVA$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "execute-java" element
     */
    public void setExecuteJava(ExecuteJava executeJava)
    {
        synchronized (monitor())
        {
            check_orphaned();
            ExecuteJava target = null;
            target = (ExecuteJava)get_store().find_element_user(EXECUTEJAVA$0, 0);
            if (target == null)
            {
                target = (ExecuteJava)get_store().add_element_user(EXECUTEJAVA$0);
            }
            target.set(executeJava);
        }
    }
    
    /**
     * Appends and returns a new empty "execute-java" element
     */
    public ExecuteJava addNewExecuteJava()
    {
        synchronized (monitor())
        {
            check_orphaned();
            ExecuteJava target = null;
            target = (ExecuteJava)get_store().add_element_user(EXECUTEJAVA$0);
            return target;
        }
    }
    /**
     * An XML execute-java(@).
     *
     * This is a complex type.
     */
    public static class ExecuteJavaImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ExecuteJava
    {
        private static final long serialVersionUID = 1L;
        
        public ExecuteJavaImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName PARAMETER$0 = 
            new javax.xml.namespace.QName("", "parameter");
        private static final javax.xml.namespace.QName CLASSNAME$2 = 
            new javax.xml.namespace.QName("", "classname");
        private static final javax.xml.namespace.QName EXECUTEAFTER$4 = 
            new javax.xml.namespace.QName("", "execute-after");
        
        
        /**
         * Gets array of all "parameter" elements
         */
        public Parameter[] getParameterArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(PARAMETER$0, targetList);
                Parameter[] result = new Parameter[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "parameter" element
         */
        public Parameter getParameterArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                Parameter target = null;
                target = (Parameter)get_store().find_element_user(PARAMETER$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "parameter" element
         */
        public int sizeOfParameterArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(PARAMETER$0);
            }
        }
        
        /**
         * Sets array of all "parameter" element
         */
        public void setParameterArray(Parameter[] parameterArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(parameterArray, PARAMETER$0);
            }
        }
        
        /**
         * Sets ith "parameter" element
         */
        public void setParameterArray(int i, Parameter parameter)
        {
            synchronized (monitor())
            {
                check_orphaned();
                Parameter target = null;
                target = (Parameter)get_store().find_element_user(PARAMETER$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(parameter);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "parameter" element
         */
        public Parameter insertNewParameter(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                Parameter target = null;
                target = (Parameter)get_store().insert_element_user(PARAMETER$0, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "parameter" element
         */
        public Parameter addNewParameter()
        {
            synchronized (monitor())
            {
                check_orphaned();
                Parameter target = null;
                target = (Parameter)get_store().add_element_user(PARAMETER$0);
                return target;
            }
        }
        
        /**
         * Removes the ith "parameter" element
         */
        public void removeParameter(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(PARAMETER$0, i);
            }
        }
        
        /**
         * Gets the "classname" attribute
         */
        public String getClassname()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CLASSNAME$2);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "classname" attribute
         */
        public org.apache.xmlbeans.XmlName xgetClassname()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlName target = null;
                target = (org.apache.xmlbeans.XmlName)get_store().find_attribute_user(CLASSNAME$2);
                return target;
            }
        }
        
        /**
         * Sets the "classname" attribute
         */
        public void setClassname(String classname)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CLASSNAME$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(CLASSNAME$2);
                }
                target.setStringValue(classname);
            }
        }
        
        /**
         * Sets (as xml) the "classname" attribute
         */
        public void xsetClassname(org.apache.xmlbeans.XmlName classname)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlName target = null;
                target = (org.apache.xmlbeans.XmlName)get_store().find_attribute_user(CLASSNAME$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlName)get_store().add_attribute_user(CLASSNAME$2);
                }
                target.set(classname);
            }
        }
        
        /**
         * Gets the "execute-after" attribute
         */
        public ExecuteAfter.Enum getExecuteAfter()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(EXECUTEAFTER$4);
                if (target == null)
                {
                    return null;
                }
                return (ExecuteAfter.Enum)target.getEnumValue();
            }
        }
        
        /**
         * Gets (as xml) the "execute-after" attribute
         */
        public ExecuteAfter xgetExecuteAfter()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ExecuteAfter target = null;
                target = (ExecuteAfter)get_store().find_attribute_user(EXECUTEAFTER$4);
                return target;
            }
        }
        
        /**
         * Sets the "execute-after" attribute
         */
        public void setExecuteAfter(ExecuteAfter.Enum executeAfter)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(EXECUTEAFTER$4);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(EXECUTEAFTER$4);
                }
                target.setEnumValue(executeAfter);
            }
        }
        
        /**
         * Sets (as xml) the "execute-after" attribute
         */
        public void xsetExecuteAfter(ExecuteAfter executeAfter)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ExecuteAfter target = null;
                target = (ExecuteAfter)get_store().find_attribute_user(EXECUTEAFTER$4);
                if (target == null)
                {
                    target = (ExecuteAfter)get_store().add_attribute_user(EXECUTEAFTER$4);
                }
                target.set(executeAfter);
            }
        }
        /**
         * An XML parameter(@).
         *
         * This is a complex type.
         */
        public static class ParameterImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements Parameter
        {
            private static final long serialVersionUID = 1L;
            
            public ParameterImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType);
            }
            
            private static final javax.xml.namespace.QName NAME$0 = 
                new javax.xml.namespace.QName("", "name");
            private static final javax.xml.namespace.QName VALUE$2 = 
                new javax.xml.namespace.QName("", "value");
            
            
            /**
             * Gets the "name" attribute
             */
            public String getName()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$0);
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
            public org.apache.xmlbeans.XmlString xgetName()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlString target = null;
                    target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$0);
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
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$0);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NAME$0);
                    }
                    target.setStringValue(name);
                }
            }
            
            /**
             * Sets (as xml) the "name" attribute
             */
            public void xsetName(org.apache.xmlbeans.XmlString name)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlString target = null;
                    target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$0);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(NAME$0);
                    }
                    target.set(name);
                }
            }
            
            /**
             * Gets the "value" attribute
             */
            public String getValue()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VALUE$2);
                    if (target == null)
                    {
                      return null;
                    }
                    return target.getStringValue();
                }
            }
            
            /**
             * Gets (as xml) the "value" attribute
             */
            public org.apache.xmlbeans.XmlString xgetValue()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlString target = null;
                    target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(VALUE$2);
                    return target;
                }
            }
            
            /**
             * Sets the "value" attribute
             */
            public void setValue(String value)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.SimpleValue target = null;
                    target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VALUE$2);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(VALUE$2);
                    }
                    target.setStringValue(value);
                }
            }
            
            /**
             * Sets (as xml) the "value" attribute
             */
            public void xsetValue(org.apache.xmlbeans.XmlString value)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    org.apache.xmlbeans.XmlString target = null;
                    target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(VALUE$2);
                    if (target == null)
                    {
                      target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(VALUE$2);
                    }
                    target.set(value);
                }
            }
        }
        /**
         * An XML execute-after(@).
         *
         * This is an atomic type that is a restriction of ch.minova.core.install.ExecuteJavaDocument$ExecuteJava$ExecuteAfter.
         */
        public static class ExecuteAfterImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements ExecuteAfter
        {
            private static final long serialVersionUID = 1L;
            
            public ExecuteAfterImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType, false);
            }
            
            protected ExecuteAfterImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
            {
                super(sType, b);
            }
        }
    }
}

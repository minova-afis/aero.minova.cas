/*
 * An XML document type.
 * Localname: node
 * Namespace: 
 * Java type: ch.minova.core.install.NodeDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one node(@) element.
 *
 * This is a complex type.
 */
public class NodeDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.NodeDocument
{
    private static final long serialVersionUID = 1L;
    
    public NodeDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NODE$0 = 
        new javax.xml.namespace.QName("", "node");
    
    
    /**
     * Gets the "node" element
     */
    public Node getNode()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Node target = null;
            target = (Node)get_store().find_element_user(NODE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "node" element
     */
    public void setNode(Node node)
    {
        synchronized (monitor())
        {
            check_orphaned();
            Node target = null;
            target = (Node)get_store().find_element_user(NODE$0, 0);
            if (target == null)
            {
                target = (Node)get_store().add_element_user(NODE$0);
            }
            target.set(node);
        }
    }
    
    /**
     * Appends and returns a new empty "node" element
     */
    public Node addNewNode()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Node target = null;
            target = (Node)get_store().add_element_user(NODE$0);
            return target;
        }
    }
    /**
     * An XML node(@).
     *
     * This is a complex type.
     */
    public static class NodeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements Node
    {
        private static final long serialVersionUID = 1L;
        
        public NodeImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName MAP$0 = 
            new javax.xml.namespace.QName("", "map");
        private static final javax.xml.namespace.QName NODE$2 = 
            new javax.xml.namespace.QName("", "node");
        private static final javax.xml.namespace.QName LANGUAGE$4 = 
            new javax.xml.namespace.QName("", "language");
        private static final javax.xml.namespace.QName NAME$6 = 
            new javax.xml.namespace.QName("", "name");
        
        
        /**
         * Gets the "map" element
         */
        public ch.minova.core.install.Map getMap()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.Map target = null;
                target = (ch.minova.core.install.Map)get_store().find_element_user(MAP$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "map" element
         */
        public boolean isSetMap()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(MAP$0) != 0;
            }
        }
        
        /**
         * Sets the "map" element
         */
        public void setMap(ch.minova.core.install.Map map)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.Map target = null;
                target = (ch.minova.core.install.Map)get_store().find_element_user(MAP$0, 0);
                if (target == null)
                {
                    target = (ch.minova.core.install.Map)get_store().add_element_user(MAP$0);
                }
                target.set(map);
            }
        }
        
        /**
         * Appends and returns a new empty "map" element
         */
        public ch.minova.core.install.Map addNewMap()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.Map target = null;
                target = (ch.minova.core.install.Map)get_store().add_element_user(MAP$0);
                return target;
            }
        }
        
        /**
         * Unsets the "map" element
         */
        public void unsetMap()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(MAP$0, 0);
            }
        }
        
        /**
         * Gets array of all "node" elements
         */
        public Node[] getNodeArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(NODE$2, targetList);
                Node[] result = new Node[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "node" element
         */
        public Node getNodeArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                Node target = null;
                target = (Node)get_store().find_element_user(NODE$2, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "node" element
         */
        public int sizeOfNodeArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(NODE$2);
            }
        }
        
        /**
         * Sets array of all "node" element
         */
        public void setNodeArray(Node[] nodeArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(nodeArray, NODE$2);
            }
        }
        
        /**
         * Sets ith "node" element
         */
        public void setNodeArray(int i, Node node)
        {
            synchronized (monitor())
            {
                check_orphaned();
                Node target = null;
                target = (Node)get_store().find_element_user(NODE$2, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(node);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "node" element
         */
        public Node insertNewNode(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                Node target = null;
                target = (Node)get_store().insert_element_user(NODE$2, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "node" element
         */
        public Node addNewNode()
        {
            synchronized (monitor())
            {
                check_orphaned();
                Node target = null;
                target = (Node)get_store().add_element_user(NODE$2);
                return target;
            }
        }
        
        /**
         * Removes the ith "node" element
         */
        public void removeNode(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(NODE$2, i);
            }
        }
        
        /**
         * Gets the "language" attribute
         */
        public Language.Enum getLanguage()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(LANGUAGE$4);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(LANGUAGE$4);
                }
                if (target == null)
                {
                    return null;
                }
                return (Language.Enum)target.getEnumValue();
            }
        }
        
        /**
         * Gets (as xml) the "language" attribute
         */
        public Language xgetLanguage()
        {
            synchronized (monitor())
            {
                check_orphaned();
                Language target = null;
                target = (Language)get_store().find_attribute_user(LANGUAGE$4);
                if (target == null)
                {
                    target = (Language)get_default_attribute_value(LANGUAGE$4);
                }
                return target;
            }
        }
        
        /**
         * True if has "language" attribute
         */
        public boolean isSetLanguage()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(LANGUAGE$4) != null;
            }
        }
        
        /**
         * Sets the "language" attribute
         */
        public void setLanguage(Language.Enum language)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(LANGUAGE$4);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(LANGUAGE$4);
                }
                target.setEnumValue(language);
            }
        }
        
        /**
         * Sets (as xml) the "language" attribute
         */
        public void xsetLanguage(Language language)
        {
            synchronized (monitor())
            {
                check_orphaned();
                Language target = null;
                target = (Language)get_store().find_attribute_user(LANGUAGE$4);
                if (target == null)
                {
                    target = (Language)get_store().add_attribute_user(LANGUAGE$4);
                }
                target.set(language);
            }
        }
        
        /**
         * Unsets the "language" attribute
         */
        public void unsetLanguage()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(LANGUAGE$4);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$6);
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
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$6);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NAME$6);
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
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(NAME$6);
                }
                target.set(name);
            }
        }
        /**
         * An XML language(@).
         *
         * This is an atomic type that is a restriction of ch.minova.core.install.NodeDocument$Node$Language.
         */
        public static class LanguageImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements Language
        {
            private static final long serialVersionUID = 1L;
            
            public LanguageImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType, false);
            }
            
            protected LanguageImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
            {
                super(sType, b);
            }
        }
    }
}

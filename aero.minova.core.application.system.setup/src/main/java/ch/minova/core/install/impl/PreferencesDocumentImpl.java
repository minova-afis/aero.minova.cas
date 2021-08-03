/*
 * An XML document type.
 * Localname: preferences
 * Namespace: 
 * Java type: ch.minova.core.install.PreferencesDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one preferences(@) element.
 *
 * This is a complex type.
 */
public class PreferencesDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.PreferencesDocument
{
    private static final long serialVersionUID = 1L;
    
    public PreferencesDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PREFERENCES$0 = 
        new javax.xml.namespace.QName("", "preferences");
    
    
    /**
     * Gets the "preferences" element
     */
    public Preferences getPreferences()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Preferences target = null;
            target = (Preferences)get_store().find_element_user(PREFERENCES$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "preferences" element
     */
    public void setPreferences(Preferences preferences)
    {
        synchronized (monitor())
        {
            check_orphaned();
            Preferences target = null;
            target = (Preferences)get_store().find_element_user(PREFERENCES$0, 0);
            if (target == null)
            {
                target = (Preferences)get_store().add_element_user(PREFERENCES$0);
            }
            target.set(preferences);
        }
    }
    
    /**
     * Appends and returns a new empty "preferences" element
     */
    public Preferences addNewPreferences()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Preferences target = null;
            target = (Preferences)get_store().add_element_user(PREFERENCES$0);
            return target;
        }
    }
    /**
     * An XML preferences(@).
     *
     * This is a complex type.
     */
    public static class PreferencesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements Preferences
    {
        private static final long serialVersionUID = 1L;
        
        public PreferencesImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName ROOT$0 = 
            new javax.xml.namespace.QName("", "root");
        private static final javax.xml.namespace.QName NAME$2 = 
            new javax.xml.namespace.QName("", "name");
        
        
        /**
         * Gets the "root" element
         */
        public Root getRoot()
        {
            synchronized (monitor())
            {
                check_orphaned();
                Root target = null;
                target = (Root)get_store().find_element_user(ROOT$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * Sets the "root" element
         */
        public void setRoot(Root root)
        {
            synchronized (monitor())
            {
                check_orphaned();
                Root target = null;
                target = (Root)get_store().find_element_user(ROOT$0, 0);
                if (target == null)
                {
                    target = (Root)get_store().add_element_user(ROOT$0);
                }
                target.set(root);
            }
        }
        
        /**
         * Appends and returns a new empty "root" element
         */
        public Root addNewRoot()
        {
            synchronized (monitor())
            {
                check_orphaned();
                Root target = null;
                target = (Root)get_store().add_element_user(ROOT$0);
                return target;
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$2);
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
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(NAME$2);
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NAME$2);
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
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(NAME$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlNCName)get_store().add_attribute_user(NAME$2);
                }
                target.set(name);
            }
        }
        /**
         * An XML root(@).
         *
         * This is a complex type.
         */
        public static class RootImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements Root
        {
            private static final long serialVersionUID = 1L;
            
            public RootImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType);
            }
            
            private static final javax.xml.namespace.QName MAP$0 = 
                new javax.xml.namespace.QName("", "map");
            private static final javax.xml.namespace.QName NODE$2 = 
                new javax.xml.namespace.QName("", "node");
            
            
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
             * Gets array of all "node" elements
             */
            public ch.minova.core.install.NodeDocument.Node[] getNodeArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    java.util.List targetList = new java.util.ArrayList();
                    get_store().find_all_element_users(NODE$2, targetList);
                    ch.minova.core.install.NodeDocument.Node[] result = new ch.minova.core.install.NodeDocument.Node[targetList.size()];
                    targetList.toArray(result);
                    return result;
                }
            }
            
            /**
             * Gets ith "node" element
             */
            public ch.minova.core.install.NodeDocument.Node getNodeArray(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.install.NodeDocument.Node target = null;
                    target = (ch.minova.core.install.NodeDocument.Node)get_store().find_element_user(NODE$2, i);
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
            public void setNodeArray(ch.minova.core.install.NodeDocument.Node[] nodeArray)
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
            public void setNodeArray(int i, ch.minova.core.install.NodeDocument.Node node)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.install.NodeDocument.Node target = null;
                    target = (ch.minova.core.install.NodeDocument.Node)get_store().find_element_user(NODE$2, i);
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
            public ch.minova.core.install.NodeDocument.Node insertNewNode(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.install.NodeDocument.Node target = null;
                    target = (ch.minova.core.install.NodeDocument.Node)get_store().insert_element_user(NODE$2, i);
                    return target;
                }
            }
            
            /**
             * Appends and returns a new empty value (as xml) as the last "node" element
             */
            public ch.minova.core.install.NodeDocument.Node addNewNode()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.minova.core.install.NodeDocument.Node target = null;
                    target = (ch.minova.core.install.NodeDocument.Node)get_store().add_element_user(NODE$2);
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
        }
    }
}

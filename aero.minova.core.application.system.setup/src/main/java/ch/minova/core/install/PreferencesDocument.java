/*
 * An XML document type.
 * Localname: preferences
 * Namespace: 
 * Java type: ch.minova.core.install.PreferencesDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install;


/**
 * A document containing one preferences(@) element.
 *
 * This is a complex type.
 */
public interface PreferencesDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(PreferencesDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("preferences00dfdoctype");
    
    /**
     * Gets the "preferences" element
     */
    Preferences getPreferences();
    
    /**
     * Sets the "preferences" element
     */
    void setPreferences(Preferences preferences);
    
    /**
     * Appends and returns a new empty "preferences" element
     */
    Preferences addNewPreferences();
    
    /**
     * An XML preferences(@).
     *
     * This is a complex type.
     */
    public interface Preferences extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Preferences.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("preferences5553elemtype");
        
        /**
         * Gets the "root" element
         */
        Root getRoot();
        
        /**
         * Sets the "root" element
         */
        void setRoot(Root root);
        
        /**
         * Appends and returns a new empty "root" element
         */
        Root addNewRoot();
        
        /**
         * Gets the "name" attribute
         */
        String getName();
        
        /**
         * Gets (as xml) the "name" attribute
         */
        org.apache.xmlbeans.XmlNCName xgetName();
        
        /**
         * Sets the "name" attribute
         */
        void setName(String name);
        
        /**
         * Sets (as xml) the "name" attribute
         */
        void xsetName(org.apache.xmlbeans.XmlNCName name);
        
        /**
         * An XML root(@).
         *
         * This is a complex type.
         */
        public interface Root extends org.apache.xmlbeans.XmlObject
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Root.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("root0bc1elemtype");
            
            /**
             * Gets the "map" element
             */
            Map getMap();
            
            /**
             * Sets the "map" element
             */
            void setMap(Map map);
            
            /**
             * Appends and returns a new empty "map" element
             */
            Map addNewMap();
            
            /**
             * Gets array of all "node" elements
             */
            NodeDocument.Node[] getNodeArray();
            
            /**
             * Gets ith "node" element
             */
            NodeDocument.Node getNodeArray(int i);
            
            /**
             * Returns number of "node" element
             */
            int sizeOfNodeArray();
            
            /**
             * Sets array of all "node" element
             */
            void setNodeArray(NodeDocument.Node[] nodeArray);
            
            /**
             * Sets ith "node" element
             */
            void setNodeArray(int i, NodeDocument.Node node);
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "node" element
             */
            NodeDocument.Node insertNewNode(int i);
            
            /**
             * Appends and returns a new empty value (as xml) as the last "node" element
             */
            NodeDocument.Node addNewNode();
            
            /**
             * Removes the ith "node" element
             */
            void removeNode(int i);
            
            /**
             * A factory class with static methods for creating instances
             * of this type.
             */
            
            public static final class Factory
            {
                public static Root newInstance() {
                  return (Root) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static Root newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (Root) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static Preferences newInstance() {
              return (Preferences) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static Preferences newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (Preferences) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static PreferencesDocument newInstance() {
          return (PreferencesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static PreferencesDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (PreferencesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static PreferencesDocument parse(String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (PreferencesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static PreferencesDocument parse(String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (PreferencesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static PreferencesDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (PreferencesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static PreferencesDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (PreferencesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static PreferencesDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (PreferencesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static PreferencesDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (PreferencesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static PreferencesDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (PreferencesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static PreferencesDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (PreferencesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static PreferencesDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (PreferencesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static PreferencesDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (PreferencesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static PreferencesDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (PreferencesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static PreferencesDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (PreferencesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static PreferencesDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (PreferencesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static PreferencesDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (PreferencesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static PreferencesDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (PreferencesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static PreferencesDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (PreferencesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}

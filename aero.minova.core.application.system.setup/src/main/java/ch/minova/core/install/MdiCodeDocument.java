/*
 * An XML document type.
 * Localname: mdi-code
 * Namespace: 
 * Java type: ch.minova.core.install.MdiCodeDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install;


/**
 * A document containing one mdi-code(@) element.
 *
 * This is a complex type.
 */
public interface MdiCodeDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(MdiCodeDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("mdicode68a1doctype");
    
    /**
     * Gets the "mdi-code" element
     */
    MdiCode getMdiCode();
    
    /**
     * Sets the "mdi-code" element
     */
    void setMdiCode(MdiCode mdiCode);
    
    /**
     * Appends and returns a new empty "mdi-code" element
     */
    MdiCode addNewMdiCode();
    
    /**
     * An XML mdi-code(@).
     *
     * This is a complex type.
     */
    public interface MdiCode extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(MdiCode.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("mdicodee46delemtype");
        
        /**
         * Gets array of all "action" elements
         */
        ActionDocument.Action[] getActionArray();
        
        /**
         * Gets ith "action" element
         */
        ActionDocument.Action getActionArray(int i);
        
        /**
         * Returns number of "action" element
         */
        int sizeOfActionArray();
        
        /**
         * Sets array of all "action" element
         */
        void setActionArray(ActionDocument.Action[] actionArray);
        
        /**
         * Sets ith "action" element
         */
        void setActionArray(int i, ActionDocument.Action action);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "action" element
         */
        ActionDocument.Action insertNewAction(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "action" element
         */
        ActionDocument.Action addNewAction();
        
        /**
         * Removes the ith "action" element
         */
        void removeAction(int i);
        
        /**
         * Gets array of all "menu" elements
         */
        MenuDocument.Menu[] getMenuArray();
        
        /**
         * Gets ith "menu" element
         */
        MenuDocument.Menu getMenuArray(int i);
        
        /**
         * Returns number of "menu" element
         */
        int sizeOfMenuArray();
        
        /**
         * Sets array of all "menu" element
         */
        void setMenuArray(MenuDocument.Menu[] menuArray);
        
        /**
         * Sets ith "menu" element
         */
        void setMenuArray(int i, MenuDocument.Menu menu);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "menu" element
         */
        MenuDocument.Menu insertNewMenu(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "menu" element
         */
        MenuDocument.Menu addNewMenu();
        
        /**
         * Removes the ith "menu" element
         */
        void removeMenu(int i);
        
        /**
         * Gets the "toolbar" element
         */
        ToolbarDocument.Toolbar getToolbar();
        
        /**
         * True if has "toolbar" element
         */
        boolean isSetToolbar();
        
        /**
         * Sets the "toolbar" element
         */
        void setToolbar(ToolbarDocument.Toolbar toolbar);
        
        /**
         * Appends and returns a new empty "toolbar" element
         */
        ToolbarDocument.Toolbar addNewToolbar();
        
        /**
         * Unsets the "toolbar" element
         */
        void unsetToolbar();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static MdiCode newInstance() {
              return (MdiCode) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static MdiCode newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (MdiCode) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static MdiCodeDocument newInstance() {
          return (MdiCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static MdiCodeDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (MdiCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static MdiCodeDocument parse(String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (MdiCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static MdiCodeDocument parse(String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (MdiCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static MdiCodeDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (MdiCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static MdiCodeDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (MdiCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static MdiCodeDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (MdiCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static MdiCodeDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (MdiCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static MdiCodeDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (MdiCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static MdiCodeDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (MdiCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static MdiCodeDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (MdiCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static MdiCodeDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (MdiCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static MdiCodeDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (MdiCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static MdiCodeDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (MdiCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static MdiCodeDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (MdiCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static MdiCodeDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (MdiCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static MdiCodeDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (MdiCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }*/
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static MdiCodeDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (MdiCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }*/
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }*/
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }*/
        
        private Factory() { } // No instance of this class allowed
    }
}

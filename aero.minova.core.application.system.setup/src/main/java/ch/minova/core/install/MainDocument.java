/*
 * An XML document type.
 * Localname: main
 * Namespace: 
 * Java type: ch.minova.core.install.MainDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install;


/**
 * A document containing one main(@) element.
 *
 * This is a complex type.
 */
public interface MainDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(MainDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("main29b2doctype");
    
    /**
     * Gets the "main" element
     */
    Main getMain();
    
    /**
     * Sets the "main" element
     */
    void setMain(Main main);
    
    /**
     * Appends and returns a new empty "main" element
     */
    Main addNewMain();
    
    /**
     * An XML main(@).
     *
     * This is a complex type.
     */
    public interface Main extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Main.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("main35cdelemtype");
        
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
         * Gets the "menu" element
         */
        MenuDocument.Menu getMenu();
        
        /**
         * Sets the "menu" element
         */
        void setMenu(MenuDocument.Menu menu);
        
        /**
         * Appends and returns a new empty "menu" element
         */
        MenuDocument.Menu addNewMenu();
        
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
         * Gets the "icon" attribute
         */
        String getIcon();
        
        /**
         * Gets (as xml) the "icon" attribute
         */
        org.apache.xmlbeans.XmlNCName xgetIcon();
        
        /**
         * True if has "icon" attribute
         */
        boolean isSetIcon();
        
        /**
         * Sets the "icon" attribute
         */
        void setIcon(String icon);
        
        /**
         * Sets (as xml) the "icon" attribute
         */
        void xsetIcon(org.apache.xmlbeans.XmlNCName icon);
        
        /**
         * Unsets the "icon" attribute
         */
        void unsetIcon();
        
        /**
         * Gets the "titel" attribute
         */
        String getTitel();
        
        /**
         * Gets (as xml) the "titel" attribute
         */
        org.apache.xmlbeans.XmlNCName xgetTitel();
        
        /**
         * True if has "titel" attribute
         */
        boolean isSetTitel();
        
        /**
         * Sets the "titel" attribute
         */
        void setTitel(String titel);
        
        /**
         * Sets (as xml) the "titel" attribute
         */
        void xsetTitel(org.apache.xmlbeans.XmlNCName titel);
        
        /**
         * Unsets the "titel" attribute
         */
        void unsetTitel();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static Main newInstance() {
              return (Main) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static Main newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (Main) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static MainDocument newInstance() {
          return (MainDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static MainDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (MainDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static MainDocument parse(String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (MainDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static MainDocument parse(String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (MainDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static MainDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (MainDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static MainDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (MainDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static MainDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (MainDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static MainDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (MainDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static MainDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (MainDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static MainDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (MainDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static MainDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (MainDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static MainDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (MainDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static MainDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (MainDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static MainDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (MainDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static MainDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (MainDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static MainDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (MainDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static MainDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (MainDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }*/
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static MainDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (MainDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }*/
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }*/
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }*/
        
        private Factory() { } // No instance of this class allowed
    }
}

/*
 * An XML document type.
 * Localname: dircopy
 * Namespace: 
 * Java type: ch.minova.core.install.DircopyDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install;


/**
 * A document containing one dircopy(@) element.
 *
 * This is a complex type.
 */
public interface DircopyDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(DircopyDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("dircopy52e9doctype");
    
    /**
     * Gets the "dircopy" element
     */
    Dircopy getDircopy();
    
    /**
     * Sets the "dircopy" element
     */
    void setDircopy(Dircopy dircopy);
    
    /**
     * Appends and returns a new empty "dircopy" element
     */
    Dircopy addNewDircopy();
    
    /**
     * An XML dircopy(@).
     *
     * This is a complex type.
     */
    public interface Dircopy extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Dircopy.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("dircopy1f67elemtype");
        
        /**
         * Gets the "fromdir" attribute
         */
        String getFromdir();
        
        /**
         * Gets (as xml) the "fromdir" attribute
         */
        org.apache.xmlbeans.XmlString xgetFromdir();
        
        /**
         * Sets the "fromdir" attribute
         */
        void setFromdir(String fromdir);
        
        /**
         * Sets (as xml) the "fromdir" attribute
         */
        void xsetFromdir(org.apache.xmlbeans.XmlString fromdir);
        
        /**
         * Gets the "todir" attribute
         */
        String getTodir();
        
        /**
         * Gets (as xml) the "todir" attribute
         */
        org.apache.xmlbeans.XmlString xgetTodir();
        
        /**
         * Sets the "todir" attribute
         */
        void setTodir(String todir);
        
        /**
         * Sets (as xml) the "todir" attribute
         */
        void xsetTodir(org.apache.xmlbeans.XmlString todir);
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static Dircopy newInstance() {
              return (Dircopy) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static Dircopy newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (Dircopy) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static DircopyDocument newInstance() {
          return (DircopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static DircopyDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (DircopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static DircopyDocument parse(String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (DircopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static DircopyDocument parse(String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (DircopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static DircopyDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (DircopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static DircopyDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (DircopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static DircopyDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (DircopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static DircopyDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (DircopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static DircopyDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (DircopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static DircopyDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (DircopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static DircopyDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (DircopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static DircopyDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (DircopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static DircopyDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (DircopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static DircopyDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (DircopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static DircopyDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (DircopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static DircopyDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (DircopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static DircopyDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (DircopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static DircopyDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (DircopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}

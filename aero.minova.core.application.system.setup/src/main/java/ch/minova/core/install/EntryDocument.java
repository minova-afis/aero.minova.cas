/*
 * An XML document type.
 * Localname: entry
 * Namespace: 
 * Java type: ch.minova.core.install.EntryDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install;


/**
 * A document containing one entry(@) element.
 *
 * This is a complex type.
 */
public interface EntryDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(EntryDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("entry6399doctype");
    
    /**
     * Gets the "entry" element
     */
    Entry getEntry();
    
    /**
     * Sets the "entry" element
     */
    void setEntry(Entry entry);
    
    /**
     * Appends and returns a new empty "entry" element
     */
    Entry addNewEntry();
    
    /**
     * An XML entry(@).
     *
     * This is a complex type.
     */
    public interface Entry extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Entry.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("entrye887elemtype");
        
        /**
         * Gets the "id" attribute
         */
        String getId();
        
        /**
         * Gets (as xml) the "id" attribute
         */
        org.apache.xmlbeans.XmlNCName xgetId();
        
        /**
         * Sets the "id" attribute
         */
        void setId(String id);
        
        /**
         * Sets (as xml) the "id" attribute
         */
        void xsetId(org.apache.xmlbeans.XmlNCName id);
        
        /**
         * Gets the "position" attribute
         */
        float getPosition();
        
        /**
         * Gets (as xml) the "position" attribute
         */
        org.apache.xmlbeans.XmlFloat xgetPosition();
        
        /**
         * Sets the "position" attribute
         */
        void setPosition(float position);
        
        /**
         * Sets (as xml) the "position" attribute
         */
        void xsetPosition(org.apache.xmlbeans.XmlFloat position);
        
        /**
         * Gets the "separator-after" attribute
         */
        boolean getSeparatorAfter();
        
        /**
         * Gets (as xml) the "separator-after" attribute
         */
        org.apache.xmlbeans.XmlBoolean xgetSeparatorAfter();
        
        /**
         * True if has "separator-after" attribute
         */
        boolean isSetSeparatorAfter();
        
        /**
         * Sets the "separator-after" attribute
         */
        void setSeparatorAfter(boolean separatorAfter);
        
        /**
         * Sets (as xml) the "separator-after" attribute
         */
        void xsetSeparatorAfter(org.apache.xmlbeans.XmlBoolean separatorAfter);
        
        /**
         * Unsets the "separator-after" attribute
         */
        void unsetSeparatorAfter();
        
        /**
         * Gets the "separator-before" attribute
         */
        boolean getSeparatorBefore();
        
        /**
         * Gets (as xml) the "separator-before" attribute
         */
        org.apache.xmlbeans.XmlBoolean xgetSeparatorBefore();
        
        /**
         * True if has "separator-before" attribute
         */
        boolean isSetSeparatorBefore();
        
        /**
         * Sets the "separator-before" attribute
         */
        void setSeparatorBefore(boolean separatorBefore);
        
        /**
         * Sets (as xml) the "separator-before" attribute
         */
        void xsetSeparatorBefore(org.apache.xmlbeans.XmlBoolean separatorBefore);
        
        /**
         * Unsets the "separator-before" attribute
         */
        void unsetSeparatorBefore();
        
        /**
         * Gets the "type" attribute
         */
        String getType();
        
        /**
         * Gets (as xml) the "type" attribute
         */
        org.apache.xmlbeans.XmlNCName xgetType();
        
        /**
         * Sets the "type" attribute
         */
        void setType(String type);
        
        /**
         * Sets (as xml) the "type" attribute
         */
        void xsetType(org.apache.xmlbeans.XmlNCName type);
        
        /**
         * Gets the "override" attribute
         */
        String getOverride();
        
        /**
         * Gets (as xml) the "override" attribute
         */
        org.apache.xmlbeans.XmlNCName xgetOverride();
        
        /**
         * True if has "override" attribute
         */
        boolean isSetOverride();
        
        /**
         * Sets the "override" attribute
         */
        void setOverride(String override);
        
        /**
         * Sets (as xml) the "override" attribute
         */
        void xsetOverride(org.apache.xmlbeans.XmlNCName override);
        
        /**
         * Unsets the "override" attribute
         */
        void unsetOverride();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static Entry newInstance() {
              return (Entry) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static Entry newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (Entry) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static EntryDocument newInstance() {
          return (EntryDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static EntryDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (EntryDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static EntryDocument parse(String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (EntryDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static EntryDocument parse(String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (EntryDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static EntryDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (EntryDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static EntryDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (EntryDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static EntryDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (EntryDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static EntryDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (EntryDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static EntryDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (EntryDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static EntryDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (EntryDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static EntryDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (EntryDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static EntryDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (EntryDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static EntryDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (EntryDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static EntryDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (EntryDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static EntryDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (EntryDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static EntryDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (EntryDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static EntryDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (EntryDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }*/
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static EntryDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (EntryDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }*/
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }*/
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }*/
        
        private Factory() { } // No instance of this class allowed
    }
}

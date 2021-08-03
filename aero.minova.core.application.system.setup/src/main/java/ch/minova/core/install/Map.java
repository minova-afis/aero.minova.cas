/*
 * XML Type:  map
 * Namespace: 
 * Java type: ch.minova.core.install.Map
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install;


/**
 * An XML map(@).
 *
 * This is a complex type.
 */
public interface Map extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Map.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("map1653type");
    
    /**
     * Gets array of all "entry" elements
     */
    Entry[] getEntryArray();
    
    /**
     * Gets ith "entry" element
     */
    Entry getEntryArray(int i);
    
    /**
     * Returns number of "entry" element
     */
    int sizeOfEntryArray();
    
    /**
     * Sets array of all "entry" element
     */
    void setEntryArray(Entry[] entryArray);
    
    /**
     * Sets ith "entry" element
     */
    void setEntryArray(int i, Entry entry);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "entry" element
     */
    Entry insertNewEntry(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "entry" element
     */
    Entry addNewEntry();
    
    /**
     * Removes the ith "entry" element
     */
    void removeEntry(int i);
    
    /**
     * An XML entry(@).
     *
     * This is a complex type.
     */
    public interface Entry extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Entry.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("entryfec1elemtype");
        
        /**
         * Gets the "key" attribute
         */
        String getKey();
        
        /**
         * Gets (as xml) the "key" attribute
         */
        org.apache.xmlbeans.XmlString xgetKey();
        
        /**
         * Sets the "key" attribute
         */
        void setKey(String key);
        
        /**
         * Sets (as xml) the "key" attribute
         */
        void xsetKey(org.apache.xmlbeans.XmlString key);
        
        /**
         * Gets the "value" attribute
         */
        String getValue();
        
        /**
         * Gets (as xml) the "value" attribute
         */
        org.apache.xmlbeans.XmlString xgetValue();
        
        /**
         * Sets the "value" attribute
         */
        void setValue(String value);
        
        /**
         * Sets (as xml) the "value" attribute
         */
        void xsetValue(org.apache.xmlbeans.XmlString value);
        
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
        public static Map newInstance() {
          return (Map) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static Map newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (Map) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static Map parse(String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (Map) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static Map parse(String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (Map) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static Map parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (Map) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static Map parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (Map) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static Map parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (Map) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static Map parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (Map) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static Map parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (Map) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static Map parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (Map) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static Map parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (Map) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static Map parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (Map) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static Map parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (Map) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static Map parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (Map) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static Map parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (Map) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static Map parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (Map) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static Map parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (Map) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static Map parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (Map) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}

/*
 * An XML document type.
 * Localname: xbs-code
 * Namespace: 
 * Java type: ch.minova.core.install.XbsCodeDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install;


/**
 * A document containing one xbs-code(@) element.
 *
 * This is a complex type.
 */
public interface XbsCodeDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(XbsCodeDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("xbscode55eadoctype");
    
    /**
     * Gets the "xbs-code" element
     */
    XbsCode getXbsCode();
    
    /**
     * Sets the "xbs-code" element
     */
    void setXbsCode(XbsCode xbsCode);
    
    /**
     * Appends and returns a new empty "xbs-code" element
     */
    XbsCode addNewXbsCode();
    
    /**
     * An XML xbs-code(@).
     *
     * This is a complex type.
     */
    public interface XbsCode extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(XbsCode.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("xbscodeeccdelemtype");
        
        /**
         * Gets the "map" element
         */
        Map getMap();
        
        /**
         * True if has "map" element
         */
        boolean isSetMap();
        
        /**
         * Sets the "map" element
         */
        void setMap(Map map);
        
        /**
         * Appends and returns a new empty "map" element
         */
        Map addNewMap();
        
        /**
         * Unsets the "map" element
         */
        void unsetMap();
        
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
         * Gets the "language" attribute
         */
        Language.Enum getLanguage();
        
        /**
         * Gets (as xml) the "language" attribute
         */
        Language xgetLanguage();
        
        /**
         * True if has "language" attribute
         */
        boolean isSetLanguage();
        
        /**
         * Sets the "language" attribute
         */
        void setLanguage(Language.Enum language);
        
        /**
         * Sets (as xml) the "language" attribute
         */
        void xsetLanguage(Language language);
        
        /**
         * Unsets the "language" attribute
         */
        void unsetLanguage();
        
        /**
         * An XML language(@).
         *
         * This is an atomic type that is a restriction of ch.minova.core.install.XbsCodeDocument$XbsCode$Language.
         */
        public interface Language extends org.apache.xmlbeans.XmlString
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Language.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("language4245attrtype");
            
            org.apache.xmlbeans.StringEnumAbstractBase enumValue();
            void set(org.apache.xmlbeans.StringEnumAbstractBase e);
            
            static final Enum DE = Enum.forString("de");
            static final Enum FR = Enum.forString("fr");
            static final Enum EN = Enum.forString("en");
            static final Enum IT = Enum.forString("it");
            
            static final int INT_DE = Enum.INT_DE;
            static final int INT_FR = Enum.INT_FR;
            static final int INT_EN = Enum.INT_EN;
            
            /**
             * Enumeration value class for ch.minova.core.install.XbsCodeDocument$XbsCode$Language.
             * These enum values can be used as follows:
             * <pre>
             * enum.toString(); // returns the string value of the enum
             * enum.intValue(); // returns an int value, useful for switches
             * // e.g., case Enum.INT_DE
             * Enum.forString(s); // returns the enum value for a string
             * Enum.forInt(i); // returns the enum value for an int
             * </pre>
             * Enumeration objects are immutable singleton objects that
             * can be compared using == object equality. They have no
             * public constructor. See the constants defined within this
             * class for all the valid values.
             */
            static final class Enum extends org.apache.xmlbeans.StringEnumAbstractBase
            {
                /**
                 * Returns the enum value for a string, or null if none.
                 */
                public static Enum forString(String s)
                    { return (Enum)table.forString(s); }
                /**
                 * Returns the enum value corresponding to an int, or null if none.
                 */
                public static Enum forInt(int i)
                    { return (Enum)table.forInt(i); }
                
                private Enum(String s, int i)
                    { super(s, i); }
                
                static final int INT_DE = 1;
                static final int INT_FR = 2;
                static final int INT_EN = 3;
                static final int INT_IT = 4;
                static final int INT_IT_2 = 5;
                
                public static final Table table =
                    new Table
                (
                    new Enum[]
                    {
                      new Enum("de", INT_DE),
                      new Enum("fr", INT_FR),
                      new Enum("en", INT_EN),
                      new Enum("it", INT_IT),
                      new Enum("it", INT_IT_2),
                    }
                );
                private static final long serialVersionUID = 1L;
                private Object readResolve() { return forInt(intValue()); } 
            }
            
            /**
             * A factory class with static methods for creating instances
             * of this type.
             */
            
            public static final class Factory
            {
                public static Language newValue(Object obj) {
                  return (Language) type.newValue( obj ); }
                
                public static Language newInstance() {
                  return (Language) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static Language newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (Language) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static XbsCode newInstance() {
              return (XbsCode) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static XbsCode newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (XbsCode) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static XbsCodeDocument newInstance() {
          return (XbsCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static XbsCodeDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (XbsCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static XbsCodeDocument parse(String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (XbsCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static XbsCodeDocument parse(String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (XbsCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static XbsCodeDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (XbsCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static XbsCodeDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (XbsCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static XbsCodeDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (XbsCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static XbsCodeDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (XbsCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static XbsCodeDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (XbsCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static XbsCodeDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (XbsCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static XbsCodeDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (XbsCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static XbsCodeDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (XbsCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static XbsCodeDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (XbsCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static XbsCodeDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (XbsCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static XbsCodeDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (XbsCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static XbsCodeDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (XbsCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static XbsCodeDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (XbsCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static XbsCodeDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (XbsCodeDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}

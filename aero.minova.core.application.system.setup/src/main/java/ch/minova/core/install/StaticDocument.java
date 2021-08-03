/*
 * An XML document type.
 * Localname: static
 * Namespace: 
 * Java type: ch.minova.core.install.StaticDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install;


/**
 * A document containing one static(@) element.
 *
 * This is a complex type.
 */
public interface StaticDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(StaticDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("staticd827doctype");
    
    /**
     * Gets the "static" element
     */
    Static getStatic();
    
    /**
     * Sets the "static" element
     */
    void setStatic(Static xstatic);
    
    /**
     * Appends and returns a new empty "static" element
     */
    Static addNewStatic();
    
    /**
     * An XML static(@).
     *
     * This is a complex type.
     */
    public interface Static extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Static.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("staticbe6delemtype");
        
        /**
         * Gets array of all "version" elements
         */
        VersionDocument.Version[] getVersionArray();
        
        /**
         * Gets ith "version" element
         */
        VersionDocument.Version getVersionArray(int i);
        
        /**
         * Returns number of "version" element
         */
        int sizeOfVersionArray();
        
        /**
         * Sets array of all "version" element
         */
        void setVersionArray(VersionDocument.Version[] versionArray);
        
        /**
         * Sets ith "version" element
         */
        void setVersionArray(int i, VersionDocument.Version version);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "version" element
         */
        VersionDocument.Version insertNewVersion(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "version" element
         */
        VersionDocument.Version addNewVersion();
        
        /**
         * Removes the ith "version" element
         */
        void removeVersion(int i);
        
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
         * Gets the "mode" attribute
         */
        java.math.BigInteger getMode();
        
        /**
         * Gets (as xml) the "mode" attribute
         */
        org.apache.xmlbeans.XmlInteger xgetMode();
        
        /**
         * Sets the "mode" attribute
         */
        void setMode(java.math.BigInteger mode);
        
        /**
         * Sets (as xml) the "mode" attribute
         */
        void xsetMode(org.apache.xmlbeans.XmlInteger mode);
        
        /**
         * Gets the "country" attribute
         */
        Country.Enum getCountry();
        
        /**
         * Gets (as xml) the "country" attribute
         */
        Country xgetCountry();
        
        /**
         * True if has "country" attribute
         */
        boolean isSetCountry();
        
        /**
         * Sets the "country" attribute
         */
        void setCountry(Country.Enum country);
        
        /**
         * Sets (as xml) the "country" attribute
         */
        void xsetCountry(Country country);
        
        /**
         * Unsets the "country" attribute
         */
        void unsetCountry();
        
        /**
         * Gets the "lang" attribute
         */
        Lang.Enum getLang();
        
        /**
         * Gets (as xml) the "lang" attribute
         */
        Lang xgetLang();
        
        /**
         * Sets the "lang" attribute
         */
        void setLang(Lang.Enum lang);
        
        /**
         * Sets (as xml) the "lang" attribute
         */
        void xsetLang(Lang lang);
        
        /**
         * An XML country(@).
         *
         * This is an atomic type that is a restriction of ch.minova.core.install.StaticDocument$Static$Country.
         */
        public interface Country extends org.apache.xmlbeans.XmlString
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Country.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("country32ebattrtype");
            
            org.apache.xmlbeans.StringEnumAbstractBase enumValue();
            void set(org.apache.xmlbeans.StringEnumAbstractBase e);
            
            static final Enum CH = Enum.forString("ch");
            static final Enum CO = Enum.forString("co");
            
            static final int INT_CH = Enum.INT_CH;
            static final int INT_CO = Enum.INT_CO;
            
            /**
             * Enumeration value class for ch.minova.core.install.StaticDocument$Static$Country.
             * These enum values can be used as follows:
             * <pre>
             * enum.toString(); // returns the string value of the enum
             * enum.intValue(); // returns an int value, useful for switches
             * // e.g., case Enum.INT_CH
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
                
                static final int INT_CH = 1;
                static final int INT_CO = 2;
                
                public static final Table table =
                    new Table
                (
                    new Enum[]
                    {
                      new Enum("ch", INT_CH),
                      new Enum("co", INT_CO),
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
                public static Country newValue(Object obj) {
                  return (Country) type.newValue( obj ); }
                
                public static Country newInstance() {
                  return (Country) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static Country newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (Country) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * An XML lang(@).
         *
         * This is an atomic type that is a restriction of ch.minova.core.install.StaticDocument$Static$Lang.
         */
        public interface Lang extends org.apache.xmlbeans.XmlString
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Lang.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("lang42fbattrtype");
            
            org.apache.xmlbeans.StringEnumAbstractBase enumValue();
            void set(org.apache.xmlbeans.StringEnumAbstractBase e);
            
            static final Enum FR = Enum.forString("fr");
            static final Enum EN = Enum.forString("en");
            static final Enum DE = Enum.forString("de");
            static final Enum ES = Enum.forString("es");
            static final Enum IT = Enum.forString("it");
            static final Enum NL = Enum.forString("nl");
            
            static final int INT_FR = Enum.INT_FR;
            static final int INT_EN = Enum.INT_EN;
            static final int INT_DE = Enum.INT_DE;
            static final int INT_ES = Enum.INT_ES;
            static final int INT_IT = Enum.INT_IT;
            static final int INT_NL = Enum.INT_NL;
            
            /**
             * Enumeration value class for ch.minova.core.install.StaticDocument$Static$Lang.
             * These enum values can be used as follows:
             * <pre>
             * enum.toString(); // returns the string value of the enum
             * enum.intValue(); // returns an int value, useful for switches
             * // e.g., case Enum.INT_FR
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
                
                static final int INT_FR = 1;
                static final int INT_EN = 2;
                static final int INT_DE = 3;
                static final int INT_ES = 4;
                static final int INT_IT = 5;
                static final int INT_NL = 6;
                
                public static final Table table =
                    new Table
                (
                    new Enum[]
                    {
                      new Enum("fr", INT_FR),
                      new Enum("en", INT_EN),
                      new Enum("de", INT_DE),
                      new Enum("es", INT_ES),
                      new Enum("it", INT_IT),
                      new Enum("nl", INT_NL),
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
                public static Lang newValue(Object obj) {
                  return (Lang) type.newValue( obj ); }
                
                public static Lang newInstance() {
                  return (Lang) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static Lang newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (Lang) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static Static newInstance() {
              return (Static) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static Static newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (Static) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static StaticDocument newInstance() {
          return (StaticDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static StaticDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (StaticDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static StaticDocument parse(String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (StaticDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static StaticDocument parse(String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (StaticDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static StaticDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (StaticDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static StaticDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (StaticDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static StaticDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (StaticDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static StaticDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (StaticDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static StaticDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (StaticDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static StaticDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (StaticDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static StaticDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (StaticDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static StaticDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (StaticDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static StaticDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (StaticDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static StaticDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (StaticDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static StaticDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (StaticDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static StaticDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (StaticDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static StaticDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (StaticDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static StaticDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (StaticDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}

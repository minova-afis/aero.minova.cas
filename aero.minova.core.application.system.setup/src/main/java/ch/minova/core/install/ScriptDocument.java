/*
 * An XML document type.
 * Localname: script
 * Namespace: 
 * Java type: ch.minova.core.install.ScriptDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install;


/**
 * A document containing one script(@) element.
 *
 * This is a complex type.
 */
public interface ScriptDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ScriptDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("scriptda84doctype");
    
    /**
     * Gets the "script" element
     */
    Script getScript();
    
    /**
     * Sets the "script" element
     */
    void setScript(Script script);
    
    /**
     * Appends and returns a new empty "script" element
     */
    Script addNewScript();
    
    /**
     * An XML script(@).
     *
     * This is a complex type.
     */
    public interface Script extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Script.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("script170delemtype");
        
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
         * Gets the "type" attribute
         */
        Type.Enum getType();
        
        /**
         * Gets (as xml) the "type" attribute
         */
        Type xgetType();
        
        /**
         * Sets the "type" attribute
         */
        void setType(Type.Enum type);
        
        /**
         * Sets (as xml) the "type" attribute
         */
        void xsetType(Type type);
        
        /**
         * An XML type(@).
         *
         * This is an atomic type that is a restriction of ch.minova.core.install.ScriptDocument$Script$Type.
         */
        public interface Type extends org.apache.xmlbeans.XmlString
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Type.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("type5de7attrtype");
            
            org.apache.xmlbeans.StringEnumAbstractBase enumValue();
            void set(org.apache.xmlbeans.StringEnumAbstractBase e);
            
            static final Enum SCRIPT = Enum.forString("script");
            static final Enum PROCEDURE = Enum.forString("procedure");
            static final Enum VIEW = Enum.forString("view");
            static final Enum FUNCTION = Enum.forString("function");
            static final Enum TABLE = Enum.forString("table");
            
            static final int INT_SCRIPT = Enum.INT_SCRIPT;
            static final int INT_PROCEDURE = Enum.INT_PROCEDURE;
            static final int INT_VIEW = Enum.INT_VIEW;
            static final int INT_FUNCTION = Enum.INT_FUNCTION;
            static final int INT_TABLE = Enum.INT_TABLE;
            
            /**
             * Enumeration value class for ch.minova.core.install.ScriptDocument$Script$Type.
             * These enum values can be used as follows:
             * <pre>
             * enum.toString(); // returns the string value of the enum
             * enum.intValue(); // returns an int value, useful for switches
             * // e.g., case Enum.INT_SCRIPT
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
                
                static final int INT_SCRIPT = 1;
                static final int INT_PROCEDURE = 2;
                static final int INT_VIEW = 3;
                static final int INT_FUNCTION = 4;
                static final int INT_TABLE = 5;
                
                public static final Table table =
                    new Table
                (
                    new Enum[]
                    {
                      new Enum("script", INT_SCRIPT),
                      new Enum("procedure", INT_PROCEDURE),
                      new Enum("view", INT_VIEW),
                      new Enum("function", INT_FUNCTION),
                      new Enum("table", INT_TABLE),
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
                public static Type newValue(Object obj) {
                  return (Type) type.newValue( obj ); }
                
                public static Type newInstance() {
                  return (Type) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static Type newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (Type) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static Script newInstance() {
              return (Script) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static Script newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (Script) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static ScriptDocument newInstance() {
          return (ScriptDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static ScriptDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (ScriptDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static ScriptDocument parse(String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (ScriptDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static ScriptDocument parse(String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ScriptDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static ScriptDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ScriptDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static ScriptDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ScriptDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static ScriptDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ScriptDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static ScriptDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ScriptDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static ScriptDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ScriptDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static ScriptDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ScriptDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static ScriptDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ScriptDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static ScriptDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ScriptDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static ScriptDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (ScriptDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static ScriptDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ScriptDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static ScriptDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (ScriptDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static ScriptDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ScriptDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static ScriptDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (ScriptDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static ScriptDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (ScriptDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}

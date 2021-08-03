/*
 * An XML document type.
 * Localname: execute-java
 * Namespace: 
 * Java type: ch.minova.core.install.ExecuteJavaDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install;


/**
 * A document containing one execute-java(@) element.
 *
 * This is a complex type.
 */
public interface ExecuteJavaDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ExecuteJavaDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("executejava5073doctype");
    
    /**
     * Gets the "execute-java" element
     */
    ExecuteJava getExecuteJava();
    
    /**
     * Sets the "execute-java" element
     */
    void setExecuteJava(ExecuteJava executeJava);
    
    /**
     * Appends and returns a new empty "execute-java" element
     */
    ExecuteJava addNewExecuteJava();
    
    /**
     * An XML execute-java(@).
     *
     * This is a complex type.
     */
    public interface ExecuteJava extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ExecuteJava.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("executejava4fadelemtype");
        
        /**
         * Gets array of all "parameter" elements
         */
        Parameter[] getParameterArray();
        
        /**
         * Gets ith "parameter" element
         */
        Parameter getParameterArray(int i);
        
        /**
         * Returns number of "parameter" element
         */
        int sizeOfParameterArray();
        
        /**
         * Sets array of all "parameter" element
         */
        void setParameterArray(Parameter[] parameterArray);
        
        /**
         * Sets ith "parameter" element
         */
        void setParameterArray(int i, Parameter parameter);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "parameter" element
         */
        Parameter insertNewParameter(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "parameter" element
         */
        Parameter addNewParameter();
        
        /**
         * Removes the ith "parameter" element
         */
        void removeParameter(int i);
        
        /**
         * Gets the "classname" attribute
         */
        String getClassname();
        
        /**
         * Gets (as xml) the "classname" attribute
         */
        org.apache.xmlbeans.XmlName xgetClassname();
        
        /**
         * Sets the "classname" attribute
         */
        void setClassname(String classname);
        
        /**
         * Sets (as xml) the "classname" attribute
         */
        void xsetClassname(org.apache.xmlbeans.XmlName classname);
        
        /**
         * Gets the "execute-after" attribute
         */
        ExecuteAfter.Enum getExecuteAfter();
        
        /**
         * Gets (as xml) the "execute-after" attribute
         */
        ExecuteAfter xgetExecuteAfter();
        
        /**
         * Sets the "execute-after" attribute
         */
        void setExecuteAfter(ExecuteAfter.Enum executeAfter);
        
        /**
         * Sets (as xml) the "execute-after" attribute
         */
        void xsetExecuteAfter(ExecuteAfter executeAfter);
        
        /**
         * An XML parameter(@).
         *
         * This is a complex type.
         */
        public interface Parameter extends org.apache.xmlbeans.XmlObject
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Parameter.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("parameterc312elemtype");
            
            /**
             * Gets the "name" attribute
             */
            String getName();
            
            /**
             * Gets (as xml) the "name" attribute
             */
            org.apache.xmlbeans.XmlString xgetName();
            
            /**
             * Sets the "name" attribute
             */
            void setName(String name);
            
            /**
             * Sets (as xml) the "name" attribute
             */
            void xsetName(org.apache.xmlbeans.XmlString name);
            
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
                public static Parameter newInstance() {
                  return (Parameter) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static Parameter newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (Parameter) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * An XML execute-after(@).
         *
         * This is an atomic type that is a restriction of ch.minova.core.install.ExecuteJavaDocument$ExecuteJava$ExecuteAfter.
         */
        public interface ExecuteAfter extends org.apache.xmlbeans.XmlString
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ExecuteAfter.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("executeafter5599attrtype");
            
            org.apache.xmlbeans.StringEnumAbstractBase enumValue();
            void set(org.apache.xmlbeans.StringEnumAbstractBase e);
            
            static final Enum COPY_FILES = Enum.forString("copy-files");
            static final Enum WRITE_MDI = Enum.forString("write-mdi");
            static final Enum WRITE_XBS = Enum.forString("write-xbs");
            static final Enum INSTALL_SERVICE = Enum.forString("install-service");
            static final Enum UPDATE_DATABASE = Enum.forString("update-database");
            static final Enum UPDATE_SCHEMA = Enum.forString("update-schema");
            
            static final int INT_COPY_FILES = Enum.INT_COPY_FILES;
            static final int INT_WRITE_MDI = Enum.INT_WRITE_MDI;
            static final int INT_WRITE_XBS = Enum.INT_WRITE_XBS;
            static final int INT_INSTALL_SERVICE = Enum.INT_INSTALL_SERVICE;
            static final int INT_UPDATE_DATABASE = Enum.INT_UPDATE_DATABASE;
            static final int INT_UPDATE_SCHEMA = Enum.INT_UPDATE_SCHEMA;
            
            /**
             * Enumeration value class for ch.minova.core.install.ExecuteJavaDocument$ExecuteJava$ExecuteAfter.
             * These enum values can be used as follows:
             * <pre>
             * enum.toString(); // returns the string value of the enum
             * enum.intValue(); // returns an int value, useful for switches
             * // e.g., case Enum.INT_COPY_FILES
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
                
                static final int INT_COPY_FILES = 1;
                static final int INT_WRITE_MDI = 2;
                static final int INT_WRITE_XBS = 3;
                static final int INT_INSTALL_SERVICE = 4;
                static final int INT_UPDATE_DATABASE = 5;
                static final int INT_UPDATE_SCHEMA = 6;
                
                public static final Table table =
                    new Table
                (
                    new Enum[]
                    {
                      new Enum("copy-files", INT_COPY_FILES),
                      new Enum("write-mdi", INT_WRITE_MDI),
                      new Enum("write-xbs", INT_WRITE_XBS),
                      new Enum("install-service", INT_INSTALL_SERVICE),
                      new Enum("update-database", INT_UPDATE_DATABASE),
                      new Enum("update-schema", INT_UPDATE_SCHEMA),
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
                public static ExecuteAfter newValue(Object obj) {
                  return (ExecuteAfter) type.newValue( obj ); }
                
                public static ExecuteAfter newInstance() {
                  return (ExecuteAfter) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static ExecuteAfter newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (ExecuteAfter) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static ExecuteJava newInstance() {
              return (ExecuteJava) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static ExecuteJava newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (ExecuteJava) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static ExecuteJavaDocument newInstance() {
          return (ExecuteJavaDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static ExecuteJavaDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (ExecuteJavaDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static ExecuteJavaDocument parse(String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (ExecuteJavaDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static ExecuteJavaDocument parse(String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ExecuteJavaDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static ExecuteJavaDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ExecuteJavaDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static ExecuteJavaDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ExecuteJavaDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static ExecuteJavaDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ExecuteJavaDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static ExecuteJavaDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ExecuteJavaDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static ExecuteJavaDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ExecuteJavaDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static ExecuteJavaDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ExecuteJavaDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static ExecuteJavaDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ExecuteJavaDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static ExecuteJavaDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ExecuteJavaDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static ExecuteJavaDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (ExecuteJavaDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static ExecuteJavaDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ExecuteJavaDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static ExecuteJavaDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (ExecuteJavaDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static ExecuteJavaDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ExecuteJavaDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static ExecuteJavaDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (ExecuteJavaDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static ExecuteJavaDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (ExecuteJavaDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}

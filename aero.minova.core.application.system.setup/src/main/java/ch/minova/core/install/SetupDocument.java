/*
 * An XML document type.
 * Localname: setup
 * Namespace: 
 * Java type: ch.minova.core.install.SetupDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install;


/**
 * A document containing one setup(@) element.
 *
 * This is a complex type.
 */
public interface SetupDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(SetupDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("setup95a4doctype");
    
    /**
     * Gets the "setup" element
     */
    Setup getSetup();
    
    /**
     * Sets the "setup" element
     */
    void setSetup(Setup setup);
    
    /**
     * Appends and returns a new empty "setup" element
     */
    Setup addNewSetup();
    
    /**
     * An XML setup(@).
     *
     * This is a complex type.
     */
    public interface Setup extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Setup.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("setup119delemtype");
        
        /**
         * Gets the "required-modules" element
         */
        RequiredModulesDocument.RequiredModules getRequiredModules();
        
        /**
         * True if has "required-modules" element
         */
        boolean isSetRequiredModules();
        
        /**
         * Sets the "required-modules" element
         */
        void setRequiredModules(RequiredModulesDocument.RequiredModules requiredModules);
        
        /**
         * Appends and returns a new empty "required-modules" element
         */
        RequiredModulesDocument.RequiredModules addNewRequiredModules();
        
        /**
         * Unsets the "required-modules" element
         */
        void unsetRequiredModules();
        
        /**
         * Gets the "required-service" element
         */
        RequiredServiceDocument.RequiredService getRequiredService();
        
        /**
         * True if has "required-service" element
         */
        boolean isSetRequiredService();
        
        /**
         * Sets the "required-service" element
         */
        void setRequiredService(RequiredServiceDocument.RequiredService requiredService);
        
        /**
         * Appends and returns a new empty "required-service" element
         */
        RequiredServiceDocument.RequiredService addNewRequiredService();
        
        /**
         * Unsets the "required-service" element
         */
        void unsetRequiredService();
        
        /**
         * Gets the "sql-code" element
         */
        SqlCodeDocument.SqlCode getSqlCode();
        
        /**
         * True if has "sql-code" element
         */
        boolean isSetSqlCode();
        
        /**
         * Sets the "sql-code" element
         */
        void setSqlCode(SqlCodeDocument.SqlCode sqlCode);
        
        /**
         * Appends and returns a new empty "sql-code" element
         */
        SqlCodeDocument.SqlCode addNewSqlCode();
        
        /**
         * Unsets the "sql-code" element
         */
        void unsetSqlCode();
        
        /**
         * Gets the "mdi-code" element
         */
        MdiCodeDocument.MdiCode getMdiCode();
        
        /**
         * True if has "mdi-code" element
         */
        boolean isSetMdiCode();
        
        /**
         * Sets the "mdi-code" element
         */
        void setMdiCode(MdiCodeDocument.MdiCode mdiCode);
        
        /**
         * Appends and returns a new empty "mdi-code" element
         */
        MdiCodeDocument.MdiCode addNewMdiCode();
        
        /**
         * Unsets the "mdi-code" element
         */
        void unsetMdiCode();
        
        /**
         * Gets the "xbs-code" element
         */
        XbsCodeDocument.XbsCode getXbsCode();
        
        /**
         * True if has "xbs-code" element
         */
        boolean isSetXbsCode();
        
        /**
         * Sets the "xbs-code" element
         */
        void setXbsCode(XbsCodeDocument.XbsCode xbsCode);
        
        /**
         * Appends and returns a new empty "xbs-code" element
         */
        XbsCodeDocument.XbsCode addNewXbsCode();
        
        /**
         * Unsets the "xbs-code" element
         */
        void unsetXbsCode();
        
        /**
         * Gets the "static-code" element
         */
        StaticCodeDocument.StaticCode getStaticCode();
        
        /**
         * True if has "static-code" element
         */
        boolean isSetStaticCode();
        
        /**
         * Sets the "static-code" element
         */
        void setStaticCode(StaticCodeDocument.StaticCode staticCode);
        
        /**
         * Appends and returns a new empty "static-code" element
         */
        StaticCodeDocument.StaticCode addNewStaticCode();
        
        /**
         * Unsets the "static-code" element
         */
        void unsetStaticCode();
        
        /**
         * Gets the "copy-file" element
         */
        CopyFileDocument.CopyFile getCopyFile();
        
        /**
         * True if has "copy-file" element
         */
        boolean isSetCopyFile();
        
        /**
         * Sets the "copy-file" element
         */
        void setCopyFile(CopyFileDocument.CopyFile copyFile);
        
        /**
         * Appends and returns a new empty "copy-file" element
         */
        CopyFileDocument.CopyFile addNewCopyFile();
        
        /**
         * Unsets the "copy-file" element
         */
        void unsetCopyFile();
        
        /**
         * Gets the "schema" element
         */
        SchemaDocument.Schema getSchema();
        
        /**
         * True if has "schema" element
         */
        boolean isSetSchema();
        
        /**
         * Sets the "schema" element
         */
        void setSchema(SchemaDocument.Schema schema);
        
        /**
         * Appends and returns a new empty "schema" element
         */
        SchemaDocument.Schema addNewSchema();
        
        /**
         * Unsets the "schema" element
         */
        void unsetSchema();
        
        /**
         * Gets array of all "execute-java" elements
         */
        ExecuteJavaDocument.ExecuteJava[] getExecuteJavaArray();
        
        /**
         * Gets ith "execute-java" element
         */
        ExecuteJavaDocument.ExecuteJava getExecuteJavaArray(int i);
        
        /**
         * Returns number of "execute-java" element
         */
        int sizeOfExecuteJavaArray();
        
        /**
         * Sets array of all "execute-java" element
         */
        void setExecuteJavaArray(ExecuteJavaDocument.ExecuteJava[] executeJavaArray);
        
        /**
         * Sets ith "execute-java" element
         */
        void setExecuteJavaArray(int i, ExecuteJavaDocument.ExecuteJava executeJava);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "execute-java" element
         */
        ExecuteJavaDocument.ExecuteJava insertNewExecuteJava(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "execute-java" element
         */
        ExecuteJavaDocument.ExecuteJava addNewExecuteJava();
        
        /**
         * Removes the ith "execute-java" element
         */
        void removeExecuteJava(int i);
        
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
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static Setup newInstance() {
              return (Setup) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static Setup newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (Setup) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static SetupDocument newInstance() {
          return (SetupDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static SetupDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (SetupDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static SetupDocument parse(String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (SetupDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static SetupDocument parse(String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (SetupDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static SetupDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (SetupDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static SetupDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (SetupDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static SetupDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (SetupDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static SetupDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (SetupDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static SetupDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (SetupDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static SetupDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (SetupDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static SetupDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (SetupDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static SetupDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (SetupDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static SetupDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (SetupDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static SetupDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (SetupDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static SetupDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (SetupDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static SetupDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (SetupDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static SetupDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (SetupDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }*/
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static SetupDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (SetupDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }*/
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }*/
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }*/
        
        private Factory() { } // No instance of this class allowed
    }
}

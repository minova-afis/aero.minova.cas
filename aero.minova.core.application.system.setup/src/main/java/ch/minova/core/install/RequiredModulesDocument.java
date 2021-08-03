/*
 * An XML document type.
 * Localname: required-modules
 * Namespace: 
 * Java type: ch.minova.core.install.RequiredModulesDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install;


/**
 * A document containing one required-modules(@) element.
 *
 * This is a complex type.
 */
public interface RequiredModulesDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(RequiredModulesDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("requiredmodules1dd2doctype");
    
    /**
     * Gets the "required-modules" element
     */
    RequiredModules getRequiredModules();
    
    /**
     * Sets the "required-modules" element
     */
    void setRequiredModules(RequiredModules requiredModules);
    
    /**
     * Appends and returns a new empty "required-modules" element
     */
    RequiredModules addNewRequiredModules();
    
    /**
     * An XML required-modules(@).
     *
     * This is a complex type.
     */
    public interface RequiredModules extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(RequiredModules.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("requiredmodules1fcdelemtype");
        
        /**
         * Gets array of all "module" elements
         */
        ModuleDocument.Module[] getModuleArray();
        
        /**
         * Gets ith "module" element
         */
        ModuleDocument.Module getModuleArray(int i);
        
        /**
         * Returns number of "module" element
         */
        int sizeOfModuleArray();
        
        /**
         * Sets array of all "module" element
         */
        void setModuleArray(ModuleDocument.Module[] moduleArray);
        
        /**
         * Sets ith "module" element
         */
        void setModuleArray(int i, ModuleDocument.Module module);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "module" element
         */
        ModuleDocument.Module insertNewModule(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "module" element
         */
        ModuleDocument.Module addNewModule();
        
        /**
         * Removes the ith "module" element
         */
        void removeModule(int i);
        
        /**
         * Gets array of all "one-of" elements
         */
        OneOfDocument.OneOf[] getOneOfArray();
        
        /**
         * Gets ith "one-of" element
         */
        OneOfDocument.OneOf getOneOfArray(int i);
        
        /**
         * Returns number of "one-of" element
         */
        int sizeOfOneOfArray();
        
        /**
         * Sets array of all "one-of" element
         */
        void setOneOfArray(OneOfDocument.OneOf[] oneOfArray);
        
        /**
         * Sets ith "one-of" element
         */
        void setOneOfArray(int i, OneOfDocument.OneOf oneOf);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "one-of" element
         */
        OneOfDocument.OneOf insertNewOneOf(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "one-of" element
         */
        OneOfDocument.OneOf addNewOneOf();
        
        /**
         * Removes the ith "one-of" element
         */
        void removeOneOf(int i);
        
        /**
         * Gets array of all "min-required" elements
         */
        MinRequiredDocument.MinRequired[] getMinRequiredArray();
        
        /**
         * Gets ith "min-required" element
         */
        MinRequiredDocument.MinRequired getMinRequiredArray(int i);
        
        /**
         * Returns number of "min-required" element
         */
        int sizeOfMinRequiredArray();
        
        /**
         * Sets array of all "min-required" element
         */
        void setMinRequiredArray(MinRequiredDocument.MinRequired[] minRequiredArray);
        
        /**
         * Sets ith "min-required" element
         */
        void setMinRequiredArray(int i, MinRequiredDocument.MinRequired minRequired);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "min-required" element
         */
        MinRequiredDocument.MinRequired insertNewMinRequired(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "min-required" element
         */
        MinRequiredDocument.MinRequired addNewMinRequired();
        
        /**
         * Removes the ith "min-required" element
         */
        void removeMinRequired(int i);
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static RequiredModules newInstance() {
              return (RequiredModules) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static RequiredModules newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (RequiredModules) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static RequiredModulesDocument newInstance() {
          return (RequiredModulesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static RequiredModulesDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (RequiredModulesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static RequiredModulesDocument parse(String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (RequiredModulesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static RequiredModulesDocument parse(String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (RequiredModulesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static RequiredModulesDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (RequiredModulesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static RequiredModulesDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (RequiredModulesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static RequiredModulesDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (RequiredModulesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static RequiredModulesDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (RequiredModulesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static RequiredModulesDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (RequiredModulesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static RequiredModulesDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (RequiredModulesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static RequiredModulesDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (RequiredModulesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static RequiredModulesDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (RequiredModulesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static RequiredModulesDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (RequiredModulesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static RequiredModulesDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (RequiredModulesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static RequiredModulesDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (RequiredModulesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static RequiredModulesDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (RequiredModulesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static RequiredModulesDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (RequiredModulesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static RequiredModulesDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (RequiredModulesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}

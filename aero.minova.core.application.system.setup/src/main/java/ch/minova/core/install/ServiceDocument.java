/*
 * An XML document type.
 * Localname: service
 * Namespace: 
 * Java type: ch.minova.core.install.ServiceDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install;


/**
 * A document containing one service(@) element.
 *
 * This is a complex type.
 */
public interface ServiceDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ServiceDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("servicef41cdoctype");
    
    /**
     * Gets the "service" element
     */
    Service getService();
    
    /**
     * Sets the "service" element
     */
    void setService(Service service);
    
    /**
     * Appends and returns a new empty "service" element
     */
    Service addNewService();
    
    /**
     * An XML service(@).
     *
     * This is a complex type.
     */
    public interface Service extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Service.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("servicebe0delemtype");
        
        /**
         * Gets the "wrapper-conf" element
         */
        WrapperConfDocument.WrapperConf getWrapperConf();
        
        /**
         * True if has "wrapper-conf" element
         */
        boolean isSetWrapperConf();
        
        /**
         * Sets the "wrapper-conf" element
         */
        void setWrapperConf(WrapperConfDocument.WrapperConf wrapperConf);
        
        /**
         * Appends and returns a new empty "wrapper-conf" element
         */
        WrapperConfDocument.WrapperConf addNewWrapperConf();
        
        /**
         * Unsets the "wrapper-conf" element
         */
        void unsetWrapperConf();
        
        /**
         * Gets the "log4j-conf" element
         */
        Log4JConfDocument.Log4JConf getLog4JConf();
        
        /**
         * True if has "log4j-conf" element
         */
        boolean isSetLog4JConf();
        
        /**
         * Sets the "log4j-conf" element
         */
        void setLog4JConf(Log4JConfDocument.Log4JConf log4JConf);
        
        /**
         * Appends and returns a new empty "log4j-conf" element
         */
        Log4JConfDocument.Log4JConf addNewLog4JConf();
        
        /**
         * Unsets the "log4j-conf" element
         */
        void unsetLog4JConf();
        
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
         * Gets the "major" attribute
         */
        java.math.BigInteger getMajor();
        
        /**
         * Gets (as xml) the "major" attribute
         */
        org.apache.xmlbeans.XmlInteger xgetMajor();
        
        /**
         * True if has "major" attribute
         */
        boolean isSetMajor();
        
        /**
         * Sets the "major" attribute
         */
        void setMajor(java.math.BigInteger major);
        
        /**
         * Sets (as xml) the "major" attribute
         */
        void xsetMajor(org.apache.xmlbeans.XmlInteger major);
        
        /**
         * Unsets the "major" attribute
         */
        void unsetMajor();
        
        /**
         * Gets the "minor" attribute
         */
        java.math.BigInteger getMinor();
        
        /**
         * Gets (as xml) the "minor" attribute
         */
        org.apache.xmlbeans.XmlInteger xgetMinor();
        
        /**
         * True if has "minor" attribute
         */
        boolean isSetMinor();
        
        /**
         * Sets the "minor" attribute
         */
        void setMinor(java.math.BigInteger minor);
        
        /**
         * Sets (as xml) the "minor" attribute
         */
        void xsetMinor(org.apache.xmlbeans.XmlInteger minor);
        
        /**
         * Unsets the "minor" attribute
         */
        void unsetMinor();
        
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
         * Gets the "service-name" attribute
         */
        String getServiceName();
        
        /**
         * Gets (as xml) the "service-name" attribute
         */
        org.apache.xmlbeans.XmlNCName xgetServiceName();
        
        /**
         * Sets the "service-name" attribute
         */
        void setServiceName(String serviceName);
        
        /**
         * Sets (as xml) the "service-name" attribute
         */
        void xsetServiceName(org.apache.xmlbeans.XmlNCName serviceName);
        
        /**
         * Gets the "patch" attribute
         */
        java.math.BigInteger getPatch();
        
        /**
         * Gets (as xml) the "patch" attribute
         */
        org.apache.xmlbeans.XmlInteger xgetPatch();
        
        /**
         * True if has "patch" attribute
         */
        boolean isSetPatch();
        
        /**
         * Sets the "patch" attribute
         */
        void setPatch(java.math.BigInteger patch);
        
        /**
         * Sets (as xml) the "patch" attribute
         */
        void xsetPatch(org.apache.xmlbeans.XmlInteger patch);
        
        /**
         * Unsets the "patch" attribute
         */
        void unsetPatch();
        
        /**
         * Gets the "buildnumber" attribute
         */
        java.math.BigInteger getBuildnumber();
        
        /**
         * Gets (as xml) the "buildnumber" attribute
         */
        org.apache.xmlbeans.XmlInteger xgetBuildnumber();
        
        /**
         * True if has "buildnumber" attribute
         */
        boolean isSetBuildnumber();
        
        /**
         * Sets the "buildnumber" attribute
         */
        void setBuildnumber(java.math.BigInteger buildnumber);
        
        /**
         * Sets (as xml) the "buildnumber" attribute
         */
        void xsetBuildnumber(org.apache.xmlbeans.XmlInteger buildnumber);
        
        /**
         * Unsets the "buildnumber" attribute
         */
        void unsetBuildnumber();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static Service newInstance() {
              return (Service) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static Service newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (Service) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static ServiceDocument newInstance() {
          return (ServiceDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static ServiceDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (ServiceDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static ServiceDocument parse(String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (ServiceDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static ServiceDocument parse(String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ServiceDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static ServiceDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ServiceDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static ServiceDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ServiceDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static ServiceDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ServiceDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static ServiceDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ServiceDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static ServiceDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ServiceDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static ServiceDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ServiceDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static ServiceDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ServiceDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static ServiceDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ServiceDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static ServiceDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (ServiceDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static ServiceDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ServiceDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static ServiceDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (ServiceDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static ServiceDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ServiceDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static ServiceDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (ServiceDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static ServiceDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (ServiceDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}

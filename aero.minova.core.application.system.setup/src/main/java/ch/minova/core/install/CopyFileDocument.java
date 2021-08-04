/*
 * An XML document type.
 * Localname: copy-file
 * Namespace: 
 * Java type: ch.minova.core.install.CopyFileDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install;


/**
 * A document containing one copy-file(@) element.
 *
 * This is a complex type.
 */
public interface CopyFileDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(CopyFileDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("copyfile387bdoctype");
    
    /**
     * Gets the "copy-file" element
     */
    CopyFile getCopyFile();
    
    /**
     * Sets the "copy-file" element
     */
    void setCopyFile(CopyFile copyFile);
    
    /**
     * Appends and returns a new empty "copy-file" element
     */
    CopyFile addNewCopyFile();
    
    /**
     * An XML copy-file(@).
     *
     * This is a complex type.
     */
    public interface CopyFile extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(CopyFile.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("copyfiled7cbelemtype");
        
        /**
         * Gets array of all "filecopy" elements
         */
        FilecopyDocument.Filecopy[] getFilecopyArray();
        
        /**
         * Gets ith "filecopy" element
         */
        FilecopyDocument.Filecopy getFilecopyArray(int i);
        
        /**
         * Returns number of "filecopy" element
         */
        int sizeOfFilecopyArray();
        
        /**
         * Sets array of all "filecopy" element
         */
        void setFilecopyArray(FilecopyDocument.Filecopy[] filecopyArray);
        
        /**
         * Sets ith "filecopy" element
         */
        void setFilecopyArray(int i, FilecopyDocument.Filecopy filecopy);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "filecopy" element
         */
        FilecopyDocument.Filecopy insertNewFilecopy(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "filecopy" element
         */
        FilecopyDocument.Filecopy addNewFilecopy();
        
        /**
         * Removes the ith "filecopy" element
         */
        void removeFilecopy(int i);
        
        /**
         * Gets array of all "dircopy" elements
         */
        DircopyDocument.Dircopy[] getDircopyArray();
        
        /**
         * Gets ith "dircopy" element
         */
        DircopyDocument.Dircopy getDircopyArray(int i);
        
        /**
         * Returns number of "dircopy" element
         */
        int sizeOfDircopyArray();
        
        /**
         * Sets array of all "dircopy" element
         */
        void setDircopyArray(DircopyDocument.Dircopy[] dircopyArray);
        
        /**
         * Sets ith "dircopy" element
         */
        void setDircopyArray(int i, DircopyDocument.Dircopy dircopy);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "dircopy" element
         */
        DircopyDocument.Dircopy insertNewDircopy(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "dircopy" element
         */
        DircopyDocument.Dircopy addNewDircopy();
        
        /**
         * Removes the ith "dircopy" element
         */
        void removeDircopy(int i);
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static CopyFile newInstance() {
              return (CopyFile) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static CopyFile newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (CopyFile) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static CopyFileDocument newInstance() {
          return (CopyFileDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static CopyFileDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (CopyFileDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static CopyFileDocument parse(String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (CopyFileDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static CopyFileDocument parse(String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (CopyFileDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static CopyFileDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (CopyFileDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static CopyFileDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (CopyFileDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static CopyFileDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (CopyFileDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static CopyFileDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (CopyFileDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static CopyFileDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (CopyFileDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static CopyFileDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (CopyFileDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static CopyFileDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (CopyFileDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static CopyFileDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (CopyFileDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static CopyFileDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (CopyFileDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static CopyFileDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (CopyFileDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static CopyFileDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (CopyFileDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static CopyFileDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (CopyFileDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static CopyFileDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (CopyFileDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }*/
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static CopyFileDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (CopyFileDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }*/
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }*/
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }*/
        
        private Factory() { } // No instance of this class allowed
    }
}

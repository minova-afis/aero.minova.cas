/*
 * An XML document type.
 * Localname: filecopy
 * Namespace: 
 * Java type: ch.minova.core.install.FilecopyDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install;


/**
 * A document containing one filecopy(@) element.
 *
 * This is a complex type.
 */
public interface FilecopyDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(FilecopyDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("filecopy066adoctype");
    
    /**
     * Gets the "filecopy" element
     */
    Filecopy getFilecopy();
    
    /**
     * Sets the "filecopy" element
     */
    void setFilecopy(Filecopy filecopy);
    
    /**
     * Appends and returns a new empty "filecopy" element
     */
    Filecopy addNewFilecopy();
    
    /**
     * An XML filecopy(@).
     *
     * This is a complex type.
     */
    public interface Filecopy extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Filecopy.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("filecopy9ccdelemtype");
        
        /**
         * Gets the "os" attribute
         */
        Os.Enum getOs();
        
        /**
         * Gets (as xml) the "os" attribute
         */
        Os xgetOs();
        
        /**
         * True if has "os" attribute
         */
        boolean isSetOs();
        
        /**
         * Sets the "os" attribute
         */
        void setOs(Os.Enum os);
        
        /**
         * Sets (as xml) the "os" attribute
         */
        void xsetOs(Os os);
        
        /**
         * Unsets the "os" attribute
         */
        void unsetOs();
        
        /**
         * Gets the "arch" attribute
         */
        Arch.Enum getArch();
        
        /**
         * Gets (as xml) the "arch" attribute
         */
        Arch xgetArch();
        
        /**
         * True if has "arch" attribute
         */
        boolean isSetArch();
        
        /**
         * Sets the "arch" attribute
         */
        void setArch(Arch.Enum arch);
        
        /**
         * Sets (as xml) the "arch" attribute
         */
        void xsetArch(Arch arch);
        
        /**
         * Unsets the "arch" attribute
         */
        void unsetArch();
        
        /**
         * Gets the "fromdir" attribute
         */
        String getFromdir();
        
        /**
         * Gets (as xml) the "fromdir" attribute
         */
        org.apache.xmlbeans.XmlString xgetFromdir();
        
        /**
         * True if has "fromdir" attribute
         */
        boolean isSetFromdir();
        
        /**
         * Sets the "fromdir" attribute
         */
        void setFromdir(String fromdir);
        
        /**
         * Sets (as xml) the "fromdir" attribute
         */
        void xsetFromdir(org.apache.xmlbeans.XmlString fromdir);
        
        /**
         * Unsets the "fromdir" attribute
         */
        void unsetFromdir();
        
        /**
         * Gets the "filename" attribute
         */
        String getFilename();
        
        /**
         * Gets (as xml) the "filename" attribute
         */
        org.apache.xmlbeans.XmlNCName xgetFilename();
        
        /**
         * Sets the "filename" attribute
         */
        void setFilename(String filename);
        
        /**
         * Sets (as xml) the "filename" attribute
         */
        void xsetFilename(org.apache.xmlbeans.XmlNCName filename);
        
        /**
         * Gets the "todir" attribute
         */
        String getTodir();
        
        /**
         * Gets (as xml) the "todir" attribute
         */
        org.apache.xmlbeans.XmlString xgetTodir();
        
        /**
         * Sets the "todir" attribute
         */
        void setTodir(String todir);
        
        /**
         * Sets (as xml) the "todir" attribute
         */
        void xsetTodir(org.apache.xmlbeans.XmlString todir);
        
        /**
         * An XML os(@).
         *
         * This is an atomic type that is a restriction of ch.minova.core.install.FilecopyDocument$Filecopy$Os.
         */
        public interface Os extends org.apache.xmlbeans.XmlString
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Os.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("os40d1attrtype");
            
            org.apache.xmlbeans.StringEnumAbstractBase enumValue();
            void set(org.apache.xmlbeans.StringEnumAbstractBase e);
            
            static final Enum WIN = Enum.forString("win");
            static final Enum LINUX = Enum.forString("linux");
            static final Enum OSX = Enum.forString("osx");
            
            static final int INT_WIN = Enum.INT_WIN;
            static final int INT_LINUX = Enum.INT_LINUX;
            static final int INT_OSX = Enum.INT_OSX;
            
            /**
             * Enumeration value class for ch.minova.core.install.FilecopyDocument$Filecopy$Os.
             * These enum values can be used as follows:
             * <pre>
             * enum.toString(); // returns the string value of the enum
             * enum.intValue(); // returns an int value, useful for switches
             * // e.g., case Enum.INT_WIN
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
                
                static final int INT_WIN = 1;
                static final int INT_LINUX = 2;
                static final int INT_OSX = 3;
                
                public static final Table table =
                    new Table
                (
                    new Enum[]
                    {
                      new Enum("win", INT_WIN),
                      new Enum("linux", INT_LINUX),
                      new Enum("osx", INT_OSX),
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
                public static Os newValue(Object obj) {
                  return (Os) type.newValue( obj ); }
                
                public static Os newInstance() {
                  return (Os) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static Os newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (Os) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * An XML arch(@).
         *
         * This is an atomic type that is a restriction of ch.minova.core.install.FilecopyDocument$Filecopy$Arch.
         */
        public interface Arch extends org.apache.xmlbeans.XmlString
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Arch.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("arch2ac3attrtype");
            
            org.apache.xmlbeans.StringEnumAbstractBase enumValue();
            void set(org.apache.xmlbeans.StringEnumAbstractBase e);
            
            static final Enum X_86 = Enum.forString("x86");
            static final Enum X_64 = Enum.forString("x64");
            
            static final int INT_X_86 = Enum.INT_X_86;
            static final int INT_X_64 = Enum.INT_X_64;
            
            /**
             * Enumeration value class for ch.minova.core.install.FilecopyDocument$Filecopy$Arch.
             * These enum values can be used as follows:
             * <pre>
             * enum.toString(); // returns the string value of the enum
             * enum.intValue(); // returns an int value, useful for switches
             * // e.g., case Enum.INT_X_86
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
                
                static final int INT_X_86 = 1;
                static final int INT_X_64 = 2;
                
                public static final Table table =
                    new Table
                (
                    new Enum[]
                    {
                      new Enum("x86", INT_X_86),
                      new Enum("x64", INT_X_64),
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
                public static Arch newValue(Object obj) {
                  return (Arch) type.newValue( obj ); }
                
                public static Arch newInstance() {
                  return (Arch) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static Arch newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (Arch) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static Filecopy newInstance() {
              return (Filecopy) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static Filecopy newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (Filecopy) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static FilecopyDocument newInstance() {
          return (FilecopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static FilecopyDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (FilecopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static FilecopyDocument parse(String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (FilecopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static FilecopyDocument parse(String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (FilecopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static FilecopyDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (FilecopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static FilecopyDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (FilecopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static FilecopyDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (FilecopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static FilecopyDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (FilecopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static FilecopyDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (FilecopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static FilecopyDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (FilecopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static FilecopyDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (FilecopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static FilecopyDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (FilecopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static FilecopyDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (FilecopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static FilecopyDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (FilecopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static FilecopyDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (FilecopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static FilecopyDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (FilecopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static FilecopyDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (FilecopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }*/
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static FilecopyDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (FilecopyDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }*/
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }*/
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }*/
        
        private Factory() { } // No instance of this class allowed
    }
}

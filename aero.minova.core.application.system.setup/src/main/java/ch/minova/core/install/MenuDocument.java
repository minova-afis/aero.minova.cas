/*
 * An XML document type.
 * Localname: menu
 * Namespace: 
 * Java type: ch.minova.core.install.MenuDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install;


/**
 * A document containing one menu(@) element.
 *
 * This is a complex type.
 */
public interface MenuDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(MenuDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("menu3958doctype");
    
    /**
     * Gets the "menu" element
     */
    Menu getMenu();
    
    /**
     * Sets the "menu" element
     */
    void setMenu(Menu menu);
    
    /**
     * Appends and returns a new empty "menu" element
     */
    Menu addNewMenu();
    
    /**
     * An XML menu(@).
     *
     * This is a complex type.
     */
    public interface Menu extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Menu.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("menu6f0delemtype");
        
        /**
         * Gets array of all "menu" elements
         */
        Menu[] getMenuArray();
        
        /**
         * Gets ith "menu" element
         */
        Menu getMenuArray(int i);
        
        /**
         * Returns number of "menu" element
         */
        int sizeOfMenuArray();
        
        /**
         * Sets array of all "menu" element
         */
        void setMenuArray(Menu[] menuArray);
        
        /**
         * Sets ith "menu" element
         */
        void setMenuArray(int i, Menu menu);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "menu" element
         */
        Menu insertNewMenu(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "menu" element
         */
        Menu addNewMenu();
        
        /**
         * Removes the ith "menu" element
         */
        void removeMenu(int i);
        
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
         * Gets the "position" attribute
         */
        float getPosition();
        
        /**
         * Gets (as xml) the "position" attribute
         */
        org.apache.xmlbeans.XmlFloat xgetPosition();
        
        /**
         * Sets the "position" attribute
         */
        void setPosition(float position);
        
        /**
         * Sets (as xml) the "position" attribute
         */
        void xsetPosition(org.apache.xmlbeans.XmlFloat position);
        
        /**
         * Gets the "id" attribute
         */
        String getId();
        
        /**
         * Gets (as xml) the "id" attribute
         */
        org.apache.xmlbeans.XmlID xgetId();
        
        /**
         * True if has "id" attribute
         */
        boolean isSetId();
        
        /**
         * Sets the "id" attribute
         */
        void setId(String id);
        
        /**
         * Sets (as xml) the "id" attribute
         */
        void xsetId(org.apache.xmlbeans.XmlID id);
        
        /**
         * Unsets the "id" attribute
         */
        void unsetId();
        
        /**
         * Gets the "text" attribute
         */
        String getText();
        
        /**
         * Gets (as xml) the "text" attribute
         */
        org.apache.xmlbeans.XmlString xgetText();
        
        /**
         * True if has "text" attribute
         */
        boolean isSetText();
        
        /**
         * Sets the "text" attribute
         */
        void setText(String text);
        
        /**
         * Sets (as xml) the "text" attribute
         */
        void xsetText(org.apache.xmlbeans.XmlString text);
        
        /**
         * Unsets the "text" attribute
         */
        void unsetText();
        
        /**
         * Gets the "override" attribute
         */
        String getOverride();
        
        /**
         * Gets (as xml) the "override" attribute
         */
        org.apache.xmlbeans.XmlNCName xgetOverride();
        
        /**
         * True if has "override" attribute
         */
        boolean isSetOverride();
        
        /**
         * Sets the "override" attribute
         */
        void setOverride(String override);
        
        /**
         * Sets (as xml) the "override" attribute
         */
        void xsetOverride(org.apache.xmlbeans.XmlNCName override);
        
        /**
         * Unsets the "override" attribute
         */
        void unsetOverride();
        
        /**
         * An XML entry(@).
         *
         * This is a complex type.
         */
        public interface Entry extends org.apache.xmlbeans.XmlObject
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Entry.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("entry097belemtype");
            
            /**
             * Gets the "id" attribute
             */
            String getId();
            
            /**
             * Gets (as xml) the "id" attribute
             */
            org.apache.xmlbeans.XmlIDREF xgetId();
            
            /**
             * True if has "id" attribute
             */
            boolean isSetId();
            
            /**
             * Sets the "id" attribute
             */
            void setId(String id);
            
            /**
             * Sets (as xml) the "id" attribute
             */
            void xsetId(org.apache.xmlbeans.XmlIDREF id);
            
            /**
             * Unsets the "id" attribute
             */
            void unsetId();
            
            /**
             * Gets the "position" attribute
             */
            float getPosition();
            
            /**
             * Gets (as xml) the "position" attribute
             */
            org.apache.xmlbeans.XmlFloat xgetPosition();
            
            /**
             * Sets the "position" attribute
             */
            void setPosition(float position);
            
            /**
             * Sets (as xml) the "position" attribute
             */
            void xsetPosition(org.apache.xmlbeans.XmlFloat position);
            
            /**
             * Gets the "override" attribute
             */
            String getOverride();
            
            /**
             * Gets (as xml) the "override" attribute
             */
            org.apache.xmlbeans.XmlNCName xgetOverride();
            
            /**
             * True if has "override" attribute
             */
            boolean isSetOverride();
            
            /**
             * Sets the "override" attribute
             */
            void setOverride(String override);
            
            /**
             * Sets (as xml) the "override" attribute
             */
            void xsetOverride(org.apache.xmlbeans.XmlNCName override);
            
            /**
             * Unsets the "override" attribute
             */
            void unsetOverride();
            
            /**
             * Gets the "separator-after" attribute
             */
            boolean getSeparatorAfter();
            
            /**
             * Gets (as xml) the "separator-after" attribute
             */
            org.apache.xmlbeans.XmlBoolean xgetSeparatorAfter();
            
            /**
             * True if has "separator-after" attribute
             */
            boolean isSetSeparatorAfter();
            
            /**
             * Sets the "separator-after" attribute
             */
            void setSeparatorAfter(boolean separatorAfter);
            
            /**
             * Sets (as xml) the "separator-after" attribute
             */
            void xsetSeparatorAfter(org.apache.xmlbeans.XmlBoolean separatorAfter);
            
            /**
             * Unsets the "separator-after" attribute
             */
            void unsetSeparatorAfter();
            
            /**
             * Gets the "separator-before" attribute
             */
            boolean getSeparatorBefore();
            
            /**
             * Gets (as xml) the "separator-before" attribute
             */
            org.apache.xmlbeans.XmlBoolean xgetSeparatorBefore();
            
            /**
             * True if has "separator-before" attribute
             */
            boolean isSetSeparatorBefore();
            
            /**
             * Sets the "separator-before" attribute
             */
            void setSeparatorBefore(boolean separatorBefore);
            
            /**
             * Sets (as xml) the "separator-before" attribute
             */
            void xsetSeparatorBefore(org.apache.xmlbeans.XmlBoolean separatorBefore);
            
            /**
             * Unsets the "separator-before" attribute
             */
            void unsetSeparatorBefore();
            
            /**
             * Gets the "type" attribute
             */
            String getType();
            
            /**
             * Gets (as xml) the "type" attribute
             */
            org.apache.xmlbeans.XmlNCName xgetType();
            
            /**
             * Sets the "type" attribute
             */
            void setType(String type);
            
            /**
             * Sets (as xml) the "type" attribute
             */
            void xsetType(org.apache.xmlbeans.XmlNCName type);
            
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
            public static Menu newInstance() {
              return (Menu) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static Menu newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (Menu) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static MenuDocument newInstance() {
          return (MenuDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static MenuDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (MenuDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static MenuDocument parse(String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (MenuDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static MenuDocument parse(String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (MenuDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static MenuDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (MenuDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static MenuDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (MenuDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static MenuDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (MenuDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static MenuDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (MenuDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static MenuDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (MenuDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static MenuDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (MenuDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static MenuDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (MenuDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static MenuDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (MenuDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static MenuDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (MenuDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static MenuDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (MenuDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static MenuDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (MenuDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static MenuDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (MenuDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static MenuDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (MenuDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static MenuDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (MenuDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}

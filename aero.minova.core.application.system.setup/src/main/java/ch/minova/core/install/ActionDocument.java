/*
 * An XML document type.
 * Localname: action
 * Namespace: 
 * Java type: ch.minova.core.install.ActionDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install;


/**
 * A document containing one action(@) element.
 *
 * This is a complex type.
 */
public interface ActionDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ActionDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("action87efdoctype");
    
    /**
     * Gets the "action" element
     */
    Action getAction();
    
    /**
     * Sets the "action" element
     */
    void setAction(Action action);
    
    /**
     * Appends and returns a new empty "action" element
     */
    Action addNewAction();
    
    /**
     * An XML action(@).
     *
     * This is a complex type.
     */
    public interface Action extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Action.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C3B7C3BC07F9606A0B3E23E6AF6F901").resolveHandle("actionff6delemtype");
        
        /**
         * Gets the "action" attribute
         */
        String getAction();
        
        /**
         * Gets (as xml) the "action" attribute
         */
        org.apache.xmlbeans.XmlNCName xgetAction();
        
        /**
         * Sets the "action" attribute
         */
        void setAction(String action);
        
        /**
         * Sets (as xml) the "action" attribute
         */
        void xsetAction(org.apache.xmlbeans.XmlNCName action);
        
        /**
         * Gets the "generic" attribute
         */
        boolean getGeneric();
        
        /**
         * Gets (as xml) the "generic" attribute
         */
        org.apache.xmlbeans.XmlBoolean xgetGeneric();
        
        /**
         * True if has "generic" attribute
         */
        boolean isSetGeneric();
        
        /**
         * Sets the "generic" attribute
         */
        void setGeneric(boolean generic);
        
        /**
         * Sets (as xml) the "generic" attribute
         */
        void xsetGeneric(org.apache.xmlbeans.XmlBoolean generic);
        
        /**
         * Unsets the "generic" attribute
         */
        void unsetGeneric();
        
        /**
         * Gets the "icon" attribute
         */
        String getIcon();
        
        /**
         * Gets (as xml) the "icon" attribute
         */
        org.apache.xmlbeans.XmlNCName xgetIcon();
        
        /**
         * Sets the "icon" attribute
         */
        void setIcon(String icon);
        
        /**
         * Sets (as xml) the "icon" attribute
         */
        void xsetIcon(org.apache.xmlbeans.XmlNCName icon);
        
        /**
         * Gets the "id" attribute
         */
        String getId();
        
        /**
         * Gets (as xml) the "id" attribute
         */
        org.apache.xmlbeans.XmlID xgetId();
        
        /**
         * Sets the "id" attribute
         */
        void setId(String id);
        
        /**
         * Sets (as xml) the "id" attribute
         */
        void xsetId(org.apache.xmlbeans.XmlID id);
        
        /**
         * Gets the "text" attribute
         */
        String getText();
        
        /**
         * Gets (as xml) the "text" attribute
         */
        org.apache.xmlbeans.XmlString xgetText();
        
        /**
         * Sets the "text" attribute
         */
        void setText(String text);
        
        /**
         * Sets (as xml) the "text" attribute
         */
        void xsetText(org.apache.xmlbeans.XmlString text);
        
        /**
         * Gets the "autostart" attribute
         */
        String getAutostart();
        
        /**
         * Gets (as xml) the "autostart" attribute
         */
        org.apache.xmlbeans.XmlNCName xgetAutostart();
        
        /**
         * True if has "autostart" attribute
         */
        boolean isSetAutostart();
        
        /**
         * Sets the "autostart" attribute
         */
        void setAutostart(String autostart);
        
        /**
         * Sets (as xml) the "autostart" attribute
         */
        void xsetAutostart(org.apache.xmlbeans.XmlNCName autostart);
        
        /**
         * Unsets the "autostart" attribute
         */
        void unsetAutostart();
        
        /**
         * Gets the "detail-visible" attribute
         */
        String getDetailVisible();
        
        /**
         * Gets (as xml) the "detail-visible" attribute
         */
        org.apache.xmlbeans.XmlNCName xgetDetailVisible();
        
        /**
         * True if has "detail-visible" attribute
         */
        boolean isSetDetailVisible();
        
        /**
         * Sets the "detail-visible" attribute
         */
        void setDetailVisible(String detailVisible);
        
        /**
         * Sets (as xml) the "detail-visible" attribute
         */
        void xsetDetailVisible(org.apache.xmlbeans.XmlNCName detailVisible);
        
        /**
         * Unsets the "detail-visible" attribute
         */
        void unsetDetailVisible();
        
        /**
         * Gets the "dialog" attribute
         */
        String getDialog();
        
        /**
         * Gets (as xml) the "dialog" attribute
         */
        org.apache.xmlbeans.XmlNCName xgetDialog();
        
        /**
         * True if has "dialog" attribute
         */
        boolean isSetDialog();
        
        /**
         * Sets the "dialog" attribute
         */
        void setDialog(String dialog);
        
        /**
         * Sets (as xml) the "dialog" attribute
         */
        void xsetDialog(org.apache.xmlbeans.XmlNCName dialog);
        
        /**
         * Unsets the "dialog" attribute
         */
        void unsetDialog();
        
        /**
         * Gets the "documentation" attribute
         */
        String getDocumentation();
        
        /**
         * Gets (as xml) the "documentation" attribute
         */
        org.apache.xmlbeans.XmlNCName xgetDocumentation();
        
        /**
         * True if has "documentation" attribute
         */
        boolean isSetDocumentation();
        
        /**
         * Sets the "documentation" attribute
         */
        void setDocumentation(String documentation);
        
        /**
         * Sets (as xml) the "documentation" attribute
         */
        void xsetDocumentation(org.apache.xmlbeans.XmlNCName documentation);
        
        /**
         * Unsets the "documentation" attribute
         */
        void unsetDocumentation();
        
        /**
         * Gets the "param" attribute
         */
        String getParam();
        
        /**
         * Gets (as xml) the "param" attribute
         */
        org.apache.xmlbeans.XmlNCName xgetParam();
        
        /**
         * True if has "param" attribute
         */
        boolean isSetParam();
        
        /**
         * Sets the "param" attribute
         */
        void setParam(String param);
        
        /**
         * Sets (as xml) the "param" attribute
         */
        void xsetParam(org.apache.xmlbeans.XmlNCName param);
        
        /**
         * Unsets the "param" attribute
         */
        void unsetParam();
        
        /**
         * Gets the "shortcut" attribute
         */
        String getShortcut();
        
        /**
         * Gets (as xml) the "shortcut" attribute
         */
        org.apache.xmlbeans.XmlNCName xgetShortcut();
        
        /**
         * True if has "shortcut" attribute
         */
        boolean isSetShortcut();
        
        /**
         * Sets the "shortcut" attribute
         */
        void setShortcut(String shortcut);
        
        /**
         * Sets (as xml) the "shortcut" attribute
         */
        void xsetShortcut(org.apache.xmlbeans.XmlNCName shortcut);
        
        /**
         * Unsets the "shortcut" attribute
         */
        void unsetShortcut();
        
        /**
         * Gets the "supress-print" attribute
         */
        String getSupressPrint();
        
        /**
         * Gets (as xml) the "supress-print" attribute
         */
        org.apache.xmlbeans.XmlNCName xgetSupressPrint();
        
        /**
         * True if has "supress-print" attribute
         */
        boolean isSetSupressPrint();
        
        /**
         * Sets the "supress-print" attribute
         */
        void setSupressPrint(String supressPrint);
        
        /**
         * Sets (as xml) the "supress-print" attribute
         */
        void xsetSupressPrint(org.apache.xmlbeans.XmlNCName supressPrint);
        
        /**
         * Unsets the "supress-print" attribute
         */
        void unsetSupressPrint();
        
        /**
         * Gets the "visible" attribute
         */
        String getVisible();
        
        /**
         * Gets (as xml) the "visible" attribute
         */
        org.apache.xmlbeans.XmlNCName xgetVisible();
        
        /**
         * True if has "visible" attribute
         */
        boolean isSetVisible();
        
        /**
         * Sets the "visible" attribute
         */
        void setVisible(String visible);
        
        /**
         * Sets (as xml) the "visible" attribute
         */
        void xsetVisible(org.apache.xmlbeans.XmlNCName visible);
        
        /**
         * Unsets the "visible" attribute
         */
        void unsetVisible();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static Action newInstance() {
              return (Action) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static Action newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (Action) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static ActionDocument newInstance() {
          return (ActionDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static ActionDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (ActionDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static ActionDocument parse(String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (ActionDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static ActionDocument parse(String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ActionDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static ActionDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ActionDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static ActionDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ActionDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static ActionDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ActionDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static ActionDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ActionDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static ActionDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ActionDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static ActionDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ActionDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static ActionDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ActionDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static ActionDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ActionDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static ActionDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (ActionDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static ActionDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ActionDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static ActionDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (ActionDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static ActionDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ActionDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }

        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static ActionDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (ActionDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }*/
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static ActionDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (ActionDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }*/
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }*/
        
        /* @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        /*public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }*/
        
        private Factory() { } // No instance of this class allowed
    }
}

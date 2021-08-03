/*
 * An XML document type.
 * Localname: filecopy
 * Namespace: 
 * Java type: ch.minova.core.install.FilecopyDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one filecopy(@) element.
 *
 * This is a complex type.
 */
public class FilecopyDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.FilecopyDocument
{
    private static final long serialVersionUID = 1L;
    
    public FilecopyDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName FILECOPY$0 = 
        new javax.xml.namespace.QName("", "filecopy");
    
    
    /**
     * Gets the "filecopy" element
     */
    public Filecopy getFilecopy()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Filecopy target = null;
            target = (Filecopy)get_store().find_element_user(FILECOPY$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "filecopy" element
     */
    public void setFilecopy(Filecopy filecopy)
    {
        synchronized (monitor())
        {
            check_orphaned();
            Filecopy target = null;
            target = (Filecopy)get_store().find_element_user(FILECOPY$0, 0);
            if (target == null)
            {
                target = (Filecopy)get_store().add_element_user(FILECOPY$0);
            }
            target.set(filecopy);
        }
    }
    
    /**
     * Appends and returns a new empty "filecopy" element
     */
    public Filecopy addNewFilecopy()
    {
        synchronized (monitor())
        {
            check_orphaned();
            Filecopy target = null;
            target = (Filecopy)get_store().add_element_user(FILECOPY$0);
            return target;
        }
    }
    /**
     * An XML filecopy(@).
     *
     * This is a complex type.
     */
    public static class FilecopyImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements Filecopy
    {
        private static final long serialVersionUID = 1L;
        
        public FilecopyImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName OS$0 = 
            new javax.xml.namespace.QName("", "os");
        private static final javax.xml.namespace.QName ARCH$2 = 
            new javax.xml.namespace.QName("", "arch");
        private static final javax.xml.namespace.QName FROMDIR$4 = 
            new javax.xml.namespace.QName("", "fromdir");
        private static final javax.xml.namespace.QName FILENAME$6 = 
            new javax.xml.namespace.QName("", "filename");
        private static final javax.xml.namespace.QName TODIR$8 = 
            new javax.xml.namespace.QName("", "todir");
        
        
        /**
         * Gets the "os" attribute
         */
        public Os.Enum getOs()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OS$0);
                if (target == null)
                {
                    return null;
                }
                return (Os.Enum)target.getEnumValue();
            }
        }
        
        /**
         * Gets (as xml) the "os" attribute
         */
        public Os xgetOs()
        {
            synchronized (monitor())
            {
                check_orphaned();
                Os target = null;
                target = (Os)get_store().find_attribute_user(OS$0);
                return target;
            }
        }
        
        /**
         * True if has "os" attribute
         */
        public boolean isSetOs()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(OS$0) != null;
            }
        }
        
        /**
         * Sets the "os" attribute
         */
        public void setOs(Os.Enum os)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OS$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(OS$0);
                }
                target.setEnumValue(os);
            }
        }
        
        /**
         * Sets (as xml) the "os" attribute
         */
        public void xsetOs(Os os)
        {
            synchronized (monitor())
            {
                check_orphaned();
                Os target = null;
                target = (Os)get_store().find_attribute_user(OS$0);
                if (target == null)
                {
                    target = (Os)get_store().add_attribute_user(OS$0);
                }
                target.set(os);
            }
        }
        
        /**
         * Unsets the "os" attribute
         */
        public void unsetOs()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(OS$0);
            }
        }
        
        /**
         * Gets the "arch" attribute
         */
        public Arch.Enum getArch()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ARCH$2);
                if (target == null)
                {
                    return null;
                }
                return (Arch.Enum)target.getEnumValue();
            }
        }
        
        /**
         * Gets (as xml) the "arch" attribute
         */
        public Arch xgetArch()
        {
            synchronized (monitor())
            {
                check_orphaned();
                Arch target = null;
                target = (Arch)get_store().find_attribute_user(ARCH$2);
                return target;
            }
        }
        
        /**
         * True if has "arch" attribute
         */
        public boolean isSetArch()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(ARCH$2) != null;
            }
        }
        
        /**
         * Sets the "arch" attribute
         */
        public void setArch(Arch.Enum arch)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ARCH$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(ARCH$2);
                }
                target.setEnumValue(arch);
            }
        }
        
        /**
         * Sets (as xml) the "arch" attribute
         */
        public void xsetArch(Arch arch)
        {
            synchronized (monitor())
            {
                check_orphaned();
                Arch target = null;
                target = (Arch)get_store().find_attribute_user(ARCH$2);
                if (target == null)
                {
                    target = (Arch)get_store().add_attribute_user(ARCH$2);
                }
                target.set(arch);
            }
        }
        
        /**
         * Unsets the "arch" attribute
         */
        public void unsetArch()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(ARCH$2);
            }
        }
        
        /**
         * Gets the "fromdir" attribute
         */
        public String getFromdir()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FROMDIR$4);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "fromdir" attribute
         */
        public org.apache.xmlbeans.XmlString xgetFromdir()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(FROMDIR$4);
                return target;
            }
        }
        
        /**
         * True if has "fromdir" attribute
         */
        public boolean isSetFromdir()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(FROMDIR$4) != null;
            }
        }
        
        /**
         * Sets the "fromdir" attribute
         */
        public void setFromdir(String fromdir)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FROMDIR$4);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(FROMDIR$4);
                }
                target.setStringValue(fromdir);
            }
        }
        
        /**
         * Sets (as xml) the "fromdir" attribute
         */
        public void xsetFromdir(org.apache.xmlbeans.XmlString fromdir)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(FROMDIR$4);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(FROMDIR$4);
                }
                target.set(fromdir);
            }
        }
        
        /**
         * Unsets the "fromdir" attribute
         */
        public void unsetFromdir()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(FROMDIR$4);
            }
        }
        
        /**
         * Gets the "filename" attribute
         */
        public String getFilename()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FILENAME$6);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "filename" attribute
         */
        public org.apache.xmlbeans.XmlNCName xgetFilename()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(FILENAME$6);
                return target;
            }
        }
        
        /**
         * Sets the "filename" attribute
         */
        public void setFilename(String filename)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FILENAME$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(FILENAME$6);
                }
                target.setStringValue(filename);
            }
        }
        
        /**
         * Sets (as xml) the "filename" attribute
         */
        public void xsetFilename(org.apache.xmlbeans.XmlNCName filename)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlNCName target = null;
                target = (org.apache.xmlbeans.XmlNCName)get_store().find_attribute_user(FILENAME$6);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlNCName)get_store().add_attribute_user(FILENAME$6);
                }
                target.set(filename);
            }
        }
        
        /**
         * Gets the "todir" attribute
         */
        public String getTodir()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TODIR$8);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "todir" attribute
         */
        public org.apache.xmlbeans.XmlString xgetTodir()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(TODIR$8);
                return target;
            }
        }
        
        /**
         * Sets the "todir" attribute
         */
        public void setTodir(String todir)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TODIR$8);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(TODIR$8);
                }
                target.setStringValue(todir);
            }
        }
        
        /**
         * Sets (as xml) the "todir" attribute
         */
        public void xsetTodir(org.apache.xmlbeans.XmlString todir)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(TODIR$8);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(TODIR$8);
                }
                target.set(todir);
            }
        }
        /**
         * An XML os(@).
         *
         * This is an atomic type that is a restriction of ch.minova.core.install.FilecopyDocument$Filecopy$Os.
         */
        public static class OsImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements Os
        {
            private static final long serialVersionUID = 1L;
            
            public OsImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType, false);
            }
            
            protected OsImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
            {
                super(sType, b);
            }
        }
        /**
         * An XML arch(@).
         *
         * This is an atomic type that is a restriction of ch.minova.core.install.FilecopyDocument$Filecopy$Arch.
         */
        public static class ArchImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements Arch
        {
            private static final long serialVersionUID = 1L;
            
            public ArchImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType, false);
            }
            
            protected ArchImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
            {
                super(sType, b);
            }
        }
    }
}

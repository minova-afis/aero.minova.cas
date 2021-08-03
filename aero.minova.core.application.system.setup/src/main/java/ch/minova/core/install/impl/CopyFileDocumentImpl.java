/*
 * An XML document type.
 * Localname: copy-file
 * Namespace: 
 * Java type: ch.minova.core.install.CopyFileDocument
 *
 * Automatically generated - do not modify.
 */
package ch.minova.core.install.impl;
/**
 * A document containing one copy-file(@) element.
 *
 * This is a complex type.
 */
public class CopyFileDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.minova.core.install.CopyFileDocument
{
    private static final long serialVersionUID = 1L;
    
    public CopyFileDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName COPYFILE$0 = 
        new javax.xml.namespace.QName("", "copy-file");
    
    
    /**
     * Gets the "copy-file" element
     */
    public CopyFile getCopyFile()
    {
        synchronized (monitor())
        {
            check_orphaned();
            CopyFile target = null;
            target = (CopyFile)get_store().find_element_user(COPYFILE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "copy-file" element
     */
    public void setCopyFile(CopyFile copyFile)
    {
        synchronized (monitor())
        {
            check_orphaned();
            CopyFile target = null;
            target = (CopyFile)get_store().find_element_user(COPYFILE$0, 0);
            if (target == null)
            {
                target = (CopyFile)get_store().add_element_user(COPYFILE$0);
            }
            target.set(copyFile);
        }
    }
    
    /**
     * Appends and returns a new empty "copy-file" element
     */
    public CopyFile addNewCopyFile()
    {
        synchronized (monitor())
        {
            check_orphaned();
            CopyFile target = null;
            target = (CopyFile)get_store().add_element_user(COPYFILE$0);
            return target;
        }
    }
    /**
     * An XML copy-file(@).
     *
     * This is a complex type.
     */
    public static class CopyFileImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements CopyFile
    {
        private static final long serialVersionUID = 1L;
        
        public CopyFileImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName FILECOPY$0 = 
            new javax.xml.namespace.QName("", "filecopy");
        private static final javax.xml.namespace.QName DIRCOPY$2 = 
            new javax.xml.namespace.QName("", "dircopy");
        
        
        /**
         * Gets array of all "filecopy" elements
         */
        public ch.minova.core.install.FilecopyDocument.Filecopy[] getFilecopyArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(FILECOPY$0, targetList);
                ch.minova.core.install.FilecopyDocument.Filecopy[] result = new ch.minova.core.install.FilecopyDocument.Filecopy[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "filecopy" element
         */
        public ch.minova.core.install.FilecopyDocument.Filecopy getFilecopyArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.FilecopyDocument.Filecopy target = null;
                target = (ch.minova.core.install.FilecopyDocument.Filecopy)get_store().find_element_user(FILECOPY$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "filecopy" element
         */
        public int sizeOfFilecopyArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(FILECOPY$0);
            }
        }
        
        /**
         * Sets array of all "filecopy" element
         */
        public void setFilecopyArray(ch.minova.core.install.FilecopyDocument.Filecopy[] filecopyArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(filecopyArray, FILECOPY$0);
            }
        }
        
        /**
         * Sets ith "filecopy" element
         */
        public void setFilecopyArray(int i, ch.minova.core.install.FilecopyDocument.Filecopy filecopy)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.FilecopyDocument.Filecopy target = null;
                target = (ch.minova.core.install.FilecopyDocument.Filecopy)get_store().find_element_user(FILECOPY$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(filecopy);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "filecopy" element
         */
        public ch.minova.core.install.FilecopyDocument.Filecopy insertNewFilecopy(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.FilecopyDocument.Filecopy target = null;
                target = (ch.minova.core.install.FilecopyDocument.Filecopy)get_store().insert_element_user(FILECOPY$0, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "filecopy" element
         */
        public ch.minova.core.install.FilecopyDocument.Filecopy addNewFilecopy()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.FilecopyDocument.Filecopy target = null;
                target = (ch.minova.core.install.FilecopyDocument.Filecopy)get_store().add_element_user(FILECOPY$0);
                return target;
            }
        }
        
        /**
         * Removes the ith "filecopy" element
         */
        public void removeFilecopy(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(FILECOPY$0, i);
            }
        }
        
        /**
         * Gets array of all "dircopy" elements
         */
        public ch.minova.core.install.DircopyDocument.Dircopy[] getDircopyArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(DIRCOPY$2, targetList);
                ch.minova.core.install.DircopyDocument.Dircopy[] result = new ch.minova.core.install.DircopyDocument.Dircopy[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "dircopy" element
         */
        public ch.minova.core.install.DircopyDocument.Dircopy getDircopyArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.DircopyDocument.Dircopy target = null;
                target = (ch.minova.core.install.DircopyDocument.Dircopy)get_store().find_element_user(DIRCOPY$2, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "dircopy" element
         */
        public int sizeOfDircopyArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(DIRCOPY$2);
            }
        }
        
        /**
         * Sets array of all "dircopy" element
         */
        public void setDircopyArray(ch.minova.core.install.DircopyDocument.Dircopy[] dircopyArray)
        {
            synchronized (monitor())
            {
                check_orphaned();
                arraySetterHelper(dircopyArray, DIRCOPY$2);
            }
        }
        
        /**
         * Sets ith "dircopy" element
         */
        public void setDircopyArray(int i, ch.minova.core.install.DircopyDocument.Dircopy dircopy)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.DircopyDocument.Dircopy target = null;
                target = (ch.minova.core.install.DircopyDocument.Dircopy)get_store().find_element_user(DIRCOPY$2, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                target.set(dircopy);
            }
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "dircopy" element
         */
        public ch.minova.core.install.DircopyDocument.Dircopy insertNewDircopy(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.DircopyDocument.Dircopy target = null;
                target = (ch.minova.core.install.DircopyDocument.Dircopy)get_store().insert_element_user(DIRCOPY$2, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "dircopy" element
         */
        public ch.minova.core.install.DircopyDocument.Dircopy addNewDircopy()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.minova.core.install.DircopyDocument.Dircopy target = null;
                target = (ch.minova.core.install.DircopyDocument.Dircopy)get_store().add_element_user(DIRCOPY$2);
                return target;
            }
        }
        
        /**
         * Removes the ith "dircopy" element
         */
        public void removeDircopy(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(DIRCOPY$2, i);
            }
        }
    }
}

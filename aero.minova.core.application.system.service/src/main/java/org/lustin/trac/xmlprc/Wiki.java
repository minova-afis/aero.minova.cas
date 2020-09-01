package org.lustin.trac.xmlprc;

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @author lustin
 */
public interface Wiki {
	Hashtable<?, ?> getRecentChanges(Date since);

	Integer getRPCVersionSupported();

	String getPage(String pagename);

	String getPage(String pagename, Integer version);

	String getPageVersion(String pagename);

	String getPageVersion(String pagename, Integer version);

	String getPageHTML(String pagename);

	String getPageHTML(String pagename, Integer version);

	String getPageHTMLVersion(String pagename);

	String getPageHTMLVersion(String pagename, Integer version);

	Vector<?> getAllPages();

	Hashtable<String, ?> getPageInfo(String pagename);

	Hashtable<String, ?> getPageInfo(String pagename, Integer version);

	Hashtable<String, ?> getPageInfoVersion(String pagename);

	Hashtable<String, ?> getPageInfoVersion(String pagename, Integer version);

	Boolean putPage(String pagename, String content, Hashtable<String, ?> attributes);

	Hashtable<String, ?> istAttachments(String pagename);

	byte[] getAttachment(String path);

	Boolean putAttachment(String path, byte[] data);

	Boolean putAttachmentEx(String pagename, String filename, String description, byte[] data);

	Boolean putAttachmentEx(String pagename, String filename, String description, byte[] data, boolean replace);

	Boolean deletePage(String name);

	Boolean deletePage(String name, int version);

	Boolean deleteAttachment(String path);

	Vector<?> listLinks(String pagename);

	String wikiToHtml(String text);
}
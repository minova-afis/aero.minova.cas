<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

    <!-- Dieses Stylesheet löscht die "description"- und "docbook"- 
        Elemente aus einer *.from.xml-Datei -->

    <!-- Ouput-Deklaration -->

    <xsl:output indent="yes" omit-xml-declaration="no" encoding="UTF-8" xml:space="default" method="xml"/>

    <!-- Root-Element "form" durchlaufen -->

    <xsl:template match="/form">
        <form>
            <xsl:for-each select="@*">
                <xsl:attribute name="{name(.)}">
                    <xsl:value-of select="."/>
                </xsl:attribute>
            </xsl:for-each>
            <xsl:apply-templates/>
        </form>
    </xsl:template>
    
    <xsl:template match="/addon">
        <addon>
            <xsl:for-each select="@*">
                <xsl:attribute name="{name(.)}">
                    <xsl:value-of select="."/>
                </xsl:attribute>
            </xsl:for-each>
            <xsl:apply-templates/>
        </addon>
    </xsl:template>

    <xsl:template match="/grid">
        <grid>
            <xsl:for-each select="@*">
                <xsl:attribute name="{name(.)}">
                    <xsl:value-of select="."/>
                </xsl:attribute>
            </xsl:for-each>
            <xsl:apply-templates/>
        </grid>
    </xsl:template>

    <!-- Template, das Elemente kopiert -->

    <xsl:template match="*">
        <!-- Alle Elemente außer "description" und "docbook" kopieren -->
        <xsl:if test="name(.)!='description' and name(.)!='docbook'">
            <xsl:choose>
                <xsl:when test="name(.) = 'detail' and boolean(@class)">
                    <!-- Wenn das Detail eine J++-Klasse ist, werfen wir den Inhalt weg, da dieser nur der Dokumentation dient -->
                    <xsl:element name="detail">
                        <xsl:for-each select="@*">
                            <xsl:attribute name="{name(.)}">
                                <xsl:value-of select="."/>
                            </xsl:attribute>
                        </xsl:for-each>
                    </xsl:element>
                </xsl:when>
                <xsl:otherwise>
                    <!-- Aktuelles Element kopieren -->
                    <xsl:element name="{name(.)}">
                        <!-- Attribute des aktuellen Elements kopieren -->
                        <xsl:for-each select="@*">
                            <xsl:attribute name="{name(.)}">
                                <xsl:value-of select="."/>
                            </xsl:attribute>
                        </xsl:for-each>
                        <!-- Template rekursiv für alle Unterelemente aufrufen. -->
                        <xsl:apply-templates/>
                    </xsl:element>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>
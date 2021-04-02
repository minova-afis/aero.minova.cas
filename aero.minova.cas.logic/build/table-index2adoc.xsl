<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

    <!-- Dieses Stylesheet Erstellt aus einer Tabelle ein adoc -->

    <!-- Ouput-Deklaration -->
    <xsl:output indent="no" omit-xml-declaration="yes" encoding="UTF-8" xml:space="default" method="text" />

    <!-- Root-Element "table" durchlaufen -->
    <xsl:template match="/files">
        <xsl:text>

== Tabellen 
</xsl:text>
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="file">
        <xsl:apply-templates></xsl:apply-templates>
    </xsl:template>

    <xsl:template match="name">
        <xsl:text>
* link:</xsl:text>
        <xsl:value-of select="substring-before(., '.table.xml')" />
        <xsl:text>.adoc[]
</xsl:text>
    </xsl:template>
</xsl:stylesheet>
<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

    <!-- Dieses Stylesheet Erstellt aus einer Tabelle ein adoc -->

    <!-- Ouput-Deklaration -->
    <xsl:output indent="no" omit-xml-declaration="yes" encoding="UTF-8" xml:space="default" method="text" />

    <!-- Root-Element "table" durchlaufen -->
    <xsl:template match="/table">
        <xsl:text>

== Tabelle </xsl:text>
        <xsl:value-of select="./@name" />
        <xsl:text> (</xsl:text>
        <xsl:value-of select="./desc" />
        <xsl:text>)
</xsl:text>
        <xsl:value-of select="./adoc" />
        <xsl:text>

=== Spalten

In der Tabelle sind folgende Spalten definiert.</xsl:text>
        <xsl:apply-templates select="column" />
    </xsl:template>


    <xsl:template match="column">
        <xsl:text>

==== </xsl:text>
        <xsl:value-of select="./@name" />
        <xsl:text>
</xsl:text>
        <!-- Kurzbezeichnung -->
        <xsl:apply-templates select="desc" />

        <!-- Datentyp angeben -->
        <xsl:apply-templates select="bigint" />
        <xsl:apply-templates select="boolean" />
        <xsl:apply-templates select="datetime" />
        <xsl:apply-templates select="float" />
        <xsl:apply-templates select="integer" />
        <xsl:apply-templates select="money" />
        <xsl:apply-templates select="varchar" />

        <!-- Default angeben -->
        <xsl:apply-templates select="./@default"></xsl:apply-templates>

        <!-- Absatz und lange Beschreibung hinzufügen -->
        <xsl:text>

// tag::column.</xsl:text>
        <xsl:value-of select="./@name"></xsl:value-of>
        <xsl:text>[]
</xsl:text>
        <xsl:if test="./adoc/@default = 'true'">
            <xsl:text>include::defaults.adoc[tags=column.</xsl:text>
            <xsl:value-of select="./@name" />
            <xsl:text>]</xsl:text>
        </xsl:if>
        <xsl:if test="not(./adoc/@default = 'true')">
            <xsl:value-of select="./adoc" />
        </xsl:if>
        <xsl:text>
// end::column.</xsl:text>
        <xsl:value-of select="./@name"></xsl:value-of>
        <xsl:text>[]
</xsl:text>
    </xsl:template>


    <xsl:template match="desc">
        <xsl:text>
Bezeichnung: </xsl:text>
        <xsl:value-of select="." />
        <xsl:text> +</xsl:text>
    </xsl:template>

    <!-- - - - - - - - - - - - - - - - -->
    <!-- Datentypen der Spalte -->
    <!-- - - - - - - - - - - - - - - - -->
    <xsl:template match="bigint">
        <xsl:text>
Datentyp: bigint</xsl:text>
        <xsl:if test="@nullable = 'false'">
            <xsl:text> not null</xsl:text>
        </xsl:if>
        <xsl:text> +</xsl:text>
    </xsl:template>

    <xsl:template match="boolean">
        <xsl:text>
Datentyp: bit</xsl:text>
        <xsl:if test="@nullable = 'false'">
            <xsl:text> not null</xsl:text>
        </xsl:if>
        <xsl:text> +</xsl:text>
    </xsl:template>

    <xsl:template match="datetime">
        <xsl:text>
Datentyp: datetime</xsl:text>
        <xsl:if test="@nullable = 'false'">
            <xsl:text> not null</xsl:text>
        </xsl:if>
        <xsl:text> +</xsl:text>
    </xsl:template>

    <xsl:template match="float">
        <xsl:text>
Datentyp: float</xsl:text>
        <xsl:if test="@nullable = 'false'">
            <xsl:text> not null</xsl:text>
        </xsl:if>
        <xsl:text> +</xsl:text>
    </xsl:template>

    <xsl:template match="integer">
        <xsl:text>
Datentyp: integer</xsl:text>
        <xsl:if test="@identity">
            <xsl:text> (identity)</xsl:text>
        </xsl:if>
        <xsl:if test="@nullable = 'false'">
            <xsl:text> not null</xsl:text>
        </xsl:if>
        <xsl:text> +</xsl:text>
    </xsl:template>

    <xsl:template match="money">
        <xsl:text>
Datentyp: money</xsl:text>
        <xsl:if test="@nullable = 'false'">
            <xsl:text> not null</xsl:text>
        </xsl:if>
        <xsl:text> +</xsl:text>
    </xsl:template>

    <xsl:template match="varchar">
        <xsl:text>
Datentyp: nvarchar(</xsl:text>
        <xsl:value-of select="./@length" />
        <xsl:text>)</xsl:text>
        <xsl:if test="@nullable = 'false'">
            <xsl:text> not null</xsl:text>
        </xsl:if>
        <xsl:text> +</xsl:text>
    </xsl:template>

    <!-- - - - - - - - - - - - - - - - -->
    <!-- Defaultwert anzeigen -->
    <!-- - - - - - - - - - - - - - - - -->
    <xsl:template match="column/@default">
        <xsl:text>
Default: `</xsl:text>
        <xsl:value-of select="." />
        <xsl:text>` +</xsl:text>
    </xsl:template>


</xsl:stylesheet>
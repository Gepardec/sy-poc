<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform' 
xmlns:li="http://com.gepardec/sy_poc/xml/message_request_internal"
xmlns='http://liwest.at/fsw_poc/xml/message_request_1_0'
>
  <xsl:output encoding='UTF-8' indent='yes' method='xml'/>

  <xsl:template match="li:message">
    <message xmlns='http://com.gepardec/sy_poc/xml/message_request_internal'>
        <xsl:copy-of select='@*' />
        <xsl:apply-templates select='@*|node()' />
    </message>
  </xsl:template>
  <!-- Identity transform -->
  <xsl:template match="@*|text()|comment()|processing-instruction()">
    <xsl:copy>
      <xsl:apply-templates select='@*|node()'/>
    </xsl:copy>
  </xsl:template>
  <!-- Previous namespace -> current. No other changes required. -->
    <xsl:template match="*">
        <xsl:element name="{local-name()}">
            <xsl:apply-templates select="@*|node()"/>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>
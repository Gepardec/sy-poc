<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:p='http://com.gepardec/sy_poc/xml/message_request_internal'>
	<xsl:output encoding='UTF-8' indent='yes' method='xml'/>

	<!-- standard copy template -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*" />
			<xsl:apply-templates />
		</xsl:copy>
	</xsl:template>
	<xsl:template match='message'>
		<xsl:element name='{local-name()}'
			namespace='http://com.gepardec/sy_poc/xml/message_request_internal'>
			<xsl:copy-of select='namespace::*[not(. = namespace-uri(current()))]' />
			<xsl:apply-templates select='@* | node()' />
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>
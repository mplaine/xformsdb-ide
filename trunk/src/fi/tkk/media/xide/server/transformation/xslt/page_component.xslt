<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns="http://www.w3.org/1999/xhtml" xmlns:ev="http://www.w3.org/2001/xml-events" xmlns:saxon="http://saxon.sf.net/" xmlns:xforms="http://www.w3.org/2002/xforms"
	xmlns:xformsdb="http://www.tml.tkk.fi/2007/xformsdb" xmlns:func="http://www.tml.tkk.fi/2007/xformsdb/xsl/functions" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:template="http://www.tml.tkk.fi/2009/template" xmlns:html="http://www.w3.org/1999/xhtml" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:exforms="http://www.exforms.org/exf/1-0"
	xmlns:xxforms="http://orbeon.org/oxf/xml/xforms" xmlns:xs="http://www.w3.org/2001/XMLSchema">

	<!-- ============== Import the identity transformation ============== -->
	
	<xsl:import href="identity.xsl" />

	<!-- ============== Define the output format ============== -->

	<xsl:output method="xml" version="1.0" indent="yes" />

	<xsl:template match="/">
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="html:html">

		<!--
			<template:component xmlns="http://www.w3.org/1999/xhtml" xmlns:ev="http://www.w3.org/2001/xml-events" xmlns:exforms="http://www.exforms.org/exf/1-0"
			xmlns:template="http://www.tml.tkk.fi/2009/template"
		-->
		<!--
			xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xforms="http://www.w3.org/2002/xforms" xmlns:xformsdb="http://www.tml.tkk.fi/2007/xformsdb"
		-->
		<!--			xmlns:xxforms="http://orbeon.org/oxf/xml/xforms">-->
		<template:component>
			<xsl:apply-templates />
		</template:component>

	</xsl:template>

	<xsl:template match="html:html/html:head">

		<template:head>

			<xsl:apply-templates />

		</template:head>

	</xsl:template>

	<xsl:template match="html:html/html:body">

		<template:body>

			<xsl:apply-templates />

		</template:body>

	</xsl:template>

	<xsl:template match="html:html/html:head//html:meta">

		<template:meta>

			<xsl:copy-of select="@*" />

		</template:meta>

	</xsl:template>

</xsl:stylesheet>
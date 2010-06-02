<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns="http://www.w3.org/1999/xhtml" xmlns:ev="http://www.w3.org/2001/xml-events" xmlns:saxon="http://saxon.sf.net/" xmlns:xforms="http://www.w3.org/2002/xforms"
	xmlns:xformsdb="http://www.tml.tkk.fi/2007/xformsdb" xmlns:func="http://www.tml.tkk.fi/2007/xformsdb/xsl/functions" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:html="http://www.w3.org/1999/xhtml" xmlns:template="http://www.tml.tkk.fi/2009/template" xmlns:exforms="http://www.exforms.org/exf/1-0" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:xxforms="http://orbeon.org/oxf/xml/xforms">

	<!-- ============== Import the identity transformation ============== -->

	<xsl:import href="identity.xsl" />
	
	<!-- ============== These parameters are set in Java ============== -->

	<xsl:param name="componentFiles" />
	<xsl:param name="userName" />
	<xsl:param name="useTheme" />

	<!-- ============== Define the output format ============== -->

	<xsl:output method="xml" version="1.0" indent="yes" />

	<!-- ============== Match the root and apply further templates ============== -->
	
	<xsl:template match="/">
		<xsl:apply-templates />
	</xsl:template>

	<!-- ============== HTML & namespace declarations here. ============== -->

	<xsl:template match="template:webpage">

		<!--
			<html xml:lang="en" lang="en" xmlns="http://www.w3.org/1999/xhtml" xmlns:ev="http://www.w3.org/2001/xml-events" xmlns:exforms="http://www.exforms.org/exf/1-0"
			xmlns:template="http://www.tml.tkk.fi/2009/template"
		-->
		<!--
			xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xforms="http://www.w3.org/2002/xforms" xmlns:xformsdb="http://www.tml.tkk.fi/2007/xformsdb"
		-->
		<!--			xmlns:xxforms="http://orbeon.org/oxf/xml/xforms">-->

		<html>
			<xsl:apply-templates />
		</html>


	</xsl:template>

	<!--  ============== HTML head (add CSS and apply further templates) ============== -->

	<xsl:template match="//template:webpage/template:head">
		<head>

			<!-- BEGIN: Application theme -->
			<xsl:if test="$useTheme = 'true'">
				<link rel="stylesheet" type="text/css" href="css/reset_theme.css" media="all" />
			</xsl:if>
			<!-- END: Application theme -->

			<!--   BEGIN: Component style(s)  -->
			<xsl:for-each select="//template:webpage//template:body//template:call-component">
				<xsl:sort select="@name" order="ascending" />
				<xsl:if test="not(@name = preceding-sibling::template:call-component/@name)">
					<xsl:apply-templates select="document(concat($componentFiles,$userName, '_', @id, '.xml'))/template:component//template:head//xhtml:link" />
				</xsl:if>
			</xsl:for-each>
			<!-- END: Component style(s) -->

			<!-- BEGIN: Page style(s) -->
			<xsl:for-each select="xhtml:link">
				<xsl:if test="not(@href = 'css/reset_theme.css')">
					<xsl:copy-of select="." />
				</xsl:if>
			</xsl:for-each>
			<!-- END: Page style(s) -->

			<xsl:apply-templates />

		</head>
	</xsl:template>

	<!-- ============== Component model instances (apply templates to each model) ============== -->

	<xsl:template match="//template:webpage//template:head//xforms:model">

		<xforms:model>

			<xsl:apply-templates />

			<xsl:for-each select="//template:webpage//template:body//template:call-component">
				<xsl:apply-templates select="document(concat($componentFiles,$userName, '_', @id, '.xml'))/template:component//template:head//xforms:model/child::*" />
				<!--				<xsl:apply-templates select="document(concat($componentFiles,$userName, '_', @id, '.xml'))/template:component//template:head//xforms:model//xformsdb:instance" />-->
				<!--				<xsl:apply-templates select="document(concat($componentFiles,$userName, '_', @id, '.xml'))/template:component//template:head//xforms:model//xformsdb:submission" />-->
				<!--				<xsl:apply-templates select="document(concat($componentFiles,$userName, '_', @id, '.xml'))/template:component//template:head//xforms:model//xformsdb:action" />-->
			</xsl:for-each>

		</xforms:model>
	</xsl:template>

	<!--  ============== HTML body (apply templates) ============== -->

	<xsl:template match="//template:webpage/template:body">
		<body>
			<xsl:apply-templates />
		</body>
	</xsl:template>

	<!-- ============== Component(s) body (apply templates) ============== -->

	<xsl:template match="//template:webpage//template:body//template:container">
		<xsl:apply-templates />
	</xsl:template>

	<!-- ============== Component(s) body (apply templates) ============== -->

	<xsl:template match="//template:webpage//template:body//template:container//template:call-component">

		<xsl:apply-templates select="document(concat($componentFiles,$userName, '_', @id, '.xml'))//template:component//template:body" />

	</xsl:template>

	<!-- ============== Component(s) body (apply templates) ============== -->

	<xsl:template match="//template:component//template:body">
		<xsl:apply-templates />
	</xsl:template>

	<!--  ============== Remove the meta elements ============== -->

	<xsl:template match="//template:webpage//template:meta">
	</xsl:template>

	<!-- ============== Container body (apply templates) ============== -->

	<xsl:template match="//template:webpage//template:body//template:container//template:body">
		<xsl:apply-templates />
	</xsl:template>

	<!--  ============== Remove the head template element of the containers -->

	<xsl:template match="//template:webpage//template:body//template:container//template:head">
	</xsl:template>

	<!--  ============== Don't apply templates to the webpage CSS since they have already been copied -->

	<xsl:template match="//template:webpage/template:head//xhtml:link">
	</xsl:template>

</xsl:stylesheet>
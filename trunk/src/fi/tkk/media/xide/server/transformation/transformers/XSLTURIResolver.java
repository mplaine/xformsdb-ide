package fi.tkk.media.xide.server.transformation.transformers;

import java.io.File;
import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import fi.tkk.media.xide.server.Config;

/**
 * This class resolves URIs when XSLT processor encounters an xsl:include,
 * xsl:import, or document() function.
 */
public class XSLTURIResolver implements URIResolver {

	public Source resolve(String href, String base) {

		// Load the identity

		if (href.equals("identity.xsl")) {
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(Config.getIdentityPath());
			StreamSource source = new StreamSource(inputStream);
			return source;

		} else {
			// Just load the file
			StreamSource source = new StreamSource(new File(href));
			return source;

		}
	}
}

package br.edu.ifg.ime.suport.xsd;

import java.io.ByteArrayInputStream;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

import br.edu.ifg.ime.controllers.ArquivoController;
import br.edu.ifg.ime.controllers.LinguagemController;
import br.edu.ifg.ime.dao.DAOConnection;
import br.edu.ifg.ime.test.ImeTest;

import com.sun.org.apache.xerces.internal.dom.DOMInputImpl;

public class SimpleResolver implements LSResourceResolver {

	private Source[] streams;

	public SimpleResolver(Source[] streams) {
		this.streams = streams;
	}

	@Override
	public LSInput resolveResource(String type, String namespaceURI,
			String publicId, String systemId, String baseURI) {
		DOMImplementationRegistry registry;

		LSInput input = new DOMInputImpl();
		try {
			if (DAOConnection.isActivated)        	
				input.setByteStream(new ByteArrayInputStream(ArquivoController.getBytesXSD(systemId)));
			else 
				input.setByteStream(ImeTest.getInputStreamOfResource(systemId));
			return input;
		} catch (Exception ex) {
			return null;
		}

	}


	public static Schema createSchema(String fonte) throws SAXException {
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);  

		StreamSource ss1 = null;

		if (DAOConnection.isActivated)
			ss1 = new StreamSource(new ByteArrayInputStream(ArquivoController.getBytesXSD(fonte)));
		else 
			ss1 = new StreamSource(ImeTest.getInputStreamOfResource(fonte));
		ss1.setSystemId(fonte);
		ss1.setPublicId(fonte);

		StreamSource ss2 = null;



		if (DAOConnection.isActivated)
			ss2 = new StreamSource(new ByteArrayInputStream(ArquivoController.getBytesXSD("imscp_v1p2.xsd")));
		else
			ss2 = new StreamSource(ImeTest.getInputStreamOfResource("imscp_v1p2.xsd"));


		ss2.setSystemId("imscp_v1p2.xsd");
		ss2.setPublicId("imscp_v1p2.xsd");

		Source[] sources = new Source[]{ss1, ss2};
		sf.setResourceResolver(new SimpleResolver(sources));
		Schema schemas = sf.newSchema(sources);
		return schemas;
	}

}


/*


sf.setResourceResolver(new LSResourceResolver() {

	@Override
	public LSInput resolveResource(String type, String namespaceURI,
			String publicId, String systemId, String baseURI) {
		// TODO Auto-generated method stub

		LSInput input = new LSInput() {
			private Reader characterStream;
			private InputStream byteStream;
			private String stringData;
			private String systemId;
			private String publicId;
			private String baseURI;
			private String encoding;
			private boolean certifiedText;

			@Override
			public void setSystemId(String systemId) {
				this.systemId = systemId;

			}

			@Override
			public void setStringData(String stringData) {
				this.stringData = stringData;

			}

			@Override
			public void setPublicId(String publicId) {
				this.publicId = publicId;

			}

			@Override
			public void setEncoding(String encoding) {
				// TODO Auto-generated method stub

			}

			@Override
			public void setCharacterStream(Reader characterStream) {
				this.characterStream = characterStream;

			}

			@Override
			public void setCertifiedText(boolean certifiedText) {
				this.certifiedText = certifiedText;
		//		This one is based on the premise that the included/imported schemas lies at the root of the classpath, and is relatively simple.
			}

			@Override
			public void setByteStream(InputStream byteStream) {
				this.byteStream = byteStream;

			}

			@Override
			public void setBaseURI(String baseURI) {
				this.baseURI = baseURI;

			}

			@Override
			public String getSystemId() {
				return systemId;
			}

			@Override
			public String getStringData() {
				return stringData;
			}

			@Override
			public String getPublicId() {
				return publicId;
			}

			@Override
			public String getEncoding() {
				return encoding;
			}

			@Override
			public Reader getCharacterStream() {
				return characterStream;
			}

			@Override
			public boolean getCertifiedText() {
				return certifiedText;
			}

			@Override
			public InputStream getByteStream() {
				return byteStream;
			}

			@Override
			public String getBaseURI() {
				return baseURI;
			}
		};

		InputStream stream = new ByteArrayInputStream(LinguagemController.getTextoPadrao(systemId).getBytes());

		input.setPublicId(publicId);
		input.setSystemId(systemId);
		input.setBaseURI(baseURI);
		input.setCharacterStream(new InputStreamReader(stream));

		return input;
	}
});
 */
package xslt;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class XSLTransformer {

	private String xslFile, inputFile, outputFile;
	private StreamSource xslCode, input;
	private StreamResult output;
	private TransformerFactory tf;
	private Transformer trans;
	
	// This method uses a xsl file to convert xml into html 
	public XSLTransformer(String inputFile, int numBattle) {
		super();
		
		this.xslFile = "./src/xslt/xslExample.xsl";
		this.inputFile = inputFile;
		this.outputFile = "./src/html_archives/battle" + numBattle + ".html";
		
		this.xslCode = new StreamSource(new File(this.xslFile));
		this.input = new StreamSource(new File(this.inputFile));
		this.output = new StreamResult(new File(this.outputFile));
		
		this.tf = TransformerFactory.newInstance();
		try {
			this.trans = tf.newTransformer(this.xslCode);
			trans.transform(input, output);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		
	}
}

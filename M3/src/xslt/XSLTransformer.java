package xslt;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import java.io.*;

public class XSLTransformer {

	private String xslFile, inputFile, outputFile;
	private StreamSource xslCode, input;
	private StreamResult output;
	private TransformerFactory tf;
	private Transformer trans;
	
	public XSLTransformer(String inputFile, String outputFile) {
		super();
		
		this.xslFile = "./src/xslt/xslExample.xsl";
		this.inputFile = inputFile;
		this.outputFile = outputFile;
		
		this.xslCode = new StreamSource(new File(this.xslFile));
		this.input = new StreamSource(new File(this.inputFile));
		this.output = new StreamResult(new File(this.outputFile));
		
		this.tf = TransformerFactory.newInstance();
		try {
			this.trans = tf.newTransformer(this.xslCode);
			trans.transform(input, output);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

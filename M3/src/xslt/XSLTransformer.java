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

	public static void main(String[] args) {
        try {
            int numeroBatalla = 7; // aqui se debe obtener el numero de la batalla 

            TransformerFactory factory = TransformerFactory.newInstance();
            Source xslt = new StreamSource(new File("src/xslt/xslExample.xsl"));
            Transformer transformer = factory.newTransformer(xslt);

            Source xml = new StreamSource(new File("src/xslt/input.xml"));
            File archivoTemporal = new File("src/xslt/output.html");
            transformer.transform(xml, new StreamResult(archivoTemporal));

            // aqui se hace la copia 
            File destino = new File("/home/super/VISUAL LDM/SpaceWarsWeb/" + numeroBatalla + ".html"); 
            Files.copy(archivoTemporal.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Path: " + destino.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
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

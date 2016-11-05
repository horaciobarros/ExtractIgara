package nfe.utilitarios;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import nfe.entidades.layoutnfse.TcCancelamentoNfse;
import nfe.entidades.layoutnfse.TcCompNfse;
import nfe.entidades.layoutnfse.TcInfNfse;
import nfe.entidades.layoutnfse.TcNfse;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BuildObjetcts {
	public static void main(String[] args){
		try {
			String xml = lerXML("c:\\tmp\\nfse_betim.xml");
//			TcCompNfse comp = getTcCompNfse(xml);
//			System.out.println(comp.getNfse().getInfNfse().getId());
//			TcInfNfse inf = getTcInfNfse(xml);
//			System.out.println(inf.getNumero());
			TcNfse nfs = getTcNfse(xml);
			System.out.println(nfs.getInfNfse().getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static TcNfse getTcNfse(String xml) throws SAXException, IOException, ParserConfigurationException, TransformerException{        
		try{
			//xml = xml.replace("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"", "xmlns=\"http://www.abrasf.org.br/ABRASF/arquivos/nfse.xsd\"");
			//xml = xml.replace("xmlns=\"http://betim.rps.com.br/sgm/zend/default/xsd/nfse?format=xml\"", "xmlns=\"http://www.abrasf.org.br/ABRASF/arquivos/nfse.xsd\"");
			//System.out.println(xml);
			Document documento = documentFactory(xml);
			NodeList nodeList = documento.getDocumentElement().getElementsByTagName("Nfse");
			String str = outputXML(nodeList.item(0));
			JAXBContext context = JAXBContext.newInstance(TcNfse.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();        
			TcNfse nfse = unmarshaller.unmarshal(new StreamSource(new StringReader(str)), TcNfse.class).getValue();        
			return nfse;
		}catch(JAXBException ex){
			ex.printStackTrace();
			return null;
		}
	}
	public static TcCancelamentoNfse getTcNfseCancelamento(String xml) {
		try{
			//xml = xml.replace("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"", "xmlns=\"http://www.abrasf.org.br/ABRASF/arquivos/nfse.xsd\"");
			//xml = xml.replace("xmlns=\"http://betim.rps.com.br/sgm/zend/default/xsd/nfse?format=xml\"", "xmlns=\"http://www.abrasf.org.br/ABRASF/arquivos/nfse.xsd\"");
			//System.out.println(xml);
			Document documento;
			try {
				documento = documentFactory(xml);
				NodeList nodeList = documento.getDocumentElement().getElementsByTagName("NfseCancelamento");
				String str = outputXML(nodeList.item(0));
				JAXBContext context = JAXBContext.newInstance(TcCancelamentoNfse.class);
				Unmarshaller unmarshaller = context.createUnmarshaller();        
				TcCancelamentoNfse nfseCancelamento = unmarshaller.unmarshal(new StreamSource(new StringReader(str)), 
						TcCancelamentoNfse.class).getValue();        
				return nfseCancelamento;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
			
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	public static TcInfNfse getTcInfNfse(String xml) throws SAXException, IOException, ParserConfigurationException, TransformerException{        
		try{
			Document documento = documentFactory(xml);
			NodeList nodeList = documento.getDocumentElement().getElementsByTagName("InfNfse");
			String str = outputXML(nodeList.item(0));
			JAXBContext context = JAXBContext.newInstance(TcInfNfse.class);
			
			Unmarshaller unmarshaller = context.createUnmarshaller();        
			TcInfNfse nfse = unmarshaller.unmarshal(new StreamSource(new StringReader(str)), TcInfNfse.class).getValue();        
			return nfse;
		}catch(JAXBException ex){
			ex.printStackTrace();
			return null;
		}
	}
	public static TcCompNfse getTcCompNfse(String xml){        
		try{
			JAXBContext context = JAXBContext.newInstance(TcCompNfse.class);        
			Unmarshaller unmarshaller = context.createUnmarshaller();        
			TcCompNfse nfse = unmarshaller.unmarshal(new StreamSource(new StringReader(xml)), TcCompNfse.class).getValue();        
			return nfse;
		}catch(JAXBException ex){
			ex.printStackTrace();
			return null;
		}
	}
	

	
	public static Document documentFactory(String xml) throws SAXException, IOException, ParserConfigurationException {  
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
		factory.setNamespaceAware(true);  
		Document document = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));  
		return document;  
	}


	public static String lerXML(String fileXML) throws IOException {  
		String linha = "";  
		StringBuilder xml = new StringBuilder();  

		BufferedReader in = new BufferedReader(new InputStreamReader(  
				new FileInputStream(fileXML)));  
		while ((linha = in.readLine()) != null) {  
			xml.append(linha);  
		}  
		in.close();  

		return xml.toString();  
	}
	public static String outputXML(Node node) throws TransformerException {  
		ByteArrayOutputStream os = new ByteArrayOutputStream();  
		TransformerFactory tf = TransformerFactory.newInstance();  
		Transformer trans = tf.newTransformer();
		trans.setOutputProperty(OutputKeys.METHOD, "xml");
		trans.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
		//trans.setOutputProperty(OutputKeys.INDENT, "yes");
		trans.transform(new DOMSource(node), new StreamResult(os));  
		String xml = os.toString();  
		if ((xml != null) && (!"".equals(xml))) {  
			xml = xml.replaceAll("<\\?xml version=\"1.0\" encoding=\"UTF-8\"\\?>", "");  
			xml = xml.replaceAll("<\\?xml version=\"1.0\"", "");
			xml = xml.replaceAll("encoding=\"ISO-8859-1\"\\?>", "");
		}  
		return xml;  
	}
	
}

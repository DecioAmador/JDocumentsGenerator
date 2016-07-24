package com.github.decioamador.jdocsgen.table;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

import com.github.decioamador.jdocsgen.Constants;
import com.github.decioamador.jdocsgen.JDocsGenException;

public class TableGeneratorJ {

	// https://docs.oracle.com/javase/tutorial/jaxp/
	// http://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/
	// http://www.tutorialspoint.com/jasper_reports/jasper_exporting_reports.htm

	private static final String LINK_JASPERREPORTS_SOURCEFORGE = "http://jasperreports.sourceforge.net/jasperreports";

	public static void xmlGenerate(final File file,final Class<?> clazz,final List<String> fields) throws JDocsGenException{
		try {
			final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			final Document doc = docBuilder.newDocument();
			final DOMImplementation docImpl = doc.getImplementation();
			
			final DocumentType doctype = docImpl.createDocumentType("jasperReport", 
					"//JasperReports//DTD Report Design//EN", 
					"http://jasperreports.sourceforge.net/dtds/jasperreport.dtd");
			
			final Element root = doc.createElement("jasperReport");
			root.setAttribute("xmlns", LINK_JASPERREPORTS_SOURCEFORGE);
			root.setAttribute("xmlns:xsi", LINK_JASPERREPORTS_SOURCEFORGE);
			root.setAttribute("xsi:schemaLocation", LINK_JASPERREPORTS_SOURCEFORGE
					+ " http://jasperreports.sourceforge.net/xsd/jasperreport.xsd");
			root.setAttribute("name", file.getName().substring(0,file.getName().indexOf('.')));
			doc.appendChild(root);
			
			putQuery(doc, root);
			putFields(clazz, fields, doc, root);
			
			final TransformerFactory transfFactory = TransformerFactory.newInstance();
			final Transformer transf = transfFactory.newTransformer();
			
			transf.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
			transf.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
			transf.setOutputProperty(OutputKeys.ENCODING, Constants.CHARSET);
			transf.setOutputProperty(OutputKeys.INDENT, "yes");
			
			final DOMSource source = new DOMSource(doc);
			final StreamResult result = new StreamResult(file);
			
			transf.transform(source, result);
		} catch (ParserConfigurationException |  TransformerException e) {
			throw new JDocsGenException("Unable to parse jrxml.", e);
		}
	}

	private static void putQuery(final Document doc, final Element root) {
		final Element elemQuery = doc.createElement("queryString");
		final CDATASection cdataQuery = doc.createCDATASection(" "); // TODO: Is this really necessary
		elemQuery.appendChild(cdataQuery);
		root.appendChild(elemQuery);
	}

	private static void putFields(final Class<?> clazz,final List<String> fields,final Document doc,final Element root)
			throws JDocsGenException {
		Element elemField, elemDescription;
		CDATASection cdata;
		
		Class<?> temp;
		Method method;
		
		for(final String field: fields){
			elemField = doc.createElement("field");
			elemField.setAttribute("name", field);
			
			try {
				method = clazz.getDeclaredMethod(Constants.GET+firstToUpperCase(field),
						Constants.EMPTY_ARRAY_CLASS);
				temp = method.getReturnType();
			} catch (NoSuchMethodException | SecurityException e) {
				throw new JDocsGenException(e);
			}
			elemField.setAttribute("class",temp.getCanonicalName());
			root.appendChild(elemField);
			
			elemDescription = doc.createElement("fieldDescription");
			cdata = doc.createCDATASection(field);
			elemDescription.appendChild(cdata);
			elemField.appendChild(elemDescription);
		}
	}
	
	private static String firstToUpperCase(final String string){
		final char [] c = string.toCharArray();
		c[0] = Character.toUpperCase(c[0]);
		return new String(c);
	}
	
}
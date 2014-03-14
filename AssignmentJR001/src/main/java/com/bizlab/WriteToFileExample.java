package com.bizlab;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WriteToFileExample {
	public static void main(String[] args) {

		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();

			Element rootElement = doc.createElement("category");
			doc.appendChild(rootElement);

			// set attribute to category element
			Attr attr1 = doc.createAttribute("id");
			attr1.setValue("cat101");
			rootElement.setAttributeNode(attr1);

			// set attribute to category element
			Attr attr2 = doc.createAttribute("name");
			attr2.setValue("Books and Papers");
			rootElement.setAttributeNode(attr2);

			// product elements

			for (long i = 1; i <= 800000; i++) {
				Element product = doc.createElement("Product");
				rootElement.appendChild(product);
				// set attribute to staff element
				Attr productAttr1 = doc.createAttribute("id");
				productAttr1.setValue("pid" + i);
				product.setAttributeNode(productAttr1);

				// set attribute to staff element
				Attr productAttr2 = doc.createAttribute("name");
				productAttr2.setValue("Product" + i);
				product.setAttributeNode(productAttr2);

				// set attribute to staff element
				Attr productAttr3 = doc.createAttribute("description");
				productAttr3.setValue("This is new product" + i + " in market");
				product.setAttributeNode(productAttr3);
			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			StreamResult result = new StreamResult(new File(
					"F:\\New folder\\test.xml"));
			transformer.transform(source, result);

			System.out.println("Done");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}

	}
}
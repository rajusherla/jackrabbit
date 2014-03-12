package com.bizlab;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;

import org.apache.jackrabbit.commons.cnd.CndImporter;
import org.apache.jackrabbit.core.TransientRepository;

public class JackrabbitTest {

	public static void main(String[] args) throws Exception {
		Repository repository = new TransientRepository();
		Session session = repository.login(new SimpleCredentials("admin",
				"admin".toCharArray()));
		InputStream is = null;
		BufferedReader bfrd = null;
		try {
			is = JackrabbitTest.class.getClassLoader().getResourceAsStream(
					"bizlabCat.cnd");
			bfrd = new BufferedReader(new InputStreamReader(is));
			CndImporter.registerNodeTypes(bfrd, session, true);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				is.close();
			}
			if (bfrd != null) {
				bfrd.close();
			}
		}

		try {

			Node root = session.getRootNode();
			if (!root.hasNode("bookstore")) {
				Node node = root.addNode("bookstore", "bizilab:typeCategory");
				InputStream stream = JackrabbitTest.class.getClassLoader()
						.getResourceAsStream("test.xml");
				session.importXML(node.getPath(), stream,
						ImportUUIDBehavior.IMPORT_UUID_CREATE_NEW);
				stream.close();
				//
				session.save();
				//
			} else {
				FileOutputStream ostream = new FileOutputStream(
						"F:\\New folder\\fulldump.xml");
				session.exportDocumentView("/bookstore", ostream, true, false);
				ostream.close();
			}

			Node savedNode = root.getNode("bookstore");
			dump(savedNode);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.logout();
			if (is != null) {
				is.close();
			}
			if (bfrd != null) {
				bfrd.close();
			}
		}
	}

	private static void dump(Node node) throws RepositoryException {
		// First output the node path
		System.out.println(node.getPath());
		// Skip the virtual (and large!) jcr:system subtree
		if (node.getName().equals("jcr:system")) {
			return;
		}

		// Then output the properties
		PropertyIterator properties = node.getProperties();
		while (properties.hasNext()) {
			Property property = properties.nextProperty();
			if (property.getDefinition().isMultiple()) {
				// A multi-valued property, print all values
				Value[] values = property.getValues();
				for (int i = 0; i < values.length; i++) {
					System.out.println(property.getPath() + " = "
							+ values[i].getString());
				}
			} else {
				// A single-valued property
				System.out.println(property.getPath() + " = "
						+ property.getString());
			}
		}

		// Finally output all the child nodes recursively
		NodeIterator nodes = node.getNodes();
		while (nodes.hasNext()) {
			dump(nodes.nextNode());
		}
	}

}

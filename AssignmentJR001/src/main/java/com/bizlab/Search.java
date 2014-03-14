package com.bizlab;

import java.util.Date;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.jackrabbit.core.TransientRepository;

public class Search {

	public static void main(String[] args) throws Exception {
		Repository repository = new TransientRepository();
		Session session = repository.login(new SimpleCredentials("admin",
				"admin".toCharArray()));

		try{
		
			// Node savedNode = root.getNode("bookstore");

			QueryManager manager = session.getWorkspace().getQueryManager();
			long lStartTime = new Date().getTime();
			Query query = manager.createQuery(
					"select * from [nt:base] where [name]='Product3'",
					Query.JCR_SQL2);
			System.out.println("query executing...............");
			QueryResult result = query.execute();
			NodeIterator nodes = result.getNodes();
			long lEndTime = new Date().getTime();
			long difference = lEndTime - lStartTime; // check different

			System.out.println("Elapsed time in milliseconds: " + difference);

			System.out.println("nodes size: " + nodes.getSize());
			while (nodes.hasNext()) {
				Node node = nodes.nextNode();
				Property property = node.getProperty("id");
				System.out.println("Matched: " + property.getString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.logout();
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

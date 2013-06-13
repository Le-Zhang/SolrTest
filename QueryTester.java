package solrTest.v1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.xml.sax.SAXException;

public class QueryTester {

	/**
	 * @param args
	 * @throws SolrServerException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws XMLStreamException 
	 */
	public static void main(String[] args) throws SolrServerException, IOException, ParserConfigurationException, SAXException, XMLStreamException {
		SolrServer solrServer = new HttpSolrServer("http://localhost:9090");
		FormQuery query = new FormQuery(solrServer,"/Users/zhangle/Documents/OpenRelavance/trec/results.txt");
		
		DataInput dinput = new DataInput(solrServer, "/Users/zhangle/Documents/OpenRelavance/trec/topics.txt");
		HashMap<String, Topic> qrelMap = dinput.getQrels();
		System.out.println(qrelMap.keySet());
		System.out.println(qrelMap.get("436"));
		HashSet<String> qrelSet = dinput.getQrelNum("/Users/zhangle/Documents/OpenRelavance/trec/qrels.txt");
		System.out.println(qrelSet);
		
		query.writeResults(qrelSet, qrelMap);
		
	}

}

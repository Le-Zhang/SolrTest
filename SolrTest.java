package solrTest.v1;

import java.io.IOException;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.xml.sax.SAXException;

public class SolrTest {

	/**
	 * @param args
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws SolrServerException 
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, ParseException, SolrServerException, ParserConfigurationException, SAXException {
		// TODO Auto-generated method stub
		SolrServer solrServer = new HttpSolrServer("http://localhost:9090");
		DataInput di = new DataInput(solrServer, "/Users/zhangle/solr/example/test-aquaint");
		di.indexFile();
	}

}

package solrTest.v1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

public class SolrTest {
	
	public static void main(String[] args) throws SolrServerException, IOException, ParseException {
		HttpSolrServer solrServer = new HttpSolrServer("http://localhost:9090");
//		
//		DataInput dataInput = new DataInput("file:///Users/zhangle/solr/example/aquaint-data/20000929_NYT.xml");
//		dataInput.parseXMLFile();
//		dataInput.parseDocument();
//		List<SolrBean> solrBeans = dataInput.getBeanList();
//		System.out.println(solrBeans.size());

		
		ReadFile rf = new ReadFile("/Users/zhangle/solr/example/aquaint-data/20000929_NYT.xml","DOCS");
		rf.setHeader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
"<!DOCTYPE DOC SYSTEM \"text.dtd\">");
		String output = rf.putTag();
		//System.out.println(output);
		ByteArrayInputStream inputstream = new ByteArrayInputStream(output.getBytes());
		DataInput di = new DataInput(inputstream);
		di.parseXMLFile();
		di.parseDocument();
		
		List<SolrBean> solrBeans = di.getBeanList();
		System.out.println(solrBeans.size());
		solrServer.addBeans(solrBeans);
		solrServer.commit();
			
	}
	
	
	

}

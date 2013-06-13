package solrTest.v1;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

public class FormQuery {

	private SolrServer solrServer;
	private String results;
	
	public FormQuery(SolrServer solrServer, String results) {
		this.solrServer = solrServer;
		this.results = results;
	}
	
	public SolrDocumentList solrQuery(String num, HashMap<String,Topic> qrelMap) throws SolrServerException {
		
		if(qrelMap.get(num) != null){
			String qrel = qrelMap.get(num).getTitle();
			System.out.println(qrel);
			SolrQuery params = new SolrQuery();
			
			params.setFields("docno","score");
			params.set("defType", "dismax");
			params.set("q", qrel);
			//params.set("qf", "body^10 text^10");
			params.set("sort", "score desc");
			
			params.setRows(100);
		
			QueryResponse response = solrServer.query(params);
			if(response == null) 
				throw new RuntimeException("Shouldn't be null");
		
			return response.getResults();
		} else {
			throw new RuntimeException("Title shouldn't be null");
		}
		
		
	}
	
	public void writeResults(HashSet<String> qrelSet, HashMap<String,Topic> qrelMap)
			throws SolrServerException, FileNotFoundException{
		PrintWriter writer = new PrintWriter(results);
		for(String id : qrelSet){
			SolrDocumentList docList = solrQuery(id, qrelMap);
			//System.out.println(docList);
			for(int i = 0; i < docList.size(); i ++){
				String docno = (String)docList.get(i).getFieldValue("docno");
				Float score = (Float)docList.get(i).getFieldValue("score");
				writer.printf("%s\t%s\t%s\t%d\t%f\t%s\n", id, "Q0", docno, i, score, "STANDARD" );
			}
		}

		writer.close();
	}
	
	
	
	
	
	
	
	
	

}

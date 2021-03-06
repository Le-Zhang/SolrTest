package solrTest.v1;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

//import javax.xml.bind.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class DataInput {
	
	private SolrServer solrServer;
	private String dataPath;


	
	private DateFormat format;
	
	public DataInput(SolrServer solrServer, String dataPath) {
		this.solrServer = solrServer;
		this.dataPath = dataPath;
		setDateFormat("");
	}
	
	
	
	/**
	 * Read files from the given folder path. Get solr bean list and add the list to solr server.
	 * @throws IOException
	 * @throws ParseException
	 * @throws SolrServerException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public void indexFile() throws IOException, ParseException, SolrServerException, ParserConfigurationException, SAXException {
		File folder = new File(dataPath);
		
		if(folder.isDirectory()) {
			File[] fileList = folder.listFiles();
			System.out.println(fileList.length);
			for(int i=0; i< fileList.length; i++) {
				if(!fileList[i].isHidden()){
					//format = getDateFormat(fileList[i].getName());
					//System.out.println(format.toString());
				    List<SolrBean> sbs = getDocs(fileList[i]);	
					
					
				    solrServer.addBeans(sbs);
				    solrServer.commit();
				    System.out.println("I have done " + i + " times");
		
			}
		
		}
			
	}
	}
	
	/**
	 * Setting header and tag to get string list. Parse each string using DOM, to get a SolrBean instance and add it to the list
	 * @param file
	 * @return SolrBean List
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws ParseException
	 */
	public List<SolrBean> getDocs(File file) throws IOException, ParserConfigurationException, SAXException, ParseException {
		List<SolrBean> sbs = new ArrayList<SolrBean>();
		
		ReadFile rf = new ReadFile(file);
		rf.setHeader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
"<!DOCTYPE DOC SYSTEM \"text.dtd\">");
		rf.setTag("DOC");
		List<String> docList = rf.seperateDocs();
		//System.out.println(output)
		
		for(String doc:docList){
			
			ByteArrayInputStream inputstream = new ByteArrayInputStream(doc.getBytes());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			if(inputstream!=null){
				Document dom = db.parse(inputstream);
				SolrBean solrBean = getDocBean(dom);
				sbs.add(solrBean);
			} 
		
	}
		return sbs;
	}
	
	public DateFormat getDateFormat(String date){
		if(date.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}[ \t].*[0-9]{2}:[0-9]{2}"))
			setDateFormat("yyyy-MM-dd hh:mm");
		else if(date.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}[ \t].*[0-9]{2}:[0-9]{2}:[0-9]{2}"))
			setDateFormat("yyyy-MM-dd hh:mm:ss");
		else if(date.matches("[0-9]{2}\\/[0-9]{2}\\/[0-9]{4}[ \t].*[0-9]{2}:[0-9]{2}"))
			setDateFormat("MM/dd/yyyy hh:mm");
		else if(date.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}"))
			setDateFormat("yyyy-MM-dd");

		return format;
	}
	private void setDateFormat(String f){
		format = new SimpleDateFormat(f);
	}
		
		
		
		
		/**
		 * Take a DOC element and read the values in. 
		 * Create an SolrBean object and set field value of SolrBean
		 * @param doc element
		 * @return a SolrBean with set value
		 * @throws ParseException 
		 */
		
		public SolrBean getDocBean(Document dom) throws ParseException {

			String string_date_time;
			Date date_time;

			SolrBean sb = new SolrBean();
			
			if(dom.getElementsByTagName("DOCNO").item(0).getFirstChild() != null)
				sb.setDocNO(dom.getElementsByTagName("DOCNO").item(0).getFirstChild().getNodeValue());
			
			if(dom.getElementsByTagName("DOCTYPE").getLength() == 0){
				sb.setDocType("");
			} else {
				sb.setDocType(dom.getElementsByTagName("DOCTYPE").item(0).getFirstChild().getNodeValue());
			}
			
//			System.out.println(dom.getElementsByTagName("DOCTYPE").getLength());
//			if(dom.getElementsByTagName("DOCTYPE").getLength() != 0 ||dom.getElementsByTagName("DOCTYPE").item(0).getFirstChild() != null || dom.getElementsByTagName("DOCTYPE") != null) {
//				docType = dom.getElementsByTagName("DOCTYPE").item(0).getFirstChild().getNodeValue();
//			} else if(dom.getElementsByTagName("DOCTYPE").getLength() == 0) {
//				docType = "";
//			}
//			System.out.println(dom.getElementsByTagName("DATE_TIME").getLength());
			if(dom.getElementsByTagName("DATE_TIME").getLength() !=0 &&dom.getElementsByTagName("DATE_TIME").item(0).getFirstChild() != null){
				string_date_time = dom.getElementsByTagName("DATE_TIME").item(0).getFirstChild().getNodeValue();
				if(string_date_time.equals(" no timestamp ")) {
					sb.setDate_time(new Date());
				} else {
					format = getDateFormat(string_date_time.trim());
					date_time = format.parse(string_date_time);
					sb.setDate_time(date_time);
				}
			}
			
			if(dom.getElementsByTagName("HEADER").getLength() == 0 ||dom.getElementsByTagName("HEADER").item(0) == null){
				sb.setHeader("");
			}else {
				sb.setHeader(dom.getElementsByTagName("HEADER").item(0).getFirstChild().getNodeValue());
			}
			
			if(dom.getElementsByTagName("SLUG").getLength() == 0||dom.getElementsByTagName("SLUG").item(0).getFirstChild() == null) {
				sb.setSlug("");
			} else {
				sb.setSlug(dom.getElementsByTagName("SLUG").item(0).getFirstChild().getNodeValue());
			}
			
			if(dom.getElementsByTagName("HEADLINE").getLength() == 0 ||dom.getElementsByTagName("HEADLINE").item(0).getFirstChild() == null) {
				sb.setHeadLine("");
			} else {
//				System.out.println(dom.getElementsByTagName("HEADLINE").getLength());
//				System.out.println(dom.getElementsByTagName("HEADLINE").item(0));
//				System.out.println(dom.getElementsByTagName("HEADLINE").item(0).getFirstChild());
				sb.setHeadLine(dom.getElementsByTagName("HEADLINE").item(0).getFirstChild().getNodeValue());
			}
			
			if(dom.getElementsByTagName("TEXT").getLength() == 0||dom.getElementsByTagName("TEXT").item(0).getFirstChild() == null) {
				sb.setContent("");
			} else {
				sb.setContent(dom.getElementsByTagName("TEXT").item(0).getTextContent());
			}
			
			if(dom.getElementsByTagName("TRAILER").getLength() != 0 && dom.getElementsByTagName("TRAILER").item(0).getFirstChild() != null){
				sb.setTrailer(dom.getElementsByTagName("TRAILER").item(0).getFirstChild().getNodeValue());
			} else {
				sb.setTrailer("");
			}
			
			return sb;
		}
		
		/**
		 * Create a Topic object and set field of topic
		 * @param dom
		 * @return a Topic object with set value
		 * @throws ParserConfigurationException
		 * @throws SAXException
		 * @throws IOException
		 * @throws XMLStreamException
		 */
		public Topic parseTopic(Document dom) throws ParserConfigurationException, SAXException, IOException, XMLStreamException {
			Topic topic = new Topic();
			
			if(dom.getElementsByTagName("num").item(0).getFirstChild() != null){
				String[] nums = dom.getElementsByTagName("num").item(0).getFirstChild().getNodeValue().split(": ");
				topic.setDocnum(nums[1]);
			}
			if(dom.getElementsByTagName("title").item(0).getFirstChild() != null)
				topic.setTitle(dom.getElementsByTagName("title").item(0).getFirstChild().getNodeValue());
			if(dom.getElementsByTagName("desc").item(0).getFirstChild() != null)
				topic.setDesc(dom.getElementsByTagName("desc").item(0).getFirstChild().getNodeValue());
			if(dom.getElementsByTagName("narr").item(0).getFirstChild() != null)
				topic.setNarr(dom.getElementsByTagName("narr").item(0).getFirstChild().getNodeValue());

			return topic;
			
		}
		
		/**
		 * dataPath is the path of topics.txt
		 * @return HashMap<String,Topic>
		 * @throws ParserConfigurationException
		 * @throws SAXException
		 * @throws IOException
		 * @throws XMLStreamException
		 */
		public HashMap<String,Topic> getQrels() throws ParserConfigurationException, SAXException, IOException, XMLStreamException {
			File file = new File(dataPath);
			
			HashMap<String,Topic> qrelMap = new HashMap<String,Topic>();
			
			ReadFile rf = new ReadFile(file);
			rf.setHeader("");
			rf.setTag("top");
			List<String> docList = rf.seperateDocs();
			//System.out.println(output);
			
			for(String doc:docList){
				
				ByteArrayInputStream inputstream = new ByteArrayInputStream(doc.getBytes());
				//DataInput di = new DataInput(inputstream);
				//di.parseXMLFile(inputstream);
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				
				if(inputstream!=null){
					Document dom = db.parse(inputstream);
					Topic t = parseTopic(dom);
					qrelMap.put(t.getDocnum(), t);
				
				}
		}
			return qrelMap;
		}
		
		/**
		 * @param qrelPath (Path of qrels.txt)
		 * @return HashSet<String> the DocNo field of qrels in qrels.txt
		 * @throws IOException
		 */
		public HashSet<String> getQrelNum(String qrelPath) throws IOException {
			File qrelFile = new File(qrelPath);
			HashSet<String> qrelSet = new HashSet<String>();
			BufferedReader br = new BufferedReader(new FileReader(qrelFile));
			
			//String firstNum = null;
			while(br.ready()&&br.readLine() != null) {
				String[] delims = br.readLine().split(" ");
				//if(delims[0] != firstNum) {
					 qrelSet.add(delims[0]);
				//}
			}
			
			br.close();
			return qrelSet;
			
		}
		

	

}

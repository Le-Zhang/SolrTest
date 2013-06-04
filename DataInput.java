package solrTest.v1;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import javax.xml.bind.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class DataInput {
	
	Document dom;
	List<SolrBean> myDocBeans;
	InputStream is;
	String docNo;
	String docType;
	String string_date_time;
	Date date_time;
	String header;
	String slug;
	String headLine;
	String text;
	String trailer;
	
	public DataInput(InputStream is) {
		this.is = is;
		dom = null;
		myDocBeans = new ArrayList<SolrBean>();
		
		this.docNo = null;
		this.docType = null;
		this.string_date_time = null;
		this.date_time = null;
		this.header = null;
		this.slug = null;
		this.headLine = null;
		this.text = null;
		this.trailer = null;
	}
	
	public void parseXMLFile() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				
				//parse using builder to get DOM representation of the XML file
				//InputStream is
				dom = db.parse(is);
				
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
		
		
		
		/**
		 * Take a DOC element and read the values in. 
		 * Create an DOC(SolrBean) object and set field value of SolrBean
		 * @param doc element
		 * @return a SolrBean with set value
		 * @throws ParseException 
		 */
		
		public SolrBean getDocBean() throws ParseException {
			//for each <DOC> element get text or data value of header, docno, doc type, date_time
			//slug, headline, text and trailer
			
			//for(int i=0; i<dom.getElementsByTagName("DOCNO").getLength(); i++) {
			
//			String docNo;
//			String docType;
//			String string_date_time = null;
//			Date date_time;
//			String header;
//			String slug;
//			String headLine;
//			String text;
//			String trailer;
			
			if(dom.getElementsByTagName("DOCNO").item(0).getFirstChild() != null)
				docNo = dom.getElementsByTagName("DOCNO").item(0).getFirstChild().getNodeValue();
			if(dom.getElementsByTagName("DOCTYPE").item(0).getFirstChild() != null)
				docType = dom.getElementsByTagName("DOCTYPE").item(0).getFirstChild().getNodeValue();
			if(dom.getElementsByTagName("DATE_TIME").item(0).getFirstChild() != null)
				string_date_time = dom.getElementsByTagName("DATE_TIME").item(0).getFirstChild().getNodeValue();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm");
				date_time = sdf.parse(string_date_time);
			if(dom.getElementsByTagName("HEADER").item(0).getFirstChild() != null)
				header = dom.getElementsByTagName("HEADER").item(0).getFirstChild().getNodeValue();
			if(dom.getElementsByTagName("SLUG").item(0) != null)
				slug = dom.getElementsByTagName("SLUG").item(0).getFirstChild().getNodeValue();
			if(dom.getElementsByTagName("HEADLINE").item(0) != null
					&& dom.getElementsByTagName("HEADLINE").item(0).getFirstChild() != null)
				headLine = dom.getElementsByTagName("HEADLINE").item(0).getFirstChild().getNodeValue();
			if(dom.getElementsByTagName("TEXT").item(0).getFirstChild() != null)
				text = dom.getElementsByTagName("TEXT").item(0).getFirstChild().getNodeValue();
			if(dom.getElementsByTagName("TRAILER").item(0).getFirstChild() != null)
				trailer = dom.getElementsByTagName("TRAILER").item(0).getFirstChild().getNodeValue();
			
			SolrBean sb = new SolrBean();
			sb.setDocNO(docNo);
			sb.setDocType(docType);
			sb.setDate_time(date_time);
			sb.setHeader(header);
			sb.setSlug(slug);
			sb.setHeadLine(headLine);
			sb.setContent(text);
			sb.setTrailer(trailer);
			
			//myDocBeans.add(sb);
			//}
			
			return sb;
		}
		

		
		public List<SolrBean> getBeanList() {
			return myDocBeans;
		}
		
		public Document getDOM() {
			return dom;
		}
	

}

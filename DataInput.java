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
	
	public DataInput(InputStream is) {
		this.is = is;
		dom = null;
		myDocBeans = new ArrayList<SolrBean>();
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
		
		public void parseDocument() throws ParseException {
			//get the root element
			Element docEle = (Element) dom.getDocumentElement();
			
			//get a nodelist of elements
			NodeList nl = docEle.getElementsByTagName("DOC");
			//int count = nl.getLength();
			if(nl != null && nl.getLength() > 0) {
					for(int i = 0; i<nl.getLength(); i++) {
						//get the document element
						Element el = (Element)nl.item(i);
						
						//get the Doc object
						SolrBean d = getDocBean(el);
						
						//add it to list
						myDocBeans.add(d);
					}
			}
		}
		
		
		/**
		 * Take a DOC element and read the values in. 
		 * Create an DOC(SolrBean) object and set field value of SolrBean
		 * @param doc element
		 * @return a SolrBean with set value
		 * @throws ParseException 
		 */
		
		private SolrBean getDocBean(Element doc) throws ParseException {
			//for each <DOC> element get text or data value of header, docno, doc type, date_time
			//slug, headline, text and trailer
			
			String docNo = getTextValue(doc, "DOCNO");
			String docType = getTextValue(doc, "DOCTYPE");
			
			String string_date_time = getTextValue(doc, "DATE_TIME");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm");
			Date date_time = sdf.parse(string_date_time);
			
			String header = getTextValue(doc,"HEADER");
			String slug = getTextValue(doc, "SLUG");
			String headLine = getTextValue(doc, "HEADLINE");
			String text = getTextValue(doc, "TEXT");
			String trailer = getTextValue(doc, "TRAILER");
			
			SolrBean sb = new SolrBean();
			sb.setDocNO(docNo);
			sb.setDocType(docType);
			sb.setDate_time(date_time);
			sb.setHeader(header);
			sb.setSlug(slug);
			sb.setHeadLine(headLine);
			sb.setContent(text);
			sb.setTrailer(trailer);
			
			return sb;
			
		}
		
		private String getTextValue(Element ele, String tagName) {
			String textVal = null;
			NodeList nl = ele.getElementsByTagName(tagName);
			if(nl !=null && nl.getLength() > 0) {
				Element el = (Element)nl.item(0);
				textVal = el.getFirstChild().getNodeValue();
			}
			
			return textVal;
		}
		
		public List<SolrBean> getBeanList() {
			return myDocBeans;
		}
		
		public Document getDOM() {
			return dom;
		}
	

}

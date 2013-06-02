package solrTest.v1;

import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;

public class SolrBean {
	
	@Field("DOC")
	String doc;
	
	@Field("DOCNO")
	String docNO;
	
	@Field("DOCTYPE")
	String docType;
	
	@Field("DATE-TIME")
	Date date_time;
	
	@Field("HEADER")
	String header;
	
	//@Field("BODY")
	//String bodyContent;
	
	@Field("SLUG")
	String slug;
	
	@Field("HEADLINE")
	String headLine;
	
	@Field("TEXT")
	String content;
	
	@Field("TRAILER")
	String trailer;

	public String getDoc() {
		return doc;
	}

	public void setDoc(String doc) {
		this.doc = doc;
	}

	public String getDocNO() {
		return docNO;
	}

	public void setDocNO(String docNO) {
		this.docNO = docNO;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public Date getDate_time() {
		return date_time;
	}

	public void setDate_time(Date date_time) {
		this.date_time = date_time;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

//	public String getBodyContent() {
//		return bodyContent;
//	}
//
//	public void setBodyContent(String bodyContent) {
//		this.bodyContent = bodyContent;
//	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getHeadLine() {
		return headLine;
	}

	public void setHeadLine(String headLine) {
		this.headLine = headLine;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTrailer() {
		return trailer;
	}

	public void setTrailer(String trailer) {
		this.trailer = trailer;
	}
	

}

package solrTest.v1;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ReadFile {

	File file;
	
	String header;
	String tag;
	List<String> docList;
	HashSet<String> qrelSet;
	String num;
	String title;
	Document topicDOM;
	InputStream is;
	
	public ReadFile(File file){
		this.file = file;
		header = null;
		docList = new ArrayList<String>();
		
		this.num = null;
		this.title = null;
		
	}
		
	public List<String> seperateDocs() throws IOException {
		String doc = new String(header); 
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		boolean isDocStart = false;
		
		while(br.ready()){
			String line = br.readLine();
			if(line.equals("<"+tag+">"))
				isDocStart = true;
			else if(line.equals("</"+tag+">")){
				isDocStart = false;
				doc += line;
				docList.add(doc);
				doc = new String(header);
			}
			
			if(isDocStart)
				doc += line;
		}
		
		br.close();
		
		return docList;
		//boolean isDocStart = false;
		
		
		
//		while(br.ready()) {
//			String line = br.readLine();
//			System.out.println(line);
//			firstLine += line;
//		}
//		
//		firstLine += lastLine;
//		br.close();
//		return firstLine;
	
	}
	
	
	public void setHeader(String header) {
		this.header = header;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	
	


}

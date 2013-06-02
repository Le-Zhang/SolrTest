package solrTest.v1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {

	String path;
	String header;
	String tag;
	
	public ReadFile(String path, String tag){
		this.path = path;
		header = null;
		this.tag = tag;
	}
	
	
	
	public String putTag() throws IOException, FileNotFoundException {
		String file = new String( header + "\n" + "<" + tag+ ">"); 
		String lastline = new String("</" + tag + ">");  //<tag> is a new wrapper tag around docs. eg. <WRAPPER>
		
		BufferedReader br = new BufferedReader(new FileReader(new File(path)));
		//boolean isDocStart = false;
		
		
		
		while(br.ready()) {
			String line = br.readLine();
			file += line;
		}
		
		file += lastline;
		br.close();
		return file;
	
	}
	
	public void setTag(String tagName) {
		this.tag = tagName;
	}
	
	public void setHeader(String header) {
		this.header = header;
	}

}

package custom.ssl.tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class EncryptFile {

	public static void encrypt(String data) {
		
		String FILENAME = "file.encrypted";	
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			File file = new File(FILENAME);
			if (!file.exists()) {
				file.createNewFile();
			}
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			String enc;
			try {
				enc = BCEncrypterDecrypter.encrypt(data, "opalou913");
				bw.write(enc+"\n");
			} catch (Exception e) {
				e.printStackTrace();
			}						
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
				if (fw != null) {
					fw.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		
		BufferedReader br = null;
        FileReader fr = null;
        try {
        	fr = new FileReader(args[0]);
        	br = new BufferedReader(fr);
        	String sCurrentLine;
        	while ((sCurrentLine = br.readLine()) != null) {
        		try {
        			encrypt(sCurrentLine);
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
        	}
        } catch (IOException e) {
        	e.printStackTrace();
        } finally {
        	try {
        		if (br != null) {
        			br.close();
        			if (fr != null) {
        				fr.close();
        			}
        		}
        	} catch (IOException ex) {
        		ex.printStackTrace();
        	}
        }
	}

}
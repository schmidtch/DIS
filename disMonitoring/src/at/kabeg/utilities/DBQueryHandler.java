package at.kabeg.utilities;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class DBQueryHandler {

	private String file = this.getClass().getResource("../../../../../queries.csv").getPath();
	private static BufferedReader br = null;
	
	private DBQueryHandler(){
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			System.err.println("FILE NOT FOUNT!!! "+e.getMessage());
		}
	}
	
	public static String getQuery(String queryName){
		String line = "", query="";
		new DBQueryHandler();
		try {
			line = br.readLine();
			while(line!=null){
				if(line.startsWith(queryName)){
					String[] ar = line.split(";",-1);
					query = ar[1];
					break;
				}
				line=br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return query;
	}
	
}

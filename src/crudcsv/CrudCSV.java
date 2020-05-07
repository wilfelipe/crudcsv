package crudcsv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CrudCSV {
	private String path;
	private String delimiter;
	private DataFrame df;
	public String[] columns;

	public CrudCSV(String path) {
		this(path, ",");
	}
		
	public CrudCSV(String path, String delimiter) {
		this.path = path;
		this.delimiter = delimiter;
		this.df = new DataFrame(this.path, delimiter);
		this.columns = df.columns;
	}
		
	public Boolean insert(String[] value) {
		int id = autoIncrement();
		if (df.countCols() == value.length+1) {
			df.values.put(id, (id + delimiter + df.arrayToCSV(value)).split(delimiter));
			return df.to_csv(this.path);
		}	
		return false;
		
	}

	public Boolean alter(int id, String column, String value) {
		String[] row = search(this.columns[0], id).get(0);
		for (int i = 0; i < this.columns.length; i++) {
			if (this.columns[i].equals(column)) {
				row[i] = value;
			}
		}
		return alter(id, Arrays.copyOfRange(row, 1, row.length));
	}
	
	public Boolean alter(int id, String[] value) {
		return (delete(id) && insert(value)) ? true : false;
	}
	
	public Boolean delete(int id) {		
		if(df.values.remove(id) == null)
			return false;
		return df.to_csv(this.path);
		
	}
	
	
	public Map<Integer, String[]> search(String column, String value) {
									
		Map<Integer, String[]> resultados = new HashMap<Integer, String[]>();

		for (String[] row : df.values.values())
			if (row[df.columnsMap.get(column)].matches(value)) { 
				resultados.put(resultados.size(), row);
			}
		
		return resultados;
	}	
	
	
	public Map<Integer, String[]> search(String column, int value) {
		return search(column, Integer.toString(value));
	}
	
	
	private int autoIncrement() {
		int id = -1;
		for (int key : df.values.keySet())
			if (key > id)
				id = key;
		return id + 1;
	}


}

class DataFrame {
	
	public Map<String,Integer> columnsMap;
	public Map<Integer,String[]> values;
	public String[] columns;
	private String delimiter;
		
	public DataFrame(String path, String delimiter) {
		
		this.delimiter = delimiter;
		
		File file = new File(path);
		values = new HashMap<Integer,String[]>();
		columnsMap = new HashMap<String,Integer>();
				
		try (Scanner inputStream = new Scanner(file)) {
			String data = inputStream.nextLine();
			String [] dados = data.split(this.delimiter);
			columns = dados;
			for (int i = 0; i < dados.length; i++) {
				columnsMap.put(dados[i], i);
			}
			
						
			String [] row;
			while(inputStream.hasNext()) {
				data = inputStream.nextLine();
				row = data.split(delimiter);			
				values.put(Integer.parseInt(row[0]), row);
			}
								
		} catch(IOException e){
			e.printStackTrace();
		}			
	}

	public boolean to_csv(String path) {
		
		String r = arrayToCSV(columns);
		for (String[] row : values.values()) 
			r = r + arrayToCSV(row);
		
		Writer output;
		try {
			output = new BufferedWriter(new FileWriter(path));
			output.append(r);
			output.close();
			return true;
		} catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public String arrayToCSV(String[] arr) {
		String r = "";
		for (Object value : arr)
			r = r + value + this.delimiter;
		return r.substring(0, r.length() - 1) + "\n";
	}

	public int countRows() {
		return values.size();
	}
	
	public int countCols() {
		return columnsMap.values().size();
	}
			
}

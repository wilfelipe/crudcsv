# crud-csv-java

Installation
============
This library read, search, alter and delete data from a CSV files.

Usage
=====
To use this library import download de Jar file and import in your project, or clone the source code in your project folder. And import the classes::

    import crudcsv.CrudCSV;
    import java.util.Map;


Open the file:

    // CrudCSV(String path, String delimiter)
    // CrudCSV(String path)
    
    CrudCSV db = new CrudCSV("data1.csv");
    
To search in the file and print all results (by default, the first column are the id column):
    
    // Map<Integer, String[]> search(String column, String value)
    // Map<Integer, String[]> search(String column, int value)
    
    Map<Integer,String[]> results = db.search("first_name", "Phil");
    for (String[] result : results.values()){
      System.out.println(Arrays.toString(result));
    }
      
 
Inserting one row (by default, the id column are AUTO_INCREMENT):
    
    // Boolean insert(String[] value)
    
    String[] values = {"Jason", "Heinlen", "heinlenjas@example.com", "Male"}; 
    db.insert(values)
      
       
Modifying a existing row:
  
    // Boolean alter(int id, String[] value) 
    // Boolean alter(int id, String column, String value)
    
    db.alter(1002, "email", "JasonH@example.com"))
 
 
To delete a row:

    // Boolean delete(int id)
    
    db.delete(1002);

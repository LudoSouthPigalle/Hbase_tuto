package com.microsoft.examples;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

public class InsertData{

   public static void main(String[] args) throws IOException {

      // Instantiating Configuration class
      Configuration config = HBaseConfiguration.create();

      // define some employees
      String[][] employee = {
        { "1", "Sacha", "Hert", "Manager"},
        { "2", "Bernard", "Spark", "Worker" },
        { "3", "Donald", "Mouse", "Driver" },
        { "4", "Kylian", "Pauleta", "Manager" },
        { "5", "Jane", "Rowling", "Wizzard"},
        { "6", "Lauren", "Bale", "Lower"} };

        // Instantiating HTable class
      HTable table = new HTable(config, "employee");
     
        // Add each employee to the table using add()
      for (int i = 0; i< employee.length; i++) {
        Put emp = new Put(Bytes.toBytes(employee[i][0]));
        emp.add(Bytes.toBytes("name"), Bytes.toBytes("first"), Bytes.toBytes(employee[i][1]));
        emp.add(Bytes.toBytes("name"), Bytes.toBytes("last"), Bytes.toBytes(employee[i][2]));
        emp.add(Bytes.toBytes("job"), Bytes.toBytes("profession"), Bytes.toBytes(employee[i][3]));
        // Saving the put Instance to the HTable.
        table.put(emp);
    }      
      System.out.println("data inserted");
      
      // closing table
      table.close();
   }
}

package com.microsoft.examples;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public class ReadData{

   public static void main(String[] args) throws IOException, Exception{
   
      // Instantiating Configuration class
      Configuration config = HBaseConfiguration.create();

      // Instantiating HTable class
      HTable table = new HTable(config, "employee");

      // Instantiating Get class
      Get get = new Get(Bytes.toBytes("1"));

      // Reading the data
      Result result = table.get(get);

      // Reading values from Result class object
      byte [] value1 = result.getValue(Bytes.toBytes("name"),Bytes.toBytes("first"));

      byte [] value2 = result.getValue(Bytes.toBytes("name"),Bytes.toBytes("last"));

      byte [] value3 = result.getValue(Bytes.toBytes("job"),Bytes.toBytes("profession"));
      
      System.out.println("first name: " + Bytes.toString(value1) + " last name: " + Bytes.toString(value2) + " profession: " + Bytes.toString(value3));
   }
}

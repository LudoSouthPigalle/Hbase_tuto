    package com.microsoft.examples;
    import java.io.IOException;
    import org.apache.hadoop.hbase.HBaseConfiguration;
    import org.apache.hadoop.hbase.HColumnDescriptor;
    import org.apache.hadoop.hbase.HTableDescriptor;
    import org.apache.hadoop.hbase.client.HBaseAdmin;
    import org.apache.hadoop.hbase.TableName;
    import org.apache.hadoop.conf.Configuration;

    public class CreateTable {

    public static void main(String[] args) throws IOException {

      // Instantiating configuration class
      Configuration config = HBaseConfiguration.create();

      // Instantiating HbaseAdmin class
      HBaseAdmin admin = new HBaseAdmin(config);

      // Instantiating table descriptor class
     HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf("employee"));

      // Adding column families to table descriptor
      tableDescriptor.addFamily(new HColumnDescriptor("name"));
      tableDescriptor.addFamily(new HColumnDescriptor("job"));

      // Execute the table through admin
      admin.createTable(tableDescriptor);

      System.out.println(" Table created ");
}
}

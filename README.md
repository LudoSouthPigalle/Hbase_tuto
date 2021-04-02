--
title: Hbase Lab
section: big-data
sort: 30
---

# Lab: Hbase

## Goals

- Create a project in order to create a table in Hbase using a Java application
- Use Maven to handle the project
- Create some classes in Java to read/write in Hbase
- Write unit testing for Hbase

## Creating and setting a Maven project

1. Create your Maven project called hbaseproject:
   ```sh
    mvn archetype:generate -DgroupId=com.adaltas.examples -DartifactId=hbaseproject -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
    ```
   This command creates a directory named **hbaseproject** at the current location, which contains a base Maven project. This directory contains:
   - `pom.xml`: the [Project Object Model](https://maven.apache.org/guides/introduction/introduction-to-the-pom.html) contains the information and configuration used to generate the project.
   - `src\main\java\com\adaltas\examples`: contains your application code.
   - `src\test\java\com\adaltas\examples`: contains tests for your application.
2. Create the `conf` directory:
   ```sh
   cd hbaseproject
   mkdir conf
   ```
   It will be used later for the configuration.
3. Add dependencies to the `pom.xml` file by typing `nano pom.xml` and inside the `<depedencies>` section add:
   ```xml
   <dependency>
    <groupId>org.apache.hbase</groupId>
    <artifactId>hbase-shaded-client</artifactId>
    <version>1.1.2</version>
   </dependency>
   <dependency>
    <groupId>org.apache.phoenix</groupId>
    <artifactId>phoenix-core</artifactId>
    <version>4.14.1-HBase-1.1</version>
    </dependency>
    ```
    This indicates that the project needs the components `hbase-client` et `phoenix-core`.
4. Add Maven plug-ins to the `pom.xml` between `</dependencies>` and `</project>`:
   ```xml
   <build>
    <sourceDirectory>src</sourceDirectory>
    <resources>
    <resource>
        <directory>${basedir}/conf</directory>
        <filtering>false</filtering>
        <includes>
        <include>hbase-site.xml</include>
        </includes>
    </resource>
    </resources>
    <plugins>
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
        <configuration>
            <source>1.8</source>
            <target>1.8</target>
        </configuration>
        </plugin>
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <version>3.2.1</version>
        <configuration>
        <transformers>
            <transformer implementation="org.apache.maven.plugins.shade.resource.ApacheLicenseResourceTransformer">
            </transformer>
        </transformers>
        </configuration>
        <executions>
        <execution>
            <phase>package</phase>
            <goals>
            <goal>shade</goal>
            </goals>
        </execution>
        </executions>
    </plugin>
    </plugins>
   </build>
   ```
   This section contains setting information for Hbase and setup [Apache Maven Compiler Plugin](https://maven.apache.org/plugins/maven-compiler-plugin/) and [Apache Maven Shade Plugin](https://maven.apache.org/plugins/maven-shade-plugin/).
5. Download the `hbase-site.xml` inside the `conf` directory that you created before by typing:
   ```sh
   scp user@edge-1.au.adaltas.cloud:/etc/hbase/conf/hbase-site.xml ./conf/hbase-site.xml
   ```
   this will copy the HBase configuration from the HBase cluster to the `conf` directory.

## Creating a Hbase Table

1. Create and open a new file `CreateTable.java` by typing:
   ```sh
   nano src/main/java/com/adaltas/examples/CreateTable.java
   ```
2. Inside the new file, instantiate the `Configuration Class` and use it in order to instantiate the `HBaseAdmin Class`:
    ```java
    Configuration config = HBaseConfiguration.create();
    HBaseAdmin admin = new HBaseAdmin(config);
    ```
3. Create a `TableDescriptor` by using the `HTableDescriptor Class` which is a kind of container of table names and column families:
    ```java
    //creating table descriptor
    HTableDescriptor tableDescriptor = new HTableDescriptor(toBytes("Table name"));

    //creating column family descriptor
    HColumnDescriptor family = new HColumnDescriptor(toBytes("Column family"));

    //adding column family to HTable
    tableDescriptor.addFamily(family);
    ```
4. Use the `createTable()` method of `HBaseAdmin` class in Admin Mode:
    ```java
    admin.createTable(tableDescriptor);
    ```

### Exemple of a CreateTable class

Below you can see a complete example of a class that creates a Hbase table named **employee** with two columns family **name** and **job**.

   ```java
    package com.adaltas.examples;
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
    }}
   ```

To excecute the program, you have to generate a `jar` file containing the app. Go to the **hbaseproject** directory and type: `mvn clean package`. Then, run the code by typing:
   ```sh
   cd target
   yarn jar hbaseproject-1.0-SNAPSHOT.jar com.adaltas.examples.CreateTable
   ```

## Inserting data

1. Create and open a new file `InsertData.java` by typing:
   ```
   nano src/main/java/com/adaltas/examples/InsertData.java
   ```
2. In the new file, instantiate the `Htable Class`. This class is used to communicate with a single HBase table, and it needs the configuration object and the table name as parameters:
   ```java
   Configuration config = HbaseConfiguration.create();
   HTable table = new HTable(config, tableName);
   ```
3. Instantiate the `Put Class`, you need to pass as parameter the row id you want to insert the data into:
   ```java
   Put p = new Put(Bytes.toBytes("1"));
   ```
4. Insert data by using the `add()` method of `Put Class`. You need to pass in this order the column family, column name, and the value to be inserted:
   ```java
   p.add(Bytes.toBytes("Column family"), Bytes.toBytes("Column name"),Bytes.toBytes("Value"));
   ```
5. Save your new rows by using the `put()` method of `Htable Class` and after don't forget to close the table:
   ```java
   table.put(p);
   table.close();
   ```

### Exemple of a InsertData class

Below you can see a complete example of a class that inserts data into a Hbase table named **employee** with two columns family **name** and **job**.

   ```java
package com.adaltas.examples;
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

      // defining some employees
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
   ```

To excecute the program, you have to generate a `jar` file containing the app. Go to the **hbaseproject** directory and type: `mvn clean package`. Then, run the code by typing:
   ```sh
   cd target
   yarn jar hbaseproject-1.0-SNAPSHOT.jar com.adaltas.examples.InsertData
   ```

## Reading table

1. Create and open a new file `ReadData.java` by typing:
   ```sh
   nano src/main/java/com/adaltas/examples/ReadData.java
   ```
2. As seen before, instantiate the `Configuration Class` and the `Htable Class`:
   ```java
   Configuration config = HbaseConfiguration.create();
   HTable table = new HTable(config, tableName);
   ```
3. Instantiate the `Get Class` to read a specific row by id:
   ```java
   Get get = new Get(toBytes("1"));
   ```
   You can aswell get a specific column from a specific column family:
   ```java
   get.addFamily(columnFamily);
   ```
   Or get all the columns from a specific column family:
    ```java
   get.addColumn(columnFamily, columnName); 
   ```
4. To get the result you need to use the `get()` method of the `Htable Class` by passing your `get` object. This method will return the requested result in a `Result Class` object:
   ```java
   Result result = table.get(get);  
   ```
5. Use the `getValue` method of the `Result Class` to read the values:
   ```java
   byte [] value = result.getValue(Bytes.toBytes("column family"),Bytes.toBytes("column name"));
   ```

### Exemple of a ReadData class

Below you can see a complete example of a class that reads data of a specific row from a Hbase table.

   ```java
package com.adaltas.examples;
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
   ```

The output should be: `first name: Sacha last name: Hert profession: Manager`.

To excecute the program, you have to generate a `jar` file containing the app. Go to the **hbaseproject** directory and type: `mvn clean package`. Then, run the code by typing:
   ```sh
   cd target
   yarn jar hbaseproject-1.0-SNAPSHOT.jar com.adaltas.examples.ReadData
   ```

## Deleting table

1. Create and open a new file `DeleteTable.java` by typing:
   ```sh
   nano src/main/java/com/adaltas/examples/DeleteTable.java
   ```
2. Create an admin object using the config:
   ```java
   Configuration config = HBaseConfiguration.create();
   HBaseAdmin admin = new HBaseAdmin(config);
   ```
3. Before deleting the table, you need to disable it:
   ```java
   admin.disableTable("employee");
   ```
4. Now, delete the table:
   ```java
   admin.deleteTable("employee");
   ```

### Exemple of a DeleteTable class

Below you can see a complete example of a class that deletes a Hbase table.

   ```java
package com.adaltas.examples;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;

public class DeleteTable {
    public static void main(String[] args) throws IOException {
    Configuration config = HBaseConfiguration.create();

    // Create an admin object using the config
    HBaseAdmin admin = new HBaseAdmin(config);

    // Disable the table
    admin.disableTable("employee");
    // Delete the table
    admin.deleteTable("employee");

    System.out.println("table deleted");
    }
}
   ```

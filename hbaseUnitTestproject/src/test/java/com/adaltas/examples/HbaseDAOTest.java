import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class HbaseDAOTest {
          	@Test
          	public void testCreatePut() throws Exception {
    	  	HBaseTestObj obj = new HBaseTestObj();
    	  	obj.setRowKey("ROWKEY-1");
    	  	obj.setData1("DATA-1");
    	  	obj.setData2("DATA-2");
    	  	Put put = HbaseDAO.createPut(obj);
    	  	assertEquals(obj.getRowKey(), Bytes.toString(put.getRow()));
    	  	assertEquals(obj.getData1(), Bytes.toString(put.get(Bytes.toBytes("CF"),
  Bytes.toBytes("CQ-1")).get(0).getValue()));
    	  	assertEquals(obj.getData2(), Bytes.toString(put.get(Bytes.toBytes("CF"),
  Bytes.toBytes("CQ-2")).get(0).getValue()));
          	}
  }

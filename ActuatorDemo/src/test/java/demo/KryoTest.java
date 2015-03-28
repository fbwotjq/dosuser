package demo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.util.StopWatch;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import com.esotericsoftware.minlog.Log;

public class KryoTest {

	/*@Test
	public void encodeToDecode(){
		
	}*/
	
	Kryo kryo = null;
	@Before
	public void initDefaultKyro(){
		kryo = new Kryo();
		kryo.setDefaultSerializer(CompatibleFieldSerializer.class); 
		kryo.register(TestKryoData.class);
	}
	
	private final int loop = 10000;
	/**
	 * tuning is evil.
	 *                                    create     ser   deser   total   size  +dfl
	 * kryo                                   63     655     838    1493    212   132
	 * kryo-manual                            63     555     616    1171    211   131
	 */
	@Test
	public void serializeTimingTest(){
		
		Output output = new Output( new ByteArrayOutputStream(10000));
		for(int l=0;l<loop;l++){
			TestKryoData serializableInstance = new TestKryoData();
		}
		TestKryoData serializableInstance = new TestKryoData();
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		for(int l=0;l<loop;l++){
			kryo.writeObject(output, serializableInstance);
		}
		stopWatch.stop();
		System.out.println(stopWatch.prettyPrint());
	}
	
	@Test
	public void legacyTest() throws IOException { 
		Log.TRACE(); 
		if (false) { 
			Output output = new Output(new FileOutputStream("kryo.dat")); 
			kryo.writeObject(output, new TestKryoData());
			//output.
			output.close(); 
		} else { 
			Input input = new Input(new FileInputStream("kryo.dat")); 
			TestKryoData dataWrapper = kryo.readObject(input, TestKryoData.class); 
			input.close(); 
			System.out.println("ddd value should be 'bbb', got: " + dataWrapper.ddd); 
		} 
			System.out.println("Done!"); 
	} 

	static public class TestKryoData { 
		public String aaa = "aaa"; 
		// public String bbb = "bbb"; 
		public String ccc = "ccc"; 
		// public String ddd = bbb; 
		public String ddd = "ddd"; 
	} 

}

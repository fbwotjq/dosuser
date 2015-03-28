package demo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;

import org.junit.Assert;
import org.junit.Test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

import demo.model.Document;
import demo.tcp.kryo.NettyKryoDecoder;
import demo.tcp.kryo.NettyKryoEncoder;

public class KeywordKryoCodecTest {

	@Test
	public void kryoEncoderTest(){

		EmbeddedChannel channel = new EmbeddedChannel(new NettyKryoEncoder());
		Kryo kryo = new Kryo(); 
		kryo.register(Document.class);
		Document message = new Document();
		message.setTitle("sdfsd");
		message.setContents("sdfsdf");
		Assert.assertTrue(channel.writeOutbound(message));
		
		ByteBuf encoded = (ByteBuf)channel.readOutbound();
		int size = encoded.readableBytes();
		byte[] data = new byte[size]; 
		encoded.readBytes(data);
		System.out.println(encoded.readableBytes());
		System.out.println(size);
		Input input = null;
		try {
			input = new Input(data);
			Document message2 = (Document) kryo.readClassAndObject(input);
			System.out.println(String.format("%s, %s", message2.getTitle(), message2.getContents())); 
		} catch(Exception e){
			e.printStackTrace();
		} finally {  
			input.close();
		}
		
	}
	
	@Test
	public void kryoDecoderTest(){
		EmbeddedChannel channel = new EmbeddedChannel(new NettyKryoDecoder());
		Kryo kryo = new Kryo(); 
		kryo.register(Document.class);
		Document message = new Document();
		message.setTitle("sdfsd");
		message.setContents("sdfsdf");
		Assert.assertTrue(channel.writeInbound(message));
		Document message2 = (Document) channel.readInbound();
		System.out.println(String.format("%s, %s", message2.getTitle(), message2.getContents())); 
		
		Assert.assertTrue(channel.writeInbound(message));
		Document message3 = (Document) channel.readInbound();
		System.out.println(String.format("%s, %s", message3.getTitle(), message3.getContents())); 
		
	}
	
}

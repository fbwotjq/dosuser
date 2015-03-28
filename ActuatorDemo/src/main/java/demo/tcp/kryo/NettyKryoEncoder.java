package demo.tcp.kryo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

import demo.model.Document;

//http://dreamworker.iteye.com/blog/1946100
@Component
@Qualifier("nettyKryoEncoder")
public class NettyKryoEncoder extends MessageToByteEncoder<Object> {

	
	public static final Kryo kryo = new Kryo();  
	
	static {
		kryo.register(Document.class);
	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

		long startPoint = System.currentTimeMillis();
        Output output = new Output(200);
        try { 
	        kryo.writeClassAndObject(output, msg);
	        output.flush();  
	        output.close();
        } catch (Exception e){
        	e.printStackTrace();
        }
        
        //out.writeByte((byte) 0);  					// reseve
        //out.writeByte((byte) 1);  					// type
        
        System.out.println(System.currentTimeMillis()-startPoint);
        System.out.println("##############################################################");
        
        byte[] byteArray = output.toBytes();
        //System.out.println(byteArray.length);
		//short bodySize = (short)byteArray.length; 	// body size
        //out.writeShort((short)bodySize);
        out.writeBytes(byteArray);
        ctx.writeAndFlush(out);
	}


}
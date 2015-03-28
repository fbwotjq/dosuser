package demo.tcp.kryo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

import demo.model.Document;

@Component
@Qualifier("nettyKryoDecoder")
public class NettyKryoDecoder extends ByteToMessageDecoder {

	public static Kryo kryo = new Kryo();  
	
	static {
		kryo.register(Document.class);
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		
		//msg.readByte();  						// reseve
		//msg.readByte();  						// type
		//short bodySize = msg.readShort(); 	// body size
		//Log.DEBUG(); 
		int length = msg.readableBytes();
		
		if(length == 0) return ;
		//System.out.println(length);
		
		Input input = null;
		try {  
			input = new Input(300);
			input.setBuffer(msg.readBytes(length).array());
			out.add(kryo.readClassAndObject(input));
		} finally {  
			input.close();
		}
	        
	}

}

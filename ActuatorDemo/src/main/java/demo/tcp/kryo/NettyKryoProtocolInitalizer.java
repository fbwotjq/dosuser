package demo.tcp.kryo;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
//import io.netty.handler.codec.string.StringDecoder;
//import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("nettyKryoProtocolInitalizer")
public class NettyKryoProtocolInitalizer extends ChannelInitializer<SocketChannel> {

	//@Autowired NettyKryoDecoder nettyKryoDecoder;
	//@Autowired NettyKryoEncoder nettyKryoEncoder;
	@Autowired NettyKryoHandler nettyKryoHandler;
	@Autowired DocumentNettyKryoHandler documentNettyKryoHandler;
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		//pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
		pipeline.addLast("decoder", new NettyKryoDecoder());
		//pipeline.addLast("decoder", new Decoder());
		pipeline.addLast("nettyKryoHandler", nettyKryoHandler);
		pipeline.addLast("documentNettyKryoHandler", documentNettyKryoHandler);
		pipeline.addLast("encoder", new NettyKryoEncoder());
		//pipeline.addLast("encoder", new Encoder());
	}

}
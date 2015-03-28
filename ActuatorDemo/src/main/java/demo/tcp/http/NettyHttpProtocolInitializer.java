package demo.tcp.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("keywordProtocolInitializer")
public class NettyHttpProtocolInitializer extends ChannelInitializer<SocketChannel>{

	@Autowired NettyHttpHandler nettyHttpBootstrapHandler;
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline channelPipeline = ch.pipeline();
		channelPipeline.addLast(new HttpServerCodec());
		channelPipeline.addLast(new HttpObjectAggregator(1048576));
		channelPipeline.addLast(nettyHttpBootstrapHandler);
	}

}
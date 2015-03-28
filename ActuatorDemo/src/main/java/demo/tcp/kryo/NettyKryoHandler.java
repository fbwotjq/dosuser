package demo.tcp.kryo;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import demo.model.User;

@Component
@Qualifier("nettyKryoHandler")
@Sharable
public class NettyKryoHandler extends SimpleChannelInboundHandler<User>{

	private final Logger logger = LoggerFactory.getLogger(NettyKryoHandler.class);
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, User msg) throws Exception {
		logger.debug("##############################################################################");
		logger.debug(String.format("SERVER::channelRead0 : %s, %s", msg.getId(), msg.getName()));
		Set<String> set = msg.getHistorySet();
		Iterator<String> iterator = set.iterator();
		while(iterator.hasNext()){
			System.out.println("history:"+iterator.next());
		}
		
		User aaa = new User();
		aaa.setName("sdfsd");
		aaa.setId("3742374827934729");
		ctx.channel().write(aaa);
		logger.debug("##############################################################################");
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Channel is active\n");
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("\nChannel is disconnected");
		super.channelInactive(ctx);
	}
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	System.out.println("exceptionCaught");
        cause.printStackTrace();
        ctx.close();
    }
	
}

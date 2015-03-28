package demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyClientHandler extends SimpleChannelInboundHandler<String> {
	
	private final ByteBuf firstMessage;
	
	public NettyClientHandler() {
        firstMessage = Unpooled.buffer(256);
        for (int i = 0; i < firstMessage.capacity(); i ++) {
            firstMessage.writeByte((byte) i);
        }
    }
	
	@Override
    public void channelActive(ChannelHandlerContext ctx) {
		System.out.println("channelActive");
        ctx.writeAndFlush("hello");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
    	System.out.println("channelRead : " + msg.toString());
        //ctx.write(msg);
    }

/*    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
    	System.out.println("channelReadComplete");
    	ctx.flush();
    }*/

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	System.out.println("exceptionCaught");
        cause.printStackTrace();
        ctx.close();
    }

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		System.out.println("channelRead0");
	}
    
}

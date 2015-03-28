package demo;

import demo.model.User;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ObjectClientHandler extends SimpleChannelInboundHandler<User> {

	@Override
    public void channelActive(ChannelHandlerContext ctx) {
		System.out.println("channelActive");
		User user = new User();
		user.setId("fbwotjq");
		user.setName("류재섭");
		user.getHistorySet().add("베이시스트");
		user.getHistorySet().add("프로그래머");
		user.getHistorySet().add("장남");
		ctx.write(user);
        
    }
	
	@Override
	public void channelRead0(ChannelHandlerContext ctx, User msg) throws Exception {
		System.out.println("##############################################################################");
		System.out.println(String.format("CLIENT::channelRead0 : %s, %s", msg.getId(), msg.getName()));
		System.out.println("##############################################################################");
	}

	@Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
    	System.out.println("channelReadComplete");
    	//ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	System.out.println("exceptionCaught");
        cause.printStackTrace();
        ctx.close();
    }
    
}

package demo.tcp.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class NettyHttpService {
	
	private final Logger logger = LoggerFactory.getLogger(NettyHttpService.class);
	
	@Autowired @Qualifier("nettyHttpBootstarp") private ServerBootstrap serverBootstrap;
	@Autowired @Qualifier("nettyHttpBootstarpInetSocketAddress") private InetSocketAddress inetSocketAddress;
	
	private Channel serverChannel;
	
	@PostConstruct
	public void start() {

		logger.info("### STARTING NETTY HTTP SERVER AT " + inetSocketAddress.getPort());
		new Thread(new Runnable() {
			public void run() {
				try {
					serverChannel = serverBootstrap.bind(inetSocketAddress).sync().channel().closeFuture().sync().channel();
				} catch (InterruptedException e) {
					logger.error("### NETTY HTTP START FAIL AT "+ inetSocketAddress.getPort() + ".. SO DOWN.. ");
					logger.error(e.getMessage());
					System.exit(-1);
					e.printStackTrace();
				}
			}
		}).start();
		
	}
	
	@PreDestroy
	public void stop(){
		serverChannel.close();
	}
	
}

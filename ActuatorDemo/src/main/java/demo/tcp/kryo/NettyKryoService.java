package demo.tcp.kryo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class NettyKryoService {
	
	private final Logger logger = LoggerFactory.getLogger(NettyKryoService.class);
	
	@Autowired private NettyKryoConfig nettyConfig;
	
	@Autowired @Qualifier("nettyKryoServerBootstrap") private ServerBootstrap serverBootstrap;
	@Autowired @Qualifier("nettyKryoTcpSocketAddress") private InetSocketAddress inetSocketAddres;
	
	private Channel serverChannel;

	@PostConstruct
	public void start() throws Exception {
		
		new Thread(new Runnable() {
			public void run() {
				try {
					logger.info("### STARTING NETTY KRYO SERVER AT " + nettyConfig.getTcpPort());
					serverChannel = serverBootstrap.bind(inetSocketAddres).sync().channel().closeFuture().sync().channel();
				} catch (InterruptedException e) {
					logger.error("### NETTY KRYO START FAIL AT "+ inetSocketAddres.getPort() + ".. SO DOWN.. ");
					logger.error(e.getMessage());
					System.exit(-1);
					e.printStackTrace();
				}
			}
		}).start();
		
	}

	@PreDestroy
	public void stop() {
		serverChannel.close();
	}
}

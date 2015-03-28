package demo.tcp.kryo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.handler.codec.string.StringDecoder;
//import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@ConfigurationProperties(prefix = "nettyKryo", ignoreUnknownFields = false)
public class NettyKryoConfig {

	private int bossThreadCount;
	private int workerThreadCount;
	private int tcpPort;
	private boolean soKeepAlive;
	private int soBacklog;
	
	public int getBossThreadCount() {
		return bossThreadCount;
	}
	
	public void setBossThreadCount(int bossThreadCount) {
		this.bossThreadCount = bossThreadCount;
	}
	
	public int getWorkerThreadCount() {
		return workerThreadCount;
	}
	
	public void setWorkerThreadCount(int workerThreadCount) {
		this.workerThreadCount = workerThreadCount;
	}
	
	public int getTcpPort() {
		return tcpPort;
	}
	
	public void setTcpPort(int tcpPort) {
		this.tcpPort = tcpPort;
	}
	
	public boolean isSoKeepAlive() {
		return soKeepAlive;
	}
	
	public void setSoKeepAlive(boolean soKeepAlive) {
		this.soKeepAlive = soKeepAlive;
	}
	
	public int getSoBacklog() {
		return soBacklog;
	}
	
	public void setSoBacklog(int soBacklog) {
		this.soBacklog = soBacklog;
	}
	
	@Bean(name = "nettyKryoTcpSocketAddress")
	public InetSocketAddress nettyKryoTcpPort() {
		return new InetSocketAddress(tcpPort);
	}
	
	@Bean(name = "nettyKryoBossGroup", destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup nettyKryoBossGroup() {
		return new NioEventLoopGroup(bossThreadCount);
	}
	
	@Bean(name = "nettyKryoWorkerGroup", destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup nettyKryoWorkerGroup() {
		return new NioEventLoopGroup(workerThreadCount);
	}
	
	@Bean(name = "nettyKryoTcpChannelOptions")
	public Map<ChannelOption<?>, Object> nettyKryoTcpChannelOptions() {
		Map<ChannelOption<?>, Object> options = new HashMap<ChannelOption<?>, Object>();
		options.put(ChannelOption.SO_KEEPALIVE, soKeepAlive);
		options.put(ChannelOption.SO_BACKLOG, soBacklog);
		return options;
	}
	
	@Autowired
	@Qualifier("nettyKryoProtocolInitalizer")
	private NettyKryoProtocolInitalizer nettyKryoProtocolInitalizer;

	@SuppressWarnings("unchecked")
	@Bean(name = "nettyKryoServerBootstrap")
	public ServerBootstrap nettyKryoServerBootstrap() {
		
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(nettyKryoBossGroup(), nettyKryoWorkerGroup())
				.channel(NioServerSocketChannel.class)
				.handler(new LoggingHandler(LogLevel.DEBUG))
				.childHandler(nettyKryoProtocolInitalizer);
		Map<ChannelOption<?>, Object> tcpChannelOptions = nettyKryoTcpChannelOptions();
		Set<ChannelOption<?>> keySet = tcpChannelOptions.keySet();
		
		for (@SuppressWarnings("rawtypes") ChannelOption option : keySet) {
			serverBootstrap.option(option, tcpChannelOptions.get(option));
		}
		
		return serverBootstrap;
	}
	
}
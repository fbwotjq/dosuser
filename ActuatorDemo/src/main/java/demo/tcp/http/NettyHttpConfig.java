package demo.tcp.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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
@ConfigurationProperties(prefix="nettyHttp", ignoreUnknownFields=false)
public class NettyHttpConfig {
	
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

	@Bean(name = "nettyHttpBootstarpInetSocketAddress")
	public InetSocketAddress nettyHttpBootstarpInetSocketAddress(){
		return new InetSocketAddress(tcpPort);
	}
	
	@Bean(name = "nettyHttpBootstarpBossGroup", destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup nettyHttpBootstarpBossGroup(){
		return new NioEventLoopGroup();
	}
	
	@Bean(name = "nettyHttpBootstarpWorkerGroup", destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup nettyHttpBootstarpWorkerGroup(){
		return new NioEventLoopGroup();
	}
	
	@Bean(name = "nettyHttpBootstarpChannelOtpions")
	public Map<ChannelOption<?>, Object> nettyHttpBootstarpChannelOtpions(){
		Map<ChannelOption<?>, Object> options = new HashMap<ChannelOption<?>, Object>();
		options.put(ChannelOption.SO_KEEPALIVE, soKeepAlive);
		options.put(ChannelOption.SO_BACKLOG, soBacklog);
		return options;
	}
	
	@Autowired @Qualifier("nettyHttpProtocolInitializer") private NettyHttpProtocolInitializer nettyHttpProtocolInitializer;
	
	@SuppressWarnings("unchecked")
	@Bean(name = "nettyHttpBootstarp")
	public ServerBootstrap nettyHttpBootstarp(){
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(nettyHttpBootstarpBossGroup(), nettyHttpBootstarpWorkerGroup())
		.channel(NioServerSocketChannel.class)
		.handler(new LoggingHandler(LogLevel.DEBUG))
		.childHandler(nettyHttpProtocolInitializer);
		Map<ChannelOption<?>, Object> channelOptionMap = nettyHttpBootstarpChannelOtpions();
		Set<ChannelOption<?>> channelOptionSet = channelOptionMap.keySet();
		for (@SuppressWarnings("rawtypes") ChannelOption channelOption : channelOptionSet) {
			serverBootstrap.option(channelOption, channelOptionMap.get(channelOption));
		}
		return serverBootstrap;
	}
	
}

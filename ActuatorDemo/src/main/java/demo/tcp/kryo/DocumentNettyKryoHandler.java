package demo.tcp.kryo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import demo.model.Document;

@Component
@Qualifier("documentNettyKryoHandler")
@Sharable
public class DocumentNettyKryoHandler extends SimpleChannelInboundHandler<Document>{

	private final Logger logger = LoggerFactory.getLogger(DocumentNettyKryoHandler.class);
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Channel is active\n");
		super.channelActive(ctx);
	}
	

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Document msg) throws Exception {
		logger.debug("##############################################################################");
		System.out.println(String.format("SERVER::channelRead0 : %s, %s", msg.getTitle(), msg.getContents()));
		Document p = null;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet("http://localhost:38080/");
			getRequest.addHeader("accept", "application/json");
 
			HttpResponse response = httpClient.execute(getRequest);
 
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}
 
			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
		
			String contents = "";
			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				contents += output;
			}
			System.out.println("######################################################");
			System.out.println(contents);
			System.out.println("######################################################");
			httpClient.getConnectionManager().shutdown();
		
			Gson gson = new GsonBuilder().create();
			p = gson.fromJson(contents, Document.class);
                
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		ctx.channel().writeAndFlush(p);
		logger.debug("##############################################################################");
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

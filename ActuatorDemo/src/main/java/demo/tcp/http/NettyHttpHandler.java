package demo.tcp.http;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.handler.codec.http.multipart.MixedAttribute;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
@Qualifier("nettyHttpBootstrapHandler")
@Sharable
public class NettyHttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);
	private final Logger logger = LoggerFactory.getLogger(NettyHttpHandler.class);
		
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		logger.debug("############################################################### channelRead0");
		if (HttpHeaders.is100ContinueExpected(msg)) {
            ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
        }
		
		byte[] returnMessageBypes = NettyHttpConstants.MESSAGE_VALID_REQUEST;
		
		
		HttpModel httpModel = getHttpModel(msg); 													// 파라메터 파싱 
		
		boolean close = HttpHeaders.Values.CLOSE.equalsIgnoreCase(msg.headers().get(CONNECTION))	// keepAlive 상태 체크
				|| msg.getProtocolVersion().equals(HttpVersion.HTTP_1_0)
				&& HttpHeaders.Values.KEEP_ALIVE.equalsIgnoreCase(msg.headers().get(CONNECTION)) == false;
		

		logger.debug("##############################################################################");
		
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
			
			returnMessageBypes = contents.getBytes();
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		logger.debug("##############################################################################");
		
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(returnMessageBypes));
		response.headers().set(CONTENT_TYPE, NettyHttpConstants.CONTENT_TYPE_APPLICATION_JSON_CHARSET_UTF_8);
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        ctx.write(response);
        ChannelFuture future = ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);
        
        if(close == true){
        	future.addListener(ChannelFutureListener.CLOSE);
        }
		
	}
	
	private HttpModel getHttpModel(HttpRequest request) throws IOException {
		
		HttpModel httpModel = null;
		HttpMethod httpMethod = request.getMethod();
		
		try {
			
			if(HttpMethod.POST.equals(httpMethod)){
				
				httpModel = new HttpModel();
				httpModel.setHttpMethod(httpMethod);
				httpModel.setUri(request.getUri());
				HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(factory, request);
				List<InterfaceHttpData> InterfaceHttpDataList = decoder.getBodyHttpDatas();
				for (InterfaceHttpData interfaceHttpData : InterfaceHttpDataList) {
					if(HttpDataType.Attribute.equals(interfaceHttpData.getHttpDataType())){
						MixedAttribute mixedAttribute = (MixedAttribute)interfaceHttpData;
						httpModel.putParameter(mixedAttribute.getName(), mixedAttribute.getValue());
					}
				}
				
			} else if(HttpMethod.GET.equals(httpMethod)){
				
				httpModel = new HttpModel();
				httpModel.setHttpMethod(httpMethod);
				httpModel.setUri(request.getUri());
				QueryStringDecoder decoderQuery = new QueryStringDecoder(request.getUri());
				Map<String, List<String>> uriAttributes = decoderQuery.parameters();
				for(Entry<String, List<String>> entry : uriAttributes.entrySet()){
					httpModel.putParameter(entry.getKey(), entry.getValue().get(0));
				}
				
			} else {
				
			}
		
		} catch(IOException e){
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			
		}
		
		return httpModel;
	}
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		
		if(cause instanceof IOException == false) {
			logger.error(cause.getMessage());
			cause.printStackTrace();
		}
        ctx.close();
    }
	
}

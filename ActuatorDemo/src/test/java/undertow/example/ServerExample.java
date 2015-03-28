package undertow.example;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class ServerExample {

	public static void main(final String[] args) {
        Undertow server = Undertow.builder()
                .addHttpListener(38080, "localhost")
                .setHandler(new HttpHandler() {
                    public void handleRequest(final HttpServerExchange exchange) throws Exception {
                        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
                        exchange.getResponseSender().send("{\"title\":\"류재섭\",\"contents\":\"developer\"}");
                    }
                }).build();
        server.start();
    }
	
}

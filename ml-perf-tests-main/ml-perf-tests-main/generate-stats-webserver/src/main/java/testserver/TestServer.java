package testserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.QueryStringDecoder;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.Http11SslContextSpec;
import reactor.netty.http.Http2SslContextSpec;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;
import reactor.util.context.Context;

/**
 * Common JUNIT test utilities.
 */
public class TestServer {

	public static void main(String argv[]) throws InterruptedException {
		DisposableServer server = TestServer.buildHttp1SecureServer().bindNow();
		System.out.println("https://localhost:" + server.port());
		Thread.sleep(Long.MAX_VALUE);
	}

	private static Flux<ByteBuf> BYTE_FLUX;
	private static String DEFAULT_FRAME_SIZE = "16000";

	private static final String SERVER_CERT = "server/cert.pem";

	private static final String SERVER_KEY = "server/key.pem";

	private static String SSE_HTLM = "<html>\r\n" //
			+ "<body>\r\n" //
			+ "<h1>Test Server SSE Example</h1>\r\n" //
			+ "<script>\r\n" //
			+ "var source = new EventSource(\"sse.php\");\r\n" //
			+ "source.onopen = function(event) {\r\n" //
			+ "  console.log('Opened');\r\n" //
			+ "};\r\n" //
			+ "\r\n" //
			+ "source.addEventListener('message', function(e) {\r\n" //
			+ "  console.log(e);\r\n" //
			+ "}, false);\r\n" //
			+ "\r\n" //
			+ "source.addEventListener('bar', function(e) {\r\n" //
			+ "  console.log('bar', e);\r\n" //
			+ "}, false);\r\n" //
			+ "\r\n" //
			+ "source.onerror = function(err) {\r\n" //
			+ "   console.log(err);\r\n" //
			+ "};\r\n" //
			+ "\r\n" //
			+ "\r\n" //
			+ "</script>\r\n" //
			+ "</body>\r\n" //
			+ "</html>";

	private static String WS_HTML = "<html>\r\n" //
			+ "<body>\r\n" //
			+ "<h1>Web Socket Example</h1>\r\n" //
			+ "<script>\r\n" //
			+ "let socket = new WebSocket(\"ws://\" + location.host + \"/testserver/ws/socket\");\r\n" //
			+ "\r\n" //
			+ "socket.onopen = function(e) {\r\n" //
			+ "  console.log(\"[open] Connection established\");\r\n" //
			+ "  console.log(\"Sending to server\");\r\n" //
			+ "  socket.send(\"My name is John\");\r\n" //
			+ "};\r\n" //
			+ "\r\n" //
			+ "socket.onmessage = function(event) {\r\n" //
			+ "  console.log(`[message] Data received from server: ${event.data}`);\r\n" //
			+ "  setTimeout(function(){\r\n" //
			+ "    console.log(\"Sending to server\");\r\n" //
			+ "    socket.send(\"Another message\");\r\n" //
			+ "  }, 10000);\r\n" //
			+ "};\r\n" //
			+ "\r\n" //
			+ "socket.onclose = function(event) {\r\n" //
			+ "  if (event.wasClean) {\r\n" //
			+ "    console.log(`[close] Connection closed cleanly, code=${event.code} reason=${event.reason}`);\r\n" //
			+ "  } else {\r\n" //
			+ "    // e.g. server process killed or network down\r\n" //
			+ "    // event.code is usually 1006 in this case\r\n" //
			+ "    console.log('[close] Connection died');\r\n" //
			+ "  }\r\n" //
			+ "};\r\n" //
			+ "\r\n" //
			+ "socket.onerror = function(error) {\r\n" //
			+ "  console.log(`[error] ${error.message}`);\r\n" //
			+ "};\r\n" //
			+ "</script>\r\n" //
			+ "</body>\r\n" //
			+ "</html>";

	static {
		StringBuilder gifString = new StringBuilder();
		byte[] BIN_BYTES = new byte[100000];
		for (int i = 0; i < 100000; i++) {
			BIN_BYTES[i] = (byte) (i % 256);
			gifString.append((char) ('a' + i % 26));
		}
		byte[] PRINT_BYTES = gifString.toString().getBytes(StandardCharsets.UTF_8);
		BYTE_FLUX = Mono.deferContextual(ctx -> {
			Long size = Long.valueOf((String) ctx.get("size"));
			Long chunkSize = Long.valueOf((String) ctx.get("chunks"));
			byte[] buf;
			if (ctx.getOrDefault("type", "printable").equals("printable")) {
				buf = PRINT_BYTES;
			} else {
				buf = BIN_BYTES;
			}
			return Mono.zip(Mono.just(size), Mono.just(chunkSize), Mono.just(buf));
		}).flatMapMany(tuple -> {
			final long initialTotalSize = tuple.getT1();
			final long chunkSize = tuple.getT2();
			final byte[] buf = tuple.getT3();
			if (chunkSize > buf.length) {
				throw new IllegalArgumentException("Increase the test server check size > " + chunkSize);
			}
			return Flux.generate(() -> initialTotalSize, //
					(remainingBytesToSend, sink) -> { //
						long bytesToWrite = Math.min(remainingBytesToSend, chunkSize);
						long nextRemainingBytesToSend = remainingBytesToSend - bytesToWrite;

						ByteBuf nextBuf = Unpooled.wrappedBuffer(buf, 0, (int) bytesToWrite);
						sink.next(nextBuf);
						if (nextRemainingBytesToSend <= 0) {
							sink.complete();
						}
						return nextRemainingBytesToSend;
					});
		});
	}

	private static HttpServer addHelloWorldRoutes(HttpServer server) {
		return server.route(routes -> //
		routes.get(testserver(""), //
				(request, response) -> response.sendString(Mono.just("Welcome!"))) //
				.get(testserver("/hello"), hello()) //
				.get(testserver("/gif/total/{size}/in/{chunks}"), chunks()) //
				.get(testserver("/binary/total/{size}/in/{chunks}"), chunks()) //
				.get(testserver("/printable/total/{size}/in/{chunks}"), printableChunks()) //
				.get(testserver("/statusCode/{statusCode}"), statusCode()) //
				.get(testserver("/setCookie"), setCookie()) //
				.head(testserver("/head"), (request, response) -> response.send()) //
				.post(testserver("/echo"), echoContent()) //
				.put(testserver("/echo"), echoContent()) //
				.get(testserver("/params"), params()) //
				.get(testserver("/sse/"), sseHtlm()) //
				.get(testserver("/sse/sse.php"), ssePhp()) //
				.get(testserver("/ws/"), wsHtlm()) //
				.get(testserver("/io-error/"), io_error()) //
				
				.get(testserver("/{page}/javatest/JavaTest.html"), page(50953)) //
				.get(testserver("/{page}/javatest/JavaTest2.html"), page(50955)) //
				.get(testserver("/{page}/javatest/20kb1.jpg"), jpeg(21452)) //
				.get(testserver("/{page}/javatest/20kb2.jpg"), jpeg(21295)) //
				.get(testserver("/{page}/javatest/20kb3.jpg"), jpeg(19517)) //
				.get(testserver("/{page}/javatest/20kb4.jpg"), jpeg(20032)) //
				.get(testserver("/{page}/javatest/20kb5.jpg"), jpeg(22002)) //
				.get(testserver("/{page}/javatest/20kb6.jpg"), jpeg(20529)) //
				.get(testserver("/{page}/javatest/20kb7.jpg"), jpeg(21452)) //
				.get(testserver("/{page}/javatest/20kb8.jpg"), jpeg(22002)) //
				.get(testserver("/{page}/javatest/20kb9.jpg"), jpeg(20032)) //
				.get(testserver("/{page}/javatest/20kb10.jpg"), jpeg(19517)) //
				.get(testserver("/{page}/javatest/20kb11.jpg"), jpeg(21295)) //
				.get(testserver("/{page}/javatest/20kb12.jpg"), jpeg(20529)) //
				
				.get(testserver("/javatest/JavaTest.html"), page(50953)) //
				.get(testserver("/javatest/JavaTest2.html"), page(50955)) //
				.get(testserver("/javatest/20kb1.jpg"), jpeg(21452)) //
				.get(testserver("/javatest/20kb2.jpg"), jpeg(21295)) //
				.get(testserver("/javatest/20kb3.jpg"), jpeg(19517)) //
				.get(testserver("/javatest/20kb4.jpg"), jpeg(20032)) //
				.get(testserver("/javatest/20kb5.jpg"), jpeg(22002)) //
				.get(testserver("/javatest/20kb6.jpg"), jpeg(20529)) //
				.get(testserver("/javatest/20kb7.jpg"), jpeg(21452)) //
				.get(testserver("/javatest/20kb8.jpg"), jpeg(22002)) //
				.get(testserver("/javatest/20kb9.jpg"), jpeg(20032)) //
				.get(testserver("/javatest/20kb10.jpg"), jpeg(19517)) //
				.get(testserver("/javatest/20kb11.jpg"), jpeg(21295)) //
				.get(testserver("/javatest/20kb12.jpg"), jpeg(20529)) //
				.get(testserver("/delay/{param}"), delay()) //
				.get(testserver("/throughput/"), throughput()) //
				.get(testserver("/path/{param}"), //
						(request, response) -> response.sendString(Mono.just(request.param("param")))) //
				.ws(testserver("/ws/socket"), //
						(wsInbound, wsOutbound) -> wsOutbound.send(wsInbound.receive().retain())));
	}

	private static HttpServer basePort(HttpServer server, int plus) {
		String basePort = System.getProperty("testServer.basePort", "7777");
		if (basePort != null) {
			return server.port(Integer.parseInt(basePort) + plus);
		} else {
			return server;
		}
	}

	static HttpServer buildH2SecureServer() {
		Http2SslContextSpec http2SslContextSpec = Http2SslContextSpec
				.forServer(getFileFromResourceAsStream(SERVER_CERT), getFileFromResourceAsStream(SERVER_KEY));
		HttpServer server = addHelloWorldRoutes(
				HttpServer.create().protocol(HttpProtocol.H2).secure(spec -> spec.sslContext(http2SslContextSpec)));
		return basePort(server, 2);
	}

	static HttpServer buildHttp1SecureServer() {
		Http11SslContextSpec http11SslContextSpec = Http11SslContextSpec
				.forServer(getFileFromResourceAsStream(SERVER_CERT), getFileFromResourceAsStream(SERVER_KEY));
		HttpServer server = addHelloWorldRoutes(
				HttpServer.create().secure(spec -> spec.sslContext(http11SslContextSpec)));
		return basePort(server, 1);
	}

	static HttpServer buildHttp1Server() {
		HttpServer server = addHelloWorldRoutes(HttpServer.create());
		return basePort(server, 0);
	}

	// Return a GIF of size in small chunks
	private static BiFunction<? super HttpServerRequest, ? super HttpServerResponse, ? extends Publisher<Void>> chunks() {
		return (req, resp) -> {
			resp.addHeader("Content-Type", "application/octet-stream");
			return resp.send(BYTE_FLUX.contextWrite(gifContext(req.param("size"), req.param("chunks"))));
		};
	}

	private static BiFunction<? super HttpServerRequest, ? super HttpServerResponse, ? extends Publisher<Void>> delay() {
		return (req, resp) -> {
			long delayMs = Long.parseLong(req.param("param"));
			return resp
					.sendString(Mono.just("Delayed response by " + delayMs).delayElement(Duration.ofMillis(delayMs)));
		};
	}

	private static BiFunction<? super HttpServerRequest, ? super HttpServerResponse, ? extends Publisher<Void>> echoContent() {
		return (req, resp) -> {
			String contentType = req.requestHeaders().get("Content-Type");
			if (contentType != null) {
				resp.header("Content-Type", contentType);
			}
			return resp.send(req.receive().retain());
		};
	}

	public static InputStream getFileFromResourceAsStream(String fileName) {
		InputStream inputStream = TestServer.class.getClassLoader().getResourceAsStream(fileName);
		if (inputStream == null) {
			throw new IllegalArgumentException("file not found! " + fileName);
		} else {
			return inputStream;
		}
	}

	private static String getQuery(Map<String, List<String>> query, String key, String defaultValue) {
		List<String> values = query.get(key);
		if (values != null) {
			return values.get(0);
		} else {
			return defaultValue;
		}
	}

//	private static BiFunction<? super HttpServerRequest, ? super HttpServerResponse, ? extends Publisher<Void>> gif(
//			int size) {
//		return (req, resp) -> {
//			resp.addHeader("Content-Type", "image/gif");
//			return resp.send(BYTE_FLUX.contextWrite(gifContext(String.valueOf(size))));
//		};
//	}

	private static BiFunction<? super HttpServerRequest, ? super HttpServerResponse, ? extends Publisher<Void>> jpeg(
			int size) {
		return (req, resp) -> {
			resp.addHeader("Content-Type", "image/jpeg");
			return resp.send(BYTE_FLUX.contextWrite(gifContext(String.valueOf(size))));
		};
	}

	private static Function<Context, Context> gifContext(String totalSize) {
		return ctx -> {
			return ctx.put("size", totalSize).put("chunks", DEFAULT_FRAME_SIZE).put("type", "binary");
		};
	}

	private static Function<Context, Context> gifContext(String totalSize, String chunkSize) {
		return ctx -> {
			return ctx.put("size", totalSize).put("chunks", chunkSize).put("type", "binary");
		};
	}

	private static BiFunction<? super HttpServerRequest, ? super HttpServerResponse, ? extends Publisher<Void>> hello() {
		return (req, resp) -> {
			resp.addHeader("Content-Type", "text/plain");
			return resp.sendString(Mono.just("Hello World!"));
		};
	}

	private static BiFunction<? super HttpServerRequest, ? super HttpServerResponse, ? extends Publisher<Void>> io_error() {
		return (req, resp) -> {
			resp.withConnection(c -> {
				c.channel().disconnect();
			});
			Flux<ByteBuf> reply = Flux.generate(() -> 0, //
					(i, sink) -> { //
						sink.complete();
						return i + 1;
					});
			return resp.send(reply);
		};
	}

	public static long now() {
		return System.currentTimeMillis();
	}

	private static BiFunction<? super HttpServerRequest, ? super HttpServerResponse, ? extends Publisher<Void>> page(
			int size) {
		return (req, resp) -> {
			resp.addHeader("Content-Type", "text/html");
			return resp.send(BYTE_FLUX.contextWrite(printableContext(String.valueOf(size))));
		};
	}
	
	private static BiFunction<? super HttpServerRequest, ? super HttpServerResponse, ? extends Publisher<Void>> throughput() {
		return (req, resp) -> {
			Map<String, List<String>> query = new QueryStringDecoder(req.uri()).parameters();
			long delayMs = Integer.parseInt(getQuery(query, "delay", "0"));
			int size = Integer.parseInt(getQuery(query, "size", "1000"));
			resp.addHeader("Content-Type", "text/html");
			return resp.send(BYTE_FLUX.contextWrite(printableContext(String.valueOf(size))) //
					.delayElements(Duration.ofMillis(delayMs)));
		};
	}

	private static BiFunction<? super HttpServerRequest, ? super HttpServerResponse, ? extends Publisher<Void>> params() {
		return (req, resp) -> {
			ObjectMapper m = new ObjectMapper();

			ObjectNode params = m.createObjectNode();
			new QueryStringDecoder(req.uri()).parameters().forEach((key, list) -> {
				AtomicInteger i = new AtomicInteger(0);
				ObjectNode paramJson = m.createObjectNode();
				list.forEach(v -> {
					paramJson.put(String.valueOf(i.getAndIncrement()), v);
				});
				params.set(key, paramJson);
			});

			ObjectNode cookies = m.createObjectNode();
			req.cookies().forEach((cookieName, setOf) -> {
				AtomicInteger i = new AtomicInteger(0);
				ObjectNode values = m.createObjectNode();
				setOf.forEach(cookie -> {
					ObjectNode cookieJson = m.createObjectNode();
					cookieJson.put("name", cookie.name());
					cookieJson.put("value", cookie.value());
					values.set(String.valueOf(i.getAndIncrement()), cookieJson);
				});
				cookies.set(cookieName.toString(), values);
			});

			ObjectNode json = m.createObjectNode();
			json.set("params", params);
			json.set("cookies", cookies);

			resp.addHeader("Content-Type", "application/json");
			try {
				return resp.sendString(Flux.just(m.writerWithDefaultPrettyPrinter().writeValueAsString(json)));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return resp.sendNotFound();
			}
		};
	}

	private static BiFunction<? super HttpServerRequest, ? super HttpServerResponse, ? extends Publisher<Void>> printableChunks() {
		return (req, resp) -> {
			resp.addHeader("Content-Type", "application/octet-stream");
			return resp.send(BYTE_FLUX.contextWrite(printableContext(req.param("size"), req.param("chunks"))));
		};
	}

	private static Function<Context, Context> printableContext(String totalSize) {
		return ctx -> {
			return ctx.put("size", totalSize).put("chunks", DEFAULT_FRAME_SIZE).put("type", "printable");
		};
	}

	private static Function<Context, Context> printableContext(String totalSize, String chunkSize) {
		return ctx -> {
			return ctx.put("size", totalSize).put("chunks", chunkSize).put("type", "printable");
		};
	}

	private static BiFunction<? super HttpServerRequest, ? super HttpServerResponse, ? extends Publisher<Void>> setCookie() {
		return (req, resp) -> {
			// All request to simulate Set-Cookie response
			req.requestHeaders().entries().stream() //
					.filter(e -> e.getKey().equalsIgnoreCase("Set-Cookie")) //
					.forEachOrdered(e -> resp.addHeader("Set-Cookie", e.getValue()));
			return params().apply(req, resp);
		};
	}

	private static BiFunction<? super HttpServerRequest, ? super HttpServerResponse, ? extends Publisher<Void>> sseHtlm() {
		return (req, resp) -> {
			resp.addHeader("Content-Type", "text/html");
			return resp.sendString(Mono.just(SSE_HTLM));
		};
	}

	private static BiFunction<? super HttpServerRequest, ? super HttpServerResponse, ? extends Publisher<Void>> ssePhp() {
		return (req, resp) -> {
			Map<String, List<String>> query = new QueryStringDecoder(req.uri()).parameters();
			int msgCount = Integer.parseInt(getQuery(query, "count", "1000"));
			long delayMs = Integer.parseInt(getQuery(query, "delay", "5000"));

			Flux<ByteBuf> flux = Flux.range(1, msgCount).delayElements(Duration.ofMillis(delayMs)).map(count -> {
				String DATA = "id: " + count + "\nevent: bar\ndata: The message number " + count + "\n\n";
				byte[] buf = DATA.getBytes();
				return Unpooled.wrappedBuffer(buf, 0, buf.length);
			});

			resp.addHeader("Content-Type", "text/event-stream");
			resp.addHeader("Cache-Control", "no-cache");
			return resp.send(flux);
		};
	}

	private static BiFunction<? super HttpServerRequest, ? super HttpServerResponse, ? extends Publisher<Void>> statusCode() {
		return (req, resp) -> {
			resp.addHeader("Content-Type", "text/html");
			resp.status(Integer.parseInt(req.param("statusCode")));
			return resp.send(BYTE_FLUX.contextWrite(printableContext("20", "20")));
		};
	}

	private static String testserver(String append) {
		return "/testserver" + append;
	}

	private static BiFunction<? super HttpServerRequest, ? super HttpServerResponse, ? extends Publisher<Void>> wsHtlm() {
		return (req, resp) -> {
			resp.addHeader("Content-Type", "text/html");
			return resp.sendString(Mono.just(WS_HTML));
		};
	}

}

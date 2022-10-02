    import java.net.URI;
    import java.net.http.HttpClient;
    import java.net.http.WebSocket;
    import java.util.concurrent.CompletionStage;
    import java.util.concurrent.CountDownLatch;
    import java.lang.Thread;

    public class JavaWebSocketClient {

        public static void main(String[] args) throws Exception {
            CountDownLatch latch = new CountDownLatch(1);
//.buildAsync(URI.create("ws://localhost:8081/websocketserver/chat"), new WebSocketClient(latch))
            WebSocket ws = HttpClient
                    .newHttpClient()
                    .newWebSocketBuilder()
                    .buildAsync(URI.create("ws://localhost:8081/3940-assignment1-group5/chat"), new WebSocketClient(latch))
                    .join();
            ws.sendText("Hello!", true);
            latch.await();
            Thread.sleep(10000);
        }

        private static class WebSocketClient implements WebSocket.Listener {
            private final CountDownLatch latch;

            public WebSocketClient(CountDownLatch latch) { this.latch = latch; }

            @Override
            public void onOpen(WebSocket webSocket) {
                System.out.println("onOpen using subprotocol " + webSocket.getSubprotocol());
                WebSocket.Listener.super.onOpen(webSocket);
            }

            @Override
            public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                System.out.println("onText received " + data);
                latch.countDown();
                return WebSocket.Listener.super.onText(webSocket, data, last);
            }

            @Override
            public void onError(WebSocket webSocket, Throwable error) {
                System.out.println("Bad day! " + webSocket.toString());
                WebSocket.Listener.super.onError(webSocket, error);
            }
        }
    }
package armeria;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;

public class ArmeriaServer {
    private final Server server;

    public ArmeriaServer(int port) {
        server = new ServerBuilder().http(port)
                                    .annotatedService("/", new HttpHandler())
                                    .build();
    }

    public void run() {
        CompletableFuture<Void> future = server.start();
        server.nextEventLoop().scheduleWithFixedDelay(HttpHandler::updateDate, 1000, 1000,
                                                      TimeUnit.MILLISECONDS);
        future.join();
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new ArmeriaServer(port).run();
    }
}

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class RestServer {
    public void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
        server.createContext("/login", new LoginHandler());
        server.createContext("/create_card", new CreateCardHandler());
        server.createContext("/show_cards", new ShowCardsHandler());
        server.createContext("/add_money", new LoginHandler());
        server.createContext("/show_money", new LoginHandler());
        server.start();
    }
}

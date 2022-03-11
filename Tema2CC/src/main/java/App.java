import com.homework.Constants;
import com.homework.api.handler.UserHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import static com.homework.Configuration.*;
import static com.homework.Constants.USERS_CONTEXT_PATH;

class App {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(Constants.PORT), 0);
        UserHandler userHandler = new UserHandler(getUserService(), getObjectMapper(), getErrorHandler());
        System.out.println("Initialized server at port 8000! Consult /api/users(/{id}) context!");
        server.createContext(USERS_CONTEXT_PATH, userHandler::handle);
        server.setExecutor(null);
        server.start();
    }
}

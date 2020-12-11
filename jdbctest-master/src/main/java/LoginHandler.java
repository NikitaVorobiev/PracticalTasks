import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.UserModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LoginHandler implements HttpHandler {
    String result;
    OutputStream outputStream;
    StringBuilder json = new StringBuilder();
    InputStream inputStream;
    Scanner s;
    int modifier = -1;
    UserModel user;
    UserDAO userDAO = new UserDAO();
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        outputStream = httpExchange.getResponseBody();
        inputStream = httpExchange.getRequestBody();
        if (httpExchange.getRequestMethod().equals("POST")) {
            Map<String, String> input = new HashMap<>();
            String[] split;
            s = new Scanner(inputStream).useDelimiter("\\A");
            result = s.hasNext() ? s.next() : "";
            split = result.split(",");
            for(String s : split) {
                String[] tmp = s.split(":");
                input.put(tmp[0], tmp[1]);
            }
            if (!login(input.get("login"), input.get("password")))
                json.append("authorisation error");
            else
                json.append("success");
            httpExchange.sendResponseHeaders(200, json.toString().getBytes().length);
            outputStream.write(json.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        }
        outputStream.close();
    }
    public boolean login(String log, String pass){
        try {
            if (modifier != -1)
                return false;
            user = userDAO.getUser(log, pass);
            modifier = user.getModifier();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }
}

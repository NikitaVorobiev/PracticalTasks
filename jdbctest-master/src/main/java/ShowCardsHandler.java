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

public class ShowCardsHandler implements HttpHandler {
    String result;
    OutputStream outputStream;
    StringBuilder json = new StringBuilder();
    InputStream inputStream;
    Scanner s;
    UserModel user;
    UserDAO userDAO = new UserDAO();
    AccountDAO accountDAO = new AccountDAO();
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
            try {
                user = userDAO.getUser(input.get("login"), input.get("password"));
            } catch (SQLException ex) {}
            Map<String, Integer> result = getBalanceByAccount();
            if (result == null)
                json.append("login error");
            else {
                for(Map.Entry<String, Integer> entry : result.entrySet()){
                    json.append("card number = " + entry.getKey())
                        .append(" card value = " + entry.getValue());
                }
            }
            httpExchange.sendResponseHeaders(200, json.toString().getBytes().length);
            outputStream.write(json.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        }
        outputStream.close();
    }
    public Map<String, Integer> getBalanceByAccount(){
        try {
            return accountDAO.getBalanceByAccount(user.getAccount());
        } catch (SQLException ex){
            return null;
        }
    }
}

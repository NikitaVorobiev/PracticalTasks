import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class HandlersTest {

    @Test
    void test1() throws IOException, SQLException {
        AccountDAO accountDAO = new AccountDAO();
        UserDAO usersDAO = new UserDAO();
        accountDAO.create();
        usersDAO.create();
        new RestServer().startServer();
        URL url = new URL("http://localhost:8081/login");
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST");
        con.setDoOutput(true);
        byte[] out = "login:login,password:password".getBytes(StandardCharsets.UTF_8);
        OutputStream outputStream = http.getOutputStream();
        outputStream.write(out);
        outputStream.flush();
        InputStream inputStream = http.getInputStream();
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        //System.out.println("Приход на клиента = " + result);
        assertEquals(result, "success");
    }
    @Test
    void test2() throws IOException, SQLException {
        AccountDAO accountDAO = new AccountDAO();
        UserDAO usersDAO = new UserDAO();
        accountDAO.create();
        usersDAO.create();
        new RestServer().startServer();
        URL url = new URL("http://localhost:8081/create_card");
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST");
        con.setDoOutput(true);
        byte[] out = "login:login,password:password".getBytes(StandardCharsets.UTF_8);
        OutputStream outputStream = http.getOutputStream();
        outputStream.write(out);
        outputStream.flush();
        InputStream inputStream = http.getInputStream();
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        assertEquals(result, "card was added");
    }
    @Test
    void test3() throws IOException, SQLException {
        AccountDAO accountDAO = new AccountDAO();
        UserDAO usersDAO = new UserDAO();
        accountDAO.create();
        String[] input = {"insert into users(username, account, log, pass, modifier) values ('Jhon Smith','12345', 'login', 'password', 0)"};
        usersDAO.create(input);
        new RestServer().startServer();
        URL url = new URL("http://localhost:8081/show_cards");
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST");
        con.setDoOutput(true);
        byte[] out = "login:login,password:password".getBytes(StandardCharsets.UTF_8);
        OutputStream outputStream = http.getOutputStream();
        outputStream.write(out);
        outputStream.flush();
        InputStream inputStream = http.getInputStream();
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        assertEquals(result, "card number = 12345 card value = 10card number = 00000 card value = 11");
    }
}

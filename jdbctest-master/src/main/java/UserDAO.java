import interfaces.UserModel;
import interfaces.UsersDAO;
import org.h2.tools.DeleteDbFiles;
import org.h2.tools.Server;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class UserDAO implements UsersDAO {
    NumberGenerator numberGenerator = NumberGenerator.getGenerator();
    AccountDAO accountDAO = new AccountDAO();
    Server server;
    Connection conn;
    private static final String URL = "jdbc:h2:~/test;AUTO_SERVER=TRUE;Mode=Oracle";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";

    public void openServer() throws SQLException{
        server = Server.createWebServer();
        server.start();
    }
    private Statement createStatement() throws SQLException{
        conn = DriverManager.getConnection(URL);
        Statement statement = conn.createStatement();
        return statement;
    }
    public void closeConnection() throws SQLException{
        conn.close();
        server.stop();
    }
    @Override
    public void create() throws SQLException{
        String[] input = {"insert into users(username, account, log, pass, modifier) values ('Jhon Smith','1111', 'login', 'password', 0)", "insert into users(username, account, log, pass, modifier) values ('Simple Name', '2222', 'login', 'password', 0)", "insert into users(username, log, pass, modifier) values ('Admin Admin', 'root', 'toor', 1)"};
        try (Statement statement = createStatement()) {
            DeleteDbFiles.execute("~", "test", true);
            statement.execute("create table users(id serial, username varchar(255), account varchar(255), log varchar(255), pass varchar(255), modifier int)");
            for (String s : input)
                statement.execute(s);
        }
    }
    @Override
    public void create(String[] input) throws SQLException{
        try (Statement statement = createStatement()) {
            DeleteDbFiles.execute("~", "test", true);
            statement.execute("create table users(id serial, username varchar(255), account varchar(255), log varchar(255), pass varchar(255), modifier int)");
            for (String s : input)
                statement.execute(s);
        }
    }
    @Override
    public List<String> read() throws SQLException{
        try (Statement statement = createStatement()) {
            List<String> result = new LinkedList<>();
            ResultSet resultSet = statement.executeQuery("select id, username, account, modifier from users");
            while (resultSet.next()) {
                Integer tmp = resultSet.getInt("id");
                result.add("id = " + tmp.toString());
                result.add("username = " + resultSet.getString("username"));
                result.add("account = " + resultSet.getString("account"));
                tmp = resultSet.getInt("modifier");
                result.add("modifier = " + tmp.toString());
            }
            return result;
        }
    }

    @Override
    public boolean addUser(String name, String log, String pass, int modifier) throws SQLException {
        try (Statement statement = createStatement()) {
            String account = numberGenerator.getAccount();
            String sql = "insert into users(username, account, log, pass, modifier) values (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = statement.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, account);
            preparedStatement.setString(3, log);
            preparedStatement.setString(4, pass);
            preparedStatement.setInt(5, modifier);
            return preparedStatement.execute();
        }
    }

    @Override
    public boolean deleteUser(int id) throws SQLException{
        try (Statement statement = createStatement()) {
            String sql = "select account, modifier from users where id = (?)";
            PreparedStatement preparedStatement = statement.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int modifier = resultSet.getInt("modifier");
                if (modifier == 0) {
                    String account = resultSet.getString("account");
                    accountDAO.deleteAccount(account);
                }
            }
            sql = "delete from users where id = (?)";
            preparedStatement = statement.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, id);
            return preparedStatement.execute();
        }
    }
    @Override
    public boolean deleteUser(int id, String card) throws SQLException{
        try (Statement statement = createStatement()) {
            String sql = "select account, modifier from users where id = (?)";
            PreparedStatement preparedStatement = statement.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int modifier = resultSet.getInt("modifier");
                if (modifier == 0) {
                    String account = resultSet.getString("account");
                    accountDAO.deleteAccount(account, card);
                }
            }
            sql = "delete from users where id = (?)";
            preparedStatement = statement.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, id);
            return preparedStatement.execute();
        }
    }

    @Override
    public UserModel getUser(String log, String pass) throws SQLException{
        User result = null;
        try(Statement statement = createStatement()){
            String sql = "select * from users where log = (?) and pass = (?)";
            PreparedStatement preparedStatement = statement.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, log);
            preparedStatement.setString(2, pass);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                result = User.newBuilder().setAccount(resultSet.getString("account"))
                        .setModifier(resultSet.getInt("modifier"))
                        .setName(resultSet.getString("username"))
                        .setCards(accountDAO.getCards(resultSet.getString("account")).toArray(new String[0]))
                        .build();
        }
        return result;
    }
}

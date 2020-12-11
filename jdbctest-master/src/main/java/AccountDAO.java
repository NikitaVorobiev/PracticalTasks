import interfaces.AccountsDAO;
import org.h2.tools.DeleteDbFiles;
import org.h2.tools.Server;

import java.sql.*;
import java.util.*;

public class AccountDAO implements AccountsDAO {
    NumberGenerator numberGenerator = NumberGenerator.getGenerator();
    Connection conn;
    Server server;
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
        String[] input = {"insert into accounts(account, card, sum) values ('12345','12345', 10)", "insert into accounts(account, card, sum) values ('12345','00000', 11)", "insert into accounts(account, card, sum) values ('11','01', 1)"};
        try (Statement statement = createStatement()){
            DeleteDbFiles.execute("~", "test", true);
            statement.execute("create table accounts(id serial, account varchar(255), card varchar(255), sum int)");
            for (String s : input)
                statement.execute(s);
        }
    }
    public void create(String[] input) throws SQLException{
        try (Statement statement = createStatement()){
            DeleteDbFiles.execute("~", "test", true);
            statement.execute("create table accounts(id serial, account varchar(255), card varchar(255), sum int)");
            for (String s : input)
                statement.execute(s);
        }
    }
    @Override
    public List<String> read() throws SQLException{
        List<String> result = new LinkedList<>();
        try(Statement statement = createStatement()) {
            ResultSet resultSet = statement.executeQuery("select id, account, card, sum from accounts ");
            while (resultSet.next()) {
                Integer tmp = resultSet.getInt("id");
                result.add("id = " + tmp.toString());
                result.add("account = " + resultSet.getString("account"));
                result.add("card = " + resultSet.getString("card"));
                tmp = resultSet.getInt("sum");
                result.add("sum = " + tmp.toString());
            }
        }
        return result;
    }
    @Override
    public boolean createCard(String account) throws SQLException{
    //public boolean createCard(String account){
        try(Statement statement = createStatement()) {
            String cardNumber = numberGenerator.getCard();
            String sql = "insert into accounts(account, card, sum) values (?, ?, 0)";
            PreparedStatement preparedStatement = statement.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, account);
            preparedStatement.setString(2, cardNumber);
            preparedStatement.execute();
            return true;
        } catch (SQLException ex){
            return false;
        }
    }
    @Override
    public List<String> getCards(String account) throws SQLException{
        try(Statement statement = createStatement()) {
            List<String> result = new LinkedList<>();
            String sql = "select card, sum from accounts where account = (?)";
            PreparedStatement preparedStatement = statement.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, account);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                result.add(resultSet.getString("card"));
            return result;
        }
    }

    @Override
    public Map<String, Integer> getBalanceByAccount(String account) throws SQLException{
        try(Statement statement = createStatement()) {
            Map<String, Integer> result = new HashMap<>();
            String sql = "select card, sum from accounts where account = (?)";
            PreparedStatement preparedStatement = statement.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, account);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                result.put((resultSet.getString("card")), resultSet.getInt("sum"));
            return result;
        }
    }

    @Override
    public int getBalanceByCardNumber(String cardNumber) throws SQLException{
        try(Statement statement = createStatement()) {
            int result = 0;
            String sql = "select sum from accounts where card = (?)";
            PreparedStatement preparedStatement = statement.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, cardNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                result = resultSet.getInt("sum");
            return result;
        }
    }

    @Override
    public boolean updateBalance(String cardNumber, int sum) throws SQLException{
        try(Statement statement = createStatement()) {
            int result = getBalanceByCardNumber(cardNumber) + sum;
            String sql = "update accounts set sum = (?) where card = (?)";
            PreparedStatement preparedStatement = statement.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, result);
            preparedStatement.setString(2, cardNumber);
            return preparedStatement.execute();
        }
    }

    @Override
    public boolean deleteCard(String deleteCardNumber, String cardToTrans) throws SQLException{
        try(Statement statement = createStatement()) {
            int sumFrom = getBalanceByCardNumber(deleteCardNumber);
            updateBalance(cardToTrans, sumFrom);
            String sql = "delete from accounts where card = (?)";
            PreparedStatement preparedStatement = statement.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, deleteCardNumber);
            return preparedStatement.execute();
        }
    }

    @Override
    public boolean deleteAccount(String account) throws SQLException{
        try(Statement statement = createStatement()) {
            String sql = "delete from accounts where account = (?)";
            PreparedStatement preparedStatement = statement.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, account);
            return preparedStatement.execute();
        }
    }

    @Override
    public boolean deleteAccount(String account, String cardNumber) throws SQLException{
        try(Statement statement = createStatement()) {
            int sumFrom = 0;
            Map<String, Integer> cards = getBalanceByAccount(account);
            Iterator<Map.Entry<String, Integer>> iterator = cards.entrySet().iterator();
            while (iterator.hasNext())
                sumFrom += iterator.next().getValue();
            updateBalance(cardNumber, sumFrom);
            String sql = "delete from accounts where account = (?)";
            PreparedStatement preparedStatement = statement.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, account);
            return preparedStatement.execute();
        }
    }
}

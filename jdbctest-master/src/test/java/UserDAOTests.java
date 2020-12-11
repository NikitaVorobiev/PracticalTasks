import interfaces.UserModel;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class UserDAOTests {
    @Test
    void test1() throws SQLException {
        UserDAO userDAO = new UserDAO();
        userDAO.openServer();
        String[] testOut = {"id = 1", "username = Jhon Smith", "account = 1111","modifier = 0", "id = 2", "username = Simple Name", "account = 2222","modifier = 0", "id = 3", "username = Admin Admin", "account = null","modifier = 1"};
        String[] testRes;
        userDAO.create();
        testRes = userDAO.read().toArray(new String[0]);
        assertArrayEquals(testOut, testRes);
        userDAO.closeConnection();
    }
    @Test
    void test2() throws SQLException {
        UserDAO userDAO = new UserDAO();
        userDAO.openServer();
        String[] input = {"insert into users(username, account, log, pass, modifier) values ('Jhon Smith','1111', 'login', 'password', 0)"};
        String[] testOut = {"id = 1", "username = Jhon Smith", "account = 1111","modifier = 0"};
        String[] testRes;
        userDAO.create(input);
        testRes = userDAO.read().toArray(new String[0]);
        assertArrayEquals(testOut, testRes);
        userDAO.closeConnection();
    }
    @Test
    void test3() throws SQLException{
        UserDAO userDAO = new UserDAO();
        userDAO.openServer();
        String[] input = {"insert into users(username, account, log, pass, modifier) values ('Jhon Smith','1111', 'login', 'password', 0)"};
        String[] testOut = {"id = 1", "username = Jhon Smith", "account = 1111","modifier = 0", "id = 2", "username = Simple Name", "account = 00000000000000000000","modifier = 0"};
        String[] testRes;
        userDAO.create(input);
        userDAO.addUser("Simple Name", "login", "password", 0);
        testRes = userDAO.read().toArray(new String[0]);
        assertArrayEquals(testOut, testRes);
        userDAO.closeConnection();
    }
    @Test
    void test4() throws SQLException{
        UserDAO userDAO = new UserDAO();
        AccountDAO accountDAO = new AccountDAO();
        String[] create = {"insert into accounts(account, card, sum) values ('1111','12345', 10)"};
        accountDAO.create(create);
        userDAO.openServer();
        String[] testOut = {"id = 1", "username = Jhon Smith", "account = 1111","modifier = 0"};
        String[] testRes;
        userDAO.create();
        userDAO.deleteUser(2);
        userDAO.deleteUser(3);
        testRes = userDAO.read().toArray(new String[0]);
        assertArrayEquals(testOut, testRes);
        userDAO.closeConnection();
    }
    @Test
    void test5() throws SQLException{
        UserDAO userDAO = new UserDAO();
        AccountDAO accountDAO = new AccountDAO();
        String[] create = {"insert into accounts(account, card, sum) values ('1111','12345', 10)", "insert into accounts(account, card, sum) values ('2222','22', 10)"};
        accountDAO.create(create);
        userDAO.openServer();
        String[] testOut = {"id = 1", "username = Jhon Smith", "account = 1111","modifier = 0"};
        String[] testRes;
        userDAO.create();
        userDAO.deleteUser(2, "22");
        userDAO.deleteUser(3);
        testRes = userDAO.read().toArray(new String[0]);
        assertArrayEquals(testOut, testRes);
        userDAO.closeConnection();
    }
    @Test
    void test6() throws SQLException {
        UserDAO userDAO = new UserDAO();
        AccountDAO accountDAO = new AccountDAO();
        userDAO.openServer();
        accountDAO.create();
        String[] input = {"insert into users(username, account, log, pass, modifier) values ('Jhon Smith','12345', 'login', 'password', 0)"};
        userDAO.create(input);
        UserModel realRes = userDAO.getUser("login", "password");
        UserModel testRes = User.newBuilder().setAccount("12345")
                .setName("Jhon Smith")
                .setModifier(0)
                .setCards(new String[]{"12345", "00000"})
                .build();
        assertEquals(realRes, testRes);
        userDAO.closeConnection();
    }
}

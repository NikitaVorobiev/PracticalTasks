import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class AccountDAOTests {
    @Test
    void test1() throws SQLException{
        AccountDAO accountDAO = new AccountDAO();
        accountDAO.openServer();
        String[] testOut = {"id = 1", "account = 12345", "card = 12345", "sum = 10", "id = 2", "account = 12345", "card = 00000", "sum = 11", "id = 3", "account = 11", "card = 01", "sum = 1"};
        String[] testRes;
        accountDAO.create();
        testRes = accountDAO.read().toArray(new String[0]);
        assertArrayEquals(testOut, testRes);
        accountDAO.closeConnection();
    }
    @Test
    void test2() throws SQLException{
        AccountDAO accountDAO = new AccountDAO();
        accountDAO.openServer();
        String[] testInput = {"insert into accounts(account, card, sum) values ('12345','12345', 10)"};
        String[] testOut = {"id = 1", "account = 12345", "card = 12345", "sum = 10"};
        String[] testRes;
        accountDAO.create(testInput);
        testRes = accountDAO.read().toArray(new String[0]);
        assertArrayEquals(testOut, testRes);
        accountDAO.closeConnection();
    }
    @Test
    void test3() throws SQLException{
        AccountDAO accountDAO = new AccountDAO();
        accountDAO.openServer();
        String[] testInput = {"insert into accounts(account, card, sum) values ('12345','12345', 10)"};
        String[] testRes = {"id = 1", "account = 12345", "card = 12345", "sum = 10", "id = 2", "account = 12345", "card = 0000000000000000", "sum = 0"};
        String[] testOut;
        accountDAO.create(testInput);
        accountDAO.createCard("12345");
        testOut = accountDAO.read().toArray(new String[0]);
        accountDAO.closeConnection();
        assertArrayEquals(testOut, testRes);
    }
    @Test
    void test4() throws SQLException{
        AccountDAO accountDAO = new AccountDAO();
        accountDAO.openServer();
        String[] testInput = {"insert into accounts(account, card, sum) values ('12345','1111', 11)", "insert into accounts(account, card, sum) values ('12345','2222', 22)"};
        String[] testRes = {"1111", "2222"};
        String[] testOut;
        accountDAO.create(testInput);
        testOut = accountDAO.getCards("12345").toArray(new String[0]);
        accountDAO.closeConnection();
        assertArrayEquals(testOut, testRes);
    }
    @Test
    void test5() throws SQLException{
        AccountDAO accountDAO = new AccountDAO();
        accountDAO.openServer();
        String[] testInput = {"insert into accounts(account, card, sum) values ('12345','1111', 11)", "insert into accounts(account, card, sum) values ('12345','2222', 22)"};
        Map testRes;
        Map<String, Integer> testOut = new HashMap<>();
        testOut.put("1111", 11);
        testOut.put("2222", 22);
        accountDAO.create(testInput);
        testRes = accountDAO.getBalanceByAccount("12345");
        accountDAO.closeConnection();
        assertEquals(testOut, testRes);
    }
    @Test
    void test6() throws SQLException{
        AccountDAO accountDAO = new AccountDAO();
        accountDAO.openServer();
        String[] testInput = {"insert into accounts(account, card, sum) values ('12345','1111', 11)"};
        int testRes = 11;
        int testOut;
        accountDAO.create(testInput);
        testOut = accountDAO.getBalanceByCardNumber("1111");
        accountDAO.closeConnection();
        assertEquals(testOut, testRes);
    }
    @Test
    void test7() throws SQLException{
        AccountDAO accountDAO = new AccountDAO();
        accountDAO.openServer();
        String[] testInput = {"insert into accounts(account, card, sum) values ('12345','1111', 11)"};
        int testRes = 20;
        int testOut;
        accountDAO.create(testInput);
        accountDAO.updateBalance("1111", 9);
        testOut = accountDAO.getBalanceByCardNumber("1111");
        accountDAO.closeConnection();
        assertEquals(testOut, testRes);
    }
    @Test
    void test8() throws SQLException{
        AccountDAO accountDAO = new AccountDAO();
        accountDAO.openServer();
        String[] testInput = {"insert into accounts(account, card, sum) values ('12345','1111', 11)", "insert into accounts(account, card, sum) values ('12345','2222', 22)"};
        String[] testRes = {"id = 2", "account = 12345", "card = 2222", "sum = 33"};
        String[] testOut;
        accountDAO.create(testInput);
        accountDAO.deleteCard("1111", "2222");
        testOut = accountDAO.read().toArray(new String[0]);
        accountDAO.closeConnection();
        assertArrayEquals(testOut, testRes);
    }
    @Test
    void test9() throws SQLException{
        AccountDAO accountDAO = new AccountDAO();
        accountDAO.openServer();
        String[] testInput = {"insert into accounts(account, card, sum) values ('12345','1111', 11)", "insert into accounts(account, card, sum) values ('54321','2222', 22)"};
        String[] testRes = {"id = 2", "account = 54321", "card = 2222", "sum = 22"};
        String[] testOut;
        accountDAO.create(testInput);
        accountDAO.deleteAccount("12345");
        testOut = accountDAO.read().toArray(new String[0]);
        accountDAO.closeConnection();
        assertArrayEquals(testOut, testRes);
    }
    @Test
    void test10() throws SQLException{
        AccountDAO accountDAO = new AccountDAO();
        accountDAO.openServer();
        String[] testInput = {"insert into accounts(account, card, sum) values ('12345','1111', 11)", "insert into accounts(account, card, sum) values ('54321','2222', 22)"};
        String[] testRes = {"id = 2", "account = 54321", "card = 2222", "sum = 33"};
        String[] testOut;
        accountDAO.create(testInput);
        accountDAO.deleteAccount("12345", "2222");
        testOut = accountDAO.read().toArray(new String[0]);
        accountDAO.closeConnection();
        assertArrayEquals(testOut, testRes);
    }
}

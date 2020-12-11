import interfaces.UserModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Main {
    int modifier = 0;
    UserModel user;
    UserDAO userDAO = new UserDAO();
    AccountDAO accountDAO = new AccountDAO();

    public static void main(String[] args){
        try {
            new RestServer().startServer();
        } catch (IOException ex){
            System.out.println("Не удалось запустить сервер");
        }
    }
    public boolean createUser(String username, String log, String pass){
        try {
            if (modifier != 1)
                return false;
            if (!userDAO.addUser(username, log, pass, 0))
                return false;
        } catch (SQLException ex){
            return false;
        }
        return true;
    }
    public boolean deleteUser(int id){
        try {
            if (modifier != 1)
                return false;
            if (!userDAO.deleteUser(id))
                return false;
        }catch (SQLException ex){
            return false;
        }
        return true;
    }
    public boolean deleteUser(int id, String card) {
        try {
            if (modifier != 1)
                return false;
            if (!userDAO.deleteUser(id, card))
                return false;
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }
    public List<String> getCards(){
        try {
            if(modifier != 0)
                return null;
            return accountDAO.getCards(user.getAccount());
        } catch (SQLException ex){
            return null;
        }
    }
    public int getBalanceByCardNumber(String cardNumber){
        try {
            int marker = 0;
            if(modifier != 0)
                return -1;
            for(String c : user.getCards())
                if (c.equals(cardNumber))
                    marker++;
            if(marker == 0)
                return -1;
            return accountDAO.getBalanceByCardNumber(cardNumber);
        } catch (SQLException ex){
            return -1;
        }
    }
    public boolean updateBalance(String cardNumber, int sum){
        try {
            if(sum < 0)
                return false;
            return accountDAO.updateBalance(cardNumber, sum);
        } catch (SQLException ex){
            return false;
        }
    }
    public boolean deleteCard(String deleteCardNumber, String cardToTrans){
        try {
            int marker = 0;
            if(modifier != 0)
                return false;
            for(String c : user.getCards())
                if (c.equals(deleteCardNumber))
                    marker++;
            if(marker == 0)
                return false;
            return accountDAO.deleteCard(deleteCardNumber, cardToTrans);
        } catch (SQLException ex){
            return false;
        }
    }
}

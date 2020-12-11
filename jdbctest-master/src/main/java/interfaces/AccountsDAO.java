package interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface AccountsDAO {
    List<String> read() throws SQLException; //считать все записи
    void create() throws SQLException; //Create (создание)
    void create(String[] input) throws SQLException; //Create (создание) с перегрузкой - содержанием
    boolean createCard(String account) throws SQLException; //выпуск новой карты по счету (удалось - нет)
    List<String> getCards(String account) throws SQLException; //получить список карт по счету
    Map<String, Integer> getBalanceByAccount(String account) throws SQLException; //получить список счетов по счету (номер карты - сумма)
    int getBalanceByCardNumber(String curdNumber) throws SQLException; //получить счет по номеру карты
    boolean updateBalance(String cardNumber, int sum) throws SQLException; //пополнение карты (удалось - нет)
    boolean deleteCard(String deleteCardNumber, String cardToTrans) throws SQLException; //удаление карты (удаляемая карта, куда перевести деньги) (удалось - нет)
    boolean deleteAccount(String account) throws SQLException; //удаление счета (удалось - нет)
    boolean deleteAccount(String account, String cardNumber) throws SQLException; //удаление счета с переводом денег на карту (удаляемый счет, номер карты  куда переводить деньги) (удалось - нет)
}

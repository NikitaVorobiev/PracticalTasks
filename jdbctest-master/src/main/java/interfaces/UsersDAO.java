package interfaces;

import java.sql.SQLException;
import java.util.List;

public interface UsersDAO {
    List<String> read() throws SQLException; //считать все записи
    void create() throws SQLException; //Заполняет таблицу данными
    void create(String[] input) throws SQLException; //Заполняет таблицу данными
    boolean addUser(String name, String log, String pass,  int modifier) throws SQLException; //Создает юзера, у обычного генерится номер счета, у админа - нет
    boolean deleteUser(int id) throws SQLException; //удаляет юзера, у обычного есть перегруженный метод, передающий деньги на другую карту
    boolean deleteUser(int id, String cardNumber) throws SQLException; //удаляет юзера, у обычного есть перегруженный метод, передающий деньги на другую карту
    UserModel getUser(String log, String pass) throws SQLException; //возвращает юзера по логину-паролю, либо налл, если такой пары не существует
}

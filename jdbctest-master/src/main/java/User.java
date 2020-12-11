import interfaces.UserModel;

import java.util.Arrays;
import java.util.Objects;

public class User implements UserModel {
    private int modifier = 0;
    private String name;
    private String account;
    private String[] cards;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return modifier == user.modifier &&
                name.equals(user.name) &&
                account.equals(user.account) &&
                Arrays.equals(cards, user.cards);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(modifier, name, account);
        result = 31 * result + Arrays.hashCode(cards);
        return result;
    }

    private User() {

    }

    public class Builder{
        private Builder(){

        }
        public Builder setName(String name){
            User.this.name = name;
            return this;
        }
        public Builder setModifier(int modifier){
            User.this.modifier = modifier;
            return this;
        }
        public Builder setAccount(String account){
            User.this.account = account;
            return this;
        }
        public Builder setCards(String[] cards){
            User.this.cards = cards;
            return this;
        }
        public User build(){
            return User.this;
        }
    }

    @Override
    public int getModifier() {
        return modifier;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAccount() {
        return account;
    }

    @Override
    public String[] getCards(){
        return cards;
    }

    public static Builder newBuilder(){
        return new User().new Builder();
    }
}

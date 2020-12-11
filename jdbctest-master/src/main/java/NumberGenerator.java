import java.math.BigInteger;

public class NumberGenerator {
    private static NumberGenerator numberGenerator;
    private static BigInteger account = BigInteger.ZERO; //20 цифр
    private static Long card = Long.valueOf(0); //16 цифр

    private NumberGenerator(){
    }
    public static synchronized NumberGenerator getGenerator(){
        if (numberGenerator == null)
            numberGenerator = new NumberGenerator();
        return numberGenerator;
    }
    public String getAccount(){
        String result = "";
        for (int i = 0; i < 20 - account.toString().length(); i++)
            result = result.concat("0");
        result = result.concat(account.toString());
        account = account.add(BigInteger.ONE);
        return result;
    }
    public String getCard(){
        String result = "";
        for(int i = 0; i < 16 - card.toString().length(); i++)
            result = result.concat("0");
        result = result.concat(card.toString());
        card++;
        return result;
    }
}

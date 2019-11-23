import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
        System.out.println("Выберите метод:\n" +
                "1 - ро-метод Полларда\n" +
                "2 - (p - 1)-метод Полларда\n" +
                "3 - метод непрерывных дробей");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String result = "";
            int c = Integer.parseInt(reader.readLine());
            switch (c) {
                case 1:{
                    System.out.println("Введите нечетное составное n:");
                    BigInteger n = new BigInteger(reader.readLine());
                    if (Help.isPrime(n.intValue(), 100)) {
                        System.out.println("Число простое! Введите составное!");
                        return;
                    }
                    result = PollardRo.expand(n);
                    break;
                }
                case 2:{
                    System.out.println("Введите нечетное составное n:");
                    BigInteger n = new BigInteger(reader.readLine());
                    if (Help.isPrime(n.intValue(), 100)) {
                        System.out.println("Число простое! Введите составное!");
                        return;
                    }
                    result = PollardPMinusOne.expand(n);
                    break;
                }
                case 3:{
                    System.out.println("Введите составное n:");
                    BigInteger n = new BigInteger(reader.readLine());
                    if (Help.isPrime(n.intValue(), 100)) {
                        System.out.println("Число простое! Введите составное!");
                        return;
                    }
                    ChainFactions.expand(n.intValue(), 7);
                    break;
                }
                default:{
                    System.out.println("Ошибка! Введите верную команду!!!");
                    return;
                }
            }
            System.out.println(result);
        } catch (IOException e) {
            System.out.println("Ошибка ввода");
        }
    }
}

import java.math.BigInteger;
import java.util.Random;

public class PollardRo {

    public static String expand(BigInteger n) {
        Random random = new Random();
        BigInteger c = (BigInteger.ONE.add(new BigInteger(n.bitLength(), random))).mod(n);
        BigInteger a = c;
        BigInteger b = c;
        BigInteger d;
        String p;
        while (true) {
            a = f(a).mod(n);
            b = f(b).mod(n);
            b = f(b).mod(n);
            d = a.subtract(b).gcd(n);

            if (d.equals(n)) {
                p = "Делитель не найден";
                break;
            }

            if (BigInteger.ONE.compareTo(d) == -1 && d.compareTo(n) == -1) {
                p = d.toString();
                break;
            }
        }
        return p;
    }

    private static BigInteger f(BigInteger x) {
        Random random = new Random();
        return x.multiply(x).add(BigInteger.ONE).add(new BigInteger(x.bitLength(), random));
    }
}

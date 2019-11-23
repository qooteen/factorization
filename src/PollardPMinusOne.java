import java.math.BigInteger;
import java.util.List;
import java.util.Random;

public class PollardPMinusOne {
    private static final BigInteger TWO = new BigInteger("2");
    private static final String B_FILE = "B.txt";
    private static final List<BigInteger> B = Help.readFromFile(B_FILE);

    public static String expand(BigInteger n) {
        Random random = new Random();
        BigInteger a = TWO.add(new BigInteger(n.bitLength(), random).mod(n.subtract(BigInteger.ONE)));
        BigInteger d = a.gcd(n);
        String p;
        if (d.compareTo(TWO) == 1 || d.equals(TWO)) {
            p = d.toString();
            return p;
        }
        for (int i = 0; i < 21; i++) {
            int l = (int)(Math.log(n.longValue()) / Math.log(B.get(i).longValue()));
            a = a.modPow(B.get(i).pow(l), n);
        }
        d = a.subtract(BigInteger.ONE).gcd(n);
        if (d.equals(BigInteger.ONE) || d.equals(n)) {
            return "Делитель не найден";
        }
        p = d.toString();
        return p;
    }
}

import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class Help {

    public static boolean isPrime(int p, int roundCount){
        int numberMinus1 = p - 1;
        int t = numberMinus1;
        int s = 0;

        while (t % 2 == 0) {
            t /= 2;
            s++;
        }

        for (int i = 0; i < roundCount; i++){
            Random random = new Random();
            int a = 2 + random.nextInt(p - 2);
            int x = pow(a, t, p);

            if (x == 1 || x == numberMinus1) continue;

            boolean flagToStop = true;

            for (int j = 0; j < s - 1; j++){
                x = pow(x, 2, p);

                if (x == 1){
                    return false;
                }

                if (x == numberMinus1){
                    flagToStop = false;
                    break;
                }
            }

            if (flagToStop) {
                return false;
            }
        }

        return true;
    }

    public static int pow(int a, int pows, int p){
        int res = 1;

        for (int i = 0; i < pows; i++){
            res *= a;
            res %= p;
        }
        return  res;
    }

    public static int gcd(int a, int b) {
        if (b == 0)
            return Math.abs(a);
        return gcd(b, a % b);
    }

    public static List<BigInteger> readFromFile(String file) {
        List<BigInteger> list = new ArrayList<>();
        try {
            String result = "";
            Scanner scanner = new Scanner(new FileReader(file));
            while (scanner.hasNext()) {
                result += scanner.nextLine();
            }
            String[] s = result.split("\t");
            list = Arrays.asList(s).stream().map(x -> new BigInteger(x)).collect(Collectors.toList());
            scanner.close();
        }
        catch (IOException e) {
            System.out.println("Error reading from file");
        }
        return list;
    }

    public static int jacobi(int a, int b) {

        if (gcd(a, b) != 1) return 0;
        int r = 1;

        if (a < 0) {
            a = -a;
            if (b % 4 == 3) {
                r = -r;
            }
        }

        while (a != 0) {
            int t = 0;
            while (a % 2 == 0){
                t++;
                a /= 2;
            }

            if (t % 2 != 0) {
                int mod = b % 8;

                if (mod == 3 || mod == 5) {
                    r = -r;
                }
            }

            int modA = a % 4;

            if ((modA == b % 4) && modA  == 3) {
                r = -r;
            }
            int c = a;
            a = b % c;
            b = c;
        }
        return r;
    }
}

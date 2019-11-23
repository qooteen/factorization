import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ChainFactions {

    private static final String B_FILE = "B.txt";
    private static final List<BigInteger> B = Help.readFromFile(B_FILE);
    private static final BigInteger TWO = new BigInteger("2");

    public static List<BigInteger> choiceBase(int m, int baseSize) {
        List<BigInteger> base = new ArrayList<>();
        base.add(BigInteger.valueOf(-1));
        for (int i = 0; base.size() - 1 != baseSize; i++) {
            if (Help.jacobi(m, B.get(i).intValue()) == 1) {
                base.add(B.get(i));
            }
        }
        return base;
    }

    public static void expand(int m, int baseSize) {
        List<BigInteger> base = choiceBase(m, baseSize);
        baseSize++;
        List<Integer> chain = new ArrayList<>();
        BigInteger bigM = BigInteger.valueOf(m);
        int[][] equations = new int[baseSize][baseSize];
        BigInteger[] xvals = new BigInteger[baseSize];
        for (int i = 0; i < baseSize; i++) {
            for (int j = 0; j < baseSize; j++) {
                equations[i][j] = 0;
            }
        }
        int[] tempEq = new int[baseSize];
        double doubleValue = Math.sqrt(m);
        int i = 0;
        int k = 0;
        int intValue = (int)Math.sqrt(m);
        chain.add(intValue);
        List<BigInteger> u = new ArrayList<>();
        u.add(BigInteger.ONE);
        u.add(BigInteger.valueOf(intValue));
        while (i < baseSize) {
            doubleValue = doubleValue - (double)intValue;
            intValue = (int)(1 / doubleValue);
            doubleValue = 1 / doubleValue;
            chain.add(intValue);
            u.add(BigInteger.valueOf(chain.get(k + 1)).multiply(u.get(k + 1)).add(u.get(k)));
            BigInteger vi = u.get(k + 2).modPow(TWO, bigM);
            BigInteger viCopy = vi;
            if (vi.compareTo(BigInteger.ZERO) == 0) {
                continue;
            }

            for (int j = 0; j < tempEq.length; j++) {
                tempEq[j] = 0;
            }

            for (int j = 0; j < base.size() - 1; j++) {
                if (vi.divideAndRemainder(base.get(baseSize - 1 - j))[1].equals(BigInteger.ZERO) && vi.intValue() != 1) {
                    while (vi.divideAndRemainder(base.get(baseSize - 1 - j))[1].equals(BigInteger.ZERO) && vi.intValue() != 1) {
                        tempEq[baseSize - 1 - j]++;
                        vi = vi.divideAndRemainder(base.get(baseSize - 1 - j))[0];
                    }
                }
            }
            if (vi.intValue() == 1) {
                xvals[i] = u.get(k + 2);
                System.arraycopy(tempEq, 0, equations[i], 0, base.size());
                i++;
            }
            else {
                for (int j = 0; j < tempEq.length; j++) {
                    tempEq[j] = 0;
                }
                BigInteger t = bigM.subtract(viCopy);
                for (int j = 0; j < base.size() - 1; j++) {
                    BigInteger p = base.get(baseSize - 1 - j);
                    if (t.divideAndRemainder(p)[1].equals(BigInteger.ZERO) && viCopy.intValue() != 1) {
                        while (t.divideAndRemainder(p)[1].equals(BigInteger.ZERO) && viCopy.intValue() != 1) {
                            tempEq[baseSize - 1 - j]++;
                            t = t.divideAndRemainder(p)[0];
                        }
                    }
                }
                if (t.intValue() == 1) {
                    xvals[i] = u.get(k + 2);
                    tempEq[0]++;
                    System.arraycopy(tempEq, 0, equations[i], 0, base.size());
                    i++;
                }
            }
            k++;
        }
        int[][] eq = new int[baseSize][baseSize];
        for (int h = 0; h < baseSize; h++)
            for (int j = 0; j < baseSize; j++) {
                eq[h][j] = equations[h][j];
        }
        int[][] mod2equations = mod2eqs(equations, baseSize);
        int[][] edmat = edmatrix(baseSize);
        int[][] matperest = gauss(mod2equations, edmat, baseSize, baseSize, true);
        int[][] edmatperest = gauss(mod2equations, edmat, baseSize, baseSize, false);

        if (!combineeqs(xvals, eq, edmatperest, matperest, baseSize, bigM, base)) {
            System.out.println("Не раскладывается");
        }
    }
    public static int[][] mod2eqs(int[][] equations, int arraySize) {
        for (int i = 0; i < arraySize; i++) {
            for (int j = 0; j < arraySize; j++) {
                if (equations[i][j] % 2 == 0) {
                    equations[i][j] = 0;
                } else {
                    equations[i][j] = 1;
                }
            }
        }
        return equations;
    }

    public static boolean combineeqs(BigInteger[] xVals, int[][] equations, int[][] edmatperest, int[][] iAdjusted, int arraySize, BigInteger n, List<BigInteger> base) {
        for (int i = 0; i < arraySize; i++) {
            BigInteger x = BigInteger.ONE;
            BigInteger y = BigInteger.ONE;
            if (isRowEmpty(edmatperest[i])) {
                for (int j = 0; j < arraySize; j++) {
                    if (iAdjusted[i][j] == 1) {
                        x = x.multiply(xVals[j]);

                        for (int k = 1; k < arraySize; k++) {
                            y = y.multiply(base.get(k).pow(equations[j][k]));
                        }
                    }
                }
            }

                x = x.mod(n);
                y = sqrt(y).mod(n);


                if (x.compareTo(y) != 0) {
                    BigInteger gcd = x.subtract(y).abs().gcd(n);
                    if (gcd.compareTo(BigInteger.ONE) != 0) {
                        BigInteger factor = n.divide(gcd);
                        System.out.println("Факторизация: " + gcd + " " + factor);
                        return true;
                    }
                }

        }
        return false;
    }

    public static boolean isRowEmpty(int[] row) {
        for (int i = 0; i < row.length; i++) {
            if (row[i] != 0) {
                return false;
            }
        }
        return true;
    }

    public static int[][] edmatrix(int size) {
        int[][] matrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = (i == j) ? 1 : 0;
            }
        }
        return matrix;
    }

    public static BigInteger sqrt(BigInteger n) {
        BigInteger a = BigInteger.ONE;
        BigInteger b = new BigInteger(n.shiftRight(5).add(new BigInteger("8")).toString());
        while (b.compareTo(a) >= 0) {
            BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
            if (mid.multiply(mid).compareTo(n) > 0) {
                b = mid.subtract(BigInteger.ONE);
            } else {
                a = mid.add(BigInteger.ONE);
            }
        }
        return a.subtract(BigInteger.ONE);
    }


    public static int[][] gauss(int[][] matrix, int[][] edmatrix, int n, int m, boolean i) {
        for (int col = 0; col < m; col++) {

            if (matrix[col][col] == 0) {
                for (int row = col + 1; row < n; row++) {
                    if (matrix[row][col] == 1) {

                        for (int k = 0; k < m; k++) {
                            int tmp = matrix[row][k];
                            matrix[row][k] = matrix[col][k];
                            matrix[col][k] = tmp;
                        }

                        for (int k = 0; k < n; k++) {
                            int tmp = edmatrix[row][k];
                            edmatrix[row][k] = edmatrix[col][k];
                            edmatrix[col][k] = tmp;
                        }

                        break;
                    }
                }
            }

            if (matrix[col][col] == 1) {
                for (int row = col + 1; row < n; row++) {

                    if (matrix[row][col] == 1) {
                        for (int k = 0; k < m; k++) {
                            matrix[row][k] = (byte) ((matrix[row][k]
                                    + matrix[col][k]) % 2);
                        }
                        for (int k = 0; k < m; k++) {
                            edmatrix[row][k] = (byte) ((edmatrix[row][k]
                                    + edmatrix[col][k]) % 2);
                        }
                    }
                }
            }
        }
        if (i) {
            return edmatrix;
        } else {
            return matrix;
        }
    }

}

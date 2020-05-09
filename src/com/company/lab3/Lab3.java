package com.company.lab3;

import com.company.helpers.EpsilonHelper;
import com.company.helpers.InputReader;
import com.company.helpers.Matrix;
import com.company.helpers.MatrixOperations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Lab3 {
    private Matrix a;
    private final Matrix b;
    private final Matrix c;
    private Matrix x;
    private List<Integer> jBasis;

    private final int n;
    private final int m;

    public Lab3(final String fileName) throws FileNotFoundException {
        final InputReader inputReader = new InputReader(new FileInputStream(fileName));

        n = inputReader.nextInt();
        m = inputReader.nextInt();

        a = new Matrix(inputReader, n, m);
        b = new Matrix(inputReader, 1, n);
        c = new Matrix(inputReader, 1, m);

        x = new Matrix(1, n + m);
        jBasis = new ArrayList<>();
    }

    public List<Object> mainPhase(final Matrix a,
                                  final Matrix c,
                                  final Matrix x,
                                  final List<Integer> jBasis) {
        int n = a.getN();
        int m = a.getM();

        final Matrix aBasis = new Matrix(n, n);
        for (int i = 0; i < n; ++i)
            aBasis.changeColumn(i, jBasis.get(i), a);

        Matrix aBasisInverse = MatrixOperations.getInverseMatrix(new Matrix(aBasis));

        Matrix cBasis = new Matrix(1, n);
        for (int i = 0; i < n; ++i)
            cBasis.set(0, i, c.get(0, jBasis.get(i)));

        while (true) {
            final Matrix u = cBasis.multiply(aBasisInverse);
            final Matrix delta = u.multiply(a).subtract(c);

            final boolean[] used = new boolean[m];
            for (int i : jBasis)
                used[i] = true;

            final List<Integer> jNotBasis = new ArrayList<>();
            for (int i = 0; i < m; ++i) {
                if (!used[i])
                    jNotBasis.add(i);
            }

            int positionNegativeValue = -1;
            for (int i : jNotBasis) {
                if (EpsilonHelper.isNegative(delta.get(0, i))) {
                    positionNegativeValue = i;
                    break;
                }
            }

            if (positionNegativeValue == -1) {
                List<Object> list = new ArrayList<>();
                list.add(x);
                list.add(jBasis);
                return list;
            }

            final Matrix z = aBasisInverse.multiply(a.getColumn(positionNegativeValue));

            final List<Double> q = new ArrayList<>();
            double mn = Double.NaN;
            int pos = -1;
            for (int i = 0; i < z.getN(); ++i) {
                if (EpsilonHelper.isPositive(z.get(i, 0))) {
                    if (Double.isNaN(mn) || x.get(0, jBasis.get(i)) / z.get(i, 0) < mn) {
                        mn = x.get(0, jBasis.get(i)) / z.get(i, 0);
                        pos = i;
                    }
                }
            }

            if (pos == -1) {
                System.out.println("Unbounded");
                System.exit(0);
            }

            for (int i = 0; i < jBasis.size(); ++i) {
                x.set(0, jBasis.get(i), x.get(0, jBasis.get(i)) - mn * z.get(i, 0));
            }

            x.set(0, positionNegativeValue, mn);
            jBasis.set(pos, positionNegativeValue);

            aBasisInverse = MatrixOperations.getInverseMatrix(aBasisInverse, a.getColumn(positionNegativeValue), pos);
            cBasis = new Matrix(1, n);
            for (int i = 0; i < jBasis.size(); ++i)
                cBasis.set(0, i, c.get(0, jBasis.get(i)));
        }
    }

    public List<Object> initialPhase() {
        int n = a.getN();
        int m = a.getM();

        for (int i = 0; i < n; ++i) {
            if (EpsilonHelper.isNegative(b.get(0, i))) {
                b.set(0, i, -b.get(0, i));

                for (int j = 0; j < m; ++j) {
                    a.set(i, j, -a.get(i, j));
                }
            }
        }

        for (int i = 0; i < n; ++i) {
            x.set(0, i + m, b.get(0, i));
        }

        for (int i = 0; i < n; ++i) {
            jBasis.add(m + i);
        }

        Matrix aAux = MatrixOperations.mergeMatrix(a, Matrix.createIdentityMatrix(n));
        final Matrix cAux = new Matrix(1, n + m);
        for (int i = m; i < n + m; ++i)
            cAux.set(0, i, -1);

        List<Object> result = mainPhase(aAux, cAux, x, jBasis);
        x = ((Matrix) result.get(0));
        jBasis = (List<Integer>) result.get(1);

        boolean findNotZeroValue = false;
        for (int i = m; i < n + m; ++i) {
            if (!EpsilonHelper.isZero(x.get(0, i))) {
                findNotZeroValue = true;
                break;
            }
        }

        if (findNotZeroValue) {
            System.out.println("No solution");
            System.exit(0);
        }

        final Matrix newX = new Matrix(1, m);
        for (int i = 0; i < m; ++i)
            newX.set(0, i, x.get(0, i));
        x = newX;

        while (true) {
            boolean find = false;
            for (int i = 0; i < jBasis.size(); ++i) {
                if (jBasis.get(i) < m)
                    continue;

                final int k = jBasis.get(i) - m;
                final Matrix aBasisAux = new Matrix(n, n);
                for (int j = 0; j < n; ++j)
                    aBasisAux.changeColumn(j, jBasis.get(j), aAux);

                final boolean[] used = new boolean[m];
                for (int j : jBasis)
                    used[i] = true;

                boolean isDelete = true;
                for (int j = 0; j < m; ++j) {
                    if (used[j])
                        continue;

                    final Matrix l = MatrixOperations.getInverseMatrix(aBasisAux).multiply(aAux.getColumn(j));

                    if (!EpsilonHelper.isZero(l.get(i, 0))) {
                        isDelete = false;
                        jBasis.set(i, j);
                        break;
                    }
                }

                if (isDelete) {
                    jBasis.remove(i);
                    a = a.remove(i);
                    aAux = aAux.remove(i);
                    --n;
                }
            }

            if (!find) {
                List<Object> list = new ArrayList<>();
                list.add(a);
                list.add(x);
                list.add(jBasis);
                return list;
            }
        }
    }

    public void run() {
        List<Object> initialResult = initialPhase();

        List<Object> mainResult = mainPhase((Matrix) initialResult.get(0), c, (Matrix) initialResult.get(1), (List<Integer>) initialResult.get(2));

        System.out.println("Bounded");

        ((Matrix) mainResult.get(0)).print();
    }
}

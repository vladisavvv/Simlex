package com.company.lab4;

import com.company.helpers.EpsilonHelper;
import com.company.helpers.InputReader;
import com.company.helpers.Matrix;
import com.company.helpers.MatrixOperations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Lab4 {
    private Matrix a;
    private final Matrix b;
    private final Matrix c;
    private List<Integer> jBasis;

    private final int n;
    private final int m;

    public Lab4(final String fileName) throws FileNotFoundException {
        final InputReader inputReader = new InputReader(new FileInputStream(fileName));

        n = inputReader.nextInt();
        m = inputReader.nextInt();

        a = new Matrix(inputReader, n, m);
        b = new Matrix(inputReader, 1, n);
        c = new Matrix(inputReader, 1, m);

        jBasis = new ArrayList<>();
        for (int i = 0; i < n; ++i)
            jBasis.add(inputReader.nextInt());
    }

    public Matrix dualSimplexMethod() {
        final Matrix aBasis = new Matrix(n, n);
        for (int i = 0; i < n; ++i)
            aBasis.changeColumn(i, jBasis.get(i), a);

        Matrix aBasisInverse = MatrixOperations.getInverseMatrix(new Matrix(aBasis));

        Matrix cBasis = new Matrix(1, n);
        for (int i = 0; i < n; ++i)
            cBasis.set(0, i, c.get(0, jBasis.get(i)));

        Matrix y = cBasis.multiply(aBasisInverse);

        while (true) {
            Matrix k = new Matrix(1, m);
            Matrix kBasis = aBasisInverse.multiply(b);

            for (int i = 0; i < jBasis.size(); ++i)
                k.set(0, jBasis.get(i), kBasis.get(i, 0));

            int i = -1;
            for (int j = 0; j < kBasis.getN(); ++j) {
                if (EpsilonHelper.isNegative(kBasis.get(j, 0))) {
                    i = j;
                    break;
                }
            }

            if (i == -1)
                return k;

            final Matrix deltaY = new Matrix(1, n);
            for (int j = 0; j < n; ++j)
                deltaY.set(0, j, aBasisInverse.get(i, j));

            final boolean[] used = new boolean[m];
            for (int j : jBasis)
                used[j] = true;

            final List<Integer> jNotBasis = new ArrayList<>();
            for (int j = 0; j < m; ++j) {
                if (!used[j])
                    jNotBasis.add(j);
            }

            double g0 = Double.POSITIVE_INFINITY;
            int jMin = 0;

            for (int j : jNotBasis) {
                double m = deltaY.multiply(a.getColumn(j)).get(0, 0);
                if (EpsilonHelper.isNegative(m)) {
                    double temp = (c.get(0, j) - y.multiply(a.getColumn(j)).get(0, 0)) / m;
                    if (g0 > temp) {
                        g0 = temp;
                        jMin = j;
                    }
                }
            }

            if (Double.isInfinite(g0)) {
                System.out.println("No solution");
                System.exit(0);
            }

            jBasis.set(i, jMin);
            y = y.subtract(deltaY.multiply(-g0));

            aBasisInverse = MatrixOperations.getInverseMatrix(aBasisInverse, a.getColumn(jBasis.get(i)), i);
        }
    }

    public void run() {
        dualSimplexMethod().print();
    }
}

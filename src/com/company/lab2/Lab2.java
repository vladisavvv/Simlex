package com.company.lab2;

import com.company.helpers.EpsilonHelper;
import com.company.helpers.InputReader;
import com.company.helpers.Matrix;
import com.company.helpers.MatrixOperations;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Lab2 {
    private final Matrix a;
    private final Matrix b;
    private final Matrix c;
    private final Matrix x;
    private final List<Integer> jBasis;

    private final int n;
    private final int m;

    public Lab2(final String fileName) throws FileNotFoundException {
        final InputReader inputReader = new InputReader(new FileInputStream(fileName));

        n = inputReader.nextInt();
        m = inputReader.nextInt();

        a = new Matrix(inputReader, n, m);
        b = new Matrix(inputReader, 1, n);
        c = new Matrix(inputReader, 1, m);
        x = new Matrix(inputReader, 1, m);

        jBasis = new ArrayList<>();
        for (int i = 0; i < n; ++i)
            jBasis.add(inputReader.nextInt() - 1);
    }

    public Matrix mainPhase() {
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

            if (positionNegativeValue == -1)
                return x;

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

    public void run() {
        mainPhase().print();
    }
}

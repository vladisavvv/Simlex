package com.company.lab6;

import com.company.helpers.EpsilonHelper;
import com.company.helpers.InputReader;
import com.company.helpers.Matrix;
import com.company.helpers.MatrixOperations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Lab6 {
    private final Matrix A;
    private final Matrix c;
    private final Matrix D;
    private Matrix x;
    private final List<Integer> jOporn = new ArrayList<>();
    private final List<Integer> jOpornExt = new ArrayList<>();

    private final int n;
    private final int m;
    private final int k;

    public Lab6(final String fileName) throws FileNotFoundException {
        final InputReader inputReader = new InputReader(new FileInputStream(fileName));
//        final InputReader inputReader = new InputReader(System.in);

        m = inputReader.nextInt();
        n = inputReader.nextInt();
        k = inputReader.nextInt();

        A = new Matrix(inputReader, m, n);
        final Matrix b = new Matrix(inputReader, 1, m); // unused
        c = new Matrix(inputReader, 1, n);
        D = new Matrix(inputReader, n, n);
        x = new Matrix(inputReader, 1, n);

        for (int i = 0; i < m; ++i)
            jOporn.add(inputReader.nextInt() - 1);

        for (int i = 0; i < k; ++i)
            jOpornExt.add(inputReader.nextInt() - 1);
    }

    public void run() {
        while (true) {
            Matrix cX = c.add(D.multiply(x));
            Matrix cBasisX = new Matrix(1, jOporn.size());
            for (int i = 0; i < jOporn.size(); ++i)
                cBasisX.set(0, i, cX.get(0, jOporn.get(i)));

            Matrix ABasis = new Matrix(A.getN(), jOporn.size());
            for (int i = 0; i < A.getN(); ++i) {
                for (int j = 0; j < jOporn.size(); ++j) {
                    ABasis.set(i, j, A.get(i, jOporn.get(j)));
                }
            }

            Matrix ABasisInverse = MatrixOperations.getInverseMatrix(ABasis);

            Matrix ux = cBasisX.multiply(ABasisInverse).multiply(-1);
            Matrix deltaX = ux.multiply(A).add(cX);

            int firstNegativeValue = -1;
            for (int i = 0; i < n; ++i) {
                if (EpsilonHelper.isNegative(deltaX.get(0, i))) {
                    firstNegativeValue = i;
                    break;
                }
            }

            if (firstNegativeValue == -1) {
                System.out.println("Bounded");
                x.print();
                return;
            }

            Matrix ABasisExt = new Matrix(A.getN(), jOpornExt.size());
            for (int i = 0; i < A.getN(); ++i) {
                for (int j = 0; j < jOpornExt.size(); ++j) {
                    ABasisExt.set(i, j, A.get(i, jOpornExt.get(j)));
                }
            }

            Matrix DExt = new Matrix(jOpornExt.size(), jOpornExt.size());
            for (int i = 0; i < jOpornExt.size(); ++i) {
                for (int j = 0; j < jOpornExt.size(); ++j) {
                    DExt.set(i, j, D.get(jOpornExt.get(i), jOpornExt.get(j)));
                }
            }

            final Matrix H = MatrixOperations.mergeMatrix2(
                MatrixOperations.mergeMatrix(DExt, ABasisExt.transpose()),
                MatrixOperations.mergeMatrix(ABasisExt, new Matrix(m, m))
            );

            Matrix bLeft = new Matrix(jOpornExt.size(), 1);
            for (int i = 0; i < jOpornExt.size(); ++i) {
                bLeft.set(i, 0, D.get(jOpornExt.get(i), firstNegativeValue));
            }
            Matrix bRight = A.getColumn(firstNegativeValue);

            Matrix b = MatrixOperations.mergeMatrix2(bLeft, bRight);

            Matrix newX = MatrixOperations.getInverseMatrix(H).multiply(b).multiply(-1);

            Matrix vectorL = new Matrix(1, n);
            vectorL.set(0, firstNegativeValue, 1.0);
            int k = 0;
            for (int i : jOpornExt) {
                vectorL.set(0, i, newX.get(k, 0));
                ++k;
            }

            double sigma = vectorL.multiply(D).multiply(vectorL).get(0, 0);
            Matrix theta = new Matrix(1, n);
            for (int i = 0; i < n; ++i)
                theta.set(0, i, Double.POSITIVE_INFINITY);

            if (!EpsilonHelper.isZero(sigma)) {
                theta.set(0, firstNegativeValue, Math.abs(deltaX.get(0, firstNegativeValue)) / sigma);
            }

            for (int i : jOpornExt) {
                if (EpsilonHelper.isNegative(vectorL.get(0, i)))
                    theta.set(0, i, (-x.get(0, i)) / vectorL.get(0, i));
            }

            double minTheta = Double.POSITIVE_INFINITY;
            int posMinTheta = -1;
            for (int i = 0; i < n; ++i) {
                if (minTheta > theta.get(0, i)) {
                    minTheta = theta.get(0, i);
                    posMinTheta = i;
                }
            }

            if (minTheta == Double.POSITIVE_INFINITY) {
                System.out.println("Unbounded");
                return;
            }

            x = x.add(vectorL.multiply(minTheta));

            if (posMinTheta == firstNegativeValue) {
                jOpornExt.add(posMinTheta);
            } else if (!jOporn.contains(posMinTheta)) {
                jOpornExt.remove(posMinTheta);
            } else {
                int s = jOporn.indexOf(posMinTheta);

                List<Integer> tempForCheck = new ArrayList<>(jOpornExt);
                tempForCheck.removeAll(jOporn);
                if (tempForCheck.isEmpty()) {
                    jOporn.set(jOporn.indexOf(posMinTheta), firstNegativeValue);
                    jOpornExt.set(jOpornExt.indexOf(posMinTheta), firstNegativeValue);
                } else {
                    boolean isExist = false;
                    int newJ = -1;
                    for (int i : tempForCheck) {
                        if (!EpsilonHelper.isZero(ABasisInverse.multiply(A.getColumn(i)).get(0, s))) {
                            newJ = i;
                            isExist = true;
                            break;
                        }
                    }

                    if (isExist) {
                        jOporn.set(jOporn.indexOf(posMinTheta), newJ);
                        jOpornExt.remove(posMinTheta);
                    } else {
                        jOporn.set(jOporn.indexOf(posMinTheta), firstNegativeValue);
                        jOpornExt.set(jOpornExt.indexOf(posMinTheta), firstNegativeValue);
                    }
                }
            }
        }
    }

}

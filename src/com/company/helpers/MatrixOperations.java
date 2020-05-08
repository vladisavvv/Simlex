package com.company.helpers;

public class MatrixOperations {
    public static Matrix getInverseMatrix(final Matrix a) {
        assert a.getN() == a.getM();

        int size = a.getN();
        final Matrix A = Matrix.createIdentityMatrix(size);

        for (int i = 0; i < size; ++i) {
            if (EpsilonHelper.isZero(a.get(i, i))) {
                for (int j = i + 1; j < size; ++j) {
                    if (!EpsilonHelper.isZero(a.get(j, i))) {
                        a.swapLine(i, j);
                        A.swapLine(i, j);

                        break;
                    }
                }
            }

            double temp = a.get(i, i);
            for (int j = i; j < size; ++j)
                a.set(i, j, a.get(i, j) / temp);

            for (int j = 0; j < size; ++j)
                A.set(i, j, A.get(i, j) / temp);

            for (int j = 0; j < size; ++j) {
                if (i == j)
                    continue;

                temp = a.get(j, i) / a.get(i, i);

                for (int k = i; k < size; ++k)
                    a.set(j, k, a.get(j, k) - a.get(i, k) * temp);

                for (int k = 0; k < size; ++k)
                    A.set(j, k, A.get(j, k) - A.get(i, k) * temp);
            }
        }

        return A;
    }

    public static Matrix getInverseMatrix(final Matrix aInverse,
                                          final Matrix x,
                                          final int i) {
        final Matrix l0 = aInverse.multiply(x);

        if (EpsilonHelper.isZero(l0.get(i, 0))) {
            System.out.println("noninvertible");
            System.exit(0);
        }

        final Matrix l1 = new Matrix(l0);
        l1.set(i, 0, -1);

        final Matrix l2 = l1.multiply(-1.0 / l0.get(i, 0));

        final Matrix e = Matrix.createIdentityMatrix(aInverse.getN());
        e.changeColumn(i, l2);

        return e.multiply(aInverse);
    }
}

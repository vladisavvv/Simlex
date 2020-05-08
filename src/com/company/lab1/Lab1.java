package com.company.lab1;

import com.company.helpers.InputReader;
import com.company.helpers.Matrix;

import java.io.*;

public class Lab1 {
    private static final double EPS = 1e-9;

    private final Matrix a;
    private final Matrix aInv;
    private final Matrix x;
    private final int i;
    private final int n;

    public Lab1(final String fileName) throws FileNotFoundException {
        final InputReader inputReader = new InputReader(new FileInputStream(fileName));

        n = inputReader.nextInt();
        i = inputReader.nextInt() - 1;

        a = new Matrix(inputReader, n, n);
        aInv = new Matrix(inputReader, n, n);

        x = new Matrix(inputReader, n, 1);
    }

    public void run() {
        a.changeColumn(i, x);
        final Matrix l0 = aInv.multiply(x);

        if (Math.abs(l0.get(i, 0)) < EPS) {
            System.out.println("NO");
            return;
        }

        final Matrix l1 = new Matrix(l0);
        l1.set(i, 0, -1);

        final Matrix l2 = l1.multiply(-1.0 / l0.get(i, 0));

        final Matrix e = Matrix.createIdentityMatrix(n);
        e.changeColumn(i, l2);

        System.out.println("YES");
        (e.multiply(aInv)).print();
    }
}

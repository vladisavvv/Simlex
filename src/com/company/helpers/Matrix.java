package com.company.helpers;

public class Matrix {
    private final double[][] a;

    public Matrix(int n, int m) {
        a = new double[n][m];
    }

    public Matrix(final InputReader inputReader,
                  final int n,
                  final int m) {
        this(n, m);

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j)
                a[i][j] = inputReader.nextDouble();
        }
    }

    public Matrix(final Matrix matrix) {
        this(matrix.getN(), matrix.getM());

        for (int i = 0; i < getN(); ++i) {
            for (int j = 0; j < getM(); ++j)
                a[i][j] = matrix.get(i, j);
        }
    }

    public void changeColumn(final int idColumn,
                             final Matrix source) {
        for (int i = 0; i < getN(); ++i) {
            a[i][idColumn] = source.get(i, 0);
        }
    }

    public void changeColumn(final int idColumn,
                             final int idSourceColumn,
                             final Matrix source) {
        for (int i = 0; i < getN(); ++i) {
            a[i][idColumn] = source.get(i, idSourceColumn);
        }
    }

    public double get(final int i,
                      final int j) {
        assert i < getN() && j < getM();
        return a[i][j];
    }

    public void set(final int i,
                    final int j,
                    final double newValue) {
        a[i][j] = newValue;
    }

    public int getN() {
        return a.length;
    }

    public int getM() {
        return a[0].length;
    }

    public void print() {
        for (int i = 0; i < getN(); ++i) {
            for (int j = 0; j < getM(); ++j) {
                System.out.printf("%.7f ", a[i][j]);
            }
            System.out.println();
        }
    }

    public Matrix multiply(final Matrix second) {
        final Matrix answer;

        if (second.getN() == getM()) {
            answer = new Matrix(getN(), second.getM());

            for (int i = 0; i < getN(); ++i) {
                for (int j = 0; j < second.getM(); ++j) {
                    for (int k = 0; k < getM(); ++k) {
                        answer.set(i, j, answer.get(i, j) + a[i][k] * second.get(k, j));
                    }
                }
            }
        } else {
            answer = new Matrix(getN(), second.getN());

            for (int i = 0; i < getN(); ++i) {
                for (int j = 0; j < second.getN(); ++j) {
                    for (int k = 0; k < getM(); ++k) {
                        answer.set(i, j, answer.get(i, j) + a[i][k] * second.get(j, k));
                    }
                }
            }
        }

        return answer;
    }

    public Matrix multiply(final double value) {
        final Matrix answer = new Matrix(getN(), getM());

        for (int i = 0; i < getN(); ++i) {
            for (int j = 0; j < getM(); ++j) {
                answer.set(i, j, a[i][j] * value);
            }
        }

        return answer;
    }

    public void swapLine(int i, int j) {
        if (i == j)
            return;

        double[] temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static Matrix createIdentityMatrix(int n) {
        final Matrix matrix = new Matrix(n, n);
        for (int i = 0; i < n; ++i) {
            matrix.set(i, i, 1);
        }

        return matrix;
    }

    public Matrix transpose() {
        final Matrix answer = new Matrix(getM(), getN());

        for (int i = 0; i < getN(); ++i) {
            for (int j = 0; j < getM(); ++j) {
                answer.set(j, i, get(i, j));
            }
        }

        return answer;
    }

    public Matrix getColumn(int idColumn) {
        final Matrix answer = new Matrix(getN(), 1);

        for (int i = 0; i < getN(); ++i)
            answer.set(i, 0, a[i][idColumn]);

        return answer;
    }

    public Matrix subtract(final Matrix b) {
        Matrix newB = new Matrix(b);

        if (!(getN() == b.getN() && getM() == b.getM()))
            newB = b.transpose();

        assert getN() == newB.getN() && getM() == newB.getM();

        for (int i = 0; i < getN(); ++i) {
            for (int j = 0; j < getM(); ++j)
                newB.set(i, j, a[i][j] - newB.get(i, j));
        }

        return newB;
    }

    public Matrix add(final Matrix b) {
        Matrix newB = new Matrix(b);

        if (!(getN() == b.getN() && getM() == b.getM()))
            newB = b.transpose();

        assert getN() == newB.getN() && getM() == newB.getM();

        for (int i = 0; i < getN(); ++i) {
            for (int j = 0; j < getM(); ++j)
                newB.set(i, j, a[i][j] + newB.get(i, j));
        }

        return newB;
    }

    public Matrix remove(int idLine) {
        assert idLine < getN();
        final Matrix matrix = new Matrix(getN() - 1, getM());
        for (int i = 0; i < getN() - 1; ++i) {
            for (int j = 0; j < getM(); ++j) {
                matrix.set(i, j, a[i + (i >= idLine ? 1 : 0)][j]);
            }
        }
        return matrix;
    }
}

package com.company.helpers;

public class IntegerPair implements Comparable {
    private int x;
    private int y;

    public IntegerPair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return x + ", " + y;
    }

    @Override
    public boolean equals(final Object b) {
        IntegerPair B = (IntegerPair) b;
        return (getX() == B.getX() && getY() == B.getY());
    }

    @Override
    public int compareTo(Object o) {
        int res = Integer.compare(x, ((IntegerPair) o).getX());

        if (res != 0)
            return res;

        return Long.compare(y, ((IntegerPair) o).getY());
    }
}

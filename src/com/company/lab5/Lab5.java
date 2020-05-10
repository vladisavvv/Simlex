package com.company.lab5;

import com.company.helpers.InputReader;
import com.company.helpers.IntegerPair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Lab5 {
    private final List<Long> a;
    private final List<Long> b;
    private final List<List<Long> > c;
    private final Random random = new Random();

    private final int N;
    private final int M;

    public Lab5(final String fileName) throws FileNotFoundException {
//        final InputReader inputReader = new InputReader(new FileInputStream(fileName));
        final InputReader inputReader = new InputReader(System.in);

        c = new ArrayList<>();
        a = new ArrayList<>();
        b = new ArrayList<>();

        N = inputReader.nextInt();
        M = inputReader.nextInt();

//        N = M = 2;

        for (int i = 0; i < N; ++i) {
            c.add(new ArrayList<>());
            for (int j = 0; j < M; ++j) {
                c.get(i).add((long) inputReader.nextInt());
//                c.get(i).add((long) random.nextInt(1000)- 500);
            }
        }

        for (int i = 0; i < N; ++i) {
            a.add((long) inputReader.nextInt());
//            a.add((long) random.nextInt(500));
        }

        for (int i = 0; i < M; ++i) {
            b.add((long) inputReader.nextInt());
//            b.add((long) random.nextInt(500));
        }

//        System.out.println("---------------");
//        System.out.printf("%d %d\n", N, M);
//        for (int i = 0; i < N; ++i) {
//            for (int j = 0; j < M; ++j)
//                System.out.printf("%d ", c.get(i).get(j));
//            System.out.println();
//        }
//
//        for (int i = 0; i < N; ++i)
//            System.out.printf("%d ", a.get(i));
//        System.out.println();
//
//        for (int i = 0; i < M; ++i)
//            System.out.printf("%d ", b.get(i));
//        System.out.println();
    }

    public void run() {
        long sumA = 0;
        for (int i = 0; i < N; ++i) {
            sumA += a.get(i);
        }

        long sumB = 0;
        for (int i = 0; i < M; ++i) {
            sumB += b.get(i);
        }
        
        if (sumA != sumB) {
            balance(sumA, sumB);
        }

        northwestCornerMethod();
        potentialsMethod();
    }

    boolean odd;
    int minOddCellX;
    int minOddCellY;
    long minOddCellValue;


    public void potentialsMethod() {
        int n = c.size();
        int m = c.get(0).size();

        List<Long> u = new ArrayList<>();
        for (int i = 0; i < n; ++i)
            u.add(Long.MIN_VALUE);
        List<Long> v = new ArrayList<>();
        for (int i = 0; i < m; ++i)
            v.add(Long.MIN_VALUE);

        v.set(m - 1, 0L);

        final TreeSet<IntegerPair> uN = new TreeSet<>();
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                if (!x.containsKey(new IntegerPair(i, j)))
                    uN.add(new IntegerPair(i, j));
            }
        }

        while (true) {
            List<IntegerPair> undefined = new ArrayList<>();

            for (int k = cellsCol.size() - 1; k >= 0; --k) {
                for (IntegerPair it : cellsCol.get(k)) {
                    int i = it.getX();
                    int j = it.getY();

                    if (v.get(j) != Long.MIN_VALUE)
                        u.set(i, c.get(i).get(j) - v.get(j));
                    else if (u.get(i) != Long.MIN_VALUE)
                        v.set(j, c.get(i).get(j) - u.get(i));
                    else
                        undefined.add(it);
                }
            }

            List<IntegerPair> temp = new ArrayList<>();
            while (!undefined.isEmpty()) {
                for (IntegerPair it : undefined) {
                    int i = it.getX();
                    int j = it.getY();

                    if (u.get(i) != Long.MIN_VALUE) {
                        v.set(j, c.get(i).get(j) - u.get(i));
                        temp.add(it);
                    } else if (v.get(j) != Long.MIN_VALUE) {
                        u.set(i, c.get(i).get(j) - v.get(j));
                        temp.add(it);
                    }
                }

                for (IntegerPair it : temp)
                    undefined.remove(it);
                temp.clear();
            }

            int i0 = 0, j0 = 0;
            long maxW = 0;

            for (IntegerPair it : uN) {
                int i = it.getX();
                int j = it.getY();

                if (u.get(i) == Long.MIN_VALUE || v.get(j) == Long.MIN_VALUE)
                    continue;

                long w = u.get(i) + v.get(j) - c.get(i).get(j);
                if (w > maxW) {
                    maxW = w;
                    i0 = i;
                    j0 = j;
                }
            }

            if (maxW == 0) {
                for (int i = 0; i < N; ++i) {
                    for (int j = 0; j < M; ++j)
                        System.out.printf("%d ", x.get(new IntegerPair(i, j)) != null ? x.get(new IntegerPair(i, j)) : 0L);
                    System.out.println();
                }
                return;
            }

            x.put(new IntegerPair(i0, j0), 0L);
            undefined.remove(new IntegerPair(i0, j0));

            cellsRow.get(i0).add(new IntegerPair(i0, j0));
            cellsCol.get(j0).add(new IntegerPair(i0, j0));

            odd = true;
            minOddCellX = minOddCellY = -1;
            minOddCellValue = Long.MAX_VALUE;

            List<IntegerPair> loopCells = new ArrayList<>();
            loopCells.add(new IntegerPair(i0, j0));

            List<IntegerPair> evenLoopCells = new ArrayList<>();
            evenLoopCells.add(new IntegerPair(i0, j0));

            List<IntegerPair> oddLoopCells = new ArrayList<>();

            getLoop(loopCells, evenLoopCells, oddLoopCells, new TreeSet<>());

            for (IntegerPair cell : oddLoopCells) {
                x.put(cell, x.get(cell) - minOddCellValue);
            }
            for (IntegerPair cell : evenLoopCells) {
                x.put(cell, x.get(cell) + minOddCellValue);
            }


            x.remove(new IntegerPair(minOddCellX, minOddCellY));

            undefined.add(new IntegerPair(minOddCellX, minOddCellY));

            cellsRow.get(minOddCellX).remove(new IntegerPair(minOddCellX, minOddCellY));
            cellsCol.get(minOddCellY).remove(new IntegerPair(minOddCellX, minOddCellY));

            for (int i = 0; i < n; ++i)
                u.set(i, Long.MIN_VALUE);
            for (int i = 0; i < m - 1; ++i)
                v.set(i, Long.MIN_VALUE);
        }
    }

    public TreeSet<IntegerPair> getNextCells(List<IntegerPair> loopCells) {
        if (loopCells.size() > 1 && loopCells.get(loopCells.size() - 1).getX() == loopCells.get(loopCells.size() - 2).getX())
            return cellsCol.get(loopCells.get(loopCells.size() - 1).getY());
        else
            return cellsRow.get(loopCells.get(loopCells.size() - 1).getX());
    }

    private boolean getLoop(List<IntegerPair> loopCells,
                            List<IntegerPair> evenLoopCells,
                            List<IntegerPair> oddLoopCells,
                            TreeSet<IntegerPair> visited) {
        visited.add(loopCells.get(loopCells.size() - 1));
        for (IntegerPair cell : getNextCells(loopCells)) {
            if (!cell.equals(loopCells.get(loopCells.size() - 1))) {
                if (!visited.contains(cell)) {
                    List<IntegerPair> newLoopCells = new ArrayList<>(loopCells);
                    newLoopCells.add(cell);

                    if (getLoop(newLoopCells, evenLoopCells, oddLoopCells, visited)) {
                        if (odd) {
                            oddLoopCells.add(cell);
                            if (x.get(cell) < minOddCellValue) {
                                minOddCellValue = x.get(cell);
                                minOddCellX = cell.getX();
                                minOddCellY = cell.getY();
                            }
                        } else {
                            evenLoopCells.add(cell);
                        }
                        odd = !odd;
                        return true;
                    }
                } else if (cell.equals(loopCells.get(0))) {
                    return true;
                }
            }
        }

        return false;
    }

    TreeMap<IntegerPair, Long> x;
    List<TreeSet<IntegerPair>> cellsCol;
    List<TreeSet<IntegerPair>> cellsRow;

    private void northwestCornerMethod() {
        int n = a.size();
        int m = b.size();

        cellsRow = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            cellsRow.add(new TreeSet<>());
        }
        cellsCol = new ArrayList<>();
        for (int i = 0; i < m; ++i) {
            cellsCol.add(new TreeSet<>());
        }

        int i = 0;
        int j = 0;

        x = new TreeMap<>();

        while (i < n) {
            while (j < m) {
                if (a.get(i) < b.get(j)) {
                    x.put(new IntegerPair(i, j), a.get(i));
                    cellsRow.get(i).add(new IntegerPair(i, j));
                    cellsCol.get(j).add(new IntegerPair(i, j));

                    b.set(j, b.get(j) - a.get(i));
                    ++i;
                } else {
                    if (a.get(i).equals(b.get(j)) && j < m - 1) {
                        x.put(new IntegerPair(i, j + 1), 0L);
                        cellsRow.get(i).add(new IntegerPair(i, j + 1));
                        cellsCol.get(j + 1).add(new IntegerPair(i, j + 1));
                    }

                    x.put(new IntegerPair(i, j), b.get(j));
                    cellsRow.get(i).add(new IntegerPair(i, j));
                    cellsCol.get(j).add(new IntegerPair(i, j));

                    a.set(i, a.get(i) - b.get(j));
                    ++j;
                }
            }

            ++i;
        }
    }

    private void balance(final long aSum,
                         final long bSum) {
        if (aSum > bSum) {
            b.add(aSum - bSum);
            for (int i = 0; i < c.size(); ++i)
                c.get(i).add(0L);
        } else {
            a.add(bSum - aSum);
            c.add(new ArrayList<>());
            for (int i = 0; i < c.get(0).size(); ++i)
                c.get(c.size() - 1).add(0L);
        }
    }
}

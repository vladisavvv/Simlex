package com.company;

import com.company.helpers.IntegerPair;
import com.company.lab1.Lab1;
import com.company.lab2.Lab2;
import com.company.lab3.Lab3;
import com.company.lab4.Lab4;
import com.company.lab5.Lab5;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        for (int i = 0; i < 1; ++i) {
            try {
                (new Lab5("./src/com/company/lab5/input.txt")).run();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

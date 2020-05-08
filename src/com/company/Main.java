package com.company;

import com.company.lab1.Lab1;
import com.company.lab2.Lab2;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
//        try {
//            (new Lab1("./src/com/company/lab1/input.txt")).run();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        try {
            (new Lab2("./src/com/company/lab2/input.txt")).run();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

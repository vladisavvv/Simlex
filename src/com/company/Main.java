package com.company;

import com.company.lab1.Lab1;
import com.company.lab2.Lab2;
import com.company.lab3.Lab3;
import com.company.lab4.Lab4;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
//        try {
//            (new Lab1("./src/com/company/lab1/input.txt")).run();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        try {
            (new Lab4("./src/com/company/lab4/input.txt")).run();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

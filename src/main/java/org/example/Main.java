package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.text.ParseException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws ParseException, JsonProcessingException, InterruptedException {
//            System.out.println(SingletonClient.getInstance().getExpiresAt());
        SingletonClient.testSingletonClient(100);
        }
    }
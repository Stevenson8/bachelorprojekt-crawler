package org.main;

import Configuration.Configuration;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");
        Controller controller=new Controller();
        controller.doAnalysis(Configuration.NUMBER_OF_WEBSITES);
    }
}
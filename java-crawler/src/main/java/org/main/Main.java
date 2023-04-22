package org.main;

import configuration.configuration;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");
        Controller controller=new Controller();
        controller.doAnalysis(configuration.NUMBER_OF_WEBSITES);
    }
}
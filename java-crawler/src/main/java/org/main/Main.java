package org.main;

import configuration.Configuration;
import database.DbWriter;


public class Main {

    public static void main(String[] args) {


        Controller controller=new Controller();
        controller.doAnalysis();
    }
}
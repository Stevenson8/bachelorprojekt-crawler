package org.main;

import configuration.Configuration;
import database.DbWriter;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        BasicConfigurator.configure();

        logger.info("Started the Program in Main Method");

        Controller controller=new Controller();
        controller.doAnalysis();
    }
}
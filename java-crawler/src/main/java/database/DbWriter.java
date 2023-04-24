package database;

import model.AnalysisResult;

public class DbWriter {

    private DatabaseHelper dbHelper;
    public DbWriter() {
        this.dbHelper = new DatabaseHelper();
    }

    public void writeResultToDatabase(AnalysisResult result){
        //Todo
        dbHelper.insertIntoWebsite("Test.com");

        dbHelper.close();
    }
}

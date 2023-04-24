package database;

import model.AnalysisResult;

public class DbWriter {

    private DatabaseHelper dbHelper;
    public DbWriter() {
        this.dbHelper = new DatabaseHelper();
    }

    public void writeResultToDatabase(AnalysisResult result){
        //Todo
        dbHelper.insertIntoWebsite(1,"xyz.com","");
        dbHelper.insertIntoRequest(1,"status haha","EU","2022-01-01",false,"","HTTP");
        dbHelper.insertIntoCookie(1,"ga_12345","hvoeurbgoesbg","2019-01-09");
        dbHelper.insertIntoWebsiteHasRequest(1,1);
        dbHelper.insertIntoRequestHasCookie(1,1);


        dbHelper.close();
    }
}

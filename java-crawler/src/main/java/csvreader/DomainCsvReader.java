package csvreader;

import configuration.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import java.util.concurrent.ExecutionException;

public class DomainCsvReader {

    private List<String> values;
    private int index;

    public DomainCsvReader() {

        this.values = new ArrayList<>();
        index=0;
    }

    public boolean hasNextUrl(){
        return index<values.size();
    }

    public String readNextUrl(){
        if (index>=values.size()){
        return "xyz";
        }

        return "www."+values.get(index++);
    }

    public int getCurrentRank(){
        return index+Configuration.WEBSITE_RANK_START;
    }

    public void initializeReader(){
        values.clear();

        Scanner sc = null;
        try {
            sc = new Scanner(new File(Configuration.DOMAIN_CSV_PATH));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        sc.useDelimiter("\n");   //sets the delimiter pattern


        for(int i=0;i<Configuration.WEBSITE_RANK_END;i++){
            if (sc.hasNext()) {
                if(i>=Configuration.WEBSITE_RANK_START-1)
                    values.add(sc.next().split(",")[1]);
                else
                    sc.next();
            }
        }

        sc.close();  //closes the scanner
    }
}

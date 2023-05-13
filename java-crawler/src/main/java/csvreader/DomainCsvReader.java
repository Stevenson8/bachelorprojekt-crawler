package csvreader;

import configuration.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;

public class DomainCsvReader {

    private List<String> values;
    private int index;

    public DomainCsvReader() {

        this.values = new ArrayList<>();
        index=0;
    }

    public boolean hasNext(){
        return index<values.size();
    }

    public void setToBeginning(){
        index=0;
    }

    public int getCurrentIndex(){
        return index;
    }

    public String readNext(){
        if (index>=values.size()){}
            //Todo log here
        return values.get(index++);
    }

    public void initializeReader(){
        values.clear();
        index=Configuration.WEBSITE_RANK_START-1;

        Scanner sc = null;
        try {
            sc = new Scanner(new File(Configuration.DOMAIN_CSV_PATH));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        sc.useDelimiter("\n");   //sets the delimiter pattern

        for(int i=index;i<Configuration.WEBSITE_RANK_END;i++){
            if (sc.hasNext())
                values.add(sc.next().split(",")[1]);
        }

        sc.close();  //closes the scanner
    }
}

package edu.bdf.mycontactlist;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class FileIOHelper {

    private static final String FILENAME = "data.txt";
    private static final String TAG = "FileIOHelper";


    public void writeFile(Activity activity, ArrayList<String> items)
    {
        try
        {
            Log.d(TAG, "writeFile: " + "Start");
            OutputStreamWriter writer = new OutputStreamWriter(activity.openFileOutput(FILENAME, 0));
            // Format : 1|ITEM
            int counter = 1;
            String row = "";

            for(String i : items)
            {
                Log.d(TAG, "writeFile: " + i);
                row = counter + "|" + i;

                if(counter < items.size()-1){
                    row += "\r\n";
                }
                writer.write(row);
                Log.d(TAG, "writeFile: " + row);
                counter++;
            }
            writer.close();
        }
        catch(Exception e)
        {
            Log.d(TAG, "writeFile: " + e.getMessage());
        }
    }

    public String readFile(Activity activity)
    {
        String results = "";
        try {
            StringBuffer stringBuffer;
            try (InputStream is = activity.openFileInput(FILENAME)) {
                InputStreamReader reader = new InputStreamReader(is);
                BufferedReader bufferedReader = new BufferedReader(reader);
                stringBuffer = new StringBuffer();

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line + "\r\n");
                }
                is.close();
            }
            results = stringBuffer.toString();


        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return results;
    }
}

package Lexer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Source {
    public String sources;
    public ArrayList<Integer> lines=new ArrayList<>();

    public Source(String filename) throws IOException {
        FileInputStream in = new FileInputStream(filename);
        InputStreamReader read = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(read);
        String lineText;
        sources = "";
        lines.add(0);
        while((lineText = bufferedReader.readLine())!=null) {
            sources += lineText;
            sources += "\n";
            lines.add(sources.length());
        }
        //System.out.println(sources);
        //System.out.println(lines.toString());
    }

    public int getLine(int num) {
        int ans = 0;
        for (int i=0;i<lines.size();i++) {
            if(lines.get(i)>num){
                ans = i;
                break;
            }
        }
        return ans;
    }

}
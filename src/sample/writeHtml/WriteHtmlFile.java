package strings;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class WriteHtmlFile {

    private String templateFile = "src/strings/IELTS_02.html";
    private BufferedReader br;

    public WriteHtmlFile() {
    }

    public void readTemplate(){
        try {
            String line;
            br = new BufferedReader(new FileReader(templateFile));
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WriteHtmlFile wf = new WriteHtmlFile();
        wf.readTemplate();
    }


}

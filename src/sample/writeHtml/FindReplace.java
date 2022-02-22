package sample.writeHtml;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindReplace {

    private String templateFile = "src/sample/writeHtml/IELTS_02.html";
    private String outFile = "src/sample/writeHtml/outfile.html";
    private BufferedReader br;
    private FileWriter filewriter = null;
    private boolean isPrintTemplate = true;

    private String mTitle = "<title>Cambridge Practice Tests for IELTS 1. Pt. 2</title>";
    private String replaceTitle = "1 Harry Potter and the Sorcerers Stone. Pts 1-41 Harry Potter and the Sorcerers Stone. Pts 1-4";
    private String P01S = "<title>";
    private String P01F = "</title>";
    private String P02S = "<h3>";
    private String P02F = "</h3>";
    private String P03S = "<p>";
    private String P03F = "</p>";

    private String reppattern = "=======OOO======";

    private Pattern pattern;
    private int patternID = 0;
    private List<String> patternsList;

    public FindReplace() {
        patternsList = new ArrayList<>();
        patternsList.add("title");
//        patternsList.add(P01F);
        patternsList.add("h3");
//        patternsList.add(P02F);
        patternsList.add("p");
//        patternsList.add(P03F);
        patternsList.add("tbody");

        pattern = Pattern.compile(patternsList.get(patternID));

    }

    public void writeOutHTML(String line) {
        try {
            if (filewriter == null) {
                filewriter = new FileWriter(new File(outFile));
            }
            filewriter.write(line+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void findPatterns() {
//        Pattern pattern = Pattern.compile(P01S);
        Matcher matcher = pattern.matcher(mTitle.toLowerCase());
        if (matcher.find()) {
            System.out.println("Gotcha!!! ..........");
        }
        String patt = patternsList.get(patternID);
        int first = mTitle.indexOf(patt);
        first = mTitle.indexOf(">", first);
        first++;
        int last = mTitle.indexOf(patt, first);
        last -= 2;
//        System.out.println("01: "+first+", 02: "+last);
        System.out.println(mTitle.substring(first, last));
        StringBuffer buffer = new StringBuffer(mTitle);
        buffer.replace(first, last, replaceTitle);
        System.out.println(buffer.toString());
    }

    public String replacePattern(String inpLine) {

        Matcher matcher = pattern.matcher(inpLine.toLowerCase());
        if (matcher.find()) {
            System.out.println("Gotcha!!! ..........");
            if (patternID == patternsList.size() - 1) {
                isPrintTemplate = false;
                pattern = Pattern.compile("<!-------");
                return inpLine;
            }

            String patt = patternsList.get(patternID);
            int first = inpLine.indexOf(patt);
            first = inpLine.indexOf(">", first);
            first++;
            int last = inpLine.indexOf(patt, first);
            last -= 2;
            StringBuffer buffer = new StringBuffer(inpLine);
            buffer.replace(first, last, reppattern);
            inpLine = buffer.toString();
            patternID += 1;
            if (patternID < patternsList.size())
                pattern = Pattern.compile(patternsList.get(patternID));
            return inpLine;
        }
        return inpLine;
//        writeOutHTML(inpLine);
    }

    public void setReppattern(String reppattern) {
        this.reppattern = reppattern;
    }

    public static void main(String[] args) {
        FindReplace fr = new FindReplace();
//        fr.findPatterns();
        fr.readTemplate();
    }

    public void readTemplate() {
        try {
            String line;
            br = new BufferedReader(new FileReader(templateFile));
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if (isPrintTemplate) {
                    line = replacePattern(line);
                    writeOutHTML(line);
                } else {
                    writeTable();
//                    line = br.readLine();
                    isPrintTemplate = true;
                }
            }
            filewriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeTable() {
        System.out.println("========= My table =========================");

    }

}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CipherCracker {

    static StringBuilder ciphertext = new StringBuilder(250000);        //To Store the given cipher text
    static StringBuilder msgpart = new StringBuilder(250000);           //To Store the message Fragment
    static ArrayList<String> possibleop = new ArrayList<>();              //To Store equal chunks of cipher text with size equal to that ofthe given message fragment


    static ArrayList<Integer> mfc_a = new ArrayList<>();                //To store the frequency values of the letters in order present in the message fragment
    static ArrayList<String> mfc_astring = new ArrayList<>();           //To store the letters in order present in the message fragment

    static HashMap<String, Integer> mfmap = new HashMap<>();            //For message fragment where key is the letter and value is the frequency
    static HashMap<String, Integer> mfc = new HashMap<>();              //For message fragment where key is the letter and value is the order


    static HashMap<String, Integer> tmap = new HashMap<>();             //To be used by the frequencycomparator method
    static HashMap<String, Integer> tc = new HashMap<>();
    static ArrayList<String> tc_string = new ArrayList<>();

    public static void main(String[] args) {

        int c = 0;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            String Input;
            while ((Input = in.readLine()) != null && !Input.isEmpty()) {
                if (c == 0) {
                    ciphertext.append(Input);
                    c++;
                } else {
                    msgpart.append(Input);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    cipherfrequencycracker();    //Calling our method to do the job
    }


    static void cipherfrequencycracker() { //Updated Cipher cracker using frequency analysis method

        final String regex = ".";
        int d = 0;

        final Pattern pattern = Pattern.compile(regex);
        final Matcher msgmatcher = pattern.matcher(String.valueOf(msgpart));

        frequencymapper(d, mfmap, mfc, msgmatcher, mfc_astring); // For msg fragment

        for(int f = 0; f<mfc.size();f++) {                      //Storing the letter frequencies and order
            mfc_a.add(mfmap.get(mfc_astring.get(f)));
        }

        splitter();                                     //Splitting the cipher text into possible word sequences of size equal to the message fragment

        frequencycomparator();                                  //To compare the letter frequencies of the each possible word sequence with our message fragment frequency

    }


    private static void frequencycomparator() {  //Takes each possible part in the cipher and matches its frequency to the frequency of the message fragment

    StringBuilder result = new StringBuilder(250000);

    String regex1 = ".";
    Pattern pattern1 = Pattern.compile(regex1);
    int i = 0, c;
    int tcount;
    int fullcount = 0;
    while (i < possibleop.size()) {
        c = 0;
        tcount = 0;
        Matcher sb_matcher = pattern1.matcher(String.valueOf(possibleop.get(i)));

        frequencymapper(c,tmap,tc,sb_matcher,tc_string);

        if(tmap.size()== mfc_astring.size()) {

            for(int j = 0; j < mfc_astring.size();j++) {
                if(tmap.get(String.valueOf(tc_string.get(j))) == mfc_a.get(j)) {
                    tcount++;
                }
            }

            if(tcount == mfc_astring.size()) {
                fullcount++;
                result.append(possibleop.get(i));
            }
        }

        tmap.clear();
        tc.clear();
        tc_string.clear();
        i++;
    }

    if(fullcount == 0) {
        System.out.println("0");
    }
    else if(fullcount == 1) {
        System.out.println(result.substring(0, msgpart.length()));
    }
    else {
        System.out.println(fullcount);
    }
}

    private static void splitter() {  //Generates the possible cipher fragments each having the length of the msgpart.

        int limit = (ciphertext.length() - msgpart.length())+1;

        for(int i = 0; i<limit;i++) {
            StringBuilder sb = new StringBuilder(250000);
            for (int j = i; j<i+msgpart.length();j++) {
                sb.append(String.valueOf(ciphertext.charAt(j)));
            }
            possibleop.add(String.valueOf(sb));
        }
    }


    private static void frequencymapper(int c, HashMap<String, Integer> fmap, HashMap<String, Integer> fc, Matcher matcher, ArrayList<String> di) {
        while(matcher.find()) {
            if(fmap.containsKey(matcher.group(0))) {
                fmap.put(matcher.group(0), fmap.get(matcher.group(0)) + 1);
            }
            else {
                fmap.put(matcher.group(0), 1);
                fc.put(matcher.group(0),c);
                di.add(String.valueOf(matcher.group(0)));
                c++;
            }
        }
    }
}
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

/***
 * This is main  class, this class contains main method
 * File absolute path will given to the program as command line argument
 */
public class ProcessFile {
    public static void main( String[] args)  {
        String error="";
        // Condition to check only one argument has been passed to program
        if(args.length != 1) {
            error="Invalid command line, exactly one argument required";
            System.err.println(error);
//            System.exit(1);
            return;
        }
        // Read file and store the string data for further processing
        String data = null;
        try {
            Path path = Paths.get(args[0]);
            if(path.toString().endsWith(".txt")){
                data = new String(Files.readAllBytes(path));
            } else{
                error="Unsupported File!";
                System.out.println(error);
                return;
            }


        //Pre-processing of the string
        data = data.toLowerCase().trim().replaceAll("[,.:!?*#$%-_=+/]","");
        data = data.replaceAll("\n"," ");
        // Split the words delimited by spaces
        String[] strArray = data.split(" ");

        // TreeMap object to store the key(Word),value(frequency) in sorted order(desc)
        TreeMap<String,Integer> wordFreqMap = new TreeMap<>(Collections.reverseOrder());
        // Word frequency counting logic
        // if word found as key in the tree, increment the frequency
        // if not found add to the Map with key and frequency value as 1
        for(String s: strArray){
            if(wordFreqMap.isEmpty()){
                wordFreqMap.put(s.trim(),1);
            } else{
                if(wordFreqMap.containsKey(s)){
                    wordFreqMap.put(s.trim(),wordFreqMap.get(s)+1);
                } else{
                    wordFreqMap.put(s.trim(),1);
                }
            }
        }

        // Sort the Map by value in dsc order
        Map<Object, Object> sortedMap =sortByValue(wordFreqMap);

        // Counter variable to control the number of most frequent words to be printed
        int numberOfEntriesAllowed=0;
        for(Object key: sortedMap.keySet()){
            Integer value = (Integer) sortedMap.get(key);
            System.out.println(value.toString()+" "+key);
            numberOfEntriesAllowed++;

            if(numberOfEntriesAllowed == 20){
                break;
            }
        }

        } catch (IOException e) {
            System.out.println("File Not Found!");
        }
    }

    // function to sort hashmap by values
    public static Map<Object, Object> sortByValue(TreeMap<String, Integer> hm) {
        // let's sort this map by values first
        return hm
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));

    }
}

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

// Class to hold the entities of a day
// Contains counts as siteId,Count pairs
class DayEntity{
    Map<Long,Integer> referenceMap;
    DayEntity(){
        referenceMap = new HashMap<>();
    }

    public void hit(Long id){
        Integer temp = referenceMap.getOrDefault(id,0);
        temp++;
        referenceMap.put(id,temp);
    }
}

// Main class which parses a given file
class LogParser{

    // final map that will have all values
    Map<String,DayEntity> outMap;

    // site id mapping and vice versa
    Map<String,Long> idMapper;
    Map<Long,String> siteMapper;

    // unique counter for siteid assignment
    Long runner;

    LogParser(){
        outMap = new TreeMap<>();
        idMapper = new HashMap<>();
        siteMapper = new HashMap<>();
        runner = 1L;
    }

    // function that parses a given file
    public void fileParser(String filePath) throws IOException {
        FileInputStream fStream = new FileInputStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fStream));
        String line;
        while((line = reader.readLine())!=null){
            lineParser(line);
        }
        reader.close();
    }

    private void lineParser(String line){
        String data[] = line.split("\\|");
        Long ts = Long.parseLong(data[0]);
        String siteName = data[1].trim();
        String key = getKeyString(ts);
        Long id = getId(siteName);
        DayEntity temp;
        if(!outMap.containsKey(key)){
            outMap.put(key,new DayEntity());
        }
        temp = outMap.get(key);
        temp.hit(id);
    }

    private String getKeyString(Long ts){
        Date date = new Date(ts*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd z");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return (sdf.format(date));
    }

    private Long getId(String siteName){
        if(idMapper.containsKey(siteName)){
            return idMapper.get(siteName);
        }
        idMapper.put(siteName,runner);
        siteMapper.put(runner,siteName);
        return runner++;
    }

    public void printData(){
        for (Map.Entry<String, DayEntity> entry : outMap.entrySet()) {
            System.out.println(entry.getKey());
            printSet(entry.getValue());
        }
    }

    private void printSet(DayEntity d){
        /*ValueSorter vs = new ValueSorter(d.referenceMap);
        TreeMap<Long,Integer> sortedMap = new TreeMap<>(vs);
        sortedMap.putAll(d.referenceMap);
        for (Map.Entry<Long, Integer> entry : sortedMap.entrySet()) {
            System.out.println( siteMapper.get(entry.getKey() )+ " " + entry.getValue() );
        }
        */


        /*
        Replacing the above code with count sort to sort in O(N) time
        * */
        // Assuming that the size of k is far small than N, so limiting it to 1000 for programming
        Map<Integer,List<Long>> sortMap = countSorter(d.referenceMap);
        for(int i = 1000 ; i > 0 ; i-- ){
            if(sortMap.containsKey(i)){
                for(Long j : sortMap.get(i)){
                    System.out.println(siteMapper.get(j) + " " + i);
                }
            }
        }

    }

    private Map<Integer,List<Long>> countSorter(Map<Long,Integer> inputMap){
        Map<Integer,List<Long>> outputMap = new HashMap<>();
        List<Long> temp;
        for (Map.Entry<Long, Integer> entry : inputMap.entrySet()) {
            temp = outputMap.getOrDefault(entry.getValue(),new ArrayList<>());
            temp.add(entry.getKey());
            outputMap.put(entry.getValue(),temp);
        }
        return outputMap;
    }

    public void resetParser(){
        outMap.clear();
    }
}

public class FileParser {
    public static void main(String[] args) {
        LogParser logParser = new LogParser();
        try {
            logParser.fileParser(args[0]);
        } catch (IOException e) {
            System.out.println("Exception while reading file! Please check file format");
            e.printStackTrace();
        }
        logParser.printData();
    }
}

// comparator to sort a given map as per values
class ValueSorter implements Comparator<Long>{
    Map<Long,Integer> map;
    ValueSorter(Map<Long,Integer> map){
        this.map = map;
    }

    @Override
    public int compare(Long o1, Long o2) {
        Integer e1 = map.get(o1);
        Integer e2 = map.get(o2);
        if(e1 >= e2 ){
            return -1;
        }
        return 1;
    }
}
package Solution;

import java.io.*;
import java.net.URL;
import java.util.*;

public class UrlTextHandler implements Matcher.OnJobDone {
    private int linesPerBlock;
    private String urlStr;
    private Set<String> stringsToMatch;
    private List<Map<String, List<WordLocation>>> matchersResList;

    public UrlTextHandler(int linesPerBlock, String urlStr, Set<String> stringsToMatch) {
        this.linesPerBlock = linesPerBlock;
        this.urlStr = urlStr;
        this.stringsToMatch = stringsToMatch;
        matchersResList = new ArrayList<>();
    }

    public void printResults() {
        List<BlockData> blocksList = parseUrlToBlocks();
        List<Thread> threadList = new ArrayList<>();
        for (BlockData blockDataElement : blocksList) {
            Matcher matcher = new Matcher(blockDataElement, stringsToMatch, this);
            Thread thread = new Thread(matcher);
            threadList.add(thread);
            thread.start();
        }

        threadList.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        aggregate(matchersResList);
    }

    private void aggregate(List<Map<String, List<WordLocation>>> matchersResList) {
        Map<String, List<WordLocation>> resMap = new HashMap<>();

        matchersResList.forEach(map -> map.keySet().forEach(key -> {
            if (resMap.containsKey(key)) {
                resMap.get(key).addAll(map.get(key));
            } else {
                resMap.put(key, map.get(key));
            }
        }));

        System.out.println(resMap);
    }


    private List<BlockData> parseUrlToBlocks() {
        List<BlockData> blocksList = new ArrayList<>();
        try {
            URL url = new URL(urlStr);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openStream()));
            int blockStartCharOffset = 1;
            int blockStartLine = 1;
            String line;
            StringBuilder currBlock = new StringBuilder();
            LineNumberReader linesReader = new LineNumberReader(reader);
            while ((line = linesReader.readLine()) != null) {
                currBlock.append(line).append(System.lineSeparator());
                if (linesReader.getLineNumber() % linesPerBlock == 0) {
                    BlockData blockToInsert = new BlockData(blockStartLine, blockStartCharOffset, currBlock.toString());
                    blocksList.add(blockToInsert);
                    blockStartLine = linesReader.getLineNumber() + 1;
                    blockStartCharOffset += (currBlock.length() + 1);
                    currBlock.delete(0, currBlock.length());
                }
            }
            //add the last block
            if (currBlock.length() > 0) {
                BlockData blockToInsert = new BlockData(blockStartLine, blockStartCharOffset, currBlock.toString());
                blocksList.add(blockToInsert);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return blocksList;
    }

    public static void main(String[] args) {

        String input = "James,John,Robert,Michael,William,David,Richard,Charles,Joseph,Thomas,Christopher,Daniel,Paul,Mark,Donald" +
                ",George,Kenneth,Steven,Edward,Brian,Ronald,Anthony,Kevin,Jason,Matthew,Gary,Timothy,Jose,Larry,Jeffrey," +
                "Frank,Scott,Eric,Stephen,Andrew,Raymond,Gregory,Joshua,Jerry,Dennis,Walter,Patrick,Peter,Harold,Douglas," +
                "Henry,Carl,Arthur,Ryan,Roger";
        Set<String> names = new HashSet<>(Arrays.asList(input.split(",")));
        UrlTextHandler parser = new UrlTextHandler(1000, "http://norvig.com/big.txt", names);
        parser.printResults();

    }

    @Override
    public void onDone(Map<String, List<WordLocation>> res) {
        matchersResList.add(res);
    }
}



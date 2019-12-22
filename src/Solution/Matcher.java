package Solution;

import java.util.*;

public class Matcher implements Runnable {

    public interface OnJobDone {
        void onDone(Map<String, List<WordLocation>> res);
    }

    private int blockLineOffset;
    private int blockCharOffset;
    private Set<String> stringsToMatchSet;
    private String strBlock;
    private OnJobDone listener;

    Matcher(BlockData blockData, Set<String> stringsToMatchSet, OnJobDone listener) {
        strBlock = blockData.getData();
        blockLineOffset = blockData.getLineOffset();
        blockCharOffset = blockData.getCharOffset();
        this.stringsToMatchSet = stringsToMatchSet;
        this.listener = listener;
    }

    @Override
    public void run() {
        Map<String, List<WordLocation>> retMap = new HashMap<>();
        List<String> listOfLines = Arrays.asList(strBlock.split(System.lineSeparator()));
        int charOffset = 0;
        int index;

        for (int line = 0; line < listOfLines.size(); line++) {
            for (String strTomatch : stringsToMatchSet) {
                index = listOfLines.get(line).indexOf(strTomatch);
                while (index >= 0) {  // indexOf returns -1 if no match found
                    WordLocation locationToAdd = new WordLocation(this.blockLineOffset + line, this.blockCharOffset + charOffset + index);
                    if (!retMap.containsKey(strTomatch)) {
                        List<WordLocation> ListToAdd = new ArrayList<>();
                        ListToAdd.add(locationToAdd);
                        retMap.put(strTomatch, ListToAdd);
                    } else {
                        retMap.get(strTomatch).add(locationToAdd);
                    }
                    index = listOfLines.get(line).indexOf(strTomatch, index + strTomatch.length());
                }
            }
            charOffset += listOfLines.get(line).length();
        }
        if (listener != null) {
            listener.onDone(retMap);
        }
    }
}

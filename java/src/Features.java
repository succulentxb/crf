import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * class Features, store feature functions and values
 *
 * attributes: rawTemplates
 * usage: store all the raw templates from file
 * content format: ["%x[1,0]", "%x[0,0]"]
 *
 * attributes: featureFuncs
 * usage: use map to store feature function values, use a int array to store different tag weights
 * content format: {"%x[0,0]": {"字": int[4]}}
 *
 * attributes: tag2indexMap
 * usage: use map to construct a relation between tag and index
 * content format: {'S': 0, 'B': 1, 'I': 2, 'E': 3}
 */
public class Features {
    private String[] rawTemplates;
    private Map<String, Integer> tag2indexMap;
    private Map<Integer, String> index2tagMap;
    private Map<String, Map<String, Integer[]>> featuresFuncs;

    public Features(String[] rawTemplates, String[] tags) {
        this.rawTemplates = rawTemplates.clone();
        this.featuresFuncs = new HashMap<>();
        this.tag2indexMap = new HashMap<>();
        this.index2tagMap = new HashMap<>();
        for (String rawTemp: this.rawTemplates) {
            Map<String, Integer[]> tmpMap = new HashMap<>();
            this.featuresFuncs.put(rawTemp, tmpMap);
        }
        for (int i = 0; i < tags.length; i++) {
            this.tag2indexMap.put(tags[i], i);
            this.index2tagMap.put(i, tags[i]);
        }

    }

    /**
     * @param wordSeq is input word sequence, format like ["中", "国"]
     *                every position only one char
     * */
    public void generateFeaturesOf(String[] wordSeq) {
        int wordSeqLen = wordSeq.length;
        for (int i = 0; i < wordSeqLen; i++) {
            for (String rawTemp: this.rawTemplates) {
                String keyVal = this.getTempWord(wordSeq, i, rawTemp);
                Integer tagVals[] = {0, 0, 0, 0};
                this.featuresFuncs.get(rawTemp).put(keyVal, tagVals);
            }
        }
    }

    /**
     * local variable tagScoresMatrix content like
     *      "今"  "天"  "晴"
     *  "S"  0     0     0
     *  "B"  1     0     2
     *  "I"  0     0     1
     *  "E"  1     3     0
     * */
    public String[][] tagWordSeq(String[] wordSeq) {
        ArrayList<Integer[]> tagScoresMatrix = new ArrayList<>();
        int seqLen = wordSeq.length;
        for (int i = 0; i < seqLen; i++) {
            Integer[] wordTagScore = {0, 0, 0, 0};
            for (String rawTemp: this.rawTemplates) {
                String tempwords = this.getTempWord(wordSeq, i, rawTemp);
                Integer[] score = this.featuresFuncs.get(rawTemp).get(tempwords);
                if (score != null && score.length == 4)
                    wordTagScore = Utils.addTwoIntArr(wordTagScore, score);
            }
            tagScoresMatrix.add(wordTagScore);
        }
        String[] tagSeq = new String[seqLen];
        for (int i = 0; i < seqLen; i++)
            tagSeq[i] = this.index2tagMap.get(Utils.maxOf(tagScoresMatrix.get(i)));
        String[][] resArr = {wordSeq, tagSeq};
        return resArr;
    }

    public void updateFeatureVals(String[] wordSeq, String[] idealTagSeq, String[] actuTagSeq) {
        int seqLen = wordSeq.length;
        for (int i = 0; i < seqLen; i++) {
            for (String rawTemp: this.rawTemplates) {
                String tempWords = this.getTempWord(wordSeq, i, rawTemp);
                Integer[] score = this.featuresFuncs.get(rawTemp).get(tempWords);
                if (score != null) {
                    score[this.tag2indexMap.get(idealTagSeq[i])]++;
                    score[this.tag2indexMap.get(actuTagSeq[i])]--;
                    //this.featuresFuncs.get(rawTemp).put(tempWords, score);
                }
            }
        }
    }

    private String getTempWord(String[] wordSeq, int currIndex, String rawTemp) {
        String[] temps = rawTemp.split("/");
        String tempwords = "";
        for (String temp: temps) {
            int tempWordIdx = currIndex + Utils.tempFirstNum(temp);
            String tempWord;
            if (tempWordIdx < 0 || tempWordIdx >= wordSeq.length)
                tempWord = " ";
            else
                tempWord = wordSeq[tempWordIdx];
            tempwords = tempwords + tempWord;
        }
        return tempwords;
    }
}

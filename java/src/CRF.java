import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CRF {
    double accuracy = 0;

    public static void crf_train(String trainFileName, String labelFileName, String templateFileName)
            throws IOException {
        String[] labels = getLabels(labelFileName);
        String[] temps = getTemplates(templateFileName);
        Features features = new Features(temps, labels);
        featureInit(features, trainFileName);
        String resFileName = "result.utf8";
        String testResFileName = "testResult.utf8";
        for (int i = 0; i < 100; i++) {
            System.out.println("train time: " + (i+1));
            train(features, trainFileName, resFileName);
            evalAndWrite(features, trainFileName, resFileName);
            System.out.println("------------------------------");
        }
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Input test file name: ");
            String testFileName = scanner.nextLine();
            FileInputStream testIS = new FileInputStream(testFileName);
            WordSeqReader wordSeqReader = new WordSeqReader(new InputStreamReader(testIS));
            FileOutputStream resOS = new FileOutputStream(testResFileName);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(resOS));
            ArrayList<String> wordList = wordSeqReader.readOneSeq();
            int sen_num = 0;
            while (wordList != null) {
                System.out.println("test No." + (sen_num++) + " sentence.");
                String[] wordSeq = Utils.strListToArray(wordList);
                String[][] res = features.tagWordSeq(wordSeq);
                for (int i = 0; i < res[0].length; i++) {
                    bufferedWriter.write(res[0][i] + " " + res[1][i]);
                    bufferedWriter.newLine();
                }
                bufferedWriter.newLine();
                wordList = wordSeqReader.readOneSeq();
            }
            testIS.close();
            wordSeqReader.close();
            bufferedWriter.close();
            resOS.close();
            System.out.println("done!");
        }
    }

    private static String[] getLabels(String labelFileName) throws IOException {
        // read labels
        ArrayList<String> labelList = new ArrayList<>();
        FileInputStream labelIS = new FileInputStream(labelFileName);
        BufferedReader labelReader = new BufferedReader(new InputStreamReader(labelIS));
        String label;
        while ((label = labelReader.readLine()) != null)
            labelList.add(label);
        labelIS.close();
        labelReader.close();
        return Utils.strListToArray(labelList);
    }

    private static String[] getTemplates(String templateFileName) throws IOException {
        // read templates
        // transform template U00:%x[0,0] into %x[0,0]
        ArrayList<String> tempList = new ArrayList<>();
        FileInputStream tempIS = new FileInputStream(templateFileName);
        BufferedReader tempReader = new BufferedReader(new InputStreamReader(tempIS));
        String rawTemp;
        while ((rawTemp = tempReader.readLine()) != null) {
            // if first char is #, give up the template
            if ((rawTemp.indexOf('%') != -1) && rawTemp.charAt(0) != '#')
                tempList.add(rawTemp.substring(rawTemp.indexOf('%')));
        }
        tempIS.close();
        tempReader.close();
        return Utils.strListToArray(tempList);
    }

    private static void featureInit(Features features, String trainFileName) throws IOException {
        // generate feature functions
        FileInputStream trainIS = new FileInputStream(trainFileName);
        WordSeqReader wordSeqReader = new WordSeqReader(new InputStreamReader(trainIS));
        ArrayList<String> wordAndTagList = wordSeqReader.readOneSeq();
        while (wordAndTagList != null) {
            int wordSeqLen = wordAndTagList.size();
            String[] wordSeq = new String[wordSeqLen];
            for (int i = 0; i < wordSeqLen; i++)
                wordSeq[i] = "" + wordAndTagList.get(i).charAt(0);
            features.generateFeaturesOf(wordSeq);
            //System.out.println(Utils.toString(wordSeq));
            wordAndTagList = wordSeqReader.readOneSeq();
        }
    }

    private static void train(Features features, String trainFileName, String resFileName) throws IOException {
        FileInputStream trainIS = new FileInputStream(trainFileName);
        WordSeqReader trainReader = new WordSeqReader(new InputStreamReader(trainIS));
        FileInputStream resIS = new FileInputStream(resFileName);
        WordSeqReader resReader = new WordSeqReader(new InputStreamReader(resIS));
        ArrayList<String> trainWordTagSeq = trainReader.readOneSeq();
        ArrayList<String> resWordTagSeq = resReader.readOneSeq();
        while (trainWordTagSeq != null && resWordTagSeq != null) {
            if (trainWordTagSeq.size() == resWordTagSeq.size()) {
                //System.out.println(resWordTagSeq);
                String[][] trainArr = wordAndTagToArr(trainWordTagSeq);
                String[][] resArr = wordAndTagToArr(resWordTagSeq);
                features.updateFeatureVals(trainArr[0], trainArr[1], resArr[1]);
            }
            trainWordTagSeq = trainReader.readOneSeq();
            resWordTagSeq = resReader.readOneSeq();
        }
        resReader.close();
        trainReader.close();
        resIS.close();
        trainIS.close();
    }

    private static void evalAndWrite(Features features, String trainFileName, String resFileName) throws IOException {
        FileInputStream trainIS = new FileInputStream(trainFileName);
        WordSeqReader wordSeqReader = new WordSeqReader(new InputStreamReader(trainIS));
        ArrayList<String> wordAndTagList = wordSeqReader.readOneSeq();
        FileOutputStream resOS = new FileOutputStream(resFileName);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(resOS));
        ArrayList<Double> accuracies = new ArrayList<>();
        while (wordAndTagList != null) {
            int wordSeqLen = wordAndTagList.size();
            String[][] transRes = wordAndTagToArr(wordAndTagList);
            String[] wordSeq = transRes[0];
            String[][] taggedSeq = features.tagWordSeq(wordSeq);
            accuracies.add(evalAccu(transRes[1], taggedSeq[1]));
            //System.out.println(Utils.toString(taggedSeq[0]));
            //System.out.println(Utils.toString(taggedSeq[1]));
            for (int i = 0; i < wordSeqLen; i++) {
                bufferedWriter.write(taggedSeq[0][i] + " " + taggedSeq[1][i]);
                bufferedWriter.newLine();
            }
            bufferedWriter.newLine();
            wordAndTagList = wordSeqReader.readOneSeq();
        }
        double accuracy = 0.0;
        for (Double accu: accuracies)
            accuracy += accu;
        accuracy /= accuracies.size();
        System.out.println("accuracy: " + accuracy*100 + "%");
        trainIS.close();
        wordSeqReader.close();

        bufferedWriter.close();
        resOS.close();
    }

    private static Map<String, String> wordAndTagToMap(ArrayList<String> wordAndTagSeq) {
        Map<String, String> wordTagMap = new HashMap<>();
        for (String wordAndTag: wordAndTagSeq) {
            String[] strs = wordAndTag.split(" ");
            wordTagMap.put(strs[0], strs[1]);
        }
        return wordTagMap;
    }

    private static String[][] wordAndTagToArr(ArrayList<String> wordAndTagSeq) {
        int seqLen = wordAndTagSeq.size();
        String[] wordSeq = new String[seqLen];
        String[] tagSeq = new String[seqLen];
        for (int i = 0; i < seqLen; i++) {
            String[] strs = wordAndTagSeq.get(i).split(" ");
            wordSeq[i] = strs[0];
            tagSeq[i] = strs[1];
        }
        String[][] resArr = {wordSeq, tagSeq};
        return resArr;
    }

    private static double evalAccu(String[] idealTags, String[] actuTags) {
        int equalNum = 0;
        for (int i = 0; i < idealTags.length; i++) {
            if (idealTags[i].equals(actuTags[i]))
                equalNum++;
        }
        return (1.0*equalNum/idealTags.length);
    }
}

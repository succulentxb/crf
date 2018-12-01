import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * WordSeqReader extends BufferedReader to add a method readOneSeq()
 * readOneSeq() can read a word sequence from train or test file
 * */
public class WordSeqReader extends BufferedReader {
    public WordSeqReader(InputStreamReader inputStreamReader) {
        super(inputStreamReader);
    }

    public ArrayList<String> readOneSeq() throws IOException {
        ArrayList<String> wordList = new ArrayList<>();
        String wordAndTag = this.readLine();
        while ((!"".equals(wordAndTag)) && wordAndTag != null) {
            wordList.add(wordAndTag);
            wordAndTag = this.readLine();
        }
        if (wordList.size() == 0)
            return null;
        return wordList;
    }
}

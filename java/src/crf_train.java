import java.io.IOException;

public class crf_train {
    public static void main(String[] args) throws IOException {
        //System.out.println(args.length);
        String trainFileName = null;
        String labelFileName = null;
        String templateFileName = null;
        if (args.length == 3) {
            trainFileName = args[0];
            labelFileName = args[1];
            templateFileName = args[2];
        }
        else if (args.length == 0) {
            trainFileName = "train.utf8";
            labelFileName = "labels.utf8";
            templateFileName = "template.utf8";
        }
        else {
            System.err.println("Error: wrong arguments.");
            System.exit(-1);
        }
        CRF.crf_train(trainFileName, labelFileName, templateFileName);
    }
}

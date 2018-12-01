import java.util.ArrayList;

public class Utils {
    public static int tempFirstNum(String temp) {
        String numStr = temp.substring(3, temp.length()-1);
        String[] numStrs = numStr.split(",");
        return Integer.parseInt(numStrs[0]);
    }

    public static String[] strListToArray(ArrayList<String> arrayList) {
        int listSize = arrayList.size();
        String strs[] = new String[listSize];
        for (int i = 0; i < listSize; i++)
            strs[i] = arrayList.get(i);
        return strs;
    }

    public static String toString(String[] strs) {
        String finalString = "";
        for (String str: strs)
            finalString += str + " ";
        return finalString;
    }

    public static Integer[] addTwoIntArr(Integer[] a, Integer[] b) {
        if (a.length != b.length)
            return null;
        Integer[] res = new Integer[a.length];
        for (int i = 0; i < res.length; i++)
            res[i] = a[i] + b[i];
        return res;
    }

    public static int maxOf(Integer[] arr) {
        int max = -100000;
        int maxIndex = 0;
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            if (arr[i] > max) {
                max = arr[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}

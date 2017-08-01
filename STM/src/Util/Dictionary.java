package Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Created by XJ on 2015/11/7.
 */
public class Dictionary {
    private static HashMap<String, Integer> word2id;
    private static HashMap<Integer, String> id2word;
    private static HashMap<Integer, String> id2cate;
    private static HashMap<String, Integer> cate2id;
    private static int index = 0;
    private static int cateIndex = 0;


    public static int initDic(String dicPath) {
        word2id = new HashMap<>();
        id2word = new HashMap<>();
        cate2id = new HashMap<>();
        id2cate = new HashMap<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(dicPath));
            String line;
            while ((line = br.readLine()) != null) {
                add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size();
    }

    public static void initDic() {
        word2id = new HashMap<>();
        id2word = new HashMap<>();
        cate2id = new HashMap<>();
        id2cate = new HashMap<>();
    }

    public static String getWord(int index) {
        if (id2word.containsKey(index))
            return id2word.get(index);
        else
            return null;
    }

    public static int getIndex(String word) {
         if (word2id.containsKey(word))
            return word2id.get(word);
        else
            return -1;
    }

    public static void output(String path) {
        try {
            PrintWriter pw = new PrintWriter(path);
            word2id.keySet().forEach(pw::println);
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int contains(String word) {
        if (word2id.containsKey(word))
            return word2id.get(word);
        else return -1;
    }

    public static String containsCate(int index) {
        if (id2cate.containsKey(index))
            return id2cate.get(index);
        return null;
    }

    public static int containsCate(String cate) {
        if (cate2id.containsKey(cate))
            return cate2id.get(cate);
        else
            return addCate(cate);
    }

    public static String contains(int index) {
        if (id2word.containsKey(index))
            return id2word.get(index);
        return null;
    }

    public static int size() {
        return word2id.size();
    }

    public static int size4cate() {
        return cate2id.size();
    }

    public static int add(String word) {
        int w = contains(word);
        if (w != -1)
            return w;
        word2id.put(word, index);
        id2word.put(index, word);
        index++;
        return index - 1;
    }

    public static int addCate(String cate) {
        cate2id.put(cate, cateIndex);
        id2cate.put(cateIndex, cate);
        cateIndex++;
        return cateIndex - 1;
    }
}

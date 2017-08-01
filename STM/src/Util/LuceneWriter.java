package Util;

import Util.paper.index.PaperAbsIndexWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by XJ on 2016/8/6.
 */
public class LuceneWriter {

    public static int writeIndex(String indexPath, String corpusPath, int cataSize, String catalogPath, boolean check) {
        ArrayList<String>[] groupInfo = new ArrayList[cataSize];
        for (int i = 0; i < cataSize; i++)
            groupInfo[i] = new ArrayList<>();

        BufferedReader br;
        String line;
        String[] vecs;
        int cateIndex = -1;
        try {
            br = new BufferedReader(new FileReader(catalogPath));
            while ((line = br.readLine()) != null) {
                vecs = line.split(" ");
                if (vecs[0].equals("cate")) {
                    cateIndex++;
                    continue;
                }
                groupInfo[cateIndex].add(vecs[0]);
            }
            br.close();
        } catch (Exception e) {
            System.out.println("catalog file path is invalid");
            e.printStackTrace();
            System.exit(-1);
        }


        //write groupIndex of train set
        if (groupInfo.length == 0)
            return -1;

        int numDoc = 0;

        numDoc += PaperAbsIndexWriter.startIndex(corpusPath + "\\" + groupInfo[0].get(0), indexPath, 0, true, check);
        for (int i = 1; i < groupInfo[0].size(); i++)
            numDoc += PaperAbsIndexWriter.startIndex(corpusPath + "\\" + groupInfo[0].get(i), indexPath, 0, false, check);

        for (int c = 1; c < groupInfo.length; c++) {
            for (String s : groupInfo[c])
                numDoc += PaperAbsIndexWriter.startIndex(corpusPath + "\\" + s, indexPath, c, false, check);
        }
        return numDoc;
    }

    public static int writeIndex_append(String indexPath, String corpusPath, int cataSize, String catalogPath, boolean check) {
        ArrayList<String>[] groupInfo = new ArrayList[cataSize];
        for (int i = 0; i < cataSize; i++)
            groupInfo[i] = new ArrayList<>();

        BufferedReader br;
        String line;
        String[] vecs;
        int cateIndex = -1;
        try {
            br = new BufferedReader(new FileReader(catalogPath));
            while ((line = br.readLine()) != null) {
                vecs = line.split(" ");
                if (vecs[0].equals("cate")) {
                    cateIndex++;
                    continue;
                }
                groupInfo[cateIndex].add(vecs[0]);
            }
            br.close();
        } catch (Exception e) {
            System.out.println("catalog file path is invalid");
            e.printStackTrace();
            System.exit(-1);
        }


        //write groupIndex of train set
        if (groupInfo.length == 0)
            return -1;

        int numDoc = 0;

        for (int i = 0; i < groupInfo[0].size(); i++)
            numDoc += PaperAbsIndexWriter.startIndex(corpusPath + "\\" + groupInfo[0].get(i), indexPath, 0, false, check);

        for (int c = 1; c < groupInfo.length; c++) {
            for (String s : groupInfo[c])
                numDoc += PaperAbsIndexWriter.startIndex(corpusPath + "\\" + s, indexPath, c, false, check);
        }
        return numDoc;
    }
}

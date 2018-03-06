package Util;

import java.util.Map;

/**
 * Created by XJ on 2015/12/18.
 */
public class MathUtil {


    public static void main(String[] args) {

    }

    public static int sample(double[] raw) {

        for (int i = 1; i < raw.length; i++) {
            raw[i] += raw[i - 1];
        }
        if (raw[raw.length - 1] == 0)
            return 0;
        double p = Math.random() * raw[raw.length - 1];
        int re;
        for (re = 0; re < raw.length; re++)
            if (p < raw[re])
                break;
        if (re > raw.length - 1)
            return -2;
        return re;
    }

    public static int sample_neg(double[] raw) {

        double max = raw[0];
        for (double d : raw)
            if (d > max)
                max = d;
        for (int i = 0; i < raw.length; i++)
            raw[i] = Math.pow(Math.E, raw[i] - max);
        for (int i = 1; i < raw.length; i++)
            raw[i] += raw[i - 1];

        for (int i = 0; i < raw.length; i++)
            raw[i] = max + Math.log(raw[i]);

        double r = Math.random();
        r = Math.log(r) + raw[raw.length - 1];

        int re;
        for (re = 0; re < raw.length; re++)
            if (r < raw[re])
                break;
        return re;
    }

    public static double[] proportion_neg(double[] raw) {
        double max = raw[0];
        double propor[]=new double[raw.length];
        for (double d : raw)
            if (d > max)
                max = d;
        double total = 0;
        for (int i = 0; i < raw.length; i++) {
            propor[i] =Math.exp(raw[i]- max);
            total += propor[i];
        }

        for(int i=0;i<propor.length;i++){
            propor[i]/=total;
        }
        return propor;
    }


    public static double cosSim(double[] a, double[] b) {
        double re = 0;
        double moda = 0;
        double modb = 0;

        if (a.length != b.length) {
            System.out.println("cosin Similarity");
            return -1;
        }
        for (double d : a)
            moda += Math.pow(d, 2);
        moda = Math.sqrt(moda);

        for (double d : b)
            modb += Math.pow(d, 2);
        modb = Math.sqrt(modb);

        for (int i = 0; i < a.length; i++)
            re += a[i] * b[i];
        re = re / (moda * modb);
        return re;
    }

    public static float cosSim(Map<Integer, Integer> a, Map<Integer, Integer> b) {
        float re = 0;
        for (int i : a.keySet())
            if (b.containsKey(i))
                re++;
        if (re == 0)
            return 0;
        re = 0;
        float moda = 0, modb = 0;
        for (int i : a.keySet())
            moda += Math.pow(a.get(i), 2);
        moda = (float) Math.sqrt(moda);

        for (int i : b.keySet())
            modb += Math.pow(b.get(i), 2);
        modb = (float) Math.sqrt(modb);

        for (int i : a.keySet())
            if (b.containsKey(i))
                re += ((float) a.get(i) / moda) * ((float) b.get(i) / modb);

        return re / (moda * modb);
    }

    /**
     * @param raw raw[0]=value , raw[1]=index
     * @return
     */
    public static double[] max(double[] raw) {
        double[] re = new double[2];
        re[0] = raw[0];
        for (int i = 0; i < raw.length; i++)
            if (raw[i] > re[0]) {
                re[0] = raw[i];
                re[1] = i;
            }
        return re;
    }

    public static int[] insertSort_decend(double[] raw, int len) {
        int[] re = new int[len + 1];
        double[] val = new double[len + 1];
        int rawLen = raw.length;
        int index;
        for (int r = 0; r < rawLen; r++) {
            index = len - 1;
            while (raw[r] > val[index]) {
                val[index + 1] = val[index];
                re[index + 1] = re[index];

                val[index] = raw[r];
                re[index] = r;
                index--;
                if (index < 0)
                    break;
            }
        }

        return re;
    }
}

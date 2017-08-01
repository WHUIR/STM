/**
 * 
 */
package Util;

import java.util.Comparator;

/**
 * @author Chenliang Li [cllee@whu.edu.cn]
 *
 */
public class CMPUtil {
  
  private CMPUtil(){ // noninstantiable class
    throw new AssertionError();
  }

  public static boolean isPos(double value){
    return Double.compare(0, value) < 0;
  }

  public static boolean isNeg(double value){
    return Double.compare(0, value) > 0;
  }

  public static boolean isZero(double value){
    return Double.compare(value, 0) == 0;
  }

  /**
   * Return 0 if the specified value is numerically equal to 0;
   * Return -1 if the specified value is numerically less than 0;
   * Return 1 if the specified value is numerically greater than 0;
   * @param value
   * @return
   */
  public static int cmpZero(double value){
    return Double.compare(value, 0.0);
  }

  public static Comparator<Long> LONG_ASC = 
      new Comparator<Long>() {

    @Override
    public int compare(Long l1, Long l2) {
      // TODO Auto-generated macroF1 stub
      return l1.compareTo(l2);
    }
  };

  public static Comparator<Long> LONG_DESC = 
      new Comparator<Long>() {

    @Override
    public int compare(Long l1, Long l2) {
      // TODO Auto-generated macroF1 stub
      return -l1.compareTo(l2);
    }
  };

  public static Comparator<Double> DOUBLE_ASC = 
      new Comparator<Double>() {
    @Override
    public int compare(Double d1, Double d2) {
      // TODO Auto-generated
      // macroF1 stub
      return d1.compareTo(d2);
    }
  };

  public static Comparator<Double> DOUBLE_DESC = 
      new Comparator<Double>() {
    @Override
    public int compare(Double d1, Double d2) {
      // TODO Auto-generated
      // macroF1 stub
      return -d1.compareTo(d2);
    }
  };

  public static Comparator<Integer> INT_ASC = 
      new Comparator<Integer>() {
    @Override
    public int compare(Integer d1, Integer d2) {
      // TODO Auto-generated
      // macroF1 stub
      return d1.compareTo(d2);
    }
  };

  public static Comparator<Integer> INT_DESC = 
      new Comparator<Integer>() {
    @Override
    public int compare(Integer d1, Integer d2) {
      // TODO Auto-generated
      // macroF1 stub
      return -d1.compareTo(d2);
    }
  };
}

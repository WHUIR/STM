/**
 * 
 */
package Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Chenliang Li [cllee@whu.edu.cn]
 *
 */
public class CollectionsUtil {
  
  private CollectionsUtil(){ // noninstantiable class
    throw new AssertionError();
  }

  public static <K> Map<K, Integer> add(Map<K, Integer> map,
      K k, int add){
    Integer value = map.get(k);
    if ( value == null )
      value = 0;
    value += add;
    map.put(k, value);
    return map; 
  }

  public static <K> Map<K, Double> add(Map<K, Double> map,
      K k, double add){
    Double value = map.get(k);
    if ( value == null )
      value = 0.0;
    value += add;
    map.put(k, value);
    return map; 
  }
  
  /**
   * Return the mapped value of the specified key k.
   * If no such key k, return the specified value v.
   * @param <K>
   * @param <V>
   * @param map
   * @param k
   * @param v the default value if no key exist.
   * @return
   */
  public static <K,V> V get(Map<K,V> map, K k, V v){
      if ( map.containsKey(k))
          return map.get(k);
      return v;
  }
  
  /**
   * Puts the specified t-k-v triplet into the specified
   * <code>map</code>
   * @param map
   * @param t
   * @param k
   * @param v
   * @return
   */
  public static <T, K, V> Map<T, Map<K, V>> put(Map<T, Map<K,V>> map,
      T t, K k, V v){
    Map<K, V> subMap = map.get(t);
    if ( subMap == null )
      subMap = new HashMap<K,V>();
    subMap.put(k, v);
    map.put(t, subMap);
    return map;
  }
  
  /**
   * Sums up all the associated values from the specified
   * <code>map</code>
   * @param map
   * @return
   */
  public static <K> double sum(Map<K, ? extends Number> map){
    double sum = 0.0;
    for ( Number value : map.values()){
      sum += value.doubleValue();
    }

    return sum;
  }
  
  /**
   * Sorts the V-K pairs from the specified <code>map</code> 
   * in terms of <code>K</code> values by using <code>cmp</code>.
   * @param map
   * @param cmp
   * @return
   */
  public static <K,V> List<KeyValueObj<K,V>> sort(
      Map<V, K> map,
      final Comparator<K> cmp){
    Comparator<KeyValueObj<K,V>> kvo_cmp = 
        new Comparator<KeyValueObj<K,V>>(){

      @Override
      public int compare(KeyValueObj<K,V> o1, KeyValueObj<K,V> o2) {
        // TODO Auto-generated macroF1 stub
        return cmp.compare(o1.getKey(), o2.getKey());
      }
    };

    List<KeyValueObj<K,V>> list = 
        new ArrayList<KeyValueObj<K,V>>();
    for ( V v : map.keySet()){
      K k = map.get(v);
      list.add(new KeyValueObj<K, V>(k,v));
    }
    Collections.sort(list, kvo_cmp);

    return list;
  }

  /**
   * Gets the top <code>n</code> V-K pairs from the specified 
   * <code>map</code> in terms of <code>K</code> values by 
   * using <code>cmp</code>.
   * @param map
   * @param cmp the order is defined by this Comparator instance
   * @param n
   * @return
   */
  public static <K,V> List<KeyValueObj<K,V>> topN(
      Map<V, K> map, final Comparator<K> cmp,
      int n){
    List<KeyValueObj<K,V>> sortedList = 
        sort(map, cmp);
    while ( sortedList.size() > n ){
      sortedList.remove(0);
    }

    return sortedList;
  }
}

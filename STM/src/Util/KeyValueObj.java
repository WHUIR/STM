/**
 * 
 */
package Util;

/**
 * @author Chenliang Li [cllee@whu.edu.cn]
 *
 */
public class KeyValueObj<K, V> {
  private K key;
  private V value;
  
  public KeyValueObj(K key, V value){
      this.key = key;
      this.value = value;
  }
  
  public K getKey(){
      return key;
  }
  
  public V getValue(){
      return value;
  }
  
  @Override
  public boolean equals(Object o){
    if ( o == this )
      return true;

    if ( o instanceof KeyValueObj<?, ?> ){
      KeyValueObj<K, V> other = (KeyValueObj<K, V>)o;
      return (
          key == null ? other.key == null : key.equals(other.key) &&
          value == null ? other.value == null : value.equals(other.value)
          );
    }

    return false;
  }
  
  @Override
  public int hashCode(){
    int code = super.hashCode();
    if ( key != null )
      code = code * 31 + key.hashCode();
    if ( value != null )
      code = code * 31 + value.hashCode();
    return code;
  }
}

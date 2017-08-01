/**
 * 
 */
package Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Chenliang Li [cllee@whu.edu.cn]
 *
 */
public class IOUtil {
  
  private IOUtil(){ // noninstantiable class
    throw new AssertionError();
  }
  
  /**
   * Returns a BufferedReader instance on the specified file.
   * 
   * @param file
   * @return
   * @throws IOException
   */
  public static BufferedReader getFileReader(File file) throws IOException {
    BufferedReader in = null;
    in = new BufferedReader(
        new InputStreamReader(new FileInputStream(file)));
    return in;
  }
  
  /**
   * Returns a BufferedReader instance on the specified file.
   * <code>charset</code> is used to specify the code scheme.
   * @param file
   * @param charset
   * @return
   * @throws IOException
   */
  public static BufferedReader getFileReader(String file, String charset )
      throws IOException {
    BufferedReader in = null;
    in = new BufferedReader(
        new InputStreamReader(new FileInputStream(file), charset));
    return in;
  }

  /**
   * Loads a String-Int map from the specified file <code>file</code>.
   * The file coding scheme is specified by <code>charset</code>.
   * @param file
   * @param charset
   * @return
   */
  public static Map<String, Integer> readStringToIntegerMap(
      String file, String charset){
    Map<String, Integer> map = new HashMap<String, Integer>();
    BufferedReader in = null;

    try {
      in = getFileReader(file, charset);
      String line = null;

      while ((line = in.readLine()) != null) {
        int index = line.lastIndexOf(',');
        if (index <= 0)
          continue;

        String key = line.substring(0, index);
        String value = line.substring(index + 1);
        int intValue = Integer.parseInt(value);
        map.put(key, intValue);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(-1);
    } finally {
      closeReader(in);
    }

    return map;
  }
  
  /**
   * Writes the specified String-Int <code>map</code> into the 
   * specified file <code>output</code>.
   * @param output
   * @param map
   */
  public static void writeStringToIntegerMap(
      String output, 
      Map<String, Integer> map){
    try {
      PrintWriter out = new PrintWriter(output);
      for ( String key : map.keySet()){
        int id = map.get(key);
        out.print(key);
        out.print(',');
        out.println(id);
      }
      out.flush();
      out.close();
    } catch ( Exception e ){
      e.printStackTrace();
      System.exit(-1);
    }
  }

  /**
   * Checks whether the specified file does exist.
   * @param file
   * @return
   */
  public static boolean exist(String file){
    try {
      File f = new File(file);
      if ( f.exists())
        return true;
    } catch ( Exception e ){
      e.printStackTrace();
    }

    return false;
  }

  public static void closeReader(Reader reader){
    try {
      if ( reader != null ){
        reader.close();
      }
    } catch ( Exception ignore ){
    }
  }
  
  /**
   * Returns the text contents of the specified file.
   * @param file
   * @return
   */
  public static String getFileText(String file) {
    StringBuilder sb = new StringBuilder();
    BufferedReader in = null;
    try {
      in = new BufferedReader(new InputStreamReader(
          new FileInputStream(file)));
      String line = null;

      while ((line=in.readLine())!=null){
        sb.append(line);
        sb.append('\n');
      }
    } catch ( Exception e ){
      e.printStackTrace();
    } finally {
      closeReader(in);
    }
    return sb.toString();
  }
  
  /**
   * Returns the text contents of the specified file by using
   * the default coding scheme.
   * @param file
   * @return
   */
  public static String getFileText(File file){
    StringBuilder sb = new StringBuilder();
    BufferedReader in = null;
    try {
      in = new BufferedReader(new InputStreamReader(
          new FileInputStream(file)));
      String line = null;

      while ((line=in.readLine())!=null){
        sb.append(line);
        sb.append('\n');
      }
    } catch ( Exception e ){
      e.printStackTrace();
    } finally {
      closeReader(in);
    }

    return sb.toString();
  }
  
  /**
   * Returns the text contents of the specified file by using
   * the specified charset coding scheme.
   * @param file
   * @param charset
   * @return
   */
  public static String getFileText(File file, String charset){
    StringBuilder sb = new StringBuilder();
    BufferedReader in = null;
    try {
      in = new BufferedReader(new InputStreamReader(
          new FileInputStream(file), charset));
      String line = null;

      while ((line=in.readLine())!=null){
        sb.append(line);
        sb.append('\n');
      }
    } catch ( Exception e ){
      e.printStackTrace();
    } finally {
      closeReader(in);
    }

    return sb.toString();
  }
  
  /**
   * Returns the text contents of the specified file.
   * @param file
   * @return
   */
  public static String getFileText(String file, String charset) {
    StringBuilder sb = new StringBuilder();
    BufferedReader in = null;
    try {
      in = new BufferedReader(new InputStreamReader(
          new FileInputStream(file), charset));
      String line = null;

      while ((line=in.readLine())!=null){
        sb.append(line);
        sb.append('\n');
      }
    } catch ( Exception e ){
      e.printStackTrace();
    } finally {
      closeReader(in);
    }

    return sb.toString();
  }
}

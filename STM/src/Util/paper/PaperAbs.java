/**
 * 
 */
package Util.paper;

/**
 * @author Chenliang Li [cllee@whu.edu.cn]
 *
 */
public class PaperAbs {
  
  private int id;
  private String abs; // the abstract of the paper
  private String title; // the title of this paper
  
  public PaperAbs(){
    // a empty instance
    id = -1;
    abs = "";
    title = "";
  }
  
  public PaperAbs(int id, String abs, String title){
    this.id = id;
    this.abs = abs;
    this.title = title;
  }
  
  public PaperAbs renew(PaperAbs reusable,
      int id, String abs, String title){
    reusable.id = id;
    reusable.abs = abs;
    reusable.title = title;
    return reusable;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getAbs() {
    return abs;
  }

  public void setAbs(String abs) {
    this.abs = abs;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
  
  @Override
  public int hashCode() {
    int code = super.hashCode();
    code = code * 31 + id;
    if (abs != null)
      code = code * 31 + abs.hashCode();
    if (title != null)
      code = code * 31 + title.hashCode();
    
    return code;
  }
  
  @Override
  public boolean equals(Object obj){
    if ( obj == this )
      return true;
    
    if ( obj instanceof PaperAbs){
      final PaperAbs other = (PaperAbs) obj;
      return (
          id == other.id &&
          abs == null ? other.abs == null : abs.equals(other.abs) &&
          title == null ? other.title == null : title.equals(other.title)
          );
    } else
      return false;
  }
  
  @Override
  public String toString(){
    StringBuilder buffer = new StringBuilder();
    buffer.append(id).append(',').append(abs==null ? "null" : abs);
    buffer.append(" FROM [").append(title==null ? "null" : title);
    buffer.append(']');
    return buffer.toString();
  }
}

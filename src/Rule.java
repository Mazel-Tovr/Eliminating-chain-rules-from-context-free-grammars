public class Rule
{
  public char nt; //non-terminal;
  public  String replace;

  public Rule(char nt, String replace) {
    this.nt = nt;
    this.replace = replace;
  }
}

package Model;

public class Rule
{
  private char nt; //non-terminal;
  private   String terminalString;

  public Rule(char nt, String terminalString) {
    this.nt = nt;
    this.terminalString = terminalString;
  }

  public char getNt() {
    return nt;
  }

  public String getTerminalString() {
    return terminalString;
  }
}

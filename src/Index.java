public class Index {

    //count of non terminals
    private int cntNT;
    //Non terminals array
    private char[] strNT;
    //position of new non terminal like ||S->...S->...B->...|| S-0;B-2 ||
    private int []startPos;
    //non terminal (on which position (look up ↑)) have chain and how many
    private int []chains;
    //on the contrary of (look up ↑)
    private int []nonChains;

    private Table table;

    public Index() {
        table = new Table();
    }

    public int getCntNT() {
        return cntNT;
    }

    public void setCntNT(int cntNT) {
        this.cntNT = cntNT;
    }

    public int[] getStartPos() {
        return startPos;
    }

    public int[] getChains() {
        return chains;
    }

    public int[] getNonChains() {
        return nonChains;
    }

    public Table getTable() {
        return table;
    }

    public void setStrNT(char[] strNT) {
        this.strNT = strNT;
    }

    public void setStartPos(int[] startPos) {
        this.startPos = startPos;
    }

    public void setChains(int[] chains) {
        this.chains = chains;
    }

    public void setNonChains(int[] nonChains) {
        this.nonChains = nonChains;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public char[] getStrNT() {
        return strNT;
    }
}

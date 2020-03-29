import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        char axiom='S';


        List<Rule> rules = new ArrayList<>();

        rules.add( new Rule('S', "aFb"));
        rules.add(new Rule('S', "A"));
        rules.add(new Rule('A', "aA"));
        rules.add(new Rule('A', "B"));
        rules.add( new Rule('B', "aSb"));
        rules.add(new Rule('B', "S"));
        rules.add( new Rule('F', "bc"));
        rules.add(new Rule('F', "bFc"));

        Grammar grammar = new Grammar(axiom,rules);

        System.out.println("This context-free grammar was entered:");
        printGrammar(grammar);

        Index index = new Index();
        presortGrammar(grammar,index);

        processGrammar(grammar,index);

        System.out.println("The grammar after re-ordering:");

        printGrammar(grammar);

        initTable(grammar, index);

        System.out.println("Matrix representation of the chain rules:");
        printTable(index);

        System.out.println("Table filling results (all the long chain substitutions are contracted):");
        fillTable(index.getTable());
        printTable(index);

        System.out.println("An equivalent grammar without chain rules:");
        printGrammarWithoutChains(grammar, index);

    }


    static void printGrammar(Grammar grammar) {
        List<Rule> rules = grammar.getRules();
        for (int i = 0; i < rules.size(); ++i) {
            Rule rule = rules.get(i);
            System.out.println(rule.getNt() + " -> " + rule.getTerminalString());
        }
    }

    static void  swap( List<Rule> list,  int indexA, int indexB) {
        Rule tmp = list.get(indexA);
        list.set(indexA, list.get(indexB));
        list.set(indexB, tmp);
    }


    static void presortGrammar(Grammar grammar, Index index) {
        char axiom = grammar.getAxiom();
        List<Rule> rules = grammar.getRules();
        int size = rules.size();

        int cntNT = 1;

        // searching for a rule which LHS is the axiom
        if (rules.get(0).getNt() != axiom) {
            int i;
            for (i = 1; i < size; ++i)
                if (rules.get(i).getNt() == axiom)
                    break;
            if (i < size)
                swap(rules,i , 0);
            else
                // all the symbols are unreachable!
                System.out.println("error");
        }

        char tmp = axiom;
        for (int i = 1; i < size; ++i) {
            if (rules.get(i).getNt() != tmp) {
                ++cntNT;
                tmp = rules.get(i).getNt();
            }
            int j = i + 1;
            while (j < size && rules.get(j).getNt() != tmp  )
                ++j;
            if (j < size && j != i + 1)
                swap(rules,i + 1, j);
        }
        index.setCntNT(cntNT);

    }


    static void  processGrammar(Grammar grammar, Index index)
    {
        List<Rule> rules = grammar.getRules();
        int size = rules.size();

        int cntNT = index.getCntNT();

        char [] strNT = new char[cntNT];

        int []startPos =new int[cntNT];

        int []chains = new int[cntNT];

        int []nonChains = new int[cntNT];


        char tmp = rules.get(0).getNt();
        int writePos = 0;
        int start = 0;
        int cntChains = 0;
        int cntNonChains = 0;
        for (int i = 0; i < size; ++i) {
            if (rules.get(i).getNt() != tmp) {
                strNT[writePos] = tmp;
                startPos[writePos] = start;
                start = i;
                chains[writePos] = cntChains;
                cntChains = 0;
                nonChains[writePos] = cntNonChains;
                cntNonChains = 0;

                ++writePos;
                tmp = rules.get(i).getNt();
            }

            String replace = rules.get(i).getTerminalString();
            if (Character.isUpperCase(replace.charAt(0)) && replace.length() ==1) {
                swap( rules,  start + cntChains,  i);
                ++cntChains;
            }
            else
                ++cntNonChains;
        }
        // calculations for the last non-terminal
        strNT[writePos] = tmp;
        //strNT[writePos + 1] = '\0';
        startPos[writePos] = start;
        chains[writePos] = cntChains;
        nonChains[writePos] = cntNonChains;

        // dumping the results
        index.setStrNT(strNT);
        index.setStartPos(startPos);
        index.setChains(chains);
        index.setNonChains(nonChains);

    }

    static int initTable(Grammar grammar, Index index)
    {

        int cntNT = index.getCntNT();
        boolean [][]table = new boolean[cntNT][cntNT];


        char[] strNT = index.getStrNT();
        int[] startPos = index.getStartPos();
        int[] chains = index.getChains();
        List<Rule> rules = grammar.getRules();

        for (int i = 0; i < cntNT; ++i)
            for (int k = startPos[i]; k < startPos[i] + chains[i]; ++k) {
                int j = findPos(strNT, rules.get(k).getTerminalString().charAt(0));//TODO хз
                // unproductive symbowls in chain rules are actually removed here.
                if (j >= 0) {
                    table[i][j]=true;
                }
            }
        index.getTable().setTable(table);
        index.getTable().setSize(cntNT);
        return 0;
    }

    static int findPos(char[] str, char c)
    {
        for (int i = 0; i <str.length ; i++) {
            if(str[i] == c) return i;
        }
        return -1;
    }

    static void printTable(Index index)
    {
        int size = index.getTable().getSize();
        boolean [][]table = index.getTable().getTable();
        char []strNT = index.getStrNT();
        System.out.print("   ");
        for (int i = 0; i < size; ++i)
            System.out.print(strNT[i]+" ");
        System.out.println();
        for (int i = 0; i < size; ++i) {
            System.out.print(strNT[i]+"|" );
            for (int j = 0; j < size; ++j)
                System.out.print(" "+(table[i][j]?1:0));
            System.out.println();
        }
        System.out.println();
    }

    static void fillTable(Table matrix)
    {

        int size = matrix.getSize();
        boolean[][] table = matrix.getTable();
        for (int i = 0; i < size; ++i)
            for (int j = 0; j < size; ++j)
                if (table[ i][ j])
                    for (int k = 0; k < size; ++k)
                        if (table[ j][ k])
                            table[ i][ k] = true;

        // clearing the main diagonal
        // chain rules of the type A -> A are excluded
        for (int i = 0; i < size; ++i)
            table[i][i] = false;
    }

    static void printGrammarWithoutChains(Grammar grammar, Index index)
    {
        int size = index.getCntNT();
        char []strNT = index.getStrNT();
        List<Rule> rules = grammar.getRules();

        int[] startPos = index.getStartPos();
        int[] chains = index.getChains();
        int[] nonChains = index.getNonChains();
        boolean[][] table = index.getTable().getTable();

        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j)
                if (table[i][j])
                    for (int k = startPos[j] + chains[j];
                         k < startPos[j] + chains[j] + nonChains[j]; ++k)
                        System.out.println(strNT[i]+" -> "+ rules.get(k).getTerminalString());

            for (int k = startPos[i] + chains[i];
                 k < startPos[i] + chains[i] + nonChains[i]; ++k)
                System.out.println(strNT[i]+" -> " + rules.get(k).getTerminalString());
        }
        System.out.println();;
    }
}

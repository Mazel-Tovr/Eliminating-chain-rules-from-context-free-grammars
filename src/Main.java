import java.lang.reflect.Array;

public class Main {
    public static void main(String[] args) {

        Grammar grammar = new Grammar();
        grammar.axiom = 'S';
        grammar.size = 8;
        grammar.rules = new Rule[grammar.size];

        Rule[] rules = grammar.rules;
        rules[0] = new Rule('S', "aFb");
        rules[1] = new Rule('S', "A");
        rules[2] = new Rule('A', "aA");
        rules[3] = new Rule('A', "B");
        rules[4] = new Rule('B', "aSb");
        rules[5] = new Rule('B', "S");
        rules[6] = new Rule('F', "bc");
        rules[7] = new Rule('F', "bFc");

        System.out.println("This context-free grammar was entered:");
        printGrammar(grammar);

        Index index = new Index();
        index.table = new Table();
        presortGrammar(grammar,index);

        processGrammar(grammar,index);

        System.out.println("The grammar after re-ordering:");

        printGrammar(grammar);

        initTable(grammar, index);

        System.out.println("Matrix representation of the chain rules:");
        printTable(index);
    }


    static void printGrammar(Grammar grammar) {
        for (int i = 0; i < grammar.size; ++i) {
            Rule rule = grammar.rules[i];
            System.out.println(rule.nt + " -> " + rule.replace);
        }
    }

    static void  swap(Rule[]array,  int indexA, int indexB) {
        Rule tmp = array[indexA];
        array[indexA] = array[indexB];
        array[indexB] = tmp;

    }


    static void presortGrammar(Grammar grammar, Index index) {
        char axiom = grammar.axiom;
        int size = grammar.size;
        Rule[] rules = grammar.rules;
        int cntNT = 1;

        // searching for a rule which LHS is the axiom
        if (rules[0].nt != axiom) {
            int i;
            for (i = 1; i < size; ++i)
                if (rules[i].nt == axiom)
                    break;
            if (i < size)
                swap(rules,i , 0);
            else
                // all the symbols are unreachable!
                System.out.println("error");
        }

        char tmp = axiom;
        for (int i = 1; i < size; ++i) {
            if (rules[i].nt != tmp) {
                ++cntNT;
                tmp = rules[i].nt;
            }
            int j = i + 1;
            while (j < size && rules[j].nt != tmp  )
                ++j;
            if (j < size && j != i + 1)
                swap(rules,i + 1, j);
        }
        index.cntNT = cntNT;

    }


    static void  processGrammar(Grammar grammar, Index index)
    {

        int size = grammar.size;
        Rule[] rules = grammar.rules;
        int cntNT = index.cntNT;

        char [] strNT = new char[cntNT + 1];

        int []startPos =new int[cntNT];

        int []chains = new int[cntNT];

        int []nonChains = new int[cntNT];


        char tmp = rules[0].nt;
        int writePos = 0;
        int start = 0;
        int cntChains = 0;
        int cntNonChains = 0;
        for (int i = 0; i < size; ++i) {
            if (rules[i].nt != tmp) {
                strNT[writePos] = tmp;
                startPos[writePos] = start;
                start = i;
                chains[writePos] = cntChains;
                cntChains = 0;
                nonChains[writePos] = cntNonChains;
                cntNonChains = 0;

                ++writePos;
                tmp = rules[i].nt;
            }

          String replace = rules[i].replace;
            if (Character.isUpperCase(replace.charAt(0)) && replace.length() ==1) {
                swap( rules,  start + cntChains,  i);
                ++cntChains;
            }
        else
            ++cntNonChains;
        }
        // calculations for the last non-terminal
        strNT[writePos] = tmp;
        strNT[writePos + 1] = '\0';
        startPos[writePos] = start;
        chains[writePos] = cntChains;
        nonChains[writePos] = cntNonChains;

        // dumping the results
        index.strNT = strNT;
        index.startPos = startPos;
        index.chains = chains;
        index.nonChains = nonChains;

    }

    static int initTable(Grammar grammar, Index index)
    {

        int cntNT = index.cntNT;
        boolean []table = new boolean[cntNT*cntNT];//(bool *)calloc(cntNT * cntNT, sizeof(bool));

        index.table.table = table;
        index.table.size = cntNT;
        char[] strNT = index.strNT;
        int[] startPos = index.startPos;
        int[] chains = index.chains;
        Rule[] rules = grammar.rules;

        for (int i = 0; i < cntNT; ++i)
            for (int k = startPos[i]; k < startPos[i] + chains[i]; ++k) {
                int j = findPos(strNT, rules[k].replace.charAt(0));//TODO хз
                // unproductive symbowls in chain rules are actually removed here.
                if (j >= 0)
                    table[cntNT * i + j] = true;
            }
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
        int size = index.table.size;
        boolean []table = index.table.table;
        char []strNT = index.strNT;
        System.out.print("   ");
        for (int i = 0; i < size; ++i)
            System.out.print(strNT[i]+" ");
        System.out.println();
        for (int i = 0; i < size; ++i) {
            System.out.print(strNT[i]+"|" );
            for (int j = 0; j < size; ++j)
                System.out.print(" "+(table[size * i + j]?1:0));
            System.out.println();
        }
        System.out.println();
    }
}

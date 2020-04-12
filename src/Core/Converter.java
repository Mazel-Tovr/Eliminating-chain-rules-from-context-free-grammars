package Core;

import Model.Grammar;
import Model.Index;
import Model.Rule;
import Model.Table;
import Printer.Printer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Converter
{
    private String FILE_PATH = "outPutResult.txt";
    private Grammar grammar;
    private Index index;

    public Converter( Grammar grammar , String FILE_PATH ) {
        this.grammar = grammar;
        index = new Index();
        this.FILE_PATH = FILE_PATH.isEmpty() ? this.FILE_PATH : FILE_PATH;
    }
    public Converter( Grammar grammar) {
        this.grammar = grammar;
        index = new Index();
    }
   public void eliminateChanRules() throws IOException {

       Printer printer = new Printer(FILE_PATH);

       printer.printGrammar("This context-free grammar was entered:",grammar);

       presortGrammar(grammar,index);

       searchingForChainRules(grammar,index);

       System.out.println();

       printer.printGrammar("The grammar after re-ordering:",grammar);

       initTable(grammar, index);

       printer.printTable("Matrix representation of the chain rules:",index);

       fillTable(index.getTable());
       printer.printTable("Model.Table filling results (all the long chain substitutions are contracted):",index);

       printer.printGrammarWithoutChains(grammar, index);
       printer.flush();
   }

    /*Sort rules like:
    S->aAb
    A->bSa
    S->A
    --To--
    S->A
    S->aAb
    A->bSa
    */
    private void presortGrammar(Grammar grammar, Index index) {
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
                Util.swap(rules,i , 0);
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
                Util.swap(rules,i + 1, j);
        }
        //set non terminals
        index.setCntNT(cntNT);

    }

    //Finding chan rules and Fill index
    private void searchingForChainRules(Grammar grammar, Index index)
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
                Util.swap( rules,  start + cntChains,  i);
                ++cntChains;
            }
            else
                ++cntNonChains;
        }
        // calculations for the last non-terminal
        strNT[writePos] = tmp;
        startPos[writePos] = start;
        chains[writePos] = cntChains;
        nonChains[writePos] = cntNonChains;

        // dumping the results
        index.setStrNT(strNT);
        index.setStartPos(startPos);
        index.setChains(chains);
        index.setNonChains(nonChains);

    }

    //Int chain rules table
    private void initTable(Grammar grammar, Index index)
    {
        int cntNT = index.getCntNT();
        boolean [][]table = new boolean[cntNT][cntNT];

        char[] strNT = index.getStrNT();
        int[] startPos = index.getStartPos();
        int[] chains = index.getChains();
        List<Rule> rules = grammar.getRules();

        for (int i = 0; i < cntNT; ++i)
            for (int k = startPos[i]; k < startPos[i] + chains[i]; ++k) {
                int j = Util.findPos(strNT, rules.get(k).getTerminalString().charAt(0));
                // unproductive symbols in chain rules are actually removed here.
                if (j >= 0) {
                    table[i][j]=true;
                }
            }
        index.getTable().setTable(table);
        index.getTable().setSize(cntNT);
    }




    //add into table rules that linked with pref rules (like recurs)
    private void fillTable(Table matrix)
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
}

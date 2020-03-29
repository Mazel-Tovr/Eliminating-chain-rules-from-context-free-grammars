package Printer;

import Model.Grammar;
import Model.Index;
import Model.Rule;

import java.util.List;

public class Printer {

    public void printGrammar(String message,Grammar grammar) {
        System.out.println(message);
        List<Rule> rules = grammar.getRules();
        for (Rule rule : rules) {
            System.out.println(rule.getNt() + " -> " + rule.getTerminalString());
        }
    }

    public void printTable(String message, Index index)
    {
        System.out.println(message);
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
    public void printGrammarWithoutChains(Grammar grammar, Index index)
    {
        System.out.println("An equivalent grammar without chain rules:");
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

package Printer;

import Model.Grammar;
import Model.Index;
import Model.Rule;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Printer {

    private FileWriter fileWriter;

    public Printer(String FILE_PATH) throws IOException {
            fileWriter =  new FileWriter(FILE_PATH);
    }

    public void printGrammar(String message, Grammar grammar) throws IOException {
        fileWriter.write(message+"\n");
        System.out.println(message);
        List<Rule> rules = grammar.getRules();
        for (Rule rule : rules) {
            System.out.println(rule.getNt() + " -> " + rule.getTerminalString());
            fileWriter.write(rule.getNt() + " -> " + rule.getTerminalString()+"\n");
        }
        fileWriter.write("\n");
    }

    public void printTable(String message, Index index) throws IOException {
        fileWriter.write(message+"\n");
        System.out.println(message);
        int size = index.getTable().getSize();
        boolean [][]table = index.getTable().getTable();
        char []strNT = index.getStrNT();
        System.out.print("   ");
        fileWriter.write("   ");
        for (int i = 0; i < size; ++i) {
            System.out.print(strNT[i] + " ");
            fileWriter.write(strNT[i] + " ");
        }
        fileWriter.write("\n");
        System.out.println();
        for (int i = 0; i < size; ++i) {
            System.out.print(strNT[i]+"|" );
            fileWriter.write(strNT[i] + "|");
            for (int j = 0; j < size; ++j) {
                System.out.print(" " + (table[i][j] ? 1 : 0));
                fileWriter.write(" " + (table[i][j] ? 1 : 0));
            }
            System.out.println();
            fileWriter.write("\n");
        }
        System.out.println();
        fileWriter.write("\n");
    }
    public void printGrammarWithoutChains(Grammar grammar, Index index) throws IOException {

        System.out.println("An equivalent grammar without chain rules:");
        fileWriter.write("An equivalent grammar without chain rules:"+"\n");
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
                         k < startPos[j] + chains[j] + nonChains[j]; ++k){
                        System.out.println(strNT[i]+" -> "+ rules.get(k).getTerminalString());
                        fileWriter.write(strNT[i]+" -> "+ rules.get(k).getTerminalString()+"\n"); //mb switch
                    }

            for (int k = startPos[i] + chains[i];
                 k < startPos[i] + chains[i] + nonChains[i]; ++k) {
                System.out.println(strNT[i] + " -> " + rules.get(k).getTerminalString());
                fileWriter.write(strNT[i] + " -> " + rules.get(k).getTerminalString()+"\n");
            }
        }
        fileWriter.write("\n");
        System.out.println();;
    }

    public void flush() throws IOException {
        fileWriter.flush();
        fileWriter.close();
    }
}

import Core.Converter;
import Model.Grammar;
import Model.Rule;
import Parser.FileParser;

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

        Converter converter = new Converter(grammar);
      //  converter.eliminateChanRules();

        FileParser fileParser = new FileParser();
        try {
            converter = new Converter(fileParser.getGrammar("D:\\GitHub\\Eliminating-chain-rules-from-context-free-grammars\\testRulesInput2.txt"));
            converter.eliminateChanRules();
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }



}

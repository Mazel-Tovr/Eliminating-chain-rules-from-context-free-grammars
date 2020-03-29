import Core.Converter;
import Model.Grammar;
import Model.Index;
import Model.Rule;
import Model.Table;

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
        converter.eliminateChanRules();


    }



}

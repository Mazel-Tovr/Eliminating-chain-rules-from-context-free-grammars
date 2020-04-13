import Core.Converter;
import Model.Rule;
import Parser.FileParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            Converter converter = new Converter(new FileParser().getGrammar("D:\\GitHub\\Eliminating-chain-rules-from-context-free-grammars\\testRulesInput1.txt"));
            converter.eliminateChanRules();
        }catch (Exception e)
        {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}

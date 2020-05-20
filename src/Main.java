import Core.Converter;
import Model.Rule;
import Parser.FileParser;

import java.io.File;
import java.util.*;

public class Main {
    //"D:\\GitHub\\Eliminating-chain-rules-from-context-free-grammars\\testRulesInput1.txt"
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Введите путь к грамматике");
            String path = scanner.nextLine();

            Converter converter = new Converter(new FileParser().getGrammar(path),new File(".").getAbsolutePath());
            converter.eliminateChanRules();

        }catch (Exception e)
        {
            System.err.println(e.toString());
        }
        scanner.nextLine();
    }
}

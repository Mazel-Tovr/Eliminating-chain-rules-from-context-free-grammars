package Parser;

import Model.Grammar;
import Model.Rule;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileParser {

    private char axiom;

    private String readFile(String filePath) throws IOException {
        StringBuilder fileInput = new StringBuilder();
        Files.lines(Paths.get(filePath), StandardCharsets.UTF_8).forEach(e -> fileInput.append(e).append(";"));
        return fileInput.toString();
    }

    private List<Rule> getRules(String filePath) throws Exception {
        List<Rule> rulesList = new ArrayList<>();
        String fileInput = readFile(filePath);
        String[] rules = fileInput.split(";");

        if (rules[0].length() != 1)
            throw new Exception("Error: You should to pointed axiom");
        axiom = rules[0].charAt(0);
        for (int i = 1; i < rules.length; i++) {
            String[] splitInputRule = rules[i].split("->");
            if (splitInputRule.length != 2)
                throw new Exception("Error: Wrong rule input ");
            char tempNT = splitInputRule[0].charAt(0);
            for (String terminalString : splitInputRule[1].split("\\|")) {
                rulesList.add(new Rule(tempNT, terminalString));
            }

        }
        return rulesList;
    }

    public Grammar getGrammar(String filePath) throws Exception {
        List<Rule> ruleList = getRules(filePath);
        return new Grammar(axiom, ruleList);
    }

}

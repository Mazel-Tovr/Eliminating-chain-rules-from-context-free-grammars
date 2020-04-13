package Model;

import java.util.List;

public class Grammar {

   private char axiom;
   private List<Rule> rules;

   public Grammar(char axiom, List<Rule> rules) {
      this.axiom = axiom;
      this.rules = rules;
   }

   public char getAxiom() {
      return axiom;
   }

   public List<Rule> getRules() {
      return rules;
   }

}

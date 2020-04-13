package Core;

import Model.Rule;

import java.util.List;

public class Util
{
    //Swap position of 2 rules
    static void  swap(List<Rule> list, int indexA, int indexB) {
        Rule tmp = list.get(indexA);
        list.set(indexA, list.get(indexB));
        list.set(indexB, tmp);
    }

    //find position of letter in char array (non terminal)
    static int findPos(char[] strNt, char c)
    {
        for (int i = 0; i <strNt.length ; i++) {
            if(strNt[i] == c) return i;
        }
        return -1;
    }
}

import java.util.HashMap;
import java.util.Map;

public class TrieNode {

    //Used to represent child nodes
    Map<Character, TrieNode> trieNodes;

    //Count of number of occurrences of a certain word till current node
    Integer numberOfWordsCompletingHere;

    TrieNode(){
        trieNodes = new HashMap<>();
        numberOfWordsCompletingHere = 0;
    }

    TrieNode nextNode(final char c){
        if(trieNodes.containsKey(c)){
            return trieNodes.get(c);
        }
        return null;
    }

    void createLinkOf(final char c){
        TrieNode newTrieNode = new TrieNode();
        trieNodes.put(c, newTrieNode);
    }

    void DeleteLinkOf(final char c){
        trieNodes.remove(c);
    }

    void wordCompleted(){
        numberOfWordsCompletingHere++;
    }

    void decrementWordsCompleted(){
        numberOfWordsCompletingHere--;
    }

    int getCompletedWords(){
        return numberOfWordsCompletingHere;
    }

    boolean isLeaf(){
        return trieNodes.isEmpty();
    }

}

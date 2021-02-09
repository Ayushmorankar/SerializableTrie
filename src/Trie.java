import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;


public class Trie {

    private final TrieNode root;

    //Default Constructor creates an empty trie
    Trie(){
        this.root = new TrieNode();
    }

    //Used in deserialization
    private Trie(TrieNode root){
        this.root = root;
    }

    //Constructs Trie from the ArrayList passed in the constructor
    Trie(ArrayList<String> listOfStrings) throws Exception {
        this.root = new TrieNode();
        for(String s: listOfStrings){
            this.insert(s);
        }
    }

    //Constructs a trie by reading words from the given absolute path of the file
    public Trie(String filePath) throws Exception {
        this.root = new TrieNode();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        while(true){
            final String s = reader.readLine();
            if(s == null) break;
            this.insert(s);
        }
    }

    //Checks if the given word is present in the trie
    public boolean isPresent(final String s){

        final int stringLength = s.length();
        char c;

        TrieNode currentNode = root;
        for(int i = 0; i<stringLength; i++){
            c = s.charAt(i);
            if(currentNode.nextNode(c) == null){
                return false;
            }
            currentNode = currentNode.nextNode(c);
        }
        return currentNode.getCompletedWords()>0;
    }


    //inserts a word into the trie
    public void insert(final String s) throws Exception {

        final int stringLength = s.length();

        if(stringLength == 0){
            throw new Exception("Empty string not allowed");
        }

        TrieNode currentNode = root;
        char c;

        for(int i = 0; i<stringLength; i++){
            c = s.charAt(i);
            if(currentNode.nextNode(c) == null){
                currentNode.createLinkOf(c);
            }
            currentNode = currentNode.nextNode(c);
            if(i == stringLength-1){
                currentNode.wordCompleted();
            }
        }
    }

    //A helper function that recursively deletes TrieNodes
    private boolean deleteHelper(TrieNode node, int currentPosition, String s) throws Exception {

        if(currentPosition == s.length()){
            if(node.isLeaf()) {
                if(node.getCompletedWords() == 1) return true;
                node.decrementWordsCompleted();
                return false;
            }
            else if(node.getCompletedWords() == 0){
                throw new Exception();
            }
            node.decrementWordsCompleted();
            return false;
        }

        final char c = s.charAt(currentPosition);
        TrieNode childNode = node.nextNode(c);

        if(deleteHelper(childNode, currentPosition+1, s)){
            node.DeleteLinkOf(c);
            return node.isLeaf();
        }
        return false;
    }

    //Deletes the given word if present
    public void delete(final String s) throws Exception {
        try {
            deleteHelper(root, 0, s);
        }
        catch (Exception e){
            throw new Exception("No such string");
        }
    }

    //A helper function that does a dfs and returns list of reversed suffixes
    private List<StringBuilder> fetchHelper(TrieNode node){
        if(node.isLeaf()){
            return new ArrayList<>();
        }
        List<StringBuilder> allStrings = new ArrayList<>();
        TrieNode childNode;
        char c;
        for(Map.Entry element: node.trieNodes.entrySet()){
            childNode = (TrieNode) element.getValue();
            c = (char) element.getKey();
            List<StringBuilder> list = fetchHelper(childNode);
            for(StringBuilder sb: list){
                allStrings.add(sb.append(c));
            }
            if(childNode.getCompletedWords()>0) allStrings.add(new StringBuilder().append(c));
        }
        return allStrings;
    }

    //Function to fetch all unique strings
    public List<String> fetchAllUniqueStrings(){

        List<StringBuilder> returnValue = fetchHelper(root);
        List<String> allStrings = new ArrayList<>();
        for(StringBuilder sb: returnValue){
            allStrings.add(sb.reverse().toString());
        }
        return allStrings;
    }

    //Returns the number of occurrences of a string
    public int findNumberOfOccurrencesOf(final String s) throws Exception {

        final int stringLength = s.length();
        char c;

        TrieNode currentNode = root;
        for(int i = 0; i<stringLength; i++){
            c = s.charAt(i);
            if(currentNode.nextNode(c) == null){
                throw new Exception("No such string");
            }
            currentNode = currentNode.nextNode(c);
        }
        return currentNode.getCompletedWords();
    }

    //A utility function that serializes trie
    private StringBuilder serializer(TrieNode node){
        StringBuilder sb = new StringBuilder();
        if(node.isLeaf()){
            sb.append('<').append(node.getCompletedWords()).append('>');
            return sb;
        }
        sb.append('<');
        char c;
        TrieNode childNode;
        for(Map.Entry element: node.trieNodes.entrySet()){
            c = (char) element.getKey();
            childNode = (TrieNode) element.getValue();
            sb.append(c);
            StringBuilder child = serializer(childNode);
            sb.append(child);
        }
        if(node.getCompletedWords()>0)
        sb.append(node.getCompletedWords());
        sb.append('>');
        return sb;
    }

    //This function returns serialized trie as a string
    public String serialize(){
        StringBuilder serializedTrie = new StringBuilder();
        serializedTrie = serializer(this.root);
        return serializedTrie.toString();
    }

    //This is a static method used to construct a trie from its serialized form
    public static Trie deserialize(final String serializedString) throws Exception {

        if(serializedString == null || serializedString.length() == 0){
            throw new Exception("Could not deserialize");
        }

        TrieNode root = new TrieNode();
        TrieNode currentNode = root;
        Stack<TrieNode> store = new Stack<>();
        char[] serializedForm = serializedString.toCharArray();


        StringBuilder occurrence = new StringBuilder();
        for (char c : serializedForm) {
            if (c == '<') {
                store.push(currentNode);
            }
            else if (c == '>') {
                if(!occurrence.isEmpty())
                store.peek().numberOfWordsCompletingHere = Integer.parseInt(occurrence.toString());
                store.pop();
                occurrence.replace(0, occurrence.length(), "");
            }
            else {
                int asciiValue = c;
                if (asciiValue >= 48 && asciiValue <= 57) {
                    if(asciiValue == 48 && occurrence.isEmpty()) continue;
                    else {
                        occurrence.append(c);
                    }
                }
                else {
                    currentNode = new TrieNode();
                    store.peek().trieNodes.put(c, currentNode);
                }
            }
        }
        return new Trie(root);
    }

}

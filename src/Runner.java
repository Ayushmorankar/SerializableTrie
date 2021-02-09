import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 *The Runner class constructs a trie from the given serialized trie and checks for all values from TestData1.txt which
 * was used to construct the original trie which was serialized.
 * It then checks for any missing word
 **/
public class Runner {

    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new FileReader("D:\\Javaprojects\\projects\\SerializableTrie\\src\\SerializedForm2.txt"));
        String serializedString;
        serializedString = br.readLine();

        Trie deserializedTrie = Trie.deserialize(serializedString);

        BufferedReader reader = new BufferedReader(new FileReader("D:\\Javaprojects\\projects\\SerializableTrie\\src\\TestData2.txt"));
        boolean found = true;
        while (true){
            String s = reader.readLine();
            if(s == null) break;
            if(!deserializedTrie.isPresent(s)){
                found = false;
                reader.close();
                break;
            }
        }
        if(found) System.out.println("All Present");
        else System.out.println("Some words are missing missing");
    }
}

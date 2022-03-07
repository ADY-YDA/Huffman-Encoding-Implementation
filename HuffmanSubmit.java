import java.util.*;
import java.io.*;

/*
* HuffmanSubmit class containing all methods and constructors relevant to Trie object type
*
* Angelina Yang
*/

/**
* This class constructs and implements a Huffman Tree to encode and decode files.
* Encode method outputs a frequency file and a encoded file using file to encode.
* Decode method outputs a decoded file using a frequency file and encoded file.
*/


public class HuffmanSubmit implements Huffman {
  ArrayList<Character> boo = new ArrayList<>();
  ArrayList<Integer> frequency = new ArrayList<>();
  ArrayList<Node> nodes  = new ArrayList<>();
  ArrayList<String> chars = new ArrayList<>();
  ArrayList<String> codes = new ArrayList<>();
  int height = 1;
  String code = "";

  //Node class
  class Node
  {
    //pointer to left node, always 0
    private Node left = null;
    //pointer to right node, always 1
    private Node right = null;
    //value of internal node or leaf, 0 or 1
    private String value = " ";
    //char data held in leaf node
    private String data = " ";
    //frequency of char in file
    private int freq = 0;
    Node () {}
    Node (String val)
    {
      value = val;
    }
    Node (String dat, int fre)
    {
      data = dat;
      freq = fre;
    }
    Node getRight()
    {
      return right;
    }
    void setRight(Node r)
    {
      right = r;
    }
    Node getLeft()
    {
      return left;
    }
    void setLeft(Node l)
    {
      left = l;
    }
    String getValue()
    {
      return value;
    }
    void setValue(String val)
    {
      value = val;
    }
    String getData()
    {
      return data;
    }
    void setData(String str)
    {
      data = str;
    }
    int getFreq()
    {
      return freq;
    }
    void setFreq(int fre)
    {
      freq = fre;
    }
  }

  public Node buildTree()
  {
    Node root = null;
    while (nodes.size()>1)
    {
      Node x = minNode();
      nodes.remove(nodes.indexOf(x));
      Node y = minNode();
      nodes.remove(nodes.indexOf(y));
      Node mid = new Node();
      mid.setFreq(x.getFreq()+y.getFreq());
      mid.setRight(y);
      mid.setLeft(x);
      root = mid;
      nodes.add(mid);
      height++;
    }
    return root;
  }

  //returns if node is a leaf
  public boolean isLeaf(Node c) throws NullPointerException
  {
    if (c.getLeft() == null
      && c.getRight() == null)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  //generates arraylist of chars and codes from huffman tree
  public void treeList(Node r, String st)
  {
    if (r.getLeft()
            == null
        && r.getRight()
               == null)
    {
      chars.add(r.getData());
      String coding = st;
      codes.add(coding);
      return;
    }
    treeList(r.getLeft(), st+"0");
    treeList(r.getRight(), st+"1");
  }

  //searches and finds matching code to char
  public String searchList(Node r, char c)
  {
    String ch = Character.toString(c);
    for (int x = 0; x < chars.size(); x++)
    {
      if (chars.get(x).equals(ch))
      {
        return codes.get(x);
      }
    }
    return "";
  }

  //searches and finds matching char to code string
  public String compareList(Node r, String comp)
  {
    for (int x = 0; x < codes.size(); x++)
    {
      if (codes.get(x).equals(comp))
      {
        return chars.get(x);
      }
    }
    return "";
  }

  //used to append number of values to remove padded 0s later
  public void appendFreqFile(String freqFile, int x) throws IOException
  {
    FileWriter fw = new FileWriter(freqFile, true);
    fw.write("X:"+x);
    fw.close();
  }

  //outputs frequency file using arraylist of nodes
  public void writeFreqFile(String freqFile) throws IOException
  {
    FileWriter fw = new FileWriter(freqFile);
    for (Node n : nodes)
    {
      char c = n.getData().charAt(0);
      String bin = String.format("%8s", Integer.toBinaryString(c).replace(' ', '0'));
      bin = String.format("%8s", bin.replace(' ', '0'));
      fw.write(bin+":"+n.getFreq());
      fw.write("\n");
    }
    fw.close();
  }

  //reads frequency file and generates arraylist of nodes
  public void readFreqFile(String freqFile) throws IOException
  {
    File file = new File(freqFile);
    if(! (file.exists() && file.canRead()))
    {
        System.err.println("Cannot access file! Non-existent or read access restricted");
    }
    List<String> lines = new ArrayList<String>();
    try
    {
      //scanner to read file
        Scanner scanner = new Scanner(file);
        while(scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }
        scanner.close();
    }
    catch (FileNotFoundException ex)
    {}
    for (String st : lines)
    {
      String[] stArray = st.split(":");
      if (!stArray[0].equals("X"))
      {
        String edit = stArray[0];
        int ed = Integer.parseInt(edit, 2);
        char e = (char) ed;
        nodes.add(new Node(Character.toString(e),Integer.parseInt(stArray[1])));
      }
    }
  }

  //returns number of values without padded 0s recorded earlier
  public int findX(String freqFile) throws IOException
  {
    File file = new File(freqFile);
    if(! (file.exists() && file.canRead()))
    {
        System.err.println("Cannot access file! Non-existent or read access restricted");
    }
    List<String> lines = new ArrayList<String>();
    try
    {
      //scanner to read file
        Scanner scanner = new Scanner(file);
        while(scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }
        scanner.close();
    }
    catch (FileNotFoundException ex)
    {}
    for (String st : lines)
    {
      String[] stArray = st.split(":");
      if (stArray[0].equals("X"))
      {
        return Integer.parseInt(stArray[1]);
      }
    }
    return -1;
  }

  //finds the node with smallest freq in arraylist of nodes
  public Node minNode()
  {
    Node min = new Node("", nodes.get(0).getFreq()+1);
    int index = 0;
    for (int x = 0; x < nodes.size(); x++)
    {
      Node n = nodes.get(x);
      if ( n.getFreq() < min.getFreq())
      {
        min = n;
        index = x;
      }
    }
    return min;
  }

  //takes in file to encode and outputs frequency file and encoded file
	public void encode(String inputFile, String outputFile, String freqFile)
  {
    BinaryIn in = new BinaryIn(inputFile);
    BinaryOut out = new BinaryOut(outputFile);
    while (!in.isEmpty())
    {
      Character b = in.readChar();
      if (boo.contains(b))
      {
        int temp = (int)frequency.get(boo.indexOf(b));
        temp++;
        frequency.set(boo.indexOf(b), temp);
      }
      else
      {
        boo.add(b);
        frequency.add(1);
      }
    }
    while (boo.size() > 0)
    {
      int n = 0;
      int index = 0;
      for (int x = 0; x < frequency.size(); x++)
      {
        int f = frequency.get(x);
        if ( f > n)
        {
          n = f;
          index = x;
        }
      }
      nodes.add(new Node(Character.toString(boo.get(index)), frequency.get(index)));
      boo.remove(index);
      frequency.remove(index);
    }
    try
    {
      writeFreqFile(freqFile);
    }
    catch (IOException e)
    {}
    Node root = buildTree();
    treeList(root, "");
    BinaryIn repeat = new BinaryIn(inputFile);
    int tally = 0;
    while (!repeat.isEmpty())
    {
      Character b = repeat.readChar();
      String c = searchList(root, b);
      for (int x = 0; x < c.length(); x++)
      {
        tally++;
        if (c.charAt(x) == '0')
        {
          out.write(false);
        }
        else if (c.charAt(x) == '1')
        {
          out.write(true);
        }
      }
    }
    out.flush();
    //records number of values to remove padded 0s later
    try
    {
      appendFreqFile(freqFile, tally);
    }
    catch (IOException e)
    {}
   }

   //takes in file to decode and frequency file and outputs decoded file
   public void decode(String inputFile, String outputFile, String freqFile)
   {
     BinaryIn in = new BinaryIn(inputFile);
     BinaryOut out = new BinaryOut(outputFile);
     int tally = 0;
     try
     {
       readFreqFile(freqFile);
       tally = findX(freqFile);
     }
     catch (IOException e)
     {}
     Node root = buildTree();
     treeList(root, "");
     int t = 0;
     String temp = "";
     while (!in.isEmpty() && t<tally)
     {
       boolean b = in.readBoolean();
       if (b == false)
       {
         temp = temp + "0";
       }
       else if (b == true)
       {
         temp = temp + "1";
       }
       if (!compareList(root, temp).equals(""))
       {
         out.write(compareList(root, temp).charAt(0));
         temp = "";
       }
       t++;
     }
     out.flush();
   }

   //Only uncomment one line at a time if not initializing multiple HuffmanSubmit()!!!
   public static void main(String[] args) {
    Huffman  huffman1 = new HuffmanSubmit();
    huffman1.encode("testing.jpg", "testing.enc", "freq.txt");
    // huffman1.encode("alice30.txt", "alice30.enc", "freq.txt");
    Huffman  huffman2 = new HuffmanSubmit();
    huffman2.decode("testing.enc", "testing_dec.jpg", "freq.txt");
    // huffman2.decode("alice30.enc", "alice30_dec.txt", "freq.txt");

		// After decoding, both testing.jpg and testing_dec.jpg
    //  and both alice30.txt and alice30_dec.txt should be the same.
		// On linux and mac, you can use 'diff' command to check if they are the same.
   }

}

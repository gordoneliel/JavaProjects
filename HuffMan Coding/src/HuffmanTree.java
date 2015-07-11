import java.io.File;
import java.util.TreeSet;

/**
 * Created by gordoneliel on 3/14/15.
 */
public class HuffmanTree
{
    private static int CHAR_SIZE = 256;

    private String magicNumber = "HF";
    private String[] codes = new String[CHAR_SIZE];
    private int[] frequency = new int[CHAR_SIZE];
    private int numTree = 0;
    private int size = 0;

    private HuffmanNode treeRoot;


    //Arguements
    String infile = null, outfile = null;
    boolean compress = false;
    boolean verbose = false;
    boolean forceCompress = false;

    boolean isFeasibleToCompressFile = false;

    //The Textfile for reading
    TextFile textFile;

    public HuffmanTree(boolean compress, boolean verbose, boolean forceCompress, String infile, String outfile)
    {
        this.compress = compress;
        this.verbose = verbose;
        this.forceCompress = forceCompress;
        this.infile = infile;
        this.outfile = outfile;
        textFile = new TextFile(infile, 'r');

        // For compressing, else decompress
        if (compress)
        {
            countCharFrequencies();
            buildHuffmanTree();
            lookUpTable();
            compressFile();
        }
        else
        {
            decompressFile();
        }
    }

    /**
     * Builds the huffman tree from the character frequencies from
     * *countCharFrequencies*
     */
    public void buildHuffmanTree()
    {
        TreeSet<HuffmanNode> sortedTrees = new TreeSet<HuffmanNode>();
        int uncompressedFileSize = 0;

        for (int i = 0; i < frequency.length ; i++)
        {
            //Checks if the frequency is greater than Zero, then create huffman nodes
            if(frequency[i] > 0)
            {
                HuffmanNode node = new HuffmanNode((char)i, frequency[i]);
                if (verbose)
                {
                    System.out.println(i + "--> "+ node);
                }
                sortedTrees.add(node);

                //computes the size
                uncompressedFileSize  += frequency[i] * 8;
                //System.out.println(node);
            }
        }


        isFeasibleToCompressFile = isFeasibleToCompress(uncompressedFileSize, huffmanTreeSize(treeRoot));

        if(verbose)
        {
            System.out.println("Verbose Print: Sorted Huffman Nodes for Huffman Tree");
            System.out.println(sortedTrees);
        }

        buildHuffmanTreeFromData(sortedTrees);

    }


    /**
     * Helper method that constructs the huffman tree from huffman nodes
     * @param sortedTrees
     *                  - A treeset of huffman nodes to be constructed into a huffman tree
     */
    public void buildHuffmanTreeFromData(TreeSet<HuffmanNode> sortedTrees)
    {
        while(sortedTrees.size() > 1)
        {
            HuffmanNode lessThan = sortedTrees.first();
            HuffmanNode greaterThan = sortedTrees.higher(lessThan);
            sortedTrees.remove(lessThan);
            sortedTrees.remove(greaterThan);
            sortedTrees.add(new HuffmanNode(lessThan, greaterThan));
        }

        treeRoot = sortedTrees.first();

        if (verbose)
        {
            System.out.println("Verbose Print: Huffman Tree: ");
            System.out.println(treeRoot);
        }
    }
    /**
     * Helper method to calculate the size of the huffman tree
     * Size of the tree (1 bit for each internal node, 9 bits for each leaf
     * Basic computation of size of a tree recursively
     */
    public int huffmanTreeSize(HuffmanNode root)
    {
        if (treeRoot == null) return 0;
        //

        return  (treeRoot.getLeftChild() == null && treeRoot.getRightChild() == null) ?
                9 : 1 + huffmanTreeSize(root.getLeftChild()) + huffmanTreeSize(root.getRightChild());



    }


    public void lookUpTable()
    {
        lookUpTable("", treeRoot);
        if (verbose)
        {
            System.out.println("Verbose Print: Lookup Table: ");
            for (int i = 0; i < codes.length; i++)
            {
                if (codes[i] != null)
                {
                    System.out.println((char)i + "\t" + codes[i]);
                }

            }
        }

    }

    public void lookUpTable(String charCode, HuffmanNode currentNode)
    {
        if (currentNode == null) return; // Exit if null

        if (currentNode.getLeftChild() == null && currentNode.getRightChild() == null)
        {
            codes[currentNode.getCharacter()] = charCode;
            return;
        }

        String right = charCode + '1';
        String left = charCode + '0';

        lookUpTable(left, currentNode.getLeftChild());
        lookUpTable(right, currentNode.getRightChild());
    }
    /**
     * Counts the frequency of each character in a file and puts it in an array
     *
     */
    public void countCharFrequencies()
    {

        while(!textFile.EndOfFile())
        {
            char ch = textFile.readChar();
            System.out.println("Ch " + (int)ch);
            frequency[(int)ch]++;
        }


    }

    /**
     * Compresses the input file
     * Writes the compressed file to a new output file specified
     */
    public void compressFile()
    {
        File outputFile = new File(outfile);
        BinaryFile binaryFile = new BinaryFile(outfile, 'w');
        binaryFile.writeChar('H');
        binaryFile.writeChar('F');
        //Write the huffman tree to a file
        writeTreeToFile(treeRoot, binaryFile);

        textFile.rewind();

        if (isFeasibleToCompressFile)
        {
            while(!textFile.EndOfFile())
            {
                char c = textFile.readChar();
                String str = codes[(int)c];
                //System.out.println("Str " + str);
                if (str != null)
                {
                    for(int i = 0; i < str.length(); i++)
                    {
                        if (str.charAt(i) == '1')
                        {
                            binaryFile.writeBit(true);
                        }
                        else
                        {
                            binaryFile.writeBit(false);
                        }

                    }
                }

            }
        }
        else
        {
            System.err.println("Compressed file will be larger or same");
            System.exit(0);
        }

        binaryFile.close();
        textFile.close();
    }

    /**
     * Helper method that writes the huffman Tree to the specified output file
     */
    public void writeTreeToFile(HuffmanNode treeRoot, BinaryFile binFile)
    {
        //base cases
        if (treeRoot == null) return;

        if (treeRoot.getLeftChild() == null && treeRoot.getRightChild() == null)
        {
            binFile.writeBit(true);
            binFile.writeChar(treeRoot.getCharacter());
            return;
        }
        else
        {
            binFile.writeBit(false);
            writeTreeToFile(treeRoot.getLeftChild(), binFile);
            writeTreeToFile(treeRoot.getRightChild(), binFile);
        }

    }

    /**
     * Helper method to compute file size
     * The size of the uncompressed file (in bits):
     *     - # of characters in the input file) * 8
     * Size of the compressed file (in bits)
     *     Add up:
     *     - For each character c in the input file, (frequency of c) * size of the encoding for c
     *     - Size of the tree (1 bit for each internal node, 9 bits for each leaf
     *     - An extra 2 bytes (16 bits) for the magic number
     *     - An extra 4 bytes (32 bits) for header information used in the BinaryFile class
     * @param originalSize
     * @param treeSize
     */
    public boolean isFeasibleToCompress(int originalSize, int treeSize)
    {
        // Compress regardless if in force mode
        if (forceCompress)
        {
            return true;
        }

        int compressedFileSize = 16 + 32 + treeSize; // Account for 'HF' code, more in method description above
        for (int i = 0; i < codes.length; i++)
        {
            if (codes[i] != null)
            {
                compressedFileSize += frequency[i] * codes[i].length();
            }

        }

        if (compressedFileSize % 8 != 0)
        {
            int remainder = compressedFileSize % 8;
            compressedFileSize += remainder;
        }

        if (originalSize >= compressedFileSize)
        {
            return false;
        }
        else
        {
            return true;
        }

    }


    /**
     * Uncompresses files we have encoded
     * To read in the Huffman tree, we do a preorder traversal of the tree
     *  -- guided by the input file -- creating nodes as we go.
     */
    public void decompressFile()
    {
        File file = new File(outfile);
        BinaryFile binaryFile = new BinaryFile(infile, 'r');
        textFile = new TextFile(outfile, 'w');
        //System.out.println(canDecompress(binaryFile) );

        if (canDecompress(binaryFile) )
        {
            // Go ahead and decompress
            treeRoot = readInTreeForDecompression(binaryFile, textFile);

            while (!binaryFile.EndOfFile())
            {
                    decodeInputFromTree(treeRoot, textFile, binaryFile);

            }
        }
        else
        {
            System.err.println("Cannot uncompress file");
            System.exit(0);
        }
        textFile.close();
        binaryFile.close();

    }


    /**
     * Decode the input, using the Huffman tree
     * Start from the root of the tree, follow the appropriate child based on the next bit read in
     * from the input file until a leaf is reached, and then print out the character stored at that leaf.
     *
     */
    public void decodeInputFromTree(HuffmanNode node, TextFile textFile, BinaryFile binFile)
    {
        //if we have no children we are a leaf
        if (node.getLeftChild() == null || node.getRightChild() == null)
        {
            textFile.writeChar(node.getCharacter());
            return;
        }
        //temp
        if(binFile.readBit() == false)
        {
            decodeInputFromTree(node.getLeftChild(), textFile, binFile);
        }
        else
        {
            decodeInputFromTree(node.getRightChild(), textFile, binFile);
        }
    }

    /**
     * Reads in a tree bit by bit and reconstructs it for decompression
     */
    public HuffmanNode readInTreeForDecompression(BinaryFile binaryFile, TextFile textFile)
    {
        HuffmanNode newNode;

        if (binaryFile.readBit())
        {
            newNode = new HuffmanNode(binaryFile.readChar(), 0);
            return newNode;
        }
        else
        {
            return newNode = new HuffmanNode(readInTreeForDecompression(binaryFile, textFile), readInTreeForDecompression(binaryFile, textFile));
        }

    }


    /**
     * Validates our Huffman signature
     * @param binFile
     */
    public boolean canDecompress(BinaryFile binFile)
    {
        if (binFile.readChar() == 'H' && binFile.readChar() == 'F')
        {
            return true;
        }
        else
        {
            return false;
        }

    }


    public static void main(String[] args)
    {

    }


}

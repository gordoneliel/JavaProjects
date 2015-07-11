/**
 * Created by gordoneliel on 3/13/15.
 */
public class HuffmanNode implements Comparable<HuffmanNode>
{
    private HuffmanNode leftChild;
    private HuffmanNode rightChild;
    private int frequency;
    private char character;

    public HuffmanNode(char character, int frequency)
    {
        this.character = character;
        this.frequency = frequency;
    }

    public  HuffmanNode(HuffmanNode leftChild, HuffmanNode rightChild)
    {
        this.frequency = leftChild.frequency + rightChild.frequency;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }
    /**
     * Compares two Huffman nodes by character frequency
     * Allows for sorting by decending order  AAAaaaaaa
     * @param otherNode
     *                  - the node to be compared with
     * @return
     */
    public int compareTo(HuffmanNode otherNode)
    {
        int compare;

//        if (this.frequency.CompareTo(otherNode.frequency) >= 0) {
//            if (this.frequency.CompareTo(otherNode.frequency) == 0) {
//               return this.frequency - otherNode.frequency
//            }
//        }
//        else {
//
//        }


        if (this.frequency - otherNode.frequency == 0) {
            return (int)this.character - (int)otherNode.character;
        }
        return this.frequency - otherNode.frequency;


//        if (this.frequency == otherNode.frequency)
//        {
//             compare = 0;
//        }
//        else if(this.frequency > otherNode.frequency)
//        {
//            compare = 1;
//        }
//        else
//        {
//            compare = -1;
//        }
//
//        return compare;
    }

    // Getter and Setters
    public void setLeftChild(HuffmanNode leftChild)
    {
        this.leftChild = leftChild;
    }

    public void setRightChild(HuffmanNode rightChild)
    {
        this.rightChild = rightChild;
    }

    public void setFrequency(int frequency)
    {
        this.frequency = frequency;
    }

    public void setCharacter(char character)
    {
        this.character = character;
    }

    //Getters

    public char getCharacter()
    {
        return character;
    }

    public HuffmanNode getLeftChild()
    {
        return leftChild;
    }

    public HuffmanNode getRightChild()
    {
        return rightChild;
    }

    public String toString()
    {
        return "( " + "Char: " + character + " Freq: " + frequency + " )" +
                " \n" + "LeftChild: " + leftChild + " \n" + "RightChild: " + rightChild + "\n";
    }
}

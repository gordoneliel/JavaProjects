public class Main
{

    public static void main(String[] args)
    {
        boolean compress = false;
        boolean verbose = false;
        boolean forceCompress = false;
        String infile = null, outfile = null;

        if(args.length < 3)
        {
            System.err.println("No input arguements");
        }
        for (int i = 0; i < args.length ; i++)
        {
            // Infile and outfile
            if(i == args.length - 2)
            {
                infile = args[i];

            }
            if(i == args.length - 1)
            {
                outfile = args[i];
                break;
            }

            // All other flags
            if (args[i].equals("-c"))
            {
                compress = true;
            }
            else if(args[i].equals("-u"))
            {
                compress = false;
            }
            else if(args[i].equals("-v"))
            {
                verbose = true;
            }
            else if(args[i].equals("-f"))
            {
                forceCompress = true;
            }

        }


        // Constructs a huffmanTree maker with the command line arguments
        HuffmanTree huffmanTree = new HuffmanTree(compress, verbose, forceCompress, infile, outfile);

    }
}

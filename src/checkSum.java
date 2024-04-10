import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class checkSum {

    public static void main(String[] args) throws IOException {

        // Command line inputs
        String input = args[0];
        int bitSize = Integer.parseInt(args[1]);

        // Error if bitSize is invalid
        if(bitSize != 8 && bitSize != 16 && bitSize != 32){
            System.err.println("Valid checksum sizes are 8, 16, or 32");
            return;
        }

        // Read input into plnTxt and print it
        String plnTxt = readInput(input);
        plnTxt = pad(plnTxt, bitSize);
        print80(plnTxt);
        int length = plnTxt.length();

        // Evaluate checksum
        int sum = checkSum(plnTxt, bitSize);

        // Print final line, depending on the bitSize, we mod by a factor of 256 to eliminate the carries
        System.out.print(bitSize + " bit checksum is\t");
        if(bitSize == 8)
            System.out.format("%2x", sum%256);
        else if(bitSize == 16)
            System.out.format("%4x", sum%((int)Math.pow(256, 2)));
        else
            System.out.format("%8x", sum%((int)Math.pow(256, 4)));

        System.out.print(" for all " + length + " chars");
        System.out.println();
    }

    public static int checkSum(String plnTxt, int bitSize){

        int sum = 0;

        // Calculate the checkSum for each line at a time
        // chars are 8 bits long so for bitSize 8, we needed no shifting
        // for bitSize 16, we needed to shift the left most by 8 to fit a second char
        // for bitSize 32, we shifted the first 3 by 24, 16, and 8 respectively in order to fit 4 chars
        for(int i=0; i<plnTxt.length();){
            if(bitSize == 8){
                sum += plnTxt.charAt(i);
                i++;
            } else if(bitSize == 16){
                sum += (plnTxt.charAt(i) << 8) + plnTxt.charAt(i+1);
                i+=2;
            }else if(bitSize == 32){
                sum += (plnTxt.charAt(i) << 24) + (plnTxt.charAt(i+1) << 16) + (plnTxt.charAt(i+2) << 8) + plnTxt.charAt(i+3);
                i+=4;
            }

        }
        return sum;
    }

    // Reads input file and is modified to only contain the required input
    public static String readInput(String input) throws IOException {

        File inputFile = new File(input);
        BufferedReader file = new BufferedReader(new FileReader(inputFile));

        StringBuilder plnTxtBuild = new StringBuilder();

        String tempRead = file.readLine();
        while (tempRead != null && !tempRead.contains("checksum")){
            plnTxtBuild.append(tempRead);
            tempRead = file.readLine();
        }

        plnTxtBuild.append("\n");
        return plnTxtBuild.toString();
    }

    // Method to pad the plnTxt with X's
    public static String pad(String plnTxt, int bitSize){

        int remainder = plnTxt.length()%(bitSize/8);

        if(remainder < bitSize/8 && remainder != 0){
            remainder = bitSize/8 - remainder;
        }

        if(remainder != 0){
            plnTxt = plnTxt + "X".repeat(Math.max(0, remainder));
        }
        return plnTxt;
    }

    // Method that prints a text 80 characters at a time
    public static void print80(String text){
        int index=0;
        int count=0;

        System.out.println();
        while(count < 81 && index != text.length()){
            System.out.print(text.charAt(index));
            index++;
            count++;
            if(count == 80){
                System.out.println();
                count=0;
            }
        }
        System.out.println();
    }
}
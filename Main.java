/**
 * @author Yusuf Anıl Yazıcı 2021400207
 * 
 * Main.java - reads commands from an input file to construct and manipulate a family tree.
 * Outputs results to a specified file.
 * Commands include adding/removing members, targeting members, and rank analysis.
 * */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        FamilyTree familyTree = new FamilyTree(); // Create a new FamilyTree.

        // Define some strings for file reading and writing purposes.
        String line;
        String inputFile;
        String outputFile;

        // Assign values according to the given parameters,
        // if none is given it assumes input.txt and output.txt are default values.
        try {
            inputFile = args[0];
            outputFile = args[1];
        } catch (Exception e) {
            inputFile = "input.txt";
            outputFile = "output.txt";
        }


        // Read the specified input file and assign first line as boss.
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            if ((line = br.readLine()) != null) {
                String[] bossData = line.split(" ");
                familyTree.insert(bossData[0], Double.parseDouble(bossData[1]));
            }

            // Read remaining lines and split them by " " to access elements.
            while ((line = br.readLine()) != null) {

                String[] parts = line.split(" ");
                String command = parts[0]; // Command is the first word in the line specifying the command comes from service.

                switch (command) {

                    case "MEMBER_IN": // If command is MEMBER_IN, insert a new member to the familytree.
                        String nameIn = parts[1]; // Take the second word as the SURNAME_NAME of the member.
                        double gmsIn = Double.parseDouble(parts[2]); // Take the third word as the GMS of the member.
                        familyTree.insert(nameIn, gmsIn); // Insert according to the given SURNAME_NAME and GMS.
                        break;

                    case "MEMBER_OUT": // If command is MEMBER_OUT, remove the specified member from the familytree.
                        String nameOut = parts[1]; // Take the second word as the SURNAME_NAME of the member.
                        double gmsOut = Double.parseDouble(parts[2]);// Take the third word as the GMS of the member.
                        familyTree.remove(nameOut, gmsOut); // Remove according to the given SURNAME_NAME and GMS.
                        break;

                    case "INTEL_TARGET": // If command is INTEL_TARGET, find a target according to specified members.
                        String nameTarget1 = parts[1]; // Take second word as the SURNAME_NAME of the first member.
                        String nameTarget2 = parts[3]; // Take 4th word as the SURNAME_NAME of the second member.
                        familyTree.intelTarget(nameTarget1, nameTarget2); // Find a target member from the specified members.
                        break;

                    case "INTEL_DIVIDE": // If command is INTEL_DIVIDE, find the maximum number of independent members to divide the family.
                        familyTree.divideFamily();
                        break;

                    case "INTEL_RANK": // If command is INTEL_RANK, find all the members that have the same rank as the specified member.
                        String nameRank = parts[1]; // Take the second word as the SURNAME_NAME of the specified member.
                        familyTree.getSameRanks(nameRank); // Get all of the same ranked members.
                        break;

                    default:
                        System.out.println("Unknown command: " + command); // In case of non-specified commands, print this.
                        break;
                }
            }

            // Write the output to the specified output file.
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
                for (String outputLine : familyTree.getOutput()) {
                    bw.write(outputLine);
                    bw.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

package org.example;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Must provide an .asm file as an argument");
            System.exit(1);
        }

        File asmFile = new File("src/" + args[0].trim());
        if (!asmFile.exists()) {
            System.out.println("Source assembly file could not be found");
            System.out.println(asmFile.getAbsolutePath());
            System.exit(1);
        }

        String sourceDirectory = asmFile.toPath().getParent().toString();
        String outputFilePath = sourceDirectory + "/" + asmFile.getName().replace(".asm", ".hack");
        File binaryFile = new File(outputFilePath);

        System.out.println("Reading source file:    " + asmFile.getAbsolutePath());
        System.out.println("Writing to target file: " + binaryFile.getAbsolutePath());

        try {
            if (binaryFile.exists()) {
                binaryFile.delete();
            }
            binaryFile.createNewFile();

            Assembler assembler = new Assembler(asmFile, binaryFile);
            assembler.translate();

            System.out.println("Successfully assembled and wrote to target file");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
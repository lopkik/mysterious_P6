package org.example;

import java.io.*;

public class Assembler {
    private final File asmFile;
    private final BufferedWriter binaryWriter;
    private final Code binaryEncoder;
    private final SymbolTable symbolTable;

    public Assembler(File source, File target) throws IOException {
        this.asmFile = source;
        this.binaryWriter = new BufferedWriter(new FileWriter(target));

        this.binaryEncoder = new Code();
        this.symbolTable = new SymbolTable();
    }

    public void translate() throws IOException {
        this.buildSymbolTable();
        this.writeToBinaryFile();
    }

    private void buildSymbolTable() throws IOException {
        Parser parser = new Parser(this.asmFile);
        while (parser.hasMoreCommands()) {
            parser.advance();

            if (parser.commandType() == CommandType.L_COMMAND) {
                String symbol = parser.symbol();
                int address = this.symbolTable.getInstructionAddress();
                this.symbolTable.addEntry(symbol, address);
            } else {
                this.symbolTable.incrementInstructionAddress();
            }
        }
        parser.close();
    }

    private void writeToBinaryFile() throws IOException {
        Parser parser = new Parser(this.asmFile);
        while (parser.hasMoreCommands()) {
            parser.advance();
            CommandType commandType = parser.commandType();
            String instruction = null;

            switch(commandType) {
                case L_COMMAND:
                    continue;
                case A_COMMAND:
                    String symbol = parser.symbol();
                    String value = getAInstructionValue(symbol);

                    instruction = this.getBinaryAInstruction(value);
                    break;
                case C_COMMAND:
                    instruction = this.getBinaryCInstruction(parser.comp(), parser.dest(), parser.jump());
                    break;
            }

            this.binaryWriter.write(instruction);
            if (parser.hasMoreCommands()) {
                this.binaryWriter.newLine();
            }
        }

        parser.close();
        this.binaryWriter.flush();
        this.binaryWriter.close();
    }

    private String getAInstructionValue(String symbol) {
        String value;
        boolean isNonNumeric = (!Character.isDigit(symbol.charAt(0)));

        if (isNonNumeric) {
            if (!this.symbolTable.contains(symbol)) {
                int ramAddress = this.symbolTable.getRamAddress();
                this.symbolTable.addEntry(symbol, ramAddress);
                this.symbolTable.incrementRamAddress();
            }

            value = this.symbolTable.getAddress(symbol) + "";
        } else {
            value = symbol;
        }

        return value;
    }

    private String getBinaryAInstruction(String value) {
        int integerValue = Integer.parseInt(value);
        String binaryString = Integer.toBinaryString(integerValue);
        String binaryString15Bit = String.format("%15s", binaryString).replace(" ", "0");
        return "0" + binaryString15Bit;
    }

    private String getBinaryCInstruction(String comp, String dest, String jump) {
        StringWriter instruction = new StringWriter();
        instruction.append("111");
        instruction.append(this.binaryEncoder.comp(comp));
        instruction.append(this.binaryEncoder.dest(dest));
        instruction.append(this.binaryEncoder.jump(jump));
        return instruction.toString();
    }
}

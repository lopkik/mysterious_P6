package org.example;

import java.util.HashMap;

public class SymbolTable {
    private int ramAddress;
    private int instructionAddress;
    private final HashMap<String, Integer> symbolTable;

    public SymbolTable() {
        ramAddress = 16;
        instructionAddress = 0;
        symbolTable = new HashMap<>() {{
            put("SP", 0);
            put("LCL", 1);
            put("ARG", 2);
            put("THIS", 3);
            put("THAT", 4);
            put("R0", 0);
            put("R1", 1);
            put("R2", 2);
            put("R3", 3);
            put("R4", 4);
            put("R5", 5);
            put("R6", 6);
            put("R7", 7);
            put("R8", 8);
            put("R9", 9);
            put("R10", 10);
            put("R11", 11);
            put("R12", 12);
            put("R13", 13);
            put("R14", 14);
            put("R15", 15);
            put("SCREEN", 16384);
            put("KBD", 24576);
        }};
    }

    public void addEntry(String symbol, int address) {
        symbolTable.put(symbol, address);
    }

    public boolean contains(String symbol) {
        return symbolTable.containsKey(symbol);
    }

    public int getAddress(String symbol) {
        return symbolTable.get(symbol);
    }

    public int getInstructionAddress() {
        return this.instructionAddress;
    }

    public void incrementInstructionAddress() {
        this.instructionAddress++;
    }

    public int getRamAddress() {
        return this.ramAddress;
    }

    public void incrementRamAddress() {
        this.ramAddress++;
    }
}

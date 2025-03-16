package org.example;

import java.io.*;

public class Parser {
    private BufferedReader reader;
    private String currLine;
    private String nextLine;

    public Parser(File asmFile) throws IOException {
        this.reader = new BufferedReader(new FileReader(asmFile));
        this.currLine = null;
        this.nextLine = this.getNextLine();
    }

    private String getNextLine() throws IOException {
        String nextLine = this.reader.readLine();
        if (nextLine == null) return null;

        while (nextLine.trim().isEmpty() || nextLine.trim().startsWith("//")) {
            nextLine = this.reader.readLine();
            if (nextLine == null) return null;
        }

        int commentIndex = nextLine.indexOf("//");
        if (commentIndex != -1) {
            nextLine = nextLine.substring(0, commentIndex).trim();
        }
        return nextLine.trim();
    }

    public boolean hasMoreCommands() {
        return this.nextLine != null;
    }

    public void advance() throws IOException {
        if (this.hasMoreCommands()) {
            this.currLine = this.nextLine;
            this.nextLine = this.getNextLine();
        }
    }

    public CommandType commandType() {
        if (currLine.startsWith("(") && currLine.endsWith(")")) {
            return CommandType.L_COMMAND;
        } else if (currLine.startsWith("@")) {
            return CommandType.A_COMMAND;
        } else {
            return CommandType.C_COMMAND;
        }
    }

    public String symbol() {
        CommandType currLineCommandType = commandType();
        if (currLineCommandType == CommandType.A_COMMAND) {
            return currLine.substring(1);
        } else if (currLineCommandType == CommandType.L_COMMAND) {
            return currLine.substring(1, currLine.length() - 1);
        } else {
            return null;
        }
    }

    public String dest() {
        if (commandType() != CommandType.C_COMMAND) return null;

        int assignmentIndex = currLine.indexOf("=");
        if (assignmentIndex == -1) return null;
        return currLine.substring(0, assignmentIndex);
    }

    public String comp() {
        String comp = this.currLine;
        if (commandType() != CommandType.C_COMMAND) return null;

        int assignmentIndex = currLine.indexOf("=");
        if (assignmentIndex != -1) {
            comp = comp.substring(assignmentIndex + 1);
        }

        int semicolonIndex = currLine.indexOf(";");
        if (semicolonIndex != -1) {
            comp = comp.substring(0, semicolonIndex);
        }
        return comp;
    }

    public String jump() {
        if (commandType() != CommandType.C_COMMAND) return null;

        int semicolonIndex = currLine.indexOf(";");
        if (semicolonIndex != -1) {
            return currLine.substring(semicolonIndex + 1);
        } else {
            return null;
        }
    }

    public void close() throws IOException {
        this.reader.close();
    }
}

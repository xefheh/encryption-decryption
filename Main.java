import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.io.File;

public class Main {

    private static boolean isUNICODE = false;
    private static boolean isEncrypt = true;
    public static final List<String> ALPHABET = List.of("abcdefghijklmnopqrstuvwxyz".split(""));
    public static final List<String> ALPHABET_CAPITAL = List.of("ABCDEFGHIJKLMNOPQRSTUVWXYZ".split(""));

    public static void main(String[] args) {
        String inPath = "";
        String outPath = "";
        String input = "";
        int shift = 0;

        for(int i = 0; i < args.length; i++) {
            if(args[i].equals("-mode")) {
                isEncrypt = args[i + 1].equals("enc");
                i++;
            } else if(args[i].equals("-key")) {
                shift = Integer.parseInt(args[i + 1]);
                i++;
            } else if(args[i].equals("-in") && input.equals("")) {
                inPath = args[i + 1];
                i++;
            } else if(args[i].equals("-data")) {
                input = args[i + 1];
                i++;
            } else if(args[i].equals("-out")) {
                outPath = args[i + 1];
                i++;
            } else if(args[i].equals("-alg")) {
                isUNICODE = args[i + 1].equals("unicode");
                i++;
            }
        }

        String result = (inPath.equals("")) ?
                    (isEncrypt) ?
                            getEncrypt(input, shift) :
                            getDecrypt(input, shift) :
                    (isEncrypt) ?
                            encryptFromFile(inPath, shift) :
                            decryptFromFile(inPath, shift);

        if(outPath.equals("")) {
            System.out.println(result);
        } else {
            writeInFile(outPath, result);
        }
    }


    public static String getEncrypt(String input, int shift) {
        return (isUNICODE) ? encryptUNICODE(input, shift) :
                encryptShift(input, shift);
    }

    public static String getDecrypt(String input, int shift) {
        return (isUNICODE) ? decryptUNICODE(input, shift) :
                decryptShift(input, shift);
    }

    public static void writeInFile(String path, String output) {
        try (FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.write(output);
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    public static String encryptFromFile(String path, int shift) {
        try(Scanner scanner = new Scanner(new File(path))) {
            if(scanner.hasNext()) {
                String input = scanner.nextLine();
                return getEncrypt(input, shift);
            } else {
                return "";
            }
        } catch (FileNotFoundException e) {
            return "Error";
        }
    }

    public static String decryptFromFile(String path, int shift) {
        try (Scanner scanner = new Scanner(new File(path))) {
            if(scanner.hasNext()) {
                String input = scanner.nextLine();
                return getDecrypt(input, shift);
            } else {
                return "";
            }
        } catch (FileNotFoundException e) {
            return "Error";
        }
    }

    public static String encryptUNICODE(String input, int shift) {
        char[] result = new char[input.length()];
        for(int i = 0; i < result.length; i++) {
            result[i] = (char)(input.charAt(i) + shift);
        }
        return String.valueOf(result);
    }

    public static String decryptUNICODE(String input, int shift) {
        char[] result = new char[input.length()];
        for(int i = 0; i < result.length; i++) {
            result[i] = (char)(input.charAt(i) - shift);
        }
        return String.valueOf(result);
    }

    public static String encryptShift(String input, int shift) {
        char[] result = new char[input.length()];
        for(int i = 0; i < result.length; i++) {
            char currentChar = input.charAt(i);
            if(Character.isAlphabetic(currentChar)) {
                if(Character.isLowerCase(currentChar)) {
                    int index = ALPHABET.indexOf(String.valueOf(currentChar));
                    result[i] = (index + shift < ALPHABET.size()) ?
                            ALPHABET.get(index + shift).toCharArray()[0] :
                            ALPHABET.get(index + shift - ALPHABET.size()).toCharArray()[0];
                } else {
                    int index = ALPHABET_CAPITAL.indexOf(String.valueOf(currentChar));
                    result[i] = (index + shift < ALPHABET_CAPITAL.size()) ?
                            ALPHABET_CAPITAL.get(index + shift).toCharArray()[0] :
                            ALPHABET_CAPITAL.get(index + shift - ALPHABET_CAPITAL.size() - 1).toCharArray()[0];
                }
            } else {
                result[i] = currentChar;
            }
        }
        return String.valueOf(result);
    }

    public static String decryptShift(String input, int shift) {
        char[] result = new char[input.length()];
        for(int i = 0; i < result.length; i++) {
            char currentChar = input.charAt(i);
            if(Character.isAlphabetic(currentChar)) {
                if(Character.isLowerCase(currentChar)) {
                    int index = ALPHABET.indexOf(String.valueOf(currentChar));
                    System.out.println(index);
                    result[i] = (index - shift >= 0) ?
                            ALPHABET.get(index - shift).toCharArray()[0] :
                            ALPHABET.get(index - shift + ALPHABET.size()).toCharArray()[0];
                } else {
                    int index = ALPHABET_CAPITAL.indexOf(String.valueOf(currentChar));
                    result[i] = (index - shift >= 0) ?
                            ALPHABET_CAPITAL.get(index - shift).toCharArray()[0] :
                            ALPHABET_CAPITAL.get(index - shift + ALPHABET_CAPITAL.size()).toCharArray()[0];
                }
            } else {
                result[i] = currentChar;
            }
        }
        return String.valueOf(result);
    }
}

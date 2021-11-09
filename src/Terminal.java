/**
 * Christina Montasser Saad     20190380
 * Karim Mostafa Abd El Monaem  20190388
 * Mirette Amin Danial          20190570
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;


class Parser {
    String commandName;
    String[] args;

    //This method will divide the input into commandName and args
    // where "input" is the string command entered by the user
    public boolean parse(String input) {

        String[] temp = input.split(" ");
        commandName = temp[0];
        boolean flag = false;
        if (temp.length >= 2) {
            if (temp[1].equals("-r")) {
                commandName += " -r";
                flag = true;
            }
            if(flag && temp.length >= 3) {
                args = Arrays.copyOfRange(temp, 2, temp.length);
                return true;
            }
            else {
                args = Arrays.copyOfRange(temp, 1, temp.length);
                if(args ==  null)
                    return false;
                else
                    return true;
            }
        }
        else{
            return false;
        }
    }

    public String getCommandName(){
        return commandName;
    }
    public String[] getArgs(){
        return args;
    }
}

public class Terminal {
    Parser parser;
    public String[] echo(){
        return parser.getArgs();
    }

    public String pwd(){
        return System.getProperty("user.dir");
    }

    public void cd(String[] args){
        if(args == null){
            System.setProperty("user.dir",System.getProperty("user.home"));
            System.out.println(System.getProperty("user.dir"));
        }
        else if(args.length == 1){
            if(args[0].equals("..")){
                System.out.println(System.getProperty("user.dir"));
                Path temp = Paths.get(System.getProperty("user.dir"));
                System.setProperty("user.dir", String.valueOf(temp.getParent()));
                System.out.println(System.getProperty("user.dir"));
            }
            else if(Files.isDirectory(Paths.get(args[0]))) {
                System.out.println(System.getProperty("user.dir"));
                System.setProperty("user.dir", args[0]);
                System.out.println(System.getProperty("user.dir"));
            }
            else
                System.out.println("Wrong Argument, You should enter no arguments or .. or a full path or the relative (short) path");
        }
    } // ... //This method will choose the suitable command method to be called

    public void ls(){
        try {
            Files.list(Paths.get(System.getProperty("user.dir"))).sorted().forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ls(){
        try {
            List temp = new List(Files.list(Paths.get(System.getProperty("user.dir"))).sorted());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void chooseCommandAction(){
        switch (parser.getCommandName()){
            case "echo":
                echo();
                break;
            case "pwd":
                pwd();
                break;
            case "cd":
                cd(parser.getArgs());
                break;
            case "ls":
                ls();
                break;
            case "ls -r":
                break;
            case "mkdir":
                break;
            case "rmdir":
                break;
            case "touch":
                break;
            case "cp":
                break;
            case "cp -r":
                break;
            case "rm":
                break;
            case "cat":
                break;
            case ">":
                break;
            case ">>":
                break;
            default:
                System.out.println("Error: Command not found or invalid parameters are entered!");
                break;
        }
    }

    public static void main(String[] args){
        Terminal t = new Terminal();
        Scanner scan = new Scanner(System.in);
        t.parser = new Parser();
        System.out.print(">");
        t.parser.parse(scan.nextLine());
        t.chooseCommandAction();
    }
}

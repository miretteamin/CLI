/**
 * Christina Montasser Saad     20190380
 * Karim Mostafa Abd ElMonem    20190388
 * Mirette Amin Danial          20190570
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

class Parser {
    String commandName;
    String[] args;

    //This method will divide the input into commandName and args
    // where "input" is the string command entered by the user

    public boolean parse(String input) {
        //Split the input according to space
        input = input.trim();
        String[] temp = input.split(" ");
        //Take the command part from the split input 
        commandName = temp[0];
        boolean flag = false;
        //checking whether there are arguments after command part or not 
        if (temp.length >= 2) {
            //command with two parts
            if (temp[1].equals("-r")) {
                commandName += " -r";
                flag = true;
            }
            //it's a command with two parts, so take the other for agrs
            if (flag && temp.length >= 3) {
                args = Arrays.copyOfRange(temp, 2, temp.length);
                return true;
            } else {
                args = Arrays.copyOfRange(temp, 1, temp.length);
                if (args == null)
                    return false;
                else
                    return true;
            }
        }
        //No arguments
        else {
            return false;
        }
    }

    public String getCommandName() {
        return commandName;
    }
    public String[] getArgs() {
        return args;
    }
}

public class Terminal {
    Parser parser;
    //Prints the arguments
    public void echo(String[] args) {
        if(args == null)
        {
            System.out.println("echo should take an argument to print it out.");
        }
        else
            System.out.println(args[0]);

    }
    //prints the current path
    public String pwd() {
        System.out.println(System.getProperty("user.dir"));
        return System.getProperty("user.dir");
    }

    public void cd(String[] args) {
        if (args == null) {
            System.setProperty("user.dir", System.getProperty("user.home"));
            System.out.println(System.getProperty("user.dir"));
        } else if (args.length == 1) {
            if (args[0].equals("..")) {
                Path temp = Paths.get(System.getProperty("user.dir"));
                System.setProperty("user.dir", String.valueOf(temp.getParent()));
                System.out.println(System.getProperty("user.dir"));
            }
            //QUESTION HERE
            else if (Files.isDirectory(Paths.get(args[0]))) {
                System.setProperty("user.dir", args[0]);
                System.out.println(System.getProperty("user.dir"));
            } else
                System.out.println("Wrong Argument, You should enter no arguments or .. or a full path or the relative (short) path");
        }
    }

    //The current directory sorted alphabetically.
    public void ls() {
        try {
            Files.list(Paths.get(System.getProperty("user.dir"))).sorted().forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    //By Christina
    public void ls_r() {
        File[] directories = new File(System.getProperty("user.dir")).listFiles();
        for (int i = directories.length - 1; i >= 0; i--)
            System.out.println(directories[i]);
    }

    /*Takes 1 or more arguments and creates a directory for each argument. Each argument can be:
     *  • Directory name (in this case the new directory is created in the current directory)
    • Path (full/short) that ends with a directory name (in this case the new directory is created in the given path*/
    public void mkdir(String[] args) throws IOException {
        if (args == null) {
            System.out.println("Wrong Argument, You should enter one or more argument to create a directory to them");
        } else {
            String sl = "\\";
            //It's a path
            if (args[0].contains(sl)) {
            	//Cut that path to names of files to find what's not exists to be created
                String[] files = args[0].split("\\\\");
				/*
				 * for (int j = 0; j < files.length; j++) { System.out.println(files[j]); }
				 */
                File f = new File(files[0] + "\\" + files[1]);
               // System.out.println(f);
                for (int j = 2; j <= files.length; j++) {
                    if (!f.exists()) {
                        Files.createDirectory(Paths.get(f.getPath()));
                    }
                    if (j != files.length)
                        f = new File(f.getPath() + "\\" + files[j]);
                }
            } else {
                for (int i = 0; i < args.length; i++) {
                    //Short or full path case2
                    Files.createDirectory(Paths.get(System.getProperty("user.dir")+(args[i])));
                }
            }

        }
    }

    /*rmdir takes 1 argument which is ï¿½*ï¿½ (e.g. rmdir *) and removes all the empty directories 
     * in the current directory. 
     * 2. rmdir takes 1 argument which is either the full path or 
     * the relative (short) path and removes the given directory only if it is empty. */
    public void rmdir(String[] args) {
        if (args.length == 1) {
            if (args[0].contentEquals("*")) {
                File[] directories = new File(System.getProperty("user.dir")).listFiles(File::isDirectory);
                for (int i = 0; i < directories.length; i++) {
                    if (!directories[i].toString().contains("System Volume Information")) {
                        File[] contained = new File(directories[i].toURI()).listFiles();
                        if (contained.length == 0)
                            directories[i].delete();

                    }
                }
            } else if (Files.exists(Paths.get(args[0]))) {
                File[] directories = new File(args[0]).listFiles(File::isDirectory);

                if (directories.length == 0) {
                    File fi = new File(args[0]);
                    fi.delete();
                }
            }
            //cd then file name
            else if (Files.exists(Paths.get(System.getProperty("user.dir")+args[0]))) {
            	
                File[] directories = new File(System.getProperty("user.dir")+args[0]).listFiles(File::isDirectory);

                if (directories.length == 0) {
                    File fi = new File(System.getProperty("user.dir")+args[0]);
                    fi.delete();
                }
            }
            else {
                System.out.println("NOExists");
            }

        } else {
            System.out.println("Enter ONE argument");
        }
    }

    public void touch(String[] args) {
        if (args.length == 1) {
            try {
                String FolderExists = args[0].substring(0, args[0].lastIndexOf("\\"));
                if (!Files.exists((Paths.get(FolderExists)))) {
                    String[] NonExists = FolderExists.split(" ");
                    // System.out.println("NOFile");
                    mkdir(NonExists);
                }
                FileWriter wantedFile = new FileWriter(args[0]);
                wantedFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Wrong Argument, You should enter one argument");
        }

    }


    public void rm(String[] args) {
        File toBeDeletedFile = new File(System.getProperty("user.dir") + "\\" + args[0]);
        if (!toBeDeletedFile.delete()) {
            System.out.println("File Doesn't Exist");
        }

    }

    public void cat(String[] args) throws IOException {
        if (args == null) {
            System.out.println("Wrong number of arguments");
        } else if (args.length == 1) {

            String firstToBeRead = System.getProperty("user.dir") + "\\" + args[0];
            File check = new File(firstToBeRead);
            if (check.exists()) {
                String fileOne = new String(Files.readAllBytes(Paths.get(firstToBeRead)));
                System.out.println(fileOne);
            } else {
                System.out.println("This file doesn't exist");
            }

        } else {
            String firstToBeRead = System.getProperty("user.dir") + "\\" + args[0];
            String secondToBeRead = System.getProperty("user.dir") + "\\" + args[1];
            File check_1 = new File(firstToBeRead);
            File check_2 = new File(secondToBeRead);
            if (check_1.exists() && check_2.exists()) {
                String fileOne = new String(Files.readAllBytes(Paths.get(firstToBeRead)));
                System.out.println(fileOne);
                String fileTwo = new String(Files.readAllBytes(Paths.get(secondToBeRead)));
                System.out.println(fileTwo);
            } else {
                System.out.println("One of your files doesn't exist");
            }
        }
    }

    public void cp(String[] args) {
        if (args == null || args.length == 1) {
            System.out.println("Wrong number of arguments");
        } else {
            String sourceFilePath = System.getProperty("user.dir") + "\\" + args[0];
            String dstFilePath = System.getProperty("user.dir") + "\\" + args[1];
            try (BufferedReader bufferedLines = new BufferedReader(new FileReader(sourceFilePath))) {
                String fileLines = bufferedLines.readLine();
                BufferedWriter writeLines = new BufferedWriter(new FileWriter(dstFilePath, true));
                while (fileLines != null) {

                    writeLines.append(fileLines);
                    writeLines.append('\n');
                    fileLines = bufferedLines.readLine();
                }
                writeLines.close();
            } catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

        }
    }
    public void cp_r(String[] args) {
        if (args == null || args.length == 1 || args.length >= 3) {
            System.out.println("Wrong number of arguments");
        } else {
        	 String firstToBeRead = System.getProperty("user.dir") + "\\" + args[0];
             String secondToBeRead = System.getProperty("user.dir") + "\\" + args[1];
             File check_1 = new File(firstToBeRead);
             File check_2 = new File(secondToBeRead);
             createAndOrCopyDirectory(check_1, check_2);
             File src = new File(check_1, "f1");
             System.out.println(src);

        }
    }
    public void createAndOrCopyDirectory(File srcDir, File dstDir) {
    	if(!dstDir.exists())
        {
    		dstDir.mkdir();
    	}
		int counter = srcDir.list().length;
		int i = 0;
		while(i < counter)
		{
			checkDirChildren(new File(srcDir, srcDir.list()[i]), new File(dstDir, srcDir.list()[i]));
			i++;
		}
    }
    
    public void checkDirChildren(File src, File dst)
    {
    	if(src.isDirectory())
    	{
    		createAndOrCopyDirectory(src, dst);
    	}
    	else
    	{
    		copyFile(src, dst);
    	}
    }
    public void copyFile(File src, File dst)
    {
    	
    	try (BufferedReader bufferedLines = new BufferedReader(new FileReader(src))) {
            String fileLines = bufferedLines.readLine();
            dst.createNewFile();

            BufferedWriter writeLines = new BufferedWriter(new FileWriter(dst, true));
            while (fileLines != null) {

                writeLines.append(fileLines);
                writeLines.append('\n');
                fileLines = bufferedLines.readLine();
            }
            writeLines.close();

        } catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    //C:\Users\CompuStore\OneDrive\Documents\GitHub\CLI
    //This method will choose the suitable command method to be called
    public void chooseCommandAction() {
        switch (parser.getCommandName()) {
            case "echo":
                echo(parser.getArgs());
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
            case "ls-r":
                ls_r();
                break;
            case "mkdir":
                try {
                    mkdir(parser.getArgs());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "rmdir":
                rmdir(parser.getArgs());
                break;
            case "touch":
                touch(parser.getArgs());
                break;
            case "cp":
            	cp(parser.getArgs());
                break;
            case "cp-r":
                cp_r(parser.getArgs());
                break;
            case "rm":
                rm(parser.getArgs());
                break;
            case "cat":
                try {
                    cat(parser.getArgs());
                } catch (IOException e) {
                    e.printStackTrace();
                } //user.dir
                break;
            case "exit":
                System.exit(0);
                break;
            default:
                System.out.println("Error: Command not found or invalid parameters are entered!");
                break;
        }
    }

    public static void main(String[] args) {
        Terminal t = new Terminal();
        while (true) {
            Scanner scan = new Scanner(System.in);
            t.parser = new Parser();
            System.out.print('>');
            t.parser.parse(scan.nextLine());
            t.chooseCommandAction();
        }
    }
}
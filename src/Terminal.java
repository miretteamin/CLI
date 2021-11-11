/**
 * Christina Montasser Saad     20190380
 * Karim Mostafa Abd El Monaem  20190388
 * Mirette Amin Danial          20190570
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;




class Parser {
    String commandName;
    String[] args;

    //This method will divide the input into commandName and args
    // where "input" is the string command entered by the user
    public boolean parse(String input) {
    	//Split the input according to space 
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
        //No arguments
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
    //Prints the arguments
    public String[] echo(){
        return parser.getArgs();
    }
    //prints the current path
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
            //QUESTION HERE
            else if(Files.isDirectory(Paths.get(args[0]))) {
                System.setProperty("user.dir", args[0]);
                System.out.println(System.getProperty("user.dir"));
            }
            else
                System.out.println("Wrong Argument, You should enter no arguments or .. or a full path or the relative (short) path");
        }
    } 

	//The current directory sorted alphabetically.
	  public void ls(){ 
		  try {
			  Files.list(Paths.get(System.getProperty("user.dir"))).sorted().forEach(System.out::println); 
			 } 
		  catch (IOException e) { 
			  e.printStackTrace(); 
			  } 
		 }
	 
	
			  public void ls_r(){ 
				  Stream<Path> paths;
				  try {
					paths =  Files.list(Paths.get(System.getProperty("user.dir")));
					paths.forEach(System.out::println);
			//		Collections.reverse(paths);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*
				 * List mylist = new ArrayList<String>();
				 * Paths.get(System.getProperty("user.dir")).forEach(mylist::add);
				 * Collections.reverse(mylist);
				 */
			  }
			
	 
	  public void mkdir(String[] args) throws IOException
	  {
		  if(args==null)
		  {
			  System.out.println("Wrong Argument, You should enter one or more argument to create a directory to them");
		  }
		  else
		  {		  
			  String sl = "\\";
			if(args[0].contains(sl))
				{
					 String[] files = args[0].split("\\\\"); 
					 for(int j=0; j<files.length; j++) {
						 System.out.println(files[j]);
					 }
					 File f = new File(files[0]+"\\"+files[1]);
					 System.out.println(f);
					 for(int j=2; j<=files.length; j++) { 
						 if(!f.exists()) { 
							Files.createDirectory(Paths.get(f.getPath()));
						 }
						 if(j!=files.length)
							 f = new File(f.getPath()+ "\\" +files[j]);
						}  
				 }
			  else
			  {
				  for(int i=0; i<args.length; i++)
				  {
					  //Short or full path case2
					  Files.createDirectory(Paths.get((args[i]))); 
				  }
			  }

		  }
	  }
	  public void rmdir ()
	  {
		  
	  }
	  public void touch(String[] args)
	  {
		  if(args.length==1)
		  {
			  try {
				 // String FolderExists = args[0].substring(0, args[0].lastIndexOf("\\"));
				  //mkdir(FolderExists);
				  FileWriter wantedFile = new FileWriter(args[0]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		  else
		  {
			  System.out.println("Wrong Argument, You should enter one argument");
		  }

	  }
	  
	  public void rm(String [] args) 
	  {
		  File toBeDeletedFile = new File(pwd()+ "\\" + args[0]);
		  if(!toBeDeletedFile.delete())
		  {
			  System.out.println("File Doesn't Exist");
		  }
		  
	  }
	  
	  //C:\Users\CompuStore\OneDrive\Documents\GitHub\CLI
	//This method will choose the suitable command method to be called
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
            case "ls-r":
            	ls_r();
                break;
            case "mkdir":
			try {
				mkdir(parser.getArgs());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
                break;
            case "rmdir":
                break;
            case "touch":
            	touch(parser.getArgs());
                break;
            case "cp":
                break;
            case "cp -r":
                break;
            case "rm":
            	rm(parser.getArgs());
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
        while(true)
        { 
        	Scanner scan = new Scanner(System.in);
        	t.parser = new Parser();
        	System.out.print('>');
        	t.parser.parse(scan.nextLine());
        	t.chooseCommandAction();
        }
        
        
        /*try (Scanner scan = new Scanner(System.in)) {
			t.parser = new Parser();
			System.out.print(">");
			t.parser.parse(scan.nextLine());
		}
        t.chooseCommandAction();*/
    }
}

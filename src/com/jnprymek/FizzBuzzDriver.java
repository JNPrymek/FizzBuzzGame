package com.jnprymek;

import java.util.Scanner;
import java.util.HashMap;
import java.util.Set;

public class FizzBuzzDriver 
{
	private Scanner Scan;
	private FizzBuzzRules Rules;
	
	public FizzBuzzDriver()
	{
		Scan = new Scanner(System.in);
		Rules = new FizzBuzzRules();
	}
	
	public static void main(String[] args)
	{
		//Create instance of Driver and run game
		FizzBuzzDriver FBD = new FizzBuzzDriver();
		FBD.runFizzBuzzGame();
	}
	
	public void runFizzBuzzGame()
	{
		// greet player
		printMainControls();
		while(true) //quit method bypasses end of loop
		{
			String command = requestUserInput("Enter a command");
			processUserInput(command);
		}
	}
	

	//print command list for the main menu
	private void printMainControls()
	{
		System.out.println("\nFizz Buzz Game Driver.\n");
		System.out.println("To see information about the rules, type 'rules'");
		System.out.println("To start a game, type 'start'");
		System.out.println("To show controls (this list), type 'help'");
		System.out.println("To show special number mappings, type 'mappings'");
		System.out.println("To add special number mappings, type 'mappings add'");
		System.out.println("To remove special number mappings, type 'mappings remove'");
		System.out.println("To change COM difficulty, type 'difficulty'");
		System.out.println("To exit program, type 'exit'");
		System.out.println();
	}
	
	//print prompt & get user's response
	private String requestUserInput(String prompt)
	{
		System.out.print(prompt + " : ");
		return (Scan.nextLine());
	}
	
	//parse user response & execute commands
	private boolean processUserInput(String inputText)
	{
		String command = inputText.trim().toLowerCase();
		switch (command)
		{
		// Exit program
		case "exit":
		case "quit":
		case "q":
		case "e":
		case "-q":
		case "-e":
			quitGame();
			break;
		
		// go into rules of the game (fizz buzz)
		case "rules":
		case "how to play":
		case "-r":
		case "--rules":
			printClassicGameRules();
			break;
		
		//Display mappings of special numbers
		case "mappings":
		case "mappings list":
		case "map list":
		case "map":
		case "mapping":
		case "special numbers":
		case "numbers":
		case "-m":
		case "-m -l":
		case "-ml":
			printNumberMapping();
			break;
			
		//add number mappings to the rules
		case "mapping add":
		case "mappings add":
		case "-m -a":
		case "-m-a":
		case "-ma":
		case "--mapadd":
		case "mapadd":
		case "map add":
			addNumberMapping();
			break;
			
		// remove number mappings from the rules
		case "mapping remove":
		case "mappings remove":
		case "-m -r":
		case "-m-r":
		case "-mr":
		case "--mapremove":
		case "mapremove":
		case "map remove":
		case "map rem":
		case "maprem":
		case "--maprem":
		case "mapping rem":
		case "mappings rem":
			removeNumberMapping();
			break;	
			
		// start playing a game of fizz buzz using the current rule mappings
		case "start":
		case "start game":
		case "play game":
		case "pg":
		case "-pg":
		case "play":
			playFizzBuzz();
			break;
						
		// help & show command list
		case "help":
		case "?":
		case "-h":
		case "-help":
		case "--help":
			printMainControls();
			break;
			
		case "difficulty":
		case "level":
		case "dif":
		case "-d":
		case "d":
			setDifficulty();
			break;
			
		case "":
			//do nothing.  Main loop will re-prompt
			break;
		//something went wrong
		default:
			System.out.println("Command '" + inputText + "' not recognized.");
			return false;
		}
		return true;
	}
	
	//Play a game against the computer
	private void playFizzBuzz()
	{
		System.out.println("Begining new game of FizzBuzz.");
		System.out.println("Starting at one (1) and counting upwards, enter a number\n or the word that should replace it.");
		
		int turnIndex = 1;
		boolean isHumanTurn = true;
		boolean shouldContinueGame = true;
		
		long turnTime = Rules.getTurnTime();
		boolean isEasyMode = (Rules.getDifficulty() == Difficulty.EASY);
		
		UserInputUnit UInput = new UserInputUnit();
		
		Rules.reCompute();
		
		while (shouldContinueGame)
		{
			UInput.SetInputExpired(false);
			printFBPrompt(isHumanTurn, turnIndex, isEasyMode);
			String correctAnswer = Rules.getAnswer(turnIndex);
			if (isHumanTurn)
			{
				//get timed input
				InputTimeout ITimeOut = new InputTimeout(UInput, turnTime);
				ITimeOut.start();
				UInput.SetInputString(Scan.nextLine());
				ITimeOut.CancelTimeout();
				if (UInput.isExpired())
				{
					System.out.println("Sorry!  You took too long to respond.  CPU wins.");
					shouldContinueGame = false;
				}
				else
				{
					//sanitize input
					String input = UInput.GetInputString().toLowerCase().trim();
					//handle escape command
					if (isEscapeString(input))
					{
						shouldContinueGame = false;
						System.out.println("Forfeiting Fizz Buzz.");
					}
					else // handle guess
					{
						
						if (!correctAnswer.equalsIgnoreCase(input))
						{
							//wrong answer
							shouldContinueGame = false;
							System.out.println("Sorry.  The correct answer is : " + correctAnswer);
							System.out.println("Exiting to Fizz Buzz Driver Main Menu.");
						}
					}
				}
			}
			else // is COM's turn
			{
				String ComAnswer = Rules.generateCOMMove(turnIndex);
				System.out.println(ComAnswer);
				if (!ComAnswer.equalsIgnoreCase(correctAnswer))
				{
					System.out.println("COM has guessed wrong.  Correct answer was '" + correctAnswer + "'.");
					System.out.println("Congratulations!  You Win!");
					shouldContinueGame = false;
				}
			}
			
			//Turn transition
			turnIndex += 1;
			isHumanTurn = !isHumanTurn;
		}// End Loop Game Round
		
		System.out.println("Fizz Buzz Game ending.");
		
	}
	
	//print prompt at each round of Fizz Buzz Game
	private void printFBPrompt(boolean isHuman, int index, boolean isEasy)
	{
		String p = "CPU";
		if (isHuman)
		{
			p = "PLA";
		}
		if (isEasy)
		{
			System.out.print(p + " (" + index + ") : ");
		}
		else
		{
			System.out.print(p + " : ");
		}
	}
	
	//Set Difficulty level of the AI
	private void setDifficulty()
	{
		System.out.println("Current difficulty is " + Rules.getDifficulty().toString());
		System.out.println("Enter a difficulty level.");
		String rawInput = Scan.nextLine().toLowerCase().trim();
		switch (rawInput)
		{
		case "easy":
		case "e":
			Rules.setDifficulty(Difficulty.EASY);
			break;
		case "medium":
		case "med":
		case "m":
			Rules.setDifficulty(Difficulty.MEDIUM);
			break;
		case "hard":
		case "h":
			Rules.setDifficulty(Difficulty.HARD);
			break;
		case "expert":
		case "x":
			Rules.setDifficulty(Difficulty.EXPERT);
			break;
			default:
				System.out.println("I'm sorry.  That difficulty was not recognized.");
				break;
		}
		System.out.println("Difficulty set to " + Rules.getDifficulty().toString());
	}
	
	//Map a new special word to a number, or change existing
	private void addNumberMapping()
	{
		System.out.println("Enter the special number to add to the list.");
		System.out.println("Numbers must be positive integers.  1 is not recommended.");
		System.out.println("If the number is already a special number, the mapping will be replaced.");
		String tempIn = Scan.nextLine();
		try
		{
			int intIn = Integer.parseInt(tempIn);
			System.out.println("Enter the word that will substitute for " + intIn + " and its multiples.");
			tempIn = "";
			tempIn = Scan.nextLine();
			if (tempIn.length() > 0)
			{
				tempIn = tempIn.toLowerCase();
				if (!isEscapeString(tempIn))
				{
					Rules.addMapping(intIn, tempIn);
				}
				else
				{
					System.out.println("I'm sorry.  That is a reserved word.");
				}
			}
			else
			{
				System.out.println("I'm sorry.  That did not appear to be a valid word.");
			}
		}
		catch (NumberFormatException nfe)
		{
			System.out.println("I'm sorry.  That did not appear to be a positive integer.");
		}
	}
	
	//are you trying to quit in the middle of the game?
	private boolean isEscapeString(String s)
	{
		boolean status = false;
		status = status || s.equalsIgnoreCase("quit");
		status = status || s.equalsIgnoreCase("-q");
		status = status || s.equalsIgnoreCase("forfeit");
		status = status || s.equalsIgnoreCase("-f");
		return status;
	}
	
	//Remove special word mapping for a specified number
	private void removeNumberMapping()
	{
		System.out.println("Enter the special number to remove from the list.");
		String tempIn = Scan.nextLine();
		try
		{
			int intIn = Integer.parseInt(tempIn);
			String oldText = Rules.GetMapping(intIn);
			if (oldText == null)
			{
				System.out.println("No mapping was found for " + intIn);
				return;
			}
			System.out.println("Are you sure you want to remove " + intIn + "? Y/n");
			tempIn = "";
			tempIn = Scan.nextLine();
			switch (tempIn.toLowerCase())
			{
			case "":
			case "y":
			case "yes":
				
				Rules.removeMapping(intIn);
				System.out.println("Rule '" + intIn + " = " + oldText + "' has been removed.");
				break;
			default:
				System.out.println("Rule '" + intIn + " = " + oldText + "' has NOT been removed.");
				break;
			}
		}
		catch (NumberFormatException nfe)
		{
			System.out.println("I'm sorry.  That did not appear to be a positive integer.");
		}
	}
	
	//Display which numbers are mapped to words
	private void printNumberMapping()
	{
		HashMap<Integer, String> map = Rules.GetMappings();
		if (map.size() == 0)
		{
			System.out.println("There are no mappings for special numbers yet.");
			return;
		}
		Set<Integer> keys = map.keySet();
		for (Integer k : keys)
		{
			System.out.println(k.toString() + " = " + map.get(k));
		}
	}
	
	//Display explanation of Fizz Buzz Rules
	private void printClassicGameRules()
	{
		System.out.println("Fizz Buzz Rules");
		System.out.println("Enter numbers in order, starting from 1.\n  Ex: 1, 2, 3.  One entry per line.");
		System.out.println("For the special numbers and their multiples,\n replace the number with the mapped word.");
		System.out.println("If a number is a multiple of more than one special number,\n concatenate the mapped words in order from least to greatest.");
		
		System.out.println("Example Mapping\n  3 = Fizz, 5 = Buzz\n Answers would be:");
		System.out.println("1, 2, Fizz, 4, Buzz, Fizz, 7, ... 14, FizzBuzz");
		System.out.println("All words are case-insensitive.");
	}
	
	//Exit application
	private void quitGame()
	{
		if (Scan != null)
		{
			Scan.close();
		}
		System.exit(0);
	}

}

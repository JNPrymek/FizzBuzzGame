package com.jnprymek;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.Random;

import java.util.Arrays;

enum Difficulty
{
	EASY, //display index number on prompt
	MEDIUM, //hide index number
	HARD, //COM will not miss basic fizz buzz words (single factors)
	EXPERT; //COM will not miss any answers
}

public class FizzBuzzRules 
{
	//Difficulty Options
		// there is a 1 in X chance COM will forget each factor
		int easyRnd = 10;
		int mediumRnd = 20;
		int hardRnd = 30;
		//expert does not get wrong answers
		
		//Turn Times
		private long easyTurnTime = (20 * 1000);
		private long mediumTurnTime = (15 * 1000);
		private long hardTurnTime = (10 * 1000);
		private long expertTurnTime = (7 * 1000);
		
		
		
		// ------------------------------------------------
		private HashMap<Integer, String> Mappings;
		private String[] Answers;
		private boolean answersComputed;
		private Difficulty Diff;
		private boolean bIsHumanFirst;
		
		
		// Default Constructor
		public FizzBuzzRules()
		{
			this(null);
		}
		
		public FizzBuzzRules(HashMap<Integer, String> templateMap)
		{
			if (templateMap != null)
			{
				Mappings = new HashMap<Integer, String>(templateMap);
			}
			else
			{
				Mappings = new HashMap<Integer, String>();
			}

			Answers = new String[0];
			answersComputed = false;
			Diff = Difficulty.EASY;
			bIsHumanFirst = true;
			
			//set classic values
			Mappings.put(Integer.valueOf(3), "fizz");
			Mappings.put(Integer.valueOf(5), "buzz");
		}
		
		public String generateCOMMove(int index)
		{
			String result = "";
			Random rnd = new Random();
			
			// Expert will never miss an answer
			if (Diff == Difficulty.EXPERT)
			{
				return getAnswer(index);
			}
			
			Set<Integer> KeySet = Mappings.keySet();
			ArrayList<Integer> Keys = new ArrayList<Integer>();
			for (Integer key : KeySet)
			{
				if (index % key.intValue() == 0) //this is a factor of a special word value
				{
					Keys.add(key);
				}
			}
			Collections.sort(Keys); //make sure we check in proper order
			int keyIndex = 0;
			int missRange = 0;
			// HARD will not miss unless there are multiple factors
			if (Diff == Difficulty.HARD)
			{
				if (Keys.size() < 2)
					return getAnswer(index);
				keyIndex = 1;
			}
			
			if (Diff == Difficulty.EASY) {missRange = easyRnd;}
			if (Diff == Difficulty.MEDIUM) {missRange = mediumRnd;}
			if (Diff == Difficulty.HARD) {missRange = hardRnd;}
			
			for (int i = keyIndex; i < Keys.size(); i++)
			{
				//if not the 1 in X chance of forgetting
				if (rnd.nextInt(missRange) != 0)
				{
					// add keyword to result
					result = result.concat(Mappings.get(Keys.get(i)));
				}
			}
			
			//if there is no special word, say the number
			//if (Keys.size() == 0)
			if (result.length() == 0)
			{
				result = String.valueOf(index);
			}
			
			return result;
		}
		
		public long getTurnTime()
		{
			long result = easyTurnTime;
			switch (Diff)
			{
			case EASY: //throws warnings if missing an option
				result = easyTurnTime;
				break;
			case MEDIUM:
				result = mediumTurnTime;
				break;
			case HARD:
				result = hardTurnTime;
				break;
			case EXPERT:
				result = expertTurnTime;
				break;
			}
			return result;
		}
		
		public void setDifficulty(Difficulty newDiff)
		{
			Diff = newDiff;
		}
		
		public Difficulty getDifficulty()
		{
			return Diff;
		}
		
		//set whether player goes first
		public void setPlayerFirst(boolean pFirst)
		{
			bIsHumanFirst = pFirst;
		}
		
		public boolean isHumanFirst()
		{
			return bIsHumanFirst;
		}
		
		//Remove a word rule from the list & return the word removed
		public String removeMapping(int number)
		{
			String result = Mappings.get(Integer.valueOf(number));
			if (result != null)
			{
				//System.out.println("Map retrieved " + result + " as value for " + number + ".");
				Mappings.remove(Integer.valueOf(number));
				answersComputed = false;
			}
			else
			{
				System.out.println("ERROR");
			}
			
			return result;
		}
		//Add a word rule to the list
		public void addMapping(int number, String word)
		{
			Integer numObj = Integer.valueOf(number);
			if ( Mappings.containsKey(numObj))
			{
				if (Mappings.get(numObj).equals(word))
				{
					// mapping is already in list & no alterations needed.
					return;
				}
				// overwrite with new value.  Will need to recompute answers
				Mappings.replace(numObj, word);
			}
			else
			{
				//add new key value pair
				Mappings.put(numObj, word);  //should also be able to overwrite, but there was a more explicit method.
			}
			// mark that answers are now incorrect (potentially)
			answersComputed = false;
		}
		
		public String getMappingString()
		{
			return Mappings.toString();
		}
		
		public HashMap<Integer, String> GetMappings()
		{
			//returns shallow copy for read only access.  Clone() returns Object class.
			HashMap<Integer, String> result = new HashMap<Integer, String>(Mappings);
			return result;
		}
		
		public String GetMapping(int key)
		{
			return Mappings.get(key);
		}
		
		public String[] getAnswers(int range)
		{
			if (range >= Answers.length)
			{
				computeAnswers(range);
			}
			return Arrays.copyOfRange(Answers, 0, range + 1);
		}
		
		public String getAnswer(int number)
		{
			if (number >= Answers.length)
			{
				computeAnswers(number);
			}
			return Answers[number];
		}
		
		private void computeAnswers(int maxNum)
		{
			int rangeBegin = Answers.length;
			if (!answersComputed)
			{
				rangeBegin = 0;
			}
			
			int rangeEnd = Math.max(maxNum, Answers.length - 1);
			
			if (rangeEnd >= Answers.length)
			{
				// Re-size answer array
				String[] newAnswers = new String[(rangeEnd + 1)];
				for (int i = 0; i < Answers.length; i++)
				{
					newAnswers[i] = Answers[i];
				}
				Answers = newAnswers; // make the answers array larger capacity
			}
			
			
			/// Get keys from mappings and sort them into ascending order
			Set<Integer> KeysSet = Mappings.keySet();
			ArrayList<Integer> Keys = new ArrayList<Integer>(KeysSet);
			Collections.sort(Keys);
			
			for (int i = rangeBegin; i <= rangeEnd; i++)
			{
				if (i == 0) {continue;} // index 0 not used for this program
				
				boolean needNum = true;
				String answer = "";
				for (Integer key : Keys)
				{
					// check if i is multiple of key
					if (i % key.intValue() == 0)
					{
						needNum = false;
						// concatenate string from mapping to current answer
						answer = answer.concat(Mappings.get(key));
					}
				}
				
				if (needNum) // only print number if no words were printed
				{
					answer = String.valueOf(i);
				}
				
				// add this answer to the list
				Answers[i] = answer;
				//System.out.println("DEBUG: Calculating answer [" + i + "] : " + answer);
			}
			
			answersComputed = true;
		}
		
		public void reCompute()
		{
			answersComputed = false;
			computeAnswers(20);
		}
}

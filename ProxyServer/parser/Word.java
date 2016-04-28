package parser;

import java.util.ArrayList;

public class Word {
	/*
	 * Instance Variable Declarations  
	 */
	private String word;
	private ArrayList<String> wordDefintions;
	
	/*
	 * Constructor 
	 * Word Object 
	 * 
	 */
	Word()
	{
		word = "";
		wordDefintions = new ArrayList<String>();
	}
	
	/*
	 * Get word
	 * @param: none
	 * @return: word(string)
	 */
	public String getWord()
	{
		return word;
	}
	
	/*
	 * Set word
	 * @param: new word
	 * @return: void
	 */
	public void setWord(String newWord)
	{
		word = newWord;
	}

}

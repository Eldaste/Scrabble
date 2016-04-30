package gameState;

public class GameState {
	/*
	 * Class Variables
	 */
	String gName;
	int gNumPlayers;
	int gameNum;

	/*
	 *  Ctor
	 */
	public GameState(String name, int numPlayers, int numID)
	{
		gName = name;
		gNumPlayers = numPlayers;
		gameNum = numID;
		
	}
	
	public int getGameNum() {
		return gameNum;
	}
	
	public String getgName() {
		return gName;
	}
	
	public int getgNumPlayers() {
		return gNumPlayers;
	}
	
	//Converts Name to an int array
	public int[] char2IntArray()
	{
		char[] tmp = gName.toCharArray();
		int[] newName = new int[tmp.length];
		
		for(int i = 0; i < tmp.length; i++)
		{
			newName[i] = (int)tmp[i];
		}
		
		return newName;
	}
}

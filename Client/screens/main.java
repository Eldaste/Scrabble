package screens;


import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import gameState.GameState;

import java.awt.GridLayout;
import java.util.Arrays;

import player.GenericUsernameError;
import player.Player;

public class main extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final int MAX_TILES = 7;
	final int BOARD_TILES = 15;
	private JPanel contentPane;
	private JPanel PlayerN;
	private JPanel PlayerW;
	private JPanel PlayerS;
	private JPanel PlayerE;
	
	Player myPlayer; //southPlayer
	Player wPlayer;
	Player nPlayer;
	Player ePlayer;
	
	/*
	 * Final Declarations
	 */
	static final Object[] gameOptions = {"Make Game", "Join Game"};
	static final Object[] playerOptions = {"2","3","4"};	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// initialize the board and start it running 
					main frame = new main();
					frame.pack();
					frame.setVisible(true);
					
					String name;
					while(true)
					{
					//Ask for user input 
				    name = JOptionPane.showInputDialog(frame, "What's your name?");

				    if(name != null)
				    	break;
				   }
					
					//create the player
				    Player myPlayer = new Player(name);
				    
				   //Custom button text
				    
				    int n = JOptionPane.showOptionDialog(frame,"Which would you like?","Game Setup",
						    									JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,
						    									null,gameOptions,gameOptions[1]);
				    System.out.println(n);
				    System.out.println(myPlayer.getMyName());
				    
				    GameState myGame;
				    //case statement for the options  
				    switch(n)
				    {
				    case 0:
				    	//logic for making a game 
				    	int totPlayers = Integer.parseInt(howMany(frame));
				    	String GN = gameName(frame);
				    	myGame = myPlayer.makeNewGame(totPlayers,GN);	    	
				    	break;
//				    case 1:
//				    	//logic for joining game
//				    	myPlayer.joinNewGame();
//				    	break;
				    default:
				    	break;
				    }
				    
				    //you are in a game
				    
				   
				    
				    
				    //game logic
				    
					
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300,300,1800,1200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

	    PlayerN = new JPanel();
	    
	    JLabel northNumOfTiles = new JLabel("#ofTiles: " + MAX_TILES);
	    JLabel northScore = new JLabel("Score:" + 0);
	    JLabel northName = new JLabel("Name:" + "Default");
	    
	    PlayerN.add(northNumOfTiles);
	    PlayerN.add(northScore);
	    PlayerN.add(northName);

	    contentPane.add(PlayerN, BorderLayout.NORTH);

	    PlayerW = new JPanel();
	    PlayerW.setLayout(new BoxLayout(PlayerW, BoxLayout.Y_AXIS));
	    
	    JLabel westNumOfTiles = new JLabel("#ofTiles: " + MAX_TILES);
	    JLabel westScore = new JLabel("Score:" + 0);
	    JLabel westName = new JLabel("Name:" + "Default");
	    
	    PlayerW.add(westNumOfTiles);
	    PlayerW.add(westScore);
	    PlayerW.add(westName);

	  
	    contentPane.add(PlayerW, BorderLayout.WEST);
	   
	    PlayerE = new JPanel();
	    PlayerE.setLayout(new BoxLayout(PlayerE, BoxLayout.Y_AXIS));
	    
	    JLabel eastNumOfTiles = new JLabel("#ofTiles: " + MAX_TILES);
	    JLabel eastScore = new JLabel("Score:" + 0);
	    JLabel eastName = new JLabel("Name:" + "Default");
	    
	    PlayerE.add(eastNumOfTiles);
	    PlayerE.add(eastScore);
	    PlayerE.add(eastName);

	    
	    contentPane.add(PlayerE, BorderLayout.EAST);
	    

	    PlayerS = new JPanel();
	    PlayerS.setLayout(new BoxLayout(PlayerS, BoxLayout.Y_AXIS));
	    
	    JLabel southNumOfTiles = new JLabel("#ofTiles: " + MAX_TILES);
	    JLabel southScore = new JLabel("Score:" + 0);
	    JLabel southName = new JLabel("Name:" + "Default");
    
	    PlayerS.add(southNumOfTiles);
	    PlayerS.add(southScore);
	    PlayerS.add(southName);


	    contentPane.add(PlayerS, BorderLayout.SOUTH);
	    
	    JPanel tileArray = new JPanel();
	    tileArray.setLayout(new GridLayout(1,MAX_TILES));
	    
	    JButton [] myTiles = new JButton[MAX_TILES];
	    
	    for(int i = 0; i < MAX_TILES; i++)
	    {
	    	myTiles[i] = new JButton("Tile: " + i);
	    }
	    
	    for(int i = 0; i < MAX_TILES; i++)
	    {
	    	tileArray.add(myTiles[i]);
	    	myTiles[i].setVisible(true);
	    }
	    
	    PlayerS.add(tileArray);
	    
	    JPanel BOARD = new JPanel();
	    BOARD.setLayout(new GridLayout(BOARD_TILES,BOARD_TILES));
	    
	    JButton [][] myBoard = new JButton[BOARD_TILES][BOARD_TILES];
	    
	    for(int i = 0; i < BOARD_TILES; i++)
	    {
	    	for(int j = 0; j < BOARD_TILES; j++)
		    {
	    		myBoard[i][j] = new JButton("");
		    }
	    }
	    
	    for(int i = 0; i < BOARD_TILES; i++)
	    {
	    	for(int j = 0; j < BOARD_TILES; j++)
		    {
	    		if(Arrays.asList(0,7,14).contains(i) && (j % 7 == 0))
	    		{
	    			if((i == 7) && (j == 7))
	    			{
	    				myBoard[i][j].setText("***");
	    			}else
	    			{
	    				myBoard[i][j].setText("3x WS");
	    			}
	    			
	    			
	    		}
	    		
	    		if(Arrays.asList(1,5,9,13).contains(i) && (Arrays.asList(5,9).contains(j)))
	    		{
	    			myBoard[i][j].setText("3x L");
	    		}
	    		
	    		if(Arrays.asList(5,9).contains(i) && (Arrays.asList(1,13).contains(j)))
	    		{
	    			myBoard[i][j].setText("3x L");
	    		}
	    		
	    		if(Arrays.asList(0,7,14).contains(i) && (Arrays.asList(3,11).contains(j)))
	    		{
	    			myBoard[i][j].setText("2x L");
	    		}
	    		
	    		if(Arrays.asList(2,11).contains(i) && (Arrays.asList(6,8).contains(j)) )
	    		{
	    			myBoard[i][j].setText("2x L");
	    		}
	    		
	    		if(Arrays.asList(3,10).contains(i) && (Arrays.asList(0,7,14).contains(j))) 
	    		{
	    			myBoard[i][j].setText("2x L");
	    		}

	    		if(Arrays.asList(6,8).contains(i) && (Arrays.asList(2,6,8,12).contains(j))) 
	    		{
	    			myBoard[i][j].setText("2x L");
	    		}
	    		
	    		if(Arrays.asList(1,13).contains(i) && (Arrays.asList(1,13).contains(j))) 
	    		{
	    			myBoard[i][j].setText("2x W");
	    		}
	    		
	    		if(Arrays.asList(2,12).contains(i) && (Arrays.asList(2,12).contains(j))) 
	    		{
	    			myBoard[i][j].setText("2x W");
	    		}
	    		
	    		if(Arrays.asList(3,11).contains(i) && (Arrays.asList(3,11).contains(j))) 
	    		{
	    			myBoard[i][j].setText("2x W");
	    		}
	    		
	    		if(Arrays.asList(4,10).contains(i) && (Arrays.asList(4,10).contains(j))) 
	    		{
	    			myBoard[i][j].setText("2x W");
	    		}
	    		
		    }
	    }
	    
	    
	    for(int i = 0; i < BOARD_TILES; i++)
	    {
	    	for(int j = 0; j < BOARD_TILES; j++)
		    {
	    		BOARD.add(myBoard[i][j]);
		    }
	    }
	    
	    contentPane.add(BOARD, BorderLayout.CENTER);
	}
	
		
	public void deactivateAllButtons(JButton [][] myBoard)
	{
		for(int i = 0; i < BOARD_TILES; i++)
	    {
	    	for(int j = 0; j < BOARD_TILES; j++)
		    {
	    		myBoard[i][j].setEnabled(false);
		    }
	    }   
	}
	
	public void activateAllButtons(JButton [][] myBoard)
	{
		for(int i = 0; i < BOARD_TILES; i++)
	    {
	    	for(int j = 0; j < BOARD_TILES; j++)
		    {
	    		myBoard[i][j].setEnabled(true);
		    }
	    }   
	}
	
	public void deactivateTile(JButton [] myTiles)
	{
    	for(int i = 0; i < MAX_TILES; i++)
	    {
    		myTiles[i].setEnabled(false);
	    }
	}
	
	public void activateTile(JButton [] myTiles)
	{
    	for(int i = 0; i < MAX_TILES; i++)
	    {
    		myTiles[i].setEnabled(true);
	    }
	}
	
	//asks how many players and returns that number
	public static String howMany(JFrame parentFrame)
	{
		String n = (String) JOptionPane.showInputDialog(parentFrame,"How many players?","Game Setup",JOptionPane.QUESTION_MESSAGE, null, playerOptions, playerOptions[1]);
		return n;

	}
	
	//asks how many players and returns that number
	public static String gameName(JFrame parentFrame)
	{
		String GN = JOptionPane.showInputDialog(parentFrame, "What's your game name?");
		return GN;

	}
}

package screens;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;

public class main extends JFrame {
	
	final int MAX_TILES = 7;
	final int BOARD_TILES = 15;
	private JPanel contentPane;
	private JPanel PlayerN;
	private JPanel PlayerW;
	private JPanel PlayerS;
	private JPanel PlayerE;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					main frame = new main();
					frame.pack();
					frame.setVisible(true);
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

	    //We create a sub-panel. Notice, that we don't use any layout-manager,
	    //Because we want it to use the default FlowLayout
	    PlayerN = new JPanel();
	    
	    JLabel northNumOfTiles = new JLabel("#ofTiles: " + MAX_TILES);
	    JLabel northScore = new JLabel("Score:" + 0);
	    JLabel northName = new JLabel("Name:" + "Default");
	    
	    PlayerN.add(northNumOfTiles);
	    PlayerN.add(northScore);
	    PlayerN.add(northName);

	    //Now we simply add it to your main panel.
	    contentPane.add(PlayerN, BorderLayout.NORTH);
	    
	    //We create a sub-panel. Notice, that we don't use any layout-manager,
	    //Because we want it to use the default FlowLayout
	    PlayerW = new JPanel();
	    PlayerW.setLayout(new BoxLayout(PlayerW, BoxLayout.Y_AXIS));
	    
	    JLabel westNumOfTiles = new JLabel("#ofTiles: " + MAX_TILES);
	    JLabel westScore = new JLabel("Score:" + 0);
	    JLabel westName = new JLabel("Name:" + "Default");
	    
	    PlayerW.add(westNumOfTiles);
	    PlayerW.add(westScore);
	    PlayerW.add(westName);

	    //Now we simply add it to your main panel.
	    contentPane.add(PlayerW, BorderLayout.WEST);
	    
	    //We create a sub-panel. Notice, that we don't use any layout-manager,
	    //Because we want it to use the default FlowLayout
	    PlayerE = new JPanel();
	    PlayerE.setLayout(new BoxLayout(PlayerE, BoxLayout.Y_AXIS));
	    
	    JLabel eastNumOfTiles = new JLabel("#ofTiles: " + MAX_TILES);
	    JLabel eastScore = new JLabel("Score:" + 0);
	    JLabel eastName = new JLabel("Name:" + "Default");
	    
	    PlayerE.add(eastNumOfTiles);
	    PlayerE.add(eastScore);
	    PlayerE.add(eastName);

	    //Now we simply add it to your main panel.
	    contentPane.add(PlayerE, BorderLayout.EAST);
	    
	    //We create a sub-panel. Notice, that we don't use any layout-manager,
	    //Because we want it to use the default FlowLayout
	    PlayerS = new JPanel();
	    PlayerS.setLayout(new BoxLayout(PlayerS, BoxLayout.Y_AXIS));
	    
	    JLabel southNumOfTiles = new JLabel("#ofTiles: " + MAX_TILES);
	    JLabel southScore = new JLabel("Score:" + 0);
	    JLabel southName = new JLabel("Name:" + "Default");
    
	    PlayerS.add(southNumOfTiles);
	    PlayerS.add(southScore);
	    PlayerS.add(southName);

	    //Now we simply add it to your main panel.
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
	    
	    for(int i = 0; i < BOARD_TILES; i = i+7)
	    {
	    	for(int j = 0; j < BOARD_TILES; j = j+7)
		    {
	    		myBoard[i][j].setText("3x WS");
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

}

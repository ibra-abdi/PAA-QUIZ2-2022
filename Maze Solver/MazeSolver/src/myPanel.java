import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class myPanel extends JPanel implements ActionListener,ChangeListener{
	
	private final int mazeSize = 600;
	private int cellSize = 40;
	private int slowestDelay = 110;
	private int delay = 10;
	private int windowW,windowH;
	private int running = -1;

	private int switchCaseVar = 0;
	
	private myMaze maze;
	private Timer tm;
	
	
	private JPanel smallPanel;
	private myButton startButton;
	private myButton resetButton;
	private myButton reMazeButton;
	
	private JLabel cellLabel;
	private JSlider cellSlider;
	
	private JLabel speedLabel;
	private JSlider speedSlider;
	
	private JCheckBox mazeCheckbox;
	
	private JCheckBox BFSCheckbox;
	private JComboBox algoBox;
	private int mode ;
	private JLabel algoBoxLabel;
	
	
	private myButton startSolvingButton;
	private boolean flag = true;

	myPanel(int windowW,int windowH){
		this.windowW = windowW;
		this.windowH = windowH;
		
		tm = new Timer(delay,this);
		
		this.setPreferredSize(new Dimension(windowW,windowH));
		this.setBounds(0, 0, windowW, windowH);
		this.setBackground(new Color(200,200,200));
		this.setLayout(null);
		
		
		//right panel
		smallPanel = new JPanel();
		smallPanel.setPreferredSize(new Dimension((windowW-mazeSize),windowH));
		smallPanel.setBounds(mazeSize, 0, windowW-mazeSize, windowH);
		smallPanel.setLayout(null);
		this.add(smallPanel);
		
		
		//start button
		startButton = new myButton("Start",(int)(smallPanel.getSize().width/2)-120-30, 30, 120, 50,Color.PINK);
		smallPanel.add(startButton);
		startButton.addActionListener(this);
				
		//remaze button
		reMazeButton = new myButton("Re-Maze",(int)(smallPanel.getSize().width/2)+30, 30, 120, 50,Color.CYAN);
		smallPanel.add(reMazeButton);
		reMazeButton.addActionListener(this);
		reMazeButton.setEnabled(false);
		
		//cellslider and panel
		cellSlider = new JSlider(10,100,40);
		cellSlider.setBounds((windowW-mazeSize)/2-(350/2), 150, 350, 50);
		cellSlider.setPaintTicks(true);
		cellSlider.setMinorTickSpacing(5);
		cellSlider.setPaintTrack(true);
		cellSlider.setMajorTickSpacing(10);
		cellSlider.setSnapToTicks(true);
		cellSlider.setPaintLabels(true);
		cellSlider.addChangeListener(this);

		cellLabel = new JLabel();
		cellLabel.setText("Cell's size adjustment: " + cellSlider.getValue());
		cellLabel.setBounds(30, 130, 250, 20);
		cellLabel.setFont(new Font("",Font.BOLD,18));
		
		smallPanel.add(cellLabel);
		smallPanel.add(cellSlider);
		
		//speed and label
		speedSlider = new JSlider(1,5,5);
		speedSlider.setBounds((windowW-mazeSize)/2-(350/2), 230, 350, 40);
		speedSlider.setPaintTrack(true);
		speedSlider.setMajorTickSpacing(1);
		speedSlider.setSnapToTicks(true);
		speedSlider.setPaintLabels(true);
		speedSlider.addChangeListener(this);

		speedLabel = new JLabel();
		speedLabel.setText("Speed: " + speedSlider.getValue());
		speedLabel.setBounds(30, 210, 180, 20);
		speedLabel.setFont(new Font("",Font.BOLD,18));
				
		smallPanel.add(speedLabel);
		smallPanel.add(speedSlider);
		
		
		//generate Check box
		mazeCheckbox = new JCheckBox();

		mazeCheckbox.setFocusable(false);
	
		smallPanel.add(mazeCheckbox);
		
		algoBoxLabel = new JLabel("Pathfinding Algorithms");
		algoBoxLabel.setBounds(smallPanel.getSize().width/2-155, 370, 220, 30);
		algoBoxLabel.setFont(new Font("",Font.BOLD,15));
		
		smallPanel.add(algoBoxLabel);
		
		String[] algoList = {"Breadth First Search(BFS)","Depth First Search(DFS)"};// (0 == BFS , 1 = DFS)
		algoBox = new JComboBox(algoList);
		algoBox.setBounds(smallPanel.getSize().width/2-160, 400, 220, 30);
		algoBox.setFont(new Font("",Font.BOLD,15));
		algoBox.setFocusable(false);
		algoBox.addActionListener(this);
		//algoBox.setEnabled(false);
		
		mode = algoBox.getSelectedIndex(); // (0 == BFS , 1 = DFS)
		
		smallPanel.add(algoBox);
		
		//BFS button
		startSolvingButton = new myButton("Start",smallPanel.getSize().width/2-130-20, 300, 130, 60,new Color(255, 100, 100));
		smallPanel.add(startSolvingButton);
		startSolvingButton.addActionListener(this);
		startSolvingButton.setEnabled(false);
		
		//resetmaze button
		resetButton = new myButton("Reset Maze",smallPanel.getSize().width/2+20, 300, 130, 60,new Color(100, 255, 100));
		smallPanel.add(resetButton);
		resetButton.addActionListener(this);
		initMaze();
		tm.start();
	}
	
	private void initMaze() {
		maze = new myMaze(mazeSize,cellSize);	
	}

	public void paintComponent(Graphics g) {
		
		if(maze.checkFinished()) {
			reMazeButton.setEnabled(true);
			startSolvingButton.setEnabled(true);
			algoBox.setEnabled(true);
		}else {
			startSolvingButton.setEnabled(false);
			resetButton.setEnabled(false);
			algoBox.setEnabled(false);
		}
		
		super.paintComponent(g);
		maze.drawMaze(g);	
		if(!flag) {
			maze.drawPathFinder(g,mode);
			if(maze.finish) {
				mazeCheckbox.setEnabled(true);
				startSolvingButton.setEnabled(true);
				resetButton.setEnabled(true);
				tm.stop();
			}
		}
		
			maze.mazeAlgorithm(g);

	}
	
	private void reset() {
		tm.start();
		running = -1;	
		startButton.setEnabled(true);
		repaint();
	}
	
	@Override
	
	public void actionPerformed(ActionEvent e) {
		//create new maze
		if(e.getSource()==reMazeButton) {
			flag = true;
			mazeCheckbox.setEnabled(true);
			initMaze();
			reset();
			startButton.setText("Start");
		}
		//press start button 
		if (e.getSource()==startButton) {
			running *= -1;
			if(running == 1)
				startButton.setText("Pause");
			else
				startButton.setText("Start");
		}
		if(e.getSource()==algoBox) {
			mode = algoBox.getSelectedIndex();
		}
		//start solving
		if(e.getSource() == startSolvingButton) {
			mazeCheckbox.setSelected(false);
			mazeCheckbox.setEnabled(false);
			
			if(flag) {
				maze.initStartAndEnd();
				flag = false;
			}	
		}
		//reset maze button
		if(e.getSource()==resetButton) {
			flag = true;
			maze.resetMaze();
			tm.start();
		}
		if(running==1)
			repaint();
	}
	
	@Override
	
	public void stateChanged(ChangeEvent e) {
		//cellSlider
		if(e.getSource()==cellSlider) {
			if(cellSlider.getValue()%5==0) {
				mazeCheckbox.setEnabled(true);
				cellLabel.setText("Cell's size adjustment: " + cellSlider.getValue());
				cellSize = cellSlider.getValue();
				startButton.setText("Start");
				flag = true;
				initMaze();
				reset();
			}
		}
		//SpeedSlider
		if(e.getSource()==speedSlider) {
				speedLabel.setText("Speed: " + speedSlider.getValue());
				delay = slowestDelay - (speedSlider.getValue()-1)* ((slowestDelay-10)/4) ;
				tm.setDelay(delay);
				System.out.println(delay);
		}		
	}
}

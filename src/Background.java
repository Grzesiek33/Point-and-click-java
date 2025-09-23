import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Background {

	//varibles
	Image Background;
	BufferedImage BackgroundDist;
	Double[][] Distances = new Double[192][108];
	Color color;
	String name;
	String backgroundPath;
	
	int noiseRandomness;
	int darkness;
	int id;
	double gradient;
	boolean flashlightOn;
	
	ArrayList<Item> items = new ArrayList<Item>();
	ArrayList<Action> actions = new ArrayList<Action>();
	ArrayList<Passage> passages = new ArrayList<Passage>();
	ArrayList<Puzzle> puzzles = new ArrayList<Puzzle>();
	
	//story
	boolean storyOn=false;
	ArrayList<String> stories = new ArrayList<String>();
	int directionLeft=0, directionRight=0;
	Image arrows; String arrowPath="graphics/items/arrow.png";
	Rectangle arrowLeft = new Rectangle(50, 950, 100, 100), arrowRight = new Rectangle(1780, 950, 100, 100);
	
	public Background(String backgroundPath, String backgroundDistPath, int noiseRandomness, int darkness, double gradient, boolean flashlightOn,
				      ArrayList<Item> items, ArrayList<Action> actions, ArrayList<Passage> passages, ArrayList<Puzzle> puzzles) {
		try {
			BackgroundDist = ImageIO.read(new File(backgroundDistPath));
		} catch (IOException e) {e.printStackTrace();}
		
		try {
			Background = ImageIO.read(new File(backgroundPath.substring(0, backgroundPath.length()-4)+"_English.png"));
		} catch (IOException e) {
			try {
			Background = ImageIO.read(new File(backgroundPath));
			} catch (IOException e2) {}
		}
		
		this.backgroundPath=backgroundPath;
		this.noiseRandomness = noiseRandomness;
		this.darkness = darkness;
		this.gradient=gradient;
		this.flashlightOn = flashlightOn;
		
		this.name = backgroundPath.substring(21, backgroundPath.length()-4);
		
		for(Item i:items) {
		this.items.add(new Item(i.id, i.iconPath, i.iconEqPath, i.iconDistPath, i.x, i.y, i.width, i.height, i.data, i.allowDist, i.canBePicked,
				i.soundId, i.soundIdCant));
		}
		
		for(Action a:actions) {
			this.actions.add(new Action(a.data, a.id, a.iconPath, a.iconDistPath, a.icon2Path, a.icon2DistPath,
					         a.allowDist, a.x, a.y, a.width, a.height, a.x2, a.y2, a.width2, a.height2, a.enableItem, a.neededItem, a.idReq, a.idReqChange,
					         a.reversable, a.soundId, a.soundIdCant));
			}
		
		for(Passage i:passages) {
			this.passages.add(new Passage(i.data, i.id, i.x, i.y, i.width, i.height, i.where, i.idReq, i.soundId));
			}
		
		for(Puzzle i:puzzles) {
			this.puzzles.add(new Puzzle(i.data, i.id, i.x, i.y, i.width, i.height, i.firstColor, i.secondColor, i.endColor, i.length, i.grid, i.allowDist, i.neededItem,
							            i.idReq, i.idReqChange, i.soundIdAction, i.soundIdCant, i.soundIdDone, i.iconPath, i.iconDonePath, i.iconDistPath, i.iconZoomedPath));
			}
		
		for(int i=0; i<192; i++) {
			for(int j=0; j<108; j++) {
				color = new Color(BackgroundDist.getRGB(i, j), true);
				if(color.getRed()==color.getGreen()) Distances[i][j] = (double)(color.getRed());
				else Distances[i][j] = (double)((color.getRed()+color.getGreen()));
			}
		}
		
	}
	
	public void paintComponent(Graphics g2, ImageObserver io) {
		if(!storyOn) {
		for(Item i:items) {
			i.paintComponent(g2, io);
			}
		
		for(Puzzle i:puzzles) {
			if(!i.lunched)
			i.paintComponent(g2, io);
			}
		
		for(Action i:actions) {
			i.paintComponent(g2, io);
			}
		
		for(Puzzle i:puzzles) {
			if(i.lunched)
			i.paintComponent(g2, io);
			}
		} else {
			g2.setColor(color = new Color(20, 20, 20));
			g2.fillRect(0, 0, 1920, 1080);
			g2.setColor(Color.yellow);
			for(String s:stories) 
			g2.drawString(s, 10, 50+50*stories.indexOf(s));
			
			if(directionLeft>=0) g2.drawImage(arrows, 150, 950, -100, 100, io);
			if(directionRight>=0) g2.drawImage(arrows, 1780, 950, 100, 100, io);
		}
	}
	
	public void ChangeLanguage(String which) {
		for(Action a:actions) {
			a.ChangeLanguage(which);
		}
		
		for(Item a:items) {
			a.ChangeLanguage(which);
		}

		for(Puzzle a:puzzles) {
			a.ChangeLanguage(which);
		}

		for(Passage a:passages) {
			a.ChangeLanguage(which);
		}
		
		if(id>=0) {
		// File path is passed as parameter
        File file = new File("translations/"+which+"/plot/"+id+".txt");
 
        // Creating an object of BufferedReader class
        BufferedReader br;
        String st;
		try {
			stories.clear();
			br = new BufferedReader(new FileReader(file, Charset.forName("UTF-16")));
			st=br.readLine();
			
			while(st!=null) {
				char c=' ';
				if(st.length()>0) c = st.charAt(0);
				if(!Character.toString(c).equals("/"))
				stories.add(st);
				st=br.readLine();
			}
		} catch (IOException e) {e.printStackTrace();}
		
		try {
			Background = ImageIO.read(new File(backgroundPath.substring(0, backgroundPath.length()-4)+"_"+which+".png"));
		} catch (IOException e) {
			try {
			Background = ImageIO.read(new File(backgroundPath));
			} catch (IOException e2) {}
		}
		}
	}
	
	public void MakeStory(Data data, int id, int directionLeft, int directionRight) {
		this.directionLeft=directionLeft;
		this.directionRight=directionRight;
		darkness=0;
		noiseRandomness=1;
		gradient=0;
		storyOn=true;
		this.id=id;
		
		try {
			arrows = ImageIO.read(new File(arrowPath));
		} catch (IOException e) {e.printStackTrace();}
		
		if(id>=0) {
		
		// File path is passed as parameter
        File file = new File("translations/"+data.language+"/plot/"+id+".txt");
 
        // Creating an object of BufferedReader class
        BufferedReader br;
        String st;
		try {
			br = new BufferedReader(new FileReader(file, Charset.forName("UTF-16")));
			st=br.readLine();
			
			while(st!=null) {
				char c=' ';
				if(st.length()>0) c = st.charAt(0);
				if(!Character.toString(c).equals("/"))
				stories.add(st);
				st=br.readLine();
			}
		} catch (IOException e) {e.printStackTrace();}
		}
	}

}

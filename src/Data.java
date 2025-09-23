import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Data {
	
		//objects
		EndScreen endScreen = new EndScreen(7);
	
		//varibles
	
			Random rand = new Random();
			
			//ints
			int FPS=30;
			int currentBackground=0;
			long gameTime=0;
			String language="English";
			
			//boolean
			boolean flashlightOn=true, flashlightBroken=false;
			boolean fancyFlashlight=true;
			boolean writing=false;
			boolean developerMode=false;
			boolean freeWill=false;
			boolean puzzleOn=false;
			boolean holdingCable=false;
			boolean lightsOn=false;
			
			boolean[] reqs = new boolean[20];
			boolean[] reqsDev = new boolean[20];
			
				//Inventory
				boolean drawInventory=true;
				boolean openInventory=true;
				boolean itemInHand=false;
				int itemId=-1;
				int eqSize=80;
				int verDist=5;
				int horDist=5;
			
			//Backgrounds
			ArrayList<Background> backgrounds = new ArrayList<Background>();
		
			//Items, interactions
			ArrayList<Item> items = new ArrayList<Item>();
			ArrayList<Action> actions = new ArrayList<Action>();
			ArrayList<Passage> passages = new ArrayList<Passage>();
			ArrayList<Puzzle> puzzles = new ArrayList<Puzzle>();
			//colors: 0-black, 1-red, 2-green, 3-blue
			
		//mouse
		int xmouse, ymouse;
		Rectangle mouse = new Rectangle();
		Font customFont, customFontSmall;

		//Initializing the Toolkit
		Toolkit tkit=Toolkit.getDefaultToolkit();
		//Extractig the Images for the custom cursor
		Image imgCursor = tkit.getImage("graphics/cursors/cursor.png");
		Image imgHover = tkit.getImage("graphics/cursors/hover.png");
		//Creating two custom cursors
		Cursor cursor = tkit.createCustomCursor(imgCursor, new Point(15, 15), "Dupa");
		Cursor hover = tkit.createCustomCursor(imgHover, new Point(15, 15), "Naprawdê nie wiem co to robi");
		
		//noise
			//flashlight
		boolean lgbt=false, broken=true;
		int howLongBroken=0;
		int howOftenBreaks=100;
		int howOftenWorks=5;
		int homManyColors=5, whichColor=0;
		String[] color = new String[homManyColors];
		
		
		int fRt[]= new int[homManyColors], fGt[]= new int[homManyColors], fBt[]= new int[homManyColors];
		int fR=0, fG=0, fB=0;
		int flashNoiseRandomness=30;
		int flashRadious=30;
		int flashPower=500;
		double flashGradient=0.85;
			//inner circle
		int innerCirclehNoiseRandomness=30;
		int innerCircleRadiousMin=0;
		int innerCircleRadiousMax=15;
		int innerCirclePower=200;
		double innerCircleGradient=0.85;
		double shift=0.8;
			//vignette
		int noiseRandomness=30;
		int darkness=120;
		double gradient=0.2;
			//general
		int R=0, G=0, B=0;
		int defoultBright=20;
		int defoultDark=255;
		int defoultBrightRandomness=20;
		int defoultDarkRandomness=1;
		
		//Inforamtions
		int howManyInf=6;
		ArrayList<String> information = new ArrayList<String>();
		
		//Music
		SoundControler sd;
		int whatMusic=1, howManySongs;
		
		//Puzzle
		int[] firstColor = new int[5];
		int[] secondColor = new int[5];
		int[] endColor = new int[5];
		int[] length = new int[5];
		boolean[][] grid = new boolean[10][5];
	
	public Data(SoundControler sd) {
		
		//setting colors for light
		fRt[0]=0; fGt[0]=0; fBt[0]=0; color[0] = "white"; //0 - white
		fRt[1]=55; fGt[1]=55; fBt[1]=0; color[1] = "yellow"; //1 - yellow
		fRt[2]=155; fGt[2]=0; fBt[2]=0; color[2] = "red"; //2 - red
		fRt[3]=0; fGt[3]=155; fBt[3]=0; color[3] = "green"; //3 - green
		fRt[4]=0; fGt[4]=0; fBt[4]=255; color[4] = "blue"; //4 - blue
		
		this.sd = sd;
		howManySongs=sd.musics.size();
		
		ChangeMusic(whatMusic);
		
		for(int i=0; i<reqs.length; i++) {
			reqs[i]=false;
		}  
	    
	    // File path is passed as parameter
        File file = new File("translations/"+language+"/informations/informations.txt");
 
        // Creating an object of BufferedReader class
        BufferedReader br;
        String st;
		try {
			br = new BufferedReader(new FileReader(file, Charset.forName("UTF-16")));
		
			while((st = br.readLine()) != null) {
				information.add(st);
			} 
			
		} catch (IOException e) {e.printStackTrace();}
		
		
		//fonts
		try {
		    //create the font to use. Specify the size!
			customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/CoolPixelartRegular-DODp9.ttf")).deriveFont(40F);
			customFontSmall = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/CoolPixelartRegular-DODp9.ttf")).deriveFont(20F);

		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    //register the font
		    ge.registerFont(customFont);
		} catch (IOException e) {e.printStackTrace();} catch(FontFormatException e) {e.printStackTrace();}
		
		//Backgrounds
		
				//0 - car
				
				//Items
				items.clear();
				items.add(new Item(2, "graphics/items/flashlight_map.png", "graphics/items/flashlight_eq.png", "graphics/items/trans.png", 520, 570, 120, 70, this, false, true, 4, 5));
				
				items.add(new Item(3, "graphics/items/rock_map.png", "graphics/items/rock_eq.png", "graphics/items/trans.png", 290, 600, 80, 60, this, false, true, 4, 5));
				
				//Actions
				actions.clear();
				actions.add(new Action(this, 0, "graphics/interactions/trunk.png", "graphics/interactions/trans.png", "graphics/interactions/trunk_opened.png", "graphics/interactions/trans.png",
						true, 520, 570, 120, 70, 520, 570, 120, 70, -1, -1, -1, 1, true, 2, 3));
				
				actions.add(new Action(this,  1, "graphics/interactions/trans.png", "graphics/interactions/trans.png", "graphics/interactions/hood.png", "graphics/interactions/trans.png",
						true, 980, 550, 190, 110, 980, 550, 190, 110, -1, -1, -1, 1, true, 2, 3));
				
				actions.add(new Action(this,  7, "graphics/interactions/trans.png", "graphics/interactions/trans.png", "graphics/interactions/seatbelt_car.png", "graphics/interactions/trans.png",
						true, 770, 470, 200, 140, 770, 470, 200, 140, -1, 7, -1, 9, false, 14, 12));
				
				actions.add(new Action(this,  8, "graphics/interactions/trans.png", "graphics/interactions/trans.png", "graphics/interactions/trans.png", "graphics/interactions/trans.png",
						true, 640, 580, 40, 40, 640, 580, 40, 40, -1, 6, -1, 10, false, 15, 10));
				
				//Passages
				passages.clear();
				passages.add(new Passage(this, 0, 1290, 0, 310, 390, 1, 1, 1));
				passages.add(new Passage(this, 1, 1710, 720, 210, 360, 2, 1, 1));
				passages.add(new Passage(this, 2, 0, 750, 210, 330, 0, 0, 1));
				passages.add(new Passage(this, 3, 380, 00, 110, 640, 0, 0, 1));
				passages.add(new Passage(this, 4, 1460, 470, 80, 70, 0, 0, 1));
				passages.add(new Passage(this, 5, 1710, 580, 100, 70, 0, 0, 1));
				
				//Puzzles
				puzzles.clear();
				
					backgrounds.add(new Background("graphics/backgrounds/car.png", "graphics/backgrounds/car_dist.png", 30, 60, 0.2, false, items, actions, passages, puzzles) );
					
				//1 - shed
					
				//Items
				items.clear();
				items.add(new Item(1, "graphics/items/stick_map.png", "graphics/items/stick_eq.png", "graphics/items/trans.png", 470, 610, 60, 230, this, true, true, 4, 5));
				//Actions
				actions.clear();
				actions.add(new Action(this,  2, "graphics/interactions/shed_door.png", "graphics/interactions/shed_door_dist.png", "graphics/interactions/shed_door_opened.png", "graphics/interactions/shed_door_opened_dist.png",
						
						true, 730, 260, 230, 550, 520, 330, 460, 520, -1, 0, -1, 2, false, 6, 7));
				
				//Passages
				passages.clear();
				passages.add(new Passage(this, 6, 0, 700, 210, 480, 0, -1, 1));
				passages.add(new Passage(this, 7, 1710, 0, 210, 1080, 0, 0, 1));
				passages.add(new Passage(this, 8, 730, 270, 210, 530, 4, 2, 1));
				
				//Puzzles
				puzzles.clear();
				
					backgrounds.add(new Background("graphics/backgrounds/shed.png", "graphics/backgrounds/shed_dist.png", 30, 180, 0.2, true, items, actions, passages, puzzles));
					
				//2 - front gas
					
				//Items
				items.clear();
				items.add(new Item(6, "graphics/items/canister_full_map.png", "graphics/items/canister_eq.png", "graphics/items/trans.png", 440, 540, 230, 410, this, true, false, 4, 0));
				
				//Actions
				actions.clear();
				actions.add(new Action(this,  3, "graphics/interactions/trans.png", "graphics/interactions/trans.png", "graphics/interactions/break.png", "graphics/interactions/trans.png",
						true, 1550, 460, 230, 330, 0, 0, 0, 0, -1, 3, -1, 5, false, 16, 10));
				actions.add(new Action(this,  6, "graphics/interactions/trans.png", "graphics/interactions/trans.png", "graphics/interactions/trans.png", "graphics/interactions/trans.png",
						true, 440, 540, 230, 410, 440, 540, 230, 410, 6, 5, 7, 8, false, 15, 11));
				
				//Passages
				passages.clear();
				passages.add(new Passage(this, 9, 660, 270, 430, 700, 3, -1, 1));
				passages.add(new Passage(this, 10, 00, 290, 210, 790, 0, -1, 1));
				passages.add(new Passage(this, 11, 1550, 460, 230, 330, 5, 5, 1));
				passages.add(new Passage(this, 38, 1240, 590, 150, 210, 0, 0, 1));
				
				//Puzzles
				puzzles.clear();
				
					backgrounds.add(new Background("graphics/backgrounds/gas.png", "graphics/backgrounds/gas_dist.png", 30, 180, 0.2, true, items, actions, passages, puzzles));
					
				//3 - back gas
					
				//Items
				items.clear();
				items.add(new Item(0, "graphics/items/crowbar_map.png", "graphics/items/crowbar_eq.png", "graphics/items/crowbar_dist.png", 240, 680, 50, 220, this, true, false, 4, 5));
				
				//Actions
				actions.clear();
				actions.add(new Action(this,  4, "graphics/interactions/trashbin.png", "graphics/interactions/trashbin_dist.png", "graphics/interactions/trashbin_moved.png", "graphics/interactions/trashbin_moved_dist.png",
						true, 260, 600, 450, 340, 490, 600, 450, 340, 0, 1, -1, 3, true, 8, 9));
				
				//Passages
				passages.clear();
				passages.add(new Passage(this, 12, 1530, 240, 390, 570, 2, -1, 1));
				passages.add(new Passage(this, 13, 0, 190, 260, 890, 2, 0, 1));
				passages.add(new Passage(this, 14, 560, 830, 500, 100, 2, 0, 1));
				passages.add(new Passage(this, 15, 670, 310, 510, 490, 2, 0, 0));
				passages.add(new Passage(this, 37, 310, 810, 30, 70, 2, 0, 0));
				
				//Puzzles
				puzzles.clear();
				
					backgrounds.add(new Background("graphics/backgrounds/gas2.png", "graphics/backgrounds/gas2_dist.png", 30, 180, 0.2, true, items, actions, passages, puzzles));
					
				//4- shed inside
					
				//Items
				items.clear();
				items.add(new Item(4, "graphics/items/screwdriver_map.png", "graphics/items/screwdriver_eq.png", "graphics/items/trans.png", 770, 440, 60, 60, this, true, true, 4, 5));
				items.add(new Item(5, "graphics/items/canister_map.png", "graphics/items/canister_eq.png", "graphics/items/trans.png", 460, 210, 200, 280, this, true, true, 4, 5));
				
				//Actions
				actions.clear();
				
				//Passages
				passages.clear();
				passages.add(new Passage(this, 16, 0, 930, 1920, 150, 1, -1, 1));
					
				//Puzzles
				puzzles.clear();
				
					backgrounds.add(new Background("graphics/backgrounds/shed_inside.png", "graphics/backgrounds/shed_inside_dist.png", 30, 200, 0.2, true, items, actions, passages, puzzles));
		
				//5 gas inside
					
					//Items
					items.clear();
					items.add(new Item(7, "graphics/items/seatbelt_map.png", "graphics/items/seatbelt_eq.png", "graphics/items/trans.png", 1470, 620, 120, 60, this, true, true, 4, 5));
					
					//Actions
					actions.clear();
					actions.add(new Action(this, 5, "graphics/interactions/trans.png", "graphics/interactions/trans.png", "graphics/interactions/electric_box_opened.png", "graphics/interactions/trans.png",
							true, 1110, 480, 110, 100, 0, 0, 0, 0, -1, 4, -1, 4, false, 13, 10));
					
					//Passages
					passages.clear();
					passages.add(new Passage(this, 17, 0, 930, 1920, 150, 2, -1, 1));
					passages.add(new Passage(this, 18, 330, 620, 30, 70, -1, 0, 0));
					passages.add(new Passage(this, 19, 410, 610, 30, 70, -1, 0, 0));
					passages.add(new Passage(this, 20, 370, 570, 30, 70, -1, 0, 0));
					passages.add(new Passage(this, 21, 480, 560, 30, 70, -1, 0, 0));
					passages.add(new Passage(this, 22, 410, 790, 20, 70, -1, 0, 0));
					passages.add(new Passage(this, 23, 430, 710, 30, 70, -1, 0, 0));
					passages.add(new Passage(this, 24, 480, 680, 30, 70, -1, 0, 0));
					passages.add(new Passage(this, 25, 600, 850, 100, 20, -1, 0, 0));
					passages.add(new Passage(this, 26, 690, 770, 80, 20, -1, 0, 0));
					passages.add(new Passage(this, 27, 660, 730, 80, 20, -1, 0, 0));
					passages.add(new Passage(this, 28, 930, 510, 130, 100, -1, 0, 0));
					passages.add(new Passage(this, 29, 350, 400, 130, 220, -1, 0, 0));
					passages.add(new Passage(this, 30, 1430, 400, 130, 220, -1, 0, 0));
					passages.add(new Passage(this, 31, 1400, 660, 30, 80, -1, 0, 0));
					passages.add(new Passage(this, 32, 1440, 710, 30, 80, -1, 0, 0));
					passages.add(new Passage(this, 33, 1480, 780, 20, 80, -1, 0, 0));
					passages.add(new Passage(this, 34, 1130, 690, 120, 90, -1, 0, 0));
					passages.add(new Passage(this, 35, 1240, 760, 50, 60, -1, 0, 0));
					passages.add(new Passage(this, 36, 1180, 830, 100, 40, -1, 0, 0));
					
						
					//Puzzles
					puzzles.clear();
					
					for(int i=0; i<10; i++) {
						for(int j=0; j<5; j++) {
							grid[i][j]=false;
						}
					}
					
					firstColor[0]=3; firstColor[1]=2; firstColor[2]=3; firstColor[3]=2; firstColor[4]=0;
					secondColor[0]=2; secondColor[1]=0; secondColor[2]=0; secondColor[3]=1; secondColor[4]=1;
					endColor[0]=0; endColor[1]=1; endColor[2]=2; endColor[3]=1; endColor[4]=3;
					length[0]=2; length[1]=1; length[2]=2; length[3]=2; length[4]=1;
					grid[1][0]=true; grid[4][1]=true; grid[1][3]=true; grid[3][3]=true;
					
					puzzles.add(new Puzzle(this, 0, 1110, 480, 110, 100, firstColor, secondColor, endColor,length, grid, true, -1, 4, 7, 18, 17, 0,
						"graphics/interactions/trans.png", "graphics/interactions/trans.png", "graphics/interactions/trans.png", "graphics/interactions/box_zoomed.png"));
					
						backgrounds.add(new Background("graphics/backgrounds/gas_inside.png", "graphics/backgrounds/gas_inside_dist.png", 30, 180, 0.2, true, items, actions, passages, puzzles));
						
				//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
				//backgrounds making ends here
				items.clear();		
				actions.clear();
				passages.clear();
				puzzles.clear();
				//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
						
				//6 story (0)
						
					backgrounds.add(new Background("graphics/backgrounds/trans.png", "graphics/backgrounds/trans.png", 30, 200, 0.2, true, items, actions, passages, puzzles));
					backgrounds.get(backgrounds.size()-1).MakeStory(this, backgrounds.size()-7, -1, 7);
					
				//7 story (1)
					
					backgrounds.add(new Background("graphics/backgrounds/trans.png", "graphics/backgrounds/trans.png", 30, 200, 0.2, true, items, actions, passages, puzzles));
					backgrounds.get(backgrounds.size()-1).MakeStory(this, backgrounds.size()-7, 6, 8);
					
				//8 story (2)
					
					backgrounds.add(new Background("graphics/backgrounds/trans.png", "graphics/backgrounds/trans.png", 30, 200, 0.2, true, items, actions, passages, puzzles));
					backgrounds.get(backgrounds.size()-1).MakeStory(this, backgrounds.size()-7, 7, 9);
					
				//9 story (3)
					
					backgrounds.add(new Background("graphics/backgrounds/trans.png", "graphics/backgrounds/trans.png", 30, 200, 0.2, true, items, actions, passages, puzzles));
					backgrounds.get(backgrounds.size()-1).MakeStory(this, backgrounds.size()-7, 8, 10);
					
				//10 story (4)
					
					backgrounds.add(new Background("graphics/backgrounds/trans.png", "graphics/backgrounds/trans.png", 30, 200, 0.2, true, items, actions, passages, puzzles));
					backgrounds.get(backgrounds.size()-1).MakeStory(this, backgrounds.size()-7, 9, 11);
					
				//11 story (5)
					
					backgrounds.add(new Background("graphics/backgrounds/trans.png", "graphics/backgrounds/trans.png", 30, 200, 0.2, true, items, actions, passages, puzzles));
					backgrounds.get(backgrounds.size()-1).MakeStory(this, backgrounds.size()-7, 10, 12);
					
				//12 story (6)
					
					backgrounds.add(new Background("graphics/backgrounds/trans.png", "graphics/backgrounds/trans.png", 30, 200, 0.2, true, items, actions, passages, puzzles));
					backgrounds.get(backgrounds.size()-1).MakeStory(this, backgrounds.size()-7, 11, 0);
					
				//13 story (7)
					
					backgrounds.add(new Background("graphics/backgrounds/trans.png", "graphics/backgrounds/trans.png", 30, 200, 0.2, true, items, actions, passages, puzzles));
					backgrounds.get(backgrounds.size()-1).MakeStory(this, -1, -1, -1);
					
		ChangeScenery(6);
		
	}

	public void ChangeMusic(int id) {
		sd.StartSong(id);
		whatMusic=id;
	}
	
	public void ChangeMusic(String name) {
		sd.StartSong(name);
		for(Sound s:sd.sounds) 
		if(s.name.equals(name)) {
				whatMusic=s.id;
				break;
			}
	}
	
	public void ChangeScenery(int id) {
		currentBackground=id;
		
		noiseRandomness=backgrounds.get(id).noiseRandomness;
		darkness=backgrounds.get(id).darkness;
		gradient=backgrounds.get(id).gradient;
		
		if(!developerMode)
		flashlightOn=backgrounds.get(id).flashlightOn;
		
	}
	
	public void ChangeColor(int id) {
		fR=fRt[id]; fG=fGt[id]; fB=fBt[id]; whichColor=id;
	}
	
	public void ChangeColor(String name) {
		for(int i=0; i<homManyColors; i++)
			if(color[i].equals(name)) {
			fR=fRt[i]; fG=fGt[i]; fB=fBt[i];
			break;
		}
	}

	public void Update() {
		if(broken) {
			if(rand.nextInt(howOftenBreaks)==0) ChangeColor(1);
			else if(rand.nextInt(howOftenWorks)==0) ChangeColor(0);
			
			if(rand.nextInt(howOftenBreaks)==0) flashlightBroken=true;
			else if(rand.nextInt(howOftenWorks)==0) flashlightBroken=false;
		}
		
		endScreen.Update();
		
	}
	
	public void ChangeLanguage(String which) {
		language=which;
		
		// File path is passed as parameter
        File file = new File("translations/"+language+"/informations/informations.txt");
 
        // Creating an object of BufferedReader class
        BufferedReader br;
        String st;
		try {
			br = new BufferedReader(new FileReader(file, Charset.forName("UTF-16")));
		
			information.clear();
			
			while((st = br.readLine()) != null) {
				information.add(st);
			} 
			
		} catch (IOException e) {e.printStackTrace();}
		
		for(Background b:backgrounds) {
			b.ChangeLanguage(which);
		}
		endScreen.ChangeLanguage(which);
	}
	
}

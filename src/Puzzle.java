import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Puzzle {

	//Objects
	Data data;
	
	//varibles
	Rectangle rec = new Rectangle();
	Rectangle innerRec = new Rectangle(100, 100, 1720, 880);
	String display="";
	
	Color color;
	static Image icon, iconDone, iconZoomed;
	static BufferedImage iconCable;
	BufferedImage iconDist;
	int x, y, width, height;
	String iconPath="graphics/interactions/box.png", iconDonePath="graphics/interactions/box.png", iconDistPath="graphics/interactions/trans.png",
		   iconZoomedPath="graphics/interactions/box_zoomed.png", iconCablePath="graphics/interactions/cable.png";
	String subtitle, subtitleDone, subtitleCant, subtitleExit;
	int[] firstColor = new int[5];
	int[] secondColor = new int[5];
	int[] endColor = new int[5];
	int[] length = new int[5];
	boolean[][] grid = new boolean[9][5];
	int[][] capacity = new int[9][5];
	boolean[] endCapacity = new boolean[5];
	boolean done=false, hover=false;
	boolean lunched=false;
	
	boolean allowDist;
	double[][] Distances = new double[192][108];
	boolean[][] alphaDist = new boolean[192][108];
	int neededItem, idReq, idReqChange;
	int id;
	
	int soundIdAction=0, soundIdDone=0, soundIdCant=0;
	
	//cables
	ArrayList<Cable> cables = new ArrayList<Cable>();
	
	//rectangle grid 
	static Rectangle[][] smallGrid = new Rectangle[192][108];
	
	//grid
	static Rectangle[][] recGrid = new Rectangle[9][5];
	
	//end Rectangles
	static Rectangle[] endRec = new Rectangle[5];
	
	public Puzzle(Data data, int id, int x, int y, int width, int height, int[] firstColor, int[] secondColor, int[] endColor, int[] length, boolean[][] grid,
			      boolean allowDist, int neededItem, int idReq, int idReqChange, int soundIdAction, int soundIdDone, int soundIdCant,
			      String iconPath, String iconDonePath, String iconDistPath, String iconZoomedPath) {
		this.data=data;
		this.id = id;
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.firstColor=firstColor;
		this.secondColor=secondColor;
		this.endColor=endColor;
		this.length=length;
		this.grid=grid;
		this.allowDist=allowDist;
		this.neededItem=neededItem;
		this.idReq=idReq;
		this.idReqChange=idReqChange;
		
		this.soundIdAction=soundIdAction;
		this.soundIdCant=soundIdCant;
		this.soundIdDone=soundIdDone;
		
		this.iconPath=iconPath;
		this.iconDistPath=iconDistPath;
		this.iconDonePath=iconDonePath;
		this.iconZoomedPath=iconZoomedPath;
		
		for(int i=0; i<192; i++) {
			for(int j=0; j<108; j++) {
				smallGrid[i][j] = new Rectangle(i*10, j*10, 10, 10);
			}
		}
		
		for(int i=0; i<9; i++) {
			for(int j=0; j<5; j++) {
				recGrid[i][j] = new Rectangle(460+i*110, 230+j*130, 100, 100);
				capacity[i][j] = 2;
			}
		}
		
			for(int i=0; i<5; i++) {
				endRec[i] = new Rectangle(1400, 230+i*130, 100, 100);
				endCapacity[i] = false;
			}
		
		rec.setBounds(x, y, width, height);
		
		try {
			icon = ImageIO.read(new File(iconPath));
			iconZoomed = ImageIO.read(new File(iconZoomedPath));
			iconDist = ImageIO.read(new File(iconDistPath));
			iconDone = ImageIO.read(new File(iconDonePath));
			iconCable = ImageIO.read(new File(iconCablePath));
		} catch (IOException e) {e.printStackTrace();}
		
		for(int i=0; i<192; i++) {
			for(int j=0; j<108; j++) {
				color = new Color(iconDist.getRGB(i, j), true);
				if(color.getAlpha()>0) {
					if(color.getRed()==color.getGreen()) Distances[i][j] = (double)(color.getRed());
					else Distances[i][j] = (double)((color.getRed()+color.getGreen()));
					alphaDist[i][j]=true;
					}
				else alphaDist[i][j]=false;
			}
		}
		
		for(int i=0; i<5; i++) {
			cables.add(new Cable(data, firstColor[i], secondColor[i], length[i], i, grid, capacity, endCapacity, endColor));
		}
		
		ChangeLanguage(data.language);
		
	}
	
	public void paintComponent(Graphics g, ImageObserver io) {
		
		if(!done) g.drawImage(icon, 0, 0, 1920, 1080, 0, 0, 192, 108, io);
		else g.drawImage(iconDone, 0, 0, 1920, 1080, 0, 0, 192, 108, io);
		 
		if(lunched) {
			
			g.drawImage(iconZoomed, 0, 0, 1920, 1080, 0, 0, 192, 108, io);
			
			for(int i=0; i<5; i++) {
				g.setColor(ReturnColor(endColor[i]));
				 g.fillRect(1500, 250+i*130, 50, 60);
				}
			
			//cables
			for(Cable c:cables) c.paintComponent(g, io);
			
			//grid
			for(int i=0; i<9; i++) {
				for(int j=0; j<5; j++) {
					if(grid[i][j])
					g.drawImage(iconCable, 460+i*110, 230+j*130, 560+i*110, 330+j*130, 0, 0, 10, 10, io);
					
				}
			}
			
		}
		
		
		
	}
	
	public void Clicked() {
		
		if(!lunched&&(idReq<0||data.reqs[idReq])) {
			lunched=true;
			data.puzzleOn=true;
			rec.setBounds(0, 0, 1920, 1080);
			data.flashlightOn=false;
			data.darkness=50;
			
			for(Cable c:cables) {
				c.StopDrawing();
			}
			
		} else if(!lunched) {
			display=subtitleCant;
			data.sd.StartSound(soundIdCant);
		} else {
			
			if(!data.mouse.intersects(innerRec)) {
				lunched=false;
				data.puzzleOn=false;
				rec.setBounds(x, y, width, height);
				data.flashlightOn=true;
				data.ChangeScenery(data.currentBackground);
			} else {
				if(!data.holdingCable) {
					
					boolean flag=false;
					
				for(Cable c:cables) {
					if(c.rec.intersects(data.mouse)) {
						c.ClickedStartingBolt();
						data.sd.StartSound(soundIdAction);
						break;
					}
					for(Segment s:c.segments) {
						
						if(s.displayFirstBool[data.xmouse/10][data.ymouse/10]||s.displaySecondBool[data.xmouse/10][data.ymouse/10]) {
							c.ClickedCable(c.segments.indexOf(s));
							data.sd.StartSound(soundIdAction);
							flag=true;
							break;
						}
					} if(flag) break;
				}
				
				
				
				} else {
					for(Cable c:cables) {
							if(c.drawingSeg) {
								data.sd.StartSound(soundIdAction);
								if(!CheckInterserection(c)) c.StopDrawing();
								else c.CancelCable();
							}
					}
				}
			}
			
			boolean flag=true;
			
			for(int i=0; i<cables.size(); i++) {
				if(!cables.get(i).goodConection) {
					flag=false; break;
				}
			}
			
			if(flag) {
				done=true;
				if(idReqChange>=0) data.reqs[idReqChange]=true;
				lunched=false;
				data.puzzleOn=false;
				rec.setBounds(x, y, width, height);
				data.flashlightOn=true;
				data.darkness=data.backgrounds.get(data.currentBackground).darkness;
				data.sd.StartSound(17);
				}
			
		}
		
	}
	
	public boolean CheckIfHover() {
		
		for(Cable c:cables) {
			if(c.rec.intersects(data.mouse)) return true;
			for(Segment s:c.segments) {
				if(s.displayFirstBool[data.xmouse/10][data.ymouse/10]||s.displaySecondBool[data.xmouse/10][data.ymouse/10]) return true;
			}
		}
		for(int i=0; i<9; i++) {
			for(int j=0; j<5; j++) {
				if(data.mouse.intersects(recGrid[i][j])&&grid[i][j]) return true;
			}
		}
		
		for(int i=0; i<5; i++) {
			if(data.mouse.intersects(endRec[i])) return true;
		}
		
		return false;
	}

	public void Hovered() {
		
		if(!lunched) {
			if(!done) display=subtitle;
			else display=subtitleDone;
			hover=true;
		}
		else if(!data.mouse.intersects(innerRec)) {
			display=subtitleExit;
			hover=true;
		} else hover=false;
		
	}

	public void NoHover() {
		hover = false;
		display="";
	}

	public void Update() {
		for(Cable c:cables) {
			if(!CheckInterserection(c)) c.Update();
		}
		
	}
	
	public boolean CheckInterserection(Cable c) {
		boolean flag=false;
		
			if(c.drawingSeg&&c.doingSeg!=null) {
				for(Cable c2:cables) {
					for(Segment s:c2.segments) {
						
						Line2D line = new Line2D.Double(c.doingSeg.m.getX1(), c.doingSeg.m.getY1(), (double)data.xmouse, (double)data.ymouse);
						
						if(s.lFirst.intersectsLine(line)&&s.lSecond.intersectsLine(line)) flag=true;
						
					
					}
				}
			}
		
		return flag;
	}
	
	public static Color ReturnColor(int id) {
		
		if(id==0) return Color.black;
		if(id==1) return Color.red;
		if(id==2) return Color.green;
		if(id==3) return Color.blue;
		
		return Color.black;
	}
	
	public void ChangeLanguage(String which) {
		// File path is passed as parameter
        File file = new File("translations/"+data.language+"/puzzles/"+id+".txt");
 
        // Creating an object of BufferedReader class
        BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file, Charset.forName("UTF-16")));
			this.subtitle = br.readLine();
			this.subtitleDone = br.readLine();
			this.subtitleCant = br.readLine();
			
			this.subtitleExit = br.readLine();
 
		} catch (IOException e) {
			this.subtitle = "file not found. Designated path: "+"translations/"+data.language+"/puzzles/"+id+".txt";
			this.subtitleDone = "file not found. Designated path: "+"translations/"+data.language+"/puzzles/"+id+".txt";
			this.subtitleCant = "file not found. Designated path: "+"translations/"+data.language+"/puzzles/"+id+".txt";
			
			this.subtitleExit = "file not found. Designated path: "+"translations/"+data.language+"/puzzles/"+id+".txt";
		}
	}
	
}

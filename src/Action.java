import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Random;

import javax.imageio.ImageIO;

public class Action extends Interactions{
	
	//varibles
	Image icon, icon2;
	BufferedImage iconDist, icon2Dist;
	double[][] Distances = new double[192][108];
	double[][] Distances2 = new double[192][108];
	Color color;
	String subtitle2, subtitle3;
	String subtitleChange=null, subtitle2Change;
	int x2, y2, width2, height2;
	String iconPath, iconDistPath, icon2Path, icon2DistPath;
	int enableItem, neededItem;
	int idReq, idReqChange;
	int soundId, soundIdCant;
	Random rand = new Random();
	boolean distHolderForItem=false;
	int id;
	
	//booleans
	boolean allowDist=true;
	boolean[][] alphaDist = new boolean[192][108]; 
	boolean[][] alphaDist2 = new boolean[192][108]; 
	boolean done=false;
	boolean reversable;
	
	public Action(Data data, int id, String iconPath, String iconDistPath, String icon2Path, String icon2DistPath,
			      boolean allowDist, int x, int y, int width, int height, int x2, int y2, int width2, int height2, int enableItem, int neededItem, int idReq, int idReqChange,
			      boolean reversable, int soundId, int soundIdCant) {
		super();
		this.data = data;
		
		try {
			icon = ImageIO.read(new File(iconPath));
			iconDist = ImageIO.read(new File(iconDistPath));
			icon2 = ImageIO.read(new File(icon2Path));
			icon2Dist = ImageIO.read(new File(icon2DistPath));
		} catch (IOException e) {e.printStackTrace();}
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.x2 = x2;
		this.y2 = y2;
		this.width2 = width2;
		this.height2 = height2;
		this.iconPath = iconPath;
		this.icon2Path = icon2Path;
		this.iconDistPath = iconDistPath;
		this.icon2DistPath = icon2DistPath;
		this.enableItem = enableItem;
		this.neededItem = neededItem;
		this.id = id;
		
		this.idReq = idReq;
		this.idReqChange = idReqChange;
		
		this.reversable=reversable;
		
		this.soundId = soundId;
		this.soundIdCant = soundIdCant;
		
		for(int i=0; i<192; i++) {
			for(int j=0; j<108; j++) {
				color = new Color(iconDist.getRGB(i, j), true);
				if(color.getAlpha()>0) {
					if(color.getRed()==color.getGreen()) Distances[i][j] = (double)(color.getRed());
					else Distances[i][j] = (double)((color.getRed()+color.getGreen()));
				alphaDist[i][j]=true;
				}
				else alphaDist[i][j]=false;
				
				color = new Color(icon2Dist.getRGB(i, j), true);
				if(color.getAlpha()>0) {
					if(color.getRed()==color.getGreen()) Distances2[i][j] = (double)(color.getRed());
					else Distances2[i][j] = (double)((color.getRed()+color.getGreen()));
					alphaDist2[i][j]=true;
					}
				else alphaDist2[i][j]=false;
			}
		}
		
		ChangeLanguage(data.language);
		
	}
	
	public void paintComponent(Graphics g, ImageObserver io) {
		if(!done) rec.setBounds(x, y, width, height); 
		else rec.setBounds(x2, y2, width2, height2);
		
		if(!done) g.drawImage(icon, 0, 0, 1920, 1080, 0, 0, 192, 108, io);
		else g.drawImage(icon2, 0, 0, 1920, 1080, 0, 0, 192, 108, io);
		
	}
	
	public void Clicked() {
		if(!done&&((idReq<0)||(idReq>=0&&data.reqs[idReq]))) {
			if((neededItem>=0&&data.itemInHand&&neededItem==data.itemId)||neededItem<0) {
		if(idReqChange>=0) data.reqs[idReqChange]=true;		
		done=true;
		rec.setBounds(x2, y2, width2, height2);
		display=subtitle2;
		data.sd.StartSound(soundId);
		if(enableItem>=0)
			for(int i=0; i<data.backgrounds.size(); i++) {
				for(int j=0; j<data.backgrounds.get(i).items.size(); j++) {
					if(data.backgrounds.get(i).items.get(j).id==enableItem) {
						distHolderForItem=data.backgrounds.get(i).items.get(j).allowDist;
						data.backgrounds.get(i).items.get(j).allowDist=true;
						data.backgrounds.get(i).items.get(j).canBePicked=true;
					}
				}
			}
		} 
			else if(neededItem>=0&&data.itemId!=neededItem&&data.itemInHand) {
				data.sd.StartSound(soundIdCant);
				display=data.information.get(rand.nextInt(data.information.size()));
			} else {
				display=subtitle3;
				data.sd.StartSound(soundIdCant);
				}
		} else if(reversable) {
			
			if(x!=x2||y!=y2||width!=width2||height!=height2) data.sd.StartSound(soundId);
			else data.sd.StartSound(soundIdCant);
			
			done=false;
			rec.setBounds(x, y, width, height);
			if(enableItem>=0)
			for(int i=0; i<data.backgrounds.size(); i++) {
				for(int j=0; j<data.backgrounds.get(i).items.size(); j++) {
					if(data.backgrounds.get(i).items.get(j).id==enableItem) {
						data.backgrounds.get(i).items.get(j).allowDist=distHolderForItem;
						data.backgrounds.get(i).items.get(j).canBePicked=false;
					}
				}
			}
			
			subtitle=subtitleChange;
			subtitle2=subtitle2Change;
			display=subtitle;
		} else {
			display=subtitle3;
			data.sd.StartSound(soundIdCant);
		}
		
	}
	
	public void Hovered() {
		hover = true;
		if(!done) display=subtitle;
		else display=subtitle2;
	}

	public void NoHover() {
		hover = false;
		display="";
	}

	public void ChangeLanguage(String which) {
		// File path is passed as parameter
        File file = new File("translations/"+data.language+"/actions/"+id+".txt");
 
        // Creating an object of BufferedReader class
        BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file, Charset.forName("UTF-16")));
		
			String garbage=null;
			
			if(subtitleChange==null||!subtitle.equals(subtitleChange)) {
				this.subtitle = br.readLine();
				this.subtitle2 = br.readLine();
				}
			else {
				garbage = br.readLine();
				garbage = br.readLine();
			}
			
			this.subtitle3 = br.readLine();
			
			this.subtitleChange = br.readLine();
			this.subtitle2Change = br.readLine();
			
			if(garbage!=null) {
				this.subtitle = this.subtitleChange;
				this.subtitle2 = this.subtitle2Change;
			}
 
		} catch (IOException e) {
			this.subtitle ="file not found. Designated path: "+"translations/"+data.language+"/actions/"+id+".txt";
			this.subtitle2 = "file not found. Designated path: "+"translations/"+data.language+"/actions/"+id+".txt";
			this.subtitle3 = "file not found. Designated path: "+"translations/"+data.language+"/actions/"+id+".txt";
			
			this.subtitleChange = "file not found. Designated path: "+"translations/"+data.language+"/actions/"+id+".txt";
			this.subtitle2Change = "file not found. Designated path: "+"translations/"+data.language+"/actions/"+id+".txt";
		}
	}
	
} 

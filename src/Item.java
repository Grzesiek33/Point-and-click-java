import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.imageio.ImageIO;

public class Item implements Cloneable{

	//Objects
	Data data;
	
	//Varibles
	Image icon, iconEq;
	BufferedImage iconDist;
	int x, y, width, height, yForFlaing=0, xForFlaing=0, xShrink=0, yShrink=0;
	double[][] Distances = new double[192][108];
	int id;
	String subtitle, subtitleEq, nonPickable;
	String iconPath, iconEqPath, iconDistPath;
	Rectangle rec = new Rectangle();
	Color color;
	String display="";
	int soundId, soundIdCant;
	
	//booleans
		boolean inEq=false;
		boolean hover=false;
		boolean canBePicked=true;
		boolean inPlace=false;
		boolean allowDist=true;
		boolean inHand=false;
		boolean[][] alphaDist = new boolean[192][108]; 
	
	public Item(int id, String iconPath, String iconEqPath, String iconDistPath, int x, int y, int width, int height, Data data,
			    boolean allowDist, boolean canBePicked, int soundId, int soundIdCant) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.iconPath = iconPath;
		this.iconEqPath = iconEqPath;
		this.iconDistPath = iconDistPath;
		this.id = id;
		this.data = data;
		this.allowDist = allowDist;
		this.canBePicked = canBePicked;
		
		this.soundId = soundId;
		this.soundIdCant = soundIdCant;
		
		try {
			icon = ImageIO.read(new File(iconPath));
			iconEq = ImageIO.read(new File(iconEqPath));
			iconDist = ImageIO.read(new File(iconDistPath));
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
		
		ChangeLanguage(data.language);
		
	}

	public void paintComponent(Graphics g, ImageObserver io) {
		if(!inPlace) rec.setBounds(x, y, width, height);
		else if(!inHand) rec.setBounds(x, y, data.eqSize, data.eqSize);
		else rec.setBounds(x, y, data.eqSize, data.eqSize);
		
		if(!inPlace) g.drawImage(icon, 0, 0+yForFlaing, 1920-xShrink, 1080+yForFlaing-yShrink, 0, 0, 192, 108, io);
		else if(y<90&&!inHand) g.drawImage(iconEq, x+xForFlaing, y+yForFlaing, data.eqSize, data.eqSize,  io);
		else if(inHand) g.drawImage(iconEq, data.xmouse, data.ymouse, data.eqSize, data.eqSize,  io);
		
	}

	public void Hovered() {
		hover = true;
		if(!inEq) display=subtitle;
		else if(inEq) display=subtitleEq;
	}

	public void NoHover() {
		hover = false;	
		display="";
	}

	public void Clicked() {
		if(!canBePicked) {
			data.sd.StartSound(soundIdCant);
			display=nonPickable;
			}
		else {
			data.sd.StartSound(soundId);
			}
	}
	
	public void ChangeLanguage(String which) {
		// File path is passed as parameter
        File file = new File("translations/"+data.language+"/items/"+id+".txt");
 
        // Creating an object of BufferedReader class
        BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file, Charset.forName("UTF-16")));
		
			this.subtitle = br.readLine();
			this.subtitleEq = br.readLine();
			this.nonPickable = br.readLine();
 
		} catch (IOException e) {
			this.subtitle = "file not found. Designated path: "+"translations/"+data.language+"/items/"+id+".txt";
			this.subtitleEq = "file not found. Designated path: "+"translations/"+data.language+"/items/"+id+".txt";
			this.nonPickable = "file not found. Designated path: "+"translations/"+data.language+"/items/"+id+".txt";
		}
	}
	
}

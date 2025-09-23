import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

public class EndScreen {

	Random rand = new Random();
	int id;
	ArrayList<String> stories = new ArrayList<String>();
	Color color;
	Image grass, trees1, trees2, trees3, road, car, light;
	String grassPath="graphics/endgame/grass.png",
		   trees1Path="graphics/endgame/trees1.png",
		   trees2Path="graphics/endgame/trees2.png",
		   trees3Path="graphics/endgame/trees3.png",
		   roadPath="graphics/endgame/road.png",
		   carPath="graphics/endgame/car.png",
		   lightPath="graphics/endgame/light.png";
	
	int shiftx=0, shifty=0, maxCar=80;
	
	double shiftGrass=1,
		   shiftTrees1=2,
		   shiftTrees2=3,
		   shiftTress3=4,
		   shiftroad=7,
		   shiftCar=2;
	
	boolean carUp=true;
	
	public EndScreen(int id) {
		this.id=id;
		try {
			System.out.println(System.getProperty("user.dir"));
			grass = ImageIO.read(new File(grassPath));
			trees1 = ImageIO.read(new File(trees1Path));
			trees2 = ImageIO.read(new File(trees2Path));
			trees3 = ImageIO.read(new File(trees3Path));
			road = ImageIO.read(new File(roadPath));
			car = ImageIO.read(new File(carPath));
			light = ImageIO.read(new File(lightPath));
			
		} catch (IOException e) {e.printStackTrace();}
		
		ChangeLanguage("English");
	}

	public void paintComponent(Graphics g2, ImageObserver io) {
		
		int xGrass=(int)((shiftx%(1920/shiftGrass))*shiftGrass),
			xTrees1=(int)((shiftx%(1920/shiftTrees1))*shiftTrees1),
			xTrees2=(int)((shiftx%(1920/shiftTrees2))*shiftTrees2),
			xTrees3=(int)((shiftx%(1920/shiftTress3))*shiftTress3),
			xRoad=(int)((shiftx%(1920/shiftroad))*shiftroad),
			yCar=shifty+rand.nextInt(3);
		
		g2.drawImage(grass, xGrass, 0, 1920, 1080, io);
		g2.drawImage(grass, xGrass+1920, 0, 1920, 1080, io);
		
		g2.drawImage(trees1, xTrees1, 0, 1920, 1080, io);
		g2.drawImage(trees1, xTrees1+1920, 0, 1920, 1080, io);
		
		g2.drawImage(trees2, xTrees2, 0, 1920, 1080, io);
		g2.drawImage(trees2, xTrees2+1920, 0, 1920, 1080, io);
		
		g2.drawImage(trees3, xTrees3, 0, 1920, 1080, io);
		g2.drawImage(trees3, xTrees3+1920, 0, 1920, 1080, io);
		
		g2.drawImage(road, xRoad, 0, 1920, 1080, io);
		g2.drawImage(road, xRoad+1920, 0, 1920, 1080, io);
		
		g2.drawImage(car, 0, yCar, 1920, 1080, io);
		
		g2.drawImage(light, xTrees3, 0, 1920, 1080, io);
		g2.drawImage(light, xTrees3+1920, 0, 1920, 1080, io);
		
		g2.setColor(color = new Color(0, 0, 0, 150));
		g2.fillRect(0, 0, 1920, 1080);
		g2.setColor(Color.white);
		for(String s:stories) 
		g2.drawString(s, 10, 50+50*stories.indexOf(s));
		
	}
	
	public void Update() {
		shiftx--;
		
		if(shifty<maxCar&&carUp) shifty+=shiftCar;
		else if(shifty>=maxCar&&carUp&&rand.nextInt(50)==0) {carUp=false; shifty-=shiftCar;}
		else if(shifty>-maxCar&&!carUp) shifty-=shiftCar;
		else if(shifty<=-maxCar&&!carUp&&rand.nextInt(50)==0) {carUp=true; shifty+=shiftCar;}
	}
	
	public void ChangeLanguage(String which) {
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
	}
	
}

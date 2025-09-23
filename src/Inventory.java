import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public class Inventory {

	//Objects
	Data data;
	
	//Varibles
	int height=0, maxHeight=0;
	ArrayList<Item> items = new ArrayList<Item>();
	
	public Inventory(Data data) {
		this.data = data;
		maxHeight=data.eqSize+2*data.verDist;
	}
		
	public void paintComponent(Graphics g, ImageObserver io) {
		g.setColor(new Color(20, 20, 20, 160));
		g.fillRect(0, 0, 1920, height);
		
		
		
	}

	public void Update() {
		
		if(data.ymouse<5||data.ymouse<=height) data.openInventory=true;
		else data.openInventory=false;
		
		if(height<maxHeight&&data.openInventory) height+=7;
		else if(height>0&&!data.openInventory) height-=7;
		
		for(Item i:items) {
			if(!i.inPlace&&i.inEq&&!i.inHand) {
			 if(i.yForFlaing>=-1080) {i.yForFlaing-=30; i.xShrink+=20; i.yShrink+=15;}
			 else i.inPlace=true;
			}
			
			if(i.inPlace&&!i.inEq&&!i.inHand) {
				 if(i.yForFlaing>0) {i.yForFlaing-=30;}
				 else i.inEq=true;
				}
			
			if(i.inPlace&&i.inEq&&!i.inHand) {
				i.yForFlaing=0;
				i.xForFlaing=0;
				i.y=height-maxHeight+data.verDist;
				i.x=items.indexOf(i)*data.eqSize+data.horDist*(items.indexOf(i)+1);
			}
		}
		 
	}
	
	public void ChangeLanguage(String which) {
		for(Item a:items) {
			a.ChangeLanguage(which);
		}
	}
	
}

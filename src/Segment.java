import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Line2D;
import java.awt.image.ImageObserver;

public class Segment {

	double xStart, yStart, xEnd, yEnd;
	int firstColor, secondColor;
	boolean[][] takenBolt = new boolean[10][5];
	int endingBolX=0, endingBolY=0;
	
	Line2D m, lFirst, lSecond, per;
	
	//colors
	boolean swapColors=false;
	int whenSwap=0;
	boolean setInPlace=false;
	
	//rectangles
	boolean[][] displayFirstBool = new boolean[192][108];
	boolean[][] displaySecondBool = new boolean[192][108];
	
	public Segment(double xStart, double yStart, double xEnd, double yEnd, int firstColor, int secondColor) {
		this.xEnd=xEnd;
		this.xStart=xStart;
		this.yEnd=yEnd;
		this.yStart=yStart;
		this.firstColor=firstColor;
		this.secondColor=secondColor;
		
		for(int i=0; i<10; i++) {
			for(int j=0; j<5; j++) {
				takenBolt[i][j]=false;
			}
		}
		
		m = new Line2D.Float(0, 0, 0, 0);
		per = new Line2D.Float(0, 0, 0, 0);
		
		lSecond = new Line2D.Float(0, 0, 0, 0);
		lFirst = new Line2D.Float(0, 0, 0, 0);
		
		CheckPosition();
		
	}
	
	public void CheckPosition() {
		
		double xSxE, ySyE;
		
		if(xStart-xEnd!=0) xSxE=xStart-xEnd;
		else xSxE=0.00000001;
		
		if(yStart-yEnd!=0) ySyE=yStart-yEnd;
		else ySyE=0.00000001;
		
		double am = (ySyE)/(xSxE);
		double bm = yStart - (xStart*am);
		
		double b1 = bm*2-5*Math.sqrt(Math.pow(am, 2)+1) + am*xStart - yStart;
		double b2 = bm*2+5*Math.sqrt(Math.pow(am, 2)+1) + am*xStart - yStart;
		
		double xStartFirst = (yStart + (xStart/am) - b1)/(am + (1/am));
		double xEndFirst = (yEnd + (xEnd/am) - b1)/(am + (1/am));
		
		double xStartSecond = (yStart + (xStart/am) - b2)/(am + (1/am));
		double xEndSecond = (yEnd + (xEnd/am) - b2)/(am + (1/am));
		
		double yStartFirst = (am*xStartFirst+b1);
		double yEndFirst = (am*xEndFirst+b1);
		
		double yStartSecond = (am*xStartSecond+b2);
		double yEndSecond = (am*xEndSecond+b2);
		
		m.setLine(xStart, yStart, xEnd, yEnd);
		
		lSecond.setLine(xStartFirst, yStartFirst, xEndFirst, yEndFirst);
		lFirst.setLine(xStartSecond, yStartSecond, xEndSecond, yEndSecond);
		
		for(int i=0; i<192; i++) {
			for(int j=0; j<108; j++) {
				if(lFirst.intersects(Puzzle.smallGrid[i][j])) displayFirstBool[i][j] = true;
				else displayFirstBool[i][j] = false;
			}
		}
		
		for(int i=0; i<192; i++) {
			for(int j=0; j<108; j++) {
				if(lSecond.intersects(Puzzle.smallGrid[i][j])) displaySecondBool[i][j] = true;
				else displaySecondBool[i][j] = false;
			}
		}
	}

	public void paintComponent(Graphics g, ImageObserver io) {
		
		if(!setInPlace) {
			
			g.setColor(Puzzle.ReturnColor(firstColor));
			
			for(int i=0; i<192; i++) {
				for(int j=0; j<108; j++) {
					if(displayFirstBool[i][j]) {	
						g.fillRect(i*10, j*10, 10, 10);
						}
				}
			}
			
			g.setColor(Puzzle.ReturnColor(secondColor));
			
			for(int i=0; i<192; i++) {
				for(int j=0; j<108; j++) {
					if(displaySecondBool[i][j]) {
						g.fillRect(i*10, j*10, 10, 10);
						}
				}
			}
			
		} else {
			
		if(xStart==xEnd||yStart==yEnd) whenSwap=1; 
		else whenSwap=0;
		
		swapColors=false;
		
		for(int i=0; i<192; i++) {
			for(int j=0; j<108; j++) {
				if(displayFirstBool[i][j]) 
					{
						if(!swapColors) g.setColor(Puzzle.ReturnColor(firstColor));
						else g.setColor(Puzzle.ReturnColor(secondColor));
						whenSwap++;
						
						if(whenSwap>=5) {
							if(swapColors) swapColors=false; else swapColors=true;
							whenSwap=0;
						}
						
					g.fillRect(i*10, j*10, 10, 10);
					}
			}
		}
		
		whenSwap=0; swapColors=false;
		
		for(int i=0; i<192; i++) {
			for(int j=0; j<108; j++) {
				if(displaySecondBool[i][j]) {
					
					if(!swapColors) g.setColor(Puzzle.ReturnColor(secondColor));
					else g.setColor(Puzzle.ReturnColor(firstColor));
					whenSwap++;
					
					if(whenSwap>=5) {
						if(swapColors) swapColors=false; else swapColors=true;
						whenSwap=0;
					}
					
					g.fillRect(i*10, j*10, 10, 10);
					}
			}
		}
		
	}
		
	}
	
}

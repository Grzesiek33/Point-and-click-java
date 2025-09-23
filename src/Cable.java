import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Random;

public class Cable {

	Data data;
	
	int firstColor, secondColor, length, position;
	int[][] capacity;
	int[] endColor;
	boolean[][] grid;
	boolean[] endCapacity;
	boolean goodConection=false;
	Rectangle rec = new Rectangle();
	Random rand = new Random();
	
	Segment doingSeg = null;
	boolean drawingSeg=false;
	
	//segments
	ArrayList<Segment> segments = new ArrayList<Segment>();
	
	public Cable(Data data, int FirstColor, int secondColor, int length, int position, boolean[][] grid, int[][] capacity, boolean[] endCapacity, int[] endColor) {
		this.firstColor = FirstColor;
		this.secondColor = secondColor;
		this.length = length;
		this.position = position;
		this.grid = grid;
		this.data = data;
		this.capacity = capacity;
		this.endCapacity = endCapacity;
		this.endColor = endColor;
		
		rec.setBounds(360, 230+position*130, 100, 100);
		
		while(true) {
			int rand1=rand.nextInt(9), rand2=rand.nextInt(5);
			
			if(grid[rand1][rand2]) {
			segments.add(new Segment(460, 280+position*130, 510+rand1*110, 280+rand2*130, FirstColor, secondColor));
			segments.get(segments.size()-1).setInPlace=true;
			this.length--;
			break;
			}
		}
	}
	
	public void paintComponent(Graphics g, ImageObserver io) {
		 
			g.setColor(Puzzle.ReturnColor(firstColor));
		 g.fillRect(360, 250+position*130, 50, 30);
		 	g.setColor(Puzzle.ReturnColor(secondColor));
		 g.fillRect(360, 280+position*130, 50, 30);
		 
		 g.setColor(Color.white);
		 g.drawString(Integer.toString(length), 320, 300+position*130);
		 
		 //segments
		 for(Segment s:segments) s.paintComponent(g, io);
		 if(doingSeg!=null) doingSeg.paintComponent(g, io);
	}

	public void ClickedStartingBolt() {
		if(!drawingSeg) {
			clearBolts(0);
			segments.clear();
			data.holdingCable=true;
			drawingSeg=true;
			doingSeg = new Segment(460, 280+position*130, data.xmouse, data.ymouse, firstColor, secondColor);
		} else {
			StopDrawing();
		}
		
	}
	
	public void ClickedCable(int segmentId) {
		if(!drawingSeg) {
			goodConection=false;
			if(segmentId==0) {
			clearBolts(segmentId);
			segments.clear();
			data.holdingCable=true;
			drawingSeg=true;
			doingSeg = new Segment(460, 280+position*130, data.xmouse, data.ymouse, firstColor, secondColor);
			} else {
				clearBolts(segmentId);
				for(int i=segments.size()-1; i>=segmentId; i--) {
					segments.remove(i);
				}
				data.holdingCable=true;
				drawingSeg=true;
				
				doingSeg = new Segment(segments.get(segmentId-1).endingBolX, segments.get(segmentId-1).endingBolY, data.xmouse, data.ymouse, firstColor, secondColor);
			}
		} else {
			StopDrawing();
		}
		
	}
	
	public void StopDrawing() {
		if(doingSeg!=null) MakeSegment();
		
	}
	
	public void clearBolts(int id) {
		for(Segment s: segments) {
			if(segments.indexOf(s)>=id) {
				length++;
				for(int i=0; i<5; i++) {
					if(s.takenBolt[9][i]) length--;
				}
				for(int j=0; j<5; j++) {
					for(int i=0; i<9; i++) {
					if(s.takenBolt[i][j]) {
						s.takenBolt[i][j]=false;
						capacity[i][j]++;
					}
				}
					if(s.takenBolt[9][j]) {
						s.takenBolt[9][j]=false;
						endCapacity[j]=false;
					}
					
				}
			}
		}
	}
	
	public void CancelCable() {
		data.holdingCable=false;
		drawingSeg=false;
		doingSeg=null;
	}
	
	public void MakeSegment() {
		boolean flag=false;
		
		for(int i=0; i<9; i++) {
			for(int j=0; j<5; j++) {
				if(data.mouse.intersects(Puzzle.recGrid[i][j])&&grid[i][j]&&capacity[i][j]>0&&length>0) {
					capacity[i][j]--;
				segments.add(new Segment(doingSeg.xStart, doingSeg.yStart, 510+i*110, 280+j*130, doingSeg.firstColor, doingSeg.secondColor));
				segments.get(segments.size()-1).takenBolt[i][j]=true;
				segments.get(segments.size()-1).endingBolX=(int) Puzzle.recGrid[i][j].getX()+50;
				segments.get(segments.size()-1).endingBolY=(int) Puzzle.recGrid[i][j].getY()+50;
				segments.get(segments.size()-1).setInPlace=true;
				
				length--;
				doingSeg = new Segment(segments.get(segments.size()-1).endingBolX, segments.get(segments.size()-1).endingBolY, data.xmouse, data.ymouse, firstColor, secondColor);
				
				flag=true;
				}
				if(flag) break;
			}
			if(flag) break;
		}

		if(!flag) {
			for(int i=0; i<5; i++) {
				if(data.mouse.intersects(Puzzle.endRec[i])&&!endCapacity[i]) {
					endCapacity[i] = true;
					segments.add(new Segment(doingSeg.xStart, doingSeg.yStart, 1450, 280+i*130, doingSeg.firstColor, doingSeg.secondColor));
					segments.get(segments.size()-1).takenBolt[9][i]=true;
					segments.get(segments.size()-1).setInPlace=true;
					if(firstColor==endColor[i]||secondColor==endColor[i]) goodConection=true;
					
					break;
				}
			}
		}
		
		if(!flag) CancelCable();
		
	}
	
	public void Update() {
		if(drawingSeg&&doingSeg!=null) {
			doingSeg.xEnd=data.xmouse;
			doingSeg.yEnd=data.ymouse;
			doingSeg.CheckPosition();
		}
	}
		 
}

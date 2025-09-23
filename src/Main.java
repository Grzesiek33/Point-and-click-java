import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

@SuppressWarnings("LossyEncoding")
public class Main extends JFrame implements KeyListener, Runnable{

	//Objects
	SoundControler sd = new SoundControler();
	Data data = new Data(sd);
	Jpanel Panel = new Jpanel(data);
	Thread GameThread = new Thread();
	
	//Varibles
	ArrayList<Integer> pressedKeys = new ArrayList<Integer>();
	boolean end=false;
	
	public Main() {
		setTitle("GRA");
		setUndecorated(true);
		addKeyListener(this);
		add(Panel);	
		
		GameThread = new Thread(this);
		GameThread.start();
		
		setVisible(true);
		toFront();
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.setDefaultCloseOperation(EXIT_ON_CLOSE);
		main.setExtendedState(JFrame.MAXIMIZED_BOTH);
		main.setVisible(true);
		main.toFront();
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if(!pressedKeys.contains(e.getKeyCode()))
		pressedKeys.add(Integer.valueOf(e.getKeyCode()));
		
		if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
			if(Panel.menu) Panel.menu=false;
			else Panel.menu=true;
		}
		
		if(e.getKeyCode()==KeyEvent.VK_F1&&e.isShiftDown()) {
			if(!data.developerMode) data.developerMode=true;
			else data.developerMode=false;
		}
		
		if(data.developerMode) {
			
			if(e.getKeyCode()==KeyEvent.VK_1&&!data.freeWill) {
				data.freeWill=true;
				for(int i=1; i<data.reqs.length; i++) {
					data.reqsDev[i]=data.reqs[i];
					data.reqs[i]=true;
				}
			}
			
			if(e.getKeyCode()==KeyEvent.VK_2&&data.freeWill) {
				data.freeWill=false;
				for(int i=1; i<data.reqs.length; i++)
					data.reqs[i]=data.reqsDev[i];
			}
			
		if(e.getKeyCode()==KeyEvent.VK_UP) {
			if(data.whatMusic<data.howManySongs-1)
				data.ChangeMusic(data.whatMusic+1);
			else data.ChangeMusic(0);
		}
		
		if(e.getKeyCode()==KeyEvent.VK_DOWN) {
			if(data.whatMusic>0)
				data.ChangeMusic(data.whatMusic-1);
			else data.ChangeMusic(data.howManySongs-1);
		}
			
		if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
			if(data.backgrounds.size()>data.currentBackground+1) data.ChangeScenery(data.currentBackground+1);
			else data.ChangeScenery(0);
		}
		
		if(e.getKeyCode()==KeyEvent.VK_LEFT) {
			if(data.currentBackground>0) data.ChangeScenery(data.currentBackground-1);
			else data.ChangeScenery(data.backgrounds.size()-1);
		}
		
		if(e.getKeyCode()==KeyEvent.VK_F&&e.isShiftDown()) {
			if(data.fancyFlashlight) data.fancyFlashlight=false;
			else data.fancyFlashlight=true;
		}
		
		else if(e.getKeyCode()==KeyEvent.VK_F) {
			if(data.flashlightOn) data.flashlightOn=false;
			else data.flashlightOn=true;
		}
		
		if(e.getKeyCode()==KeyEvent.VK_W) {
			data.ChangeColor(0);
		}
		
		if(e.getKeyCode()==KeyEvent.VK_Y) {
			data.ChangeColor(1);
		}
		
		if(e.getKeyCode()==KeyEvent.VK_R) {
			data.ChangeColor(2);
		}
		
		if(e.getKeyCode()==KeyEvent.VK_G) {
			data.ChangeColor(3);
		}
		
		if(e.getKeyCode()==KeyEvent.VK_B) {
			data.ChangeColor(4);
		}
		
		if(e.getKeyCode()==KeyEvent.VK_P) {
			data.ChangeLanguage("Polish");
			Panel.inventory.ChangeLanguage("Polish");
		}
		
		if(e.getKeyCode()==KeyEvent.VK_E) {
			data.ChangeLanguage("English");
			Panel.inventory.ChangeLanguage("English");
		}
		
		if(e.getKeyCode()==KeyEvent.VK_D) {
			if(data.broken) data.broken=false;
			else data.broken=true;
		}
		
		if(e.getKeyCode()==KeyEvent.VK_A) {
			data.ChangeLanguage(data.language);
		}
		
		}
		
		boolean flag=true;
		
		if(!pressedKeys.isEmpty()&&pressedKeys.size()==3) {
			for(Integer i:pressedKeys)
				if(i!=KeyEvent.VK_L&&i!=KeyEvent.VK_G&&i!=KeyEvent.VK_B)
					flag=false;
		} else flag=false;
		
		if(flag) {
			if(data.lgbt) data.lgbt=false;
			else data.lgbt=true;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		pressedKeys.remove(Integer.valueOf(e.getKeyCode()));
		
	}

	public void Update() {
		data.gameTime++;
		
		if(data.drawInventory&&!data.backgrounds.get(data.currentBackground).storyOn) Panel.inventory.Update();
		
		if(data.lgbt) {
		if(data.whichColor<data.homManyColors-1) data.ChangeColor(data.whichColor+1);
		else data.ChangeColor(0);
		}
		
		if(!data.developerMode&&data.reqs[3]&&data.howLongBroken==0) {
			data.howOftenBreaks=30;
			data.howOftenWorks=10;
			data.howLongBroken=(int)data.gameTime+150;
		} else if(data.reqs[3]) {
			if(data.howLongBroken==data.gameTime) {
				data.howOftenBreaks=100;
				data.howOftenWorks=5;
			}
		}
		
		for(Puzzle p:data.backgrounds.get(data.currentBackground).puzzles) {
			p.Update();
		}
		
		if(data.reqs[6]&&!data.lightsOn) {
			data.lightsOn=true;
			data.backgrounds.get(data.currentBackground).flashlightOn=false;
			data.backgrounds.get(data.currentBackground).darkness=0;
			data.ChangeScenery(data.currentBackground);
		}
		
		if(data.reqs[7]) { //gas station puzzle solved
			Panel.showInstrybutor=true;
			if(data.language.equals("Polish")) data.backgrounds.get(2).actions.get(1).subtitle3="Potrzebuje jakiego� pojemnika";
			else data.backgrounds.get(2).actions.get(1).subtitle3="I need a container of some sort";
		}
		
		if(data.reqs[8]) { //taken fuel
			data.backgrounds.get(2).actions.get(1).subtitle3=data.backgrounds.get(2).actions.get(1).subtitle2;
			for(Item i:Panel.inventory.items) {
				if(i.id==5) {
					Panel.inventory.items.remove(i);
					break;
				}
			}
			
		}
		
		if(data.reqs[9]) {
			for(Item i:Panel.inventory.items) {
				if(i.id==7) {
					Panel.inventory.items.remove(i);
				break;
				}
			}
		}
		
		if(data.reqs[9]&&data.reqs[10]&&!end) {
			end=true;
			Panel.inventory.items.clear();
			Panel.end=true;
			data.ChangeScenery(13);
			data.ChangeMusic(2);
		}
		
		data.Update();
	}
	
	@Override
	public void run() {
		double DrawInterwal = 1000000000/data.FPS;
		double NextDrawTime = System.nanoTime() + DrawInterwal;
		
		// game loop
		while (GameThread!=null) {
			Update();
			
			Panel.repaint();
			
			//FPS
			double RemainingTime = NextDrawTime-System.nanoTime();
			RemainingTime/=1000000;
			
			if(RemainingTime<0) RemainingTime=0;
			
			NextDrawTime+=DrawInterwal;
			
			try {Thread.sleep((long)RemainingTime);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		
	}
	
}

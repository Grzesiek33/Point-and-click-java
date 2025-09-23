import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Jpanel extends JPanel implements MouseListener, MouseMotionListener{
	
	//Objects
	Data data;
	Inventory inventory;
	Random random = new Random();
	
	//Varibles
	Rectangle languageButton = new Rectangle(1350, 20, 450, 40);
	boolean hoveredOnButton=false;
	boolean showInstrybutor=false;
	Image instrybutor, instrybutor2; 
	String instrybutorPath="graphics/interactions/instrybutor_off.png";
	String instrybutor2Path="graphics/interactions/instrybutor_on.png";
	
	//endgame
	boolean end=false;
	boolean hoveredEndButton=false;
	Rectangle endRec = new Rectangle(1000, 500, 230, 50);
	
	//menu
	boolean menu=true;
	boolean hoveredConButton=false;
	Rectangle conRec = new Rectangle(850, 300, 210, 50);
	
	boolean hoveredExitButton=false;
	Rectangle exitRec = new Rectangle(850, 500, 100, 50);
	
	//Colors
	Color colorWhite = new Color(100, 100, 100);
	Color colorOther = new Color(150, 100, 50);
	
	public Jpanel(Data data) {
		this.data = data;
		inventory = new Inventory(data);
		setCursor(data.cursor);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		try {
			instrybutor = ImageIO.read(new File(instrybutorPath));
			instrybutor2 = ImageIO.read(new File(instrybutor2Path));
		}catch (Exception e) {}
		
	}
	
public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setFont(data.customFont);
		
		//clearing whole screen
		g2.clearRect(0, 0, 2000, 2000);
		
		g2.drawImage(data.backgrounds.get(data.currentBackground).Background, 0, 0, 1920, 1080, this);
		
		//drawing background
		data.backgrounds.get(data.currentBackground).paintComponent(g2, this);
		
		if(data.backgrounds.get(data.currentBackground).storyOn&&!end) {
		if(!hoveredOnButton) g2.setColor(Color.lightGray);
		else g2.setColor(Color.YELLOW);
		g2.drawString("Language: "+data.language, 1350, 50);
		}
		
		//instrybutor
				if(showInstrybutor&&data.currentBackground==2) g.drawImage(instrybutor2, 0, 0, 1920, 1080, this);
				else if(data.currentBackground==2) g.drawImage(instrybutor, 0, 0, 1920, 1080, this);
		
		//Random noise
		for(int i=0; i<192; i++) {
			for(int j=0; j<108; j++) {
				int rand = random.nextInt(data.flashNoiseRandomness);
				int rand2 = random.nextInt(data.noiseRandomness);
				
				int randDefoult = random.nextInt(data.defoultBrightRandomness);
				int randDefoult2 = random.nextInt(data.defoultDarkRandomness);
				
				int alpha = rand2+data.darkness+(int)(Math.sqrt((Math.pow(96-i, 2)*0.56)+Math.pow(54-j, 2))*data.gradient);
				
				Rectangle pixel = new Rectangle(i*10, j*10, 1, 1);
				
				double dist=data.backgrounds.get(data.currentBackground).Distances[i][j];
				
				for(Item item:data.backgrounds.get(data.currentBackground).items) {
					if(item.rec.intersects(pixel)&&item.allowDist&&item.alphaDist[i][j]) dist = item.Distances[i][j];
				}
				
				for(Action action:data.backgrounds.get(data.currentBackground).actions) {
					
					if(action.done&&action.rec.intersects(pixel)&&action.allowDist&&action.alphaDist2[i][j]) dist = action.Distances2[i][j];
					if(!action.done&&action.rec.intersects(pixel)&&action.allowDist&&action.alphaDist[i][j]) dist = action.Distances[i][j];
				}
				
				
				int innerCirclepower = (int)(data.innerCirclePower*((double)dist/255));
				int innerCircleRadiusmax = (int)(data.innerCircleRadiousMax*((double)dist/255));
				
				int circlepower = (int)(data.flashPower*((double)dist/255));
				int circleRadius = (int)(data.flashRadious*((double)dist/255));
				
			
				int alphaInnerCircle = -(innerCirclepower)+rand+innerCircleRadiusmax-
						(int)((innerCirclepower)*((Math.abs(Math.sqrt(Math.pow(data.xmouse/10-i, 2)+Math.pow(data.ymouse/10-j, 2))-
								innerCircleRadiusmax*data.shift)*2/innerCircleRadiusmax))*data.innerCircleGradient)+(data.fR+data.fG+data.fB)/10;
						
				int alphaCircle = rand-(circlepower)+
						
				(int)((circlepower)*(Math.sqrt((Math.pow(data.xmouse/10-i, 2)+Math.pow(data.ymouse/10-j, 2)))/(circleRadius))*data.flashGradient)+(data.fR+data.fG+data.fB)/10;
				
				if(Math.sqrt(Math.pow(data.xmouse/10-i, 2)+Math.pow(data.ymouse/10-j, 2))<=innerCircleRadiusmax&&Math.sqrt(Math.pow(data.xmouse/10-i, 2)+Math.pow(data.ymouse/10-j, 2))>=data.innerCircleRadiousMin&&data.flashlightOn
						&&data.fancyFlashlight&&!data.flashlightBroken) {
							
					if(alpha+alphaInnerCircle<=255&&alpha+alphaInnerCircle>=0) {
						g2.setColor(new Color(data.R, data.G, data.B, alpha+alphaInnerCircle));
					} else if(alpha+alphaInnerCircle>=0) g2.setColor(new Color(data.R, data.G, data.B, 255));
					else g2.setColor(new Color(data.R, data.G, data.B, 0));
					
					g2.fillRect(i*10, j*10, 10, 10);
					
						if(alphaInnerCircle<=255&&alphaInnerCircle>=0) 
								g2.setColor(new Color(data.fR, data.fG, data.fB, alphaInnerCircle));
							
							else if(alphaInnerCircle>=0)
								if(data.defoultDark+randDefoult2<=255)
								g2.setColor(new Color(data.fR, data.fG, data.fB, data.defoultDark+randDefoult2));
								else g2.setColor(new Color(data.fR, data.fG, data.fB, 255));
								

							else if(alphaInnerCircle<=255)
								if(data.defoultBright+randDefoult>0)
								g2.setColor(new Color(data.fR, data.fG, data.fB, data.defoultBright+randDefoult));
								else g2.setColor(new Color(data.fR, data.fG, data.fB, 0));
						
						}
				
				else if(Math.sqrt(Math.pow(data.xmouse/10-i, 2)+Math.pow(data.ymouse/10-j, 2))<=circleRadius&&data.flashlightOn&&!data.flashlightBroken) {
					
					if(alpha+alphaCircle<=255&&alpha+alphaCircle>=0) {
						g2.setColor(new Color(data.R, data.G, data.B, alpha+alphaCircle));
					} else if(alpha+alphaCircle>=0) g2.setColor(new Color(data.R, data.G, data.B, 255));
					else g2.setColor(new Color(data.R, data.G, data.B, 0));
					
					g2.fillRect(i*10, j*10, 10, 10);
						
					if(alphaCircle<=255&&alphaCircle>=0) 
							g2.setColor(new Color(data.fR, data.fG, data.fB, alphaCircle));
						
						else if(alphaCircle>=0)
							if(data.defoultDark+randDefoult2<=255)
							g2.setColor(new Color(data.fR, data.fG, data.fB, data.defoultDark+randDefoult2));
							else g2.setColor(new Color(data.fR, data.fG, data.fB, 255));
							

						else if(alphaCircle<=255)
							if(data.defoultBright+randDefoult>0)
							g2.setColor(new Color(data.fR, data.fG, data.fB, data.defoultBright+randDefoult));
							else g2.setColor(new Color(data.fR, data.fG, data.fB, 0));
					
					}
						
						else if(alpha<=255) {
							g2.setColor(new Color(data.R, data.G, data.B, alpha));
						} else if(data.defoultDark+randDefoult2<=255) g2.setColor(new Color(data.R, data.G, data.B, data.defoultDark+randDefoult2));
					else g2.setColor(new Color(data.R, data.G, data.B, data.defoultDark+randDefoult2));
				
				g2.fillRect(i*10, j*10, 10, 10);
			}
		}
		
		inventory.paintComponent(g2, this);
		
		//drawing subtitles 
		data.writing=false;
		
		if(!data.writing)
			for(Item i:inventory.items) {
				if(i.hover) {
					g.setColor(colorOther);
					g.drawString(i.display, 100, 1000);
					data.writing=true;
					if(data.developerMode) {
						g.setColor(Color.white);
						g.drawString("id: "+i.id, 100, 150);
						g.drawString(i.iconEqPath.substring(15, i.iconEqPath.length()-7), 100, 200);
					}
				}
			}
		
		if(!data.writing)
			for(Action i:data.backgrounds.get(data.currentBackground).actions) {
				if(i.hover) {
					g.setColor(colorOther);
					g.drawString(i.display, 100, 1000);
					data.writing=true;
					
					if(data.developerMode) {
						g.setColor(Color.white);
						g.drawString("id: "+i.id, 100, 50);
						g.drawString(i.iconPath.substring(22, i.iconPath.length()-4), 100, 100);
						g.drawString(i.icon2Path.substring(22, i.icon2Path.length()-4), 100, 150);
						g.drawString("dist: "+i.allowDist, 100, 200);
						g.drawString("item needed: "+i.neededItem, 100, 250);
						g.drawString("item unlock: "+i.enableItem, 100, 300);
						g.drawString("req needed: "+i.idReq, 100, 350);
						g.drawString("req change: "+i.idReqChange, 100, 400);
						g.drawString("reversable: "+i.reversable, 100, 450);
						g.drawString("sounds: id-"+i.soundId+", name-"+data.sd.sounds.get(i.soundId).name, 100, 500);
						g.drawString("id-"+i.soundIdCant+", name-"+data.sd.sounds.get(i.soundIdCant).name, 100, 550);
					}
				}
			}
		
		if(!data.writing)
				for(Item i:data.backgrounds.get(data.currentBackground).items) {
					if(i.hover&&!i.display.equals("")) {
						g.setColor(colorOther);
						g.drawString(i.display, 100, 1000);
						data.writing=true;
						if(data.developerMode) {
							g.setColor(Color.white);
							g.drawString("id: "+i.id, 1400, 50);
							g.drawString(i.iconPath.substring(15, i.iconPath.length()-8), 1400, 100);
							g.drawString("pick: "+i.canBePicked, 1400, 150);
							g.drawString("dist: "+i.allowDist, 1400, 200);
							g.drawString("sounds:", 1400, 250);
							g.drawString("id-"+i.soundId+", name-"+data.sd.sounds.get(i.soundId).name, 1400, 300);
							g.drawString("id-"+i.soundIdCant+", name-"+data.sd.sounds.get(i.soundIdCant).name, 1400, 350);
						}
					}
				}
				
				
				
				if(!data.writing)
					for(Passage i:data.backgrounds.get(data.currentBackground).passages) {
						if(i.hover) {
							g.setColor(colorOther);
							g.drawString(i.display, 100, 1000);
							data.writing=true;
							
							if(data.developerMode) {
								g.setColor(Color.white);
								g.drawString("id: "+i.id, 1500, 750);
								g.drawString("req: "+i.idReq, 1500, 800);
								g.drawString("sounds: id-"+i.soundId, 1500, 850);
								g.drawString("name-"+data.sd.sounds.get(i.soundId).name, 1500, 900);
							}
							break;
						}
					}
				
				if(!data.writing)
					for(Puzzle i:data.backgrounds.get(data.currentBackground).puzzles) {
						if(i.hover) {
							g.setColor(colorOther);
							g.drawString(i.display, 100, 1000);
							data.writing=true;
							
							if(data.developerMode) {
								g.setColor(Color.white);
								g.drawString(i.iconPath.substring(22, i.iconPath.length()-4), 1400, 500);
								g.drawString(i.iconDonePath.substring(22, i.iconDonePath.length()-4), 1400, 550);
								g.drawString("id: "+i.id, 1400, 550);
								g.drawString("dist: "+i.allowDist, 1400, 600);
								g.drawString("item needed: "+i.neededItem, 1400, 650);
								g.drawString("req needed: "+i.idReq, 1400, 700);
								g.drawString("req change: "+i.idReqChange, 1400, 750);
								g.drawString("sounds:", 1400, 800);
								g.drawString("id-"+i.soundIdAction+", name-"+data.sd.sounds.get(i.soundIdAction).name, 1400, 850);
								g.drawString("id-"+i.soundIdDone+", name-"+data.sd.sounds.get(i.soundIdAction).name, 1400, 900);
							}
						}
					}
		
		for(Item i:inventory.items) {
			i.paintComponent(g2, this);
			}
		
		//developer mode
		if(data.developerMode) {
			g.setColor(Color.white);
			g.drawString("A: load language files", 100, 700);
			g.drawString("P/E: language: "+data.language, 100, 750);
			g.drawString(data.backgrounds.get(data.currentBackground).name, 100, 800);
			g.drawString("color: "+data.color[data.whichColor], 100, 850);
			g.drawString("UP/DOWN: "+data.sd.musics.get(data.whatMusic).name, 100, 900);
			g.drawString("1/2: free will: "+data.freeWill, 100, 950);
		}
		
		if(end) {
			g2.setFont(data.customFontSmall);
			data.endScreen.paintComponent(g2, this);
			g2.setFont(data.customFont);
			
			if(hoveredEndButton) g.setColor(Color.yellow);
			else g.setColor(Color.white);
			if(data.language.equals("Polish")) g.drawString("KONIEC", endRec.x, endRec.y+50);
			else g.drawString("ENDGAME", endRec.x, endRec.y+50);
		}
		
		if(menu) {
			g.setColor(new Color(0, 0, 0, 230));
			g.fillRect(0, 0, 1920, 1080);
			g.setColor(Color.white);
			g.drawString("MENU", 850, 50);
			
			if(!hoveredOnButton) g2.setColor(Color.lightGray);
			else g2.setColor(Color.YELLOW);
			g2.drawString("Language: "+data.language, 1350, 50);
			
			if(hoveredConButton) g.setColor(Color.yellow);
			else g.setColor(Color.white);
			
			if(data.language.equals("Polish")) g.drawString("Kontynuuj", conRec.x, conRec.y+50);
			else g.drawString("Continue", conRec.x, conRec.y+50);
			
			
			if(hoveredExitButton) g.setColor(Color.yellow);
			else g.setColor(Color.white);
			
			if(data.language.equals("Polish")) g.drawString("WyjdŸ", exitRec.x, exitRec.y+50);
			else g.drawString("Exit", exitRec.x, exitRec.y+50);
			
		}
		
		//i don't know what that means, but a guy from net uses this
		g2.dispose();
	}

	
	
	@Override
	public void mouseDragged(MouseEvent e) {
		data.xmouse = e.getX();
		data.ymouse = e.getY();
		data.mouse.setBounds(data.xmouse, data.ymouse, 1, 1);
		
		CheckHoverness();
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		data.xmouse = e.getX();
		data.ymouse = e.getY();
		data.mouse.setBounds(data.xmouse, data.ymouse, 1, 1);
		
		CheckHoverness();
		
	}
	
	public void CheckHoverness() {
		data.writing=false;
		
		if(!menu) {
		
		if(!data.backgrounds.get(data.currentBackground).storyOn) {
		
		if(!data.puzzleOn) {
		
		for(Action i:data.backgrounds.get(data.currentBackground).actions) {
			if(data.mouse.intersects(i.rec)) {
				data.writing=true;
				if(!i.hover) {
				i.Hovered();
				}
			}
			else if(!data.mouse.intersects(i.rec)&&i.hover) {i.NoHover();}
			}
		
		for(Item i:inventory.items) {
		if(data.mouse.intersects(i.rec)&&i.inPlace&&!data.writing) {
			data.writing=true;
			if(!i.hover) {
			i.Hovered();
			}
		}
		else if(!data.mouse.intersects(i.rec)&&i.hover) {i.NoHover();}
		}
			for(Item i:data.backgrounds.get(data.currentBackground).items) {
				if(data.mouse.intersects(i.rec)&&!data.writing) {
					data.writing=true;
					if(!i.hover) {
					i.Hovered();
					}
				}
				else if(!data.mouse.intersects(i.rec)&&i.hover) {i.NoHover();}
				}
			
			for(Passage i:data.backgrounds.get(data.currentBackground).passages) {
				if(data.mouse.intersects(i.rec)&&!data.writing) {
					data.writing=true;
					
					if(!i.hover) {
					i.Hovered();
					}
				}
				else if(!data.mouse.intersects(i.rec)&&i.hover) {i.NoHover();}
				}
			
			for(Puzzle i:data.backgrounds.get(data.currentBackground).puzzles) {
				if(data.mouse.intersects(i.rec)&&!data.writing) {
					data.writing=true;
					
					if(!i.hover) {
					i.Hovered();
					}
				}
				else if(!data.mouse.intersects(i.rec)&&i.hover) {i.NoHover();}
				}
		
		if(!data.writing) {
			setCursor(data.cursor);
		} else {
			setCursor(data.hover);
		}
		
		} else {
			for(Puzzle i:data.backgrounds.get(data.currentBackground).puzzles) 
				if(i.rec.intersects(data.mouse)&&i.lunched&&!i.innerRec.intersects(data.mouse)) {
						i.Hovered();
						setCursor(data.hover);
				}
				else if(i.CheckIfHover()) setCursor(data.hover);
				else {
					i.NoHover();
					setCursor(data.cursor);
				}
		}
		
		} else {
			
			if(!end) {
			if(data.mouse.intersects(languageButton)) hoveredOnButton=true;
			else hoveredOnButton=false;
				
			if((data.backgrounds.get(data.currentBackground).directionLeft>=0&&data.mouse.intersects(data.backgrounds.get(data.currentBackground).arrowLeft))
			 ||(data.backgrounds.get(data.currentBackground).directionRight>=0&&data.mouse.intersects(data.backgrounds.get(data.currentBackground).arrowRight))
			 ||(data.mouse.intersects(languageButton))) {
				setCursor(data.hover);
			} else setCursor(data.cursor);
			
			} else {
				if(data.mouse.intersects(endRec)) hoveredEndButton=true;
				else hoveredEndButton=false;
			}
			
		}
		
		} else {
			if(data.mouse.intersects(languageButton)) hoveredOnButton=true;
			else hoveredOnButton=false;
			
			if(data.mouse.intersects(conRec)) hoveredConButton=true;
			else hoveredConButton=false;
			
			if(data.mouse.intersects(exitRec)) hoveredExitButton=true;
			else hoveredExitButton=false;
			
			if(data.mouse.intersects(conRec)||data.mouse.intersects(exitRec)||data.mouse.intersects(endRec)) setCursor(data.hover);
			else setCursor(data.cursor);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		if(!end&&!menu) {
		
		if(!data.backgrounds.get(data.currentBackground).storyOn) {
		
		if(!data.puzzleOn) {
		
			boolean flag=true;
			
			for(Item i2:inventory.items) {
				if(!i2.inPlace||!i2.inEq) flag=false;
			}
			
			for(Passage i:data.backgrounds.get(data.currentBackground).passages) {
				if(data.mouse.intersects(i.rec)&&flag) {
					if((i.idReq>=0&&data.reqs[i.idReq])||i.idReq<0) {setCursor(data.cursor); i.hover=false; flag=false;}
					i.Clicked();
					break;
				}
			}

			for(Action i:data.backgrounds.get(data.currentBackground).actions) {
				if(data.mouse.intersects(i.rec)&&flag) {
					i.Clicked();
					flag=false;
					if(data.mouse.intersects(i.rec)) i.hover=true;
					else i.hover=false;
					break;
				}
			}
			
		for(Item i:data.backgrounds.get(data.currentBackground).items) {	
			if(data.mouse.intersects(i.rec)) {
				i.Clicked();
				if(i.canBePicked) {
					flag=false;
				setCursor(data.cursor);
				inventory.items.add(new Item(i.id, i.iconPath, i.iconEqPath, i.iconDistPath, i.x, i.y, i.width, i.height, i.data, i.allowDist, i.canBePicked, i.soundId, i.soundIdCant));
				inventory.items.get(inventory.items.size()-1).inEq=true;
				data.backgrounds.get(data.currentBackground).items.remove(i);
				break;
				}
			}
		}
		
		for(Puzzle i:data.backgrounds.get(data.currentBackground).puzzles) {
			if(data.mouse.intersects(i.rec)&&flag&&!i.done) {
				i.Clicked();
				setCursor(data.cursor);
				break;
			}
		}
		
		if(!data.itemInHand)
		for(Item i:inventory.items) {
			if(data.mouse.intersects(i.rec)&&i.inPlace) {
				data.itemId=i.id;
				data.itemInHand=true;
				i.inHand=true;
				
				for(int j=inventory.items.indexOf(i); j<inventory.items.size(); j++) {
					if(j+1<inventory.items.size())
					inventory.items.set(j, inventory.items.get(j+1));
					else
					inventory.items.set(j, i);
				}
				
				break;
			}
		}
		
		else 
		for(Item i:inventory.items) {
			if(i.inHand) {
				data.itemInHand=false;
				data.itemId=-1;
				i.inHand=false;
				i.inEq=false;
				i.xForFlaing=data.xmouse;
				i.yForFlaing=data.ymouse;
			}
			}
		
		} else {
			for(Puzzle i:data.backgrounds.get(data.currentBackground).puzzles) 
				if(i.rec.intersects(data.mouse)&&i.lunched) i.Clicked();
		}
		
		} else {
			if(data.backgrounds.get(data.currentBackground).directionLeft>=0&&data.mouse.intersects(data.backgrounds.get(data.currentBackground).arrowLeft)) {
				data.ChangeScenery(data.backgrounds.get(data.currentBackground).directionLeft);
			}
			
			if(data.backgrounds.get(data.currentBackground).directionRight>=0&&data.mouse.intersects(data.backgrounds.get(data.currentBackground).arrowRight)) {
				data.ChangeScenery(data.backgrounds.get(data.currentBackground).directionRight);
			}
			
			if(data.mouse.intersects(languageButton)) {
				if(data.language.equals("English")) data.ChangeLanguage("Polish");
				else data.ChangeLanguage("English");
			}
		}
		
		} else {
			if(data.mouse.intersects(languageButton)) {
				if(data.language.equals("English")) data.ChangeLanguage("Polish");
				else data.ChangeLanguage("English");
			}
			
			if(data.mouse.intersects(endRec)&&end) System.exit(0);
			
			if(data.mouse.intersects(exitRec)&&menu) System.exit(0);
			
			if(data.mouse.intersects(conRec)&&menu) menu=false;
		}
		CheckHoverness();
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}

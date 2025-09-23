import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Random;

public class Passage extends Interactions{

	//Objects
		Data data;
		
		//varibles
		int where, idReq;
		String subtitleCant;
		Random rand = new Random();
		int soundId;
	
	public Passage(Data data, int id, int x, int y, int width, int height, int where, int idReq, int soundId) {
		super();
		this.data = data;
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.where=where;
		this.idReq = idReq;
		this.soundId = soundId;
		this.id = id;
		
		rec.setBounds(x, y, width, height);
		
		ChangeLanguage(data.language);
		
	}
	
	public void Clicked() {
		if(idReq>=0&&!data.reqs[idReq]&&!data.itemInHand) {
			display=subtitleCant;
		} else if(idReq>=0&&!data.reqs[idReq]&&data.itemInHand) {
			display=data.information.get(rand.nextInt(data.information.size()));
		} else {
			data.sd.StartSound(soundId);
			data.ChangeScenery(where);
		}
	}
	
	public void Hovered() {
		hover = true;
		display=subtitle;
	}

	public void NoHover() {
		hover = false;
		display="";
	}
	
	public void ChangeLanguage(String which) {
		// File path is passed as parameter
        File file = new File("translations/"+data.language+"/passages/"+id+".txt");
 
        // Creating an object of BufferedReader class
        BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file, Charset.forName("UTF-16")));
		
			this.subtitle = br.readLine();
			this.subtitleCant = br.readLine();
 
		} catch (IOException e) {
			this.subtitle="file not found. Designated path: "+"translations/"+data.language+"/passages/"+id+".txt";
			this.subtitleCant="file not found. Designated path: "+"translations/"+data.language+"/passages/"+id+".txt";
			}
	}

}

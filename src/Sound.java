import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {

	Clip clip;
	int moment = 0, id;
	float level;
	float max;
    float min;
    
    String name;
	
	public Sound(int id, String name) {
		
		this.id = id;
		this.name=name;
		try
        {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File("sounds/"+name+".wav")));
            clip.setFramePosition(moment);
            
            ChangeVolume(50.0);
            clip.stop();
        }
        catch(Exception e) {}
	}

	public void ChangeVolume(Double ilechceprocent) {
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        
        max = gainControl.getMaximum();
        min = gainControl.getMinimum();
        level = (float)((max-min)*Math.log10(ilechceprocent/10) + min);
    	gainControl.setValue(level);
	}
	
	  public void Start()
	    {
		  clip.setFramePosition(0);
		  clip.start();
	    }
	  
	  public void Stop()
	    {
		  moment = clip.getFramePosition();
		  if(clip.isActive())
		    	clip.stop();
	    }
	
}

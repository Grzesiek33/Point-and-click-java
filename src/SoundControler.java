import java.util.ArrayList;

public class SoundControler {

	ArrayList<Music> musics = new ArrayList<Music>();
	ArrayList<Sound> sounds = new ArrayList<Sound>();
	
	ArrayList<String> musicsNames = new ArrayList<String>();
	ArrayList<String> soundsNames = new ArrayList<String>();
	   
	public SoundControler() {
		
		// MUSICS
		
		musicsNames.add("Nothing");
		musicsNames.add("forest");
		musicsNames.add("Rising_moon");
		musicsNames.add("Russian_music");
		
		for(int i=0; i<musicsNames.size(); i++)
		musics.add(new Music(musics.size(), musicsNames.get(musics.size())));
		
		// SOUNDS
		
		soundsNames.add("Nothing");
		soundsNames.add("walking");       //1
		soundsNames.add("car_door_open"); //2
		soundsNames.add("car_door_shut"); //3
		soundsNames.add("take");          //4
		soundsNames.add("cant_take");     //5
		soundsNames.add("door_open");     //6
		soundsNames.add("door_shut");     //7
		soundsNames.add("heavy_moving");  //8
		soundsNames.add("creak2");        //9
		soundsNames.add("knocking");      //10
		soundsNames.add("distributor");   //11
		soundsNames.add("car_door");      //12
		soundsNames.add("box_opened");    //13
		soundsNames.add("seat_belt");     //14
		soundsNames.add("fuel_pour");     //15
		soundsNames.add("glass_breaking");//16
		soundsNames.add("electricity_done");   //17
		soundsNames.add("electricity_action");   //18
		
		for(int i=0; i<soundsNames.size(); i++)
			sounds.add(new Sound(sounds.size(), soundsNames.get(sounds.size())));
		
	}
	
	 	public void StartSong(int id) {
	 		StopSong(-1);
	 		musics.get(id).Start();
	    }
	 	
	 	public void StartSong(String name) {
	 		StopSong(-1);
	 		for(Music m:musics) 
	 			if(m.name.equals(name)) {
	 				m.Start();
	 				break;
	 			}
	    }
	    
	    public void StartSound(int id) {
	    	StopSound(id);
	    	sounds.get(id).Start();
	    }
	    
	    public void StartSound(String name) {
	 		for(Sound s:sounds) 
	 			if(s.name.equals(name)) {
	 				StopSong(s.id);
	 				s.Start();
	 				break;
	 			}
	    }
	    
	    public void StopSong(int id) {	
	    	if(id>=0) musics.get(id).Stop();
	    	else for(int i=0; i<musicsNames.size(); i++)
	    		musics.get(i).Stop();
	    }
	    
	    public void StopSound(int id) {
				sounds.get(id).Stop();
	    }
	    
	    public void ChangeSongVolume(double procent) {
	    	if(procent>99.9)
	    		procent=99.9;
	    	if(procent<10)
	    		procent=10;
				for(int i=0; i<procent; i++) {
					musics.get(i).ChangeVolume(procent);
				}
	    }
	    
	    public void ChangeSoundVolume(double procent) {
	    	if(procent>99.9)
	    		procent=99.9;
	    	if(procent<10)
	    		procent=10;
				for(int i=0; i<procent; i++) {
					sounds.get(i).ChangeVolume(procent);
				}
	    }

}

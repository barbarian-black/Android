package com.example.whack;

import android.os.Handler;
import android.util.Log;
import android.widget.Button;

public class mole_handler {
	
	//image resource id's for animating coming mole
    private static final int mole_start[] = {
    		R.drawable.ani1, R.drawable.ani2, R.drawable.ani3, R.drawable.ani4,
    		R.drawable.ani5, R.drawable.ani6
    };
	
    //id of whacked image
    private int whacked_image = R.drawable.ani_whacked;
    
	private long MOLE_DISPLAYING_TIME = 200;
	private int image_index = 0;
	protected boolean is_clicked = false;
	private boolean is_mole_red = false;
	
	//Button which has to be handled
	Button mole;
	
	//handler for managing the execution thread
	Handler hand =  new Handler();
	
    //constructor for mole_handler
	public mole_handler(Button mole){
		this.mole = mole;
	}
	
	public void animate_mole(){
		mole.setClickable(true);
		image_index = 0;
		hand.post(start);
	}
	
	/**
	 * Handler for animating mole coming
	 */
	Runnable start = new Runnable(){
		public void run(){
			
			mole.setBackgroundResource(mole_start[image_index]);
			image_index++;
			
			if(image_index >= mole_start.length)
				stop();
			else 
			    hand.postDelayed(start, MOLE_DISPLAYING_TIME);
		}
	};
	
	public void animate_mole_exit(){
		
		 mole.setBackgroundResource(R.drawable.ani0);
		 mole.setClickable(false);
		 hand.removeCallbacks(start);
		//image_index = 0;
		//hand.post(exit);
	}
		
	/*
	 * Function for stopping
	 */
	public void stop(){
		
		try{
		  hand.removeCallbacks(start);
		}catch(Exception e){
			Log.d("Handler Error","Error in stopping comming thread");
		}
		
		animate_mole_exit();
		
		if(!is_clicked){
			Game_activity.dec_max_left();
		}
	}
	
	/**
	 * Function when mole is whacked
	 * @return 
	 */
	public void mole_whacked(){
		is_clicked = true;
		mole.setBackgroundResource(whacked_image);
		hand.postDelayed(whacked,100);
	}
	
	public boolean is_red(){
		return is_mole_red;
	}
	
	Runnable whacked = new Runnable(){
		public void run(){
			animate_mole_exit();
		}
	};
	
}



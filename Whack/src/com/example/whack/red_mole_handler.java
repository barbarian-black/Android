package com.example.whack;

import android.os.Handler;
import android.util.Log;
import android.widget.Button;

public class red_mole_handler extends mole_handler{
	
	public red_mole_handler(Button mole) {
		super(mole);
		this.mole = mole;
	}

	//image resource id's for animating coming mole
    private static final int mole_start[] = {
    		R.drawable.ani_red1, R.drawable.ani_red2, R.drawable.ani_red3, R.drawable.ani_red4,
    		R.drawable.ani_red5, R.drawable.ani_red6
    };
	
    //id of whacked image
    private int whacked_image = R.drawable.ani_whacked;
    
	private long MOLE_DISPLAYING_TIME = 200;
	private int image_index = 0;
	private boolean is_mole_red = true;

	
	//Button which has to be handled
	Button mole;
	
	//handler for managing the execution thread
	Handler hand =  new Handler();
	
	@Override
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
	
		
	/*
	 * Function for stopping
	 */
	@Override
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
	 */
	@Override
	public void mole_whacked(){
		is_clicked = true;
		mole.setBackgroundResource(whacked_image);
		hand.postDelayed(whacked,100);
		
	}
	
	Runnable whacked = new Runnable(){
		public void run(){
			animate_mole_exit();
		}
	};
	
	public boolean is_red(){
		return is_mole_red;
	}
	
}

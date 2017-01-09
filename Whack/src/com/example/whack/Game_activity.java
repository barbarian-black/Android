package com.example.whack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "NewApi", "UseSparseArrays" })
public class Game_activity extends Activity {

	//defining the speed modes for the mole
	private static final int SPEED_SLOW = 0;
	private static final int SPEED_MEDIUM = 1;
	private static final int SPEED_FAST = 2;
	private static final int SPEED_SUPER_FAST = 3;
	
	//Frequently used Views of the game Activity
    private TextView game_score , timer;
	private static TextView moles_left;
    
    //Random number generator
    private Random num ;
    
    //Time constants
	private long GET_TIME_INTERVAL = 1000; //1s
	private static final long GET_TIME_INTERVAL_UPDATE_VALUE = 1000;
	private final long TIME_MAX = 60000 ; // 1min
	
	//Handlers
	SharedPreferences sharedPreference ;
    Handler hand = new Handler();
    Handler time = new Handler();

	//boolean variable to store whether the extreme mode is on or not
    boolean is_extreme = false , is_time_attack=false , is_mole_miss = false;
    
    //handlers for each buttons
	mole_handler[] e = new mole_handler[12];
	
	Button mole;
	Map<Integer, Integer> buttons = new HashMap<Integer, Integer>();
	Map<Integer, Integer> button_rev = new HashMap<Integer, Integer>();
	
	//score for the game
	int score = 0;
    	
	 //maximum no. of moles that can be left
	static int max_left;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_activity);
		
		sharedPreference = getSharedPreferences(Splash.SETTINGS,MODE_PRIVATE);
		
		game_score = (TextView)findViewById(R.id.game_score);
		timer = (TextView)findViewById(R.id.time);
		moles_left = (TextView)findViewById(R.id.moles_left);
		
		time.postDelayed(trun,GET_TIME_INTERVAL_UPDATE_VALUE);
		hand.post(mrun);
		
		initialize();
	}

	/**
	 * Setting the initial requirement for the game
	 */
	public void initialize(){
		
		String mode = sharedPreference.getString(Splash.MODE, Splash.TIME_ATTACK);
		if(mode.matches(Splash.EXTREME))
			is_extreme = true;
		else if(mode.matches(Splash.MOLE_MISS))
			is_mole_miss = true;
		else
			is_time_attack = true;
			
		buttons.put(1,R.id.hole1);   button_rev.put(R.id.hole1,1);
		buttons.put(2,R.id.hole2) ;  button_rev.put(R.id.hole2,2);
		buttons.put(3,R.id.hole3);   button_rev.put(R.id.hole3,3);
		buttons.put(4,R.id.hole4);   button_rev.put(R.id.hole4,4);
		buttons.put(5,R.id.hole5);   button_rev.put(R.id.hole5,5);
		buttons.put(6,R.id.hole6);   button_rev.put(R.id.hole6,6);
		buttons.put(7,R.id.hole7);   button_rev.put(R.id.hole7,7);
		buttons.put(8,R.id.hole8);   button_rev.put(R.id.hole8,8);
		buttons.put(9,R.id.hole9);   button_rev.put(R.id.hole9,9);
		buttons.put(10,R.id.hole10); button_rev.put(R.id.hole10,10);
		buttons.put(11,R.id.hole11); button_rev.put(R.id.hole11,11);
		buttons.put(12,R.id.hole12); button_rev.put(R.id.hole12,12);
		
		max_left = Integer.parseInt(moles_left.getText().toString());
		
	}
	
	/**
	 * Setting animation for the random hole
	 */
	public void show_mole(int n , boolean mode){

		mole = (Button)findViewById(buttons.get(n));

		if(mode)
		  e[n-1] = new red_mole_handler(mole);
		else
		  e[n-1] = new mole_handler(mole);
		
		e[n-1].animate_mole();
	}
	
	
	/**
	 * Randomly shows the mole
	 */
	public void random_show(){
		
		boolean m = false;
		num = new Random();
		// range from [1,12]
		int number = num.nextInt(12)+1;
		
		if(is_extreme)
			m = num.nextBoolean();

		show_mole(number, m);
		
	}
	
	/**
	 * update the score after every click
	 */
	public void update_score(View view){
        int n = button_rev.get(view.getId());
		
        e[n-1].mole_whacked();
		//return true if red mole is whacked
		if(e[n-1].is_red()){
			exit();
			Toast.makeText(this, "Wrong mole hitted", Toast.LENGTH_SHORT).show();
		}
				
		score++;
        game_score.setText(""+score);
        
	}	
	
	public void exit(){
		
		int max_score = sharedPreference.getInt(Splash.MAX_SCORE, 0);
		
		if(max_score<score){
			SharedPreferences.Editor edit = sharedPreference.edit();
			edit.putInt(Splash.MAX_SCORE, score);
			edit.commit();
		}
		
		hand.removeCallbacks(mrun);
		time.removeCallbacks(trun);
		
		for(int i=0; i<12; i++){
			if(e[i]!=null)
			 e[i].stop();
		}	
		
		finish();
		
		//calling game_end_activity
		Intent end_game = new Intent(this, Game_Exit_Activity.class);
		end_game.putExtra(Game_Exit_Activity.GAME_SCORE, score);
		startActivity(end_game);
		
	}
	
	Runnable mrun = new Runnable(){
		boolean stop = false;
		
		@Override
		public void run(){
			
			random_show();
			GET_TIME_INTERVAL = get_time_interval(score);
			
			if(is_mole_miss)
			   if(max_left<=0){
				   exit();
                   stop = true;
			   }
			
			if(!stop)
				hand.postDelayed(mrun, GET_TIME_INTERVAL);
			
			
		}
	};
	
	Runnable trun = new Runnable(){
		
		boolean stop = false;
		@Override
		public void run(){
			
			String time_value[] = timer.getText().toString().split(":");
			int seconds = Integer.parseInt(time_value[1]);
			long new_time = seconds + GET_TIME_INTERVAL_UPDATE_VALUE;
			String update_time = "00:"+new_time;
			timer.setText(update_time);
			
			if(is_time_attack)
				if(new_time >= TIME_MAX){
					stop = true;
					exit();
				}	
			
			if(!stop)
				time.postDelayed(trun, GET_TIME_INTERVAL_UPDATE_VALUE);
			
		}
	};
	
	
	/**
	 * Function for setting intervals between 2 mole appearance
	 */
     public long get_time_interval(int score){
    	
    	if(score>=0 && score<=10)
    	   return 900;

    	else if(score>10 && score<=15)
    	   return 800;
    	
    	else if(score>15 && score<=20)
    	   return 700;
    	
    	else if(score>20 && score<=25)
        	return 600;
        
    	else if(score>25 && score<=30)
        	return 400;
        
    	else if(score>30 && score<=35)
        	return 300;
               
    	 return 250; 
    }
     
   /**
    * Function for decreasing max_left
    */
     public static void dec_max_left(){
    	 max_left--;
    	 moles_left.setText(""+max_left);
    	 
     }


}

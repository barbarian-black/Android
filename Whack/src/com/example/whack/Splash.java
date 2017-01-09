package com.example.whack;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Splash extends Activity {

	//File and Attributes name
	public static final String SETTINGS = "setting file";
	public final static String MODE = "game modes";
	public static final String MAX_SCORE = "maximum score for the game";
	
	//Game Mode Settings
	public static final String TIME_ATTACK = "normal";
	public static final String MOLE_MISS = "hard";
	public static final String EXTREME = "extreme";
	
	//views
	//TextView for showing the maximum score of the game
	private TextView max_score;
	
	//Home Screen content View
	private RelativeLayout home;
	
	//RadioGroup for showing the modes
	RadioGroup mode_options;
	
	//Radio Button for getting the saved choice
	RadioButton mode_selected;
		
	//SharedPreferences for storing the preferences
	SharedPreferences sharedpreference;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_splash);
		
		max_score = (TextView)findViewById(R.id.max_score);
		home = (RelativeLayout)findViewById(R.id.home_content);
		mode_options = (RadioGroup)findViewById(R.id.mode_options);
		sharedpreference = getSharedPreferences(SETTINGS,MODE_PRIVATE);
		
		set_max_score();	
	}
	
	//transfer game activity control from home screen to main game activity
	public void start_game(View view){
		Intent gameActivity = new Intent(this,Game_activity.class);
		startActivity(gameActivity);
	}
	
	
	/**
	 * change mode
	 */
	public void change_mode(View view){

		//setting the home opacity low to focus modes
		home.setAlpha((float)0.3);
		
		//making mode view available
		findViewById(R.id.show_mode).setVisibility(View.VISIBLE);
		
		
		String mode = sharedpreference.getString(MODE, TIME_ATTACK);
		
		if(mode.matches(EXTREME))
			mode_selected = (RadioButton)findViewById(R.id.mode3);
		
		else if(mode.matches(MOLE_MISS))
			mode_selected = (RadioButton)findViewById(R.id.mode2);
		
		else 
			mode_selected = (RadioButton)findViewById(R.id.mode1);
		
		mode_selected.setChecked(true);

	}
	
	
	/**
	 * save mode in setting file
	 */
	public void save_mode(View view){
		
		String choice = null;
		
		switch(mode_options.getCheckedRadioButtonId()){
		   case R.id.mode1: choice = TIME_ATTACK;
			   break;
		   case R.id.mode2: choice = MOLE_MISS;
			   break;
		   case R.id.mode3: choice = EXTREME;
			   break;
		}
		
		SharedPreferences.Editor editor = sharedpreference.edit();
		editor.putString(MODE,choice);
		editor.commit();
		
		hide_mode();
	}
	
	/**
	 * Hide mode options
	 */
	public void hide_mode(){
		home.setAlpha(1f);
		findViewById(R.id.show_mode).setVisibility(View.GONE);
	}
	
	/**
	 * Setting max score on the home screen
	 */
	public void set_max_score(){
		max_score.setText(""+sharedpreference.getInt(MAX_SCORE, 0));
	}
	
	@Override
	public void onResume(){
		super.onResume();;
		set_max_score();
	}
	
    public void start_camera(View view){
    	Toast.makeText(this, "This is not working", Toast.LENGTH_SHORT).show();
    	//Intent capture_face = new Intent(this,Capture_face.class);
    	//startActivity(capture_face);
    }
}


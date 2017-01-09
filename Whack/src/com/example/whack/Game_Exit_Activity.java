package com.example.whack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Game_Exit_Activity extends Activity {

	public final static String GAME_SCORE = "score of the game";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game__exit_);
		
		Intent recieve = getIntent();
		int score = recieve.getIntExtra(GAME_SCORE, 0);
		TextView score_field = (TextView)findViewById(R.id.score_field);
		score_field.setText(""+score);
	}
    
	
	public void start_game(View view){
		finish();
		Intent start_game = new Intent(this,Game_activity.class);
		startActivity(start_game);
	}
	
	public void change_mode(View view){
		
	}
	
	public void goback_splash(View view){
		finish();
	}
}

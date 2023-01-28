package com.petrunnel.memoria;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class MemoriaActivity extends Activity {
	
	private GridView mGrid;
	private GridAdapter mAdapter;
	private TextView mStepScreen;
	private Chronometer mTimeScreen;
	
	private Integer StepCount;
	int GRID_SIZE = 6;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Настройки
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String PictureCollection = settings.getString("PictureCollection", "animal");
        int BackgroundColor = Color.parseColor(settings.getString("BackgroundColor", "black"));
        
        mTimeScreen = (Chronometer) findViewById(R.id.timeview);
        mStepScreen = (TextView)findViewById(R.id.stepview);

        StepCount = 0;
        mStepScreen.setText (StepCount.toString());
        
        mTimeScreen.start();
        
        mGrid = (GridView)findViewById(R.id.field);
        View root = mGrid.getRootView();
        root.setBackgroundColor(BackgroundColor);
        mGrid.setEnabled(true);
        
        mAdapter = new GridAdapter(this, GRID_SIZE, GRID_SIZE, PictureCollection);
        mGrid.setAdapter(mAdapter);
        
        mGrid.setOnItemClickListener((parent, v, position, id) -> {

            mAdapter.checkOpenCells ();
            if (mAdapter.openCell (position)) {
                StepCount ++;
                mStepScreen.setText (StepCount.toString());
            }

            if (mAdapter.checkGameOver())
            {
                mTimeScreen.stop();
                ShowGameOver();
            }
});
    }
    
  
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
      if (newConfig.orientation ==  Configuration.ORIENTATION_LANDSCAPE)
    	  mGrid.setNumColumns(9);
      if (newConfig.orientation ==  Configuration.ORIENTATION_PORTRAIT)
    	  mGrid.setNumColumns(6);
    }
    
    private void ShowGameOver () {
    	
    	String time = mTimeScreen.getText().toString();
    	
    	// Читаем файл с рекордами
    	RecordAdapter ra = new RecordAdapter (this);
    	// Добавляем новые значения
    	ra.addPoint(StepCount);
    	ra.addTime(time);
    	// Записываем рекорды в файл
    	ra.WriteRecords();
    	
    	// Диалоговое окно
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);

        // Заголовок и текст
        alertbox.setTitle("Поздравляем!");
        String TextToast = "Игра закончена \nХодов: " + StepCount.toString() + "\nВремя: " + time;
        alertbox.setMessage(TextToast);
        
        // Добавляем кнопку 
        alertbox.setNeutralButton("Ok", (arg0, arg1) -> {
            // закрываем текущюю Activity
            finish();
        });

        // показываем окно
        alertbox.show();
    	
    }
}
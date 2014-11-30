package com.example.illuminancesenser;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {

	private SensorManager sm;
	private TextView brightnessSensorValue;
	private Button btnStartService;
	private Button btnKillService;
	public static MainActivity context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		brightnessSensorValue = (TextView) findViewById(R.id.value);
		btnStartService = (Button) findViewById(R.id.start_service);
		btnStartService.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				if (LuxService.luxService == null) {
					Toast.makeText(getApplicationContext(), "サービスを起動します", Toast.LENGTH_SHORT).show();
					startService(new Intent(getBaseContext(), LuxService.class));
				} else {
					Toast.makeText(getApplicationContext(), "サービスがすでに起動されています", Toast.LENGTH_SHORT).show();
				}
			}
		});
		btnKillService = (Button) findViewById(R.id.kill_service);
		btnKillService.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				if (LuxService.luxService != null) {
					Toast.makeText(getApplicationContext(), "サービスを停止します", Toast.LENGTH_SHORT).show();
					stopService(new Intent(getBaseContext(), LuxService.class));
				} else {
					Toast.makeText(getApplicationContext(), "サービスが起動されていません", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	// センサーの値が変更されたに呼び出される。
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO 自動生成されたメソッド・スタブ
		String str;
		if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
			str = "照度:" + event.values[0];
			brightnessSensorValue.setText(str);
		}
	}

	// センサーの精度が変更されたときに呼び出される。
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	protected void onStop() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStop();
		sm.unregisterListener(this);
	}

	@Override
	protected void onResume() {
		// TODO 自動生成されたメソッド・スタブ
		super.onResume();
		List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_LIGHT);
		if (sensors.size() > 0) {
			Sensor s = sensors.get(0);
			sm.registerListener(this, s, sm.SENSOR_DELAY_FASTEST);
		}
	}

}

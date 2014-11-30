package com.example.illuminancesenser;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
/**
 *
 * @author meem
 * @see
 * 初期値が閾値以下だとバイブレーションされない＆止まらない
 */
public class LuxService extends Service implements SensorEventListener {

	public static LuxService luxService = null;
	private Vibrator vib;
	private SensorManager sm;
	private long[] pattern = {0, 200, 300, 200, 300, 600, 500}; // 休憩時間,振動時間・・・

	@Override
	public void onCreate() {
		// TODO 自動生成されたメソッド・スタブ
		Log.d(getPackageName(), "onCreate");
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_LIGHT);
		if (sensors.size() > 0) {
			Sensor s = sensors.get(0);
			sm.registerListener(this, s, SensorManager.SENSOR_DELAY_FASTEST);
		}
		super.onCreate();
	}
	@Override
	public void onDestroy() {
		// TODO 自動生成されたメソッド・スタブ
		sm.unregisterListener(luxService);
		if (luxService != null)
			luxService = null;
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO 自動生成されたメソッド・スタブ
		Log.d(getPackageName(), "onStartCommand");
		luxService = this;
		vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void unbindService(ServiceConnection conn) {
		// TODO 自動生成されたメソッド・スタブ
		super.unbindService(conn);
		MainActivity.context.unbindService(conn);
	}

	// センサーの値が変更されたに呼び出される。
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO 自動生成されたメソッド・スタブ
		if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
			if (event.values[0] < 1000) {
				Log.d(getPackageName(), "onSensorChanged");
				vib.vibrate(pattern, 0);
			} else {
				vib.cancel();
			}
		}
	}

	// センサーの精度が変更されたときに呼び出される。
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO 自動生成されたメソッド・スタブ
	}

}

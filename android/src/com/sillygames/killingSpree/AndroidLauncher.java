package com.sillygames.killingSpree;

import android.os.Bundle;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.sillygames.killingSpree.KillingSpree;
import com.sillygames.killingSpree.PlatformServices;

public class AndroidLauncher extends AndroidApplication implements PlatformServices {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new KillingSpree(this), config);
	}

    @Override
    public void toast(final String message) {
        runOnUiThread(new Runnable() {
            public void run() {
              Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
          });
    }

    @Override
    public void shortToast(final String message) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

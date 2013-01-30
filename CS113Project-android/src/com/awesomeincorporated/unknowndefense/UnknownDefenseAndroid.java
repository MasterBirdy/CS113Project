package com.awesomeincorporated.unknowndefense;

import android.os.Bundle;

import com.awesomeincorporated.unknowndefense.UnknownDefense;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class UnknownDefenseAndroid extends AndroidApplication {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        
        initialize(new UnknownDefense(), cfg);
    }
}
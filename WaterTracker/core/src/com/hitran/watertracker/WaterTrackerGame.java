package com.hitran.watertracker;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hitran.watertracker.screen.MainScreen;

import java.util.Calendar;

public class WaterTrackerGame extends Game {
    // Constants
    public static final int WIDTH = 960;
    public static final int HEIGHT = 1600;

    private SpriteBatch batch;
    private Preferences prefs;

    @Override
    public void create() {
        batch = new SpriteBatch();

        prefs = Gdx.app.getPreferences("WaterTracker");

        Calendar cal = Calendar.getInstance();
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        if (!prefs.contains("dayOfMonth") || prefs.getInteger("dayOfMonth") != dayOfMonth) {
            prefs.putInteger("dayOfMonth", dayOfMonth);

            emptyGlasses();

            prefs.flush();
        }

        setScreen(new MainScreen(this));
    }

    private void emptyGlasses() {
        for (int i = 0; i < 8; i++) {
            prefs.putBoolean("isEmptyGlass" + i, true);
        }
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    }
}

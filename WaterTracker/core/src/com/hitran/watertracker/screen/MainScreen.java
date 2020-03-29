package com.hitran.watertracker.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hitran.watertracker.WaterTrackerGame;

import java.util.ArrayList;
import java.util.List;


public class MainScreen implements Screen {

    private final WaterTrackerGame game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;

    private Preferences prefs;

    private float viewportX, viewportY, viewportWidth, viewportHeight;

    private Texture emptyGlass, fullGlass;

    private List<Sprite> glasses;
    private List<Rectangle> buttons;

    public MainScreen(WaterTrackerGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        Vector2 size = Scaling.fit.apply(WaterTrackerGame.WIDTH, WaterTrackerGame.HEIGHT, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewportX = (Gdx.graphics.getWidth() - size.x) / 2;
        viewportY = (Gdx.graphics.getHeight() - size.y) / 2;
        viewportWidth = size.x;
        viewportHeight = size.y;

        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(WaterTrackerGame.WIDTH, WaterTrackerGame.HEIGHT, gameCam);
        gamePort.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        prefs = Gdx.app.getPreferences("WaterTracker");

        fullGlass = new Texture(Gdx.files.internal("full_glass.png"));
        emptyGlass = new Texture(Gdx.files.internal("empty_glass.png"));

        glasses = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            glasses.add(new Sprite(prefs.getBoolean("isEmptyGlass" + i) ? emptyGlass : fullGlass));
        }

        // Set up position of glasses
        float spriteWidth = glasses.get(0).getWidth();
        float spriteHeight = glasses.get(0).getHeight();

        glasses.get(0).setPosition(70, 80);
        glasses.get(1).setPosition(glasses.get(0).getX() + spriteWidth + 70, 80);
        glasses.get(2).setPosition(glasses.get(1).getX() + spriteWidth + 70, 80);
        glasses.get(3).setPosition(70, glasses.get(0).getY() + spriteHeight + 80);
        glasses.get(4).setPosition(glasses.get(3).getX() + spriteWidth + 70, glasses.get(1).getY() + spriteHeight + 80);
        glasses.get(5).setPosition(glasses.get(4).getX() + spriteWidth + 70, glasses.get(1).getY() + spriteHeight + 80);
        glasses.get(6).setPosition(230, glasses.get(4).getY() + glasses.get(4).getHeight() + 80);
        glasses.get(7).setPosition(glasses.get(6).getX() + spriteWidth + 70, glasses.get(4).getY() + spriteHeight + 80);

        buttons = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            buttons.add(new Rectangle(glasses.get(i).getX(), glasses.get(i).getY(), spriteWidth, spriteHeight));
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        game.getBatch().setProjectionMatrix(gameCam.combined);

        game.getBatch().begin();

        for (Sprite glass : glasses) {
            glass.draw(game.getBatch());
        }

        game.getBatch().end();
    }

    private void update(float delta) {
        gameCam.update();

        handleInput(delta);
    }

    private void handleInput(float delta) {
        if (Gdx.input.justTouched()) {
            Vector3 press = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            Vector3 camCoordinates = gameCam.unproject(press, viewportX, viewportY, viewportWidth, viewportHeight);

            for (int i = 0; i < 8; i++) {
                if (buttons.get(i).contains(camCoordinates.x, camCoordinates.y)) {
                    boolean newValue = !prefs.getBoolean("isEmptyGlass"+i);
                    prefs.putBoolean("isEmptyGlass"+i, newValue);
                    prefs.flush();

                    glasses.get(i).setTexture(newValue ? emptyGlass : fullGlass);
                }
                
            }

        } else if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            this.dispose();
            game.dispose();
            System.exit(0);
        }
    }

    @Override
    public void resize(int width, int height) {
        Vector2 size = Scaling.fit.apply(WaterTrackerGame.WIDTH, WaterTrackerGame.HEIGHT, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewportX = (width - size.x) / 2;
        viewportY = (height - size.y) / 2;
        viewportWidth = size.x;
        viewportHeight = size.y;
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        fullGlass.dispose();
        emptyGlass.dispose();
    }
}

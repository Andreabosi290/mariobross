package com.mario.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mario.game.MarioGame;

public class GameOver implements Screen {

    public Viewport viewport;
    public Stage stage;

    public Game game;

    public GameOver(Game game){
        this.game = game;
        viewport = new FitViewport(MarioGame.WORLD_WIDTH,MarioGame.WORLD_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport,((MarioGame) game).batch);

        Table table = new Table();
        table.center(); // setta al centro il table
        table.setFillParent(true); // setta il table della dimensione dello schermo
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        table.add(new Label("GAME OVER",font)).expandX();
        table.row();
        table.add(new Label("Click to PLAY AGAIN",font)).expandX().padTop(10f);
        stage.addActor(table);

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isTouched()){
            dispose();
            game.setScreen(new PlayScreen((MarioGame) game));
        }

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
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
        stage.dispose();
    }
}

package com.mario.game.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mario.game.MarioGame;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private int WorldTime;
    private float TimeCount;


    Label ScoreLabel;
    Label TimeLabel;
    Label WorldLabel;
    Label LevelLabel;
    Label CountDownLabel;
    Label MarioLabel;


    public Hud(Batch batch) {

        WorldTime = 300;
        TimeCount = 0;
        viewport = new FitViewport(MarioGame.WORLD_WIDTH,MarioGame.WORLD_HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport,batch);


        //creo un oggetto table per contenere le label e metterle nella giusta posizione
        Table table = new Table();
        table.top();                        // fa partire il table dal alto
        table.setFillParent(true);          //table della stessa dimensione dello schermo

        CountDownLabel = new Label(String.format("%03d",WorldTime),new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        TimeLabel = new Label(String.format("TIME",WorldTime),new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        WorldLabel = new Label("WORLD",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        LevelLabel = new Label("1-1",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        ScoreLabel = new Label(String.format("%06d", MarioGame.score),new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        MarioLabel = new Label("MARIO",new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        //aggiungo le label alla table

        table.add(MarioLabel).expandX().padTop(10); //aggiungo mario alla prima riga della table, con expand X si estende per occupare tutta
                                                    //la linea, ma se ne inserisco di più sulla stessa linea queste si adatteranno in automatico
                                                    //pad top vuol dire distanza dal top
        table.add(WorldLabel).expandX().padTop(10);
        table.add(TimeLabel).expandX().padTop(10);

        //aggiungo una linea alla table, d'ora in poi tutto quello che aggiungo sarà nella linea sotto
        table.row();
        table.add(ScoreLabel).expandX();
        table.add(LevelLabel).expandX();
        table.add(CountDownLabel).expandX();

        //aggiungo allo stage l'attore table
        stage.addActor(table);
    }
    public void update(float deltaTime){
        TimeCount += deltaTime;
        if(TimeCount >= 1){
            WorldTime --;
            CountDownLabel.setText(String.format("%03d",WorldTime));
            TimeCount -= 1;
        }
        ScoreLabel.setText(String.format("%06d", MarioGame.score));
    }



    @Override
    public void dispose() {
        stage.dispose();
    }
}

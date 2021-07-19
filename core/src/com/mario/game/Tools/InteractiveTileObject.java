package com.mario.game.Tools;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mario.game.MarioGame;
import com.mario.game.Screens.PlayScreen;
import com.mario.game.Sprites.Mario;

import java.util.Scanner;


public abstract class InteractiveTileObject {
    protected World world;
    protected Body body;
    protected TiledMap map;
    protected Rectangle rect;
    protected BodyDef bodyDef;
    protected PolygonShape Shape;
    protected FixtureDef fixtureDef;
    protected Fixture fixture;
    protected PlayScreen screen;
    protected RectangleMapObject objects;

    public InteractiveTileObject(PlayScreen screen, RectangleMapObject objects, BodyDef bodyDef, PolygonShape Shape, FixtureDef fixtureDef) {

        this.screen = screen;
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.objects = objects;
        this.rect = objects.getRectangle();
        this.bodyDef = bodyDef;
        this.Shape = Shape;
        this.fixtureDef = fixtureDef;

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((rect.getX() + rect.getWidth()/2)/ MarioGame.PPM, (rect.getY() + rect.getHeight()/2)/ MarioGame.PPM);
        body = world.createBody(bodyDef);
        Shape.setAsBox(rect.getWidth()/2/ MarioGame.PPM,rect.getHeight()/2/ MarioGame.PPM);
        fixtureDef.shape = Shape;
        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);//serve per "chiamare" la fixture con il nome della classe, es Coins: -> [Coins] (lo vedo se faccio la println)
    }
    public abstract void OnHeadHit(Mario mario);

    //metodo per settare il filtro ad un valore che viene passato, es: DESTROYED_BIT, passando il filtro e la fixture alla chiamata (vedi coins e bricks)
    public void setCategoryFilter(short filterbit){
        Filter filter = new Filter();
        filter.categoryBits = filterbit;
        fixture.setFilterData(filter);
    }
//metodo che restituisce una cella della tiled map, mi serve per poter poi modificare quella cella, ad esempio rimuovendola se l'oggetto è stato distrutto
    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1); //prendiamo i layer dalla mappa, rispettivamente gli el del layer 0 (quello grafico)
        return layer.getCell((int) (body.getPosition().x * MarioGame.PPM / 16),(int) (body.getPosition().y * MarioGame.PPM / 16));
        //voglio ritornare la cella, ma dato che la tiled map non è scalata, mentre il gioco si, dovremo prima riscalare alla dim originale, e poi dividere per la dimensione di ciascun
        //blocco nella tiled map, cioè 16
    }
}

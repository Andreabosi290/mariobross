package com.mario.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mario.game.MarioGame;
import com.mario.game.Screens.PlayScreen;
import com.mario.game.Sprites.Items.ItemDef;
import com.mario.game.Sprites.Items.Mushroom;
import com.mario.game.Sprites.Mario;

public class Coins extends InteractiveTileObject{

//    private static TiledMapTileSet tileset;
    private int numofhits = 0;

    public Coins(PlayScreen screen, RectangleMapObject objects, BodyDef bodyDef, PolygonShape Shape, FixtureDef fixtureDef) {
        super(screen, objects, bodyDef, Shape, fixtureDef);
        //tileset = map.getTileSets().getTileSet("tileset_gutterr"); ho definito un tileset a quello di nome "tileset_gutter" che si trova nel file tmx map
        setCategoryFilter(MarioGame.COIN_BIT); //settiamo il filtro a quello di COIN
    }

    @Override
    public void OnHeadHit(Mario mario) {
        Gdx.app.log("Coin","Collision");
        if(numofhits < 3){
            MarioGame.score += 150;
            numofhits ++;
            MarioGame.manager.get("audios/sounds/coinsound.wav", Sound.class).play();
               }
        else{
            if(numofhits == 3) {
                getCell().setTile(null);
                numofhits ++;
                //ho cambiato il costruttore in modo da passare l'oggetto piuttosto che il rettangolo, in questo modo posso accedere all'oggetto ed a un suo metodo:
                //il metodo getProperties, che contiene un metodo containsKey, che vede se l'oggetto in questione è associato o meno alla chiave passata come parametro
                //per aggiungere la proprietà fai tasto destro sull'oggetto coin nel tmx, e aggiungi una proprietà personalizzata
                if(objects.getProperties().containsKey("mushroom")){ //faccio lo spawn solo se quell'ogg ha la proprietà mushroom
                    screen.SpawnItem(new ItemDef(new Vector2(body.getPosition().x ,body.getPosition().y + 16/MarioGame.PPM), Mushroom.class)); //ogni volta che c'è una hit chiamo il
                    //metodo spawn, che genera un oggetto, per generarlo passo un vettore che indica la posizione ed il tipo
                    MarioGame.manager.get("audios/sounds/powerupspawn.wav", Sound.class).play();
                }
                else MarioGame.manager.get("audios/sounds/coinsound.wav", Sound.class).play();
            }
            else MarioGame.manager.get("audios/sounds/bumpsound.wav", Sound.class).play();
        }

        //getCell().setTile(tileset.getTile(28)); setto la cella al tile che prendo dal tileset e con indice 28 (l'indice è 27, ma si indica con id + 1, l'id si vede su tmx), pero non funziona con tileset_gutter
        //probabilmente non lo trova ma non so perchè


    }
}

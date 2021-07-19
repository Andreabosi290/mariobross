package com.mario.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mario.game.MarioGame;
import com.mario.game.Screens.PlayScreen;
import com.mario.game.Sprites.Mario;

public class Bricks extends InteractiveTileObject{

    public Bricks(PlayScreen screen, RectangleMapObject objects, BodyDef bodyDef, PolygonShape Shape, FixtureDef fixtureDef) {
        super(screen, objects, bodyDef, Shape, fixtureDef);
        setCategoryFilter(MarioGame.BRICK_BIT);
    }


    @Override
    public void OnHeadHit(Mario mario) {
        if (mario.isBig) {
            Gdx.app.log("Brick", "Collision");
            MarioGame.manager.get("audios/sounds/breakblock.wav", Sound.class).play();
            setCategoryFilter(MarioGame.DESTROYED_BIT); //quando c'è una hit, settiamo il filtro al blocco, così dato che mario può fare collisioni solo con default, coin e brick
            //non potrà collidere con quelli distrutti, e quindi ci passerà attraverso
            getCell().setTile(null);
            //a questo punto ho  aggiunto un livello alla tmx perchè con la rimozione del blocco rimaneva lo schermo nero, questo perchè era stato fatto tutta la parte grafica su un solo
            //livello => rimuovendo un quadrato non c'è nulla sotto e si vede nero, il livello aggiunto si trova sotto ed è un cielo, così rimuovendo il quadrato si vede il cielo sotto
            MarioGame.score += 100;
            return;
        }
        MarioGame.manager.get("audios/sounds/bumpsound.wav", Sound.class).play();
        return;

    }

}

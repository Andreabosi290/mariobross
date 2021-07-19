package com.mario.game.Sprites.Enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mario.game.MarioGame;
import com.mario.game.Screens.PlayScreen;

public abstract class Enemy extends Sprite {
    protected World world;
    protected Body body;
    protected PlayScreen screen;
    protected Vector2 velocity;

    protected boolean setToDestroy = false;
    protected boolean destroyed = false;


    public Enemy(PlayScreen screen,float x,float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x,y);
        defineEnemy();
        velocity = new Vector2(0.3f,0 );
        body.setActive(false); //posso impostare i nemici come inattivi di default, e riattivarli dopo, in questo modo saranno fermi finchè non li attivo, è utile ad esempio per evitare
        //che ad esempio un goomba che si trova vicino ad un dirupo cada senza che il giocatore faccia in tempo ad arrivare per vederlo (vedi update in playscreen)
    }
    protected abstract void defineEnemy();
    public void hitOnHead() {
        setToDestroy = true;
        MarioGame.manager.get("audios/sounds/stomp.wav", Sound.class).play();
    }
    public abstract void update(float deltaTime);
    public void reverseVelocity(boolean x,boolean y){
        if(x)velocity.x = - velocity.x;
        if(y)velocity.y = - velocity.y;
    }

    public Body getBody() {
        return body;
    }
}

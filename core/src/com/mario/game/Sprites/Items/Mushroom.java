package com.mario.game.Sprites.Items;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mario.game.MarioGame;
import com.mario.game.Screens.PlayScreen;
import com.mario.game.Sprites.Mario;

public class Mushroom extends Item{


    public Mushroom(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(new TextureRegion(screen.getAtlas().findRegion("mushroom"),0,0,16,16));
        velocity.set(0.4f,0);
    }

    @Override
    public void defineItem() {

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(),getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6.1f/ MarioGame.PPM);
        fixtureDef.filter.categoryBits = MarioGame.ITEM_BIT;
        fixtureDef.filter.maskBits = MarioGame.GROUND_BIT | MarioGame.COIN_BIT | MarioGame.BRICK_BIT | MarioGame.ENEMY_BIT | MarioGame.OBJECT_BIT | MarioGame.MARIO_BIT;
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef).setUserData(this);

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        setPosition(body.getPosition().x - getWidth()/2,body.getPosition().y - getHeight()/2);
        velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity); //sperimentato: con questa linea se la velocità è 0,0, il fungo scende, ma scende super lentamente perchè ogni volta la velocità viene rifatta partire da 0, e poi risubentra la gravità
    }

    @Override
    public void use(Mario mario) {
        destroy();
        MarioGame.manager.get("audios/sounds/powerup.wav", Sound.class).play();
        mario.toGrow();
    }

}

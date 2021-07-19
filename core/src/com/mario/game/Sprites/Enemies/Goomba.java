package com.mario.game.Sprites.Enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mario.game.MarioGame;
import com.mario.game.Screens.PlayScreen;

public class Goomba extends Enemy {

    private float stateTime = 0;
    private Animation<TextureRegion> walkanimation;

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setBounds(getX(),getY(),16/MarioGame.PPM,16/MarioGame.PPM);//dobbiamo indicare dove fare il render e di che dimensioni (le dimensioni le abbiamo scalate come tutto il resto del codice
        //creo le animazioni
        TextureRegion[] frames = new TextureRegion[2];
        for(int i = 0; i < 2; i ++) {
            frames[i] = new TextureRegion(screen.getAtlas().findRegion("goomba"),i * 16,0,16,16);
        }
        walkanimation = new Animation<TextureRegion>(0.1f, frames);
    }
    //metodo update
    public void update(float DeltaTime){
        stateTime += DeltaTime;
        //la prima cosa che facciamo è vedere se deve essere distrutto e non lo è già
        if(setToDestroy && !destroyed){
            world.destroyBody(body); //questo metodo di world distrugge il body, ricorda che la texture che viene disegnata è dovuta a sprite, e quindi distruggendo il body
            //non si elimina anche la texture, che invece rimane dove era il body
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"),32,0,16,16));
            stateTime = 0;
        }//se non è distrutto e non deve esserlo invece fa quello sotto
        else if(!destroyed){
            body.setLinearVelocity(velocity);//settiamo la velocità al vector2 che abbiamo definito in enemy
            setPosition(body.getPosition().x - getWidth()/2,body.getPosition().y - getHeight()/2);//settiamo la posizione nella quale disegneremo, ricorda che il body era fissato con coordinate del centro
            setRegion(walkanimation.getKeyFrame(stateTime,true)); //sceglie il frame da utilizzare, dico anche che deve looppare

        }
        else
        {
            if(stateTime >= 0.5f);
        }
    }
    //creo l'oggetto goomba nel mondo world
    @Override
    protected void defineEnemy() {
        //creo il goomba usando lo stesso modo che ho fatto con mario, e gli do la possibilità di collidere con quegli el
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(),getY()); //setto la posizione a getX e getY di sprite (definita in enemy che è chiamato con la super)
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6.1f/ MarioGame.PPM);
        fixtureDef.filter.categoryBits = MarioGame.ENEMY_BIT; //setto il bit di goomba
        //definisco con chi può collidere, sperimentando ho visto che per collidere, sia mario che goomba devono avere il bit nemico disponibile; se anche solo uno dei
        //2 non lo ha si trapassano
        fixtureDef.filter.maskBits = MarioGame.GROUND_BIT | MarioGame.COIN_BIT | MarioGame.BRICK_BIT | MarioGame.ENEMY_BIT | MarioGame.OBJECT_BIT | MarioGame.MARIO_BIT;
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef).setUserData(this);

        //mario elimina i goomba saltandoci sopra, quindi ci servirà un modo per capire se il contatto c'è stato tra mario e goomba, o tra mario e la testa del goomba
        //creiamo quindi una head come abbiamo fatto con mario

        PolygonShape head = new PolygonShape(); //polygonshape prende una lista di vector2 che rappresentano i vertici
        //definiamo i vettori
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-4,8).scl(1/MarioGame.PPM); //pos -5 dal centro e 8 sopra il centro del goomba, il tutto scalato con scl
        vertice[1] = new Vector2(4,8).scl(1/MarioGame.PPM);
        vertice[2] = new Vector2(-3,3).scl(1/MarioGame.PPM);
        vertice[3] = new Vector2(3,3).scl(1/MarioGame.PPM);
        head.set(vertice);
        fixtureDef.shape = head;
        //setto la shape della fixture che voglio creare (quindi sostanzialmente quella selezionata in questo momento) a head

        //aggiungiamo il fatto che se mario collide con la testa, rimbalza in alto
        fixtureDef.restitution = 0.7f; //se il valore è 1, mario rimbalza dello stesso numero di pixel da cui è atterrato, con 0.5 salta solo la metà
        fixtureDef.filter.categoryBits = MarioGame.ENEMY_HEAD_BIT;
        body.createFixture(fixtureDef).setUserData(this); //per fare in modo che possiamo accedervi durante le collisioni, in realtà serve anche perchè nel worldContactListener
        //c'è un if che guarda gli user data, e quindi se c'è una collisione, quello guarda i dati, ma se alcuni el non hanno il nome genera un errore e crasha

    }

    //implementazione collisione della testa con mario
    //potremmo pensare di eliminare l'oggetto al contatto, ma non possiamo perchè questo metodo è chiamato in WorldContactListener, che è chiamato da world.step in play screen
    //questo perchè saremmo durante una simulazione, e creerebbe tanti problemi con la gestione delle collisioni ed il processo attivo; per questo invece di
    //distruggere l'oggetto in questo metodo, dichiariamo una variabile booleana che mi ricorda che quell'oggetto deve essere distrutto

    @Override
    public void draw(Batch batch) {
        if(!destroyed || stateTime < 0.5f) super.draw(batch); //finchè il goomba non è distrutto, o se lo è, non da più di 0.5 secondi, il goomba viene disegnato
    }
}


package com.mario.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mario.game.MarioGame;
import com.mario.game.Screens.PlayScreen;

public class Mario extends Sprite {

    //Sprite: Holds the geometry, color, and texture information for drawing 2D sprites using Batch ...
    public World world;
    public Body body;
    protected TextureRegion marioStanding; //voglio prendere la texture individuale di mario piccolo che sta fermo dal pacchetto che ho passato di little mario
    protected TextureRegion marioFalling;
    protected TextureRegion mariojump;
    protected TextureRegion mariochangingleft;
    protected TextureRegion mariochangingright;
    protected TextureRegion bigmarioStanding;
    protected TextureRegion bigmarioJump;
    protected TextureRegion bigmariochangingleft;
    protected TextureRegion bigmariochangingright;
    //per animazioni
    public enum state {FALLING, JUMPING, STANDING,RUNNING,CHANGINGDIRECTIONR,CHANGINGDIRECTIONL,GROWING} ; //enum è un modo per creare una variabile (state) che può assumere solo uno tra i valori tra parentesi
    public state currentState;
    public state previousState; //creo variabili di tipo state (definito sopra) che rappresentano lo stato attuale e quello precedente
    protected Animation<TextureRegion> mariorun;//creo le animazioni per la corsa
    private boolean runningright;
    private float timer;
    protected Animation<TextureRegion> growingAnimation;
    protected Animation<TextureRegion> bigrunningAnimation;
    public boolean isBig;
    private boolean grow;
    private boolean definingbig;



    public Mario(PlayScreen screen) {
        this.world = screen.getWorld();
        defineMario(); // faccio il solito per creare mario
        marioStanding = new TextureRegion(screen.getAtlas().findRegion("little_mario"),0,0,16,16);//get texture prende la texture dalla sprite, texture che abbiamo passato sopra, e le coordinate sono 0,0 per l'angolo in alto a sinistra
        marioFalling = new TextureRegion(screen.getAtlas().findRegion("little_mario"),16*6,0,16,16);
        setBounds(0,0,16/MarioGame.PPM,16/MarioGame.PPM); //dobbiamo indicare dove fare il render e di che dimensioni (le dimensioni le abbiamo scalate come tutto il resto del codice, è un metodo di sprite
        //setRegion(marioStanding); //setto l'immagine associata a mario a mariostanding
        currentState = state.STANDING; //setto il valore dello stato corrente a standing
        previousState = state.STANDING;
        timer = 0;
        runningright = true; //vuol dire che è girato a destra
        mariojump = new TextureRegion(screen.getAtlas().findRegion("little_mario"),16*5,0,16,16);
        mariochangingleft = new TextureRegion(screen.getAtlas().findRegion("little_mario"),16*4,0,16,16);
        mariochangingright = new TextureRegion(screen.getAtlas().findRegion("little_mario"),16*4,0,16,16);
        mariochangingleft.flip(true,false);
        bigmarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"),16*5,0,16,32);
        bigmarioStanding = new TextureRegion(screen.getAtlas().findRegion("big_mario"),0,0,16,32);

        //creo le animazioni
        TextureRegion[] frames = new TextureRegion[3];
        for(int i = 1; i < 4; i ++) {
            frames[i-1] = new TextureRegion(screen.getAtlas().findRegion("little_mario"),i * 16,0,16,16);
        }
        mariorun = new Animation<TextureRegion>(0.1f, frames);

        frames = new TextureRegion[3];
        for (int i = 1; i < 4; i++){
            frames[i-1] = new TextureRegion(screen.getAtlas().findRegion("big_mario"),i * 16,0,16,32);
        }
        bigrunningAnimation = new Animation<TextureRegion>(0.1f,frames);

        bigmariochangingleft = new TextureRegion(screen.getAtlas().findRegion("big_mario"),16*4,0,16,32);
        bigmariochangingright = new TextureRegion(screen.getAtlas().findRegion("big_mario"),16*4,0,16,32);
        bigmariochangingleft.flip(true,false);

        frames = new TextureRegion[5];
        frames[0] = new TextureRegion(bigmarioStanding);
        frames[1] = new TextureRegion(new TextureRegion(screen.getAtlas().findRegion("big_mario"),16*15,0,16,32));
        frames[2] = new TextureRegion(bigmarioStanding);
        frames[3] = new TextureRegion(new TextureRegion(screen.getAtlas().findRegion("big_mario"),16*15,0,16,32));
        frames[4] = new TextureRegion(bigmarioStanding);

        growingAnimation = new Animation<TextureRegion>(0.2f,frames);
        grow = false;
        isBig = false;

    }
    public void toGrow(){
        grow = true;
        isBig = true;
        definingbig = true;
        setBounds(getX(),getY(),getWidth(),32/MarioGame.PPM);

    }
    //update di mario
    public void update(float DeltaTime){
        if(isBig)
        {
            setPosition(body.getPosition().x - getWidth()/2,body.getPosition().y - getHeight()/2 - 6/MarioGame.PPM); //abbassiamo un pelo la texture se è grande, cosi sembra che sia a terra
        }
        else{
            setPosition(body.getPosition().x - getWidth()/2,body.getPosition().y - getHeight()/2);//settiamo la posizione nella quale disegneremo, ricorda che il body era fissato con coordinate del centro
        }

        setRegion(getFrame(DeltaTime)); //sceglie il frame da utilizzare
        if(definingbig)
        {
            defineBigMario();
        }
    }
    //definiamo il body di mario
    public void defineMario(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32/ MarioGame.PPM,100/ MarioGame.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6.1f/ MarioGame.PPM);
        //dobbiamo settare la catogoria (categorybits) ed indicare con cosa può fare collisione (c'è da mettere prima di creare la fixture)
        fixtureDef.filter.categoryBits = MarioGame.MARIO_BIT; //(c'è un category bit mario)
        fixtureDef.filter.maskBits = MarioGame.GROUND_BIT | MarioGame.COIN_BIT | MarioGame.BRICK_BIT | MarioGame.OBJECT_BIT | MarioGame.ENEMY_BIT | MarioGame.ENEMY_HEAD_BIT
        | MarioGame.ITEM_BIT; //indichiamo con cosa mario può effettuare collisioni:
        //in questo caso può fare collisioni con monete, blocchi oppure default (tutto)


        fixtureDef.shape = shape;

        body.createFixture(fixtureDef).setUserData(this);

        //ora creo una fixture per rappresentare la testa di mario che mi serve per le collisioni con i blocchi e le monete
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/MarioGame.PPM,6.1f/MarioGame.PPM),new Vector2(2/MarioGame.PPM,6.1f/MarioGame.PPM));
        //questo mi serve per creare una nuova shape, che è sostanzialmente una linea, ottenuta unendo 2 punti che vengono passati mediante
        //vector2, con delle coordinate; ricorda che il body ha l'origine di default nel centro (penso) e quindi -2x vuol dire -2 dal centro per la coordinata x
        //e 6.1 sopra il centro, mentre l'altro punto è alla stessa altezza però 2 avanti
        fixtureDef.filter.categoryBits = MarioGame.MARIO_HEAD_BIT;
        fixtureDef.shape = head;
        //setto la shape della fixture che voglio creare (quindi sostanzialmente quella selezionata in questo momento) a head
        fixtureDef.isSensor = true;
        //ho messo la fixture come sensore, da questo punto in poi, tutto quello che viene creato con la fixture impostata a sensore NON effettua più le collisioni, ma
        //è solo utilizzabile per fare query, vedere certe cose ecc... ma non provoca effetti come rimbalzi ecc... come le fixture che ho creato prima (quindi direi che head non
        //provoca rimbalzi. ma è sempre circle a farlo)

        body.createFixture(fixtureDef).setUserData(this);
        //creo la fixture selezionata (head) e uso setUserData che fornisce un nome unico per indicare quella determinata shape
    }

    //quando mario si ingrandisce, devo modificare il suo body per far si che sia concorde alle texture utilizzate
    public void defineBigMario(){
        Vector2 currentposition = body.getPosition(); //creo un vettore che salva la posizione del body, prima che questo venga eliminato
        world.destroyBody(body); //distruggo il body, perchè lo dovrò poi ricreare diversamente

        BodyDef bodyDef = new BodyDef();
        // A questo punto devo settare ancora la posizione, ed userò quella corrente, ma in più aggiungo 10 di altezza perchè big mario è più alto (la posizione rappresenta
        //praticamente il centro del body
        bodyDef.position.set(currentposition.add(0,10/MarioGame.PPM));
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6.1f/ MarioGame.PPM);
        fixtureDef.filter.categoryBits = MarioGame.MARIO_BIT;
        fixtureDef.filter.maskBits = MarioGame.GROUND_BIT | MarioGame.COIN_BIT | MarioGame.BRICK_BIT | MarioGame.OBJECT_BIT | MarioGame.ENEMY_BIT | MarioGame.ENEMY_HEAD_BIT
                | MarioGame.ITEM_BIT;

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);

        shape.setPosition(new Vector2(0,-14/MarioGame.PPM)); //setto la posizione della shape più in basso
        body.createFixture(fixtureDef).setUserData(this); //riuso la shape per inserire il cerchio sotto al cerchio definito prima

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/MarioGame.PPM,6.1f/MarioGame.PPM),new Vector2(2/MarioGame.PPM,6.1f/MarioGame.PPM));
        fixtureDef.filter.categoryBits = MarioGame.MARIO_HEAD_BIT;
        fixtureDef.shape = head;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData(this);

        definingbig = false;
    }
    //prende il frame usando gli stati, e considera se mario è girato a destra o sinistra
    public TextureRegion getFrame (float delta){
        currentState = getState();
        TextureRegion region;
        switch (currentState){
            case STANDING:
                default:
            case FALLING:
                region = isBig == true ? bigmarioStanding : marioStanding;
                break;
            case JUMPING:
                region = isBig == true ? bigmarioJump :mariojump;
                break;
            case RUNNING:
                region = isBig == true ? bigrunningAnimation.getKeyFrame(timer,true) : mariorun.getKeyFrame(timer,true);
                break;
            case CHANGINGDIRECTIONR:
                region = isBig == true ? bigmariochangingright : mariochangingright;
                break;
            case CHANGINGDIRECTIONL:
                region = isBig == true ? bigmariochangingleft : mariochangingleft;
                break;
            case GROWING:
                region = growingAnimation.getKeyFrame(timer);
                if(growingAnimation.isAnimationFinished(timer)){
                    grow = false;
                }
                break;

        }
        if((body.getLinearVelocity().x < 0 || runningright == false) && !region.isFlipX() && region != mariochangingleft && region != mariochangingright && region != bigmariochangingleft && region != bigmariochangingright){
            region.flip(true,false);
            runningright = false;
        }
        else if((body.getLinearVelocity().x > 0 || runningright == true) && region.isFlipX() && region != mariochangingleft && region != mariochangingright && region != bigmariochangingleft && region != bigmariochangingright){
            region.flip(true,false);
            runningright = true;
        }
        timer = currentState == previousState ? timer + delta : 0; //vuol dire che timer = timer + delta se currentstate = previousstate, 0 altrimenti
        previousState = currentState;
        return region;
    }
    //ritorna lo stato in cui si trova mario
    public state getState(){
        if(grow){
            return state.GROWING;
        }
        else if(body.getLinearVelocity().y > 0 || (body.getLinearVelocity().y < 0 && previousState == state.JUMPING)  ){
            return state.JUMPING;
        }
        else if (body.getLinearVelocity().y < 0)
        {
            return state.FALLING;
        }
        else if(body.getLinearVelocity().x != 0){
            if((Gdx.input.isKeyPressed(Input.Keys.RIGHT) && body.getLinearVelocity().x < 0) )
            {
                return state.CHANGINGDIRECTIONR;
            }
            else if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) && body.getLinearVelocity().x > 0))
            {
                return  state.CHANGINGDIRECTIONL;
            }
            return state.RUNNING;
        }
        return state.STANDING;
    }
}

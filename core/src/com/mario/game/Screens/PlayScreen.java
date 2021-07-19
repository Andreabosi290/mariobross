package com.mario.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mario.game.MarioGame;
import com.mario.game.Scenes.Hud;
import com.mario.game.Sprites.Enemies.Enemy;
import com.mario.game.Sprites.Items.Item;
import com.mario.game.Sprites.Items.ItemDef;
import com.mario.game.Sprites.Items.Mushroom;
import com.mario.game.Sprites.Mario;
import com.mario.game.Tools.WorldContactListener;
import com.mario.game.Tools.WorldCreator;

import java.util.ArrayList;
import java.util.LinkedList;

public class PlayScreen implements Screen {

    protected MarioGame game;
    protected OrthographicCamera camera;
    protected Viewport viewport;

    //usano scene2d
    private Hud hud;

    //usano tiled
    private TmxMapLoader mapLoader; //carica la mappa nel gioco
    private TiledMap map; //riferimento alla mappa
    private OrthogonalTiledMapRenderer renderer; //fa il render della mappa

    //usano box2d
    private World world; //il mondo di gioco nel quale poi verrà analizzata la fisica
    private Box2DDebugRenderer boxrend; // serve per rappresentare le caratteristiche di ogni elemento nel mondo world come fixtures e bodies
    private Mario mario;
    private WorldCreator creator;

    private float jumpcooldowncounter = 0f;

    //atlas
    private TextureAtlas atlas;

    //musica e suoni
    private Music music;

    //ItemDefinition
    private ArrayList<Item> items; //la lista con tutti gli item del gioco
    private LinkedList<ItemDef> itemsToAdd; //la lista con gli elementi di tipo variabile (il tipo sarà quello dentro IDef, ma saranno comunque tutti degli Item)

    public PlayScreen(MarioGame game) {
        atlas = new TextureAtlas("mario_and_enemies.atlas");
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(MarioGame.WORLD_WIDTH / MarioGame.PPM,MarioGame.WORLD_HEIGHT/ MarioGame.PPM,camera);
        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("mariomap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map,1/ MarioGame.PPM); // aggiungo anche la scala
        camera.position.set(viewport.getWorldWidth()/2,viewport.getWorldHeight()/2,0);

        world = new World(new Vector2(0, -10), true); // è per la gravità, y = -10 quando si vuole rappresentare la realtà, il secondo
                                                                    //parametro serve per mettere a dormire certi oggetti
        boxrend = new Box2DDebugRenderer();
        //ora c'è da aggiungere i bodies e le fixtures (sono sostanzialmente le rappresentazioni grafiche degli oggetti con varie caratteristiche
        //come  shape, ecc...

        creator = new WorldCreator(this); // per rendere il codice più leggibile ho creato un'altra classe con tutto quello che c'è da fare

        mario = new Mario(this);

        world.setContactListener(new WorldContactListener()); //dobbiamo creare un ascoltatore per il nostro mondo

        //inizializziamo gli item
        items = new ArrayList<>();
        itemsToAdd = new LinkedList<>();
    }

    public void SpawnItem(ItemDef def){
        //questo metodo prende come parametro una definizione di un item, quindi caratterizzato da posizione e tipo, e la aggiunge alla linked list degli item che devono essere creati
        itemsToAdd.add(def);

    }

    public void SpawningItems(){
        if(!itemsToAdd.isEmpty())
        {
            ItemDef idef = itemsToAdd.poll(); //questo prende un oggetto dalla lista (linkedList è un tipo di queue in cui VALE l'ordine, => prende quello aggiunto da più tempo
            //e lo rimuove anche
            if(idef.type == Mushroom.class) //se l'oggetto che ho prelevato è dello stesso tipo dei mushroom
            {
                items.add(new Mushroom(this,idef.position.x,idef.position.y)); //aggiungiamo il fungo alla lista di tutti gli el
            }
        }
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }
    public void handleInput(float deltaTime){
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && mario.body.getLinearVelocity().x >= -1.6f)
        {
            mario.body.applyLinearImpulse(new Vector2(-0.1f,0),mario.body.getWorldCenter(),true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && mario.body.getLinearVelocity().x <= 1.6f)
        {
            mario.body.applyLinearImpulse(new Vector2(0.1f,0),mario.body.getWorldCenter(),true);
        }
        //ci sono 2 modi per eseguire gli spostamenti con box2d (quindi usando la fisica): usare la forza(incremento graduale della velocità) o impulse (spostamento immediato)
        //usiamo impulse per il salto
        jumpcooldowncounter -= deltaTime;
        if(Gdx.input.isKeyPressed(Input.Keys.UP) && jumpcooldowncounter <= 0 && mario.body.getLinearVelocity().y == 0){
            mario.body.applyLinearImpulse(new Vector2(0,4f),mario.body.getWorldCenter(),true);
            //applylinearimpulse prende come parametri un vettore che indica le componenti in cui effettuare lo spostamento, il punto in cui applicare lo spostamento, quindi in questo
            //caso il centro del corpo, e vero o falso per il fatto di svegliare l'oggetto se è dormiente (il dormiente era un aspetto di box2d per il quale gli oggetti dormienti non
            //vengono presi in considerazione durante il calcolo andando quindi a gravare di meno sul peso dell'app
            jumpcooldowncounter = 0.3f;
        }
        music = MarioGame.manager.get("audios/Music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.setVolume(0.01f);
        music.play();
    }

    public void update(float deltaTime){
        handleInput(deltaTime);
        SpawningItems();

        world.step(1/60f,6,2);//60 volte al secondo
        //setto le volte,i parametri sono timestep (tempo tra step), una velocita e posizione  di iterazione ( non le usiamo qua, ma servono per un calcolo più preciso delle collisioni, rimbalzi ecc..)

        mario.update(deltaTime); //facciamo l'update della posizione della texture di mario
        for (Enemy enemy : creator.getGoombas())//per ogni goomba nell'array dei goomba
        {
            enemy.update(deltaTime);
            if(!enemy.getBody().isActive() && enemy.getX() < mario.getX() + 22 * 16/MarioGame.PPM){ //se il nemico non è attivo e si trova ad una distanza di 22 (cubetti, contandoli sono 16 tra schermo e mario, ma ne ho aggiunti alcuni per farlo fare prima)
                //moltiplicati per la dimesione del cubo (16) e scalati per PPM
                enemy.getBody().setActive(true);
            }
        }

        //update di tutti gli item presenti nel gioco
        for(Item item : items)
        {
            item.update(deltaTime);
        }

        hud.update(deltaTime);
        camera.position.x = Math.max(mario.body.getPosition().x,(MarioGame.WORLD_WIDTH/2)/MarioGame.PPM);


        //per far funzionare il calcolo della fisica di box2d dobbiamo dire quante volte calcolare al secondo =>
        camera.update();
        renderer.setView(camera);
    }
    @Override
    public void render(float delta) {

//
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);

        renderer.render();

        boxrend.render(world, camera.combined);
        // per disegnare uso il box renderer che avevo creato, a cui passo il world e la camera (combined)
        //se fai il render si possono vedere delle linee verdi che indicano che box2d ha fatto il suo

        game.batch.setProjectionMatrix(camera.combined);//settiamo la camera correttamente prima di aprire la batch
        game.batch.begin();

        //disegniamo gli item
        for(Item item : items)
        {
            item.draw(game.batch);
        }

        for (Enemy enemy : creator.getGoombas())//per ogni goomba nell'array dei goomba
        {
            enemy.draw(game.batch);
        }


        mario.draw(game.batch); //chiamiamo il metodo draw per disegnare mario, che non è un metodo che abbiamo messo noi, ma è gia presente in Sprite che abbiamo esteso
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        // disegna lo stage, e quindi tutto quello che c'è dentro, non serve fare begin della batch

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
        //bisogna sempre fare il dispose per evitare carichi pesanti per il calcolatore
        map.dispose();
        renderer.dispose();
        world.dispose();
        boxrend.dispose();
        hud.dispose();

    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }
}

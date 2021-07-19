package com.mario.game.Tools;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mario.game.MarioGame;
import com.mario.game.Screens.PlayScreen;
import com.mario.game.Sprites.Enemies.Goomba;

import java.util.ArrayList;

public class WorldCreator {

    private ArrayList<Goomba> goombas;

    public WorldCreator(PlayScreen screen) {

        World world =  screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bodyDef = new BodyDef(); //prima di creare un body devo definire cos'è  quel body
        PolygonShape Shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef(); //anche qui bisogna prima definire la fixture
        Body body;

        //ora per creare un body per ogni singolo oggetto della tiled map uso un for
        //ground
        for(
                RectangleMapObject objects : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            //prende ogni oggetto dalla tiled map, accedendo ai layer (indice dal basso verso l'alto con 0 in basso))
            //devo selezionare il tipo di quel layer (ground in questo caso), prendendo gli oggetti e del tipo rectanglemapobject
            Rectangle rect = objects.getRectangle();
            //setto un rettangolo ottenuto dal layer

            //definiamo il body
            bodyDef.type = BodyDef.BodyType.StaticBody; //ci sono 3 tipi di body: static, dynamic e Kinematic
            //gli static non si muovono mai, se non forzati mediante codice
            //i kinematic non sono soggetti a gravità, ma si possono muovere mediante una velocità come le piattaforme volanti mobili ecc
            //dynamic si possono muovere e sono soggetti a gravità
            bodyDef.position.set((rect.getX() + rect.getWidth()/2)/ MarioGame.PPM, (rect.getY() + rect.getHeight()/2)/ MarioGame.PPM); //definisco la posizione, indicando il centro
            body = world.createBody(bodyDef); //creo il body usando il metodo di world che richiede un bodydef che ho definito sopra

            Shape.setAsBox(rect.getWidth()/2/ MarioGame.PPM,rect.getHeight()/2/ MarioGame.PPM); //diviso 2 perchè parte dal centro e poi va in entrambe le direzioni
            fixtureDef.shape = Shape; //setto la shape
            body.createFixture(fixtureDef); //creo la fixture con la definizione che ho fatto sopra
        }
        //pipes
        for(RectangleMapObject objects : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = objects.getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth()/2)/ MarioGame.PPM, (rect.getY() + rect.getHeight()/2)/ MarioGame.PPM);
            body = world.createBody(bodyDef);
            Shape.setAsBox(rect.getWidth()/2/ MarioGame.PPM,rect.getHeight()/2/ MarioGame.PPM);
            fixtureDef.shape = Shape;
            fixtureDef.filter.categoryBits = MarioGame.OBJECT_BIT; // associo
            body.createFixture(fixtureDef);
        }
        //coins
        for(RectangleMapObject objects : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            new Coins(screen,objects,bodyDef,Shape,fixtureDef);
        }
        //bricks
        for(RectangleMapObject objects : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            new Bricks(screen,objects,bodyDef,Shape,fixtureDef);
        }
        //TiledMap si può anche usare per creare i nemici, infatti dopo aver definito il processo per la creazione di un nemico, possiamo crearne quanti ne vogliamo semplicemente
        //richiamando quella classe, quindi se ad esempio creiamo un layer aggiuntivo in tiledMap e lo chiamiamo goomba, possiamo mettere dei rettangoli (Tieni premuto ctrl e usa il
        //mouse + sezione rettangolare per creare rettangoli perfetti) che poi verrano riusati per creare goomba in quelle posizioni
        goombas = new ArrayList<>();
        for(RectangleMapObject objects : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = objects.getRectangle();
            goombas.add(new Goomba(screen, rect.getX()/MarioGame.PPM, rect.getY()/MarioGame.PPM));
        }
    }

    public ArrayList<Goomba> getGoombas() {
        return goombas;
    }
}

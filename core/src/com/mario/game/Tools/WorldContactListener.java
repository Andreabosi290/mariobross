package com.mario.game.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mario.game.MarioGame;
import com.mario.game.Sprites.Enemies.Enemy;
import com.mario.game.Sprites.Items.Item;
import com.mario.game.Sprites.Mario;


//Un ContactListener è quello che viene chiamato quando 2 fixture hanno una collisione tra di loro in box2d
//Vengono chiamati i 4 metodi sotto:
//begin è chiamato quando il contatto ha inizio
//endcontact è chiamato quando il contatto termina
//presolve ci consente di cambiare caratteristiche del contatto una volta che questo avviene
//postsolve ci ritorna gli effetti di quella collisione (es angoli,effetti sulle fixture, ecc...)



public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        //L'attributo contact ha 2 fixture, una A ed una B, che sono le fixture responsabili del contatto
        //definisco cDef, che assume il valore dell'OR logico tra i 2 valori, es se ho 1 e 2, l'OR tra i 2 è: (0001 OR 0010) = 0011 = 3, l'importante però è che in questo modo
        //so quali sono i bit delle 2 fixture coinvolte
        int cDef = contact.getFixtureA().getFilterData().categoryBits | contact.getFixtureB().getFilterData().categoryBits;

        //verifico se uno dei 2 fixture del contatto è testa (di mario) e poi una volta determinato chi è chi, chiamo il metodo hit dell'oggetto
//        if("head".equals(contact.getFixtureA().getUserData()) || "head".equals(contact.getFixtureB().getUserData())){
//            Fixture head = contact.getFixtureA().getUserData() == "head" ? contact.getFixtureA() : contact.getFixtureB();
//            Fixture object = head == contact.getFixtureA()? contact.getFixtureB() : contact.getFixtureA();
//            if(object.getUserData() != null && object.getUserData() instanceof InteractiveTileObject){
//                ((InteractiveTileObject) object.getUserData()).OnHeadHit();
//            }
//        }
//        l'ho sostituito con il caso sotto che usa anche i bit

        switch (cDef){
            case MarioGame.MARIO_HEAD_BIT | MarioGame.BRICK_BIT:
            case MarioGame.MARIO_HEAD_BIT | MarioGame.COIN_BIT:
                if(contact.getFixtureA().getFilterData().categoryBits == MarioGame.MARIO_HEAD_BIT)//se fixA è la testa del
                {
                    ((InteractiveTileObject) contact.getFixtureB().getUserData()).OnHeadHit((Mario) contact.getFixtureA().getUserData());
                }
                else {
                    ((InteractiveTileObject) contact.getFixtureA().getUserData()).OnHeadHit((Mario) contact.getFixtureB().getUserData());
                }
                break;

            case MarioGame.ENEMY_HEAD_BIT | MarioGame.MARIO_BIT: //caso in cui i 2 el sono la testa del nemico e mario
                if(contact.getFixtureA().getFilterData().categoryBits == MarioGame.ENEMY_HEAD_BIT)//se fixA è la testa del
                {
                    ((Enemy) contact.getFixtureA().getUserData()).hitOnHead(); //chiamiamo il metodo hitOnHead di Enemy, prima però devo fare il type cast
                }
                else {
                    ((Enemy) contact.getFixtureB().getUserData()).hitOnHead();
                }
                break;
            case MarioGame.ENEMY_BIT | MarioGame.OBJECT_BIT: //se il nemico collide con un oggetto, la velocità viene invertita
                if(contact.getFixtureA().getFilterData().categoryBits == MarioGame.ENEMY_BIT)
                {
                    ((Enemy) contact.getFixtureA().getUserData()).reverseVelocity(true,false);
                }
                else {
                    ((Enemy) contact.getFixtureB().getUserData()).reverseVelocity(true,false);
                }
                break;
            case MarioGame.ENEMY_BIT | MarioGame.ENEMY_BIT:
                ((Enemy) contact.getFixtureA().getUserData()).reverseVelocity(true,false);
                ((Enemy) contact.getFixtureB().getUserData()).reverseVelocity(true,false);
                break;
            case MarioGame.ITEM_BIT | MarioGame.OBJECT_BIT:
                if(contact.getFixtureA().getFilterData().categoryBits == MarioGame.ITEM_BIT)
                {
                    ((Item) contact.getFixtureA().getUserData()).reverseVelocity(true,false);
                }
                else {
                    ((Item) contact.getFixtureB().getUserData()).reverseVelocity(true,false);
                }
                break;
            case MarioGame.ITEM_BIT | MarioGame.MARIO_BIT:
                if(contact.getFixtureA().getFilterData().categoryBits == MarioGame.ITEM_BIT)
                {
                    ((Item) contact.getFixtureA().getUserData()).use((Mario) contact.getFixtureB().getUserData());
                }
                else {
                    ((Item) contact.getFixtureB().getUserData()).use((Mario) contact.getFixtureA().getUserData());
                }
                break;
            case MarioGame.ITEM_BIT | MarioGame.ENEMY_BIT:
                if(contact.getFixtureA().getFilterData().categoryBits == MarioGame.ITEM_BIT)
                {
                    ((Item) contact.getFixtureA().getUserData()).reverseVelocity(true,false);
                }
                else {
                    ((Item) contact.getFixtureB().getUserData()).reverseVelocity(true,false);
                }
                break;
            case MarioGame.ENEMY_BIT | MarioGame.MARIO_BIT: //caso in cui i 2 el sono il nemico e mario
                if(contact.getFixtureA().getFilterData().categoryBits == MarioGame.ENEMY_BIT)//se fixA è il nemico
                {
                    ((Mario) contact.getFixtureB().getUserData()).hit(); //chiamiamo il metodo hit di mario, prima però devo fare il type cast
                    ((Enemy) contact.getFixtureA().getUserData()).hitOnHead();
                }
                else {
                    ((Mario) contact.getFixtureA().getUserData()).hit();
                    ((Enemy) contact.getFixtureB().getUserData()).hitOnHead();
                }
                break;

        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}

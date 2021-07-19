package com.mario.game.Sprites.Items;

import com.badlogic.gdx.math.Vector2;

public class ItemDef {

    //questa classe ha 2 attributi, e mi serve semplicemente per creare degli item di tipi diversi, senza fare la creazione per ciascun tipo, ma mantenendo un codice
    //più generico e riutilizzabile

    public Vector2 position; //la posizione dell'item
    public Class<?> type; //rappresenta il tipo dell'oggetto che dovrò creare il

    public ItemDef(Vector2 position, Class<?> type) {
        this.position = position;
        this.type = type;
    }

    //il resto è in PlayScreen sotto item def
}

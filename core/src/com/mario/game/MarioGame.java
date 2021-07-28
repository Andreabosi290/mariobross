package com.mario.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mario.game.Screens.PlayScreen;

public class MarioGame extends Game {

	public SpriteBatch batch;
	public static final float WORLD_HEIGHT = 208;
	public static final float WORLD_WIDTH = 500;
	public static final float PPM = 100; // box2d usa i metri come scala, quindi ad 1 pixel è associato un metro, quindi definisco una variabile che userò per scalare le misure in modo che 100 pixel = 1 metro
	public static int score = 0;

	//creiamo dei valori di default per i filtri che usiamo (guarda mario e Filters)
	public static final short NOTHING_BIT = 0;
	public static final short MARIO_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	public static final short GROUND_BIT = 1;
	public static final short OBJECT_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short ENEMY_HEAD_BIT = 128;
	public static final short ITEM_BIT = 256;
	public static final short MARIO_HEAD_BIT = 3;
	//tutti gli oggetti creati hanno delle fixture, le quali hanno dei bit di categoria (categorybits) che servono per le collisioni; normalmente se non specificati i bit di default sono = 1

	//creiamo un AssetManager (vedi libgdx api) per usare i file musicali ed i suoni
	public static AssetManager manager; //per ora è static, ma sarebbe meglio passarlo alle classi a cui serve
	@Override
	public void create() {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("audios/Music/mario_music.ogg", Music.class);//dobbiamo mettere in coda gli asset per essere caricati dal manager, il primo è di tipo Music
		manager.load("audios/sounds/coinsound.wav", Sound.class);
		manager.load("audios/sounds/bumpsound.wav", Sound.class);
		manager.load("audios/sounds/breakblock.wav", Sound.class);
		manager.load("audios/sounds/powerup.wav", Sound.class);
		manager.load("audios/sounds/powerdown.wav", Sound.class);
		manager.load("audios/sounds/powerupspawn.wav", Sound.class);
		manager.load("audios/sounds/stomp.wav", Sound.class);
		manager.load("audios/sounds/mariodeath.wav", Sound.class);
		manager.finishLoading(); //questo blocca tutto finche non ha caricato gli assets

		setScreen(new PlayScreen(this));
	}

	@Override
	public void render() {
		super.render();
		//manager.update(); questo metodo che si mette nel render serve per far caricare al manager continuamente i valori, update ritorna un boolean che dice se
		//gli asset sono stati tutti caricati o no, è un altro modo per caricarli
	}

	@Override
	public void dispose() {
		super.dispose();
		manager.dispose();
		batch.dispose();
	}
}

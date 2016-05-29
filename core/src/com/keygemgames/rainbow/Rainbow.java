package com.keygemgames.rainbow;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * This game extends "Game" of the LibGDX api. This allows you to use specific screens, which are basically different
 * stages of the game. For example, you can have a tutorial screen, help screen, loading screen, play screen, etc.
 */
public class Rainbow extends Game {
	//Constants for the game.
	public static final int VIRTUAL_WIDTH = 480;
	public static final int VIRTUAL_HEIGHT = 800;

	//Asset manager (Stores image/sound files).
	public AssetManager assets;

	//Saves information such as highscore.
	public Preferences prefs;

	//A virtual camera to display the game on the Android screen.
	public static OrthographicCamera camera;
	private BitmapFont font;

	public Rainbow()
	{
		assets = new AssetManager();
	}

	@Override
	public void create () {

		initializeFonts();

		camera = new OrthographicCamera();
		//Gives the camera a virtual height and width (of 480 x 800, a standard smartphone size).
		camera.setToOrtho(false, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

		//Sets preference file. Creates it if it does not exist.
		prefs = Gdx.app.getPreferences("Preferences.txt");

		//If the preference file does not contain a key called highscore, create that key and the corresponding default
		//highscore, 0.
		if(!prefs.contains("highscore"))
		{
			prefs.putInteger("highscore", 0);
			prefs.flush();// This saves the preferences file.

			//Sets the current screen to TutorialScreen.
			setScreen(new LoadingScreen(this));

		}
		else
		{
			//Sets the current screen to LoadingScreen.
			setScreen(new LoadingScreen(this));
		}
	}

	private void initializeFonts() {

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("DAYPBL.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
		param.size = 36;
		param.color = Color.WHITE;
		generator.generateData(param);

		font = generator.generateFont(param);

	}


	@Override
	public void render () {
		//Renders the current screen.
		super.render();
	}
}

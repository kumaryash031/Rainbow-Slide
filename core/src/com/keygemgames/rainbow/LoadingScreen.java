package com.keygemgames.rainbow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;


/**
 * Created by musial321 on 4/8/2016.
 */
public class LoadingScreen implements Screen {
    Rainbow app;
    private ShapeRenderer shapeRenderer;
    private float progress;

    private Stage stage;
    private Image splashImage;

    public LoadingScreen(Rainbow app)
    {
        setupStage();
        this.app = app;
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
    }


    @Override
    public void show() {
        progress = 0f;

        splashImage = new Image(new Texture("splash.png"));
        splashImage.setOrigin(splashImage.getWidth()/2,splashImage.getHeight()/2);
        splashImage.scaleBy(2);
        splashImage.setPosition(stage.getWidth()/2 - splashImage.getWidth()/2, stage.getHeight()/2 +32);

        stage.addActor(splashImage);



        splashImage.addAction(sequence(alpha(0f),scaleTo(.1f,.1f),
                parallel(fadeIn(1.5f, Interpolation.pow2),scaleTo(3f,3f,2f,Interpolation.pow5),moveTo(stage.getWidth()/2-splashImage.getWidth()/2,stage.getHeight()/2, 2f,Interpolation.swing))

        ));

        queueAssets();
    }

    private void setupStage()
    {
        stage = new Stage(new FitViewport(Rainbow.VIRTUAL_WIDTH, Rainbow.VIRTUAL_HEIGHT,app.camera));

        //stage.addListener()
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(175/255f,216/255f,1f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(32, app.camera.viewportHeight/2 - 8-200,progress*(app.VIRTUAL_WIDTH - 64),16);
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height);
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

    }

    private void queueAssets()
    {
        app.assets.load("single/r.png",Texture.class);
        app.assets.load("single/o.png",Texture.class);
        app.assets.load("single/y.png",Texture.class);
        app.assets.load("single/g.png",Texture.class);
        app.assets.load("single/b.png",Texture.class);
        app.assets.load("single/p.png",Texture.class);

        app.assets.load("double/bp.png",Texture.class);
        app.assets.load("double/gb.png",Texture.class);
        app.assets.load("double/gp.png",Texture.class);
        app.assets.load("double/ob.png",Texture.class);
        app.assets.load("double/og.png",Texture.class);
        app.assets.load("double/op.png",Texture.class);
        app.assets.load("double/oy.png",Texture.class);
        app.assets.load("double/rb.png",Texture.class);
        app.assets.load("double/rg.png",Texture.class);
        app.assets.load("double/ro.png",Texture.class);
        app.assets.load("double/rp.png",Texture.class);
        app.assets.load("double/ry.png",Texture.class);
        app.assets.load("double/yb.png",Texture.class);
        app.assets.load("double/yg.png",Texture.class);
        app.assets.load("double/yp.png",Texture.class);

        app.assets.load("triple/gbp.png",Texture.class);
        app.assets.load("triple/obp.png",Texture.class);
        app.assets.load("triple/ogb.png",Texture.class);
        app.assets.load("triple/ogp.png",Texture.class);
        app.assets.load("triple/oyb.png",Texture.class);
        app.assets.load("triple/oyg.png",Texture.class);
        app.assets.load("triple/oyp.png",Texture.class);
        app.assets.load("triple/rbp.png",Texture.class);
        app.assets.load("triple/rgb.png",Texture.class);
        app.assets.load("triple/rgp.png",Texture.class);
        app.assets.load("triple/rob.png",Texture.class);
        app.assets.load("triple/rog.png",Texture.class);
        app.assets.load("triple/rop.png",Texture.class);
        app.assets.load("triple/roy.png",Texture.class);
        app.assets.load("triple/ryb.png",Texture.class);
        app.assets.load("triple/ryg.png",Texture.class);
        app.assets.load("triple/ryp.png",Texture.class);
        app.assets.load("triple/ybp.png",Texture.class);
        app.assets.load("triple/ygb.png",Texture.class);
        app.assets.load("triple/ygp.png",Texture.class);

        app.assets.load("quad/ogbp.png",Texture.class);
        app.assets.load("quad/oybp.png",Texture.class);
        app.assets.load("quad/oygb.png",Texture.class);
        app.assets.load("quad/oygp.png",Texture.class);
        app.assets.load("quad/rgbp.png",Texture.class);
        app.assets.load("quad/robp.png",Texture.class);
        app.assets.load("quad/rogb.png",Texture.class);
        app.assets.load("quad/rogp.png",Texture.class);
        app.assets.load("quad/royb.png",Texture.class);
        app.assets.load("quad/royg.png",Texture.class);
        app.assets.load("quad/royp.png",Texture.class);
        app.assets.load("quad/rybp.png",Texture.class);
        app.assets.load("quad/rygb.png",Texture.class);
        app.assets.load("quad/rygp.png",Texture.class);
        app.assets.load("quad/ygbp.png",Texture.class);

        app.assets.load("quint/oygbp.png",Texture.class);
        app.assets.load("quint/rogbp.png",Texture.class);
        app.assets.load("quint/roybp.png",Texture.class);
        app.assets.load("quint/roygb.png",Texture.class);
        app.assets.load("quint/roygp.png",Texture.class);
        app.assets.load("quint/rygbp.png",Texture.class);

        app.assets.load("roygbp.png",Texture.class);

        app.assets.load("background.png",Texture.class);

        app.assets.load("restart.png",Texture.class);
    }

    private void update(float delta)
    {
        stage.act(delta);

        progress = MathUtils.lerp(progress, app.assets.getProgress(), .05f);

        if(app.assets.update() && progress >= app.assets.getProgress() - .01)
        {
            app.setScreen(new PlayScreen(app));
        }
    }
}


package com.keygemgames.rainbow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Rainbow
 * Application by KeyGemGames.
 * Version 1.0
 */
public class PlayScreen implements Screen {
    private static final int STORAGE_ROW = 8;
    private static final int NUM_COLORS = 6;

    Rainbow app;
    Stage stage;

    char[][] diskStorage;

    String[][] board;

    Rectangle[][] diskLocations;

    Image background;
    Image restart;
    Image[][] diskActors;

    private int score;

    public PlayScreen(Rainbow app)
    {
        this.app = app;
    }

    //Runs once every time the game is set to this screen.
    @Override
    public void show() {
        score = 0;

        setupDiskLocations();
        setupStorage();
        setupDisks();

        setupStage();
        addBackground();
        addRestart();
        addDisksToStage();

        setupInput();

        //Just to test printing out the storage and board.
        printBoard();
        System.out.println();
        printStorage();
    }

    //Runs continuously to check for changes in the game.
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(213/255f, 213/255f, 219/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateStorage();
        checkRainbow();
        stage.act(delta);
        stage.draw();

        drawScores();
    }

    private void drawScores() {
        //font
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

    private void setupDiskLocations() {
        diskLocations = new Rectangle[3][3];

        for(int row = 0; row < 3; row++)
        {
            for(int col = 0; col < 3; col++)
            {
                diskLocations[row][col] = new Rectangle(36f + col*140.4f, 430f - row*144f,129.6f,129.6f);
                System.out.println(row + " " + col + " " + diskLocations[row][col].x + " " + diskLocations[row][col].y);
            }
        }


    }

    private void addRestart() {
        restart = new Image(app.assets.get("restart.png",Texture.class));
        restart.setPosition(392, 653);
        stage.addActor(restart);

        restart.addListener(new InputListener()
        {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                Rectangle boundingBox = new Rectangle(0,0,restart.getWidth(),restart.getHeight());

                if(boundingBox.contains(x,y))
                {
                    show();
                }
            }
        });



    }

    private void setupInput() {


        InputMultiplexer multi = new InputMultiplexer();

        multi.addProcessor(new GestureDetector(new GestureDetector.GestureListener() {
            int rowFlung;
            int colFlung;
            boolean flungDisk;
            int currRow;
            int currCol;

            @Override
            public boolean touchDown(float x, float y, int pointer, int button) {
                flungDisk = false;
                //  Vector2 vect = new Vector2(x,800-y);
                //  Vector2 proj = new Vector2(stage.getViewport().unproject(vect).x, stage.getViewport().unproject(vect).y);

                Vector2 proj = stage.getViewport().unproject(new Vector2(x,y));


                currRow = 0;
                currCol = 0;


                for(Rectangle[] row: diskLocations)
                {
                    for(Rectangle disk: row)
                    {
                        if(disk.contains(proj.x,proj.y))
                        {
                            rowFlung = currRow;
                            colFlung = currCol;
                            flungDisk = true;
                        }
                        currCol++;
                    }
                    currRow++;
                    currCol = 0;
                }

                return false;
            }


            @Override
            public boolean tap(float x, float y, int count, int button) {
                return false;
            }

            @Override
            public boolean longPress(float x, float y) {
                return false;
            }

            @Override
            public boolean fling(float velocityX, float velocityY, int button) {

                if(flungDisk)
                {
                    //System.out.println(rowFlung + " " + colFlung);

                    handleFling(rowFlung,colFlung,velocityX,velocityY);

                }
                //System.out.println(velocityX + " " + velocityY);
                return true;
            }

            @Override
            public boolean pan(float x, float y, float deltaX, float deltaY) {
                return false;
            }

            @Override
            public boolean panStop(float x, float y, int pointer, int button) {
                return false;
            }

            @Override
            public boolean zoom(float initialDistance, float distance) {
                return false;
            }

            @Override
            public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
                return false;
            }
        }));

        stage.addListener(new InputListener()
        {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {

            }
        });

        multi.addProcessor(stage);

        Gdx.input.setInputProcessor(multi);
    }

    private void handleFling(int row, int col,float velocityX, float velocityY) {
        boolean validMove = false;
        String direction = "";


        //Left or right swipe.
        if(Math.abs(velocityX) > Math.abs(velocityY))
        {
            //Left
            if(velocityX < 0)
            {
                if(col > 0)
                    validMove = true;
                direction = "left";
            }
            //Right
            else
            {
                if(col < 2)
                    validMove = true;
                direction = "right";
            }
        }
        //Up or down swipe.
        else
        {
            //Y velocity goes from top to bottom. So a positive velocity is downwards.
            if(velocityY < 0)
            {
                if(row > 0)
                {
                    validMove = true;
                    direction = "up";
                }
            }
            else
            {
                if(row < 2)
                {
                    validMove = true;
                    direction = "down";
                }
            }
        }



        if(validMove)
        {
            makeMove(direction,row,col);
        }
    }

    private void makeMove(String direction, int startRow, int startCol) {
        int endRow=0;
        int endCol=0;

        //u, d, l, r for up down left right.
        switch(direction.charAt(0))
        {
            case 'u': endRow = startRow-1;
                endCol = startCol;break;
            case 'd': endRow = startRow+1;
                endCol = startCol;break;
            case 'l': endRow = startRow;
                endCol = startCol - 1;break;
            case 'r': endRow = startRow;
                endCol = startCol + 1;break;
        }

        boolean invalidMove = false;

        for(char color: board[startRow][startCol].toCharArray())
        {
            if(board[endRow][endCol].contains(Character.toString(color)))
            {
                invalidMove = true;
            }
        }

        float xMove = 0;
        float yMove = 0;

        switch(direction.charAt(0))
        {
            case 'u': yMove =  141.3f/2;break;
            case 'd': yMove = -141.3f/2;break;
            case 'l': xMove = -141.187f/2;break;
            case 'r': xMove =  141.187f/2;break;
        }

        if(invalidMove)
        {
            //Makes the invalid move.
            diskActors[startRow][startCol].addAction(Actions.sequence(Actions.moveBy(xMove,yMove,.2f,Interpolation.circle),Actions.moveBy(-xMove,-yMove,.2f)));
        }
        else
        {


            //Move the initial disk to final location.
            diskActors[startRow][startCol].addAction(Actions.sequence(Actions.moveBy(xMove*2,yMove*2,.2f,Interpolation.circle),Actions.removeActor()));


            //Uses the compareTo method which compares colors in a rainbow. Then gets the final String representation of
            //a disk.
            for(char color: board[startRow][startCol].toCharArray())
            {
                int index = 0;

                while(index < board[endRow][endCol].length() && compareTo(color, board[endRow][endCol].charAt(index)) > 0)
                {
                    index++;
                }

                board[endRow][endCol] = board[endRow][endCol].substring(0,index) + color + board[endRow][endCol].substring(index,board[endRow][endCol].length());
            }

            //HERE
            setRandomDisk(startRow,startCol);
            addDisk(startRow,startCol);

            setupNext(endRow, endCol);



        }


        // diskActors[row][col].setPosition(45.713f + col*141.187f, 443.7f - row*141.3f);

        // diskActors[startRow][startCol].addAction(Actions.moveBy());

    }


    private void setupNext(int row, int col)
    {
        String folder = "";

        switch(board[row][col].length())
        {
            case 1: folder = "single/";break;
            case 2: folder = "double/";break;
            case 3: folder = "triple/";break;
            case 4: folder = "quad/";break;
            case 5: folder = "quint/";break;
        }

        diskActors[row][col].remove();
        diskActors[row][col] = new Image(app.assets.get(folder + board[row][col] + ".png", Texture.class));

        diskActors[row][col].setPosition(45.713f + col*141.187f, 443.7f - row*141.3f);
        diskActors[row][col].setOrigin(app.assets.get("single/r.png", Texture.class).getWidth()/2,
                app.assets.get("single/r.png", Texture.class).getHeight()/2);
        diskActors[row][col].addAction(Actions.sequence(Actions.scaleTo(1.2f,1.2f,.1f),Actions.scaleTo(1,1,.1f)));
        stage.addActor(diskActors[row][col]);
    }

    private int compareTo(char color1, char color2)
    {
        if(color1 == color2)
        {
            return 0;
        }

        if(color1 == 'r')
            return -1;
        else if(color1 == 'o')
        {
            if(color2 == 'r')
            {
                return 1;
            }
            else
            {
                return -1;
            }
        }
        else if(color1 == 'y')
        {
            if(color2 == 'r' || color2 == 'o')
            {
                return 1;
            }
            else
            {
                return -1;
            }
        }
        else if(color1 == 'g')
        {
            if(color2 == 'r' || color2 == 'o' || color2 == 'y')
            {
                return 1;
            }
            else
            {
                return -1;
            }
        }
        else if(color1 == 'b')
        {
            if(color2 == 'r' || color2 == 'o' || color2 == 'y' || color2 == 'g')
            {
                return 1;
            }
            else
            {
                return -1;
            }
        }
        else if(color1 == 'p')
        {
            return 1;
        }


        return 0;
    }

    private void addBackground() {
        background = new Image(app.assets.get("background.png",Texture.class));

        background.setPosition(24.9f,138.7f);

        stage.addActor(background);

    }

    private void addDisksToStage() {
        diskActors = new Image[3][3];

        for(int row = 0; row < 3; row++)
        {
            for(int col = 0; col < 3; col++)
            {
                addDisk(row,col);
            }
        }

    }

    private void addDisk(int row, int col) {
        String folder = "";

        switch(board[row][col].length())
        {
            case 1: folder = "single/";break;
            case 2: folder = "double/";break;
            case 3: folder = "triple/";break;
            case 4: folder = "quad/";break;
            case 5: folder = "quint/";break;
        }

        diskActors[row][col] = new Image(app.assets.get(folder + board[row][col] + ".png", Texture.class));

        diskActors[row][col].setPosition(45.713f + col*141.187f, 443.7f - row*141.3f);
        diskActors[row][col].setScale(0);
        diskActors[row][col].setOrigin(app.assets.get("single/r.png", Texture.class).getWidth()/2,
                app.assets.get("single/r.png", Texture.class).getHeight()/2);
        diskActors[row][col].addAction(Actions.scaleTo(1,1,.7f, Interpolation.bounceOut));
        stage.addActor(diskActors[row][col]);
    }

    private void printStorage() {
        for(int row = 0; row < STORAGE_ROW; row++)
        {
            for(int col = 0; col < NUM_COLORS; col++)
            {
                System.out.print(diskStorage[row][col]);
            }
            System.out.println();
        }
    }

    private void printBoard()
    {
        for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board[i].length;j++)
            {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }

    private void setupDisks() {
        board = new String[3][3];

        for(int row = 0; row < 3; row++)
        {
            for(int col = 0; col < 3; col++)
            {
                setRandomDisk(row,col);
            }
        }
    }

    private void setRandomDisk(int row, int col) {
        int pickedRow;
        int pickedCol;

        do {
            pickedRow = MathUtils.random(STORAGE_ROW - 1);
            pickedCol = MathUtils.random(NUM_COLORS - 1);
        }
        while(diskStorage[pickedRow][pickedCol] == ' ');

        board[row][col] = Character.toString(diskStorage[pickedRow][pickedCol]);
        diskStorage[pickedRow][pickedCol] = ' ';
    }

    private void setupStorage() {
        diskStorage = new char[STORAGE_ROW][NUM_COLORS];

        for(int row = 0; row < STORAGE_ROW; row++)
        {
            insertRainbow(row);
        }

    }

    private void insertRainbow(int row) {
        for(int color = 0; color < NUM_COLORS; color++)
        {
            char colorChar = ' ';

            switch(color)
            {
                case 0: colorChar = 'r';break;
                case 1: colorChar = 'o';break;
                case 2: colorChar = 'y';break;
                case 3: colorChar = 'g';break;
                case 4: colorChar = 'b';break;
                case 5: colorChar = 'p';break;
            }



            diskStorage[row][color] = colorChar;
        }
    }

    private void setupStage() {
        stage = new Stage(new FitViewport(Rainbow.VIRTUAL_WIDTH, Rainbow.VIRTUAL_HEIGHT,app.camera));
    }

    private void checkRainbow() {
        int rowNum = 0;
        int colNum = 0;

        for(String[] row: board)
        {
            for(String disk: row)
            {
                if(disk.equals("roygbp"))
                {
                    diskActors[rowNum][colNum].addAction(Actions.sequence(Actions.scaleTo(50f,50f,1f),Actions.removeActor()));

                    if(score > app.prefs.getInteger("highscore"))
                    {
                        app.prefs.putInteger("highscore",score);
                        app.prefs.flush();
                    }

                    setRandomDisk(rowNum,colNum);
                    addDisk(rowNum,colNum);
                }

                colNum++;
            }
            rowNum++;
            colNum = 0;
        }


    }

    private void updateStorage() {
        int rowNum = 0;

        for(char[] row: diskStorage)
        {
            boolean empty = true;

            for(char disk: row)
            {
                if(disk != ' ')
                {
                    empty = false;
                }
            }

            if(empty)
            {
                insertRainbow(rowNum);
            }

            rowNum++;
        }
    }

}

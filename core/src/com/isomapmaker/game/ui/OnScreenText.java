package com.isomapmaker.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class OnScreenText {
    private Vector2 pos;
    private BitmapFont font;
    private String text;
    
    public OnScreenText(String text, Vector2 pos){
        this.pos = new Vector2(pos.x,pos.y);
        this.text = text;
        this.font = new BitmapFont(Gdx.files.internal("fonts/badd_mono.fnt"));
    }

    public void render(SpriteBatch batch){
        font.draw(batch,this.text,this.pos.x, this.pos.y);
    }

    public void dispose(){
        font.dispose();
    }

    public void setPos(Vector2 pos){
        this.pos = pos;
    }

    public void setText(String text){
        this.text = text;
    }

}

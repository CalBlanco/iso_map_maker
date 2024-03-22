package com.isomapmaker.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class OnScreenText {
    private Vector2 pos;
    private BitmapFont font;
    private String text;
    private GlyphLayout gl;
    
    public OnScreenText(String text, Vector2 pos){
        this.pos = new Vector2(pos.x,pos.y);
        this.text = text;
        this.font = new BitmapFont(Gdx.files.internal("fonts/badd_mono.fnt"));
        this.gl = new GlyphLayout(font,"");
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
        this.gl = new GlyphLayout(font,text);
    }

    public Vector2 getBound(){
        return new Vector2(this.gl.width, this.gl.height);
    }

}

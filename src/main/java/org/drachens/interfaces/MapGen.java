package org.drachens.interfaces;

public abstract class MapGen implements MapGenerator {
    int sizeX;
    int sizeY;
    public int getSizeX(){
        return sizeX;
    }
    public int getSizeY(){
        return sizeY;
    }
    public MapGen(int sizeX, int sizeY){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }
}

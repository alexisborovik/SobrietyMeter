package com.alexis.borovik.sobrietymeter;

/**
 * Created by Alexis on 27.10.2016.
 */

public class Alcohol {
    int id;
    private String name;
    private int volume;
    private int volumeOfAlc;
    private int imagePath;
    private int favorite;

    public Alcohol( String name, int volume, int volumeOfAlc,  int imagePath, int favorite) {
        this.favorite = favorite;
        this.imagePath = imagePath;
        this.name = name;
        this.volume = volume;
        this.volumeOfAlc = volumeOfAlc;
    }
    public Alcohol( int id, String name, int volume, int volumeOfAlc,  int imagePath, int favorite) {
        this.favorite = favorite;
        this.imagePath = imagePath;
        this.name = name;
        this.volume = volume;
        this.volumeOfAlc = volumeOfAlc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public int getImagePath() {
        return imagePath;
    }

    public void setImagePath(int imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getVolumeOfAlc() {
        return volumeOfAlc;
    }

    public void setVolumeOfAlc(int volumeOfAlc) {
        this.volumeOfAlc = volumeOfAlc;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.utility;

import javafx.scene.paint.Color;

/**
 *
 * @author chegini
 */
public enum Colours {
    Red("f44336"),
    Brown("795548"),
    Blue("2196f3"),
    Green("4caf50"),
    Orange("ff9800"),
    Purple("9c27b0"),
    Lime("cddc39"),
    Pink("ff4081"),
    Yellow("ffee58"),
    Cyan("4dd0e1"),
    Lavender("ce93d8"),
    Amber("ffc107"),
    Navy("283593"),
    Crismon("b71c1c"),
    Mint("a5d6a7"),
    Olive("827717"),
    DarkPurple("880e4f"),
    LighIndigo("c5cae9"),
    BlueGrey("607d8b");
    

    private final String Hex;

    private Colours(String Hex) {
        this.Hex = Hex;
    }

    public String getHex() {
        return this.Hex;
    }

    public String getHexHashtag() {
        return "#" + this.Hex;
    }
    
    public Color getColorWeb(){
        return Color.web(getHexHashtag());
    }
}

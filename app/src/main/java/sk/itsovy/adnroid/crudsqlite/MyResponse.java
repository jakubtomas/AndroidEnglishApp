package sk.itsovy.adnroid.crudsqlite;

import java.lang.reflect.Array;

public class MyResponse {

    public String word;
    public Array phonetics;
    public Array meanings;

    public MyResponse(String word, Array phonetics, Array meanings) {
        this.word = word;
        this.phonetics = phonetics;
        this.meanings = meanings;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Array getPhonetics() {
        return phonetics;
    }

    public void setPhonetics(Array phonetics) {
        this.phonetics = phonetics;
    }

    public Array getMeanings() {
        return meanings;
    }

    public void setMeanings(Array meanings) {
        this.meanings = meanings;
    }
}

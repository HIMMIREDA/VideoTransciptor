package com.ensa.videots;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
public final class Chronometer {

    IntegerProperty hh  = new SimpleIntegerProperty();
    IntegerProperty mm  = new SimpleIntegerProperty();
    IntegerProperty ss  = new SimpleIntegerProperty();
    IntegerProperty th  = new SimpleIntegerProperty();
    IntegerProperty hd  = new SimpleIntegerProperty();

    IntegerProperty cpt = new SimpleIntegerProperty();

    public Chronometer() {

    }

    void reset() {
        hh.set(0);
        mm.set(0);
        ss.set(0);
        th.set(0);
        hd.set(0);
        cpt.set(0);
    }

    void update() {
        cpt.set(cpt.get() + 1);
        hd.set(cpt.get() % 10);
        th.set((cpt.get() / 10) % 10);
        ss.set((cpt.get() / 100) % 60);
        mm.set((cpt.get() / 6000) % 60);
        hh.set((cpt.get() / 360000) % 24);
    }

}
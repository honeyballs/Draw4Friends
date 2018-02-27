package de.thm.draw4friends.Model;


/**
 * Created by Yannick Bals on 27.02.2018.
 */

public interface Subject {

    void registerObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers();

}

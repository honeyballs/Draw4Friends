package de.thm.draw4friends.Paint;

import java.util.ArrayList;
import java.util.List;

import de.thm.draw4friends.Model.Observer;
import de.thm.draw4friends.Model.Subject;

/**
 * Created by Farea on 27.02.2018.
 */

public class SubjectColor implements Subject{

    private String color;
    private List<Observer> observerList;

    public SubjectColor(String color) {
        this.color = color;
        this.observerList = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer observer) {
        this.observerList.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {

    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observerList) {
            observer.update(color);
        }
    }
}

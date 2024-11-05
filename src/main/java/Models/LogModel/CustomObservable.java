package Models.LogModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Views.LogView.*;

/**
 * Custom subject (observable) class responsible for managing observers.
 * 
 * @author Sumit Sharma
 * @author Muhammad Adnan Asad
 * @author Pranaykumar Chauhan
 * @author Darshansinh Dilipsinh Devda
 * @author Dhruvkumar Parmar
 * @author Dhruv Patel
 * @version 3.0.0
 */
public class CustomObservable implements Serializable {

    /**
     * List to maintain registered observers subscribed to this observable subject.
     */
    private List<CustomObserver> observers = new ArrayList<>();

    /**
     * Adds an observer to the list of registered observers.
     *
     * @param observer The observer to be added.
     */
    public void addObserver(CustomObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes a specific observer from the list of registered observers.
     *
     * @param observer The observer to be removed.
     */
    public void removeObserver(CustomObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all registered observers about changes in the observable.
     */
    public void notifyObservers() {
        for (CustomObserver observer : observers) {
            observer.update(this);
        }
    }
}

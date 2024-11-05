package Views.LogView;

import java.io.Serializable;

/**
 * Custom observer interface.
 * 
 * @author Sumit Sharma
 * @author Muhammad Adnan Asad
 * @author Pranaykumar Chauhan
 * @author Darshansinh Dilipsinh Devda
 * @author Dhruvkumar Parmar
 * @author Dhruv Patel
 * @version 3.0.0
 */
public abstract class CustomObserver implements Serializable {
    /**
     * This method is called when the observed object is changed.
     * 
     * @param observable The object that has changed and is being observed.
     */
    public abstract void update(Object observable);
}
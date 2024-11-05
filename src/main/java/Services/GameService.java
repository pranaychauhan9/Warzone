package Services;

import Constants.AppConstants;
import Models.GamePlayPhase;

import java.io.*;

/**
 * The GameService class load and save game file.
 * 
 * @author Sumit Sharma
 * @author Muhammad Adnan Asad
 * @author Pranaykumar Chauhan
 * @author Darshansinh Dilipsinh Devda
 * @author Dhruvkumar Parmar
 * @author Dhruv Patel
 * @version 3.0.0
 */
public class GameService {

    /**
     * Serialize and save current game to specific file.
     *
     * @param p_phase    instance of current game phase
     * @param p_filename name of the file
     */
    public static void saveGame(GamePlayPhase p_phase, String p_filename) {
        try {
            FileOutputStream l_gameSaveFile = new FileOutputStream(AppConstants.SRC_MAIN_RESOURCES + "/" + p_filename);
            ObjectOutputStream l_gameSaveFileObjectStream = new ObjectOutputStream(l_gameSaveFile);
            l_gameSaveFileObjectStream.writeObject(p_phase);
            l_gameSaveFileObjectStream.flush();
            l_gameSaveFileObjectStream.close();
        } catch (Exception l_e) {
            l_e.printStackTrace();
        }
    }

    /**
     * De-Serialize the Phase stored in specified file.
     *
     * @param p_filename name of file to load phase from
     * @return the Phase saved in file
     * @throws IOException            input output exception when file not found
     * @throws ClassNotFoundException if phase Phase class not found
     */
    public static GamePlayPhase loadGame(String p_filename) throws IOException, ClassNotFoundException {
        ObjectInputStream l_inputStream = new ObjectInputStream(
                new FileInputStream(AppConstants.SRC_MAIN_RESOURCES + "/" + p_filename));
        GamePlayPhase l_phase = (GamePlayPhase) l_inputStream.readObject();

        l_inputStream.close();
        return l_phase;
    }
}

package BookDirectory;

import java.util.logging.*;

/**
 * Точка входа в программу - файл для запуска.
 * @version 1.2
 */
public class Main
{
    /**
     * "Логгер" класса.
     */
    private static final Logger LOGGER = Logger.getLogger(Model.class.getName());

    public static void main(String[] args){
        /** Logger initialization **/
        try{
            Model.getProperties("BookDirectory.properties");
            if(!Model.startLogger("logs")){
                FileHandler fileHandler = new FileHandler("BookDirectoryLog.txt");
                fileHandler.setEncoding("UTF-8");
                Formatter formatTXT = new Model.TxtFormatter();
                fileHandler.setFormatter(formatTXT);
                LOGGER.addHandler(fileHandler);
                LOGGER.setUseParentHandlers(false);
            }
            LOGGER.log(Level.INFO, "Start application");
            LOGGER.log(Level.INFO, "Java version is ", System.getProperty("java.version") + ", vendor: " + System.getProperty("java.vendor"));
            LOGGER.log(Level.INFO, "Working directory is ", System.getProperty("user.dir"));
            LOGGER.log(Level.INFO, "System name is: ", System.getProperty("os.name") + " (version: " + System.getProperty("os.version") + ") " + ", system arch: " + System.getProperty("os.arch"));
            LOGGER.log(Level.INFO, "Is development mode? ", Model.isDevelopmentMode());
            LOGGER.log(Level.INFO, "The level of logging: ", Model.getLOG_LEVEL());

            /**Main engine **/
            Model.setLocaleWindows();
            View.getInstance().initializeView();
            //Controller.getInstance().mainMenuDraw();
            Controller.getInstance().mainMenuDrawNew();

            Runtime runtime = Runtime.getRuntime();
            runtime.gc();
            long memory = runtime.totalMemory() - runtime.freeMemory();
            if(LOGGER.isLoggable(Level.FINE)){
                LOGGER.log(Level.FINE, "The used memory (in MBytes):", Model.bytesToMegabytes(memory));
                LOGGER.log(Level.FINE, "The used memory (in Bytes):", memory);
            }

            LOGGER.log(Level.INFO, "End application");
        }
        catch (SecurityException e){View.getInstance().printErrorText(23);}
        catch (Exception e){
            View.getInstance().printErrorText(0);
            LOGGER.log(Level.SEVERE, "Application exception: " + e.toString() + "");
        }
    }
}
package BookDirectory;


import java.util.logging.Logger;


/**
 * Класс, отвечающий за функциональность главного меню программы.
 */
public class MainMenu extends Menu implements MenuInterface {

    /**
     * "Логгер" класса.
     */
    private final Logger LOGGER = Logger.getLogger(Model.class.getName());

    MainMenu() {
    }
}
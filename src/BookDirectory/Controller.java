package BookDirectory;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Контроллер - обеспечивает связь между пользователем и системой (модель MVC).
 * Использует модель и представление для реализации необходимой реакции.
 * Паттерн Singleton.
 *
 * @author dyakonov
 * @version 1.4
 */
public final class Controller {

    /**
     * Статическая переменная класса для хранения единственного экземпляра класса
     */
    private static Controller instance;

    /**
     * Конструктор класса с закрытым доступом (private).
     */
    private Controller() {
    }

    /**
     * "Логгер" класса.
     */
    private final Logger LOGGER = Logger.getLogger(Model.class.getName());

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public void mainMenuDraw() {
        try {
            boolean exitProgram = false;
            while (!exitProgram) {
                MainMenu mainMenu = new MainMenu();
                Integer choiceNew = mainMenu.checkMenuItem(0, '5');
                if (choiceNew == 0) {
                    exitProgram = true;
                } else {
                    boolean returnMainManu = false;
                    while (!returnMainManu) {
                        switch (choiceNew) {
                            case 1: {
                                SectionListings sectionListings = new SectionListings();
                                returnMainManu = sectionListings.action1();
                                break;
                            }
                            case 2: {
                                SectionBooks sectionBooks = new SectionBooks();
                                returnMainManu = true;
                                break;
                            }
                            case 3: {
                                SectionIllustrations sectionIllustrations = new SectionIllustrations();
                                returnMainManu = true;
                                break;
                            }
                            case 4: {
                                SectionExport sectionExport = new SectionExport();
                                returnMainManu = true;
                                break;
                            }
                            case 5: {
                                SectionImport sectionImport = new SectionImport();
                                returnMainManu = true;
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            View.getInstance().printErrorText(0);
            LOGGER.log(Level.SEVERE, "Application Error: " + e.toString() + "");
        }
    }
}
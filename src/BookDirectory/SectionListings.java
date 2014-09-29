package BookDirectory;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс, определяющий функциональность первого раздела программы - вывод каталога.
 */
public class SectionListings extends Menu implements MenuInterface {

    /**
     * "Логгер" класса.
     */
    private final Logger LOGGER = Logger.getLogger(Model.class.getName());

    @Override
    /**
     * Метод для вывода всего каталога.
     */
    public boolean action1() {
        if(LOGGER.isLoggable(Level.FINE)){
            LOGGER.fine("Menu select: list of books(1)");}
        View.getInstance().printMenu(1);
        if (Model.checkAvailabilityBooks()){
            for(Book book : Model.getBooks()){
                System.out.println(book);
            }
            if(LOGGER.isLoggable(Level.FINE)){
                LOGGER.fine("The end of the list of books in the catalog");}
        }
        return backToMainMenu();
    }
}

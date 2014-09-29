package BookDirectory;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс, определяющий функциональность второго раздела программы - операции с книгами (добавление в каталог, удаление из каталога).
 */
public class SectionBooks extends Menu implements MenuInterface {

    /**
     * "Логгер" класса.
     */
    private final Logger LOGGER = Logger.getLogger(Model.class.getName());

    /**
     * Конструктор класса. При его вызове - запрашивает у пользователя пункт меню для дальнейшей работы.
     */
    public SectionBooks(){
        int number = checkMenuItem(2, '2');
        if(number == 1){action1();}
        else if(number ==2) {action2();}
    }

    @Override
    /**
     * Метод для добавления книги в каталог.
     * @see #checkBookValues(String, String, String)
     * @see BookDirectory.Model#addBook(String, String, String)
     */
    public boolean action1() {
        if(LOGGER.isLoggable(Level.FINE)){
            LOGGER.fine("Menu select: add book(2-1)");}
        View.getInstance().printMenu(2);
        String[] inputBookValues = View.getInstance().printAddBookMenu();
        if(checkBookValues(inputBookValues[0], inputBookValues[1], inputBookValues[2])){
            Model.addBook(inputBookValues[0], inputBookValues[1], inputBookValues[2]);
        }
        return backToMainMenu();
    }

    @Override
    /**
     * Метод для удаления книги из каталога.
     * @see BookDirectory.Model#deleteBook(String)
     */
    public boolean action2() {
        if(LOGGER.isLoggable(Level.FINE)){
            LOGGER.fine("Menu select: delete book(2-2)");}
        View.getInstance().printSubMenuText(7);
        String isbn = Model.keyboardInput();
        if (Model.deleteBook(isbn)) {
            View.getInstance().printMessage(9);
        }
        else{
            View.getInstance().printErrorText(5);
            System.out.println("(" + isbn + ").");
            if(LOGGER.isLoggable(Level.FINE)){
                LOGGER.fine("Book for removal was not found in the catalog");}
        }
        return backToMainMenu();
    }
}
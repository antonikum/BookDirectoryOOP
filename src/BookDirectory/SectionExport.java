package BookDirectory;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс, определяющий функциональность четвёртого раздела программы - экспорта.
 */
public class SectionExport extends Menu implements MenuInterface {

    /**
     * "Логгер" класса.
     */
    private final Logger LOGGER = Logger.getLogger(Model.class.getName());

    /**
     * Конструктор класса. При его вызове - запрашивает у пользователя пункт меню для дальнейшей работы.
     */
    public SectionExport(){
        int number = checkMenuItem(4, '2');
        if(number == 1){action1();}
        else if(number ==2) {action2();}
    }

    @Override
    /**
     * Метод для экспорта одной книги (и всех её иллюстраций).
     */
    public boolean action1(){
        if(LOGGER.isLoggable(Level.FINE)){
            LOGGER.fine("Menu select: Export book(4-1)");}
        View.getInstance().printSubMenuText(1);
        String isbn = Model.keyboardInput();
        if(isbn.length() <= Model.getBookIsbnSize() && !(isbn.isEmpty())){
            boolean searchSuccess = false;
            for(Book book : Model.getBooks()){
                if(book.getIsbn().equals(isbn)){
                    if(Model.writeBookExport(book)){
                        View.getInstance().printMessage(7);
                        searchSuccess = true;
                    }
                }
            }
            if(!searchSuccess){View.getInstance().printErrorText(5);}
        }
        else View.getInstance().printMessage(8);
        return backToMainMenu();
    }

    @Override
    /**
     * Метод для экспорта всего каталога.
     */
    public boolean action2(){
        if(LOGGER.isLoggable(Level.FINE)){
            LOGGER.fine("Menu select: Export catalog(5-2)");}
        if (Model.checkAvailabilityBooks()){
            Model.writeCatalogExport("catalog_export.txt");
        }
        return backToMainMenu();
    }
}
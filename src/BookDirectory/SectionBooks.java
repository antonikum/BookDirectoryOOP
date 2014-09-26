package BookDirectory;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 */
public class SectionBooks extends Menu implements MenuInterface {

    /**
     * "Логгер" класса.
     */
    private final Logger LOGGER = Logger.getLogger(Model.class.getName());

    public SectionBooks(){
        int number = checkMenuItem(2, '2');
        if(number == 1){action1();}
        else if(number ==2) {action2();}
    }

    @Override
    public boolean action1() {
        /** добавление книжки */
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
    public boolean action2() {
        /** удаление книги */
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

    @Override
    public void action5() {

    }

    @Override
    public void action6() {

    }

    /**
     * Служебный метод для проверки допустимых значений атрибутов книги.
     * @param isbn String - Isbn книги.
     * @param title String - навзвание книги.
     * @param author String - автор книги.
     * @return Boolean: true, если переданные атрибуты соответствуют допустимым значениям, иначе - false.
     */
    public boolean checkBookValues(String isbn, String title, String author){
        boolean resultCheck = false;
        if(isbn.length() <= Model.getBookIsbnSize() &&
                title.length() <= Model.getBookTitleSize() &&
                author.length() <= Model.getBookAuthorSize() && !(isbn.isEmpty()) ){
            char forbidden[] = { '/', ':', '*', '|', '<', '>', '\"', '\''};
            for(char symbol : forbidden){
                Pattern pattern = Pattern.compile(".*["+symbol+"]+.*");
                if(pattern.matcher(isbn).lookingAt()){
                    resultCheck = false;
                    break;
                }
                else{
                    resultCheck = true;
                }
            }
            Pattern patternBackSlash = Pattern.compile(".*[\\\\]+.*");
            if(patternBackSlash.matcher(isbn).lookingAt()){
                resultCheck = false;
            }
            if(!resultCheck){View.getInstance().printErrorText(13);}
        }
        else if(!(isbn.length() <= Model.getBookIsbnSize()) || isbn.isEmpty()){
            View.getInstance().printMessage(8);
        }
        else if(!(title.length() <= Model.getBookTitleSize())){
            View.getInstance().printErrorText(11);
        }
        else {View.getInstance().printErrorText(12);}
        if(LOGGER.isLoggable(Level.FINE)){
            LOGGER.log(Level.FINE, "The result of check book values is: ", resultCheck);}
        return resultCheck;
    }
}

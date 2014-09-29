package BookDirectory;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Интерфейс для объектов - пунктов меню (разделов).
 */
interface MenuInterface{
    boolean action1();
    boolean action2();
    boolean action3();
    boolean action4();
}


/**
 * Абстрактный класс, содержащий общие методы для разделов программы.
 * Также в нём определны "заглушки" для методов action1, action2, action3, action4.
 */
public abstract class Menu {

    /**
     * "Логгер" класса.
     */
    private final Logger LOGGER = Logger.getLogger(Model.class.getName());

    /**
     * Служебный метод для навигация по пунктам меню.
     * @param numberOfSubMenu - номер раздела, в котором находимся (0 - если главное меню).
     * @param endPattern - количество пунктов меню в разделе.
     * @return выбранный пункт меню раздела. 0 - если главное меню.
     */
    public Integer checkMenuItem(Integer numberOfSubMenu, char endPattern) {
        View.getInstance().printMenu(numberOfSubMenu);
        Integer select=0;
        boolean exit = false;
        try {
            while (!exit){
                View.getInstance().printMessage(15);
                String inputMenu = Model.keyboardInput();
                /** пустая строка - выход из метода с select=0 */
                if (inputMenu.isEmpty()) {exit = true;}
                /** если строка не пустая */
                else{
                    Pattern pattern = Pattern.compile("[1-"+endPattern+"]");
                    boolean result = pattern.matcher(inputMenu).matches();
                    if(result){
                        /** пункт меню прошёл проверку - передаём его */
                        select = Integer.parseInt(inputMenu);
                        exit = true;
                    }
                    else{
                        View.getInstance().printErrorText(1);
                        View.getInstance().printMenu(numberOfSubMenu);
                        if(LOGGER.isLoggable(Level.FINE)){
                            LOGGER.log(Level.FINE, "Error in a menu selection");}
                    }
                }
            }
        }
        catch (Exception e){View.getInstance().printErrorText(0);}
        if(LOGGER.isLoggable(Level.FINE)){
            LOGGER.log(Level.FINE, "The result of check menu selection is: ", exit);}
        return select;
    }

    /**
     * Служебный метод для возврата в главное меню программы.
     * @return Boolean
     */
    public boolean backToMainMenu(){
        if(LOGGER.isLoggable(Level.FINE)){
            LOGGER.fine("Back to main menu.");}
        View.getInstance().printSubMenuText(0);
        Model.keyboardInput();
        return true;
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

    /**
     * Служебный метод для проверки допустимых значений атрибутов иллюстрации.
     * @param imageId String - Id иллюстрации.
     * @param name String - название иллюстрации.
     * @param author String - автор иллюстрации.
     * @return Boolean: true, если переданные атрибуты соответствуют допустимым значениям, иначе - false.
     */
    public boolean checkIllustrationValues(String imageId, String name, String author){
        boolean resultCheck = false;
        if(imageId.length() <= Model.getIllustrationIdSize() && name.length() <= Model.getIllustrationNameSize() && author.length() <= Model.getIllustrationAuthorSize() && !(imageId.isEmpty())){
            resultCheck = true;
        }
        else if(!(imageId.length() <= Model.getIllustrationIdSize()) || imageId.isEmpty()){
            View.getInstance().printErrorText(8);
        }
        else if(!(name.length() <= Model.getIllustrationNameSize())){
            View.getInstance().printErrorText(11);
        }
        else {View.getInstance().printErrorText(12);}
        if(LOGGER.isLoggable(Level.FINE)){
            LOGGER.log(Level.FINE, "The result of check illustration values is: ", resultCheck);}
        return resultCheck;
    }

    public boolean action1(){
        return false;
    }

    public boolean action2(){
        return false;
    }

    public boolean action3(){
        return false;
    }

    public boolean action4(){
        return false;
    }
}
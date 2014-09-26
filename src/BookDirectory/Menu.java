package BookDirectory;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

interface MenuInterface{
    boolean action1();
    boolean action2();
    boolean action3();
    boolean action4();
    void action5();
    void action6();
}


/**
 *
 */
public class Menu {

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

    public void printMenu(Integer number) {
        View.getInstance().printMenu(number);
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

    public boolean action1(){
        boolean result = false;
        return result;
    }

    public boolean action2(){
        boolean result = false;
        return result;
    }

    public boolean action3(){
        boolean result = false;
        return result;
    }

    public boolean action4(){
        boolean result = false;
        return result;
    }
}

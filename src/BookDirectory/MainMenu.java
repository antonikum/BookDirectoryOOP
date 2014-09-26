package BookDirectory;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;



/**
 *
 */
public class MainMenu extends Menu implements MenuInterface {

    /**
     * "Логгер" класса.
     */
    private final Logger LOGGER = Logger.getLogger(Model.class.getName());

    MainMenu(){
    }

    public MenuInterface getSubMenu(Integer number){
        MenuInterface menuInterface=null;
        switch (number){
            case 1: {menuInterface = new SectionListings();break;}
            case 2: {menuInterface = new SectionBooks();break;}
            case 3: {menuInterface = new SectionIllustrations();break;}
            case 4: {menuInterface = new SectionExport();break;}
            case 5: {menuInterface = new SectionImport(); break;}
        }
        return menuInterface;
    }

    @Override
    public void action5() {

    }

    @Override
    public void action6() {

    }
}

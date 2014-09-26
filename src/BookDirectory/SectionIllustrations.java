package BookDirectory;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class SectionIllustrations extends Menu implements MenuInterface {

    /**
     * "Логгер" класса.
     */
    private final Logger LOGGER = Logger.getLogger(Model.class.getName());

    @Override
    public boolean action1(){
        View.getInstance().printSubMenuText(2);
        if(LOGGER.isLoggable(Level.FINE)){
            LOGGER.fine("Menu select: add illustration(4-1)");}
        if(Model.checkAvailabilityBooks()){
            try{
                View.getInstance().printSubMenuText(3);
                Integer bookCount = Integer.parseInt(Model.keyboardInput());
                if(LOGGER.isLoggable(Level.FINE)){
                    LOGGER.log(Level.FINE, "The number of books to which user want to add an illustration: ", bookCount);}
                ArrayList<String> isbns = new ArrayList<String>();
                for (int i = 0; i < bookCount; i++){
                    View.getInstance().printSubMenuText(1);
                    String isbn = Model.keyboardInput();
                    Book book = Model.getBookByISbn(isbn);
                    if(book != null){
                        isbns.add(isbn);
                    }
                    else{
                        View.getInstance().printErrorText(5);
                        System.out.println("(" + isbn + ").");
                    }
                }
                if (!(isbns.isEmpty())){
                    View.getInstance().printMessage(10);
                    for(String isbn : isbns){
                        System.out.println(Model.getBookByISbn(isbn));
                    }
                    if(LOGGER.isLoggable(Level.FINE)){
                        LOGGER.log(Level.FINE, "Number of books, which will be added to illustration: ", isbns.size());}
                    String[] inputIllustrValues = View.getInstance().printAddIllustrationMenu();
                    if(checkIllustrationValues(inputIllustrValues[0], inputIllustrValues[1], inputIllustrValues[2])){
                        for(String isbn : isbns){
                            Book book = Model.getBookByISbn(isbn);
                            if(book.getIllustrations().size() > 0){
                                boolean addFlag = true;
                                for(Book.Illustration illustration : book.getIllustrations()){
                                    if(illustration.getId().equals(inputIllustrValues[0])){
                                        System.out.println("\nОшибка, у книги(ISBN: "+book.getIsbn()+ ") уже есть иллюстрация с таким Id.\n");
                                        if(LOGGER.isLoggable(Level.WARNING)){
                                            LOGGER.log(Level.WARNING, "The book in the catalog already has illustration with such id");}
                                        addFlag = false;
                                    }
                                }
                                if(addFlag){Model.addIllustration(isbn, inputIllustrValues[0], inputIllustrValues[1], inputIllustrValues[2]);}
                            }
                            else{Model.addIllustration(isbn, inputIllustrValues[0], inputIllustrValues[1], inputIllustrValues[2]);}
                        }
                    }
                }
            }
            catch (NumberFormatException e){View.getInstance().printErrorText(7);}
        }
        return backToMainMenu();
    }

    @Override
    public boolean action2(){
        if(LOGGER.isLoggable(Level.FINE)){
            LOGGER.fine("Menu select: delete illustration(4-2)");}
        View.getInstance().printSubMenuText(4);
        String imageId = Model.keyboardInput();
        if(imageId.length() < Model.getIllustrationIdSize()){
            Model.deleteIllustrationById(imageId);
        }
        else{View.getInstance().printErrorText(8);}
        return backToMainMenu();
    }

    @Override
    public boolean action3() {
        if(LOGGER.isLoggable(Level.FINE)){
            LOGGER.fine("Menu select: Search Books by illustration(4-3)");}
        View.getInstance().printSubMenuText(5);
        String imageId = Model.keyboardInput();
        boolean successSearchId = false;
        if(imageId.length() < Model.getIllustrationIdSize()){
            try{
                for(Book book : Model.getBooks()){
                    ArrayList<Book.Illustration> illustrations = book.getIllustrations();
                    if(illustrations.size() > 0){
                        for(Book.Illustration illustration : illustrations){
                            if(illustration.getId().equals(imageId)){
                                System.out.println(book);
                                successSearchId = true;
                            }
                        }
                    }
                }
                if(!successSearchId){View.getInstance().printErrorText(9);}
            }
            catch (Exception e){
                View.getInstance().printErrorText(0);
                LOGGER.log(Level.SEVERE, "Application error: " + e.toString() + "");
            }
        }
        else{View.getInstance().printErrorText(8);}
        return backToMainMenu();
    }

    @Override
    public boolean action4() {

        return backToMainMenu();
    }

    @Override
    public void action5() {

    }

    @Override
    public void action6() {

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
}

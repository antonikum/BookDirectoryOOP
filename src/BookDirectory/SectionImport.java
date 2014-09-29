package BookDirectory;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс, определяющий функциональность шестого раздела программы - импорта.
 */
public class SectionImport extends Menu implements MenuInterface {

    /**
     * "Логгер" класса.
     */
    private final Logger LOGGER = Logger.getLogger(Model.class.getName());

    /**
     * Конструктор класса. При его вызове - запрашивает у пользователя пункт меню для дальнейшей работы.
     */
    public SectionImport() {
        int number = checkMenuItem(5, '2');
        if (number == 1) {
            action1();
        } else if (number == 2) {
            action2();
        }
    }

    @Override
    /**
     * Метод для импорта одной книги и всех иллюстраций из текстового файла.
     */
    public boolean action1() {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Menu select: Import book(5-1)");
        }
        View.getInstance().printSubMenuText(6);
        String fileText = FileInOut.getInstance().readFile(Model.keyboardInput() + ".txt", "import");
        int textSize = fileText.length();
        try {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info("Search for book in the import file");
            }
            fileText = fileText.replace("\n\r", "\n");
            if (fileText.length() > 0) { //проверка, что файл существует
                /** поиск книги и попытка добавить её в каталог **/
                String[] bookValues = findValuesBook(fileText);
                if (bookValues[0] != null) {
                    if (Model.getBookByISbn(bookValues[0]) == null) {
                        if (checkBookValues(bookValues[0], bookValues[1], bookValues[2])) {
                            Model.addBook(bookValues[0], bookValues[1], bookValues[2]);
                        }
                    } else {
                        View.getInstance().printErrorText(15);
                        System.out.println(bookValues[0]);
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.log(Level.WARNING, "Found the book already exists in the directory: ", bookValues[0]);
                        }
                    }
                } else {
                    View.getInstance().printErrorText(16);
                }
                /** Поиск всех иллюстраций в файле импорта и попытка добавить их в каталог
                 *  начало **/
                View.getInstance().printMessage(13);
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("Search for illustrations in the import file");
                }
                /** проверка, что в тексте присутствует тег [Иллюстрация] **/
                if (findValuesIllustration(fileText)[4] != null) {
                    int offset = 0;
                    Set<String[]> illustrationsForImport = new LinkedHashSet<String[]>();
                    while (offset < textSize - 2) {
                        String buffer = fileText.substring(offset, textSize);
                        String[] illustrationValues = findValuesIllustration(buffer);
                        /** проверка дальнейшей целесообразности просмотра текста **/
                        if (illustrationValues[4] == null) {
                            break;
                        }
                        if (checkIllustrationValues(illustrationValues[0], illustrationValues[2], illustrationValues[3])) {
                            illustrationsForImport.add(illustrationValues);
                        }
                        offset += Integer.parseInt(illustrationValues[4]);
                    }
                    Set<String[]> isbnsAndIds = new HashSet<String[]>();
                    /** получаем список пар isbn-id в каталоге **/
                    for (Book book : Model.getBooks()) {
                        String[] isbn_andId = new String[2];
                        if (book.getIllustrations().size() > 0) {
                            for (Book.Illustration illustration : book.getIllustrations()) {
                                isbn_andId[0] = illustration.getIsbn();
                                isbn_andId[1] = illustration.getId();
                                isbnsAndIds.add(isbn_andId);
                            }
                        } else {
                            isbn_andId[0] = book.getIsbn();
                            isbnsAndIds.add(isbn_andId);
                        }
                    }
                    for (String[] illustrationValues : illustrationsForImport) {
                        boolean isbnFound = false;
                        for (String[] isbnAndId : isbnsAndIds) {
                            /** проверили, что книга с таким isbn есть в каталоге **/
                            if (isbnAndId[0].equals(illustrationValues[1])) {
                                isbnFound = true;
                                if (isbnAndId[1] != null) {
                                    /** проверка, что у книги нет иллюстрации с таким id */
                                    if (!isbnAndId[1].equals(illustrationValues[0])) {
                                        Model.addIllustration(illustrationValues[1], illustrationValues[0], illustrationValues[2], illustrationValues[3]);
                                    } else {
                                        System.out.println("Ошибка! В каталоге у книги с Isbn=" + illustrationValues[1] + " уже есть иллюстрация с Id=" + illustrationValues[0]);
                                        if (LOGGER.isLoggable(Level.WARNING)) {
                                            LOGGER.log(Level.WARNING, "illustration found (with id= " + illustrationValues[0] + ") already exists in the catalog and added to the book with isbn: ", illustrationValues[1]);
                                        }
                                    }
                                } else {
                                    Model.addIllustration(illustrationValues[1], illustrationValues[0], illustrationValues[2], illustrationValues[3]);
                                }
                                break;
                            }
                        }
                        if (!isbnFound) {
                            System.out.println("\nОшибка! Не получилось добавить в каталог иллюстрацию с id=" + illustrationValues[0] + "\nВ каталоге отсутствует книга с ISBN=" + illustrationValues[1] + "\n");
                            if (LOGGER.isLoggable(Level.WARNING)) {
                                LOGGER.log(Level.WARNING, "illustration found (with ID= " + illustrationValues[0] + ") can not be added to the catalog, there is no book with isbn: ", illustrationValues[1]);
                            }
                        }
                    }
                }
                /** Поиск всех иллюстраций в файле импорта и попытка добавить их в каталог
                 *  конец **/
            }
        } catch (Exception e) {
            View.getInstance().printErrorText(17);
        }
        return backToMainMenu();
    }

    @Override
    /**
     * Метод для импорта всех книг и иллюстраций из файла импорта.
     */
    public boolean action2() {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Menu select: Import catalog(5-2)");
        }
        /** импорт каталога из файла **/
        View.getInstance().printSubMenuText(6);
        String fileText = FileInOut.getInstance().readFile(Model.keyboardInput() + ".txt", "import");
        int textSize = fileText.length();
        try {
            fileText = fileText.replace("\n\r", "\n");
            if (fileText.length() > 0) {
                /** поиск книг и попытка добавить их в каталог **/
                try {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.info("Search for books in the import file");
                    }
                    /** проверка, что в тексте присутствует тег [Книга] **/
                    if (findValuesBook(fileText)[3] != null) {
                        long startTime = System.currentTimeMillis();
                        int offset = 0;
                        Set<Book> bookImportHashSet = new LinkedHashSet<Book>();
                        while (offset < textSize - 2) {
                            String buffer = fileText.substring(offset, textSize);
                            String[] bookValues = findValuesBook(buffer);
                            if (bookValues[3] == null) {
                                break;
                            }
                            /** проверяем атрибуты книги и добавляем к сету **/
                            if (checkBookValues(bookValues[0], bookValues[1], bookValues[2])) {
                                bookImportHashSet.add(Book.getBook(bookValues[0], bookValues[1], bookValues[2]));
                            }
                            offset += Integer.parseInt(bookValues[3]);
                        }
                        if (LOGGER.isLoggable(Level.FINE)) {
                            LOGGER.log(Level.FINE, "Number of books found in the import file: ", bookImportHashSet.size());
                        }
                        Set<String> isbnsInCatalog = new HashSet<String>();
                        /** получаем список isbn книг в каталоге **/
                        for (Book book : Model.getBooks()) {
                            isbnsInCatalog.add(book.getIsbn());
                        }
                        for (Book book : bookImportHashSet) {
                            if (!isbnsInCatalog.contains(book.getIsbn())) {
                                Model.addBook(book.getIsbn(), book.getName(), book.getAuthor());
                            } else {
                                View.getInstance().printErrorText(15);
                                System.out.println(book.getIsbn());
                                if (LOGGER.isLoggable(Level.WARNING)) {
                                    LOGGER.log(Level.WARNING, "Found the book already exists in the directory: ", book.getIsbn());
                                }
                            }
                        }
                        long stopTime = System.currentTimeMillis();
                        long elapsedTime = stopTime - startTime;
                        if (LOGGER.isLoggable(Level.FINE)) {
                            LOGGER.log(Level.FINE, "The time spent on the import of all books (in ms):", elapsedTime);
                        }
                    } else {
                        View.getInstance().printErrorText(17);
                    }
                } catch (Exception e) {
                    View.getInstance().printErrorText(0);
                    LOGGER.log(Level.SEVERE, "Application error: " + e.toString() + "");
                }
                /** Поиск всех иллюстраций в файле импорта и попытка добавить их в каталог
                 *  начало **/
                View.getInstance().printMessage(13);
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("Search for illustrations in the import file");
                }
                /** проверка, что в тексте присутствует тег [Иллюстрация] **/
                if (findValuesIllustration(fileText)[4] != null) {
                    int offset = 0;
                    Set<String[]> illustrationsForImport = new LinkedHashSet<String[]>();
                    while (offset < textSize - 2) {
                        String buffer = fileText.substring(offset, textSize);
                        String[] illustrationValues = findValuesIllustration(buffer);
                        /** проверка дальнейшей целесообразности просмотра текста **/
                        if (illustrationValues[4] == null) {
                            break;
                        }
                        if (checkIllustrationValues(illustrationValues[0], illustrationValues[2], illustrationValues[3])) {
                            illustrationsForImport.add(illustrationValues);
                        }
                        offset += Integer.parseInt(illustrationValues[4]);
                    }
                    Set<String[]> isbnsAndIds = new HashSet<String[]>();
                    /** получаем список пар isbn-id в каталоге **/
                    for (Book book : Model.getBooks()) {
                        String[] isbn_andId = new String[2];
                        if (book.getIllustrations().size() > 0) {
                            for (Book.Illustration illustration : book.getIllustrations()) {
                                isbn_andId[0] = illustration.getIsbn();
                                isbn_andId[1] = illustration.getId();
                                isbnsAndIds.add(isbn_andId);
                            }
                        } else {
                            isbn_andId[0] = book.getIsbn();
                            isbnsAndIds.add(isbn_andId);
                        }
                    }
                    for (String[] illustrationValues : illustrationsForImport) {
                        boolean isbnFound = false;
                        for (String[] isbnAndId : isbnsAndIds) {
                            /** проверили, что книга с таким isbn есть в каталоге **/
                            if (isbnAndId[0].equals(illustrationValues[1])) {
                                isbnFound = true;
                                if (isbnAndId[1] != null) {
                                    /** проверка, что у книги нет иллюстрации с таким id */
                                    if (!isbnAndId[1].equals(illustrationValues[0])) {
                                        Model.addIllustration(illustrationValues[1], illustrationValues[0], illustrationValues[2], illustrationValues[3]);
                                    } else {
                                        System.out.println("Ошибка! В каталоге у книги с Isbn=" + illustrationValues[1] + " уже есть иллюстрация с Id=" + illustrationValues[0]);
                                        if (LOGGER.isLoggable(Level.WARNING)) {
                                            LOGGER.log(Level.WARNING, "illustration found (with id= " + illustrationValues[0] + ") already exists in the catalog and added to the book with isbn: ", illustrationValues[1]);
                                        }
                                    }
                                } else {
                                    Model.addIllustration(illustrationValues[1], illustrationValues[0], illustrationValues[2], illustrationValues[3]);
                                }
                                break;
                            }
                        }
                        if (!isbnFound) {
                            System.out.println("\nОшибка! Не получилось добавить в каталог иллюстрацию с id=" + illustrationValues[0] + "\nВ каталоге отсутствует книга с ISBN=" + illustrationValues[1] + "\n");
                            if (LOGGER.isLoggable(Level.WARNING)) {
                                LOGGER.log(Level.WARNING, "illustration found (with ID= " + illustrationValues[0] + ") can not be added to the catalog, there is no book with isbn: ", illustrationValues[1]);
                            }
                        }
                    }
                }
                /** Поиск всех иллюстраций в файле импорта и попытка добавить их в каталог
                 *  конец **/
            }
        } catch (Exception e) {
            View.getInstance().printErrorText(17);
            LOGGER.log(Level.SEVERE, "Application or format of file error: " + e.toString() + "");
        }
        return backToMainMenu();
    }

    /**
     * Служебный метод для поиска атрибутов книги в тексте файла импорта.
     *
     * @param fileText String - текст для поиска.
     * @return String[4], где [0] - isbn книги, [1] - название книги, [2] - автор книги, [3] - позиция, на котором поиск остановился.
     */
    private String[] findValuesBook(String fileText) {
        String[] values = new String[4];
        if (fileText.contains("[Книга]")) { //проверка на наличие тега
            int beginStr = fileText.indexOf("[Книга]"); //символ начала тега книга
            int endStr = fileText.indexOf('\n', beginStr); //номер символа - конец тега книга
            values[0] = fileText.substring(endStr + 1, fileText.indexOf('\n', endStr + 1)).trim();
            beginStr = fileText.indexOf('\n', endStr + 1) + 1; // номер символа - начало названия книги
            endStr = fileText.indexOf('\n', beginStr); // номер символа - конец названия книги
            values[1] = fileText.substring(beginStr, endStr).trim();
            beginStr = endStr + 1; // номер символа - начало автора книги
            endStr = fileText.indexOf('\n', beginStr); // номер символа - конец автора книги
            values[2] = fileText.substring(beginStr, endStr).trim();
            values[3] = String.valueOf(endStr);
        } else {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning("Tag [Book] is not found text file ");
            }
        }
        return values;
    }

    /**
     * Служебный метод для поиска атрибутов иллюстрации в тексте файла импорта.
     *
     * @param fileText String - текст для поиска.
     * @return String[5], где [0] - id иллюстрации, [1] - isbn книги, [2] - название илюстрации, [3] - автор иллюстрации, [4] - позиция, на котором поиск остановился.
     */
    private String[] findValuesIllustration(String fileText) {
        String[] values = new String[5];
        if (fileText.contains("[Иллюстрация]")) { //проверка на наличие тега
            int beginStr = fileText.indexOf("[Иллюстрация]"); //символ начала тега книга
            int endStr = fileText.indexOf('\n', beginStr); //номер символа - конец тега книга
            values[0] = fileText.substring(endStr + 1, fileText.indexOf('\n', endStr + 1)).trim();
            beginStr = fileText.indexOf('\n', endStr + 1) + 1; // номер символа - начало названия книги
            endStr = fileText.indexOf('\n', beginStr); // номер символа - конец названия книги
            values[1] = fileText.substring(beginStr, endStr).trim();
            beginStr = endStr + 1; // номер символа - начало автора книги
            endStr = fileText.indexOf('\n', beginStr); // номер символа - конец автора книги
            values[2] = fileText.substring(beginStr, endStr).trim();
            beginStr = endStr + 1;
            endStr = fileText.indexOf('\n', beginStr);
            values[3] = fileText.substring(beginStr, endStr).trim();
            values[4] = String.valueOf(endStr);
        } else {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning("Tag [Illustration] is not found text file ");
            }
        }
        return values;
    }
}

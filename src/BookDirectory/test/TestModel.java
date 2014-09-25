package BookDirectory.test;

import BookDirectory.Model;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Тестовый класс для проверки методов класса Model.
 * @see BookDirectory.Model
 */
public class TestModel {

    @Test
    public void testEncoding_trueReturned() throws Exception{
        //arrange
        boolean expected = true;

        //act
        boolean result = Model.encoding().length() > 0;

        //assert
        assertTrue("Encoding string is empty!", result==expected);
    }
}
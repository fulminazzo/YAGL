import org.gradle.internal.impldep.org.junit.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class JavaDocUtilsTest {

    @Test
    void testCommonPaths() {
        String expected = System.getProperty("user.dir") + "/this/path/is/expected/";
        File path1 = new File(expected, "this/is/not");
        File path2 = new File(expected, "me/neither");

        Object actual = JavaDocUtils.commonPath(path1, path2);
        assertEquals(expected, actual);
    }

}
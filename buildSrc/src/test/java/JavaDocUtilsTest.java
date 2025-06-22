import it.fulminazzo.yamlparser.utils.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class JavaDocUtilsTest {

    @Test
    void testCopy() throws IOException {
        File parent = new File("build/resources/test");
        if (parent.exists()) FileUtils.deleteFolder(parent);
        FileUtils.createFolder(parent);
        // Setup
        File dir1 = new File(parent, "dir1");
        dir1.mkdir();
        File file1 = new File(dir1, "file1");
        file1.createNewFile();
        FileUtils.writeToFile(file1, "Hello");
        File file2 = new File(dir1, "file2");
        file2.createNewFile();
        FileUtils.writeToFile(file2, "World");

        // Copy
        File dir2 = new File(parent, "dir2");
        JavaDocUtils.copyDirectory(dir1, dir2);

        // Verify
        assertTrue(dir2.isDirectory(), String.format("No directory %s", dir2.getAbsolutePath()));

        file1 = new File(dir2, "file1");
        assertTrue(file1.exists(), String.format("No file %s", file1.getAbsolutePath()));
        assertEquals("Hello", FileUtils.readFileToString(file1));

        file2 = new File(dir2, "file2");
        assertTrue(file2.exists(), String.format("No file %s", file2.getAbsolutePath()));
        assertEquals("World", FileUtils.readFileToString(file2));
    }

    @Test
    void testCommonPaths() {
        String expected = System.getProperty("user.dir") + "/this/path/is/expected/";
        File path1 = new File(expected, "this/is/not");
        File path2 = new File(expected, "me/neither");

        Object actual = JavaDocUtils.commonPath(path1, path2);
        assertEquals(expected, actual);
    }

    @Test
    void testGetResource() throws IOException {
        InputStream stream = (InputStream) JavaDocUtils.getResource("mock.txt");
        assertNotNull(stream);
        StringBuilder builder = new StringBuilder();
        while (stream.available() > 0) builder.append((char) stream.read());
        assertEquals("Hello world", builder.toString());
    }

    @Test
    void testInvalidResource() {
        assertThrowsExactly(IllegalArgumentException.class, () -> JavaDocUtils.getResource("invalid"));
    }

}

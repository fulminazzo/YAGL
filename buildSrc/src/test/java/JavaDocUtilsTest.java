import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class JavaDocUtilsTest {

    @Test
    void testCopy() throws IOException {
        File parent = new File("build/resources/test/copy_test");
        if (parent.exists()) deleteDir(parent);
        assertTrue(parent.mkdirs(), "Could not create directory " + parent.getAbsolutePath());
        // Setup
        File dir1 = new File(parent, "dir1");
        assertTrue(dir1.mkdir(), "Could not create directory " + dir1.getAbsolutePath());
        File file1 = new File(dir1, "file1");
        assertTrue(file1.createNewFile(), "Could not create file " + file1.getAbsolutePath());
        writeToFile(file1, "Hello");
        File file2 = new File(dir1, "file2");
        assertTrue(file2.createNewFile(), "Could not create file " + file2.getAbsolutePath());
        writeToFile(file2, "World");

        // Copy
        File dir2 = new File(parent, "dir2");
        JavaDocUtils.copyDirectory(dir1, dir2);

        // Verify
        assertTrue(dir2.isDirectory(), String.format("No directory %s", dir2.getAbsolutePath()));

        file1 = new File(dir2, "file1");
        assertTrue(file1.exists(), String.format("No file %s", file1.getAbsolutePath()));
        assertEquals("Hello", readFileToString(file1));

        file2 = new File(dir2, "file2");
        assertTrue(file2.exists(), String.format("No file %s", file2.getAbsolutePath()));
        assertEquals("World", readFileToString(file2));
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

    private static void deleteDir(final @NotNull File directory) {
        File[] files = directory.listFiles();
        assertNotNull(files, "Could not list files in directory " + directory.getAbsolutePath());
        for (File file : files)
            if (file.isDirectory()) deleteDir(file);
            else assertTrue(file.delete(), "Could not delete file " + file.getAbsolutePath());
        assertTrue(directory.delete(), "Could not delete directory " + directory.getAbsolutePath());
    }

    private static @NotNull String readFileToString(final @NotNull File file) throws IOException {
        try (FileInputStream input = new FileInputStream(file)) {
            return new String(input.readAllBytes());
        }
    }

    private static void writeToFile(final @NotNull File file, final @NotNull String content) throws IOException {
        try (FileOutputStream output = new FileOutputStream(file)) {
            output.write(content.getBytes());
        }
    }

}

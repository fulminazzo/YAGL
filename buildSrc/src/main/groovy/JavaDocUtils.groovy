import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * A collection of utilities to aggregate Javadocs
 */
class JavaDocUtils {
    private static final def DOCS_DIR = "javadoc"
    private static final def IGNORE_DIRS = [ "main", "java", "groovy", "src", "buildSrc", "resources" ]
    private static final def RESOURCES = [ "index.html", "javadoc-stylesheet.css" ]
    private static final def MODULE_PLACEHOLDER = "%modules%"
    private static final def MODULE_FORMAT_FILE = "module-format.html"

    /**
     * Aggregates the javadoc of all the root and subprojects into a single directory
     *
     * @param output the output directory
     * @param ignoreDirs the directories (modules) to be ignore
     */
    static def aggregateJavaDoc(String output, String name, String version, String... ignoreDirs) {
        def current = new File(System.getProperty("user.dir"))
        if (current.getName() == "buildSrc") current = current.getParentFile()
        def outputDir = new File(current, output)

        if (outputDir.exists() && !outputDir.deleteDir())
            throw new IllegalStateException("Could not delete previous directory ${output}")
        if (!outputDir.mkdirs()) throw new IllegalStateException("Could not create directory ${output}")

        aggregateJavaDocRec(current, outputDir, ignoreDirs)
        createModulesPage(name, version, outputDir)
    }

    private static def createModulesPage(String name, String version, File file) {
        if (!file.isDirectory()) return
        def files = file.listFiles()
        if (files == null) return
        if (files.any { it.getName().contains(".html") } ) return

        RESOURCES.each { parseResource(file, it, name, version, files) }

        files.each { createModulesPage(it.getName(), version, it) }
    }

    private static def parseResource(File parentFile, String resource, String name, String version, File[] files) {
        JavaDocUtils.class.getResourceAsStream("/${resource}").withReader { reader ->
            new File(parentFile, resource).withWriter { writer ->
                String line
                while ((line = reader.readLine()) != null) {
                    line = line
                            .replace("%module_name%", name)
                            .replace("%module_version%", version)
                    if (line.contains(MODULE_PLACEHOLDER))
                        line = parseModulesPlaceholder(line, files)
                    writer.write(line)
                    writer.write('\n')
                }
            }
        }
    }

    private static def parseModulesPlaceholder(String line, File[] files) {
        String output = ""
        for (f in files)
            JavaDocUtils.class.getResourceAsStream("/${MODULE_FORMAT_FILE}").withReader { r -> {
                String l
                while ((l = r.readLine()) != null)
                    output += l.replace("%submodule_name%", f.getName())
                            .replace("%submodule_path%", "${f.getName()}${File.separator}index.html")
            }}
        return line.replace(MODULE_PLACEHOLDER, output)
    }

    private static def aggregateJavaDocRec(File current, File output, String... ignoreDirs) {
        if (!current.isDirectory()) return

        def files = current.listFiles()
        if (files == null) return

        main_loop:
        for (file in files) {
            def fileName = file.getName()
            for (dir in IGNORE_DIRS)
                if (fileName == dir) continue main_loop
            for (dir in ignoreDirs)
                if (fileName == dir) continue main_loop

            if (file.isDirectory())
                if (file.getName() == DOCS_DIR) {
                    def dest = getDestinationFromModule(output, file)
                    copyDirectory(file, new File(output, dest))
                } else aggregateJavaDocRec(file, output)
        }
    }

    /**
     * Copies the given file.
     *
     * @param src the source (copied) file
     * @param dst the destination (copy) file
     */
    static def copyDirectory(File src, File dst) {
        if (src.isDirectory()) {
            def files = src.listFiles()
            dst.mkdirs()
            if (files != null)
                for (file in files)
                    copyDirectory(new File(src, file.getName()), new File(dst, file.getName()))
        } else Files.copy(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING)
    }

    private static def getDestinationFromModule(File output, File file) {
        def parent = new File(commonPath(output, file))
        def module = new File(file, "")
        while (module.getParentFile().getParentFile() != parent)
            module = module.getParentFile()
        def moduleName = module.getName()

        def dst = "${module.getParentFile().getName()}"
        if (moduleName != "build") dst += "${File.separator}${module.getName()}"
        return dst
    }

    /**
     * Finds the most common path between the two given files
     *
     * @param file1 the first file
     * @param file2 the second file
     * @return the common path
     */
    static def commonPath(File file1, File file2) {
        def path1 = file1.getAbsolutePath()
        def path2 = file2.getAbsolutePath()
        def result = ""

        for (i in 0..Math.min(path1.length(), path2.length())) {
            def c1 = path1[i]
            def c2 = path2[i]
            if (c1 != c2) break
            else result += c1
        }

        return result
    }
}

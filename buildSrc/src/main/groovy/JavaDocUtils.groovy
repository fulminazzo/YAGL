import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * A collection of utilities to aggregate Javadocs
 */
class JavaDocUtils {
    private static final def DOCS_DIR = 'javadoc'
    private static final def IGNORE_DIRS = [ 'main', 'java', 'groovy', 'src', 'buildSrc', 'resources' ]
    private static final def RESOURCES = [ 'index.html', 'javadoc-stylesheet.css' ]
    private static final def MODULE_PLACEHOLDER = '%modules%'
    private static final def MODULE_FORMAT_FILE = 'module-format.html'

    /**
     * Aggregates the javadoc of all the root and subprojects into a single directory
     *
     * @param output the output directory
     * @param ignoreDirs the directories (modules) to be ignore
     */
    static void aggregateJavaDoc(String output, String name, String version, String... ignoreDirs) {
        def current = new File(System.getProperty('user.dir'))
        if (current.name == 'buildSrc') current = current.parentFile
        def outputDir = new File(current, output)

        if (outputDir.exists() && !outputDir.deleteDir())
            throw new IllegalStateException("Could not delete previous directory ${output}")
        if (!outputDir.mkdirs()) throw new IllegalStateException("Could not create directory ${output}")

        aggregateJavaDocRec(current, outputDir, ignoreDirs)
        createModulesPage(name, version, outputDir)
    }

    private static void aggregateJavaDocRec(File current, File output, String... ignoreDirs) {
        if (!current.directory) return

        def files = current.listFiles()
        if (files == null) return

        files.findAll { f -> f.directory }
                .findAll { f -> !IGNORE_DIRS.any { d -> d == f.name } }
                .findAll { f -> !ignoreDirs.any { d -> d == f.name } }
                .each { f -> {
                    if (f.name == DOCS_DIR) {
                        def dest = getDestinationFromModule(output, f)
                        copyDirectory(f, new File(output, dest))
                    } else aggregateJavaDocRec(f, output)
                } }
    }

    private static void createModulesPage(String name, String version, File file) {
        if (!file.directory) return
        def files = file.listFiles()
        if (files == null) return
        if (files.any { it.name.contains('.html') } ) return

        RESOURCES.each { parseResource(file, it, name, version, files) }

        files.each { createModulesPage(it.name, version, it) }
    }

    private static void parseResource(File parentFile, String resource, String name, String version, File[] files) {
        getResource('/${resource}').withReader { reader ->
            new File(parentFile, resource).withWriter { writer ->
                String line
                while ((line = reader.readLine()) != null) {
                    line = line.replace('%module_name%', name)
                            .replace('%module_version%', version)
                    if (line.contains(MODULE_PLACEHOLDER))
                        line = parseModulesPlaceholder(line, files)
                    writer.write(line)
                    writer.write('\n')
                }
            }
        }
    }

    private static String parseModulesPlaceholder(String line, File[] files) {
        String output = ''
        files.collect { it.name } .each { n -> getResource("/${MODULE_FORMAT_FILE}").withReader { r ->
            String l
            while ((l = r.readLine()) != null)
                output += l.replace('%submodule_name%', n)
                        .replace('%submodule_path%', "${n}${File.separator}index.html")
        } }
        return line.replace(MODULE_PLACEHOLDER, output)
    }

    /**
     * Copies the given file.
     *
     * @param src the source (copied) file
     * @param dst the destination (copy) file
     */
    static void copyDirectory(File src, File dst) {
        if (src.directory) {
            def files = src.listFiles()
            dst.mkdirs()
            if (files != null)
                files.collect { it.name } .each { copyDirectory(new File(src, it), new File(dst, it)) }
        } else Files.copy(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING)
    }

    private static String getDestinationFromModule(File output, File file) {
        def parent = new File(commonPath(output, file))
        def module = new File(file, '')
        while (module.parentFile.parentFile != parent)
            module = module.parentFile
        def moduleName = module.name

        def dst = "${module.parentFile.name}"
        if (moduleName != 'build') dst += "${File.separator}${module.name}"
        return dst
    }

    /**
     * Finds the most common path between the two given files
     *
     * @param file1 the first file
     * @param file2 the second file
     * @return the common path
     */
    static String commonPath(File file1, File file2) {
        def path1 = file1.absolutePath
        def path2 = file2.absolutePath
        def result = ''

        for (i in 0..Math.min(path1.length(), path2.length())) {
            def c1 = path1[i]
            def c2 = path2[i]
            if (c1 != c2) break
            else result += c1
        }

        return result
    }

    private static InputStream getResource(String name) {
        while (name.startsWith('/')) name = name.substring(1)
        def resource = JavaDocUtils.class.getResourceAsStream(name)
        if (resource == null) resource = JavaDocUtils.class.getResourceAsStream("/${name}")
        if (resource == null) throw new IllegalArgumentException("Could not find resource '${name}'")
        return resource
    }
}

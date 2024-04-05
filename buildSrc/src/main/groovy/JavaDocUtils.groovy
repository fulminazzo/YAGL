import java.nio.file.Files
import java.nio.file.StandardCopyOption

class JavaDocUtils {
    private static final def DOCS_DIR = "javadoc"
    private static final def IGNORE_DIRS = [ "main", "java", "groovy", "src", "buildSrc", "resources" ]

    static def aggregateJavaDoc(String output) {
        def current = new File(System.getProperty("user.dir")).getParentFile()
        def outputDir = new File(current, output)

        if (outputDir.exists() && !outputDir.deleteDir())
            throw new IllegalStateException("Could not delete previous directory ${output}")
        if (!outputDir.mkdirs()) throw new IllegalStateException("Could not create directory ${output}")

        aggregateJavaDocRec(current, outputDir)
    }

    private static def aggregateJavaDocRec(File current, File output) {
        if (!current.isDirectory()) return

        def files = current.listFiles()
        if (files == null) return

        main_loop:
        for (file in files) {
            def fileName = file.getName()
            for (dir in IGNORE_DIRS)
                if (fileName == dir) continue main_loop

            if (file.isDirectory())
                if (file.getName() == DOCS_DIR) {
                    def dest = getDestinationFromModule(output, file)
                    copyDirectory(file, new File(output, dest))
                } else aggregateJavaDocRec(file, output)
        }
    }

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

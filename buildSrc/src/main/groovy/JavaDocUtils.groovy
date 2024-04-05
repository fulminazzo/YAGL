class JavaDocUtils {

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

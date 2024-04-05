import spock.lang.Specification

class JavaDocUtilsTest extends Specification {

    def "test common paths"() {
        given:
            def expected = System.getProperty("user.dir") + "/this/path/is/expected/"
            def path1 = new File(expected, "this/is/not")
            def path2 = new File(expected, "me/neither")

        when:
            def actual = JavaDocUtils.commonPath(path1, path2)

        then:
            expected == actual
    }
}

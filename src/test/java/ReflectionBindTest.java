import org.junit.Test;
import org.lintx.plugins.yinwuchat.Util.ReflectionUtil;

import java.lang.reflect.Method;

public class ReflectionBindTest {
    public static String test(String inc) {
        return inc;
    }

    @Test
    public void testBind() throws Throwable {
        Method met = ReflectionBindTest.class.getDeclaredMethod("test", String.class);
        System.out.println(ReflectionUtil.bindTo(String.class, met).apply(null));
    }
}

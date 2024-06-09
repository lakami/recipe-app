package dev.pjatk.recipeapp.usecase;

import com.google.common.reflect.ClassPath;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class UseCasesIntegrityTest {

    @Test
    void allUseCasesShouldHaveExecuteMethod() throws IOException {
        // given
        List<Class> classesWithMissingMethod = new LinkedList<>();
        String packageName = getClass().getPackageName();
        var classes = findAllClassesUsingGoogleGuice(packageName);

        // when
        classes.forEach(clazz -> {
            try {
                var method = getExecute(clazz);
                if (Modifier.isPrivate(method.getModifiers())) {
                    classesWithMissingMethod.add(clazz);
                }
            } catch (NoSuchMethodException e) {
                classesWithMissingMethod.add(clazz);
            }
        });

        // then: Verify all classes have public method named execute
        assertTrue(classesWithMissingMethod.isEmpty());
    }

    private Method getExecute(Class<?> clazz) throws NoSuchMethodException {
        var methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals("execute")) {
                return method;
            }
        }
        throw new NoSuchMethodException();
    }

    public Set<Class> findAllClassesUsingGoogleGuice(String packageName) throws IOException {
        return ClassPath.from(ClassLoader.getSystemClassLoader())
                .getAllClasses()
                .stream()
                .filter(clazz -> clazz.getPackageName().toLowerCase()
                        .startsWith(packageName.toLowerCase()))
                .filter(clazz -> clazz.getSimpleName().endsWith("UseCase"))
                .map(clazz -> clazz.load())
                .collect(Collectors.toSet());
    }

}

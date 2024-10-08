package com.bloxbean.cardano.client.plutus.annotation.processor.util;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.CaseUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.Writer;

@Slf4j
public class JavaFileUtil {

    public static final String CARDANO_CLIENT_LIB_GENERATED_DIR = "cardano.client.lib.generated.dir";

    /**
     * First character has to be upper case when creating a new class
     * @param s
     * @return
     */
    public static String firstUpperCase(String s) {
        if(s == null || s.isEmpty())
            return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static String firstLowerCase(String s) {
        if(s == null || s.isEmpty())
            return s;
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }

    /**
     * Converts a string to camel case
     * @param s
     * @return
     */
    public static String toCamelCase(String s) {
        if (s == null || s.isEmpty())
            return s;

        if(Character.isUpperCase(s.charAt(0))) {
            return s;
        }

        return CaseUtils.toCamelCase(s, true, '_', ' ', '-');
    }

    public static String toClassNameFormat(String s) {
        if (s == null || s.isEmpty())
            return s;

        return firstUpperCase(toCamelCase(s));
    }

    /**
     * Creates a Java file from a TypeSpec with a given classname and package
     *
     * @param packageName
     * @param build
     * @param className
     * @param processingEnv
     */
    public static void createJavaFile(String packageName, TypeSpec build, String className, ProcessingEnvironment processingEnv) {
        String generatedDir = processingEnv.getOptions().get(CARDANO_CLIENT_LIB_GENERATED_DIR);

        JavaFile javaFile = JavaFile.builder(packageName, build)
                .build();

        JavaFileObject builderFile = null;
        try {

            String fullClassName = packageName + "." + className;
            builderFile = processingEnv.getFiler()
                    .createSourceFile(fullClassName);

            if (generatedDir == null) {
                Writer writer = builderFile.openWriter();
                javaFile.writeTo(writer);
                writer.close();
            } else {
                javaFile.writeTo(new java.io.File(generatedDir));
            }
        } catch (Exception e) {
            log.error("Error creating class : " + className, e);
            warn(processingEnv, null, "Error creating class: %s, package: %s, error: %s", className, packageName, e.getMessage());
        }
    }

    public static void error(ProcessingEnvironment processingEnv, Element e, String msg, Object... args) {
        processingEnv.getMessager().printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                e);
    }

    public static void warn(ProcessingEnvironment processingEnv, Element e, String msg, Object... args) {
        processingEnv.getMessager().printMessage(
                Diagnostic.Kind.WARNING,
                String.format(msg, args),
                e);
    }
}

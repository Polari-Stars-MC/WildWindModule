package org.polaris2023.p;

import com.google.auto.service.AutoService;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;
import org.tomlj.TomlVersion;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author baka4n
 * @code @Date 2025/6/14 13:23:12
 */
@SupportedSourceVersion(value = SourceVersion.RELEASE_21)
@SupportedAnnotationTypes(value = "*")
@AutoService(Processor.class)
public class APProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Path parent = Path.of(System.getProperty("user.dir"))
                .getParent().getParent().resolve("src/re-register");
        try(var f = Files.walk(parent)) {
            f
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        try {
                            TomlParseResult parse = Toml.parse(path);
                            System.out.println(parse.get("salt"));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}

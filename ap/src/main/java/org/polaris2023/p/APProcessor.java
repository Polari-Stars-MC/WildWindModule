package org.polaris2023.p;

import com.google.auto.service.AutoService;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;
import org.tomlj.TomlVersion;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.ProtectionDomain;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author baka4n
 * @code @Date 2025/6/14 13:23:12
 */
@SupportedSourceVersion(value = SourceVersion.RELEASE_21)
@SupportedAnnotationTypes(value = "*")
@AutoService(Processor.class)
@SuppressWarnings("unused")
public class APProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        ProtectionDomain protectionDomain = APProcessor.class.getProtectionDomain();
        URL location = protectionDomain.getCodeSource().getLocation();
        try {
            Path path = Path.of(location.toURI());
            System.out.println(path);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}

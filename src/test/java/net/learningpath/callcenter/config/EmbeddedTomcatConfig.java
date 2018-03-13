package net.learningpath.callcenter.config;

import io.vavr.control.Option;
import io.vavr.control.Try;
import org.apache.catalina.startup.Tomcat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.servlet.ServletException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.function.Function;

@Configuration
@PropertySource("classpath:embedded-tomcat.properties")
public class EmbeddedTomcatConfig {

    @Value("${tomcat-server-port}")
    private int serverPort;

    @Value("${tomcat-base-dir}")
    private String tomcatBaseDir;

    @Value("${project-folder-path}")
    private String projectFolderPath;

    @Value("${temp-webapp-context}")
    private String webAppContext;

    @Bean
    public Tomcat getTomcatInstance() throws ServletException {
        Tomcat tomcat = new Tomcat();
        File tempTomcatBaseDir = new File(tomcatBaseDir);
        Option<String> tomcatBaseDirOpt = createDirectory.apply(tempTomcatBaseDir);
        tomcat.setBaseDir(tomcatBaseDirOpt.getOrElseThrow(() -> new RuntimeException("FAILED WHEN CREATING TOMCAT BASE DIRECTORY")));
        tomcat.setPort(serverPort);

        String webAppLocation = new File(projectFolderPath).toPath().normalize().toFile().getAbsolutePath();

        // create the web app context
        tomcat.addWebapp(webAppContext, webAppLocation);
        return tomcat;
    }

    private Function<File, Option<String>> createDirectory = dirPath ->
            Option.of(dirPath)
                    .filter(File::exists)
                    .map(File::toPath)
                    .peek(path -> Try.of(() -> Files.walk(path)
                                                .sorted(Comparator.reverseOrder()))
                                                .peek(stream -> stream
                                                                .map(Path::toFile)
                                                                .forEach(File::delete)))
                    .map(Path::toFile)
                    .orElse(Option.of(dirPath))
                    .filter(File::mkdir)
                    .map(f -> Paths.get(f.getAbsolutePath()).normalize())
                    .map(Path::toString);

}

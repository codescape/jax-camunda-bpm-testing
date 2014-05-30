package com.opitzconsulting.pizza.arquillian;

import lombok.extern.java.Log;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Filter;

import java.net.URL;

/**
 * Filter to ignore all classes in Maven test folders.
 */
@Log
public class IgnoreMavenTestSourceFolder implements Filter<ArchivePath> {

    @Override
    public boolean include(ArchivePath archivePath) {
        boolean addToArchive = false;
        String path = fullQualifiedClassnameFor(archivePath);
        try {
            Class<?> theClass = Class.forName(path);
            URL location = theClass.getProtectionDomain().getCodeSource().getLocation();
            addToArchive = !location.toString().contains("test-classes");
        } catch (ClassNotFoundException e) {
            log.info("Could not create class " + path);
        }

        log.info(path + (addToArchive ? " added to archive." : " ignored."));
        return addToArchive;
    }

    private String fullQualifiedClassnameFor(ArchivePath archivePath) {
        String path = archivePath.get();
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        path = path.replaceAll("/", ".");
        if (path.endsWith(".class")) {
            path = path.replaceAll(".class", "");
        }
        return path;
    }

}

package org.monkey.common.junit;

import org.apache.tools.ant.DirectoryScanner;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.collect.Lists.newArrayList;

public class DirectoryScanningUtils {

    public static Class[] getClasses(String sourceRoot, String[] filesToInclude, String[] filesToExclude) throws ClassNotFoundException {
        List<Class> classes = newArrayList();
        final File sourceBaseDir = new File(System.getProperty("user.dir") + File.separator + sourceRoot);
        final DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(sourceBaseDir);
        scanner.setIncludes(filesToInclude);
        if (filesToExclude != null && filesToExclude.length > 0) {
            scanner.setExcludes(filesToExclude);
        }
        scanner.scan();
        final String[] files = scanner.getIncludedFiles();
        Pattern testFilePattern = Pattern.compile("(.*)\\.(.*)");

        for (String file : files) {
            Matcher matcher = testFilePattern.matcher(file);
            if (matcher.matches()) {
                String className = matcher.group(1);
                Class clazz = Class.forName(className.replace(File.separatorChar, '.'));
                classes.add(clazz);
            }
        }

        return classes.toArray(new Class[]{});
    }
}

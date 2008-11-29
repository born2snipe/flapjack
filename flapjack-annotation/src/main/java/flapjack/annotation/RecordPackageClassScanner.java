package flapjack.annotation;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class RecordPackageClassScanner {
    private List<String> packages = new ArrayList<String>();

    public List<Class> scan() {
        List<Class> classes = new ArrayList<Class>();
        for (String packageName : packages) {
            for (Class clazz : findClassesInPackage(packageName)) {
                if (hasRecordAnnoation(clazz))
                    classes.add(clazz);
            }
        }
        return classes;
    }

    private boolean hasRecordAnnoation(Class clazz) {
        return clazz.getAnnotation(Record.class) != null;
    }

    private List<Class> findClassesInPackage(String packageName) {
        List<Class> classes = new ArrayList<Class>();
        URL url = getClass().getClassLoader().getResource(convertPackageToPath(packageName));
        try {
            File dir = new File(url.toURI());
            String[] classFiles = dir.list(new FilenameFilter() {
                public boolean accept(File file, String filename) {
                    return filename.endsWith(".class");
                }
            });
            for (String className : classFiles) {
                classes.add(Class.forName(packageName + "." + className.replace(".class", "")));
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return classes;
    }

    private String convertPackageToPath(String packageName) {
        return packageName.replace('.', '/');
    }

    public void setPackages(List<String> packages) {
        this.packages.addAll(packages);
    }
}

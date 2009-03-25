package flapjack.mvn;

import junit.framework.TestCase;

import java.io.File;
import java.util.Arrays;

import org.apache.maven.plugin.MojoExecutionException;


public class ReportMojoTest extends TestCase {
    private static final File OUTPUT = new File("output");

    public void test_execute() throws MojoExecutionException {
        ReportMojo mojo = new ReportMojo();
        mojo.setOutputDirectory(OUTPUT);
        mojo.setPackages(Arrays.asList("flapjack"));


        mojo.execute();
    }
}

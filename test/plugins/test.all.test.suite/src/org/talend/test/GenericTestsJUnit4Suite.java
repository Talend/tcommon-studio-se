package org.talend.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

/**
 * Class that gathers all classes for test using the following java System properties to find the desired test classes. <br/>
 * see field description in javadoc
 * 
 * 
 */
@RunWith(GenericTestsJUnit4Suite.class)
@SuppressWarnings("nls")
public class GenericTestsJUnit4Suite extends Suite {

    public static final String PROP_REPORT_PATH = "talend.junit.report.path";

    public static final String PROP_PREFIX = "talend.junit.report.prefix";

    protected final static String CR = "\n";

    private static String FULL_LOG_FILE = "all_junits.log";

    private static String SUM_UP_FILE = "junits_sum_up.txt";

    protected int nbTestFailed = 0;

    protected int nbTestExecuted = 0;

    private final SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss");

    private StringBuffer mockErrors = new StringBuffer();

    private Set<String> classMethodErrors = new HashSet<String>();

    protected LinkedList<String> listOfClassesToExecute = new LinkedList<String>();

    private List<Runner> finalRunnersList = new ArrayList<Runner>();

    private IPath logPath;

    private FileWriter testLogWriter;

    private String prefixLog;

    private String getPrefixLog() {
        if (prefixLog == null) {
            prefixLog = System.getProperty(PROP_PREFIX, "");// empty by default
            if (prefixLog.length() > 0) {
                prefixLog += '-';
            }
        }
        return prefixLog;
    }

    private IPath getLogPath() {
        if (logPath == null) {
            logPath = new Path(System.getProperty(PROP_REPORT_PATH, Platform.getLocation().removeLastSegments(2).toString()));
            File folder = logPath.toFile();
            if (!folder.exists()) {
                folder.mkdirs();
            }
        }
        return logPath;
    }

    private String getLogFileName(String baseName) {
        return getPrefixLog() + baseName;
    }

    private FileWriter getFileWriter() {
        if (testLogWriter == null) {
            File fullLogFile = getLogPath().append(getLogFileName(FULL_LOG_FILE)).toFile();
            try {
                testLogWriter = new FileWriter(fullLogFile, true);
            } catch (IOException e) {
                // nothing
            }
        }
        return testLogWriter;
    }

    private RunListener runListener = new RunListener() {

        @Override
        public void testStarted(Description description) throws Exception {
            printBeforeTest(description);
        }

        @Override
        public void testFinished(Description description) throws Exception {
            printAfterTest(description);
            nbTestExecuted++;
        }

        @Override
        public void testFailure(Failure failure) throws Exception {
            processFailure(failure);
        }
    };

    static final TalendJunitsTestCollector ttc = new TalendJunitsTestCollector();

    public GenericTestsJUnit4Suite(Class<?> klass) throws InitializationError {
        super(klass, ttc.getTests());

        for (Class<?> classToTest : ttc.getTests()) {
            listOfClassesToExecute.add(classToTest.getName());
        }
    }

    @Override
    protected void runChild(Runner runner, RunNotifier notifier) {
        printBeforeTestClass(runner);
        super.runChild(runner, notifier);
        printAfterTestClass(runner);
    }

    protected void appendToLogFile(String log) {
        FileWriter fileWriter = getFileWriter();
        if (fileWriter != null) {
            try {
                fileWriter.append(log);
                fileWriter.flush();
            } catch (IOException e) {
                // print in console?
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run(RunNotifier runNotifier) {
        beforeRun(runNotifier);
        super.run(runNotifier);
        afterRun(runNotifier);
    }

    protected void beforeRun(RunNotifier runNotifier) {
        runNotifier.addListener(runListener);
    }

    protected void afterRun(RunNotifier runNotifier) {
        logDetailsAfterRun();
    }

    protected void logDetailsAfterRun() {
        File sumUpFile = getLogPath().append(getLogFileName(SUM_UP_FILE)).toFile();

        FileWriter sumUpWriter = null;
        try {
            sumUpWriter = new FileWriter(sumUpFile);
            writeSumUpHeader(sumUpWriter);
            writeSumUpFailedDetails(sumUpWriter);
        } catch (IOException e) {
            StringBuffer buff = new StringBuffer();
            buff.append("********************************************************");
            buff.append(CR);
            buff.append("Error when write sum up file for junits");
            buff.append(CR);
            if (e.getMessage() != null) {
                buff.append("--------------------------------------------------------");
                buff.append(CR);
                buff.append("Exception:" + e.getMessage());
                buff.append("********************************************************");
                buff.append(CR);
            }
            appendToLogFile(buff.toString());
        } finally {
            if (sumUpWriter != null) {
                try {
                    sumUpWriter.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
            FileWriter logWriter = getFileWriter();
            if (logWriter != null) {
                try {
                    logWriter.close();
                } catch (IOException e) {
                    //
                }
            }
        }
    }

    protected void writeSumUpHeader(FileWriter writer) throws IOException {
        writer.append(nbTestExecuted + " test executed.");
        writer.append(CR);
    }

    protected void writeSumUpFailedDetails(FileWriter writer) throws IOException {
        if (nbTestFailed == 0) {
            writer.append("no test failed.");
            writer.append(CR);
        } else {
            writer.append(nbTestFailed + " tests failed.");
            writer.append(CR);
            writer.append("List of classes/methods in error bellow. For more details please check on hudson directly.");
            writer.append(CR);
            writer.append("------------------------------------ Error details ------------------------------------");
            writer.append(CR);
            writer.flush();
            List<String> errorClassMethods = new ArrayList<String>(classMethodErrors);
            Collections.sort(errorClassMethods);
            for (String one : errorClassMethods) {
                writer.append(one);
                writer.append(CR);
            }
            writer.flush();

            if (mockErrors.length() > 0) {
                writer.append(CR);
                writer.append("------------------------------------ Mock errors ------------------------------------");
                writer.append(mockErrors.toString());
            }
        }
    }

    /**
     * @param runner
     */
    private void printAfterTestClass(Runner runner) {
        StringBuffer buff = new StringBuffer();
        buff.append("|====================================================");
        buff.append(CR);
        appendToLogFile(buff.toString());
    }

    /**
     * @param runner
     */
    private void printBeforeTestClass(Runner runner) {
        long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        StringBuffer buff = new StringBuffer();
        buff.append("|====================================================");
        buff.append(CR);
        buff.append("| [memory :" + usedMemory + "b]  - Start test class: " + runner.getDescription());
        buff.append(CR);
        listOfClassesToExecute.remove(runner.getDescription().getClassName());
        appendToLogFile(buff.toString());
    }

    /**
     * @param description
     */
    private void printAfterTest(Description description) {
        appendToLogFile("|" + format.format(new Date()) + "=> Finish: " + description.getMethodName() + CR);
    }

    /**
     * @param description
     */
    private void printBeforeTest(Description description) {
        appendToLogFile("|" + format.format(new Date()) + "=> Start: " + description.getMethodName() + CR);
    }

    @Override
    protected List<Runner> getChildren() {
        if (finalRunnersList.isEmpty()) {
            List<Runner> runners = super.getChildren();
            for (Runner runner : runners) {
                try {
                    final String className = runner.getDescription().getClassName();
                    listOfClassesToExecute.remove(className);
                    // if no exception here, add test to final list
                    finalRunnersList.add(runner);
                } catch (Exception e) {
                    mockErrors.append("* One test failed certainly because of PowerMock (check full log for detail)");
                    mockErrors.append(CR);
                }
            }

            StringBuffer buff = new StringBuffer();
            if (!listOfClassesToExecute.isEmpty()) {
                buff.append("-------- Classes not  executed (certainly problem with powermock) ---------");
                buff.append(CR);
                for (String className : listOfClassesToExecute) {
                    buff.append(className);
                    buff.append(CR);
                }
                buff.append("---------------------------------------------------------------------------");
                buff.append(CR);
                appendToLogFile(buff.toString());
            }
        }

        return finalRunnersList;
    }

    protected void processFailure(Failure failure) {
        String classMethodName = failure.getDescription().getClassName() + "." + failure.getDescription().getMethodName();
        if (!classMethodErrors.contains(classMethodName)) {
            nbTestFailed++;
            classMethodErrors.add(classMethodName);
            appendToLogFile("   **** Test Failed ****" + CR);
        }
    }
}

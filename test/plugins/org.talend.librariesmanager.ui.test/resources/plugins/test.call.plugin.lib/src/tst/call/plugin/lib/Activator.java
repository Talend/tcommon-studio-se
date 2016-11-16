package tst.call.plugin.lib;

import org.apache.commons.lang3.RandomStringUtils;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    @Override
    public void start(BundleContext context) throws Exception {
        // do nothing
    }

    public void libCall() {
        System.out.println("calling method random string ");
        String randomStr = RandomStringUtils.random(12);
        System.out.println("Random string called " + randomStr);

    }

    @Override
    public void stop(BundleContext context) throws Exception {
        // TODO Auto-generated method stub
    }

}

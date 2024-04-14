package mouse.project.lib.tests;

import mouse.project.lib.ioc.Inj;
import mouse.project.lib.ioc.annotation.Auto;
import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.tests.build.TestClassInitializer;
@Service
public class TestsInitImpl implements TestsInit {
    private final Inj inj;
    private final TestClassInitializer initializer;
    @Auto
    public TestsInitImpl(Inj inj, TestClassInitializer testClassInitializer) {
        this.inj = inj;
        this.initializer = testClassInitializer;
    }
    @Override
    public void setUp(Object testClass) {
        initializer.scanAndInitialize(testClass, inj);
    }
}

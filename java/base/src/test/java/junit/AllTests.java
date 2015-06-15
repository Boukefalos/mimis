package junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TestTcpSocketCommunication.class,
	TestTcpChannelCommunication.class
})

public class AllTests {}
// Should test start()/stop() of components, check implementation
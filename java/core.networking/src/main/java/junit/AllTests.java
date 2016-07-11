package junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import networking.TestUdpMulticastCommunication;
import networking.TestUdpUnicastCommunication;

@RunWith(Suite.class)
@SuiteClasses({
    TestTcpSocketCommunication.class,
    TestTcpChannelCommunication.class,
    TestUdpUnicastCommunication.class,
    TestUdpMulticastCommunication.class,
    TestUdpDuplexCommunication.class
})

public class AllTests {}
// Should test start()/stop() of components, check implementation
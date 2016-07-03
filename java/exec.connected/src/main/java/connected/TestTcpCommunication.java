package connected;

import java.util.Properties;

import base.Control;

import com.github.boukefalos.ibuddy.iBuddy;
import com.github.boukefalos.lirc.Lirc;
import com.github.boukefalos.lirc.Loader;

import dummy.Dummy;

public class TestTcpCommunication {
	public static void main(String[] args) throws Exception {
			Properties localProperties = new Properties();
			localProperties.setProperty("implementation", "local");
			localProperties.setProperty("server", "true");
			localProperties.setProperty("server.port", "8883");
			localProperties.setProperty("server.protocol", "tcp");

			Properties remoteProperties = new Properties();
			remoteProperties.setProperty("implementation", "remote");
			remoteProperties.setProperty("protocol", "tcp");
			remoteProperties.setProperty("remote.host", "localhost");
			remoteProperties.setProperty("remote.port", "8883");

			Loader localLoader = new Loader(localProperties);
			Loader remoteLoader = new Loader(remoteProperties);

			Lirc localLirc = localLoader.getLirc();
			Lirc remoteLirc = remoteLoader.getLirc();

			Properties iBuddyProperties = new Properties();
			iBuddyProperties.setProperty("implementation", "local");
			iBuddy iBuddy = new com.github.boukefalos.ibuddy.Loader(iBuddyProperties).getiBuddy();		
			Dummy dummy = new Dummy(localLirc, iBuddy);

			Control server = localLoader.getServer();

			remoteLirc.start();
			server.start();
			dummy.start();

			Thread.sleep(10000);
	}
}

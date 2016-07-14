/**
 * Copyright (C) 2016 Rik Veenboer <rik.veenboer@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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

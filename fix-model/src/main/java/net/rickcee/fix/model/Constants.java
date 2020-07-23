/**
 * 
 */
package net.rickcee.fix.model;

import java.net.InetAddress;

/**
 * @author rickcee
 *
 */
public class Constants {
	public static final String USERNAME;
	public static final String HOSTNAME;

	static {
		InetAddress localMachine = null;
		try {
			localMachine = InetAddress.getLocalHost();
		} catch (Exception e) {
		}

		if (localMachine != null) {
			HOSTNAME = localMachine.getHostName();
		} else {
			HOSTNAME = "N/A";
		}

		USERNAME = System.getProperty("user.name");
	}
}

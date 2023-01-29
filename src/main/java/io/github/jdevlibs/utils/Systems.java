/*  ---------------------------------------------------------------------------
 *  * Copyright 2020-2021 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  ---------------------------------------------------------------------------
 */
package io.github.jdevlibs.utils;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * Utilities class for get all information of System
 * 
 * @author Supot Saelao
 * @version 1.0
 */
public final class Systems {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String PATH_SEPARATOR = System.getProperty("path.separator");
	private static final String USER_HOME = System.getProperty("user.home");
	private static final String USER_DIR = System.getProperty("user.dir");
	private static final String JAVA_IO_TMPDIR = System.getProperty("java.io.tmpdir");
	private static final String JAVA_VERSION = System.getProperty("java.version");
	private static final String JAVA_HOME = System.getProperty("java.home");
	private static final String JAVA_VM_VERSION = System.getProperty("java.vm.version");
	private static final String WINDOW_LINE = "\r\n";
	private static final char WIN_SEPARATOR = '\\';
	private static final char SYS_SEPARATOR = File.separatorChar;
	private static final long MB = (1024L * 1024L);

	private Systems() {
	}

	public static String getLineSeparator() {
		return LINE_SEPARATOR;
	}

	public static String getWindowLineSeparator() {
		return WINDOW_LINE;
	}

	public static String getFileSeparator() {
		return FILE_SEPARATOR;
	}

	public static String getPathSeparator() {
		return PATH_SEPARATOR;
	}

	public static String getUserHome() {
		return USER_HOME;
	}

	public static String getUserDir() {
		return USER_DIR;
	}

	public static String getTmpDir() {
		return JAVA_IO_TMPDIR;
	}

	public static String getJavaVersion() {
		return JAVA_VERSION;
	}

	public static String getJavaHome() {
		return JAVA_HOME;
	}

	public static String getJVMVersion() {
		return JAVA_VM_VERSION;
	}

	public static String getIpAddress() {
		String ip = null;
		try {
			InetAddress inetAdds = InetAddress.getLocalHost();
			ip = inetAdds.getHostAddress();
		} catch (Exception e) {
			// Ignore error
		}

		return ip;
	}

	public static String getMacAddress() {
		try {
			InetAddress inetAdds = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(inetAdds);
			byte[] mac = network.getHardwareAddress();

			StringBuilder sb = new StringBuilder(32);
			for (byte b : mac) {
				sb.append(String.format("%02X%s", b, ""));
			}

			return sb.toString();
		} catch (UnknownHostException | SocketException e) {
			// Ignore error
		}

		return null;
	}

	public static String getTomcatHome() {
		return getProperty("catalina.home");
	}

	public static String getJbossHomeDir() {
		return getProperty("jboss.home.dir");
	}

	public static String getJbossServerBaseDir() {
		return getProperty("jboss.server.base.dir");
	}

	public static String getJbossServerLogDir() {
		return getProperty("jboss.server.log.dir");
	}

	public static String getProperty(String key) {
		return System.getProperty(key, null);
	}

	public static long getUsedMemory() {
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / MB;
	}

	public static boolean isWindowsOS() {
		return SYS_SEPARATOR == WIN_SEPARATOR;
	}
	
	public static void setPropertie(Map<Object, String> values) {
		if (values == null || values.isEmpty()) {
			return;
		}

		for (Map.Entry<Object, String> map : values.entrySet()) {
			System.getProperties().put(map.getKey(), map.getValue());
		}
	}
}

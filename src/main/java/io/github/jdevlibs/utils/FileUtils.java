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

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for handing IO
 * 
 * @author Supot Saelao
 * @version 1.0
 */
public final class FileUtils {
	private static final char UNIX_SEPARATOR = '/';
	private static final char WIN_SEPARATOR = '\\';
	private static final int NOT_FOUND = -1;
	private static final char EXT_SEPARATOR = '.';

	private FileUtils() {
	}

	public static void close(Closeable ins) {
		if (ins == null) {
			return;
		}

		try {
			ins.close();
		} catch (IOException ex) {
			// Ignore error
		}
	}

	public static Long getFileSize(String file) {
		return getFileSize(new File(file));
	}

	public static Long getFileSize(File file) {
		try {
			return file.length();
		} catch (Exception ex) {
			// Ignore error
		}

		return Values.ZERO_LONG;
	}

	public static String appendSlash(String path) {
		if (Validators.isEmpty(path)) {
			return path;
		}

		path = convertPathToUnix(path);
		if (path.lastIndexOf('/') != path.length() - 1) {
			path += "/";
		}

		return path;
	}

	public static File[] listFiles(String dir) {
		return listFiles(new File(dir), null);
	}

	public static File[] listFiles(File dir) {
		return listFiles(dir, null);
	}
	
	public static File[] listFiles(String dir, FilenameFilter filter) {
		return listFiles(new File(dir), filter);
	}
	
	public static File[] listFiles(File dir, FilenameFilter filter) {
		if (!dir.exists() || !dir.isDirectory()) {
			return new File[] {};
		}

		return dir.listFiles(filter);
	}
	
    public static List<File> listFileToList(String dir) {
    	return listFileToList(dir, false);
    }
    
    public static List<File> listFileToList(String dir, boolean recursive) {
		return listFileToList(new File(dir), recursive);
    }
    
    public static List<File> listFileToList(File dir, boolean recursive) {
    	List<File> files = new ArrayList<>();
    	listFiles(dir, null, files, recursive);
		return files;
    }
    
    public static List<File> listFileToList(String dir, FilenameFilter filter) {
    	return listFileToList(dir, filter, false);
    }
    
    public static List<File> listFileToList(File dir, FilenameFilter filter) {
    	return listFileToList(dir, filter, false);
    }
    
    public static List<File> listFileToList(String dir, FilenameFilter filter, boolean recursive) {
    	return listFileToList(new File(dir), filter, recursive);
    }
	
    public static List<File> listFileToList(File dir, FilenameFilter filter, boolean recursive) {
    	List<File> files = new ArrayList<>();
    	listFiles(dir, filter, files, recursive);
		return files;
    }
    
	private static void listFiles(File dir, FilenameFilter filter, List<File> files, boolean recursive) {
	    File[] fileItems;
		if (filter != null) {
			fileItems = dir.listFiles(filter);
	    } else {
	    	fileItems = dir.listFiles();
	    }
		
		if (fileItems == null) {
			return;
		}
		
	    for (File file : fileItems) {
	        if (file.isFile()) {
	            files.add(file);
	        } else if (file.isDirectory() && recursive) {
	        	listFiles(file, filter, files, true);
	        }
	    }
	}
	
	public static String getExtension(String fileName) {
		if (Validators.isEmpty(fileName)) {
			return Values.EMPTY;
		}

		String[] names = fileName.split("[.]");
		if (Validators.isEmpty(names)) {
			return null;
		}

		return names[names.length - 1];
	}

	public static String getFileName(String filePath) {
		if (Validators.isEmpty(filePath)) {
			return Values.EMPTY;
		}

		filePath = convertPathToUnix(filePath);
		String[] names = filePath.split("/");
		if (Validators.isEmpty(names)) {
			return null;
		}

		return names[names.length - 1];
	}
	
	public static String removeExtension(String filename) {
		if (filename == null) {
			return null;
		}
		int index = indexOfExtension(filename);
		if (index == -1) {
			return filename;
		} else {
			return filename.substring(0, index);
		}
	}

	public static int indexOfExtension(String filename) {
		if (filename == null) {
			return -1;
		}
		int extensionPos = filename.lastIndexOf(EXT_SEPARATOR);
		int lastSeparator = indexOfLastSeparator(filename);
		return lastSeparator > extensionPos ? -1 : extensionPos;
	}

	public static int indexOfLastSeparator(String filename) {
		if (filename == null) {
			return -1;
		}
		int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
		int lastWindowsPos = filename.lastIndexOf(WIN_SEPARATOR);
		return Math.max(lastUnixPos, lastWindowsPos);
	}

	public static String convertPathToWindows(final String path) {
		if (Validators.isEmpty(path) || path.indexOf(UNIX_SEPARATOR) == NOT_FOUND) {
			return path;
		}
		return path.replace(UNIX_SEPARATOR, WIN_SEPARATOR);
	}

	public static String convertPathToUnix(final String path) {
		if (Validators.isEmpty(path) || path.indexOf(WIN_SEPARATOR) == NOT_FOUND) {
			return path;
		}
		return path.replace(WIN_SEPARATOR, UNIX_SEPARATOR);
	}

	public static String convertPathToSystem(final String path) {
		if (Validators.isEmpty(path)) {
			return null;
		}
		if (Systems.isWindowsOS()) {
			return convertPathToWindows(path);
		} else {
			return convertPathToUnix(path);
		}
	}

	public static void mkdirByFile(String filename) {
		mkdirByFile(new File(filename));
	}

	public static void mkdirByFile(File file) {
		try {
			Path parentDir = file.toPath().getParent();
			if (!parentDir.toFile().exists()) {
				Files.createDirectories(parentDir);
			}
		} catch (IOException ex) {
			//Ignore error
		}
	}
	
	public static boolean moveFile(String source, String target) {
		if (Validators.isEmpty(source) || Validators.isEmpty(target)) {
			return false;
		}
		return moveFile(new File(source), new File(target));
	}
	
	public static boolean moveFile(File source, File target) {
		if (source != null && source.exists()) {
			FileUtils.mkdirByFile(target);
			return source.renameTo(target);
		}
		
		return false;
	}
	
	public static boolean delete(String filePath) {
		return delete(new File(filePath));
	}
	
	public static boolean delete(File file) {		
		try {
			if (file == null) {
				return false;
			}
			
			if (file.isDirectory()) {
				return deleteDirectory(file);
			} else {
				return file.delete();
			}
		} catch (Exception ex) {
			return false;
		}
	}
	
	public static boolean deleteDirectory(String dir) {
		return deleteDirectory(new File(dir), false);
	}
	
	public static boolean deleteDirectory(String dir, boolean recursive) {
		return deleteDirectory(new File(dir), recursive);
	}
	
	public static boolean deleteDirectory(File dir) {
		return deleteDirectory(dir, false);
	}
	
	public static boolean deleteDirectory(File dir, boolean recursive) {
		if (!dir.exists() || !dir.isDirectory()) {
			return false;
		}

		List<File> files = listFileToList(dir, recursive);
		if (Validators.isEmpty(files)) {
			return false;
		}

		for (File file : files) {
			forceDelete(file);
		}

		return true;
	}
	
	public static boolean forceDelete(File file) {
		if (file.isDirectory()) {
			return deleteDirectory(file, false);
		}
		return file.delete();
	}
	
	public static void writeByteArrayToFile(File file, byte[] data) throws IOException {
		writeByteArrayToFile(file, data, false);
	}

	public static void writeByteArrayToFile(File file, byte[] data, boolean append) throws IOException {
		FileOutputStream out = null;
		try {
			out = openOutputStream(file, append);
			out.write(data);
			out.close();
		} finally {
			close(out);
		}
	}
	
	public static FileOutputStream openOutputStream(File file) throws IOException {
		return openOutputStream(file, false);
	}

	public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
		if (file.exists()) {
			if (file.isDirectory()) {
				throw new IOException("File '" + file + "' exists but is a directory");
			}

			if (!file.canWrite()) {
				throw new IOException("File '" + file + "' cannot be written to");
			}
		} else {
			File parent = file.getParentFile();
			if (parent != null && !parent.mkdirs() && !parent.isDirectory()) {
				throw new IOException("Directory '" + parent + "' could not be created");
			}
		}

		return new FileOutputStream(file, append);
	}

	public static String getTempDir() {
		return System.getProperty("java.io.tmpdir");
	}

	public static File createTempDir(String dirName) {
		File tmpDir = new File(new File(getTempDir()), dirName);
		if (!tmpDir.exists()) {
			boolean success = tmpDir.mkdirs();
			return (success ? tmpDir : null);
		}

		return tmpDir;
	}

	public static String createTempDirPath(String dirName) {
		File tmpDir = new File(new File(getTempDir()), dirName);
		if (!tmpDir.exists()) {
			boolean success = tmpDir.mkdirs();
			return (success ? tmpDir.getAbsolutePath() : null);
		}

		return tmpDir.getAbsolutePath();
	}
}

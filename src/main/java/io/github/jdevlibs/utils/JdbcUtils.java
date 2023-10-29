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

import java.io.IOException;
import java.io.Reader;
import java.sql.*;
import java.util.*;

/**
 * Utility class for manage JDBC
 * 
 * @author Supot Saelao
 * @version 1.0
 */
public final class JdbcUtils {
	private static final String COL_NAME = "COLUMN_NAME";

	private static final String FM_TIME = "HH:mm:ss";
	private static final String FM_DATE_TIME = "dd/MM/yyyy HH:mm:ss";
	private static final Character ESC_CHAR = '!';
	private static final String[] ESC_CHARS = { "!", "_", "%" };
	
	private JdbcUtils() {
	}

	public static String getDatabaseProductName(Connection conn) {
		try {
			return getDatabaseProductName(conn.getMetaData());
		} catch (SQLException ex) {
			return null;
		}
	}
	
	public static String getDatabaseProductName(DatabaseMetaData dbMeta) {
		try {
			return dbMeta.getDatabaseProductName();
		} catch (SQLException ex) {
			return null;
		}
	}
	
	public static boolean isMySql(Connection conn) {
		String dbProduct = getDatabaseProductName(conn);
		if (dbProduct == null) {
			return false;
		}
		return dbProduct.toLowerCase().contains("mysql");
	}
	
	public static boolean isOracle(Connection conn) {
		String dbProduct = getDatabaseProductName(conn);
		if (dbProduct == null) {
			return false;
		}		
		return dbProduct.toLowerCase().contains("oracle");
	}
	
	public static boolean isMsSql(Connection conn) {
		String dbProduct = getDatabaseProductName(conn);
		if (dbProduct == null) {
			return false;
		}		
		return dbProduct.toLowerCase().contains("sql server")
				|| dbProduct.toLowerCase().contains("microsoft sql server");
	}
	
	/**
	 * Verify that table name are present in the Database
	 * 
	 * @param dbMeta the DatabaseMetaData object
	 * @param table  The table for verify
	 * @return true when table is exists
	 */
	public static boolean existsTable(DatabaseMetaData dbMeta, String table) {
		ResultSet rs = null;
		try {
			String schema = getUserName(dbMeta, table);
			rs = dbMeta.getColumns(null, schema, getTableName(table), null);
			return rs.next();
		} catch (SQLException ex) {
			//Ignore
		} finally {
			close(rs);
		}

		return false;
	}

	/**
	 * List all SQL select statement column
	 * 
	 * @param rsMeta The ResultSetMetaData object
	 * @return Collection of SQL select statement column names
	 */
	public static List<String> getColumns(ResultSetMetaData rsMeta) {
		List<String> columns = new ArrayList<>();
		try {
			int col = rsMeta.getColumnCount();
			for (int i = 1; i <= col; i++) {
				String colName = rsMeta.getColumnLabel(i);
				if (Validators.isEmpty(colName)) {
					colName = rsMeta.getColumnName(i);
				}
				columns.add(colName);
			}
		} catch (SQLException ex) {
			//Ignore
		}

		return columns;
	}

	public static List<ColumnInfo> getColumInfo(ResultSetMetaData rsMeta) {
		List<ColumnInfo> columns = new ArrayList<>();
		try {
			int col = rsMeta.getColumnCount();
			for (int i = 1; i <= col; i++) {
				String colName = rsMeta.getColumnLabel(i);
				if (Validators.isEmpty(colName)) {
					colName = rsMeta.getColumnName(i);
				}

				ColumnInfo type = new ColumnInfo(colName, rsMeta.getColumnType(i), rsMeta.getScale(i));
				type.setIndex(i);
				columns.add(type);
			}
		} catch (SQLException ex) {
			//Ignore
		}

		return columns;
	}

	public static List<String> getColumns(DatabaseMetaData dbMeta, String table) {
		List<String> columns = new ArrayList<>();
		ResultSet rs = null;
		try {
			String schema = getUserName(dbMeta, table);
			rs = dbMeta.getColumns(null, schema, getTableName(table), null);
			while (rs.next()) {
				columns.add(rs.getString(COL_NAME));
			}
		} catch (SQLException ex) {
			//Ignore
		} finally {
			close(rs);
		}

		return columns;
	}

	public static Map<String, Integer> getColumWithTypes(ResultSetMetaData rsMeta) {
		Map<String, Integer> columns = new LinkedHashMap<>();
		try {
			int col = rsMeta.getColumnCount();
			for (int i = 1; i <= col; i++) {
				String colName = rsMeta.getColumnLabel(i);
				if (Validators.isEmpty(colName)) {
					colName = rsMeta.getColumnName(i);
				}
				columns.put(colName, rsMeta.getColumnType(i));
			}
		} catch (SQLException ex) {
			//Ignore
		}

		return columns;
	}

	public static Map<String, ColumnType> getColumWithSqlTypes(ResultSetMetaData rsMeta) {
		Map<String, ColumnType> columns = new LinkedHashMap<>();
		try {
			int col = rsMeta.getColumnCount();
			for (int i = 1; i <= col; i++) {
				String colName = rsMeta.getColumnLabel(i);
				if (Validators.isEmpty(colName)) {
					colName = rsMeta.getColumnName(i);
				}
				ColumnType type = new ColumnType(rsMeta.getColumnType(i), rsMeta.getScale(i));
				columns.put(colName, type);
			}
		} catch (SQLException ex) {
			//Ignore
		}

		return columns;
	}

	public static Map<String, String> getPrimaryKey(DatabaseMetaData dbMeta, String table) {
		Map<String, String> keys = new TreeMap<>();
		ResultSet rs = null;
		try {
			String schema = getUserName(dbMeta, table);
			rs = dbMeta.getPrimaryKeys(null, schema, getTableName(table));
			while (rs.next()) {
				keys.put(rs.getString(COL_NAME), rs.getString("PK_NAME"));
			}
		} catch (SQLException ex) {
			//Ignore
		} finally {
			close(rs);
		}

		return keys;
	}

	public static Map<String, Integer> getColumnType(DatabaseMetaData dbMeta, String table) {
		Map<String, Integer> columns = new LinkedHashMap<>();
		ResultSet rs = null;
		try {
			String schema = getUserName(dbMeta, table);
			rs = dbMeta.getColumns(null, schema, getTableName(table), null);
			while (rs.next()) {
				columns.put(rs.getString(COL_NAME), rs.getInt("DATA_TYPE"));
			}
		} catch (SQLException ex) {
			//Ignore
		} finally {
			close(rs);
		}

		return columns;
	}

	public static Map<String, ColumnType> getColumnSqlType(DatabaseMetaData dbMeta, String table) {
		Map<String, ColumnType> columns = new LinkedHashMap<>();
		ResultSet rs = null;
		try {
			String schema = getUserName(dbMeta, table);
			rs = dbMeta.getColumns(null, schema, getTableName(table), null);
			while (rs.next()) {
				ColumnType type = new ColumnType(rs.getInt("DATA_TYPE"), rs.getInt("DECIMAL_DIGITS"));
				columns.put(rs.getString(COL_NAME), type);
			}
		} catch (SQLException ex) {
			//Ignore
		} finally {
			close(rs);
		}

		return columns;
	}

	private static String getUserName(DatabaseMetaData dbMeta, String table) {
		try {
			if (Validators.isEmpty(table)) {
				return dbMeta.getUserName();
			}

			String[] schemas = table.split("[.]");
			if (schemas.length > 1) {
				return schemas[0];
			}
			return dbMeta.getUserName();
		} catch (SQLException ex) {
			return null;
		}
	}

	private static String getTableName(String table) {
		if (Validators.isEmpty(table)) {
			return table;
		}
		String[] schemas = table.split("[.]");
		if (schemas.length > 1) {
			return schemas[schemas.length - 1];
		} else {
			return table;
		}
	}

	/**
	 * Convert ResultSet value to JDBC Type
	 * <pre>
	 * Types.CLOB to String
	 * Types.BLOB to byte[] array
	 * </pre>
	 * @param rs JDBC ResultSet
	 * @param colName The column name
	 * @param type The column type
	 * @return Value of column
	 * @throws SQLException When exception
	 */
	public static Object getResultSetValue(ResultSet rs, String colName, int type) throws SQLException {
		Object obj;
		switch (type) {
		case Types.VARCHAR:
		case Types.CHAR:
		case Types.LONGVARCHAR:
		case Types.LONGNVARCHAR:
		case Types.NVARCHAR:
		case Types.NCHAR:
			obj = rs.getString(colName);
			break;
			case Types.DATE:
			obj = rs.getDate(colName);
			break;
		case Types.TIMESTAMP:
			obj = rs.getTimestamp(colName);
			break;
		case Types.TIME:
			obj = rs.getTime(colName);
			break;
		case Types.CLOB:
			obj = readClob(rs.getClob(colName));
			break;
		case Types.BLOB:
			obj = rs.getBytes(colName);
			break;
		default:
			obj = rs.getObject(colName);
		}
		
		return obj;
	}

	/**
	 * Convert ResultSet value to String
	 * <pre>
	 * Types.CLOB to String
	 * Types.BLOB to Base64 String
	 * </pre>
	 * @param rs JDBC ResultSet
	 * @param colName The column name
	 * @param type The column type
	 * @return Value of column
	 */
	public static String getResultValue(ResultSet rs, String colName, int type) {
		String value = null;
		Object obj;
		try {
			switch (type) {
			case Types.VARCHAR:
			case Types.NCHAR:
			case Types.CHAR:
			case Types.NVARCHAR:
			case Types.LONGNVARCHAR:
			case Types.LONGVARCHAR:
				value = rs.getString(colName);
				break;
				case Types.DATE:
				value = DateFormats.format(rs.getDate(colName), FM_DATE_TIME);
				break;
			case Types.TIMESTAMP:
				value = DateFormats.format(rs.getTimestamp(colName), FM_DATE_TIME);
				break;
			case Types.TIME:
				value = DateFormats.format(rs.getTime(colName), FM_TIME);
				break;
			case Types.CLOB:
				value = readClob(rs.getClob(colName));
				break;
			case Types.BLOB:
			case Types.BINARY:
			case Types.VARBINARY:
			case Types.LONGVARBINARY:
				value = readBlob(rs.getBytes(colName));
				break;
			default:
				obj = rs.getObject(colName);
				if (Validators.isNotNull(obj)) {
					value = obj.toString();
				}
			}
		} catch (SQLException ex) {
			//Ignore
		}
		return value;
	}

	public static String readClob(Clob clob) {
		if (Validators.isNull(clob)) {
			return null;
		}

		try {
			StringBuilder sb = new StringBuilder((int) clob.length());
			Reader r = clob.getCharacterStream();
			char[] charBuffers = new char[2048];
			int n;
			while ((n = r.read(charBuffers, 0, charBuffers.length)) != -1) {
				sb.append(charBuffers, 0, n);
			}
			return sb.toString();
		} catch (SQLException | IOException ex) {
			//Ignore
		}
		return null;
	}

	public static byte[] toByte(Blob blob) {
		if (blob == null) {
			return new byte[] {};
		}

		try {
			long length = blob.length();
			byte[] bytes = blob.getBytes(1L, (int)length);
			blob.free();
			
			return bytes;
		} catch (SQLException ex) {
			//Nothing
		}

		return new byte[] {};
	}
	
	public static String readBlob(Blob blob) {
		return readBlob(toByte(blob));
	}
	
	public static String readBlob(byte[] values) {
		if (values == null || values.length == 0) {
			return "";
		}
		return Encryptions.encodeBase64String(values);
	}

	public static Object toSqlValue(int type, String objVal) {
		Object obj;
		switch (type) {
			case Types.INTEGER:
		case Types.TINYINT:
		case Types.SMALLINT:
			obj = Convertors.toInteger(objVal);
			break;
		case Types.BIGINT:
			obj = Convertors.toLong(objVal);
			break;
		case Types.DOUBLE:
			obj = Convertors.toDouble(objVal);
			break;
		case Types.DECIMAL:
		case Types.NUMERIC:
		case Types.FLOAT:
			obj = Convertors.toBigDecimal(objVal);
			break;
		case Types.DATE:
			obj = DateUtils.sqlDate(objVal, FM_DATE_TIME);
			break;
		case Types.TIMESTAMP:
			obj = DateUtils.sqlTimestamp(objVal, FM_DATE_TIME);
			break;
		case Types.TIME:
			obj = DateUtils.sqlDate(objVal, FM_TIME);
			break;
			case Types.BLOB:
		case Types.BINARY:
		case Types.VARBINARY:
		case Types.LONGVARBINARY:
			obj = Encryptions.decodeBase64(objVal);
			break;
		default:
			obj = objVal;
			break;
		}

		return obj;
	}

	public static String getColumnName(ResultSetMetaData rsMeta, int columnIndex) throws SQLException {

		String name = rsMeta.getColumnLabel(columnIndex);
		if (Validators.isEmpty(name)) {
			name = rsMeta.getColumnName(columnIndex);
		}
		return name;
	}

	public static String toPropertyName(String name) {
		if (Validators.isEmpty(name)) {
			return null;
		}

		name = name.replaceAll("\\s+", "_");
		if (isValidProperty(name)) {
			return name;
		}

		StringBuilder result = new StringBuilder();
		boolean nextIsUpper = false;
		if (name.length() > 1 && name.charAt(1) == '_') {
			result.append(name.substring(0, 1).toUpperCase());
		} else {
			result.append(name.substring(0, 1).toLowerCase());
		}

		for (int i = 1; i < name.length(); i++) {
			String s = name.substring(i, i + 1);
			if (s.equals("_")) {
				nextIsUpper = true;
			} else {
				if (nextIsUpper) {
					result.append(s.toUpperCase());
					nextIsUpper = false;
				} else {
					result.append(s.toLowerCase());
				}
			}
		}
		return result.toString();
	}

	public static String toNestedPropertyName(String name) {
		if (Validators.isEmpty(name)) {
			return null;
		}
		
		String[] pros = name.split("\\.");
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String str : pros) {
			str = toPropertyName(str);
			if (Validators.isEmpty(str)) {
				continue;
			}
			
			if (first) {
				sb.append(str);
				first = false;
			} else {
				sb.append(".").append(str);
			}
		}
	
		return sb.toString();
	}
	
	private static boolean isValidProperty(String value) {
		if (value.contains("_")) {
			return false;
		}

		char[] chs = value.toCharArray();
		if (isLowerCase(chs)) {
			return true;
		}

		return isCamelCase(chs);
	}

	private static boolean isLowerCase(char[] chs) {
		for (char ch : chs) {
			if (!Character.isLowerCase(ch) && !Character.isDigit(ch)) {
				return false;
			}
		}

		return true;
	}

	private static boolean isCamelCase(char[] chs) {
		for (int i = 0; i < chs.length; i++) {
			char ch = chs[i];
			if (i == 0 && (Character.isUpperCase(ch) || Character.isDigit(ch))) {
				return false;
			}
		}

		return true;
	}

	public static Map<String, String> toPropertyName(ResultSetMetaData rsMeta) throws SQLException {

		if (Validators.isNull(rsMeta)) {
			return Collections.emptyMap();
		}

		Map<String, String> columnMaps = new LinkedHashMap<>();
		for (int colInx = 1, size = rsMeta.getColumnCount(); colInx <= size; colInx++) {
			String columnName = getColumnName(rsMeta, colInx);
			if (Validators.isNotEmpty(columnName)) {
				columnMaps.put(columnName, toPropertyName(columnName));
			}
		}

		return columnMaps;
	}

	public static String toClassName(String name) {
		String className = toPropertyName(name);
		if (className == null || className.isEmpty()) {
			return null;
		}
		className = String.valueOf(className.charAt(0)).toUpperCase() + className.substring(1);

		return className;
	}

	public static String toHeaderName(String columnName) {
		if (Validators.isEmpty(columnName)) {
			return null;
		}

		String[] names = columnName.toLowerCase().split("_");
		if (Validators.isEmpty(names)) {
			return null;
		}

		StringBuilder sb = new StringBuilder(64);
		for (String name : names) {
			char[] chars = name.toCharArray();
			for (int i = 0, size = chars.length; i < size; i++) {
				if (i == 0) {
					sb.append(String.valueOf(chars[i]).toUpperCase());
				} else {
					sb.append(chars[i]);
				}
			}
			sb.append(" ");
		}

		return sb.toString();
	}

	public static Map<String, Object> resultSetToMap(ResultSet rs, Map<String, String> columns) throws SQLException {

		Map<String, Object> results = new HashMap<>();
		for (Map.Entry<String, String> map : columns.entrySet()) {
			results.put(map.getKey(), rs.getObject(map.getKey()));
		}

		return results;
	}

	public static boolean isTypeDate(int type) {
		return (Types.DATE == type);
	}

	public static boolean isTypeDateTime(int type) {
		return (Types.TIMESTAMP == type);
	}

	public static boolean isTypeInteger(int type) {
		boolean isNumber = false;
		switch (type) {
		case Types.INTEGER:
		case Types.BIGINT:
			isNumber = true;
			break;
		default:
			break;
		}

		return isNumber;
	}

	public static boolean isTypeDecimal(int type) {
		boolean isNumber = false;
		switch (type) {
		case Types.DECIMAL:
		case Types.DOUBLE:
		case Types.FLOAT:
		case Types.NUMERIC:
			isNumber = true;
			break;
		default:
			break;
		}

		return isNumber;
	}

	public static boolean isTypeNumber(int type) {
		return isTypeInteger(type) || isTypeDecimal(type);
	}

	public static void close(AutoCloseable rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception ex) {
			//Ignore
		}
	}

	public static String escapeChar(String value) {
		return value;
	}

	public static String getEscapeChar(String value) {
		if (isEscapeChar(value)) {
			return " escape \'" + ESC_CHAR + "\'";
		}
		return "";
	}

	public static boolean isEscapeChar(String value) {
		if (Validators.isEmpty(value)) {
			return false;
		}

		for (String str : ESC_CHARS) {
			if (value.contains(str)) {
				return true;
			}
		}
		return false;
	}

	public static String sqlFullLike(String value) {
		return "%" + escapeChar(value) + "%";
	}

	public static String sqlStartLike(String value) {
		return escapeChar(value) + "%";
	}

	public static String sqlEndLike(String value) {
		return "%" + escapeChar(value);
	}

	public static final class ColumnType {
		private int type;
		private int digit;

		public ColumnType() {
			super();
		}

		public ColumnType(int type, int digit) {
			super();
			this.type = type;
			this.digit = digit;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public int getDigit() {
			return digit;
		}

		public void setDigit(int digit) {
			this.digit = digit;
		}
	}

	public static final class ColumnInfo {
		private String column;
		private int index;
		private int type;
		private int digit;

		public ColumnInfo() {
			super();
		}

		public ColumnInfo(String column, int type, int digit) {
			super();
			this.column = column;
			this.type = type;
			this.digit = digit;
		}

		public String getColumn() {
			return column;
		}

		public void setColumn(String column) {
			this.column = column;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public int getDigit() {
			return digit;
		}

		public void setDigit(int digit) {
			this.digit = digit;
		}
	}
}

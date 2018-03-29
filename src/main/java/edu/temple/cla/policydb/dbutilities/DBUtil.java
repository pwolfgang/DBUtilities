/* 
 * Copyright (c) 2018, Temple University
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * All advertising materials features or use of this software must display 
 *   the following  acknowledgement
 *   This product includes software developed by Temple University
 * * Neither the name of the copyright holder nor the names of its 
 *   contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.temple.cla.policydb.dbutilities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.StringJoiner;

/**
 * Data base utilities
 * @author Paul Wolfgang
 */
public class DBUtil {
    
    /**
     * Convert a name to a legal format for use in MySQL
     * @param s The original name
     * @return The converted equivalent
     */
    public static StringBuilder convertToLegalName(String s) {
        if (s.isEmpty()) return new StringBuilder();
        StringBuilder sb = new StringBuilder(s);
	if (s.equalsIgnoreCase("END") 
            || s.equalsIgnoreCase("Foreign")
            || s.equalsIgnoreCase("Exec")
            || s.equalsIgnoreCase("File") 
            || s.equalsIgnoreCase("Index")
            || s.equalsIgnoreCase("Table")){
	    sb.insert(0, '_');
	    sb.append('_');
	}
	if (Character.isDigit(sb.charAt(0)))
	    sb.insert(0, '_');
	for (int i = 0; i < sb.length(); ) {
	    char c = sb.charAt(i);
	    if (c == ' ' || c == '/' || c == ')' || c == '(' || c == ':') {
		sb.setCharAt(i, '_');
		i++;
	    } else if (c == '-') {
		sb.deleteCharAt(i);
	    } else if (c == '#') {
		sb.setCharAt(i, 'N');
		sb.insert(i+1, 'o');
            } else if (c == '&') {
                sb.setCharAt(i, 'a');
                sb.insert(i+1, 'n');
                sb.insert(i+2, 'd');
	    } else {
		i++;
	    }
	}
	return sb;
    }

    /**
     * Convert an array of bytes into an equivalent hex constant
     * @param bytes The input array of bytes
     * @return Equivalent hex constant string
     */
    public static StringBuilder hexString(byte[] bytes) {
	StringBuilder result = new StringBuilder("0x");
	for (byte b : bytes) {
	    result.append(toHex((b >> 4) & 0xF));
	    result.append(toHex(b & 0xF));
	}
	return result;
    }

    /**
     * Convert an int in the range 0 - 15 to equivalent hex character
     * @param b Input integer
     * @return  Equivalent hex character
     */
    public static char toHex(int b) {
	if (b < 10) return (char)(b + '0');
	return (char)(b - 10 + 'A');
    }

    /**
     * Trim input string and replace all occurrences of a single quote with 
     * two single quotes and newline characters with "\n".
     * @param s Input string
     * @return Converted result
     */
    public static StringBuilder doubleQuotes(String s) {
	StringBuilder result = new StringBuilder();
	if (s == null) return result;
        char[] sArray = s.toCharArray();
        if (sArray.length==0) return result;
        int firstNonBlank = 0;
        while (sArray[firstNonBlank] == ' ') firstNonBlank++;
        int lastNonBlank = sArray.length-1;
        while (sArray[lastNonBlank] == ' ') lastNonBlank--;
	for (int i = firstNonBlank; i <= lastNonBlank; i++) {
            char c = sArray[i];
            switch (c) {
                case '\'':
                    result.append("''");
                    break;
                case '\n':
                    result.append("\\n");
                    break;
                default:
                    result.append(c);
                    break;
            }
	}
	return result;
    }
    
    /**
     * Insert a row copying from one database to another
     * @param destRS The destination result set
     * @param sourceRS The source result set
     * @param columnList List of columns to be copied
     * @throws SQLException
     * @throws Exception 
     */
    public static void insertRow(ResultSet destRS, ResultSet sourceRS,
            List<ColumnMetaData> columnList)
            throws SQLException, Exception {
        destRS.moveToInsertRow();
        for (ColumnMetaData metaData : columnList) {
            int columnType = metaData.getDataType();
            String columnName = metaData.getColumnName();
            String newColumnName = metaData.getNewColumnName();
            switch (columnType) {
                case java.sql.Types.BINARY:
                case java.sql.Types.VARBINARY:
                    byte[] binValue = sourceRS.getBytes(columnName);
                    destRS.updateBytes(newColumnName, binValue);
                    break;
                case java.sql.Types.CHAR:
                case java.sql.Types.VARCHAR:
                case java.sql.Types.LONGVARCHAR:
                    String sValue = sourceRS.getString(columnName);
                    destRS.updateString(newColumnName, sValue);
                    break;
                case java.sql.Types.REAL:
                    float fValue = sourceRS.getFloat(columnName);
                    destRS.updateFloat(newColumnName, fValue);
                    break;
                case java.sql.Types.DOUBLE:
                    double dValue = sourceRS.getDouble(columnName);
                    destRS.updateDouble(newColumnName, dValue);
                    break;
                case java.sql.Types.BIT:
                case java.sql.Types.SMALLINT:
                    short shortValue = sourceRS.getShort(columnName);
                    destRS.updateShort(newColumnName, shortValue);
                    break;
                case java.sql.Types.INTEGER:
                    int iValue = sourceRS.getInt(columnName);
                    destRS.updateInt(newColumnName, iValue);
                    break;
                case java.sql.Types.TIMESTAMP:
                    java.sql.Timestamp timeValue = sourceRS.getTimestamp(columnName);
                    destRS.updateTimestamp(newColumnName, timeValue);
                    break;
                default:
                    System.err.println("Unrecognized type: " + columnType);
                    System.err.println("Type name: " + metaData.getTypeName());
                    throw new Exception();
            }
        }
        destRS.insertRow();
        destRS.moveToCurrentRow();
    }
    
    /**
     * Method to create a value list from a row in a database.
     * @param sourceRS The source Result Set
     * @param metaDataList List of metadata describing the columns.
     * @return A comma separated list of string representing the data.
     * @throws SQLException
     * @throws Exception 
     */
    public static String buildValuesList(ResultSet sourceRS,
            List<ColumnMetaData> metaDataList) throws SQLException, Exception {
        StringBuilder valuesList = new StringBuilder("(");
        for (ColumnMetaData metaData : metaDataList) {
            int columnType = metaData.getDataType();
            String columnName = metaData.getColumnName();
            switch (columnType) {
                case java.sql.Types.BINARY:
                case java.sql.Types.VARBINARY:
                    byte[] binValue = sourceRS.getBytes(columnName);
                    valuesList.append(hexString(binValue));
                    valuesList.append(", ");
                    break;
                case java.sql.Types.CHAR:
                case java.sql.Types.VARCHAR:
                case java.sql.Types.LONGVARCHAR:
                    String sValue = sourceRS.getString(columnName);
                    valuesList.append("\'");
                    valuesList.append(doubleQuotes(sValue));
                    valuesList.append("\', ");
                    break;
                case java.sql.Types.REAL:
                    float fValue = sourceRS.getFloat(columnName);
                    valuesList.append(fValue);
                    valuesList.append(", ");
                    break;
                case java.sql.Types.DOUBLE:
                    double dValue = sourceRS.getDouble(columnName);
                    valuesList.append(dValue);
                    valuesList.append(", ");
                    break;
                case java.sql.Types.BIT:
                case java.sql.Types.SMALLINT:
                    short shortValue = sourceRS.getShort(columnName);
                    valuesList.append(shortValue);
                    valuesList.append(", ");
                    break;
                case java.sql.Types.INTEGER:
                    int iValue = sourceRS.getInt(columnName);
                    valuesList.append(iValue);
                    valuesList.append(", ");
                    break;
                case java.sql.Types.TIMESTAMP:
                    java.sql.Timestamp timeValue = sourceRS.getTimestamp(columnName);
                    if (timeValue != null) {
                        valuesList.append("\'");
                        valuesList.append(timeValue);
                        valuesList.append("\', ");
                    } else {
                        valuesList.append("NULL, ");
                    }
                    break;
                default:
                    System.err.println("Unrecognized type: " + columnType);
                    System.err.println("Type name: " + columnName);
                    throw new Exception();
            }
        }
        valuesList.delete(valuesList.length() - 2, valuesList.length());
        valuesList.append(")");
        return valuesList.toString();
    }
    
    /**
     * Method to create an insert statement.
     * @param tableName The table to insert into
     * @param metaDataList The metadata describing the columns
     * @return A string of the form INSERT INTO tableName (column names) VALUES
     */
    public static String buildSqlInsertStatement(String tableName, List<ColumnMetaData> metaDataList) {
        StringBuilder sqlInsertStmt = new StringBuilder();
        sqlInsertStmt.append("INSERT INTO ");
        sqlInsertStmt.append(convertToLegalName(tableName));
        StringJoiner sj1 = new StringJoiner(", ", "(", ")");
        metaDataList.forEach(metaData -> {
            sj1.add(convertToLegalName(metaData.getColumnName()));
        });
        sqlInsertStmt.append(sj1);
        sqlInsertStmt.append(" VALUES ");
        return sqlInsertStmt.toString();
    }

    /**
     * Method to create a CREATE TABLE statement. If a column named ID is found
     * it is set as the primary key, otherwise an ID column is added.
     * @param tableName The table name
     * @param metaDataList The metadata describing the columns
     * @return A CREATE TABLE statement
     */
    public static String buildSqlCreateTableStatement(String tableName, List<ColumnMetaData> metaDataList) {
        StringBuilder sqlCreateTableStmt = new StringBuilder();
        sqlCreateTableStmt.append("CREATE TABLE ");
        sqlCreateTableStmt.append(convertToLegalName(tableName));
        sqlCreateTableStmt.append("( ");
        boolean foundID = false;
        for (ColumnMetaData metaData : metaDataList) {
            String columnName = metaData.getColumnName();
            String typeName = metaData.getTypeName();
            int columnSize = metaData.getColumnSize();
            if ("ID".equals(columnName)) {
                foundID = true;
            }
            sqlCreateTableStmt.append(convertToLegalName(columnName));
            sqlCreateTableStmt.append(" ");
            if (typeName.equalsIgnoreCase("LONGCHAR")) {
                sqlCreateTableStmt.append("TEXT, ");
            } else if (typeName.equalsIgnoreCase("DATETIME")) {
                sqlCreateTableStmt.append("DATETIME, ");
            } else if (typeName.equalsIgnoreCase("INTEGER")
                    || typeName.equalsIgnoreCase("INT")) {
                sqlCreateTableStmt.append("INT, ");
            } else if (typeName.equalsIgnoreCase("SMALLINT")
                    || typeName.equalsIgnoreCase("BIT")) {
                sqlCreateTableStmt.append("SMALLINT, ");
            } else if (typeName.equalsIgnoreCase("DOUBLE")
                    || typeName.equalsIgnoreCase("REAL")) {
                sqlCreateTableStmt.append("DOUBLE, ");
            } else if (typeName.equalsIgnoreCase("COUNTER")) {
                sqlCreateTableStmt.append("INT, ");
            } else {
                sqlCreateTableStmt.append(typeName);
                sqlCreateTableStmt.append("(");
                sqlCreateTableStmt.append(columnSize);
                sqlCreateTableStmt.append("), ");
            }
        }
        sqlCreateTableStmt.delete(sqlCreateTableStmt.length() - 2, sqlCreateTableStmt.length());
        if (!foundID) {
            sqlCreateTableStmt.append(", ID INT AUTO_INCREMENT ");
        }
        sqlCreateTableStmt.append(", PRIMARY KEY (ID) ");
        sqlCreateTableStmt.append(" );");

        return sqlCreateTableStmt.toString();
    }
    
    /**
     * Method to remove commas from a string.
     * @param s The input string
     * @return A copy of the input without commas.
     */
    public static String removeCommas(String s) {
        char[] in = s.toCharArray();
        char[] out = new char[in.length];
        int j = 0;
        for (char c : in) {
            if (c != ',') out[j++] = c;
        }
        return new String(out, 0, j);
    }

}

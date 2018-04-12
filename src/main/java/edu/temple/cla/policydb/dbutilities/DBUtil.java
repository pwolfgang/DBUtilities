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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Data base utilities
 *
 * @author Paul Wolfgang
 */
public class DBUtil {

    /**
     * Convert a name to a legal format for use in MySQL
     *
     * @param s The original name
     * @return The converted equivalent
     */
    public static StringBuilder convertToLegalName(String s) {
        if (s.isEmpty()) {
            return new StringBuilder();
        }
        StringBuilder sb = new StringBuilder(s);
        if (s.equalsIgnoreCase("END")
                || s.equalsIgnoreCase("Foreign")
                || s.equalsIgnoreCase("Exec")
                || s.equalsIgnoreCase("File")
                || s.equalsIgnoreCase("Index")
                || s.equalsIgnoreCase("Table")) {
            sb.insert(0, '_');
            sb.append('_');
        }
        if (Character.isDigit(sb.charAt(0))) {
            sb.insert(0, '_');
        }
        for (int i = 0; i < sb.length();) {
            char c = sb.charAt(i);
            switch (c) {
                case ' ':
                case '/':
                case ')':
                case '(':
                case ':':
                    sb.setCharAt(i, '_');
                    i++;
                    break;
                case '-':
                    sb.deleteCharAt(i);
                    break;
                case '#':
                    sb.setCharAt(i, 'N');
                    sb.insert(i + 1, 'o');
                    break;
                case '&':
                    sb.setCharAt(i, 'a');
                    sb.insert(i + 1, 'n');
                    sb.insert(i + 2, 'd');
                    break;
                default:
                    i++;
                    break;
            }
        }
        return sb;
    }

    /**
     * Convert an array of bytes into an equivalent hex constant
     *
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
     *
     * @param b Input integer
     * @return Equivalent hex character
     */
    public static char toHex(int b) {
        if (b < 10) {
            return (char) (b + '0');
        }
        return (char) (b - 10 + 'A');
    }

    /**
     * Trim input string and replace all occurrences of a single quote with two
     * single quotes and newline characters with "\n".
     *
     * @param s Input string
     * @return Converted result
     */
    public static StringBuilder doubleQuotes(String s) {
        StringBuilder result = new StringBuilder();
        if (s == null) {
            return result;
        }
        char[] sArray = s.toCharArray();
        if (sArray.length == 0) {
            return result;
        }
        int firstNonBlank = 0;
        while (sArray[firstNonBlank] == ' ') {
            firstNonBlank++;
        }
        int lastNonBlank = sArray.length - 1;
        while (sArray[lastNonBlank] == ' ') {
            lastNonBlank--;
        }
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
     * Method to create a value list from a row in a database.
     *
     * @param sourceRS The source Result Set
     * @param metaDataList List of metadata describing the columns.
     * @return A comma separated list of string representing the data.
     * @throws SQLException
     * @throws Exception
     */
    public static String buildValuesList(ResultSet sourceRS,
            List<ColumnMetaData> metaDataList) throws SQLException, Exception {
        StringJoiner valuesList = new StringJoiner(", ", "(", ")");
        for (ColumnMetaData metaData : metaDataList) {
            int columnType = metaData.getDataType();
            String columnName = metaData.getColumnName();
            switch (columnType) {
                case java.sql.Types.BINARY:
                case java.sql.Types.VARBINARY:
                    byte[] binValue = sourceRS.getBytes(columnName);
                    valuesList.add(hexString(binValue));
                    break;
                case java.sql.Types.CHAR:
                case java.sql.Types.VARCHAR:
                case java.sql.Types.LONGVARCHAR:
                    String sValue = sourceRS.getString(columnName);
                    valuesList.add("'" + doubleQuotes(sValue) + "'");
                    break;
                case java.sql.Types.REAL:
                    float fValue = sourceRS.getFloat(columnName);
                    valuesList.add(Double.toString(fValue));
                    break;
                case java.sql.Types.DOUBLE:
                    double dValue = sourceRS.getDouble(columnName);
                    valuesList.add(Double.toString(dValue));
                    break;
                case java.sql.Types.BIT:
                case java.sql.Types.SMALLINT:
                    short shortValue = sourceRS.getShort(columnName);
                    valuesList.add(Integer.toString(shortValue));
                    break;
                case java.sql.Types.INTEGER:
                    int iValue = sourceRS.getInt(columnName);
                    valuesList.add(Integer.toString(iValue));
                    break;
                case java.sql.Types.TIMESTAMP:
                    java.sql.Timestamp timeValue = sourceRS.getTimestamp(columnName);
                    if (timeValue != null) {
                        valuesList.add("'" + timeValue.toString() + "'");
                    } else {
                        valuesList.add("NULL");
                    }
                    break;
                default:
                    System.err.println("Unrecognized type: " + columnType);
                    System.err.println("Type name: " + columnName);
                    throw new Exception();
            }
        }
        return valuesList.toString();
    }

    /**
     * Method to create a value list from Map of column names to values.
     *
     * @param fields The map of column names to values.
     * @param metaDataList List of metadata describing the columns.
     * @return A comma separated list of string representing the data.
     */
    public static String buildValuesList(Map<String, Object> fields,
            List<ColumnMetaData> metaDataList) {
        StringJoiner valuesList = new StringJoiner(", ", "(", ")");
        for (ColumnMetaData metaData : metaDataList) {
            int columnType = metaData.getDataType();
            String columnName = metaData.getColumnName();
            Object fieldValue = fields.get(columnName);
            if (fieldValue != null) {
                switch (columnType) {
                    case java.sql.Types.BINARY:
                    case java.sql.Types.VARBINARY:
                        byte[] binValue = (byte[]) fields.get(columnName);
                        valuesList.add(hexString(binValue));
                        break;
                    case java.sql.Types.CHAR:
                    case java.sql.Types.VARCHAR:
                    case java.sql.Types.LONGVARCHAR:
                        String sValue = (String) fields.get(columnName);
                        valuesList.add("'" + doubleQuotes(sValue) + "'");
                        break;
                    case java.sql.Types.REAL:
                    case java.sql.Types.DOUBLE:
                    case java.sql.Types.BIT:
                    case java.sql.Types.SMALLINT:
                    case java.sql.Types.INTEGER:
                        if (fieldValue instanceof Number) {
                            Number fValue = (Number) fields.get(columnName);
                            valuesList.add(fValue.toString());
                        } else if (fieldValue instanceof Boolean) {
                            valuesList.add((Boolean) fieldValue ? "1" : "0");
                        } else {
                            throw new RuntimeException("Unrecognized number value at column " 
                                    + columnName + " type " + fieldValue.getClass());
                        }
                        break;
                    case java.sql.Types.TIMESTAMP:
                        Date dateValue = (Date) fields.get(columnName);
                        if (dateValue != null) {
                            Instant javaInstant = Instant.ofEpochMilli(dateValue.getTime());
                            LocalDateTime javaDateTime = LocalDateTime.ofInstant(javaInstant, ZoneOffset.UTC);
                            String javaDateString = javaDateTime.format(ISO_LOCAL_DATE);
                            valuesList.add("'" + javaDateString + "'");
                        } else {
                            valuesList.add("NULL");
                        }
                        break;
                    default:
                        throw new RuntimeException("Unrecognized type: "
                                + columnType + "Column: " + columnName);
                }
            } else {
                valuesList.add("NULL");
            }
        }
        return valuesList.toString();
    }

    /**
     * Method to create an insert statement.
     *
     * @param tableName The table to insert into
     * @param metaDataList The metadata describing the columns
     * @return A string of the form INSERT INTO tableName (column names) VALUES
     */
    public static String buildSqlInsertStatement(String tableName, List<ColumnMetaData> metaDataList) {
        StringBuilder sqlInsertStmt = new StringBuilder()
                .append("INSERT INTO ")
                .append(convertToLegalName(tableName))
                .append(" ");
        StringJoiner sj1 = new StringJoiner(", ", "(", ")");
        metaDataList.forEach(metaData -> {
            sj1.add(convertToLegalName(metaData.getColumnName()));
        });
        sqlInsertStmt.append(sj1);
        sqlInsertStmt.append(" VALUES ");
        return sqlInsertStmt.toString();
    }

    /**
     * Method to remove commas from a string.
     *
     * @param s The input string
     * @return A copy of the input without commas.
     */
    public static String removeCommas(String s) {
        char[] in = s.toCharArray();
        char[] out = new char[in.length];
        int j = 0;
        for (char c : in) {
            if (c != ',') {
                out[j++] = c;
            }
        }
        return new String(out, 0, j);
    }

}

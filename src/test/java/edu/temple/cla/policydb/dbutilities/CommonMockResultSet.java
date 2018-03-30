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

import commondb.mock.MockResultSet;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *
 * @author Paul
 */
public class CommonMockResultSet extends MockResultSet implements ResultSet {
    
    public CommonMockResultSet() throws SQLException {
        super("ID,Abstract,DateDecided,Code",
              "\"1\",\"First Item\",2018-01-01,12",
              "\"2\",\"Second Item\",2018-02-02,10");
    }
    
    @Override
    public Timestamp getTimestamp(int column) throws SQLException {
        return new Timestamp(super.getDate(column).getTime());
    }
    
    @Override
    public Timestamp getTimestamp(String columnName) throws SQLException {
        return getTimestamp(getColumnIndex(columnName));
    }
    
    @Override
    public ResultSetMetaData getMetaData() {
        
        return new ResultSetMetaData() {

            private final int[] sizes = new int[]{10,255,15,9};
            private final String[] colNames = new String[]{"", "ID", "Abstract", "DateDecided", "Code"};
            private final int[] colTypes = new int[] {
                    0,
                    java.sql.Types.VARCHAR,
                    java.sql.Types.VARCHAR,
                    java.sql.Types.TIMESTAMP,
                    java.sql.Types.INTEGER
                    };
            private final String[] colTypeNames = new String[]{"","VARCHAR", "VARCHAR", "TIMESTAMP", "INTEGER"};
            private final String[] colJavaTypes = new String[]{"",
                    "java.lang.String",
                    "java.lang.String",
                    "java.lang.Date",
                    "java.lang.Integer"
                    };

            @Override
            public int getColumnCount() throws SQLException {
                return 4;
            }

            @Override
            public boolean isAutoIncrement(int column) throws SQLException {
                return false;
            }

            @Override
            public boolean isCaseSensitive(int column) throws SQLException {
                return true;
            }

            @Override
            public boolean isSearchable(int column) throws SQLException {
                return false;
            }

            @Override
            public boolean isCurrency(int column) throws SQLException {
                return false;
            }

            @Override
            public int isNullable(int column) throws SQLException {
                return ResultSetMetaData.columnNullableUnknown;
            }

            @Override
            public boolean isSigned(int column) throws SQLException {
                return false;
            }

            @Override
            public int getColumnDisplaySize(int column) throws SQLException {
                return sizes[column];
            }

            @Override
            public String getColumnLabel(int column) throws SQLException {
                return colNames[column];
            }

            @Override
            public String getColumnName(int column) throws SQLException {
                return colNames[column];
            }

            @Override
            public String getSchemaName(int column) throws SQLException {
                return "PAPolicy_Copy";
            }

            @Override
            public int getPrecision(int column) throws SQLException {
                return 0;
            }

            @Override
            public int getScale(int column) throws SQLException {
                return 0;
            }

            @Override
            public String getTableName(int column) throws SQLException {
                return "TestTable";
            }

            @Override
            public String getCatalogName(int column) throws SQLException {
                return "def";
            }

            @Override
            public int getColumnType(int column) throws SQLException {
                return colTypes[column];
            }

            @Override
            public String getColumnTypeName(int column) throws SQLException {
                return colTypeNames[column];
            }

            @Override
            public boolean isReadOnly(int column) throws SQLException {
                return false;
            }

            @Override
            public boolean isWritable(int column) throws SQLException {
                return true;
            }

            @Override
            public boolean isDefinitelyWritable(int column) throws SQLException {
                return true;
            }

            @Override
            public String getColumnClassName(int column) throws SQLException {
                return colJavaTypes[column];
            }

            @Override
            public <T> T unwrap(Class<T> iface) throws SQLException {
                return null;
            }

            @Override
            public boolean isWrapperFor(Class<?> iface) throws SQLException {
                return false;
            }
            
        };
        
    } 
    
}

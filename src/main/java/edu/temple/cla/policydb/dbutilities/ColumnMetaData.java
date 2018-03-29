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
import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent column meta data.
 * Column meta data obtained from the getMetaData call on a Result Set
 * after a query has been executed.
 * @author Paul Wolfgang
 */
public class ColumnMetaData {
    
    private final String columnName;
    private final String newColumnName;
    private final int dataType;
    private final String typeName;
    private final int columnSize;
    private final int decimalDigits;
    
    /** 
     * Create a ColumnMetaData object
     * @param columnName    The column name
     * @param dataType      Data type enumeration index
     * @param typeName      Data type as a string
     * @param columnSize    Column size (characters)
     * @param decimalDigits Number of decimal digits if floating
     */
    private ColumnMetaData(String columnName, int dataType, String typeName, 
            int columnSize, int decimalDigits) {
        this.columnName = columnName;
        this.newColumnName = DBUtil.convertToLegalName(columnName).toString();
        this.dataType = dataType;
        this.typeName = typeName;
        this.columnSize = columnSize;
        this.decimalDigits = decimalDigits;
    }
    
    /**
     * Convert a result into a list of ColumnMetaData objects
     * @param rs The result set
     * @return A list of ColumnMetaData objects
     * @throws SQLException 
     */
    public static List<ColumnMetaData> getColumnMetaDataList(ResultSet rs) 
            throws SQLException {
        List<ColumnMetaData> theList = new ArrayList<>();
        while (rs.next()) {
            String columnName = rs.getString("COLUMN_NAME");
            int dataType = rs.getInt("DATA_TYPE");
            String typeName = rs.getString("TYPE_NAME");
            int columnSize = rs.getInt("COLUMN_SIZE");
            int decimalDigits = rs.getInt("DECIMAL_DIGITS");
            theList.add(new ColumnMetaData(columnName, dataType, typeName, 
                    columnSize, decimalDigits));
        }
        return theList;
    }

    /**
     * Get the column name that has been converted to a legal name.
     * @return The converted form for the column name
     */
    public String getNewColumnName() {
        return newColumnName;
    }
    
    /**
     * @return the columnName
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * @return the dataType
     */
    public int getDataType() {
        return dataType;
    }

    /**
     * @return the typeName
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * @return the columnSize
     */
    public int getColumnSize() {
        return columnSize;
    }

    /**
     * @return the decimalDigits
     */
    public int getDecimalDigits() {
        return decimalDigits;
    }
    
    
    
}

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
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Paul
 */
public class ColumnMetaDataTest {
    
    public ColumnMetaDataTest() {
    }
    
    public static List<ColumnMetaData> getColumnMetaDataList() {
        return Arrays.asList(
                new ColumnMetaData("ID", 12, "VARCHAR", 10, 0),
                new ColumnMetaData("Abstract", 12, "VARCHAR", 255, 0),
                new ColumnMetaData("DateDecided", 93, "TIMESTAMP", 15, 0),
                new ColumnMetaData("Code", 4, "INTEGER", 9, 0)
        );
    }

    @Test
    public void testGetColumnMetaDataList() throws Exception {
        System.out.println("getColumnMetaDataList");
        ResultSet rs = new MockResultSet(
            "COLUMN_NAME,DATA_TYPE,TYPE_NAME,COLUMN_SIZE,DECIMAL_DIGITS",
                "ID,12,VARCHAR,10,0",
                "Abstract,12,VARCHAR,255,0",
                "DateDecided,93,TIMESTAMP,15,0",
                "Code,4,INTEGER,9,0"
        );
        List<ColumnMetaData> expResult = getColumnMetaDataList();
        List<ColumnMetaData> result = ColumnMetaData.getColumnMetaDataList(rs);
        assertEquals(expResult, result);
    }
    
}

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
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Paul Wolfgang
 */
public class DBUtilTest {
    
    public DBUtilTest() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testHexString() {
        System.out.println("hexString");
        byte[] bytes = new byte[]{(byte)1,(byte)35,(byte)69,(byte)103,(byte)137,(byte)171,(byte)205,(byte)239};
        String expResult = "0x0123456789ABCDEF";
        String result = DBUtil.hexString(bytes).toString();
        assertEquals(expResult, result);
    }

    @Test
    public void testDoubleQuotes() {
        System.out.println("doubleQuotes");
        String s = "    Hello 'world'    ";
        String expResult = "Hello ''world''";
        StringBuilder result = DBUtil.doubleQuotes(s);
        assertEquals(expResult, result.toString());
    }

    @Test
    public void testBuildValuesList() throws Exception {
        System.out.println("buildValuesList");
        ResultSet sourceRS = new CommonMockResultSet();
        List<ColumnMetaData> metaDataList = ColumnMetaDataTest.getColumnMetaDataList();
        String expResult = "('1', 'First Item', '2018-01-01 00:00:00.0', 12)";
        sourceRS.next();
        String result = DBUtil.buildValuesList(sourceRS, metaDataList);
        assertEquals(expResult, result);
    }

    @Test
    public void testBuildSqlInsertStatement() {
        System.out.println("buildSqlInsertStatement");
        String tableName = "TestTable";
        List<ColumnMetaData> metaDataList = ColumnMetaDataTest.getColumnMetaDataList();
        String expResult = "INSERT INTO TestTable (ID, Abstract, DateDecided, Code) VALUES ";
        String result = DBUtil.buildSqlInsertStatement(tableName, metaDataList);
        assertEquals(expResult, result);
    }

//    @Test
//    public void testBuildSqlCreateTableStatement() {
//        System.out.println("buildSqlCreateTableStatement");
//        String tableName = "";
//        List<ColumnMetaData> metaDataList = null;
//        String expResult = "";
//        String result = DBUtil.buildSqlCreateTableStatement(tableName, metaDataList);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }

    @Test
    public void testRemoveCommas() {
        System.out.println("removeCommas");
        String s = "123,456,789.00";
        String expResult = "123456789.00";
        String result = DBUtil.removeCommas(s);
        assertEquals(expResult, result);
    }
    
}

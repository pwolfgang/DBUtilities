/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.temple.cla.policydb.dbutilities;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 *
 * @author Paul
 */
public class MockResultSetMetaData implements ResultSetMetaData {
    
    private final MockResultSet resultSet;
    
    public MockResultSetMetaData(MockResultSet resultSet) {
        this.resultSet = resultSet;
    }

    @Override
    public int getColumnCount() throws SQLException {
        return resultSet.getColumnCount();
    }

    @Override
    public boolean isAutoIncrement(int arg0) throws SQLException {
        return false;
    }

    @Override
    public boolean isCaseSensitive(int arg0) throws SQLException {
        return true;
    }

    @Override
    public boolean isSearchable(int arg0) throws SQLException {
        return false;
    }

    @Override
    public boolean isCurrency(int arg0) throws SQLException {
        return false;
    }

    @Override
    public int isNullable(int arg0) throws SQLException {
        return ResultSetMetaData.columnNullableUnknown;
    }

    @Override
    public boolean isSigned(int arg0) throws SQLException {
        return false;
    }

    @Override
    public int getColumnDisplaySize(int arg0) throws SQLException {
        return 10;
    }

    @Override
    public String getColumnLabel(int arg0) throws SQLException {
        return resultSet.getColumnLabel(arg0);
    }

    @Override
    public String getColumnName(int arg0) throws SQLException {
        return resultSet.getColumnName(arg0);
    }

    @Override
    public String getSchemaName(int arg0) throws SQLException {
        return "MockSchema";
    }

    @Override
    public int getPrecision(int arg0) throws SQLException {
        return 2;
    }

    @Override
    public int getScale(int arg0) throws SQLException {
        return 2;
    }

    @Override
    public String getTableName(int arg0) throws SQLException {
        return "MockTable";
    }

    @Override
    public String getCatalogName(int arg0) throws SQLException {
        return "MockCatalog";
    }

    @Override
    public int getColumnType(int arg0) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getColumnTypeName(int arg0) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isReadOnly(int arg0) throws SQLException {
        return true;
    }

    @Override
    public boolean isWritable(int arg0) throws SQLException {
        return false;
    }

    @Override
    public boolean isDefinitelyWritable(int arg0) throws SQLException {
        return false;
    }

    @Override
    public String getColumnClassName(int arg0) throws SQLException {
        return resultSet.getColumnClassName(arg0);
    }

    @Override
    public <T> T unwrap(Class<T> arg0) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

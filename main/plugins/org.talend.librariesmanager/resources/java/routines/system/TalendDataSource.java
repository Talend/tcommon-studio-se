package routines.system;

import java.sql.Connection;
import java.sql.SQLException;

public class TalendDataSource {

    private final javax.sql.DataSource ds;

    /**
     * hold a data source inside
     * 
     * @param ds
     */
    public TalendDataSource(javax.sql.DataSource ds) {
        this.ds = ds;
    }

    /**
     * get the connection from the data source inside directly
     * 
     * @return
     * @throws SQLException
     */
    public java.sql.Connection getConnection() throws SQLException {
        Connection conn = ds.getConnection();
        if (conn == null) {
        	throw new RuntimeException("Unable to get a pooled database connection from pool");
        }
        return conn;
    }

    /**
     * get the data source inside
     * 
     * @return
     */
    public javax.sql.DataSource getRawDataSource() {
        return ds;
    }
    
    /**
     * @Deprecated
     * 
     * This method will be removed in future release
     * 
     * close all the connections which is created by the data source inside
     * 
     * @throws SQLException
     */
    
    public void close() throws SQLException {
    	
    }
    
}

package com.vanji.jdbc;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.jdbc.JDBCAppender;
import org.apache.log4j.spi.LoggingEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.StringTokenizer;


public class CustomJDBCAppender extends JDBCAppender{

    /** Holds value of property values. */
    private String values;

    /**the prepared statement object**/
    private PreparedStatement stmt = null;

    /** Holds value of property preparedSQL. */
    private String preparedSQL;

    /** Creates a new instance of CustomJDBCAppender */
    public CustomJDBCAppender() {
    }

    public void doAppend(LoggingEvent event) {
        buffer.add(event);
        if(buffer.size()>=bufferSize) {
            flushBuffer();
        }
    }

    /**
     * overridden method from the JDBCAppender. This method sets the
     * parameters for the prepared statement
     * before executing the statement
     **/
    public void execute(String sql) throws SQLException {
        System.out.println("Execute  sql: "+sql);
        PreparedStatement stmt = getPreparedStatement();
        System.out.println("Execute  sql: "+sql);
        StringTokenizer tokenizer = new StringTokenizer(sql, ",");

        int i=1;
        while(tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            System.out.println("Execute  : "+token);
             if(i==1){
                java.util.Date dt = new java.util.Date();

                java.text.SimpleDateFormat sdf =
                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                String currentTime = sdf.format(dt);
                stmt.setString(i,currentTime);
            }else if(i==4){
                StringTokenizer stoken = new StringTokenizer(token,"=");
                while(stoken.hasMoreTokens()){
                    String tokenVal = stoken.nextToken();
                    stmt.setString(i,tokenVal);
                    i++;
                }
            }else {
                stmt.setString(i, token);
            }
            i++;
        }
        stmt.executeUpdate();

    }

    /**
     * This method obtains the prepared statement object
     **/
    private PreparedStatement getPreparedStatement() throws SQLException {
        //resuse the getConnection() method from the super class
        Connection conn = getConnection();
        if(stmt==null) {
            stmt = conn.prepareStatement(getPreparedSQL());
        }
        return stmt;
    }

    /** Getter for property values.
     * @return Value of property values.
    */
    public String getValues() {
        return this.values;
    }

    /** Setter for property values.
     * @param values New value of property values.
     */
    public void setValues(String values) {
        PatternLayout layout = new PatternLayout(values);
        this.setLayout(layout);
        this.values = values;
        System.out.println("SetValues  : "+values);
    }

    /** Getter for property preparedSQL.
     * @return Value of property preparedSQL.
     */
    public String getPreparedSQL() {
        return this.preparedSQL;
    }

    /** Setter for property preparedSQL.
     * @param preparedSQL New value of property preparedSQL.
     */
    public void setPreparedSQL(String preparedSQL) {
        System.out.println("setPreparedSQL  : "+ preparedSQL);
        this.preparedSQL = preparedSQL;
    }

}

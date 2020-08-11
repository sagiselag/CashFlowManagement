package il.ac.hit.CashFlowManagement.model;

import il.ac.hit.CashFlowManagement.exception.JDBCDataBaseException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;



/**
 * The DataBase class
 */
public class JDBCDataBase
{
    private static JDBCDataBase instance = null;
    private static Object lock = new Object();
    private static Logger logger;
    private final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private final String protocol = "jdbc:derby:CashFlowManagementDB;create=true";
    private boolean isConnected;
    private Connection connection = null;
    private Statement statement = null;

    private JDBCDataBase() throws JDBCDataBaseException {
        try {
            setLogger(Logger.getLogger(JDBCDataBase.class));
            setConnected(false);
            init();
            logger.info("DataBase was create successfully!!");
        } catch (JDBCDataBaseException e){
            logger.info("DataBase creation failed!!");
            throw new JDBCDataBaseException("DataBase creation problem " + e.getMessage(), e);
        }
    }

    /**
     * This method Describes Connection to DataBase
     * @return Connection to DataBase , m_IsConnected
     */
    private boolean init() throws JDBCDataBaseException {
        try {
            Class.forName(driver);
            setConnection(DriverManager.getConnection(protocol));
            setStatement(connection.createStatement());
            setConnected(true);
        } catch (SQLException e){
            throw new JDBCDataBaseException("Connection problem" , e);
        } catch (ClassNotFoundException ex){
            throw new JDBCDataBaseException("Driver Class not found" , ex);
        }
        finally {
            return isConnected;
        }
    }

    /**
     * check if there is a singleton instance of JDBCDataBase and create it's if needed
     * @return instance of DataBase
     * @throws JDBCDataBaseException if failed to get create DB instance
     */
    public static JDBCDataBase getInstance() throws JDBCDataBaseException
    {
        if(instance == null)
        {
            synchronized (lock)
            {
                if (instance == null)
                {
                    instance = new JDBCDataBase();
                }
            }
        }

        return instance;
    }

    /**
     * getter for statement
     * @return the connection statement
     */
    public Statement getStatement()
    {
        return statement;
    }

    /**
     * getter for connection
     * @return the DB connection
     */
    public Connection getConnection()
    {
        return connection;
    }

    /**
     * setter for logger
     * @param logger logger to set
     */
    public void setLogger(Logger logger) {
        JDBCDataBase.logger = logger;
    }

    /**
     * setter for connection status
     * @param connected connection status to set
     */
    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    /**
     * setter for DB connection
     * @param connection DB connection to set
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * setter for DB statement
     * @param statement DB statement to set
     */
    public void setStatement(Statement statement) {
        this.statement = statement;
    }
}

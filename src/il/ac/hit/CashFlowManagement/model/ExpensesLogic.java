package il.ac.hit.CashFlowManagement.model;

import il.ac.hit.CashFlowManagement.exception.GetAllUserExpensesException;
import il.ac.hit.CashFlowManagement.exception.InsertToDBException;
import il.ac.hit.CashFlowManagement.exception.JDBCDataBaseLogicException;
import il.ac.hit.CashFlowManagement.view.LoginForm;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;

/**
 * The  Expenses Logic class
 */
public class ExpensesLogic implements IExpenseHandle
{
    private static Logger logger;
    private final String nameOfTable;
    private JDBCDataBaseLogic dataBaseLogic;
    private SimpleDateFormat formatter;

    /**
     * ExpensesLogic C'tor
     */
    public ExpensesLogic() {
        setLogger(Logger.getLogger(ExpensesLogic.class));
        nameOfTable = LoginForm.username.toUpperCase() + "_Expenses".toUpperCase();
        setFormatter(new SimpleDateFormat("dd/MM/yyyy"));
        setDataBaseLogic(JDBCDataBaseLogic.getInstance());
        try
        {
            dataBaseLogic.createTableIfNotExist(
                    nameOfTable,
                    "Date",
                    "varchar(300)",
                    "Classification",
                    "varchar(300)",
                    "Details",
                    "varchar(300)",
                    "Cost",
                    "varchar(300)");
            logger.info("ExpensesLogic was create successfully!!!");
        } catch (JDBCDataBaseLogicException e){
            logger.info(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @see IExpenseHandle {@link #addExpense(Expense)}
     */
    public void addExpense(@NotNull Expense expense) {
        try {
            String date = formatter.format(expense.getDate());
            String parameters =
                    "'" + date + "'" + ","
                            + "'" + expense.getClassification() + "'" + ","
                            + "'" + expense.getDetails() + "'" + ","
                            + "'" + Double.toString(expense.getCost()) + "'";

            dataBaseLogic.insertValue(nameOfTable, parameters);
            logger.info("ExpensesLogic.addExpense finished successfully");
        } catch (InsertToDBException e) {
            logger.info("ExpensesLogic.addExpense failed");
            e.printStackTrace();
        }
    }

    /**
     * @see ExpensesLogic {@link #getAllUserExpenses()}
     */
    public ResultSet getAllUserExpenses() throws GetAllUserExpensesException {
        try {
            return dataBaseLogic.query("select * from " + nameOfTable);
        } catch (JDBCDataBaseLogicException e){
            throw new GetAllUserExpensesException("failed to get all users expenses", e);
        }
    }

    /**
     * setter for logger
     * @param logger logger to set
     */
    public void setLogger(Logger logger) {
        ExpensesLogic.logger = logger;
    }

    /**
     * setter for JDBCDataBaseLogic
     * @param dataBaseLogic DB logic to set
     */
    public void setDataBaseLogic(JDBCDataBaseLogic dataBaseLogic) {
        this.dataBaseLogic = dataBaseLogic;
    }

    /**
     * setter for SimpleDateFormat
     * @param formatter SimpleDateFormat to set
     */
    public void setFormatter(SimpleDateFormat formatter) {
        this.formatter = formatter;
    }
}


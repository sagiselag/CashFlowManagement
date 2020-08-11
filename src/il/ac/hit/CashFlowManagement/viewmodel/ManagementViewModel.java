package il.ac.hit.CashFlowManagement.viewmodel;

import il.ac.hit.CashFlowManagement.exception.FormCastingException;
import il.ac.hit.CashFlowManagement.exception.GetAllUserExpensesException;
import il.ac.hit.CashFlowManagement.model.Expense;
import il.ac.hit.CashFlowManagement.model.ExpensesLogic;
import il.ac.hit.CashFlowManagement.model.UserLogic;
import il.ac.hit.CashFlowManagement.view.IView;
import il.ac.hit.CashFlowManagement.view.LoginForm;
import il.ac.hit.CashFlowManagement.view.MainForm;
import il.ac.hit.CashFlowManagement.view.RegisterForm;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.sql.ResultSet;
import java.util.Arrays;


/**
 * The  Management View Model class
 */
public class ManagementViewModel implements IViewModel{
    private static Logger logger;
    private static ManagementViewModel instance = null;
    private static Object lock = new Object();
    private IView loginForm, mainForm, registerForm;
    private ExpensesLogic expensesLogic;
    private UserLogic userLogic;
    private String newExpenseDay, newExpenseMonth, newExpenseYear, newExpenseClassification, newExpenseDetail, newExpenseCost;

    /**
     * ManagementViewModel C'tor
     */
    private ManagementViewModel() {
        BasicConfigurator.configure();
        setLogger(Logger.getLogger(ManagementViewModel.class));
        setMainForm(new MainForm());
        setLoginForm(new LoginForm());
        setRegisterForm(new RegisterForm());
        setUserLogic(new UserLogic());
        logger.info("ManagementViewModel was create successfully");
    }

    /**
     * check if there is a singleton instance of ManagementViewModel and create it's if needed
     * @return instance of ManagementViewModel
     */
    public static ManagementViewModel getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new ManagementViewModel();
                }
            }
        }

        return instance;
    }

    /**
     * @see IViewModel {@link #startProgram()}
     */
    public void startProgram() {
        logger.info("ManagementViewModel.startProgram called");
        loginForm.setViewModel();
        registerForm.setViewModel();
        mainForm.setViewModel();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                loginForm.showForm();
            }
        });
    }

    /**
     * @see IViewModel {@link #showLoginForm()}
     */
    public void showLoginForm() {
        logger.info("ManagementViewModel.LoginForm called");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                loginForm.showForm();
            }
        });
    }

    /**
     * @see IViewModel {@link #showRegisterForm()}
     */
    public void showRegisterForm() {
        logger.info("ManagementViewModel.showRegisterForm called");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                registerForm.showForm();
            }
        });
    }

    /**
     * @see IViewModel {@link #showMainForm()}
     */
    public void showMainForm(){
        logger.info("ManagementViewModel.showMainForm called");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setExpensesLogic(new ExpensesLogic());
                mainForm.showForm();
            }
        });
    }

    /**
     * @see IViewModel {@link #verifyUser(String, String)}
     */
    public boolean verifyUser(@NotNull String iUserName, @NotNull String iPassword) {
        Boolean valueToReturn = false;

        logger.info("ManagementViewModel.verifyUser started");
        if(getUserLogic().checkUserPassword(iUserName, iPassword))
        {
            valueToReturn = true;
        }

        logger.info("ManagementViewModel.verifyUser ended");

        return valueToReturn;
    }

    /**
     * @see IViewModel {@link #register(String, String, String, String)}
     */
    public boolean register(@NotNull String iUserName, @NotNull String iPassword, @NotNull String iCountry, @NotNull String iGander) {
        boolean newUserRegister = false;

        logger.info("ManagementViewModel.register started");
        if (!getUserLogic().checkIfExist(iUserName)) {
            getUserLogic().addUser(iUserName, iPassword, iCountry, iGander);
            logger.info("new user register successfully");
            newUserRegister = true;
        }
        else{
            logger.info("new user registering failed since the user " + iUserName + " is already exist");
        }

        return newUserRegister;
    }

    /**
     * @see IViewModel {@link #getAllExpenses()}
     */
    public ResultSet getAllExpenses() throws GetAllUserExpensesException {
        return expensesLogic.getAllUserExpenses();
    }

    /**
     * The method updates the new expense data members from the MainForm
     * @throws FormCastingException if the casting failed
     */
    private void getUpdatedNewExpenseInformation() throws FormCastingException {
        logger.info("ManagementViewModel.getUpdatedNewExpenseInformation started");
        try{
            MainForm form = (MainForm) mainForm;
            setNewExpenseDay(form.getDaysComboBox().getSelectedItem().toString());
            setNewExpenseMonth(form.getMonthsComboBox().getSelectedItem().toString());
            setNewExpenseYear(form.getYearsComboBox().getSelectedItem().toString());
            setNewExpenseClassification(form.getClassificationComboBox().getSelectedItem().toString());
            setNewExpenseDetail(form.getDetailTextField().getText());
            setNewExpenseCost(form.getCostTextField().getText());
        } catch (ClassCastException e){
            logger.info("casting from IForm to MainForm didn't succeed, therefor the expense data members update failed");
            throw new FormCastingException("casting from IForm to MainForm didn't succeed, therefor the expense data members update failed", e);
        }
    }

    /**
     * The method verifies a new expense if all the parameters is correct
     */
    private boolean correctInfoForNewExpense() throws FormCastingException {
        String day, month, year, classification, detail, cost;
        boolean correctInformation;

        logger.info("ManagementViewModel.correctInfoForNewExpense started");
        getUpdatedNewExpenseInformation();
        day = getNewExpenseDay();
        month = getNewExpenseMonth();
        year = getNewExpenseYear();
        classification = getNewExpenseClassification();
        detail = getNewExpenseDetail();
        cost = getNewExpenseCost();
        correctInformation = !day.equalsIgnoreCase("") && !month.equalsIgnoreCase("") && !year.equalsIgnoreCase("")
                && !classification.equalsIgnoreCase("") && !detail.equalsIgnoreCase("") && !cost.equalsIgnoreCase("");

        try{
            if (!correctInformation)
            {
                StringBuilder message = new StringBuilder("Please ");

                if(day.equalsIgnoreCase("")) message.append("select day");
                else if(month.equalsIgnoreCase("")) message.append("select month");
                else if(year.equalsIgnoreCase("")) message.append("select year");
                else if(classification.equalsIgnoreCase("")) message.append("select classification");
                else if(detail.equalsIgnoreCase("")) message.append("add some detail");
                else if (cost.equalsIgnoreCase("")) message.append("fill cost");
                JOptionPane.showMessageDialog(null, message);
            }
            Double.parseDouble(cost);
        }
        catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Cost must be only number");
            correctInformation = false;
        }
        finally {
            logger.info("ManagementViewModel.correctInfoForNewExpense ended successfully");
            return correctInformation;
        }
    }

    /**
     * @see IViewModel {@link #addNewExpense()}
     */
    public void addNewExpense() throws FormCastingException {
        try {
            MainForm form = (MainForm) mainForm;

            if (ManagementViewModel.getInstance().correctInfoForNewExpense()) {
                double cost = Double.parseDouble(getNewExpenseCost());
                String dateStr = getNewExpenseDay() + "/" + Arrays.asList(form.getMonths()).indexOf(getNewExpenseMonth()) + "/" + getNewExpenseYear();

                Expense expense = new Expense(dateStr, getNewExpenseClassification(), getNewExpenseDetail(), cost);
                expensesLogic.addExpense(expense);
            }
            logger.info("ManagementViewModel.addNewExpense ended successfully, new expense added");
        } catch (ClassCastException e){
            throw new FormCastingException("ManagementViewModel.addNewExpense failed, IForm to MainForm casting problem",e);
        }
        catch (NumberFormatException ex){
            throw new FormCastingException("ManagementViewModel.addNewExpense failed, parse from cost which is not double", ex);
        }
    }

    /**
     * setter for loginForm
     * @param loginForm LoginForm to set
     */
    public void setLoginForm(LoginForm loginForm) {
        this.loginForm = loginForm;
    }

    /**
     * setter for mainForm
     * @param mainForm MainForm to set
     */
    public void setMainForm(MainForm mainForm) {
        this.mainForm = mainForm;
    }

    /**
     * setter for registerForm
     * @param registerForm RegisterForm to set
     */
    public void setRegisterForm(RegisterForm registerForm) {
        this.registerForm = registerForm;
    }

    /**
     * setter for userLogic
     * @param userLogic UserLogic to set
     */
    public void setUserLogic(UserLogic userLogic) {
        this.userLogic = userLogic;
    }

    /**
     * setter for Logger
     * @param logger Logger to set
     */
    public void setLogger(Logger logger) {
        ManagementViewModel.logger = logger;
    }

    /**
     * setter for ExpensesLogic
     * @param expensesLogic ExpensesLogic to set
     */
    public void setExpensesLogic(ExpensesLogic expensesLogic) {
        this.expensesLogic = expensesLogic;
    }

    /**
     * getter for userLogic
     * @return object of the UserLogic class
     */
    public UserLogic getUserLogic() {
        return userLogic;
    }

    /**
     * getter for new expense day
     * @return string of new expense day
     */
    public String getNewExpenseDay() {
        return newExpenseDay;
    }

    /**
     * setter for new expense day
     * @param newExpenseDay new expense day to set
     */
    public void setNewExpenseDay(String newExpenseDay) {
        this.newExpenseDay = newExpenseDay;
    }

    /**
     * getter for new expense month
     * @return string of new expense month
     */
    public String getNewExpenseMonth() {
        return newExpenseMonth;
    }

    /**
     * setter for new expense month
     * @param newExpenseMonth new expense month to set
     */
    public void setNewExpenseMonth(String newExpenseMonth) {
        this.newExpenseMonth = newExpenseMonth;
    }

    /**
     * getter for new expense year
     * @return string of new expense year
     */
    public String getNewExpenseYear() {
        return newExpenseYear;
    }

    /**
     * setter for new expense year
     * @param newExpenseYear new expense year to set
     */
    public void setNewExpenseYear(String newExpenseYear) {
        this.newExpenseYear = newExpenseYear;
    }

    /**
     * getter for new expense classification
     * @return string of new expense classification
     */
    public String getNewExpenseClassification() {
        return newExpenseClassification;
    }

    /**
     * setter for new expense classification
     * @param newExpenseClassification new expense classification to set
     */
    public void setNewExpenseClassification(String newExpenseClassification) {
        this.newExpenseClassification = newExpenseClassification;
    }

    /**
     * getter for new expense detail
     * @return string of new expense detail
     */
    public String getNewExpenseDetail() {
        return newExpenseDetail;
    }

    /**
     * setter for new expense detail
     * @param newExpenseDetail new expense detail to set
     */
    public void setNewExpenseDetail(String newExpenseDetail) {
        this.newExpenseDetail = newExpenseDetail;
    }

    /**
     * getter for new expense cost
     * @return string of new expense cost
     */
    public String getNewExpenseCost() {
        return newExpenseCost;
    }

    /**
     * setter for new expense cost
     * @param newExpenseCost new expense cost to set
     */
    public void setNewExpenseCost(String newExpenseCost) {
        this.newExpenseCost = newExpenseCost;
    }
}
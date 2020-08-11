package il.ac.hit.CashFlowManagement;

import il.ac.hit.CashFlowManagement.viewmodel.IViewModel;
import il.ac.hit.CashFlowManagement.viewmodel.ManagementViewModel;

/**
 * The program class
 * @author Sagi Sela
 * @author Moshiko Davila
 * @version March 24 , 2020.
 */
public class Program {
    public static void main(String[] args)
    {
        IViewModel viewModel = ManagementViewModel.getInstance();

        viewModel.startProgram();
    }
}
package com.green.philip.budgetplannerdroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Philip on 4/28/2016.
 */
public class ExpenseListFragment extends Fragment {
    private RecyclerView mExpenseRecyclerView;

    private ExpenseAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense_list, container, false);

        mExpenseRecyclerView = (RecyclerView) view.findViewById(R.id.expense_recycler_view);
        mExpenseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {
        ExpenseLab expenseLab = ExpenseLab.get(getActivity());
        List<Expense> expenses = expenseLab.getExpenses();

        mAdapter = new ExpenseAdapter(expenses);
        mExpenseRecyclerView.setAdapter(mAdapter);
    }

    private class ExpenseHolder extends RecyclerView.ViewHolder {
        private Expense mExpense;
        private TextView mAmountTextView;
        private TextView mExpenseCateogoryTextView;
        private TextView mExpenseDetailsTextView;
        private CheckBox mDeleteExpenseCheckBox;

        public ExpenseHolder(View itemView) {
            super(itemView);

            mAmountTextView = (TextView)itemView.findViewById(R.id.text_view_list_item_expense_amount);
            mExpenseCateogoryTextView = (TextView)itemView.findViewById(R.id.text_view_list_item_expense_category);
            mExpenseDetailsTextView = (TextView)itemView.findViewById(R.id.text_view_list_item_expense_details);
            mDeleteExpenseCheckBox = (CheckBox)itemView.findViewById(R.id.check_box_delete_expense_list_item);
        }

        public void bindExpense(Expense expense) {
            mExpense = expense;
            mAmountTextView.setText(mExpense.getAmount().toString());

            String category = expense.getExpenditureCategory(getActivity()) + ":";
            mExpenseCateogoryTextView.setText(category);

            mExpenseDetailsTextView.setText(mExpense.getDetail());
            mDeleteExpenseCheckBox.setChecked(mExpense.isToDelete());
        }
    }

    private class ExpenseAdapter extends RecyclerView.Adapter<ExpenseHolder> {
        private List<Expense> mExpenses;

        public ExpenseAdapter(List<Expense> expenses) {
            mExpenses = expenses;
        }

        @Override
        public ExpenseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.list_item_expense, parent, false);
            return new ExpenseHolder(view);
        }

        @Override
        public void onBindViewHolder(ExpenseHolder holder, int position) {
            Expense expense = mExpenses.get(position);
            holder.bindExpense(expense);
        }

        @Override
        public int getItemCount() {
            return mExpenses.size();
        }
    }


}

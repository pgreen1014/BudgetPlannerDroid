package com.green.philip.budgetplannerdroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import helperClasses.ParseHelper;

/**
 * Created by Philip on 4/28/2016.
 */
public class ExpenseListFragment extends Fragment {
    private RecyclerView mExpenseRecyclerView;
    private Button mDeleteExpenseButton;

    private ExpenseAdapter mAdapter;
    private List<Expense> mExpenses;
    private ExpenseLab mExpenseLab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense_list, container, false);

        mExpenseRecyclerView = (RecyclerView) view.findViewById(R.id.expense_recycler_view);
        mExpenseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDeleteExpenseButton = (Button) view.findViewById(R.id.button_delete_checked_list_items);
        mDeleteExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Expense> expensesToDelete = new ArrayList<Expense>();

                for(Expense expense: mExpenses) {
                    if(expense.isToDelete()) {
                        expensesToDelete.add(expense);
                    }
                }

                if(!expensesToDelete.isEmpty()) {
                    for(Expense expense: expensesToDelete) {
                        ParseHelper.deleteObject(expense.getId());
                    }
                    mExpenses.removeAll(expensesToDelete);
                }

                updateUI();
                getActivity().finish();

            }
        });

        updateUI();

        return view;
    }



    private void updateUI() {
        mExpenseLab = ExpenseLab.get(getActivity());
        mExpenseLab.setExpenses();
        mExpenses = mExpenseLab.getExpenses();

        if(mAdapter == null) {
            mAdapter = new ExpenseAdapter(mExpenses);
            mExpenseRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setExpenses(mExpenses);
            mAdapter.notifyDataSetChanged();
        }

    }

    private class ExpenseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Expense mExpense;
        private TextView mAmountTextView;
        private TextView mExpenseCategoryTextView;
        private TextView mExpenseDetailsTextView;
        private CheckBox mDeleteExpenseCheckBox;

        public ExpenseHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mAmountTextView = (TextView)itemView.findViewById(R.id.text_view_list_item_expense_amount);
            mExpenseCategoryTextView = (TextView)itemView.findViewById(R.id.text_view_list_item_expense_category);
            mExpenseDetailsTextView = (TextView)itemView.findViewById(R.id.text_view_list_item_expense_details);
            mDeleteExpenseCheckBox = (CheckBox)itemView.findViewById(R.id.check_box_delete_expense_list_item);

        }

        public void bindExpense(Expense expense) {
            mExpense = expense;
            mAmountTextView.setText(mExpense.getAmount().toString());

            String category = expense.getExpenditureCategory(getContext()) + ":";
            mExpenseCategoryTextView.setText(category);

            mExpenseDetailsTextView.setText(mExpense.getDetail());
            mDeleteExpenseCheckBox.setChecked(mExpense.isToDelete());
        }

        @Override
        public void onClick(View v) {
            mExpense.setToDelete(!mExpense.isToDelete());
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

        public void setExpenses(List<Expense> expenses) {
            mExpenses = expenses;
        }
    }


}

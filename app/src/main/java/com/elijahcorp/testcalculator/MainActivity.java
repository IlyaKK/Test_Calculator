package com.elijahcorp.testcalculator;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {
    private final String KEY_CALCULATIONS = "key_calculations";
    private MaterialButton calculateMaterialButton;
    private TextInputLayout expressionEditText;
    private TextView resultTextView;
    private Calculation calculation = new Calculation();
    private ActivityResultLauncher<Intent> calculatorLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initialiseOnChangeListenerExpressionEditText();
        initialiseGetResultFromCalculator();
        initialiseOnClickListenerCalculateMaterialButton();
    }

    private void initViews() {
        calculateMaterialButton = findViewById(R.id.calculate_material_button);
        expressionEditText = findViewById(R.id.expression_edit_text);
        resultTextView = findViewById(R.id.result_text_view);
    }

    private void initialiseOnChangeListenerExpressionEditText() {
        expressionEditText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                expressionEditText.setError(null);
                if (start > 0 && count > 0) {
                    if (!checkInsertSymbol(charSequence.toString().charAt(start - 1), charSequence.toString().charAt(start))) {
                        expressionEditText.setError("Введите другой символ");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initialiseGetResultFromCalculator() {
        calculatorLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                String resultExpression = data.getStringExtra(KEY_CALCULATIONS);
                resultTextView.setText(resultExpression);
            }
        });
    }

    private void initialiseOnClickListenerCalculateMaterialButton() {
        calculateMaterialButton.setOnClickListener(l -> {
            Uri uri = Uri.parse("string://string_expression");
            Intent runCalculateIntent = new Intent(Intent.ACTION_VIEW, uri);
            runCalculateIntent.putExtra(KEY_CALCULATIONS, expressionEditText.getEditText().getText().toString());
            calculatorLauncher.launch(runCalculateIntent);
        });
    }

    private boolean checkInsertSymbol(Character pastSymbol, Character newSymbol) {
        boolean pastSymbolIsOperation = calculation.isOperator(pastSymbol);
        boolean newSymbolIsOperation = calculation.isOperator(newSymbol);
        boolean pastSymbolIsPoint = pastSymbol == '.';
        boolean newSymbolIsPoint = newSymbol == '.';
        boolean pastSymbolIsZero = pastSymbol == '0';
        boolean newSymbolIsZero = newSymbol == '0';
        if (pastSymbolIsOperation && newSymbolIsOperation) {
            Toast.makeText(this, "Введите другой символ", Toast.LENGTH_SHORT).show();
        } else if (pastSymbolIsOperation && newSymbolIsPoint) {
            Toast.makeText(this, "Введите другой символ", Toast.LENGTH_SHORT).show();
        } else if (pastSymbolIsPoint && newSymbolIsOperation) {
            Toast.makeText(this, "Введите другой символ", Toast.LENGTH_SHORT).show();
        } else if (expressionEditText.getEditText().getText().length() - 1 == 1 && !pastSymbolIsZero) {
            return true;
        } else if (expressionEditText.getEditText().getText().length() - 1 == 1 && newSymbolIsZero) {
            return false;
        } else if (expressionEditText.getEditText().getText().length() - 1 == 1 && !newSymbolIsZero && (newSymbolIsOperation || newSymbolIsPoint)) {
            return true;
        } else if (expressionEditText.getEditText().getText().length() - 1 == 1 && !newSymbolIsZero && !newSymbolIsPoint) {
            return true;
        } else if (newSymbolIsPoint) {
            int countPoint = 0;
            for (int i = expressionEditText.getEditText().getText().length() - 1 - 1; i >= 0; i--) {
                if (expressionEditText.getEditText().getText().charAt(i) == '.') {
                    countPoint++;
                }
                if (calculation.isOperator(expressionEditText.getEditText().getText().charAt(i))) {
                    if (countPoint > 0) {
                        Toast.makeText(this, "Введите другой символ", Toast.LENGTH_SHORT).show();
                        return false;
                    } else {
                        return true;
                    }
                } else if (i == 0 && countPoint > 0) {
                    Toast.makeText(this, "Введите другой символ", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            return true;
        } else if (newSymbolIsZero) {
            for (int i = expressionEditText.getEditText().getText().length() - 1 - 1; i >= 0; i--) {
                if ((calculation.isOperator(expressionEditText.getEditText().getText().charAt(i))) && (i + 1 < expressionEditText.getEditText().getText().length()) && expressionEditText.getEditText().getText().charAt(i + 1) == '0') {
                    Toast.makeText(this, "Введите другой символ", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (expressionEditText.getEditText().getText().charAt(i) == '.') {
                    return true;
                }
            }
            return true;
        } else {
            return true;
        }
        return false;
    }
}
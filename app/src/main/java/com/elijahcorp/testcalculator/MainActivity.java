package com.elijahcorp.testcalculator;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

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
    private final Checkers checkers = new Checkers();
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
        if (expressionEditText.getEditText() != null) {
            expressionEditText.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    if (start > 0 && count > 0) {
                        if (!checkers.checkInsertSymbol(charSequence, charSequence.toString().charAt(start - 1), charSequence.toString().charAt(start))) {
                            expressionEditText.setError(getResources().getString(R.string.insert_another_symbol));
                        }
                    }else {
                        expressionEditText.setError(null);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }

    private void initialiseGetResultFromCalculator() {
        calculatorLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    String resultExpression = data.getStringExtra(KEY_CALCULATIONS);
                    resultTextView.setText(resultExpression);
                }
            }
        });
    }

    private void initialiseOnClickListenerCalculateMaterialButton() {
        calculateMaterialButton.setOnClickListener(l -> {
            Uri uri = Uri.parse("string://string_expression");
            Intent runCalculateIntent = new Intent(Intent.ACTION_VIEW, uri);
            if (expressionEditText.getEditText() != null) {
                runCalculateIntent.putExtra(KEY_CALCULATIONS, expressionEditText.getEditText().getText().toString());
                calculatorLauncher.launch(runCalculateIntent);
            }
        });
    }
}
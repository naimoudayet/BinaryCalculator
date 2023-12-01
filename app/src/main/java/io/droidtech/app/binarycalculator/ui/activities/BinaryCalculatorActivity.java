package io.droidtech.app.binarycalculator.ui.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Objects;

import io.droidtech.app.R;
import io.droidtech.app.binarycalculator.model.Calculation;

public class BinaryCalculatorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    protected HorizontalScrollView scv_display;
    protected TextView lbl_display, lbl_expression;
    protected ImageButton imb_copy, imb_delete;
    protected Button btn_number_zero, btn_number_one, btn_number_two, btn_number_three, btn_number_four, btn_number_five,
            btn_number_six, btn_number_seven, btn_number_eight, btn_number_nine,
            btn_operation_add, btn_operation_multiply, btn_operation_subtract, btn_operation_divide, btn_evaluation, btn;

    private int nav_index = 0;
    private String currentExpression = "";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        getViews();
        updateViews(nav_index);

        imb_delete.setOnLongClickListener(v -> {
            initString();
            if (!currentExpression.equals("")) {
                currentExpression = "";
                updateDisplay(currentExpression);
                updateExpression("");
                vibrate();
            }

            return true;
        });
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {

        int button_id = v.getId();
        switch (button_id) {
            case R.id.imb_delete:
                initString();
                if (currentExpression.length() > 0) {
                    updateDisplay(currentExpression.substring(0, currentExpression.length() - 1));
                    updateExpression("");
                }
                break;
            case R.id.imb_copy:
                if (!lbl_display.getText().toString().equals("")) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("result", lbl_display.getText());
                    Objects.requireNonNull(clipboard).setPrimaryClip(clip);
                    Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_evaluation:
                if (!validateEval(currentExpression))
                    performEvaluation();
                break;
            case R.id.btn_number_zero:
            case R.id.btn_number_one:
            case R.id.btn_number_two:
            case R.id.btn_number_three:
            case R.id.btn_number_four:
            case R.id.btn_number_five:
            case R.id.btn_number_six:
            case R.id.btn_number_seven:
            case R.id.btn_number_eight:
            case R.id.btn_number_nine:
                btn = (Button) v;
                appendNumber(btn.getText().toString());
                break;
            case R.id.btn_operation_add:
            case R.id.btn_operation_divide:
            case R.id.btn_operation_multiply:
            case R.id.btn_operation_subtract:
                btn = (Button) v;
                appendOperator(btn.getText().toString());
                break;
        }
    }

    private void performEvaluation() {

        HashMap<String, String> cal = new HashMap<>();

        Calculation binCalc = new Calculation();
        initString();

        switch (nav_index) {
            case 0:
                cal = binCalc.binaryCalculator(currentExpression);
                break;
            case 1:
                cal = binCalc.binaryToDecimal(currentExpression);
                break;
            case 2:
                cal = binCalc.decimalToBinary(currentExpression);
                break;
        }

        if (Objects.requireNonNull(cal.get("error")).equals("1")) {
            showDialog(cal.get("error_message"));
        } else {
            // evaluate_epression = true;
            updateExpression(cal.get("init_expression"));
            updateDisplay(cal.get("result"));
        }

    }

    private void appendNumber(String number) {
        initString();

        if (currentExpression.length() == 1) {
            if (currentExpression.startsWith("0")) {
                if (!number.equals("0")) {
                    currentExpression += number;
                    updateDisplay(currentExpression);
                    updateExpression("");
                }
            } else {
                currentExpression += number;
                updateDisplay(currentExpression);
                updateExpression("");
            }
        } else {
            currentExpression += number;
            updateDisplay(currentExpression);
            updateExpression("");
        }

    }

    private void appendOperator(String operator) {
        initString();
        if (validateAddingOperator(currentExpression)) {
            currentExpression += operator;
            updateDisplay(currentExpression);
            updateExpression("");
        }
    }

    private boolean validateAddingOperator(String expression) {
        if (expression.endsWith("*") ||
                expression.endsWith("/") ||
                expression.endsWith("-") ||
                expression.endsWith("+")) {
            return false;
        } else if (expression.contains("*") ||
                expression.contains("/") ||
                expression.contains("-") ||
                expression.contains("+")) {
            return false;
        } else return !expression.equals("");
    }

    private boolean validateEval(String expression) {
        if (expression.endsWith("*") ||
                expression.endsWith("/") ||
                expression.endsWith("-") ||
                expression.endsWith("+")) {
            return true;
        } else return expression.equals("");
    }

    private void updateDisplay(String currentExpression) {
        lbl_display.setText(currentExpression);
        scv_display.fullScroll(View.FOCUS_RIGHT);
    }

    private void updateExpression(String currentExpression) {
        lbl_expression.setText(currentExpression);
    }

    private void initString() {
        currentExpression = lbl_display.getText().toString();
    }

    private void getViews() {

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment standard_display_fragment = fragmentManager.findFragmentById(R.id.standard_display_fragment);
        Fragment standard_input_fragment = fragmentManager.findFragmentById(R.id.standard_input_fragment);


        if (standard_display_fragment != null) {
            scv_display = Objects.requireNonNull(standard_display_fragment.getView()).findViewById(R.id.scv_display);
            lbl_display = standard_display_fragment.getView().findViewById(R.id.lbl_display);
            lbl_expression = standard_display_fragment.getView().findViewById(R.id.lbl_expression);
            imb_copy = standard_display_fragment.getView().findViewById(R.id.imb_copy);
            imb_delete = standard_display_fragment.getView().findViewById(R.id.imb_delete);
        }


        if (standard_input_fragment != null) {
            btn_number_zero = Objects.requireNonNull(standard_input_fragment.getView()).findViewById(R.id.btn_number_zero);
            btn_number_one = standard_input_fragment.getView().findViewById(R.id.btn_number_one);
            btn_number_two = standard_input_fragment.getView().findViewById(R.id.btn_number_two);
            btn_number_three = standard_input_fragment.getView().findViewById(R.id.btn_number_three);
            btn_number_four = standard_input_fragment.getView().findViewById(R.id.btn_number_four);
            btn_number_five = standard_input_fragment.getView().findViewById(R.id.btn_number_five);
            btn_number_six = standard_input_fragment.getView().findViewById(R.id.btn_number_six);
            btn_number_seven = standard_input_fragment.getView().findViewById(R.id.btn_number_seven);
            btn_number_eight = standard_input_fragment.getView().findViewById(R.id.btn_number_eight);
            btn_number_nine = standard_input_fragment.getView().findViewById(R.id.btn_number_nine);

            btn_operation_add = standard_input_fragment.getView().findViewById(R.id.btn_operation_add);
            btn_operation_multiply = standard_input_fragment.getView().findViewById(R.id.btn_operation_multiply);
            btn_operation_subtract = standard_input_fragment.getView().findViewById(R.id.btn_operation_subtract);
            btn_operation_divide = standard_input_fragment.getView().findViewById(R.id.btn_operation_divide);

            btn_evaluation = standard_input_fragment.getView().findViewById(R.id.btn_evaluation);
        }

        btn_operation_divide.setOnClickListener(this);
        btn_operation_multiply.setOnClickListener(this);
        btn_operation_subtract.setOnClickListener(this);
        btn_operation_add.setOnClickListener(this);

        btn_number_zero.setOnClickListener(this);
        btn_number_one.setOnClickListener(this);
        btn_number_two.setOnClickListener(this);
        btn_number_three.setOnClickListener(this);
        btn_number_four.setOnClickListener(this);
        btn_number_five.setOnClickListener(this);
        btn_number_six.setOnClickListener(this);
        btn_number_seven.setOnClickListener(this);
        btn_number_eight.setOnClickListener(this);
        btn_number_nine.setOnClickListener(this);

        btn_evaluation.setOnClickListener(this);

        imb_delete.setOnClickListener(this);
        imb_copy.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about_dev)
            startActivity(new Intent(BinaryCalculatorActivity.this, AboutDevActivity.class));

        return super.onOptionsItemSelected(item);
    }

    private void goToMarket(int i) {

        Uri uri;
        Intent intent;

        switch (i) {
            case 1:
                uri = Uri.parse("market://details?id=" + this.getPackageName());
                intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
                }
                break;
            case 2:
                uri = Uri.parse("market://developer?id=DroidTech1989");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/developer?id=DroidTech1989")));
                }
                break;
        }
    }

    private void updateViews(int nav_index) {

        updateDisplay("");
        updateExpression("");
        currentExpression = "";

        switch (nav_index) {
            case 0:
                enableDisableNumbers(false, "#55000000");
                enableDisableOperators(true, "#ffffff");
                updateTitle("Binary Calculator");
                break;
            case 1:
                enableDisableNumbers(false, "#55000000");
                enableDisableOperators(false, "#55000000");
                updateTitle("Binary to Decimal");
                break;
            case 2:
                enableDisableNumbers(true, "#ffffff");
                enableDisableOperators(false, "#55000000");
                updateTitle("Decimal to Binary");
                break;
        }
    }

    private void enableDisableNumbers(boolean b, String color) {
        btn_number_two.setEnabled(b);
        btn_number_three.setEnabled(b);
        btn_number_four.setEnabled(b);
        btn_number_five.setEnabled(b);
        btn_number_six.setEnabled(b);
        btn_number_seven.setEnabled(b);
        btn_number_eight.setEnabled(b);
        btn_number_nine.setEnabled(b);

        btn_number_two.setTextColor(Color.parseColor(color));
        btn_number_three.setTextColor(Color.parseColor(color));
        btn_number_four.setTextColor(Color.parseColor(color));
        btn_number_five.setTextColor(Color.parseColor(color));
        btn_number_six.setTextColor(Color.parseColor(color));
        btn_number_seven.setTextColor(Color.parseColor(color));
        btn_number_eight.setTextColor(Color.parseColor(color));
        btn_number_nine.setTextColor(Color.parseColor(color));
    }

    private void enableDisableOperators(boolean b, String color) {
        btn_operation_add.setEnabled(b);
        btn_operation_multiply.setEnabled(b);
        btn_operation_subtract.setEnabled(b);
        btn_operation_divide.setEnabled(b);

        btn_operation_add.setTextColor(Color.parseColor(color));
        btn_operation_multiply.setTextColor(Color.parseColor(color));
        btn_operation_subtract.setTextColor(Color.parseColor(color));
        btn_operation_divide.setTextColor(Color.parseColor(color));
    }

    private void updateTitle(String title) {
        toolbar.setTitle(title);
    }

    private void vibrate() {
        Vibrator vb = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        if (vb != null)
            vb.vibrate(100);
    }

    private void showDialog(String message) {
        final AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();

        alertDialog.setTitle(message);
        alertDialog.setIcon(R.drawable.shield_error_icon);

        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Close", (dialog, which) -> alertDialog.dismiss());

        alertDialog.show();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_binary_calculator:
                nav_index = 0;
                break;
            case R.id.nav_binary_to_decimal:
                nav_index = 1;
                break;
            case R.id.nav_decimal_to_binary:
                nav_index = 2;
                break;

            case R.id.nav_rate_app:
                goToMarket(1);
                break;
            case R.id.nav_more_apps:
                goToMarket(2);
                break;
            case R.id.nav_exit:
                finish();
                break;

        }
        updateViews(nav_index);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

}
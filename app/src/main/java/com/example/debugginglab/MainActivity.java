package com.example.debugginglab;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Lab6";

    private TextView tvResult;
    private Button btnStart;

    // Параметры a, b, c
    private static final double a = 2.0;
    private static final double b = 3.0;   // b ≠ 0
    private static final double c = 5.0;

    // Диапазон x: от -30 до 30
    private int currentIndex = 0;
    private final int MIN_X = -30;
    private final int MAX_X = 30;

    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);
        btnStart = findViewById(R.id.btnStart);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCalculation();
            }
        });
    }

    private void startCalculation() {
        if (timer != null) {
            return;
        }
        currentIndex = 0;
        btnStart.setEnabled(false);

        // Таймер на 61 секунду
        timer = new CountDownTimer((MAX_X - MIN_X + 1) * 1000L, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                int x = MIN_X + currentIndex;
                double result = calculateF(x);

                // Вывод на экран
                String display = String.format("x = %d\nF(x) = %.4f", x, result);
                tvResult.setText(display);

                // Логирование с тегом "Lab6"
                Log.i(TAG, String.format("x=%d, F=%.4f", x, result));

                currentIndex++;
            }

            @Override
            public void onFinish() {
                tvResult.setText("Готово!\nДиапазон -30..30 пройден");
                btnStart.setEnabled(true);
                timer = null;
            }
        }.start();
    }

    // Функция F
    private double calculateF(int x) {
        // Ветка 1: x < 3 и b ≠ 0
        if (x < 3 && b != 0) {
            return a * x * x - b * x + c;
        }
        // Ветка 2: x > 3 и b = 0
        else if (x > 3 && b == 0) {
            // Избегаем деления на ноль
            if (x == c) {
                Log.e(TAG, "Деление на ноль: x == c = " + c);
                return Double.NaN;
            }
            return (x - a) / (x - c);
        }
        //  все остальные случаи
        else {
            if (c == 0) {
                Log.e(TAG, "c = 0, деление на ноль в третьей ветке");
                return Double.NaN;
            }
            return -x / c;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
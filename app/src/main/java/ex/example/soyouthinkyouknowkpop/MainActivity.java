package ex.example.soyouthinkyouknowkpop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private IdolManager idolManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            idolManager = new IdolManager();
        } catch (Exception e) {
            Log.e("Main", "Failed to init idol manager due to ", e);
        }
    }
}
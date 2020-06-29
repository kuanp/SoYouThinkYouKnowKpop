package ex.example.soyouthinkyouknowkpop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.jsoup.select.Evaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private IdolManager idolManager;
    private ImageView imageView;
    private List<Button> buttons;
    private Random random;
    private Idol chosenIdol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        buttons = new ArrayList<>();
        buttons.add(findViewById(R.id.button1));
        buttons.add(findViewById(R.id.button2));
        buttons.add(findViewById(R.id.button3));
        buttons.add(findViewById(R.id.button4));

        try {
            idolManager = new IdolManager();
        } catch (Exception e) {
            Log.e("Main", "Failed to init idol manager due to ", e);
        }

        random = new Random();

        setUpNewChallenge();
    }

    public void onButtonClicked(View view) {
        Idol userChoice = (Idol) view.getTag();
        if (userChoice.equals(chosenIdol)) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this,
                    String.format("Wrong! That was %s", chosenIdol.getName()),
                    Toast.LENGTH_LONG).show();
        }

        setUpNewChallenge();
    }

    private void setUpNewChallenge() {
        List<Idol> finalistIdols = idolManager.chooseIdolsAtRandom(buttons.size());
        chosenIdol = finalistIdols.get(random.nextInt(finalistIdols.size()));

        try {
            imageView.setImageBitmap(IdolManager.getIdolPhoto(chosenIdol));
        } catch (Exception e) {
            Log.e("Main", "Failed to setup new challenge.");
            e.printStackTrace();
        }

        for (int i = 0; i < finalistIdols.size(); i++) {
            buttons.get(i).setTag(finalistIdols.get(i));
            buttons.get(i).setText(finalistIdols.get(i).getCompositeIdentity());
        }
    }
}
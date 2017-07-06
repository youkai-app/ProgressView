package app.youkai.progressview.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import app.youkai.progressview.ProgressView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ProgressView progressView = (ProgressView) findViewById(R.id.progressView);
        final TextView textView = (TextView) findViewById(R.id.textView);

        textView.setText("Progress: " + progressView.getProgress());

        progressView.setMax(100);
        progressView.setListener(new ProgressView.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress) {
                textView.setText("Progress: " + progress);
            }
        });
    }

}

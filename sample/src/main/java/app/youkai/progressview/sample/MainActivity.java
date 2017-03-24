package app.youkai.progressview.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import app.youkai.progressview.ProgressView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ProgressView progressView = (ProgressView) findViewById(R.id.progressView);

        progressView.setTotal(24);
        progressView.setListener(new ProgressView.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress) {
                Toast.makeText(MainActivity.this, "Progress: " + progress, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

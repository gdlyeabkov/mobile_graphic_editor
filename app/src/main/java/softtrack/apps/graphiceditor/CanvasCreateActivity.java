package softtrack.apps.graphiceditor;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class CanvasCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_create);

        initialize();

    }

    public void initialize() {
        initializeActionBar();
    }

    public void initializeActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Новый холст");
    }

}

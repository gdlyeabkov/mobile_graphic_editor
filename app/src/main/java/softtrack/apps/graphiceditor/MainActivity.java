package softtrack.apps.graphiceditor;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    public LinearLayout activityMainContainerCanvas;
    public LinearLayout activityMainContainerToolbarBodyPen;
    public LinearLayout activityMainContainerToolbarBodyEraser;
    public LinearLayout activityMainContainerToolbarBodyCurve;
    public LinearLayout activityMainContainerToolbarBodyShape;
    public LinearLayout activityMainContainerToolbarBodyFill;
    public LinearLayout activityMainContainerToolbarBodyGradient;
    public LinearLayout activityMainContainerToolbarBodyText;
    public SofttrackCanvas canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

    }

    public void initialize() {
        findViews();
        initializeCanvas();
        initializeActionBar();
        addHandlers();
    }

    public void findViews() {
        activityMainContainerToolbarBodyPen = findViewById(R.id.activity_main_container_toolbar_body_pen);
        activityMainContainerToolbarBodyEraser = findViewById(R.id.activity_main_container_toolbar_body_eraser);
        activityMainContainerToolbarBodyCurve = findViewById(R.id.activity_main_container_toolbar_body_curve);
        activityMainContainerToolbarBodyShape = findViewById(R.id.activity_main_container_toolbar_body_shape);
        activityMainContainerToolbarBodyFill = findViewById(R.id.activity_main_container_toolbar_body_fill);
        activityMainContainerToolbarBodyGradient = findViewById(R.id.activity_main_container_toolbar_body_gradient);
        activityMainContainerToolbarBodyText = findViewById(R.id.activity_main_container_toolbar_body_text);
        activityMainContainerCanvas = findViewById(R.id.activity_main_container_canvas);
    }

    public void initializeCanvas() {
        canvas = new SofttrackCanvas(MainActivity.this);
        canvas.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        activityMainContainerCanvas.addView(canvas);
    }

    public void initializeActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Softtrack Графический редактор");
    }

    public void addHandlers() {
        activityMainContainerToolbarBodyPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.setContentDescription("pen");
            }
        });
        activityMainContainerToolbarBodyEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.setContentDescription("eraser");
            }
        });
        activityMainContainerToolbarBodyCurve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.setContentDescription("curve");
            }
        });
        activityMainContainerToolbarBodyShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.setContentDescription("shape");
            }
        });
        activityMainContainerToolbarBodyFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.setContentDescription("fill");
            }
        });
        activityMainContainerToolbarBodyGradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.setContentDescription("gradient");
            }
        });
        activityMainContainerToolbarBodyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.setContentDescription("text");
            }
        });
    }

}
package softtrack.apps.graphiceditor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    public LinearLayout activityStartContainerDrawActionsNew;
    public LinearLayout activityStartContainerDrawActionsLast;
    public LinearLayout activityStartContainerDrawActionsGallery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initialize();

    }

    public void initialize() {
        findViews();
    }

    public void findViews() {
        activityStartContainerDrawActionsNew = findViewById(R.id.activity_start_container_draw_actions_new);
        activityStartContainerDrawActionsLast = findViewById(R.id.activity_start_container_draw_actions_last);
        activityStartContainerDrawActionsGallery = findViewById(R.id.activity_start_container_draw_actions_gallery);
        activityStartContainerDrawActionsNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, CanvasCreateActivity.class);
                StartActivity.this.startActivity(intent);
            }
        });
        activityStartContainerDrawActionsLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                StartActivity.this.startActivity(intent);
            }
        });
        activityStartContainerDrawActionsGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, GalleryActivity.class);
                StartActivity.this.startActivity(intent);
            }
        });

    }

}

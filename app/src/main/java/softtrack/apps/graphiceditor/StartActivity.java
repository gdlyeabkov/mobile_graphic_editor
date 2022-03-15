package softtrack.apps.graphiceditor;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class StartActivity extends AppCompatActivity {

    public LinearLayout activityStartContainerDrawActionsNew;
    public LinearLayout activityStartContainerDrawActionsLast;
    public LinearLayout activityStartContainerDrawActionsGallery;
    public Menu myMenu;
    public DrawerLayout activityStartDrawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initialize();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInfalter = getMenuInflater();
        menuInfalter.inflate(R.menu.activity_start_menu, menu);
        myMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void initialize() {
        findViews();
        addHandlers();
        initializeActionBar();
    }

    public void findViews() {
        activityStartContainerDrawActionsNew = findViewById(R.id.activity_start_container_draw_actions_new);
        activityStartContainerDrawActionsLast = findViewById(R.id.activity_start_container_draw_actions_last);
        activityStartContainerDrawActionsGallery = findViewById(R.id.activity_start_container_draw_actions_gallery);
        activityStartDrawer = findViewById(R.id.activity_start_drawer);
    }

    public void addHandlers() {
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

    public void initializeActionBar() {
        ActionBar actionBar = getSupportActionBar();
        LinearLayout customView = new LinearLayout(StartActivity.this);
        customView.setOrientation(LinearLayout.HORIZONTAL);
        ImageView customViewIcon = new ImageView(StartActivity.this);
        LinearLayout.LayoutParams customViewIconLayoutParams = new LinearLayout.LayoutParams(50, 50);
        customViewIcon.setLayoutParams(customViewIconLayoutParams);
        customViewIcon.setImageResource(R.drawable.burger);
        customView.addView(customViewIcon);
        TextView customViewHeader = new TextView(StartActivity.this);
        LinearLayout.LayoutParams customViewHeaderLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        customViewHeaderLayoutParams.setMargins(100, 20, 0, 0);
        customViewHeader.setLayoutParams(customViewHeaderLayoutParams);
        customViewHeader.setText("Softtrack Графический редактор");
        customView.addView(customViewHeader);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(customView);
        customViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityStartDrawer.openDrawer(Gravity.LEFT);
            }
        });
    }

}

package softtrack.apps.graphiceditor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.util.HashMap;

public class CanvasCreateActivity extends AppCompatActivity {

    public Button activityCanvasCreateContainerEditBtn;
    public Button activityCanvasCreateContainerCreateBtn;
    public LinearLayout canvasEditDialogContainerBackgroundColorBtnWrap;
    public int backgroundColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_create);

        initialize();

    }

    public void initialize() {
        initializeConstants();
        findViews();
        addHandlers();
        initializeActionBar();
    }

    public void initializeConstants() {
        backgroundColor = Color.WHITE;
    }

    public void findViews() {
        activityCanvasCreateContainerEditBtn = findViewById(R.id.activity_canvas_create_container_edit_btn);
        activityCanvasCreateContainerCreateBtn = findViewById(R.id.activity_canvas_create_container_create_btn);
        canvasEditDialogContainerBackgroundColorBtnWrap = findViewById(R.id.canvas_edit_dialog_container_background_color_btn_wrap);
    }

    public void addHandlers() {
        activityCanvasCreateContainerEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CanvasCreateActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.canvas_edit_dialog, null);
                builder.setView(dialogView);
                builder.setCancelable(false);
                builder.setNegativeButton("ОТМЕНА", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                canvasEditDialogContainerBackgroundColorBtnWrap = dialogView.findViewById(R.id.canvas_edit_dialog_container_background_color_btn_wrap);
                canvasEditDialogContainerBackgroundColorBtnWrap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new ColorPickerDialog.Builder(CanvasCreateActivity.this)
                            .setPreferenceName("MyColorPickerDialog")
                            .setPositiveButton("ОК",
                                new ColorEnvelopeListener() {
                                    @Override
                                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                        backgroundColor = envelope.getColor();
                                    }
                                })
                            .setNegativeButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                            .attachAlphaSlideBar(true) // the default value is true.
                            .attachBrightnessSlideBar(true)  // the default value is true.
                            .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                            .show();
                    }
                });
                AlertDialog alert = builder.create();
                alert.setTitle("");
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                    }
                });
                alert.show();
            }
        });
        activityCanvasCreateContainerCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CanvasCreateActivity.this, MainActivity.class);
                CanvasCreateActivity.this.startActivity(intent);
            }
        });
    }

    public void initializeActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Новый холст");
    }

}

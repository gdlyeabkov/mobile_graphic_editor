package softtrack.apps.graphiceditor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    public TextView activityСanvasСreateСontainerWidthLabel;
    public TextView activityСanvasСreateСontainerHeightLabel;
    public TextView activityСanvasСreateСontainerDpiLabel;
    public int width;
    public int height;
    public int dpi;
    public String paperSize;
    public int backgroundColor;
    public int tempColor;

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
        backgroundColor = Color.TRANSPARENT;
        tempColor = backgroundColor;
        width = 1007;
        height = 1414;
        dpi = 350;
        paperSize = "";
    }

    public void findViews() {
        activityCanvasCreateContainerEditBtn = findViewById(R.id.activity_canvas_create_container_edit_btn);
        activityCanvasCreateContainerCreateBtn = findViewById(R.id.activity_canvas_create_container_create_btn);
        canvasEditDialogContainerBackgroundColorBtnWrap = findViewById(R.id.canvas_edit_dialog_container_background_color_btn_wrap);
        activityСanvasСreateСontainerWidthLabel = findViewById(R.id.activity_canvas_create_container_width_label);
        activityСanvasСreateСontainerHeightLabel = findViewById(R.id.activity_canvas_create_container_height_label);
        activityСanvasСreateСontainerDpiLabel = findViewById(R.id.activity_canvas_create_container_dpi_label);
    }

    public void addHandlers() {
        activityCanvasCreateContainerEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempColor = backgroundColor;
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
                        EditText canvasEditDialogContainerWidthRowField = dialogView.findViewById(R.id.canvas_edit_dialog_container_width_row_field);
                        CharSequence rawCanvasEditDialogContainerWidthRowFieldContent =  canvasEditDialogContainerWidthRowField.getText();
                        String canvasEditDialogContainerWidthRowFieldContent = rawCanvasEditDialogContainerWidthRowFieldContent.toString();
                        activityСanvasСreateСontainerWidthLabel.setText("Ширина " + canvasEditDialogContainerWidthRowFieldContent + " px");
                        EditText canvasEditDialogContainerHeightRowField = dialogView.findViewById(R.id.canvas_edit_dialog_container_height_row_field);
                        CharSequence rawCanvasEditDialogContainerHeightRowFieldContent =  canvasEditDialogContainerHeightRowField.getText();
                        String canvasEditDialogContainerHeightRowFieldContent = rawCanvasEditDialogContainerHeightRowFieldContent.toString();
                        activityСanvasСreateСontainerHeightLabel.setText("Высота " + canvasEditDialogContainerHeightRowFieldContent + " px");
                        EditText canvasEditDialogContainerDpiField = dialogView.findViewById(R.id.canvas_edit_dialog_container_dpi_field);
                        CharSequence rawCanvasEditDialogContainerDpiFieldContent =  canvasEditDialogContainerDpiField.getText();
                        String canvasEditDialogContainerDpiFieldContent = rawCanvasEditDialogContainerDpiFieldContent.toString();
                        activityСanvasСreateСontainerDpiLabel.setText("точек на дюйм " + canvasEditDialogContainerDpiFieldContent);
                        int canvasEditDialogContainerWidthRowFieldValue = Integer.valueOf(canvasEditDialogContainerWidthRowFieldContent);
                        int canvasEditDialogContainerHeightRowFieldValue = Integer.valueOf(canvasEditDialogContainerHeightRowFieldContent);
                        int canvasEditDialogContainerDpiFieldValue = Integer.valueOf(canvasEditDialogContainerDpiFieldContent);
                        width = canvasEditDialogContainerWidthRowFieldValue;
                        height = canvasEditDialogContainerHeightRowFieldValue;
                        dpi = canvasEditDialogContainerDpiFieldValue;
                        RadioGroup canvasEditDialogContainerBackgroundGroup = dialogView.findViewById(R.id.canvas_edit_dialog_container_background_group);
                        int selectedColorTypeId = canvasEditDialogContainerBackgroundGroup.getCheckedRadioButtonId();
                        if (selectedColorTypeId == R.id.canvas_edit_dialog_container_background_color) {
                            backgroundColor = tempColor;
                        } else {
                            backgroundColor = Color.TRANSPARENT;
                        }

                    }
                });
                AlertDialog alert = builder.create();
                alert.setTitle("");
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Spinner canvasEditDialogContainerWidthRowMeasure = dialogView.findViewById(R.id.canvas_edit_dialog_container_width_row_measure);
                        Spinner canvasEditDialogContainerHeightRowMeasure = dialogView.findViewById(R.id.canvas_edit_dialog_container_height_row_measure);
                        ArrayAdapter adapter = ArrayAdapter.createFromResource(CanvasCreateActivity.this,
                            R.array.size_measures, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        canvasEditDialogContainerWidthRowMeasure.setAdapter(adapter);
                        canvasEditDialogContainerHeightRowMeasure.setAdapter(adapter);
                        canvasEditDialogContainerBackgroundColorBtnWrap = dialogView.findViewById(R.id.canvas_edit_dialog_container_background_color_btn_wrap);
                        ImageButton canvasEditDialogContainerBackgroundColorBtn = dialogView.findViewById(R.id.canvas_edit_dialog_container_background_color_btn);
                        canvasEditDialogContainerBackgroundColorBtnWrap.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new ColorPickerDialog.Builder(CanvasCreateActivity.this)
                                    .setPreferenceName("MyColorPickerDialog")
                                    .setPositiveButton("ОК",
                                        new ColorEnvelopeListener() {
                                            @Override
                                            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                                /*
                                                width = 1007;
                                                height = 1414;
                                                dpi = 350;
                                                paperSize = "";
                                                */
                                                // backgroundColor = envelope.getColor();
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
                        canvasEditDialogContainerBackgroundColorBtn = dialogView.findViewById(R.id.canvas_edit_dialog_container_background_color_btn);
                        canvasEditDialogContainerBackgroundColorBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new ColorPickerDialog.Builder(CanvasCreateActivity.this)
                                    .setPreferenceName("MyColorPickerDialog")
                                    .setPositiveButton("ОК",
                                            new ColorEnvelopeListener() {
                                                @Override
                                                public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                                    /*
                                                    backgroundColor = envelope.getColor();
                                                    */
                                                    tempColor = envelope.getColor();
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
                        Spinner canvasEditDialogContainerPaperSpinner = dialogView.findViewById(R.id.canvas_edit_dialog_container_paper_spinner);
                        adapter = ArrayAdapter.createFromResource(CanvasCreateActivity.this,
                            R.array.size_paper, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        canvasEditDialogContainerPaperSpinner.setAdapter(adapter);

                    }
                });
                alert.show();
            }
        });
        activityCanvasCreateContainerCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CanvasCreateActivity.this, MainActivity.class);
                intent.putExtra("width", width);
                intent.putExtra("height", height);
                intent.putExtra("dpi", dpi);
                intent.putExtra("paperSize", paperSize);
                intent.putExtra("backgroundColor", backgroundColor);
                CanvasCreateActivity.this.startActivity(intent);
            }
        });
    }

    public void initializeActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Новый холст");
    }

}

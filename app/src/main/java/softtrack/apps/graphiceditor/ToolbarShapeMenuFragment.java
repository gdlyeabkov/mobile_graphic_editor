package softtrack.apps.graphiceditor;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.sliders.AlphaSlideBar;

public class ToolbarShapeMenuFragment extends Fragment {

    MainActivity parentActivity;
    LinearLayout toolbarShapeMenuContainerRect;
    LinearLayout toolbarShapeMenuContainerOval;
    LinearLayout toolbarShapeMenuContainerPolygone;
    TextView toolbarShapeMenuContainerOpacityLabel;
    SeekBar toolbarShapeMenuContainerOpacitySlider;
    LinearLayout toolbarShapeMenuContainerRound;

    ToolbarShapeMenuFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.toolbar_shape_menu, container, false);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initialize() {
        initializeConstants();
        findViews();
        initializeViews();
        addHandlers();
    }

    public void initializeConstants() {
        parentActivity = (MainActivity) getActivity();
    }

    public void findViews() {
        toolbarShapeMenuContainerRect = parentActivity.findViewById(R.id.toolbar_shape_menu_container_rect);
        toolbarShapeMenuContainerOval = parentActivity.findViewById(R.id.toolbar_shape_menu_container_oval);
        toolbarShapeMenuContainerPolygone = parentActivity.findViewById(R.id.toolbar_shape_menu_container_polygone);
        toolbarShapeMenuContainerOpacityLabel = parentActivity.findViewById(R.id.toolbar_shape_menu_container_opacity_label);
        toolbarShapeMenuContainerOpacitySlider = parentActivity.findViewById(R.id.toolbar_shape_menu_container_opacity_slider);
        toolbarShapeMenuContainerRound = parentActivity.findViewById(R.id.toolbar_shape_menu_container_round);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initializeViews() {
        toolbarShapeMenuContainerOpacitySlider.setMin(0);
        toolbarShapeMenuContainerOpacitySlider.setMax(100);
        toolbarShapeMenuContainerOpacitySlider.setProgress(100);
    }

    public void addHandlers() {
        toolbarShapeMenuContainerRect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.gateway.activeToolbarMenuItem = "rect";
            }
        });
        toolbarShapeMenuContainerOval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.gateway.activeToolbarMenuItem = "oval";
            }
        });
        toolbarShapeMenuContainerPolygone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.gateway.activeToolbarMenuItem = "polygone";
            }
        });
        toolbarShapeMenuContainerOpacitySlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                toolbarShapeMenuContainerOpacityLabel.setText("Непрозрачность\n" + String.valueOf(i) + " %");
                ColorPickerView activityMainPaleteColor = MainActivity.gateway.findViewById(R.id.activity_main_palete_color);
                float alpha = i / 100f;
                activityMainPaleteColor.setAlpha(alpha);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        toolbarShapeMenuContainerRound.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                PopupWindow popupWindow = new PopupWindow(parentActivity);
                View layout = getLayoutInflater().inflate(R.layout.round_shape_menu, null);
                popupWindow.setContentView(layout);
                popupWindow.setWidth(450);
                popupWindow.setHeight(150);
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAsDropDown(view);
                TextView roundShapeMenuContainerLabel = layout.findViewById(R.id.round_shape_menu_container_label);
                SeekBar roundShapeMenuContainerSlider = layout.findViewById(R.id.round_shape_menu_container_slider);
                roundShapeMenuContainerSlider.setMin(0);
                roundShapeMenuContainerSlider.setMax(100);
                int radius = MainActivity.gateway.roundRadius;
                roundShapeMenuContainerSlider.setProgress(radius);
                roundShapeMenuContainerLabel.setText("Скругленный угол\n" + String.valueOf(radius) + " %");
                roundShapeMenuContainerSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        roundShapeMenuContainerLabel.setText("Скругленный угол\n" + String.valueOf(i) + " %");
                        MainActivity.gateway.roundRadius = i;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
            }
        });
    }

}

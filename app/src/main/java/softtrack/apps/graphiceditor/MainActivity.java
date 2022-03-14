package softtrack.apps.graphiceditor;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;

import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorListener;
import com.skydoves.colorpickerview.listeners.ColorPickerViewListener;

public class MainActivity extends AppCompatActivity {

    public DrawerLayout activityMainDrawer;
    public HorizontalScrollView activityMainContainerToolbar;
    public LinearLayout activityMainContainerCanvas;
    public LinearLayout activityMainContainerToolbarBodyPen;
    public LinearLayout activityMainContainerToolbarBodyEraser;
    public LinearLayout activityMainContainerToolbarBodyCurve;
    public LinearLayout activityMainContainerToolbarBodyShape;
    public LinearLayout activityMainContainerToolbarBodyFill;
    public LinearLayout activityMainContainerToolbarBodyGradient;
    public LinearLayout activityMainContainerToolbarBodyText;
    public SofttrackCanvas canvas;
    public LinearLayout activityMainContainerFooterBurger;
    public LinearLayout activityMainContainerFooterEdit;
    public LinearLayout activityMainContainerFooterSelection;
    public LinearLayout activityMainContainerFooterToggleOrientation;
    public LinearLayout activityMainContainerFooterTool;
    public ImageView activityMainContainerFooterToolBtn;
    public LinearLayout activityMainContainerFooterPalete;
    public LinearLayout activityMainContainerFooterLayers;
    public LinearLayout activityMainContainerFooterMaterials;
    public ColorPickerView activityMainPaleteColor;
    public int visible;
    public int unvisible;
    public int fillColor;
    public static MainActivity gateway;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

    }

    public void initialize() {
        initializeConstants();
        findViews();
        initializeCanvas();
        initializeActionBar();
        addHandlers();
    }

    public void findViews() {
        activityMainDrawer = findViewById(R.id.activity_main_drawer);
        activityMainContainerToolbar = findViewById(R.id.activity_main_container_toolbar);
        activityMainContainerToolbarBodyPen = findViewById(R.id.activity_main_container_toolbar_body_pen);
        activityMainContainerToolbarBodyEraser = findViewById(R.id.activity_main_container_toolbar_body_eraser);
        activityMainContainerToolbarBodyCurve = findViewById(R.id.activity_main_container_toolbar_body_curve);
        activityMainContainerToolbarBodyShape = findViewById(R.id.activity_main_container_toolbar_body_shape);
        activityMainContainerToolbarBodyFill = findViewById(R.id.activity_main_container_toolbar_body_fill);
        activityMainContainerToolbarBodyGradient = findViewById(R.id.activity_main_container_toolbar_body_gradient);
        activityMainContainerToolbarBodyText = findViewById(R.id.activity_main_container_toolbar_body_text);
        activityMainContainerCanvas = findViewById(R.id.activity_main_container_canvas);
        activityMainContainerFooterBurger = findViewById(R.id.activity_main_container_footer_burger);
        activityMainContainerFooterEdit = findViewById(R.id.activity_main_container_footer_edit);
        activityMainContainerFooterSelection = findViewById(R.id.activity_main_container_footer_selection);
        activityMainContainerFooterToggleOrientation = findViewById(R.id.activity_main_container_footer_toggle_orientation);
        activityMainContainerFooterTool = findViewById(R.id.activity_main_container_footer_tool);
        activityMainContainerFooterToolBtn = findViewById(R.id.activity_main_container_footer_tool_btn);
        activityMainContainerFooterPalete = findViewById(R.id.activity_main_container_footer_palete);
        activityMainContainerFooterLayers = findViewById(R.id.activity_main_container_footer_layers);
        activityMainContainerFooterMaterials = findViewById(R.id.activity_main_container_footer_materials);
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
                activityMainContainerFooterToolBtn.setImageResource(R.drawable.pen);
            }
        });
        activityMainContainerToolbarBodyEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.setContentDescription("eraser");
                activityMainContainerFooterToolBtn.setImageResource(R.drawable.eraser);
            }
        });
        activityMainContainerToolbarBodyCurve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.setContentDescription("curve");
                activityMainContainerFooterToolBtn.setImageResource(R.drawable.curve);
            }
        });
        activityMainContainerToolbarBodyShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.setContentDescription("shape");
                activityMainContainerFooterToolBtn.setImageResource(R.drawable.shape);
            }
        });
        activityMainContainerToolbarBodyFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.setContentDescription("fill");
                activityMainContainerFooterToolBtn.setImageResource(R.drawable.fill);
            }
        });
        activityMainContainerToolbarBodyGradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.setContentDescription("gradient");
                activityMainContainerFooterToolBtn.setImageResource(R.drawable.gradient);
            }
        });
        activityMainContainerToolbarBodyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.setContentDescription("text");
                activityMainContainerFooterToolBtn.setImageResource(R.drawable.text);
            }
        });
        activityMainContainerFooterBurger.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(Menu.NONE, 101, Menu.NONE, "Сохранить");
                contextMenu.add(Menu.NONE, 102, Menu.NONE, "Сохранить как");
                contextMenu.add(Menu.NONE, 103, Menu.NONE, "Экспорт PNG / JPG файлы");
                contextMenu.add(Menu.NONE, 104, Menu.NONE, "Настройки");
                contextMenu.add(Menu.NONE, 105, Menu.NONE, "Справка");
                contextMenu.add(Menu.NONE, 106, Menu.NONE, "Синхронизация");
                contextMenu.add(Menu.NONE, 107, Menu.NONE, "аннотирование");
                contextMenu.add(Menu.NONE, 108, Menu.NONE, "Start using the sonar pen/");
                contextMenu.add(Menu.NONE, 109, Menu.NONE, "Калибровка гидролокатора пера");
                contextMenu.add(Menu.NONE, 110, Menu.NONE, "Войти");
                contextMenu.add(Menu.NONE, 111, Menu.NONE, "Выход");

            }
        });
        activityMainContainerFooterEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupWindow popupWindow = new PopupWindow(MainActivity.this);
                View layout = getLayoutInflater().inflate(R.layout.edit_menu, null);
                popupWindow.setContentView(layout);
                popupWindow.setWidth(500);
                popupWindow.setHeight(500);
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAsDropDown(view);
            }
        });
        activityMainContainerFooterSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupWindow popupWindow = new PopupWindow(MainActivity.this);
                View layout = getLayoutInflater().inflate(R.layout.selection_menu, null);
                popupWindow.setContentView(layout);
                popupWindow.setWidth(500);
                popupWindow.setHeight(500);
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAsDropDown(view);
            }
        });
        activityMainContainerFooterToggleOrientation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupWindow popupWindow = new PopupWindow(MainActivity.this);
                View layout = getLayoutInflater().inflate(R.layout.toggle_orientation_menu, null);
                popupWindow.setContentView(layout);
                popupWindow.setWidth(500);
                popupWindow.setHeight(500);
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAsDropDown(view);
            }
        });
        activityMainContainerFooterTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int activityMainContainerToolbarVisibility = activityMainContainerToolbar.getVisibility();
                boolean isToolbarVisible = activityMainContainerToolbarVisibility == visible;
                if (isToolbarVisible) {
                    activityMainContainerToolbar.setVisibility(unvisible);
                } else {
                    activityMainContainerToolbar.setVisibility(visible);
                }
            }
        });
        activityMainContainerFooterPalete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityMainDrawer.closeDrawers();
                activityMainDrawer.openDrawer(Gravity.LEFT);
                activityMainDrawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
//                        if (drawerView.getId() == R.id.activity_main_palete) {
                            MainActivity.gateway.fillColor = activityMainPaleteColor.getColor();
//                        }
                    }
                });
                activityMainPaleteColor = findViewById(R.id.activity_main_palete_color);
                activityMainPaleteColor.setColorListener(new ColorListener() {
                    @Override
                    public void onColorSelected(int color, boolean fromUser) {
                        MainActivity.gateway.fillColor = color;
                        activityMainPaleteColor.setContentDescription(String.valueOf(color));
                    }
                });
            }
        });
        activityMainContainerFooterLayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityMainDrawer.closeDrawers();
                activityMainDrawer.openDrawer(Gravity.RIGHT);
            }
        });
    }

    public void initializeConstants() {
        gateway = MainActivity.this;
        visible = View.VISIBLE;
        unvisible = View.GONE;
        fillColor = Color.BLACK;
    }

}
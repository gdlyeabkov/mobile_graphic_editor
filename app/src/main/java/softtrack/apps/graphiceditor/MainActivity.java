package softtrack.apps.graphiceditor;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorListener;
import com.skydoves.colorpickerview.listeners.ColorPickerViewListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public DrawerLayout activityMainDrawer;
    public HorizontalScrollView activityMainContainerToolbar;
    public ViewPager2 activityMainContainerToolbarMenu;
    public LinearLayout activityMainContainerCanvas;
    public LinearLayout activityMainContainerToolbarBodyPen;
    public LinearLayout activityMainContainerToolbarBodyEraser;
    public LinearLayout activityMainContainerToolbarBodyCurve;
    public LinearLayout activityMainContainerToolbarBodyShape;
    public LinearLayout activityMainContainerToolbarBodyFill;
    public LinearLayout activityMainContainerToolbarBodyGradient;
    public LinearLayout activityMainContainerToolbarBodyText;
    public SofttrackCanvas canvas;
    public LinearLayout activityMainContainerPreFooterUndo;
    public LinearLayout activityMainContainerPreFooterRedo;
    public LinearLayout activityMainContainerPreFooterPipet;
    public LinearLayout activityMainContainerPreFooterPen;
    public LinearLayout activityMainContainerPreFooterEraser;
    public LinearLayout activityMainContainerPreFooterZoom;
    public Button activityMainContainerPreFooterSaveBtn;
    public LinearLayout activityMainContainerPreFooterDragger;
    public LinearLayout activityMainContainerFooterBurger;
    public LinearLayout activityMainContainerFooterEdit;
    public LinearLayout activityMainContainerFooterSelection;
    public LinearLayout activityMainContainerFooterToggleOrientation;
    public LinearLayout activityMainContainerFooterTool;
    public ImageView activityMainContainerFooterToolBtn;
    public LinearLayout activityMainContainerFooterPalete;
    public LinearLayout activityMainContainerFooterLayers;
    public LinearLayout activityMainContainerFooterMaterials;
    public LinearLayout activityMainLayersContainerLayerListBody;
    public LinearLayout activityMainLayersContainerListBodyInitialLayer;
//    public LinearLayout activityMainLayersContainerListBodyInitialLayerVisibility;
    public ImageView activityMainLayersContainerListBodyInitialLayerVisibilityIcon;
    public LinearLayout activityMainLayersContainerLayerActionsAdd;
    public ColorPickerView activityMainPaleteColor;
    public int visible;
    public int unvisible;
    public int fillColor;
    public String activeToolbarMenuItem = "line";
    public int roundRadius = 0;
    public int canvasWidth;
    public int canvasHeight;
    public int canvasDpi;
    public String canvasPaperSize;
    public int canvasBackgroundColor;
    public ArrayList<HashMap<String, Object>> layers;
    public int currentLayer;
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
        getTransferData();
        initializeToolbarMenu();
        initializeCanvas();
        initializeActionBar();
        addHandlers();
    }

    public void findViews() {
        activityMainDrawer = findViewById(R.id.activity_main_drawer);
        activityMainContainerToolbar = findViewById(R.id.activity_main_container_toolbar);
        activityMainContainerToolbarMenu = findViewById(R.id.activity_main_container_toolbar_menu);
        activityMainContainerToolbarBodyPen = findViewById(R.id.activity_main_container_toolbar_body_pen);
        activityMainContainerToolbarBodyEraser = findViewById(R.id.activity_main_container_toolbar_body_eraser);
        activityMainContainerToolbarBodyCurve = findViewById(R.id.activity_main_container_toolbar_body_curve);
        activityMainContainerToolbarBodyShape = findViewById(R.id.activity_main_container_toolbar_body_shape);
        activityMainContainerToolbarBodyFill = findViewById(R.id.activity_main_container_toolbar_body_fill);
        activityMainContainerToolbarBodyGradient = findViewById(R.id.activity_main_container_toolbar_body_gradient);
        activityMainContainerToolbarBodyText = findViewById(R.id.activity_main_container_toolbar_body_text);
        activityMainContainerCanvas = findViewById(R.id.activity_main_container_canvas);
        activityMainContainerPreFooterRedo = findViewById(R.id.activity_main_container_pre_footer_drager);
        activityMainContainerPreFooterUndo = findViewById(R.id.activity_main_container_pre_footer_drager);
        activityMainContainerPreFooterPipet = findViewById(R.id.activity_main_container_pre_footer_pipet);
        activityMainContainerPreFooterPen = findViewById(R.id.activity_main_container_pre_footer_pen);
        activityMainContainerPreFooterEraser = findViewById(R.id.activity_main_container_pre_footer_eraser);
        activityMainContainerPreFooterZoom = findViewById(R.id.activity_main_container_pre_footer_zoom);
        activityMainContainerPreFooterSaveBtn = findViewById(R.id.activity_main_container_pre_footer_save_btn);
        activityMainContainerPreFooterDragger = findViewById(R.id.activity_main_container_pre_footer_drager);
        activityMainContainerFooterBurger = findViewById(R.id.activity_main_container_footer_burger);
        activityMainContainerFooterEdit = findViewById(R.id.activity_main_container_footer_edit);
        activityMainContainerFooterSelection = findViewById(R.id.activity_main_container_footer_selection);
        activityMainContainerFooterToggleOrientation = findViewById(R.id.activity_main_container_footer_toggle_orientation);
        activityMainContainerFooterTool = findViewById(R.id.activity_main_container_footer_tool);
        activityMainContainerFooterToolBtn = findViewById(R.id.activity_main_container_footer_tool_btn);
        activityMainContainerFooterPalete = findViewById(R.id.activity_main_container_footer_palete);
        activityMainContainerFooterLayers = findViewById(R.id.activity_main_container_footer_layers);
        activityMainContainerFooterMaterials = findViewById(R.id.activity_main_container_footer_materials);
        activityMainLayersContainerLayerListBody = findViewById(R.id.activity_main_layers_container_list_body);
        activityMainLayersContainerListBodyInitialLayer = findViewById(R.id.activity_main_layers_container_list_body_initial_layer);
//        activityMainLayersContainerListBodyInitialLayerVisibility = findViewById(R.id.activity_main_layers_container_list_body_initial_layer_visibility);
        activityMainLayersContainerListBodyInitialLayerVisibilityIcon = findViewById(R.id.activity_main_layers_container_list_body_initial_layer_visibility_icon);
        activityMainLayersContainerLayerActionsAdd = findViewById(R.id.activity_main_layers_container_layer_actions_add);
    }

    public void initializeToolbarMenu() {
        FragmentManager fm = getSupportFragmentManager();
        ViewStateAdapter sa = new ViewStateAdapter(fm, getLifecycle());
        activityMainContainerToolbarMenu.setAdapter(sa);
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
                activityMainContainerToolbarMenu.setCurrentItem(0);
            }
        });
        activityMainContainerToolbarBodyEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.setContentDescription("eraser");
                activityMainContainerFooterToolBtn.setImageResource(R.drawable.eraser);
                activityMainContainerToolbarMenu.setCurrentItem(0);
            }
        });
        activityMainContainerToolbarBodyCurve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.setContentDescription("curve");
                activityMainContainerFooterToolBtn.setImageResource(R.drawable.curve);
                activityMainContainerToolbarMenu.setCurrentItem(1);
                MainActivity.gateway.activeToolbarMenuItem = "line";
            }
        });
        activityMainContainerToolbarBodyShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.setContentDescription("shape");
                activityMainContainerFooterToolBtn.setImageResource(R.drawable.shape);
                activityMainContainerToolbarMenu.setCurrentItem(2);
                MainActivity.gateway.activeToolbarMenuItem = "rect";
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
        activityMainContainerPreFooterPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.setContentDescription("pen");
                activityMainContainerFooterToolBtn.setImageResource(R.drawable.pen);
                activityMainContainerToolbarMenu.setCurrentItem(0);
            }
        });
        activityMainContainerPreFooterEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.setContentDescription("eraser");
                activityMainContainerFooterToolBtn.setImageResource(R.drawable.pen);
                activityMainContainerToolbarMenu.setCurrentItem(0);
            }
        });
        activityMainContainerPreFooterSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImg(canvas);
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
        LinearLayout activityMainLayersContainerListBodyInitialLayer = findViewById(R.id.activity_main_layers_container_list_body_initial_layer);
        activityMainLayersContainerListBodyInitialLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectLayer(view);
            }
        });
//        activityMainLayersContainerListBodyInitialLayerVisibility.setOnClickListener(new View.OnClickListener() {
        activityMainLayersContainerListBodyInitialLayerVisibilityIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleLayerVisibility(activityMainLayersContainerListBodyInitialLayer);
            }
        });
        activityMainLayersContainerLayerActionsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> layersItem = new HashMap<String, Object>();
                layersItem.put("visibility", true);
                layers.add(layersItem);
                LinearLayout layer = new LinearLayout(MainActivity.this);
                layer.setOrientation(LinearLayout.HORIZONTAL);
                ImageView layerVisibilityIcon = new ImageView(MainActivity.this);
                layerVisibilityIcon.setImageResource(R.drawable.visibility);
                LinearLayout.LayoutParams layerVisibilityIconLayoutParams = new LinearLayout.LayoutParams(65, 65);
                layerVisibilityIconLayoutParams.setMargins(0, 10, 0, 0);
                layerVisibilityIcon.setLayoutParams(layerVisibilityIconLayoutParams);
                ImageView layerThumbnailIcon = new ImageView(MainActivity.this);
                layerThumbnailIcon.setImageResource(R.drawable.shape);
                LinearLayout.LayoutParams layerThumbnailIconLayoutParams = new LinearLayout.LayoutParams(65, 65);
                layerThumbnailIconLayoutParams.setMargins(0, 10, 0, 0);
                layerThumbnailIcon.setLayoutParams(layerThumbnailIconLayoutParams);
                TextView layerNameLabel = new TextView(MainActivity.this);
                int layerNumber = layers.size();
                String rawLayerNumber = String.valueOf(layerNumber);
                layerNameLabel.setText("Слой " + rawLayerNumber);
                LinearLayout.LayoutParams layerNameLabelLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layerNameLabelLayoutParams.setMargins(25, 10, 25, 0);
                layerNameLabel.setLayoutParams(layerNameLabelLayoutParams);
                ImageView layerSettingsIcon = new ImageView(MainActivity.this);
                layerSettingsIcon.setImageResource(R.drawable.settings);
                LinearLayout.LayoutParams layerSettingsIconLayoutParams = new LinearLayout.LayoutParams(65, 65);
                layerSettingsIconLayoutParams.setMargins(25, 10, 25, 0);
                layerSettingsIcon.setLayoutParams(layerSettingsIconLayoutParams);
                layer.addView(layerVisibilityIcon);
                layer.addView(layerThumbnailIcon);
                layer.addView(layerNameLabel);
                layer.addView(layerSettingsIcon);
                activityMainLayersContainerLayerListBody.addView(layer);
                layer.setContentDescription(rawLayerNumber);
                layer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectLayer(view);
                    }
                });
                layerVisibilityIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LinearLayout layer = (LinearLayout) view.getParent();
                        toggleLayerVisibility(layer);
                    }
                });
                selectLayer(layer);
            }
        });
    }

    public void initializeConstants() {
        gateway = MainActivity.this;
        visible = View.VISIBLE;
        unvisible = View.GONE;
        fillColor = Color.BLACK;
        layers = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> initialLayer = new HashMap<String, Object>();
        initialLayer.put("visibility", true);
        layers.add(initialLayer);
        currentLayer = 0;
    }

    public void saveImg(View view) {
        canvas.setDrawingCacheEnabled(true);
        Bitmap bitmap = canvas.getDrawingCache();
        String rawDownloadsDir = Environment.DIRECTORY_DOWNLOADS;
        File downloadsDir = Environment.getExternalStoragePublicDirectory(rawDownloadsDir);
        String downloadsDirPath = downloadsDir.getPath();
        String filename = downloadsDirPath + "/graphic_editor_export.png";
        try (FileOutputStream out = new FileOutputStream(filename)) {
            int canvasDpiInPercents = canvasDpi / 4;
            String toastMessage = "";
            boolean isDpiCorrect = canvasDpiInPercents <= 100;
            if (isDpiCorrect) {
                bitmap.compress(Bitmap.CompressFormat.PNG, canvasDpiInPercents, out);
                toastMessage = "Сохранено.";
            } else {
                toastMessage = "Неправильно указан DPI.";
            }
            Toast toast = Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT);
            toast.show();
        } catch (IOException e) {
            Log.d("debug", "ошибка экспорта");
        }
    }

    public void getTransferData() {
        Intent myIntent = getIntent();
        Bundle extras = myIntent.getExtras();
        canvasWidth = extras.getInt("width");
        canvasHeight = extras.getInt("height");
        canvasDpi = extras.getInt("dpi");
        canvasPaperSize = extras.getString("paperSize");
        canvasBackgroundColor = extras.getInt("backgroundColor");
    }

    public void selectLayer(View view) {
        for (int i = 0; i < activityMainLayersContainerLayerListBody.getChildCount(); i++) {
            activityMainLayersContainerLayerListBody.getChildAt(i).setBackgroundColor(Color.rgb(255, 255, 255));
        }
        CharSequence rawLayerNumber = view.getContentDescription();
        String layerNumber = rawLayerNumber.toString();
        currentLayer = Integer.valueOf(layerNumber) - 1;
        view.setBackgroundColor(Color.rgb(235, 235, 235));
    }

    public void toggleLayerVisibility(View view) {
        CharSequence rawLayerNumber = view.getContentDescription();
        String layerNumber = rawLayerNumber.toString();
        int layerIndex = Integer.valueOf(layerNumber) - 1;
        HashMap<String, Object> layer = layers.get(layerIndex);
        boolean layerVisibility = (boolean) layer.get("visibility");
        LinearLayout someLayer = (LinearLayout) view;
//        LinearLayout someLayerVisibility = (LinearLayout) someLayer.getChildAt(0);
//        ImageView someLayerVisibilityIcon = (ImageView) someLayerVisibility.getChildAt(0);
        ImageView someLayerVisibilityIcon = (ImageView) someLayer.getChildAt(0);
        if (layerVisibility) {
            someLayerVisibilityIcon.setColorFilter(Color.rgb(200, 200, 200));
        } else {
            someLayerVisibilityIcon.setColorFilter(Color.rgb(0, 0, 0));
        }
        layer.put("visibility", !layerVisibility);
        canvas.invalidate();
    }

}
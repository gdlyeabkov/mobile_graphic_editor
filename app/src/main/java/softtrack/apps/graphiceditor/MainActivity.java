package softtrack.apps.graphiceditor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
    public ImageView activityMainLayersContainerListBodyInitialLayerVisibilityIcon;
    public TextView activityMainLayersContainerListBodyInitialLayerNameLabel;
    public ImageView activityMainLayersContainerListBodyInitialLayerSettingsIcon;
    public LinearLayout activityMainLayersContainerLayerActionsAdd;
    public LinearLayout activityMainLayersContainerLayerActionsRemove;
    public LinearLayout activityMainLayersContainerLayerActionsMore;
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
    public boolean isDetectChanges = false;
    public static MainActivity gateway;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

    }

    @Override
    public void onBackPressed() {
        if (isDetectChanges) {
           showExitDialog();
        } else {
            int activityMainContainerToolbarVisibility = activityMainContainerToolbar.getVisibility();
            boolean isToolbarVisible = activityMainContainerToolbarVisibility == visible;
            if (isToolbarVisible) {
                activityMainContainerToolbar.setVisibility(unvisible);
            } else {
                super.onBackPressed();
            }
        }
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
        activityMainLayersContainerListBodyInitialLayerNameLabel = findViewById(R.id.activity_main_layers_container_list_body_initial_layer_name_label);
        activityMainLayersContainerListBodyInitialLayerSettingsIcon = findViewById(R.id.activity_main_layers_container_list_body_initial_layer_settings_icon);
        activityMainLayersContainerLayerActionsAdd = findViewById(R.id.activity_main_layers_container_layer_actions_add);
        activityMainLayersContainerLayerActionsRemove = findViewById(R.id.activity_main_layers_container_layer_actions_remove);
        activityMainLayersContainerLayerActionsMore = findViewById(R.id.activity_main_layers_container_layer_actions_more);
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
        activityMainContainerFooterBurger.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
//                activityMainContainerFooterBurger.showContextMenu();
                activityMainContainerFooterBurger.showContextMenu(0, 0);
            }
        });
        activityMainContainerFooterBurger.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                MenuItem contextMenuItem = contextMenu.add(Menu.NONE, 101, Menu.NONE, "Сохранить");
                contextMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        saveImg(canvas, "png");
                        return false;
                    }
                });
                contextMenuItem = contextMenu.add(Menu.NONE, 102, Menu.NONE, "Сохранить как");
                contextMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        LayoutInflater inflater = getLayoutInflater();
                        builder.setMessage("");
                        builder.setCancelable(false);
                        builder.setNegativeButton("ОТМЕНА", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.setPositiveButton("Сохранить локально", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                saveImg(canvas, "png");
                            }
                        });
                        builder.setNeutralButton("Сохранить онлайн", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String toastMessage = "Требуется вход";
                                Toast toast = Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.setTitle("Пункт назначения");
                        alert.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialogInterface) {

                            }
                        });
                        alert.show();
                        return false;
                    }
                });
                contextMenuItem = contextMenu.add(Menu.NONE, 103, Menu.NONE, "Экспорт PNG / JPG файлы");
                contextMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.file_format_dialog, null);
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
                                RadioGroup fileFormatDialogContainer = dialogView.findViewById(R.id.file_format_dialog_container);
                                int selectedFormatId = fileFormatDialogContainer.getCheckedRadioButtonId();
                                String selectedFormat = "png";
                                if (selectedFormatId == R.id.file_format_dialog_container_png) {
                                    selectedFormat = "png";
                                } else if (selectedFormatId == R.id.file_format_dialog_container_png_transparency) {
                                    selectedFormat = "png";
                                } else if (selectedFormatId == R.id.file_format_dialog_container_jpg) {
                                    selectedFormat = "jpg";
                                }
                                saveImg(canvas, selectedFormat);
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.setTitle("Формат файла");
                        alert.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialogInterface) {

                            }
                        });
                        alert.show();
                        return false;
                    }
                });
                contextMenuItem = contextMenu.add(Menu.NONE, 104, Menu.NONE, "Настройки");
                contextMenuItem = contextMenu.add(Menu.NONE, 105, Menu.NONE, "Справка");
                contextMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String url = "https://medibangpaint.com/ru/android/use/";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        return false;
                    }
                });
                contextMenuItem = contextMenu.add(Menu.NONE, 106, Menu.NONE, "Синхронизация");
                contextMenuItem = contextMenu.add(Menu.NONE, 107, Menu.NONE, "аннотирование");
                contextMenuItem = contextMenu.add(Menu.NONE, 108, Menu.NONE, "Start using the sonar pen/");
                contextMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        return false;
                    }
                });
                contextMenuItem = contextMenu.add(Menu.NONE, 109, Menu.NONE, "Калибровка гидролокатора пера");
                contextMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String url = "market://details?id=com.greenbulb.calibrate";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        return false;
                    }
                });
                contextMenuItem = contextMenu.add(Menu.NONE, 110, Menu.NONE, "Войти");
                contextMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        return false;
                    }
                });
                contextMenuItem = contextMenu.add(Menu.NONE, 111, Menu.NONE, "Выход");
                contextMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (isDetectChanges) {
                            showExitDialog();
                        } else {
                            Intent intent = new Intent(MainActivity.this, StartActivity.class);
                            MainActivity.this.startActivity(intent);
                        }
                        return false;
                    }
                });
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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                saveImg(canvas, "png");
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
                LinearLayout selectionMenuContainerSelectArea = layout.findViewById(R.id.selection_menu_container_select_area);
                selectionMenuContainerSelectArea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        canvas.setClipBounds(new Rect(150, 150, 150, 150));
//                        canvas.setClipBounds(new Rect(-150, -150, -150, -150));
                        canvas.addSelectArea();
                        popupWindow.dismiss();
                    }
                });
                LinearLayout selectionMenuContainerFreeTransform = layout.findViewById(R.id.selection_menu_container_free_transform);
                selectionMenuContainerFreeTransform.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.Q)
                    @Override
                    public void onClick(View view) {
                        Matrix matrix = new Matrix();
                        matrix.setPolyToPoly(new float[] {
                            0, 0,
                            canvas.getWidth(), 0,
                            0, canvas.getHeight(),
                            canvas.getWidth(), canvas.getHeight()
                        }, 0,
                        new float[] {
                            0.0f, 0.0f,
                            1.0f, 0.0f,
                            0.0f, 1.0f,
                            1.0f, 1.0f
                        }, 0,
                        4);
                        canvas.deform(matrix);
//                        matrix.setValues(new float[]{
//                            0.0f,
//                            0.0f,
//                            0.5f,
//                            0.0f,
//                            1.0f,
//                            0.0f,
//                            0.0f,
//                            0.5f,
//                            1.0f,
//                            0.5f,
//                            0.0f,
//                            1.0f,
//                            0.5f,
//                            1.0f,
//                            1.0f,
//                            1.0f
//                        });
//                        matrix.setSkew(5.0f, 5.0f);
//                        matrix.setRotate(25f);
//                        canvas.transformMatrixToGlobal(matrix);
//                        canvas.transformMatrixToLocal(matrix);
//                        canvas.invalidate();
                        popupWindow.dismiss();
                    }
                });
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
                LinearLayout toggleOrientationMenuContainerRotateLeft = layout.findViewById(R.id.toggle_orientation_menu_container_rotate_left);
                LinearLayout toggleOrientationMenuContainerRotateRight = layout.findViewById(R.id.toggle_orientation_menu_container_rotate_right);
                LinearLayout toggleOrientationMenuContainerFlipHorizontal = layout.findViewById(R.id.toggle_orientation_menu_container_flip_horizontal);
                LinearLayout toggleOrientationMenuContainerReset = layout.findViewById(R.id.toggle_orientation_menu_container_reset);
                toggleOrientationMenuContainerRotateLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        float degress = canvas.getRotation();
                        canvas.setRotation(degress - 90f);
                        popupWindow.dismiss();
                    }
                });
                toggleOrientationMenuContainerRotateRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        float degress = canvas.getRotation();
                        canvas.setRotation(degress + 90f);
                        popupWindow.dismiss();
                    }
                });
                toggleOrientationMenuContainerFlipHorizontal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        float scale = canvas.getScaleX();
                        float reverseScale = scale * - 1.0f;
                        canvas.setScaleX(reverseScale);
                        popupWindow.dismiss();
                    }
                });
                toggleOrientationMenuContainerReset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        canvas.setRotation(0.0f);
                        canvas.setScaleX(1.0f);
                        popupWindow.dismiss();
                    }
                });
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
        activityMainLayersContainerListBodyInitialLayerSettingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.edit_layer_name_dialog, null);
                builder.setView(dialogView);
                builder.setCancelable(false);
                builder.setNegativeButton("ОТМЕНА", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("ЗАДАТЬ", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText editLayerNameDialogContainerField = null;
                        editLayerNameDialogContainerField = dialogView.findViewById(R.id.edit_layer_name_dialog_container_field);
                        CharSequence rawEditLayerNameDialogContainerFieldContent = editLayerNameDialogContainerField.getText();
                        String editLayerNameDialogContainerFieldContent = rawEditLayerNameDialogContainerFieldContent.toString();

                        LinearLayout layerContianer = (LinearLayout) view.getParent();
                        CharSequence rawLayerNumber = layerContianer.getContentDescription();
                        String layerNumber = rawLayerNumber.toString();
                        int parsedLayerNumber = Integer.valueOf(layerNumber);
                        HashMap<String, Object> layer = layers.get(parsedLayerNumber - 1);
                        layer.put("name", editLayerNameDialogContainerFieldContent);
                        refreshLayers();
                    }
                });
                AlertDialog alert = builder.create();
                alert.setTitle("");
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        EditText editLayerNameDialogContainerField = null;
                        editLayerNameDialogContainerField = dialogView.findViewById(R.id.edit_layer_name_dialog_container_field);
                        LinearLayout layerContianer = (LinearLayout) view.getParent();
                        CharSequence rawLayerNumber = layerContianer.getContentDescription();
                        String layerNumber = rawLayerNumber.toString();
                        int parsedLayerNumber = Integer.valueOf(layerNumber);
                        HashMap<String, Object> layer = layers.get(0);
                        String layerName = (String) layer.get("name");
                        editLayerNameDialogContainerField.setText(layerName);
                    }
                });
                alert.show();
            }
        });
        activityMainLayersContainerLayerActionsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> layersItem = new HashMap<String, Object>();
                String layerName = "Слой " + String.valueOf(layers.size() + 1);
                layersItem.put("name", layerName);
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
                layerNameLabel.setText(layerName);
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
                layerSettingsIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.edit_layer_name_dialog, null);
                        builder.setView(dialogView);
                        builder.setCancelable(false);
                        builder.setNegativeButton("ОТМЕНА", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.setPositiveButton("ЗАДАТЬ", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditText editLayerNameDialogContainerField = null;
                                editLayerNameDialogContainerField = dialogView.findViewById(R.id.edit_layer_name_dialog_container_field);
                                CharSequence rawEditLayerNameDialogContainerFieldContent = editLayerNameDialogContainerField.getText();
                                String editLayerNameDialogContainerFieldContent = rawEditLayerNameDialogContainerFieldContent.toString();

                                LinearLayout layerContianer = (LinearLayout) view.getParent();
                                CharSequence rawLayerNumber = layerContianer.getContentDescription();
                                String layerNumber = rawLayerNumber.toString();
                                int parsedLayerNumber = Integer.valueOf(layerNumber);
                                HashMap<String, Object> layer = layers.get(parsedLayerNumber - 1);
                                layer.put("name", editLayerNameDialogContainerFieldContent);
                                refreshLayers();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.setTitle("");
                        alert.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialogInterface) {
                                EditText editLayerNameDialogContainerField = null;
                                editLayerNameDialogContainerField = dialogView.findViewById(R.id.edit_layer_name_dialog_container_field);
                                LinearLayout layerContianer = (LinearLayout) view.getParent();
                                CharSequence rawLayerNumber = layerContianer.getContentDescription();
                                String layerNumber = rawLayerNumber.toString();
                                int parsedLayerNumber = Integer.valueOf(layerNumber);
                                HashMap<String, Object> layer = layers.get(parsedLayerNumber - 1);
                                String layerName = (String) layer.get("name");
                                editLayerNameDialogContainerField.setText(layerName);
                            }
                        });
                        alert.show();
                    }
                });
                selectLayer(layer);
                ImageView activityMainLayersContainerLayerActionsRemoveBtn = (ImageView) activityMainLayersContainerLayerActionsRemove.getChildAt(0);
                activityMainLayersContainerLayerActionsRemoveBtn.setColorFilter(Color.rgb(0, 0, 0));
            }
        });
        activityMainLayersContainerLayerActionsRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layers.size() >= 2) {
                    layers.remove(currentLayer);
                    activityMainLayersContainerLayerListBody.removeViewAt(currentLayer);
                    currentLayer = 0;
                    activityMainLayersContainerLayerListBody.getChildAt(0).setBackgroundColor(Color.rgb(235, 235, 235));
                    boolean isOneLayerLeft = layers.size() <= 1;
                    if (isOneLayerLeft) {
                        ImageView activityMainLayersContainerLayerActionsRemoveBtn = (ImageView) activityMainLayersContainerLayerActionsRemove.getChildAt(0);
                        activityMainLayersContainerLayerActionsRemoveBtn.setColorFilter(Color.rgb(235, 235, 235));
                    }
                    canvas.invalidate();
                }
            }
        });
        activityMainLayersContainerLayerActionsMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupWindow popupWindow = new PopupWindow(MainActivity.this);
                View layout = getLayoutInflater().inflate(R.layout.layers_more_menu, null);
                popupWindow.setContentView(layout);
                popupWindow.setWidth(450);
                popupWindow.setHeight(850);
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAsDropDown(view);
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
        initialLayer.put("name", "Слой 1");
        initialLayer.put("visibility", true);
        layers.add(initialLayer);
        currentLayer = 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void saveImg(View view, String format) {
        canvas.setDrawingCacheEnabled(true);
        Bitmap bitmap = canvas.getDrawingCache();

        Calendar calendar = Calendar.getInstance();
        long rawFileName = calendar.getTimeInMillis();
        String fileName = String.valueOf(rawFileName);
        File appDir = getDataDir();
        String appDirPath = appDir.getPath();
        String filename = appDirPath + "/" + fileName + "." + format;

        try (FileOutputStream out = new FileOutputStream(filename)) {
            int canvasDpiInPercents = canvasDpi / 4;
            String toastMessage = "";
            boolean isDpiCorrect = canvasDpiInPercents <= 100;
            if (isDpiCorrect) {
                bitmap.compress(Bitmap.CompressFormat.PNG, canvasDpiInPercents, out);
                toastMessage = "Сохранение выполнено.";
            } else {
                toastMessage = "Неправильно указан DPI.";
            }
            Toast toast = Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT);
            toast.show();
            isDetectChanges = false;
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
        ImageView someLayerVisibilityIcon = (ImageView) someLayer.getChildAt(0);
        if (layerVisibility) {
            someLayerVisibilityIcon.setColorFilter(Color.rgb(200, 200, 200));
        } else {
            someLayerVisibilityIcon.setColorFilter(Color.rgb(0, 0, 0));
        }
        layer.put("visibility", !layerVisibility);
        canvas.invalidate();
    }

    public void refreshLayers() {
        for (int i = 0; i < activityMainLayersContainerLayerListBody.getChildCount(); i++) {
            String layerName = (String) layers.get(i).get("name");
            LinearLayout layer = (LinearLayout) activityMainLayersContainerLayerListBody.getChildAt(i);
            TextView layerNameLabel = (TextView) layer.getChildAt(2);
            layerNameLabel.setText(layerName);
        }
    }

    public void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_layer_name_dialog, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveImg(canvas, "png");
                Intent intent = new Intent(MainActivity.this, StartActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        builder.setNeutralButton("Закрыть без сохранения", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(MainActivity.this, StartActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.setTitle("Вы закончили?");
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

            }
        });
        alert.show();
    }

}
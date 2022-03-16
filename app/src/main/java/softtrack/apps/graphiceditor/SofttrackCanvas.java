package softtrack.apps.graphiceditor;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowMetrics;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.utils.widget.MockView;
import androidx.window.layout.WindowMetricsCalculator;
import androidx.window.testing.layout.WindowLayoutInfoPublisherRule;

import com.google.android.material.textfield.TextInputEditText;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;

public class SofttrackCanvas extends View {

    public Paint paint;
    public MainActivity context;
    public float touchX = 0f;
    public float touchY = 0f;
    public float initialTouchX = 0f;
    public float initialTouchY = 0f;
    public ArrayList<HashMap<String, Object>> lines;
    public ArrayList<HashMap<String, Object>> curves;
    public ArrayList<HashMap<String, Object>> shapes;
    public ArrayList<HashMap<String, Object>> fills;
    public ArrayList<HashMap<String, Object>> gradients;
    public ArrayList<HashMap<String, Object>> texts;
    public boolean isBold = false;
    public boolean isItalic = false;
    public int enabledBtnColor;
    public int disabledBtnColor;
    public int fillColor;
    public int fillTextColor;
    public int outlineTextColor;
    public float[] initialCurvePoints = new float[6];
    public int initialCurvePointsCursor = -1;
//    public GestureDetector gestureDetector;
    public ScaleGestureDetector gestureDetector;
    public Canvas myCanvas = null;

    public SofttrackCanvas(Context context) {
        super(context);

        initialize((MainActivity) context);
        gestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                Log.d("debug", "zoom канвас");
                myCanvas.scale(0.1f, 0.1f);
                return super.onScale(detector);
            }
        });
        if (Build.VERSION.SDK_INT >= 19) {
            gestureDetector.setQuickScaleEnabled(false);
        }
        /*gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (myCanvas != null) {
                    try {
                        if (Math.abs(e1.getY() - e2.getY()) > 150)
                            return false;
                        // right to left swipe
                        if (e1.getX() - e2.getX() > 0 && Math.abs(velocityX) > 100) {
                            myCanvas.rotate(45);
                            myCanvas.scale(0.1f, 0.1f);
                            myCanvas.translate(5.0f, 5.0f);
                        } else if (e2.getX() - e1.getX() > 0 && Math.abs(velocityX) > 100) {
                            myCanvas.rotate(135);
                            myCanvas.scale(0.1f, 0.1f);
                            myCanvas.translate(5.0f, 5.0f);
                        }
                    } catch (Exception e) {
                        // nothing
                    }
                }
                return false;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                myCanvas.rotate(45);
                return true;
            }
        });*/
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        canvas.clipOutRect(150.0f, 150.0f, 150.0f, 150.0f);
        canvas.drawColor(MainActivity.gateway.canvasBackgroundColor);
        myCanvas = canvas;
        // для вращения канваса
        //        canvas.rotate(15);

        boolean isLinesExists = lines != null;
        if (isLinesExists) {
            ColorPickerView activityMainPaleteColor = MainActivity.gateway.findViewById(R.id.activity_main_palete_color);
            int activeColor = activityMainPaleteColor.getColor();
//            paint.setColor(Color.BLUE);
            paint.setColor(activeColor);
            for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
                HashMap line = lines.get(lineIndex);
                float lineX1 = (float) line.get("x1");
                float lineY1 = (float) line.get("y1");
                float lineX2 = (float) line.get("x2");
                float lineY2 = (float) line.get("y2");
                int lineColor = (int) line.get("color");
                int lineStrokeWidth = (int) line.get("strokeWidth");
                int lineLayerIndex = (int) line.get("layer");
                if (MainActivity.gateway.layers.size() > lineLayerIndex) {
                    HashMap<String, Object> lineLayer = MainActivity.gateway.layers.get(lineLayerIndex);
                    boolean lineLayerVisibility = (boolean) lineLayer.get("visibility");
                    if (lineLayerVisibility) {
                        Paint linePaint = new Paint();
                        linePaint.setColor(lineColor);
                        linePaint.setStrokeWidth(lineStrokeWidth);
                        canvas.drawLine(lineX1, lineY1, lineX2, lineY2, linePaint);
                    }
                } else if (MainActivity.gateway.layers.size() == lineLayerIndex){
                    for (int someLineIndex = 0; someLineIndex < lines.size(); someLineIndex++) {
                        if (((int)(lines.get(someLineIndex).get("layer"))) == lineLayerIndex) {
                            lines.remove(someLineIndex);
                        }
                    }
//                    lines.remove(lineLayerIndex);
                }
            }
        }

        boolean isCurvesExists = curves != null;
        if (isCurvesExists) {
            paint.setColor(Color.BLUE);
            for (int curveIndex = 0; curveIndex < curves.size(); curveIndex++) {
                HashMap curve = curves.get(curveIndex);
                String curveType = (String) curve.get("type");

                int lineLayerIndex = (int) curve.get("layer");
                HashMap<String, Object> lineLayer = MainActivity.gateway.layers.get(lineLayerIndex);
                boolean lineLayerVisibility = (boolean) lineLayer.get("visibility");
                if (lineLayerVisibility) {
                    int curveColor = (int) curve.get("color");
                    Paint curvePaint = new Paint();
                    curvePaint.setStyle(Paint.Style.STROKE);
                    curvePaint.setColor(curveColor);
                    if (curveType == "line") {
                        float curveX1 = (float) curve.get("x1");
                        float curveY1 = (float) curve.get("y1");
                        float curveX2 = (float) curve.get("x2");
                        float curveY2 = (float) curve.get("y2");
                        canvas.drawLine(curveX1, curveY1, curveX2, curveY2, curvePaint);
                    } else if (curveType == "path") {

                        float curveX1 = (float) curve.get("x1");
                        float curveY1 = (float) curve.get("y1");
                        float curveX2 = (float) curve.get("x2");
                        float curveY2 = (float) curve.get("y2");
                        float curveX3 = (float) curve.get("x3");
                        float curveY3 = (float) curve.get("y3");
                        curvePaint.setStrokeWidth(10);
                        Path path = new Path();
                        path.moveTo(curveX1, curveX2);
                        path.cubicTo(curveX1, curveY1, curveX2, curveY2, curveX3, curveY3);
                        canvas.drawPath(path, curvePaint);
                    } else if (curveType == "rect") {
                        float curveX1 = (float) curve.get("x1");
                        float curveY1 = (float) curve.get("y1");
                        float curveX2 = (float) curve.get("x2");
                        float curveY2 = (float) curve.get("y2");
                        canvas.drawRect(curveX1, curveY1, curveX2, curveY2, curvePaint);
                    } else if (curveType == "oval") {
                        float curveX1 = (float) curve.get("x1");
                        float curveY1 = (float) curve.get("y1");
                        float curveX2 = (float) curve.get("x2");
                        float curveY2 = (float) curve.get("y2");
                        canvas.drawOval(curveX1, curveY1, curveX2, curveY2, curvePaint);
                    }
                }
            }
        }

        boolean isShapesExists = shapes != null;
        if (isShapesExists) {
            paint.setColor(Color.BLUE);
            for (int shapeIndex = 0; shapeIndex < shapes.size(); shapeIndex++) {
                HashMap shape = shapes.get(shapeIndex);
                String shapeType = (String) shape.get("type");
                int shapeColor = (int) shape.get("color");

                int lineLayerIndex = (int) shape.get("layer");
                HashMap<String, Object> lineLayer = MainActivity.gateway.layers.get(lineLayerIndex);
                boolean lineLayerVisibility = (boolean) lineLayer.get("visibility");
                if (lineLayerVisibility) {
                    Paint shapePaint = new Paint();
                    shapePaint.setStyle(Paint.Style.FILL);
                    shapePaint.setColor(shapeColor );
                    if (shapeType == "rect") {
                        float shapeX1 = (float) shape.get("x1");
                        float shapeY1 = (float) shape.get("y1");
                        float shapeX2 = (float) shape.get("x2");
                        float shapeY2 = (float) shape.get("y2");
                        int shapeRadius = (int) shape.get("radius");
                        canvas.drawRoundRect(shapeX1, shapeY1, shapeX2, shapeY2, shapeRadius, shapeRadius, shapePaint);
                    } else if (shapeType == "oval") {
                        float shapeX1 = (float) shape.get("x1");
                        float shapeY1 = (float) shape.get("y1");
                        float shapeX2 = (float) shape.get("x2");
                        float shapeY2 = (float) shape.get("y2");
                        canvas.drawOval(shapeX1, shapeY1, shapeX2, shapeY2, shapePaint);
                    }
                }
            }
        }

        boolean isFillsExists = fills != null;
        if (isFillsExists) {
            paint.setColor(Color.BLUE);
            for (int fillIndex = 0; fillIndex < fills.size(); fillIndex++) {
                HashMap fill = fills.get(fillIndex);
                int fillColor = (int) fill.get("color");
                Paint fillPaint = new Paint();
                fillPaint.setColor(fillColor);
                canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), fillPaint);
            }
        }

        boolean isGradientsExists = gradients != null;
        if (isGradientsExists) {
            paint.setColor(Color.BLUE);
            for (int gradientIndex = 0; gradientIndex < gradients.size(); gradientIndex++) {
                HashMap gradient = gradients.get(gradientIndex);
                int[] colors = new int[]{ Color.BLACK, Color.WHITE };
                float[] positions = new float[]{ 0.0f, 1.0f };
                Shader linearGradient = new LinearGradient(0, 0, getMeasuredWidth(), getMeasuredHeight(), colors, positions, Shader.TileMode.CLAMP);
                Paint gradientPaint = new Paint();
                gradientPaint.setShader(linearGradient);
                canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), gradientPaint);
            }
        }

        boolean isTextsExists = texts != null;
        if (isTextsExists) {
            paint.setColor(Color.BLUE);
            for (int textIndex = 0; textIndex < texts.size(); textIndex++) {
                HashMap text = texts.get(textIndex);
                float textX = (float) text.get("x1");
                float textY = (float) text.get("y1");
                String textContent = (String) text.get("content");
                int fontSize = (int) text.get("fontSize");
                boolean isTextBold = (boolean) text.get("isTextBold");
                boolean isTextItalic = (boolean) text.get("isTextItalic");
                boolean isTextSmooth = (boolean) text.get("isTextSmooth");
                int textOutlineWidth = (int) text.get("textOutlineWidth");
                int textFillColor = (int) text.get("fillTextColor");
                int textOutlineColor = (int) text.get("outlineTextColor");

                // boolean isFontFamily = (boolean) text.get("isFontFamily");

                Paint textPaint = new Paint();

                String fontFamily = (String) text.get("fontFamily");
                if (fontFamily.length() >= 1) {

                }

                textPaint.setColor(Color.BLACK);
                textPaint.setTextSize(fontSize);
                textPaint.setFakeBoldText(isTextBold);
                if (isTextItalic) {
                    textPaint.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                }
                textPaint.setAntiAlias(isTextSmooth);
                boolean isOutlineSet = textOutlineWidth >= 1;
                if (isOutlineSet) {
                    textPaint.setStyle(Paint.Style.STROKE);
                    textPaint.setStrokeWidth(textOutlineWidth);
                    boolean isTextOutlineSmooth = (boolean) text.get("isTextOutlineSmooth");
                    if (isTextOutlineSmooth) {
                        textPaint.setStrokeCap(Paint.Cap.ROUND);
                    }
                    textPaint.setColor(textOutlineColor);
                } else {
                    textPaint.setColor(textFillColor);
                }
                /*if (isFontFamily) {
                    Typeface.defaultFromStyle(Typeface.ITALIC);
                }*/
                canvas.drawText(textContent, textX, textY, textPaint);
            }
        }

        CharSequence rawActiveTool = "";
        CharSequence contentDescription = getContentDescription();
            rawActiveTool = contentDescription;
            String activeTool = rawActiveTool.toString();
            if (activeTool.equalsIgnoreCase("pen")) {
                paint.setColor(MainActivity.gateway.fillColor);
                canvas.drawLine(touchX, touchY, touchX + 10, touchY + 10, paint);
            } else if (activeTool.equalsIgnoreCase("eraser")) {
                paint.setColor(Color.WHITE);
                canvas.drawLine(touchX, touchY, touchX + 10, touchY + 10, paint);
            } else if (activeTool.equalsIgnoreCase("curve")) {
                paint.setStyle(Paint.Style.STROKE);
                ColorPickerView activityMainPaleteColor = MainActivity.gateway.findViewById(R.id.activity_main_palete_color);
                paint.setColor(activityMainPaleteColor.getColor());
                if (MainActivity.gateway.activeToolbarMenuItem == "line") {
                    canvas.drawLine(initialTouchX, initialTouchY, touchX, touchY, paint);
                } else if (MainActivity.gateway.activeToolbarMenuItem == "path") {
                    Path path = new Path();
                    path.moveTo(initialCurvePoints[0], initialCurvePoints[1]);
                    path.cubicTo(initialCurvePoints[0], initialCurvePoints[1], initialCurvePoints[2], initialCurvePoints[3], initialCurvePoints[4], initialCurvePoints[5]);
                    canvas.drawPath(path, paint);
                } else if (MainActivity.gateway.activeToolbarMenuItem == "rect") {
                    canvas.drawRect(initialTouchX, initialTouchY, touchX, touchY, paint);
                } else if (MainActivity.gateway.activeToolbarMenuItem == "oval") {
                    canvas.drawOval(initialTouchX, initialTouchY, touchX, touchY, paint);
                }
            } else if (activeTool.equalsIgnoreCase("shape")) {
                paint.setStyle(Paint.Style.FILL);
                ColorPickerView activityMainPaleteColor = MainActivity.gateway.findViewById(R.id.activity_main_palete_color);
                paint.setColor(activityMainPaleteColor.getColor());
                 if (MainActivity.gateway.activeToolbarMenuItem == "rect") {
//                    canvas.drawRect(initialTouchX, initialTouchY, touchX, touchY, paint);
                     canvas.drawRoundRect(initialTouchX, initialTouchY, touchX, touchY, MainActivity.gateway.roundRadius, MainActivity.gateway.roundRadius, paint);
                 } else if (MainActivity.gateway.activeToolbarMenuItem == "oval") {
                    canvas.drawOval(initialTouchX, initialTouchY, touchX, touchY, paint);
                }
            } else if (activeTool.equalsIgnoreCase("fill")) {
                paint.setColor(Color.WHITE);
                canvas.drawLine(touchX, touchY, touchX + 10, touchY + 10, paint);
            } else if (activeTool.equalsIgnoreCase("gradient")) {
                paint.setColor(Color.WHITE);
                canvas.drawLine(touchX, touchY, touchX + 10, touchY + 10, paint);
            } else if (activeTool.equalsIgnoreCase("text")) {

            }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
        super.onTouchEvent(event);
        return true;
    }

    public void initialize(MainActivity context) {
        initializeConstants();
        addHandlers();
//        getTransferData();
    }

    public void initializeConstants() {
        enabledBtnColor = Color.argb(255, 255, 255, 255);
        disabledBtnColor = Color.TRANSPARENT;
        fillColor = Color.BLACK;
        fillTextColor = Color.BLACK;
        outlineTextColor = Color.BLACK;
        setContentDescription("pen");
        lines = new ArrayList<HashMap<String, Object>>();
        curves = new ArrayList<HashMap<String, Object>>();
        shapes = new ArrayList<HashMap<String, Object>>();
        fills = new ArrayList<HashMap<String, Object>>();
        gradients = new ArrayList<HashMap<String, Object>>();
        texts = new ArrayList<HashMap<String, Object>>();
        paint = new Paint();
        paint.setStrokeWidth(10);
        context = context;
    }

    public void addHandlers() {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                CharSequence rawActiveTool = getContentDescription();
                String activeTool = rawActiveTool.toString();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        MainActivity.gateway.isDetectChanges = true;
                        touchX = event.getX();
                        touchY = event.getY();
                        if (activeTool.equalsIgnoreCase("curve")) {
                            initialTouchX = touchX;
                            initialTouchY = touchY;
                            if (MainActivity.gateway.activeToolbarMenuItem == "path") {

                                initialCurvePointsCursor++;
                                initialCurvePoints[initialCurvePointsCursor] = initialTouchX;
                                initialCurvePointsCursor++;
                                initialCurvePoints[initialCurvePointsCursor] = initialTouchY;
                                Log.d("debug", "path: " + String.valueOf(initialCurvePoints[0]) + " " + String.valueOf(initialCurvePoints[1]) + " " + String.valueOf(initialCurvePoints[2]) + " " + String.valueOf(initialCurvePoints[3]) + " " + String.valueOf(initialCurvePoints[4]) + " " + String.valueOf(initialCurvePoints[5]) + ", cursor: " + String.valueOf(initialCurvePointsCursor));
                            }
                        } else if (activeTool.equalsIgnoreCase("shape")) {
                            initialTouchX = touchX;
                            initialTouchY = touchY;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        MainActivity.gateway.isDetectChanges = true;
                        touchX = event.getX();
                        touchY = event.getY();
                        if (activeTool.equalsIgnoreCase("curve")) {
                            touchX = event.getX();
                            touchY = event.getY();
                            HashMap curve = new HashMap<String, Object>();
                            ColorPickerView activityMainPaleteColor = MainActivity.gateway.findViewById(R.id.activity_main_palete_color);
                            curve.put("color", activityMainPaleteColor.getColor());
                            if (MainActivity.gateway.activeToolbarMenuItem.equalsIgnoreCase("path")) {

                                if (initialCurvePointsCursor >= 5) {
                                    curve.put("x1", initialCurvePoints[0]);
                                    curve.put("y1", initialCurvePoints[1]);
                                    curve.put("x2", initialCurvePoints[2]);
                                    curve.put("y2", initialCurvePoints[3]);
                                    curve.put("x3", initialCurvePoints[4]);
                                    curve.put("y3", initialCurvePoints[5]);
                                    curve.put("type", MainActivity.gateway.activeToolbarMenuItem);
                                    curve.put("layer", MainActivity.gateway.currentLayer);
                                    Log.d("debug", "path: " + String.valueOf(initialCurvePoints[0]) + " " + String.valueOf(initialCurvePoints[1]) + " " + String.valueOf(initialCurvePoints[2]) + " " + String.valueOf(initialCurvePoints[3]) + " " + String.valueOf(initialCurvePoints[4]) + " " + String.valueOf(initialCurvePoints[5]) + ", cursor: " + String.valueOf(initialCurvePointsCursor));
                                    initialCurvePointsCursor = -1;
                                    initialCurvePoints[0] = 0;
                                    initialCurvePoints[1] = 0;
                                    initialCurvePoints[2] = 0;
                                    initialCurvePoints[3] = 0;
                                    initialCurvePoints[4] = 0;
                                    initialCurvePoints[5] = 0;
                                    curves.add(curve);
                                }
                            } else {
                                curve.put("x1", initialTouchX);
                                curve.put("y1", initialTouchY);
                                curve.put("x2", touchX);
                                curve.put("y2", touchY);
                                curve.put("type", MainActivity.gateway.activeToolbarMenuItem);
                                curve.put("layer", MainActivity.gateway.currentLayer);
                                curves.add(curve);
                            }

                        } else if (activeTool.equalsIgnoreCase("shape")) {
                            touchX = event.getX();
                            touchY = event.getY();
                            HashMap shape = new HashMap<String, Object>();
                            shape.put("x1", initialTouchX);
                            shape.put("y1", initialTouchY);
                            shape.put("x2", touchX);
                            shape.put("y2", touchY);
                            shape.put("radius", MainActivity.gateway.roundRadius);
                            ColorPickerView activityMainPaleteColor = MainActivity.gateway.findViewById(R.id.activity_main_palete_color);
                            shape.put("color", activityMainPaleteColor.getColor());
                            shape.put("type", MainActivity.gateway.activeToolbarMenuItem);
                            shape.put("layer", MainActivity.gateway.currentLayer);
                            shapes.add(shape);
                        }  else if (activeTool.equalsIgnoreCase("fill")) {
                            HashMap fill = new HashMap<String, Object>();
                            fill.put("color", Color.BLACK);
                            fills.add(fill);
                        } else if (activeTool.equalsIgnoreCase("gradient")) {
                            touchX = event.getX();
                            touchY = event.getY();
                            HashMap gradient = new HashMap<String, Object>();
                            gradients.add(gradient);
                        } else if (activeTool.equalsIgnoreCase("text")) {
                            fillTextColor = Color.BLACK;
                            outlineTextColor = Color.BLACK;
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            MainActivity finalContext = context;
                            LayoutInflater inflater = finalContext.getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.draw_text_dialog, null);
                            builder.setView(dialogView);
                            builder.setCancelable(false);
                            ImageButton drawTextDialogContainerContentMicBtn = dialogView.findViewById(R.id.draw_text_dialog_container_content_mic_btn);
                            ImageButton drawTextDialogContainerContentKeyboardBtn = dialogView.findViewById(R.id.draw_text_dialog_container_content_keyboard_btn);
                            Spinner drawTextDialogContainerFontFamily = dialogView.findViewById(R.id.draw_text_dialog_container_font_family);
                            ImageButton drawTextDialogContainerFontDecorationColorBtn = dialogView.findViewById(R.id.draw_text_dialog_container_font_decoration_color_btn);
                            ImageButton drawTextDialogContainerFontDecorationBoldBtn = dialogView.findViewById(R.id.draw_text_dialog_container_font_decoration_bold_btn);
                            ImageButton drawTextDialogContainerFontDecorationItalicBtn = dialogView.findViewById(R.id.draw_text_dialog_container_font_decoration_italic_btn);
                            CheckBox drawTextDialogContainerFontSmooth = dialogView.findViewById(R.id.draw_text_dialog_container_font_smooth);
                            TextView drawTextDialogContainerFontSizeLabel = dialogView.findViewById(R.id.draw_text_dialog_container_font_size_label);
                            SeekBar drawTextDialogContainerFontSizeSlider = dialogView.findViewById(R.id.draw_text_dialog_container_font_size_slider);
                            TextView drawTextDialogContainerFontSpaceLabel = dialogView.findViewById(R.id.draw_text_dialog_container_font_space_label);
                            SeekBar drawTextDialogContainerFontSpaceSlider = dialogView.findViewById(R.id.draw_text_dialog_container_font_space_slider);
                            TextView drawTextDialogContainerLineHeightLabel = dialogView.findViewById(R.id.draw_text_dialog_container_line_height_label);
                            SeekBar drawTextDialogContainerLineHeightSlider = dialogView.findViewById(R.id.draw_text_dialog_container_line_height_slider);
                            ImageButton drawTextDialogContainerOutlineColorBtn = dialogView.findViewById(R.id.draw_text_dialog_container_outline_color_btn);
                            TextView drawTextDialogContainerOutlineWidthLabel = dialogView.findViewById(R.id.draw_text_dialog_container_outline_width_label);
                            SeekBar drawTextDialogContainerOutlineWidthSlider = dialogView.findViewById(R.id.draw_text_dialog_container_outline_width_slider);
                            CheckBox drawTextDialogContainerOutlineWidthSmooth = dialogView.findViewById(R.id.draw_text_dialog_container_outline_width_smooth);
                            builder.setNegativeButton("ОТМЕНА", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            builder.setPositiveButton("ЗАДАТЬ", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    TextInputEditText drawTextDialogContainerContentField = dialogView.findViewById(R.id.draw_text_dialog_container_content_field);
                                    CharSequence rawDrawTextDialogContainerContentFieldContent = drawTextDialogContainerContentField.getText();
                                    String drawTextDialogContainerContentFieldContent = rawDrawTextDialogContainerContentFieldContent.toString();
                                    boolean isTextSmooth = drawTextDialogContainerFontSmooth.isChecked();
                                    boolean isTextOutlineSmooth = drawTextDialogContainerOutlineWidthSmooth.isChecked();
                                    HashMap text = new HashMap<String, Object>();
                                    text.put("x1", touchX);
                                    text.put("y1", touchY);
                                    text.put("content", drawTextDialogContainerContentFieldContent);
                                    text.put("fontSize", drawTextDialogContainerFontSizeSlider.getProgress());
                                    text.put("isTextBold", isBold);
                                    text.put("isTextItalic", isItalic);
                                    text.put("isTextSmooth", isTextSmooth);
                                    text.put("textOutlineWidth", drawTextDialogContainerOutlineWidthSlider.getProgress());

                                    String fontFamily = (String) drawTextDialogContainerFontFamily.getSelectedItem();
                                     text.put("fontFamily", fontFamily);
                                    // text.put("isFontFamily", false);

                                    text.put("isTextOutlineSmooth", isTextOutlineSmooth);
                                    text.put("fillTextColor", fillTextColor);
                                    text.put("outlineTextColor", outlineTextColor);
                                    texts.add(text);
                                    invalidate();
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.setTitle("Текст");
                            alert.setOnShowListener(new DialogInterface.OnShowListener() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onShow(DialogInterface dialogInterface) {
                                    isBold = false;
                                    isItalic = false;
                                    drawTextDialogContainerContentMicBtn.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String toastMessage = "В настоящее время функция\nголосового набора не\nиспользуется.";
                                            Toast toast = Toast.makeText(getContext(), toastMessage, Toast.LENGTH_SHORT);
                                        }
                                    });
                                    drawTextDialogContainerContentKeyboardBtn.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            InputMethodManager imm = ((InputMethodManager)(getContext().getSystemService(Context.INPUT_METHOD_SERVICE)));
                                            imm.hideSoftInputFromWindow(getWindowToken(), 0);
                                        }
                                    });
                                    ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(),
                                            R.array.fonts, android.R.layout.simple_spinner_item);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                                    drawTextDialogContainerFontFamily.setAdapter(adapter);
                                    drawTextDialogContainerFontDecorationColorBtn.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            new ColorPickerDialog.Builder(getContext())
                                                    .setPreferenceName("MyColorPickerDialog")
                                                    .setPositiveButton("ОК",
                                                            new ColorEnvelopeListener() {
                                                                @Override
                                                                public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                                                    fillTextColor = envelope.getColor();
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
                                    drawTextDialogContainerFontDecorationBoldBtn.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            isBold = !isBold;
                                            if (isBold) {
                                                drawTextDialogContainerFontDecorationBoldBtn.setColorFilter(enabledBtnColor);
                                            } else {
                                                drawTextDialogContainerFontDecorationBoldBtn.setColorFilter(disabledBtnColor);
                                            }
                                        }
                                    });
                                    drawTextDialogContainerFontDecorationItalicBtn.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            isItalic = !isItalic;
                                            if (isItalic) {
                                                drawTextDialogContainerFontDecorationItalicBtn.setColorFilter(enabledBtnColor);
                                            } else {
                                                drawTextDialogContainerFontDecorationItalicBtn.setColorFilter(disabledBtnColor);
                                            }
                                        }
                                    });
                                    drawTextDialogContainerFontSizeSlider.setMin(0);
                                    drawTextDialogContainerFontSizeSlider.setMax(100);
                                    drawTextDialogContainerFontSizeSlider.setProgress(18);
                                    drawTextDialogContainerFontSpaceSlider.setMin(0);
                                    drawTextDialogContainerFontSpaceSlider.setMax(100);
                                    drawTextDialogContainerFontSpaceSlider.setProgress(0);
                                    drawTextDialogContainerLineHeightSlider.setMin(0);
                                    drawTextDialogContainerLineHeightSlider.setMax(100);
                                    drawTextDialogContainerLineHeightSlider.setProgress(0);
                                    drawTextDialogContainerFontSizeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                        @Override
                                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                            drawTextDialogContainerFontSizeLabel.setText("Размер текста " + String.valueOf(i) + " pt");
                                        }

                                        @Override
                                        public void onStartTrackingTouch(SeekBar seekBar) {

                                        }

                                        @Override
                                        public void onStopTrackingTouch(SeekBar seekBar) {

                                        }
                                    });
                                    drawTextDialogContainerFontSpaceSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                        @Override
                                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                            drawTextDialogContainerFontSpaceLabel.setText("Текстовое пространство " + String.valueOf(i));
                                        }

                                        @Override
                                        public void onStartTrackingTouch(SeekBar seekBar) {

                                        }

                                        @Override
                                        public void onStopTrackingTouch(SeekBar seekBar) {

                                        }
                                    });
                                    drawTextDialogContainerLineHeightSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                        @Override
                                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                            drawTextDialogContainerLineHeightLabel.setText("Муждустрочный интервал " + String.valueOf(i));
                                        }

                                        @Override
                                        public void onStartTrackingTouch(SeekBar seekBar) {

                                        }

                                        @Override
                                        public void onStopTrackingTouch(SeekBar seekBar) {

                                        }
                                    });
                                    drawTextDialogContainerOutlineColorBtn.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            new ColorPickerDialog.Builder(getContext())
                                                    .setPreferenceName("MyColorPickerDialog")
                                                    .setPositiveButton("ОК",
                                                            new ColorEnvelopeListener() {
                                                                @Override
                                                                public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                                                    outlineTextColor = envelope.getColor();
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
                                    drawTextDialogContainerOutlineWidthSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                        @Override
                                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                            drawTextDialogContainerOutlineWidthLabel.setText("Ширина края " + String.valueOf(drawTextDialogContainerOutlineWidthSlider.getProgress()) + " px");
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
                            alert.show();
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        MainActivity.gateway.isDetectChanges = true;
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        MainActivity.gateway.isDetectChanges = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        MainActivity.gateway.isDetectChanges = true;
                        touchX = event.getX();
                        touchY = event.getY();
                        if (activeTool.equalsIgnoreCase("pen")) {
                            HashMap line = new HashMap<String, Object>();
                            line.put("x1", touchX);
                            line.put("y1", touchY);
                            line.put("x2", touchX + 10);
                            line.put("y2", touchY + 10);
                            ColorPickerView activityMainPaleteColor = MainActivity.gateway.findViewById(R.id.activity_main_palete_color);
                            line.put("color", activityMainPaleteColor.getColor());
                            line.put("strokeWidth", 10);
                            lines.add(line);
                            line.put("layer", MainActivity.gateway.currentLayer);
                            invalidate();
                        } else if (activeTool.equalsIgnoreCase("eraser")) {
                            touchX = event.getX();
                            touchY = event.getY();
                            HashMap line = new HashMap<String, Object>();
                            line.put("x1", touchX);
                            line.put("y1", touchY);
                            line.put("x2", touchX + 10);
                            line.put("y2", touchY + 10);
                            line.put("color", Color.WHITE);
//                            line.put("color", MainActivity.gateway.fillColor);
                            line.put("strokeWidth", 50);
                            lines.add(line);
                            invalidate();
                        } else if (activeTool.equalsIgnoreCase("curve")) {
                            if (MainActivity.gateway.activeToolbarMenuItem == "path") {
                                if (initialCurvePointsCursor <= 5 - 2) {
                                    initialCurvePoints[initialCurvePointsCursor] = touchX;
                                    initialCurvePoints[initialCurvePointsCursor + 1] = touchY;
                                }
                            }
                            invalidate();
                        } else if (activeTool.equalsIgnoreCase("shape")) {
                            invalidate();
                        } else if (activeTool.equalsIgnoreCase("fill")) {
                            invalidate();
                        } else if (activeTool.equalsIgnoreCase("gradient")) {
                            invalidate();
                        } else if (activeTool.equalsIgnoreCase("text")) {

                        }
                        break;
                }
                return true;
            }
        });
    }

    public void getTransferData() {

    }

    public void deform(Matrix matrix) {
        Matrix deformMatrix = new Matrix();
        deformMatrix.setPolyToPoly(new float[] {
        0, 0,
        getWidth(), 0,
        0, getHeight(),
        getWidth(), getHeight()
        }, 0,
        new float[] {
            255.0f, 0.0f,
            850.0f, 320.0f,
            150.0f, 1.0f,
            1.0f, 743.0f
        }, 0,
        4);
        myCanvas.setMatrix(deformMatrix);
        // invalidate();
    }

    public void addSelectArea() {
//        boolean selectionArea = myCanvas.clipRect(150.0f, 150.0f, 150.0f, 150.0f);
        boolean selectionArea = myCanvas.clipRect(0.0f, 0.0f, 0.0f, 0.0f);
        Log.d("debug", "selectionArea: " + String.valueOf(selectionArea));
//        myCanvas.clipOutRect(150.0f, 150.0f, 150.0f, 150.0f);
        invalidate();
    }

}

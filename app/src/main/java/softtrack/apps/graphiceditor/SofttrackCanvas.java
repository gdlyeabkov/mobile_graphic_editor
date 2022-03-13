package softtrack.apps.graphiceditor;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
//import androidx.window.layout.WindowMetricsCalculator;
//import androidx.window.testing.layout.FoldingFeature;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.utils.widget.MockView;
import androidx.window.layout.WindowMetricsCalculator;
import androidx.window.testing.layout.WindowLayoutInfoPublisherRule;

import com.google.android.material.textfield.TextInputEditText;

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
    public ArrayList<HashMap<String, Object>> curves;
    public ArrayList<HashMap<String, Object>> shapes;
    public ArrayList<HashMap<String, Object>> gradients;
    public ArrayList<HashMap<String, Object>> texts;
    public boolean isBold = false;
    public boolean isItalic = false;
    public int enabledBtnColor;
    public int disabledBtnColor;

    public SofttrackCanvas(Context context) {
        super(context);

        initialize((MainActivity) context);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        boolean isCurvesExists = curves != null;
        if (isCurvesExists) {
            paint.setColor(Color.BLUE);
            for (int curveIndex = 0; curveIndex < curves.size(); curveIndex++) {
                HashMap curve = curves.get(curveIndex);
                float curveX1 = (float) curve.get("x1");
                float curveY1 = (float) curve.get("y1");
                float curveX2 = (float) curve.get("x2");
                float curveY2 = (float) curve.get("y2");
                int curveColor = (int) curve.get("color");
                int curveStrokeWidth = (int) curve.get("strokeWidth");
                Paint curvePaint = new Paint();
                curvePaint.setColor(curveColor);
                curvePaint.setStrokeWidth(curveStrokeWidth);
                canvas.drawLine(curveX1, curveY1, curveX2, curveY2, curvePaint);
            }
        }

        boolean isShapesExists = shapes != null;
        if (isShapesExists) {
            paint.setColor(Color.BLUE);
            for (int shapeIndex = 0; shapeIndex < shapes.size(); shapeIndex++) {
                HashMap shape = shapes.get(shapeIndex);
                float shapeX1 = (float) shape.get("x1");
                float shapeY1 = (float) shape.get("y1");
                float shapeX2 = (float) shape.get("x2");
                float shapeY2 = (float) shape.get("y2");
                Paint shapePaint = new Paint();
                shapePaint.setColor(Color.BLACK);
                canvas.drawRect(shapeX1, shapeY1, shapeX2, shapeY2, shapePaint);
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
                // boolean isFontFamily = (boolean) text.get("isFontFamily");
                Paint textPaint = new Paint();
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
                paint.setColor(Color.BLUE);
                canvas.drawLine(touchX, touchY, touchX + 10, touchY + 10, paint);
            } else if (activeTool.equalsIgnoreCase("eraser")) {
                paint.setColor(Color.WHITE);
                canvas.drawLine(touchX, touchY, touchX + 10, touchY + 10, paint);
            } else if (activeTool.equalsIgnoreCase("curve")) {
                paint.setColor(Color.WHITE);
                canvas.drawLine(touchX, touchY, touchX + 10, touchY + 10, paint);
            } else if (activeTool.equalsIgnoreCase("shape")) {
                paint.setColor(Color.BLACK);
                canvas.drawRect(initialTouchX, initialTouchY, touchX, touchY, paint);
            } else if (activeTool.equalsIgnoreCase("fill")) {
                paint.setColor(Color.WHITE);
                canvas.drawLine(touchX, touchY, touchX + 10, touchY + 10, paint);
            } else if (activeTool.equalsIgnoreCase("gradient")) {
                paint.setColor(Color.WHITE);
                canvas.drawLine(touchX, touchY, touchX + 10, touchY + 10, paint);
            } else if (activeTool.equalsIgnoreCase("text")) {

            }

    }

    public void initialize(MainActivity context) {
        enabledBtnColor = Color.argb(255, 255, 255, 255);
        disabledBtnColor = Color.TRANSPARENT;
        setContentDescription("pen");
        curves = new ArrayList<HashMap<String, Object>>();
        shapes = new ArrayList<HashMap<String, Object>>();
        gradients = new ArrayList<HashMap<String, Object>>();
        texts = new ArrayList<HashMap<String, Object>>();
        paint = new Paint();
        paint.setStrokeWidth(10);
        context = context;
        MainActivity finalContext = context;
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                CharSequence rawActiveTool = getContentDescription();
                String activeTool = rawActiveTool.toString();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        touchX = event.getX();
                        touchY = event.getY();
                        if (activeTool.equalsIgnoreCase("shape")) {
                            initialTouchX = touchX;
                            initialTouchY = touchY;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        touchX = event.getX();
                        touchY = event.getY();
                        if (activeTool.equalsIgnoreCase("shape")) {
                            touchX = event.getX();
                            touchY = event.getY();
                            HashMap shape = new HashMap<String, Object>();
                            shape.put("x1", initialTouchX);
                            shape.put("y1", initialTouchY);
                            shape.put("x2", touchX);
                            shape.put("y2", touchY);
                            shape.put("color", Color.BLACK);
                            shapes.add(shape);
                        } else if (activeTool.equalsIgnoreCase("gradient")) {
                            touchX = event.getX();
                            touchY = event.getY();
                            HashMap gradient = new HashMap<String, Object>();
                            gradients.add(gradient);
                        } else if (activeTool.equalsIgnoreCase("text")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            LayoutInflater inflater = finalContext.getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.draw_text_dialog, null);
                            builder.setView(dialogView);
                            builder.setCancelable(false);
                            ImageButton drawTextDialogContainerContentMicBtn = dialogView.findViewById(R.id.draw_text_dialog_container_content_mic_btn);
                            ImageButton drawTextDialogContainerContentKeyboardBtn = dialogView.findViewById(R.id.draw_text_dialog_container_content_keyboard_btn);
                            Spinner drawTextDialogContainerFontFamily = dialogView.findViewById(R.id.draw_text_dialog_container_font_family);
                            ImageButton drawTextDialogContainerFontDecorationBoldBtn = dialogView.findViewById(R.id.draw_text_dialog_container_font_decoration_bold_btn);
                            ImageButton drawTextDialogContainerFontDecorationItalicBtn = dialogView.findViewById(R.id.draw_text_dialog_container_font_decoration_italic_btn);
                            CheckBox drawTextDialogContainerFontSmooth = dialogView.findViewById(R.id.draw_text_dialog_container_font_smooth);
                            TextView drawTextDialogContainerFontSizeLabel = dialogView.findViewById(R.id.draw_text_dialog_container_font_size_label);
                            SeekBar drawTextDialogContainerFontSizeSlider = dialogView.findViewById(R.id.draw_text_dialog_container_font_size_slider);
                            TextView drawTextDialogContainerFontSpaceLabel = dialogView.findViewById(R.id.draw_text_dialog_container_font_space_label);
                            SeekBar drawTextDialogContainerFontSpaceSlider = dialogView.findViewById(R.id.draw_text_dialog_container_font_space_slider);
                            TextView drawTextDialogContainerLineHeightLabel = dialogView.findViewById(R.id.draw_text_dialog_container_line_height_label);
                            SeekBar drawTextDialogContainerLineHeightSlider = dialogView.findViewById(R.id.draw_text_dialog_container_line_height_slider);
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
                                    // text.put("isFontFamily", false);
                                    text.put("isTextOutlineSmooth", isTextOutlineSmooth);
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
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        touchX = event.getX();
                        touchY = event.getY();
                        if (activeTool.equalsIgnoreCase("pen")) {
                            HashMap curve = new HashMap<String, Object>();
                            curve.put("x1", touchX);
                            curve.put("y1", touchY);
                            curve.put("x2", touchX + 10);
                            curve.put("y2", touchY + 10);
                            curve.put("color", Color.BLUE);
                            curve.put("strokeWidth", 10);
                            curves.add(curve);
                            invalidate();
                        } else if (activeTool.equalsIgnoreCase("eraser")) {
                            touchX = event.getX();
                            touchY = event.getY();
                            HashMap curve = new HashMap<String, Object>();
                            curve.put("x1", touchX);
                            curve.put("y1", touchY);
                            curve.put("x2", touchX + 10);
                            curve.put("y2", touchY + 10);
                            curve.put("color", Color.WHITE);
                            curve.put("strokeWidth", 50);
                            curves.add(curve);
                            invalidate();
                        } else if (activeTool.equalsIgnoreCase("curve")) {
                            touchX = event.getX();
                            touchY = event.getY();
                            HashMap curve = new HashMap<String, Object>();
                            curve.put("x1", touchX);
                            curve.put("y1", touchY);
                            curve.put("x2", touchX + 10);
                            curve.put("y2", touchY + 10);
                            curve.put("color", Color.WHITE);
                            curve.put("strokeWidth", 50);
                            curves.add(curve);
                            invalidate();
                        } else if (activeTool.equalsIgnoreCase("shape")) {
                            invalidate();
                        } else if (activeTool.equalsIgnoreCase("fill")) {
                            touchX = event.getX();
                            touchY = event.getY();
                            HashMap curve = new HashMap<String, Object>();
                            curve.put("x1", touchX);
                            curve.put("y1", touchY);
                            curve.put("x2", touchX + 10);
                            curve.put("y2", touchY + 10);
                            curve.put("color", Color.WHITE);
                            curve.put("strokeWidth", 50);
                            curves.add(curve);
                            invalidate();
                        } else if (activeTool.equalsIgnoreCase("gradient")) {
                            touchX = event.getX();
                            touchY = event.getY();
                            HashMap curve = new HashMap<String, Object>();
                            curve.put("x1", touchX);
                            curve.put("y1", touchY);
                            curve.put("x2", touchX + 10);
                            curve.put("y2", touchY + 10);
                            curve.put("color", Color.WHITE);
                            curve.put("strokeWidth", 50);
                            curves.add(curve);
                            invalidate();
                        } else if (activeTool.equalsIgnoreCase("text")) {

                        }
                        break;
                }
                return true;
            }
        });
    }

}

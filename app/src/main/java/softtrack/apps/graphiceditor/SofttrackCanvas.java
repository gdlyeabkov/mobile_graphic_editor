package softtrack.apps.graphiceditor;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowMetrics;
import android.widget.EditText;

import androidx.annotation.Nullable;
//import androidx.window.layout.WindowMetricsCalculator;
//import androidx.window.testing.layout.FoldingFeature;
import androidx.constraintlayout.utils.widget.MockView;
import androidx.window.layout.WindowMetricsCalculator;
import androidx.window.testing.layout.WindowLayoutInfoPublisherRule;

import java.util.ArrayList;
import java.util.HashMap;

public class SofttrackCanvas extends View {

    public Paint paint;
    public MainActivity context;
    public float touchX = 0;
    public float touchY = 0;
    public ArrayList<HashMap<String, Object>> curves;
    public ArrayList<HashMap<String, Object>> texts;

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

        boolean isTextsExists = texts != null;
        if (isTextsExists) {
            paint.setColor(Color.BLUE);
            for (int textIndex = 0; textIndex < texts.size(); textIndex++) {
                HashMap text = texts.get(textIndex);
                float textX = (float) text.get("x1");
                float textY = (float) text.get("y1");
                String textContent = (String) text.get("content");
//                float textX = 100.0f;
//                float textY = 100.0f;
                Paint textPaint = new Paint();
                textPaint.setColor(Color.BLACK);
                textPaint.setTextSize(48);
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
                paint.setColor(Color.WHITE);
                canvas.drawLine(touchX, touchY, touchX + 10, touchY + 10, paint);
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
        setContentDescription("pen");
        curves = new ArrayList<HashMap<String, Object>>();
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
                        break;
                    case MotionEvent.ACTION_UP:
                        touchX = event.getX();
                        touchY = event.getY();
                        if (activeTool.equalsIgnoreCase("text")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            LayoutInflater inflater = finalContext.getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.draw_text_dialog, null);
                            builder.setView(dialogView);
                            builder.setCancelable(false);
                            builder.setNegativeButton("ОТМЕНА", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            builder.setPositiveButton("ЗАДАТЬ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    EditText drawTextDialogContainerContentField = dialogView.findViewById(R.id.draw_text_dialog_container_content_field);
                                    CharSequence rawDrawTextDialogContainerContentFieldContent = drawTextDialogContainerContentField.getText();
                                    String drawTextDialogContainerContentFieldContent = rawDrawTextDialogContainerContentFieldContent.toString();
                                    HashMap text = new HashMap<String, Object>();
                                    text.put("x1", touchX);
                                    text.put("y1", touchY);
                                    text.put("content", drawTextDialogContainerContentFieldContent);
                                    texts.add(text);
                                    invalidate();
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.setTitle("Текст");
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

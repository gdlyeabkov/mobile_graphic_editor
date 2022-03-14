package softtrack.apps.graphiceditor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ToolbarCurveMenuFragment extends Fragment {

    public MainActivity parentActivity;
    public LinearLayout toolbarCurveMenuContainerLine;
    public LinearLayout toolbarCurveMenuContainerPath;
    public LinearLayout toolbarCurveMenuContainerBezier;
    public LinearLayout toolbarCurveMenuContainerRect;
    public LinearLayout toolbarCurveMenuContainerOval;
    public LinearLayout toolbarCurveMenuContainerPolygone;

    ToolbarCurveMenuFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.toolbar_curve_menu, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }

    public void initialize() {
        initializeConstants();
        findViews();
        addHandlers();
    }

    public void initializeConstants() {
        parentActivity = (MainActivity) getActivity();
    }

    public void findViews() {
        toolbarCurveMenuContainerLine = parentActivity.findViewById(R.id.toolbar_curve_menu_container_line);
        toolbarCurveMenuContainerPath = parentActivity.findViewById(R.id.toolbar_curve_menu_container_path);
        toolbarCurveMenuContainerBezier = parentActivity.findViewById(R.id.toolbar_curve_menu_container_curve);
        toolbarCurveMenuContainerRect = parentActivity.findViewById(R.id.toolbar_curve_menu_container_rect);
        toolbarCurveMenuContainerOval = parentActivity.findViewById(R.id.toolbar_curve_menu_container_oval);
        toolbarCurveMenuContainerPolygone = parentActivity.findViewById(R.id.toolbar_curve_menu_container_polygone);;
    }

    public void addHandlers() {
        toolbarCurveMenuContainerLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.gateway.activeToolbarMenuItem = "line";
            }
        });
        toolbarCurveMenuContainerPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.gateway.activeToolbarMenuItem = "path";
            }
        });
        toolbarCurveMenuContainerBezier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.gateway.activeToolbarMenuItem = "bezier";
            }
        });
        toolbarCurveMenuContainerRect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.gateway.activeToolbarMenuItem = "rect";
            }
        });
        toolbarCurveMenuContainerOval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.gateway.activeToolbarMenuItem = "oval";
            }
        });
        toolbarCurveMenuContainerPolygone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.gateway.activeToolbarMenuItem = "polygone";
            }
        });
    }

}

package softtrack.apps.graphiceditor;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewStateAdapter extends FragmentStateAdapter {

    public ViewStateAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new ToolbarPenMenuFragment();
        } else if (position == 1) {
            return new ToolbarCurveMenuFragment();
        } else if (position == 2) {
            return new ToolbarShapeMenuFragment();
        }
        return new ToolbarPenMenuFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

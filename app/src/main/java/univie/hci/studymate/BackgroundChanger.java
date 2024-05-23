package univie.hci.studymate;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

public class BackgroundChanger {

    private boolean isOldBackground = true;

    public void setupBackgroundChanger(Activity activity, ConstraintLayout mainLayout, ImageView changeBackgroundButton) {
        changeBackgroundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOldBackground) {
                    Drawable altBackground = ContextCompat.getDrawable(activity, R.drawable.background_gradient_other);
                    mainLayout.setBackground(altBackground);
                } else {
                    Drawable originalBackground = ContextCompat.getDrawable(activity, R.drawable.background_gradient);
                    mainLayout.setBackground(originalBackground);
                }
                isOldBackground = !isOldBackground;
            }
        });
    }
}

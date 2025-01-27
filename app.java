package com.example.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.button.MaterialButton;

public class SwimmingWidget extends ConstraintLayout {
    private Context mContext;
    private CardView mainCard;
    private TextView titleText, sessionTitle, sessionTime;
    private MaterialButton qrButton;
    private ImageButton moreButton;

    public SwimmingWidget(Context context) {
        super(context);
        init(context);
    }

    public SwimmingWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        setupMainLayout();
    }

    private void setupMainLayout() {
        // Main CardView
        mainCard = new CardView(mContext);
        LayoutParams mainParams = new LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        );
        mainParams.setMargins(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));
        mainCard.setLayoutParams(mainParams);
        mainCard.setCardCornerRadius(dpToPx(28));
        mainCard.setCardBackgroundColor(0xFF4B75F2);
        mainCard.setCardElevation(0);

        // Main Container
        ConstraintLayout container = new ConstraintLayout(mContext);
        container.setLayoutParams(new LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        ));
        container.setPadding(dpToPx(24), dpToPx(24), dpToPx(24), dpToPx(24));

        // Swimming Icon Container
        CardView iconContainer = new CardView(mContext);
        iconContainer.setId(View.generateViewId());
        ConstraintLayout.LayoutParams iconParams = new ConstraintLayout.LayoutParams(
            dpToPx(40),
            dpToPx(40)
        );
        iconContainer.setLayoutParams(iconParams);
        iconContainer.setCardCornerRadius(dpToPx(20));
        iconContainer.setCardBackgroundColor(0xFF6B8DF4);

        // Swimming Icon
        ImageView swimmingIcon = new ImageView(mContext);
        CardView.LayoutParams iconImageParams = new CardView.LayoutParams(
            dpToPx(24),
            dpToPx(24)
        );
        iconImageParams.gravity = android.view.Gravity.CENTER;
        swimmingIcon.setLayoutParams(iconImageParams);
        swimmingIcon.setImageDrawable(createSwimmingIcon());
        iconContainer.addView(swimmingIcon);

        // Title Text
        titleText = new TextView(mContext);
        titleText.setId(View.generateViewId());
        ConstraintLayout.LayoutParams titleParams = new ConstraintLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        );
        titleParams.startToEnd = iconContainer.getId();
        titleParams.topToTop = iconContainer.getId();
        titleParams.bottomToBottom = iconContainer.getId();
        titleParams.setMarginStart(dpToPx(12));
        titleText.setLayoutParams(titleParams);
        titleText.setText("Swimming pool");
        titleText.setTextColor(0xFFFFFFFF);
        titleText.setTextSize(18);

        // More Button
        moreButton = new ImageButton(mContext);
        moreButton.setId(View.generateViewId());
        ConstraintLayout.LayoutParams moreParams = new ConstraintLayout.LayoutParams(
            dpToPx(40),
            dpToPx(40)
        );
        moreParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        moreParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        moreButton.setLayoutParams(moreParams);
        moreButton.setBackground(createCircleDrawable(0xFF6B8DF4));
        moreButton.setImageDrawable(createMoreIcon());

        // Session Title
        sessionTitle = new TextView(mContext);
        sessionTitle.setId(View.generateViewId());
        ConstraintLayout.LayoutParams sessionTitleParams = new ConstraintLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        );
        sessionTitleParams.topToBottom = iconContainer.getId();
        sessionTitleParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        sessionTitleParams.topMargin = dpToPx(24);
        sessionTitle.setLayoutParams(sessionTitleParams);
        sessionTitle.setText("Beginners session");
        sessionTitle.setTextColor(0xFFFFFFFF);
        sessionTitle.setTextSize(24);

        // Session Time
        sessionTime = new TextView(mContext);
        sessionTime.setId(View.generateViewId());
        ConstraintLayout.LayoutParams timeParams = new ConstraintLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        );
        timeParams.topToBottom = sessionTitle.getId();
        timeParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        timeParams.topMargin = dpToPx(8);
        sessionTime.setLayoutParams(timeParams);
        sessionTime.setText("10:00 – 10:45");
        sessionTime.setTextColor(0xFFFFFFFF);
        sessionTime.setTextSize(24);

        // QR Button
        qrButton = new MaterialButton(mContext);
        qrButton.setId(View.generateViewId());
        ConstraintLayout.LayoutParams qrParams = new ConstraintLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        );
        qrParams.topToBottom = sessionTime.getId();
        qrParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        qrParams.topMargin = dpToPx(24);
        qrButton.setLayoutParams(qrParams);
        qrButton.setText("Show QR code");
        qrButton.setTextColor(0xFFFFFFFF);
        qrButton.setAllCaps(false);
        qrButton.setBackgroundColor(0xFF6B8DF4);
        qrButton.setCornerRadius(dpToPx(16));
        qrButton.setPadding(dpToPx(16), 0, dpToPx(16), 0);
        qrButton.setIcon(createQRCodeIcon());
        qrButton.setIconTint(android.content.res.ColorStateList.valueOf(0xFFFFFFFF));
        qrButton.setIconGravity(MaterialButton.ICON_GRAVITY_START);

        // Add Dot Indicators
        ConstraintLayout dotsContainer = createDotIndicators();
        ConstraintLayout.LayoutParams dotsParams = new ConstraintLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        );
        dotsParams.topToBottom = qrButton.getId();
        dotsParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        dotsParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        dotsParams.topMargin = dpToPx(16);
        dotsContainer.setLayoutParams(dotsParams);

        // Add all views to container
        container.addView(iconContainer);
        container.addView(titleText);
        container.addView(moreButton);
        container.addView(sessionTitle);
        container.addView(sessionTime);
        container.addView(qrButton);
        container.addView(dotsContainer);

        // Add container to main card
        mainCard.addView(container);

        // Add main card to this widget
        addView(mainCard);

        // Setup click listeners
        setupClickListeners();
    }

    private ConstraintLayout createDotIndicators() {
        ConstraintLayout dotsContainer = new ConstraintLayout(mContext);
        android.widget.LinearLayout dots = new android.widget.LinearLayout(mContext);
        dots.setOrientation(android.widget.LinearLayout.HORIZONTAL);

        for (int i = 0; i < 4; i++) {
            View dot = new View(mContext);
            android.widget.LinearLayout.LayoutParams dotParams = new android.widget.LinearLayout.LayoutParams(
                dpToPx(8),
                dpToPx(8)
            );
            dotParams.setMargins(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));
            dot.setLayoutParams(dotParams);
            dot.setBackground(createCircleDrawable(i == 0 ? 0xFFFFFFFF : 0xFF6B8DF4));
            dots.addView(dot);
        }

        dotsContainer.addView(dots);
        return dotsContainer;
    }

    private void setupClickListeners() {
        qrButton.setOnClickListener(v -> {
            // Handle QR code button click
        });

        moreButton.setOnClickListener(v -> {
            // Handle more options button click
        });
    }

    private android.graphics.drawable.Drawable createCircleDrawable(int color) {
        android.graphics.drawable.GradientDrawable circle = new android.graphics.drawable.GradientDrawable();
        circle.setShape(android.graphics.drawable.GradientDrawable.OVAL);
        circle.setColor(color);
        return circle;
    }

    private android.graphics.drawable.Drawable createSwimmingIcon() {
        // Create a simple swimming icon using paths
        android.graphics.drawable.VectorDrawable.VectorDrawableFactory factory = 
            new android.graphics.drawable.VectorDrawable.VectorDrawableFactory();
        
        String pathData = "M22,21c-1.11,0-1.73,-0.37-2.18,-0.64-0.37,-0.22-0.6,-0.36-1.15,-0.36-0.56,0-0.78,0.13-1.15,0.36-0.46,0.27-1.07,0.64-2.18,0.64s-1.73,-0.37-2.18,-0.64c-0.37,-0.22-0.6,-0.36-1.15,-0.36-0.56,0-0.78,0.13-1.15,0.36-0.46,0.27-1.08,0.64-2.19,0.64-1.11,0-1.73,-0.37-2.18,-0.64-0.37,-0.23-0.6,-0.36-1.15,-0.36s-0.78,0.13-1.15,0.36c-0.46,0.27-1.08,0.64-2.19,0.64v-2c0.56,0,0.78,-0.13,1.15,-0.36,0.46,-0.27,1.08,-0.64,2.19,-0.64s1.73,0.37,2.18,0.64c0.37,0.23,0.59,0.36,1.15,0.36,0.56,0,0.78,-0.13,1.15,-0.36,0.46,-0.27,1.08,-0.64,2.19,-0.64,1.11,0,1.73,0.37,2.18,0.64,0.37,0.22,0.6,0.36,1.15,0.36s0.78,-0.13,1.15,-0.36c0.45,-0.27,1.07,-0.64,2.18,-0.64s1.73,0.37,2.18,0.64c0.37,0.23,0.59,0.36,1.15,0.36v2z";
        
        return factory.create(24, 24, pathData, 0xFFFFFFFF);
    }

    private android.graphics.drawable.Drawable createQRCodeIcon() {
        // Create a simple QR code icon
        android.graphics.drawable.VectorDrawable.VectorDrawableFactory factory = 
            new android.graphics.drawable.VectorDrawable.VectorDrawableFactory();
        
        String pathData = "M3,3h6v6H3V3z M21,3v6h-6V3H21z M3,21h6v-6H3V21z M11,21h2v-2h-2V21z M13,13h2v-2h-2V13z M15,21h2v-2h-2V21z M17,13h2v-2h-2V13z M15,17h2v-2h-2V17z M17,19h2v-2h-2V19z";
        
        return factory.create(24, 24, pathData, 0xFFFFFFFF);
    }

    private android.graphics.drawable.Drawable createMoreIcon() {
        // Create a simple more/options icon (three dots)
        android.graphics.drawable.VectorDrawable.VectorDrawableFactory factory = 
            new android.graphics.drawable.VectorDrawable.VectorDrawableFactory();
        
        String pathData = "M12,8c1.1,0,2-0.9,2-2s-0.9-2-2-2s-2,0.9-2,2S10.9,8,12,8z M12,10c-1.1,0-2,0.9-2,2s0.9,2,2,2s2-0.9,2-2S13.1,10,12,10z M12,16c-1.1,0-2,0.9-2,2s0.9,2,2,2s2-0.9,2-2S13.1,16,12,16z";
        
        return factory.create(24, 24, pathData, 0xFFFFFFFF);
    }

    private int dpToPx(int dp) {
        float density = mContext.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    // Public methods to update widget content
    public void setSessionInfo(String title, String time) {
        sessionTitle.setText(title);
        sessionTime.setText(time);
    }

    public void setTitle(String title) {
        titleText.setText(title);
    }
}
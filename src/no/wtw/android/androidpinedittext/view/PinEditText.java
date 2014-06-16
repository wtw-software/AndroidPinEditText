package no.wtw.android.androidpinedittext.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import no.wtw.android.androidpinedittext.R;

public class PinEditText extends EditText {

    private static final String TAG = PinEditText.class.getSimpleName();
    private Paint paint;
    private int pinLength;
    private float size;
    private float margin;
    private float radius;
    private Drawable digitBackgroundDefault;
    private Drawable digitBackgroundFocused;
    private RectF dotRectangle;
    private DisplayMetrics displayMetrics;

    public PinEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PinEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        displayMetrics = getResources().getDisplayMetrics();
        setCursorVisible(false);
        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            setTextIsSelectable(false);
        setLongClickable(false);
        dotRectangle = new RectF();
        paint = new Paint();
        paint.setAntiAlias(true);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AndroidPinEditText,
                0, 0);
        try {
            paint.setColor(getCurrentTextColor());
            pinLength = a.getInt(R.styleable.AndroidPinEditText_pinLength, 4);
            size = a.getDimension(R.styleable.AndroidPinEditText_digitSize, dpToPx(56));
            margin = a.getDimension(R.styleable.AndroidPinEditText_digitMargin, dpToPx(8));
            radius = a.getDimension(R.styleable.AndroidPinEditText_digitDotRadius, dpToPx(6));

            Drawable backgroundDefault = a.getDrawable(R.styleable.AndroidPinEditText_digitBackgroundDefault);
            Drawable backgroundFocused = a.getDrawable(R.styleable.AndroidPinEditText_digitBackgroundFocused);
            if (!isInEditMode()) {
                digitBackgroundDefault = backgroundDefault != null ? backgroundDefault : getResources().getDrawable(R.drawable.pin_digit_background_default);
                digitBackgroundFocused = backgroundFocused != null ? backgroundFocused : getResources().getDrawable(R.drawable.pin_digit_background_focused);
                digitBackgroundDefault.setBounds(0, 0, (int) size, (int) size);
                digitBackgroundFocused.setBounds(0, 0, (int) size, (int) size);
            }
        } finally {
            a.recycle();
        }


        setFilters(new InputFilter[]{new InputFilter.LengthFilter(pinLength)});
        invalidate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            setBackground(null);
        else
            setBackgroundDrawable(null);
    }

    private float dpToPx(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int chars = getText().toString().length();
        for (int i = 0; i < pinLength; i++) {
            float x = i * (size + margin) + getPaddingLeft();
            float y = getPaddingTop();
            canvas.save();
            canvas.translate(x, y);
            if (!isInEditMode()) {
                if (isFocused())
                    digitBackgroundFocused.draw(canvas);
                else
                    digitBackgroundDefault.draw(canvas);
            }
            canvas.restore();
            if (i < chars) {
                dotRectangle.set(x + size / 2 - radius, y + size / 2 - radius, x + size / 2 + radius, y + size / 2 + radius);
                canvas.drawOval(dotRectangle, paint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int) (pinLength * (size + margin) - margin + getPaddingLeft() + getPaddingRight()), (int) (size + getPaddingTop() + getPaddingBottom()));
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if (selStart < length())
            setSelection(length());
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        invalidate();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        Log.d("PIN: ", text.toString());
    }

    public void shakeIt() {
        Animation shake = new TranslateAnimation(0, 30, 0, 0);
        shake.setInterpolator(new CycleInterpolator(3));
        shake.setDuration(300);
        startAnimation(shake);
    }

}

package app.youkai.progressview;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple View for number selection.
 */
@SuppressWarnings("FieldCanBeLocal")
public class ProgressView extends LinearLayout {
    private ImageView decrement;
    private EditText progressView;
    private TextView totalView;
    private ImageView increment;

    private int progress, total;

    private OnProgressChangedListener listener;

    private boolean fromButton;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyle, int defStyleRes) {
        super(context, attrs);
        init(attrs, defStyle, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyle, int defStyleRes) {
        /* Setup our root LinearLayout */
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_progressview, this, true);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        /* Set android:animateLayoutChanges="true" */
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayoutTransition(new LayoutTransition());
        }

        /* Obtain references to our views */
        decrement = (ImageView) findViewById(R.id.decrement);
        progressView = (EditText) findViewById(R.id.progress);
        totalView = (TextView) findViewById(R.id.total);
        increment = (ImageView) findViewById(R.id.increment);

        /* Set click listeners */
        decrement.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setProgress(progress - 1);
            }
        });
        increment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setProgress(progress + 1);
            }
        });

        /* Set text change watcher */
        progressView.addTextChangedListener(new TextWatcher() {
            Timer timer = new Timer();

            String lastText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                /* do nothing */
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /* do nothing */
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (fromButton) {
                    fromButton = false;
                    return;
                }

                /* Get that progress text */
                final String text = progressView.getText().toString().trim();

                /* Don't schedule a timer if the isn't any real change */
                if (lastText.equals(text)) return;
                lastText = text;

                timer.cancel();
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            int newProgress;

                            @Override
                            public void run() {
                                /* Parse the progress string and use zero instead if it's empty */
                                newProgress = Integer.parseInt(text.length() != 0 ? text : "0");

                                /* Make sure it's not negative */
                                newProgress = Math.max(newProgress, 0);

                                /* Make sure it's not greater that the total progress allowed */
                                if (total != 0 && newProgress > total) newProgress = total;

                                /* Set the new progress (on the UI thread) */
                                progressView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        setProgress(newProgress);
                                    }
                                });
                            }
                        },
                        500
                );
            }
        });

        /* Read view attributes */
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs, R.styleable.ProgressView, defStyle, defStyleRes
        );

        boolean showTotal;
        try {
            showTotal = a.getBoolean(R.styleable.ProgressView_pv_showTotal, false);
        } finally {
            a.recycle();
        }

        /* Apply read values */
        showTotal(showTotal);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.progress = progress;
        savedState.total = total;
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        this.progress = savedState.progress;
        this.total = savedState.total;
    }

    public void setListener(OnProgressChangedListener listener) {
        this.listener = listener;
    }

    public void setProgress(final int progress) {
        if (progress < 0) return;
        if (total != 0 && progress > total) return;

        this.progress = progress;
        progressView.setText(String.valueOf(progress));
        notifyProgressChanged();
    }

    public int getProgress() {
        return progress;
    }

    @SuppressLint("SetTextI18n")
    public void setTotal(int total) {
        if (total <= 0)
            throw new IllegalArgumentException("Total cannot be less than or equal to zero");

        this.total = total;
        fromButton = true;
        totalView.setText("/ " + total);
    }

    public int getTotal() {
        return total;
    }

    public void showTotal(boolean show) {
        totalView.setVisibility(show ? VISIBLE : GONE);
    }

    private void notifyProgressChanged() {
        if (listener != null) {
            listener.onProgressChanged(progress);
        }
    }

    public interface OnProgressChangedListener {
        void onProgressChanged(int progress);
    }

    private static class SavedState extends BaseSavedState {
        int progress;
        int total;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.progress = in.readInt();
            this.total = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(progress);
            out.writeInt(total);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

public class ExpandAlphaListView extends ListView {

	private static final String TAG = ExpandAlphaListView.class.getName();

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public ExpandAlphaListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	public ExpandAlphaListView(Context context, AttributeSet attrs, int defStyleAttr) {

		super(context, attrs, defStyleAttr);
		init();
	}

	public ExpandAlphaListView(Context context, AttributeSet attrs) {

		super(context, attrs);
		init();
	}

	public ExpandAlphaListView(Context context) {
		super(context);
		init();
	}

	private float mPosX = 2;
	private OnScrollListener mScrollListener;
	private TransformMode mMode;

	/**
	 * @date 2016. 5. 24.
	 * @Method Name : setPotionOfExpand
	 * @param pos
	 *            set maximum point of list. it ranges bewteen 0 exclusive and 1 inclusive. (0~1]
	 * 
	 */
	public void setPotionOfExpand(float pos) {
		if (pos <= 1.0f && pos > 0) {
			mPosX = 1 / pos;
		}
	}

	/**
	 * @date 2016. 5. 24.
	 * @Method Name : setTransformType
	 * @param mode
	 *            set Transform mode. you can disable both alpha and scale, or one of them with mode
	 */
	public void setTransformType(TransformMode mode) {
		if (mode == null) {
			return;
		} else {
			this.mMode = mode;
		}

	}

	private void init() {

		mMode = new TransformMode(true, true);

		mScrollListener = new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				for (int i = 0; i < visibleItemCount; i++) {
					View v = view.getChildAt(i);
					if (v != null) {

						float scaleFactor;
						int[] location = new int[2];
						v.getLocationOnScreen(location);
						int parentHeight = view.getHeight();

						scaleFactor = 1.0f - ((float) Math.abs(parentHeight / mPosX - location[1]) / (float) parentHeight);
						if (mMode != null) {
							mMode.transform(v, scaleFactor);
						}
					}
				}
			}
		};
		setOnScrollListener(mScrollListener);
	}

	public static class TransformMode {
		private boolean alpha;
		private boolean scale;

		public TransformMode(boolean alpha, boolean scale) {
			this.alpha = alpha;
			this.scale = scale;
		}

		public TransformMode(boolean alpha) {
			this(alpha, true);
		}

		public TransformMode() {
			this(true, true);
		}

		void transform(View view, float value) {
			if (view != null) {
				if (alpha) {
					view.setAlpha(value);
				}
				if (scale) {
					view.setScaleX(value);
					view.setScaleY(value);
				}
			} else {
				Log.d(TAG, "View is null!");
				return;
			}
		}

	}

}

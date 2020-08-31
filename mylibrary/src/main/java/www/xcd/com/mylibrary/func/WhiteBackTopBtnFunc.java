package www.xcd.com.mylibrary.func;

import android.app.Activity;
import android.view.View;

import www.xcd.com.mylibrary.R;


public class WhiteBackTopBtnFunc extends BaseTopImageBtnFunc {

	public WhiteBackTopBtnFunc(Activity activity) {
		super(activity);
	}

	@Override
	public int getFuncId() {
		return R.id.topbar_back;
	}

	@Override
	public int getFuncIcon() {
		return R.drawable.selector_back_btn_white;
	}

	@Override
	public void onclick(View v) {
		getActivity().finish();
	}

}

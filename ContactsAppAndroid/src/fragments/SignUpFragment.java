package fragments;



import com.parse.starter.R;

//import android.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.app.Fragment;
//import android.support.v4.app.Fragment;

public class SignUpFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view = inflater.inflate(R.layout.fragment_signup, container, false);
		return view;
		//return super.onCreateView(inflater, container, savedInstanceState);
	}

}
package elapor.application.com.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import elapor.application.com.elapor.MainActivity;
import elapor.application.com.elapor.R;

public class PelanggaranFragment extends Fragment {

	public static Button buatlaporan;
	public static ListView listview;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.fragment_pelanggaran, container, false);

		buatlaporan = rootView.findViewById(R.id.buatlaporan);
		listview = rootView.findViewById(R.id.listview);

		listview.setAdapter(MainActivity.pelanggaranAdapter);

		buatlaporan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((MainActivity) getActivity()).buatLaporanPelanggaran();
			}
		});

		//((MainActivity) getActivity()).LoadDataPelanggaran();

		return rootView;
	}
}

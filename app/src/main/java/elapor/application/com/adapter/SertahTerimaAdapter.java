package elapor.application.com.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import elapor.application.com.elapor.MainActivity;
import elapor.application.com.model.serahterima;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import customfonts.MyTextView;
import elapor.application.com.elapor.R;

public class SertahTerimaAdapter extends BaseAdapter {

	ArrayList<serahterima> listdata = new ArrayList<>();
	Context context;

	public SertahTerimaAdapter(Context context, ArrayList<serahterima> listdata) {
		this.context = context;
		this.listdata = listdata;
	}

	public void UpdateSerahterimaAdapter(ArrayList<serahterima> listdata) {
		this.listdata = listdata;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return listdata.size();
	}

	@Override
	public Object getItem(int position) {
		return listdata.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {
		public ImageView image;
		public MyTextView tanggal;
		public MyTextView keterangan;
		public Button open;
		public Button delete;

		public int position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {

		final ViewHolder view;
		LayoutInflater inflator =  LayoutInflater.from(parent.getContext());
		if(convertView==null) {
			view = new ViewHolder();
			convertView = inflator.inflate(R.layout.serahterima_item_list, null);

			view.image      = convertView.findViewById(R.id.image);
			view.tanggal    = convertView.findViewById(R.id.tanggaljam);
			view.keterangan = convertView.findViewById(R.id.keterangan);
			view.open       = convertView.findViewById(R.id.open);
			view.delete     = convertView.findViewById(R.id.delete);

			convertView.setTag(view);
		} else {
			view = (ViewHolder) convertView.getTag();
		}

		final serahterima info = listdata.get(position);
		view.position = listdata.indexOf(info);

		File foto = new File(info.getFoto());
		view.image.setImageURI(Uri.fromFile(foto));

		String[] daysArray = new String[] {"Sabtu","Minggu","Senin","Selasa","Rabu","Kamis","Jum'at"};
		int dayOfWeek = 0;
		String hari = "";
		String tanggal = "";
		String jam = "";

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat newDateFormatter = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat newTimeFormatter = new SimpleDateFormat("HH:mm");

		Date date = new Date();
		try {
			date    = formatter.parse(info.getTanggaljam());
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			dayOfWeek = c.get(Calendar.DAY_OF_WEEK)-1;
			if (dayOfWeek < 0) {
				dayOfWeek += 7;
			}

			hari    = daysArray[dayOfWeek];
			tanggal = newDateFormatter.format(date);
			jam     = newTimeFormatter.format(date);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		view.tanggal.setText(hari+", "+tanggal+" jam "+jam+" WIB");
		view.keterangan.setText("Serah Terima Piket "+info.getPiket_dari()+"  "+info.getRegu_dari()+" ke Piket "+info.getPiket_kepada()+"  "+info.getRegu_kepada()+".");

		view.open.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				File file = new File(info.getFile());
				((MainActivity) parent.getContext()).openFile(file);
			}
		});

		view.delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				((MainActivity) parent.getContext()).deleteSerahterima(info);
			}
		});

		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View convertView) {

			}
		});

		return convertView;
	}
}

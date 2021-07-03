package elapor.application.com.elapor;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import customfonts.MyEditText;
import customfonts.MyTextView;
import elapor.application.com.libs.CommonUtilities;
import elapor.application.com.libs.DatabaseHandler;
import elapor.application.com.libs.GalleryFilePath;
import elapor.application.com.model.dataitem;
import elapor.application.com.model.serahterima;

public class BuatLaporanSerahTerimaActivity extends AppCompatActivity {

	Context context;
	DatabaseHandler dh;
	serahterima dataserahterima;

	final int REQUEST_FROM_GALLERY = 5;
	final int REQUEST_FROM_CAMERA  = 6;
	private static Uri mImageCaptureUri;

	ImageView back;

	RadioGroup radioGroupDariPiket, radioGroupDariRegu,radioGroupKepadaPiket, radioGroupKepadaRegu, radioGroupSimpanSebagai;
	MyEditText rupam, p2u, kplp, perawat, satops1, satops2;
	CheckBox chkbox_rupam, chkbox_p2u, chkbox_kplp, chkbox_perawat, chkbox_satops1, chkbox_satops2;
	MyEditText ket_rupam, ket_p2u, ket_kplp, ket_perawat, ket_satops1, ket_satops2;

	LinearLayout layout_ket_rupam, layout_ket_p2u, layout_ket_kplp, layout_ket_perawat, layout_ket_satops1, layout_ket_satops2;
	MyEditText narapidana, tahanan, pertanian_ngajum, rssa, rsud;
	MyEditText file_foto, keterangan;
	MyTextView upload, save;

	String piket_dari, piket_kepada;
	String regu_dari, regu_kepada;
	String save_as = "PDF";

	Dialog dialog_pilih_gambar;
	MyTextView from_camera, from_galery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
		StrictMode.setVmPolicy(builder.build());
		builder.detectFileUriExposure();

		setContentView(R.layout.activity_add_laporan_serahterima);

		context = BuatLaporanSerahTerimaActivity.this;
		dh = new DatabaseHandler(context);

		back                  = findViewById(R.id.back);
		radioGroupDariPiket   = findViewById(R.id.radioGroupDariPiket);
		radioGroupDariRegu    = findViewById(R.id.radioGroupDariRegu);
		radioGroupKepadaPiket = findViewById(R.id.radioGroupKepadaPiket);
		radioGroupKepadaRegu  = findViewById(R.id.radioGroupKepadaRegu);

		rupam = findViewById(R.id.edit_rupam);
		p2u = findViewById(R.id.edit_p2u);
		kplp = findViewById(R.id.edit_kplp);
		perawat = findViewById(R.id.edit_perawat);
		satops1 = findViewById(R.id.edit_satops1);
		satops2 = findViewById(R.id.edit_satops2);

		chkbox_rupam   = findViewById(R.id.chkbox_rupam);
		chkbox_p2u     = findViewById(R.id.chkbox_p2u);
		chkbox_kplp    = findViewById(R.id.chkbox_kplp);
		chkbox_perawat = findViewById(R.id.chkbox_perawat);
		chkbox_satops1 = findViewById(R.id.chkbox_satops1);
		chkbox_satops2 = findViewById(R.id.chkbox_satops2);

		ket_rupam   = findViewById(R.id.edit_keterangan_rupam);
		ket_p2u     = findViewById(R.id.edit_keterangan_p2u);
		ket_kplp    = findViewById(R.id.edit_keterangan_kplp);
		ket_perawat = findViewById(R.id.edit_keterangan_perawat);
		ket_satops1 = findViewById(R.id.edit_keterangan_satops1);
		ket_satops2 = findViewById(R.id.edit_keterangan_satops2);

		layout_ket_rupam   = findViewById(R.id.layout_ket_rupam);
		layout_ket_p2u     = findViewById(R.id.layout_ket_p2u);
		layout_ket_kplp    = findViewById(R.id.layout_ket_kplp);
		layout_ket_perawat = findViewById(R.id.layout_ket_perawat);
		layout_ket_satops1 = findViewById(R.id.layout_ket_satops1);
		layout_ket_satops2 = findViewById(R.id.layout_ket_satops2);

		narapidana       = findViewById(R.id.edit_narapidana);
		tahanan          = findViewById(R.id.edit_tahanan);
		pertanian_ngajum = findViewById(R.id.edit_pertanian_ngajum);
		rssa             = findViewById(R.id.edit_rssa);
		rsud             = findViewById(R.id.edit_rsud);

		file_foto  = findViewById(R.id.edit_filename);
		keterangan = findViewById(R.id.edit_keterangan);


		radioGroupSimpanSebagai  = findViewById(R.id.radioGroupSimpanSebagai);

		upload  = findViewById(R.id.upload);
		save    = findViewById(R.id.save);

		dialog_pilih_gambar = new Dialog(context);
		dialog_pilih_gambar.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog_pilih_gambar.setCancelable(true);
		dialog_pilih_gambar.setContentView(R.layout.pilih_gambar_dialog);

		from_galery = dialog_pilih_gambar.findViewById(R.id.txtFromGalley);
		from_galery.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog_pilih_gambar.dismiss();
				fromGallery();
			}
		});

		from_camera = dialog_pilih_gambar.findViewById(R.id.txtFromCamera);
		from_camera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog_pilih_gambar.dismiss();
				fromCamera();
			}
		});

		chkbox_rupam.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean isVisible = ((CheckBox) v).isChecked();
				layout_ket_rupam.setVisibility(isVisible?View.VISIBLE:View.GONE);
				ket_rupam.requestFocus();
			}
		});

		chkbox_p2u.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean isVisible = ((CheckBox) v).isChecked();
				layout_ket_p2u.setVisibility(isVisible?View.VISIBLE:View.GONE);
				ket_p2u.requestFocus();
			}
		});

		chkbox_kplp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean isVisible = ((CheckBox) v).isChecked();
				layout_ket_kplp.setVisibility(isVisible?View.VISIBLE:View.GONE);
				ket_kplp.requestFocus();
			}
		});

		chkbox_perawat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean isVisible = ((CheckBox) v).isChecked();
				layout_ket_perawat.setVisibility(isVisible?View.VISIBLE:View.GONE);
				ket_perawat.requestFocus();
			}
		});

		chkbox_satops1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean isVisible = ((CheckBox) v).isChecked();
				layout_ket_satops1.setVisibility(isVisible?View.VISIBLE:View.GONE);
				ket_satops1.requestFocus();
			}
		});

		chkbox_satops2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean isVisible = ((CheckBox) v).isChecked();
				layout_ket_satops2.setVisibility(isVisible?View.VISIBLE:View.GONE);
				ket_satops2.requestFocus();
			}
		});

		radioGroupDariPiket.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// checkedId is the RadioButton selected
				RadioButton rb = (RadioButton) findViewById(checkedId);
				piket_dari = rb.getText().toString().trim();
			}
		});

		radioGroupDariRegu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// checkedId is the RadioButton selected
				RadioButton rb = (RadioButton) findViewById(checkedId);
				regu_dari = rb.getText().toString().trim();

			}
		});

		radioGroupKepadaPiket.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// checkedId is the RadioButton selected
				RadioButton rb = (RadioButton) findViewById(checkedId);
				piket_kepada = rb.getText().toString().trim();
			}
		});

		radioGroupKepadaRegu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// checkedId is the RadioButton selected
				RadioButton rb = (RadioButton) findViewById(checkedId);
				regu_kepada = rb.getText().toString().trim();
			}
		});

		radioGroupSimpanSebagai.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// checkedId is the RadioButton selected
				RadioButton rb = (RadioButton) findViewById(checkedId);
				save_as = rb.getText().toString().trim();
			}
		});

		upload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectImage();
			}
		});

		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				prosesSimpan(save_as);
			}
		});
	}

	public void prosesSimpan(String tipe) {

		/*dataserahterima = new serahterima(
				1,
				"2021-06-24 19:00",
				"siang",
				"(D)",
				"malam",
				"(C)",
				18,
				13,
				5,
				0,
				1,
				4,
				"1 Orang Hadir Lengkap",
				"15 Orang Hadir, 3 Orang a.n NURUL HIDAYAT (cuti), CHANDRA (Cuti), dan HADI M (Sakit).",
				"1 Orang Hadir Lengkap",
				"12 Orang Hadir Lengkap",
				"1 Orang Hadir Lengkap",
				"4 Orang Hadir Lengkap",
				2768,
				562,
				2,
				2,
				0,
				GalleryFilePath.getPath(context, mImageCaptureUri),
				"Jajaran Rupam C melaksanakan Cek Fisik Mulai dari Blok Brawijaya Sampai Blok Cendrawasih Situasi aman dan LEngkap.\n" +
						"Serah terima tugas penjagaan dari rupam siang (D) ke rupam malam (C) dengan jumlah penghuni 3.3.32 Orang lengkap dan aman.\n" +
						"2 WBP an. Ali Muchtarom Rawat Inap RSSA\n" +
						"Pukul 19.00 WIB 7 WBP an. Nurkholis masih lembur kafe jagonan jali\n" +
						"Lapas Kelas 1 Malang dalam keadaan lengkap, kondusif, aman, tertib dan terkendali."
		);*/

		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String tanggaljam = formatter.format(new Date());

			if (piket_kepada.equalsIgnoreCase("Pagi")) {
				tanggaljam += " 06:00";
			} else if (piket_kepada.equalsIgnoreCase("Siang")) {
				tanggaljam += " 13:00";
			} else if (piket_kepada.equalsIgnoreCase("Malam")) {
				tanggaljam += " 19:00";
			}

			dataserahterima = new serahterima(
					0,
					tanggaljam,

					piket_dari,
					regu_dari,
					piket_kepada,
					regu_kepada,

					Integer.parseInt(rupam.getText().toString()),
					Integer.parseInt(p2u.getText().toString()),
					Integer.parseInt(kplp.getText().toString()),
					Integer.parseInt(perawat.getText().toString()),
					Integer.parseInt(satops1.getText().toString()),
					Integer.parseInt(satops2.getText().toString()),

					chkbox_rupam.isChecked() ? ket_rupam.getText().toString() : (CommonUtilities.getNumberFormat(Integer.parseInt(rupam.getText().toString())) + " Orang Hadir Lengkap"),
					chkbox_p2u.isChecked() ? ket_p2u.getText().toString() : (CommonUtilities.getNumberFormat(Integer.parseInt(p2u.getText().toString())) + " Orang Hadir Lengkap"),
					chkbox_kplp.isChecked() ? ket_kplp.getText().toString() : (CommonUtilities.getNumberFormat(Integer.parseInt(kplp.getText().toString())) + " Orang Hadir Lengkap"),
					chkbox_perawat.isChecked() ? ket_perawat.getText().toString() : (CommonUtilities.getNumberFormat(Integer.parseInt(perawat.getText().toString())) + " Orang Hadir Lengkap"),
					chkbox_satops1.isChecked() ? ket_satops1.getText().toString() : (CommonUtilities.getNumberFormat(Integer.parseInt(satops1.getText().toString())) + " Orang Hadir Lengkap"),
					chkbox_satops2.isChecked() ? ket_satops2.getText().toString() : (CommonUtilities.getNumberFormat(Integer.parseInt(satops2.getText().toString())) + " Orang Hadir Lengkap"),

					Integer.parseInt(narapidana.getText().toString()),
					Integer.parseInt(tahanan.getText().toString()),
					Integer.parseInt(pertanian_ngajum.getText().toString()),
					Integer.parseInt(rssa.getText().toString()),
					Integer.parseInt(rsud.getText().toString()),

					GalleryFilePath.getPath(context, mImageCaptureUri),
					keterangan.getText().toString()
			);

			String nama = new SimpleDateFormat("'st-'yyyyMMddhhmmss'." + save_as.toLowerCase() + "'").format(new Date());
			String pathfile = "";
			try {
				pathfile = generateReport(nama, tipe);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			if (pathfile.length() > 0) {
				dataserahterima.setFile(pathfile);
				dh.serahterimaInsertData(dataserahterima);

				Intent intent = new Intent();
				intent.putExtra("path", pathfile);
				setResult(RESULT_OK, intent);
				finish();

				Toast.makeText(context, "Buat Laporan Serah Terima Berhasil.", Toast.LENGTH_LONG).show();
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectImage() {
		dialog_pilih_gambar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog_pilih_gambar.show();
	}

	private void fromGallery() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, REQUEST_FROM_GALLERY);
	}

	private void fromCamera() {

		Intent intent = new Intent(context, AmbilFotoActivity.class);
		startActivityForResult(intent, REQUEST_FROM_CAMERA);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data_intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data_intent);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {

				case REQUEST_FROM_CAMERA: {
					File file = new File(data_intent.getStringExtra("path"));
					if(file.exists()) {
						mImageCaptureUri = Uri.fromFile(file);
						Log.e("PARDEDE", file.getAbsolutePath());
						file_foto.setText(file.getName());
					}

					break;
				}
				case REQUEST_FROM_GALLERY: {
					File file = new File(GalleryFilePath.getPath(context, data_intent.getData()));
					if(file.exists()) {
						mImageCaptureUri = Uri.fromFile(file);
						Log.e("PARDEDE", file.getAbsolutePath());
						file_foto.setText(file.getName());
					}

					break;
				}
			}
		}
	}


	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	    super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	private String generateReport(String nama, String tipe) throws FileNotFoundException {

		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), nama);
		Uri uriFile = Uri.fromFile(file);

		//To create a PDF with portrait A4 pages, therefore, you can define the page descriptions like this:
		//PageInfo pageInfo = new PageInfo.Builder(595, 842, 1).create();
		//and for a PDF with landscape A4 pages, you can define them like this:
		//PageInfo pageInfo = new PageInfo.Builder(842, 595, 1).create();

		int pageWidth = 612;
		int pageHeight = 1008;

		//foto attachment:
		File fl_foto = new File(dataserahterima.getFoto());
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap foto = BitmapFactory.decodeStream(new FileInputStream(fl_foto), null, options);
		float aspectRatio = foto.getWidth() / (float) foto.getHeight();
		int newWidth = pageWidth-105;
		int newHeight = Math.round(newWidth / aspectRatio);

		float foto_scaleWidth = ((float) newWidth) / foto.getWidth();
		float foto_scaleHeight = ((float) newHeight) / foto.getHeight();

		Matrix foto_matrix = new Matrix();
		foto_matrix.postScale(foto_scaleWidth, foto_scaleHeight);

		Bitmap bmp = null;
		Canvas canvas = null;
		PdfDocument pdfDocument = null;
		PdfDocument.PageInfo pageInfos;
		PdfDocument.Page page = null;

		if(tipe.equalsIgnoreCase("PDF")) {
			pdfDocument = new PdfDocument();
			pageInfos = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
			page = pdfDocument.startPage(pageInfos);
			canvas = page.getCanvas();
		} else {
			pageHeight += newHeight + 100;
			Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
			bmp = Bitmap.createBitmap(pageWidth, pageHeight, conf); // this creates a MUTABLE bitmap
			canvas = new Canvas(bmp);
			canvas.drawColor(Color.WHITE);
		}

		//PAGE BACKGROUND
		/*Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.bg_intro);
		float scaleWidth = ((float) pageWidth) / bm.getWidth();
		float scaleHeight = ((float) pageHeight) / bm.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resized = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, false);
		canvas.drawBitmap(resized, 0, 0, null);*/

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setTextSize(14f);
		paint.setColor(Color.parseColor("#000000"));
		paint.setFakeBoldText(true);
		canvas.drawText("LAPORAN ATENSI PIMPINAN", pageWidth/2, 50, paint);

		paint.setTextAlign(Paint.Align.LEFT);
		paint.setTextSize(12f);
		paint.setFakeBoldText(false);

		int x = 45;
		int y = 75;
		int space = 16;
		int maxLength = 90;
		int line = 1;
		String row = "";

		String datetime = dataserahterima.getTanggaljam();

		String[] daysArray = new String[] {"Minggu","Senin","Selasa","Rabu","Kamis","Jum'at","Sabtu"};
		int dayOfWeek = 0;
		String hari = "";
		String tanggal = "";
		String jam = "";

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat newDateFormatter = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat newTimeFormatter = new SimpleDateFormat("HH:mm");

		Date date = new Date();
		try {
			date    = formatter.parse(datetime);
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

		String text = "Kepada.YTH:\n" +
				"1. KALAPAS Lowokwaru.\n" +
				"2. Ka KPLP Lowokwaru.\n\n" +
				"Mohon ijin melaporkan, serah terima tugas Rupam " +
				dataserahterima.getPiket_dari()+" "+dataserahterima.getRegu_dari()+" ke Rupam " +
				dataserahterima.getPiket_kepada()+" "+dataserahterima.getRegu_kepada()+" di Lapas Kelas 1 Malang.";

		String lines[] = text.split("\\r?\\n");
		for(String str: lines) {
			row = "";
			String[] splited = str.split("\\s+");
			for (String word: splited) {
				if((row+" "+word).length()>maxLength) {
					Log.e("PARDEDE", row);
					canvas.drawText(row, x,y+(space*line), paint);
					line++;
					row = "";
				}
				row += word + " ";
			}
			if(row.length()>0) {
				Log.e("PARDEDE", row);
				canvas.drawText(row, x,y+(space*line), paint);
				line++;
			}
		}

		line++;
		paint.setFakeBoldText(true);
		canvas.drawText("URAIAN/PERISTIWA/KEJADIAN.", x,y+(space*line), paint);

		line++;
		text = "Pada hari "+hari+", "+tanggal+" Pukul "+jam+" WIB di Lapas Kelas 1 Malang telah melaksanakan serah terima tugas Rupam " +
				dataserahterima.getPiket_dari()+" "+dataserahterima.getRegu_dari()+" ke Rupam " +
				dataserahterima.getPiket_kepada()+" "+dataserahterima.getRegu_kepada()+".\n" +
				"Kekuatan pengamanan pada saat ini di Lapas Kelas 1 Malang dengan rincian sbb:";

		paint.setFakeBoldText(false);
		lines = text.split("\\r?\\n");
		for(String str: lines) {
			row = "";
			String[] splited = str.split("\\s+");
			for (String word: splited) {
				if((row+" "+word).length()>maxLength) {
					Log.e("PARDEDE", row);
					canvas.drawText(row, x,y+(space*line), paint);
					line++;
					row = "";
				}
				row += word + " ";
			}
			if(row.length()>0) {
				Log.e("PARDEDE", row);
				canvas.drawText(row, x,y+(space*line), paint);
				line++;
			}
			line++;
		}

		line--;
		int jarak = 135;
		int baris = 0;
		String satops_patnal_before = "";
		String satops_patnal_after = "";

		if(dataserahterima.getPiket_dari().equalsIgnoreCase("Pagi")) {
			satops_patnal_before = "Malam";
			satops_patnal_after = "Siang";
		}

		if(dataserahterima.getPiket_dari().equalsIgnoreCase("Siang")) {
			satops_patnal_before = "Pagi";
			satops_patnal_after = "Malam";
		}

		if(dataserahterima.getPiket_dari().equalsIgnoreCase("Malam")) {
			satops_patnal_before = "Siang";
			satops_patnal_after = "Pagi";
		}

		ArrayList<dataitem> kekuatanlist = new ArrayList<>();
		kekuatanlist.add(new dataitem("Rupam", dataserahterima.getRupam(), "Orang", dataserahterima.getKet_rupam()));
		kekuatanlist.add(new dataitem("P2U", dataserahterima.getP2u(), "Orang", dataserahterima.getKet_p2u()));
		kekuatanlist.add(new dataitem("Staff KPLP", dataserahterima.getKplp(), "Orang", dataserahterima.getKet_kplp()));
		kekuatanlist.add(new dataitem("Perawat", dataserahterima.getPerawat(), "Orang", dataserahterima.getKet_perawat()));
		kekuatanlist.add(new dataitem("Satops Patnal "+satops_patnal_before, dataserahterima.getSatops1(), "Orang", dataserahterima.getKet_satops1()));
		kekuatanlist.add(new dataitem("Satops Patnal "+satops_patnal_after, dataserahterima.getSatops2(), "Orang", dataserahterima.getKet_satops2()));

		for (dataitem item: kekuatanlist) {
			baris = y+(space*line);
			paint.setTextAlign(Paint.Align.RIGHT);
			canvas.drawText((item.getJumlah()>0?CommonUtilities.getNumberFormat(item.getJumlah())+" "+item.getSatuan():"-"), x+jarak+90, baris, paint);

			paint.setTextAlign(Paint.Align.LEFT);
			canvas.drawText(item.getNama(), x, baris, paint);
			canvas.drawText(":", x+jarak, baris, paint);
			line++;
		}
		line++;
		paint.setFakeBoldText(true);
		canvas.drawText("Keterangan", x,y+(space*line), paint);

		line++;
		int maxLength2 = 60;
		paint.setFakeBoldText(false);
		for (dataitem item: kekuatanlist) {
			baris = y+(space*line);
			canvas.drawText(item.getNama(), x, baris, paint);
			canvas.drawText(":", x+jarak, baris, paint);

			String str = item.getKeterangan().equalsIgnoreCase("0 Orang Hadir Lengkap")?"-":item.getKeterangan();
			row = "";
			String[] splited = str.split("\\s+");
			for (String word: splited) {
				if((row+" "+word).length()>maxLength2) {
					baris = y+(space*line);
					canvas.drawText(row, x+jarak+10, baris, paint);
					line++;
					row = "";
				}
				row += word + " ";
			}
			if(row.length()>0) {
				baris = y+(space*line);
				canvas.drawText(row, x+jarak+10, baris, paint);
				line++;
			}
		}

		int jumlah = dataserahterima.getNarapidana()+
				dataserahterima.getTahanan()+
				dataserahterima.getPertanian_ngajum()+
				dataserahterima.getRssa()+
				dataserahterima.getRsud();
		ArrayList<dataitem> penghunilist = new ArrayList<>();
		penghunilist.add(new dataitem("Narapidana", dataserahterima.getNarapidana(), "Orang", ""));
		penghunilist.add(new dataitem("Tahanan", dataserahterima.getTahanan(), "Orang", ""));
		penghunilist.add(new dataitem("Pertanian Ngajum", dataserahterima.getPertanian_ngajum(), "Orang", ""));
		penghunilist.add(new dataitem("RSSA", dataserahterima.getRssa(), "Orang", ""));
		penghunilist.add(new dataitem("RSUD", dataserahterima.getRsud(), "Orang", ""));
		penghunilist.add(new dataitem("Jumlah", jumlah, "Orang", ""));

		line++;
		paint.setFakeBoldText(true);
		canvas.drawText("Jumlah Penghuni", x,y+(space*line), paint);

		line++;
		paint.setFakeBoldText(false);
		for (dataitem item: penghunilist) {
			baris = y+(space*line);
			paint.setTextAlign(Paint.Align.RIGHT);
			canvas.drawText((item.getJumlah()>0?CommonUtilities.getNumberFormat(item.getJumlah())+" "+item.getSatuan():"-"), x+jarak+90, baris, paint);

			paint.setTextAlign(Paint.Align.LEFT);
			canvas.drawText(item.getNama(), x, baris, paint);
			canvas.drawText(":", x+jarak+5, baris, paint);
			line++;
		}

		line++;
		paint.setFakeBoldText(true);
		canvas.drawText("Keterangan:", x,y+(space*line), paint);

		line++;
		paint.setFakeBoldText(false);
		text = dataserahterima.getKeterangan()+"\n\nDemikian yang dapat kami sampaikan, selanjutnya mohon petunjuk dan arahan.";

		lines = text.split("\\r?\\n");
		for(String str: lines) {
			row = "";
			String[] splited = str.split("\\s+");
			for (String word: splited) {
				if((row+" "+word).length()>maxLength) {
					Log.e("PARDEDE", row);
					canvas.drawText(row, x,y+(space*line), paint);
					line++;
					row = "";
				}
				row += word + " ";
			}
			if(row.length()>0) {
				Log.e("PARDEDE", row);
				canvas.drawText(row, x,y+(space*line), paint);
				line++;
			}
			//line++;
		}

		x = pageWidth/4;

		line++;
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText("Malang, "+tanggal, x,y+(space*line), paint);
		line++;
		canvas.drawText("Karupam "+dataserahterima.getRegu_kepada(), x,y+(space*line), paint);
		line++;
		line++;
		canvas.drawText("Ttd", x,y+(space*line), paint);
		line++;
		if(dataserahterima.getRegu_kepada().equalsIgnoreCase("(A)")) {
			line++;
			canvas.drawText("UNANG H, SH. ", x,y+(space*line), paint);
			line++;
			canvas.drawText("NIP. 196601061991121001", x,y+(space*line), paint);
		}

		if(dataserahterima.getRegu_kepada().equalsIgnoreCase("(B)")) {
			line++;
			canvas.drawText("SUBARI, SH.", x,y+(space*line), paint);
			line++;
			canvas.drawText("NIP. 196601061991031001", x,y+(space*line), paint);
		}
		if(dataserahterima.getRegu_kepada().equalsIgnoreCase("(C)")) {
			line++;
			canvas.drawText("YUSUF SURYO BROTO, SH. ", x,y+(space*line), paint);
			line++;
			canvas.drawText("NIP.197402132000031001", x,y+(space*line), paint);
		}
		if(dataserahterima.getRegu_kepada().equalsIgnoreCase("(D)")) {
			line++;
			canvas.drawText("RAMLAN ", x,y+(space*line), paint);
			line++;
			canvas.drawText("NIP.196408171987031001", x,y+(space*line), paint);
		}

		//HALAMAN 2 GAMBAR
		if(tipe.equalsIgnoreCase("PDF")) {
			pdfDocument.finishPage(page);

			pageInfos = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 2).create();
			page = pdfDocument.startPage(pageInfos);
			canvas = page.getCanvas();
			line = 0;
		} else {
			line++;
			line++;
			line++;
			line++;
		}

		x = 45;
		paint.setTextAlign(Paint.Align.LEFT);
		paint.setFakeBoldText(true);
		canvas.drawText("FOTO SERAHTERIMA:", x,y+(space*line), paint);

		line++;
		Bitmap foto_resized = Bitmap.createBitmap(foto, 0, 0, foto.getWidth(), foto.getHeight(), foto_matrix, false);
		canvas.drawBitmap(foto_resized, x, y+(space*line), null);

		if(tipe.equalsIgnoreCase("PDF")) {
			pdfDocument.finishPage(page);
			savePdf(uriFile, pdfDocument);
		} else {
			saveJpg(bmp, file);
		}

		//this code will scan the image so that it will appear in your gallery when you open next time
		MediaScannerConnection.scanFile(this, new String[] { file.toString() }, null,
				new MediaScannerConnection.OnScanCompletedListener() {
					public void onScanCompleted(String path, Uri uri) {
						Log.d("appname", "image is saved in gallery and gallery is refreshed.");
					}
				}
		);

		return file.getAbsolutePath();

	}

	private void saveJpg(Bitmap bmp, File file) {

		OutputStream os = null;

		try {
			os = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, os);
			os.flush();
			os.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void savePdf(Uri uriFile, PdfDocument pdfDocument) {

		try{
			BufferedOutputStream stream = new BufferedOutputStream(Objects.requireNonNull(getContentResolver().openOutputStream(uriFile)));
			pdfDocument.writeTo(stream);
			pdfDocument.close();
			stream.flush();
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void openFile(Uri uri) {
		String mime = getContentResolver().getType(uri);
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, mime);
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		startActivity(intent);
	}
}

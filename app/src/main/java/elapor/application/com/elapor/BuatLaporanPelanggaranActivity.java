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
import elapor.application.com.model.pelanggaran;

public class BuatLaporanPelanggaranActivity extends AppCompatActivity {

	Context context;
	DatabaseHandler dh;
	pelanggaran datapelanggaran;

	final int REQUEST_FROM_GALLERY = 5;
	final int REQUEST_FROM_CAMERA  = 6;
	private static Uri mImageCaptureUri;

	ImageView back;

	RadioGroup radioGroupSimpanSebagai;
	MyEditText nama, alamat, pasal, pidana, blok, jenis_pelanggaran;
	MyEditText file_foto, keterangan;
	MyTextView upload, save;


	String save_as = "PDF";

	Dialog dialog_pilih_gambar;
	MyTextView from_camera, from_galery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
		StrictMode.setVmPolicy(builder.build());
		builder.detectFileUriExposure();

		setContentView(R.layout.activity_add_laporan_pelanggaran);

		context = BuatLaporanPelanggaranActivity.this;
		dh = new DatabaseHandler(context);

		back = findViewById(R.id.back);
		
		nama   = findViewById(R.id.edit_nama);
		alamat     = findViewById(R.id.edit_alamat);
		pasal    = findViewById(R.id.edit_pasal);
		pidana = findViewById(R.id.edit_pidana);
		blok = findViewById(R.id.edit_blok);
		jenis_pelanggaran = findViewById(R.id.edit_jenis_pelanggaran);

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

		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String tanggaljam = formatter.format(new Date());

			datapelanggaran = new pelanggaran(
					0,
					tanggaljam,

					nama.getText().toString(),
					alamat.getText().toString(),
					pasal.getText().toString(),
					pidana.getText().toString(),
					blok.getText().toString(),
					jenis_pelanggaran.getText().toString(),

					GalleryFilePath.getPath(context, mImageCaptureUri),
					keterangan.getText().toString()
			);

			String nama = new SimpleDateFormat("'pl-'yyyyMMddhhmmss'." + save_as.toLowerCase() + "'").format(new Date());
			String pathfile = "";
			try {
				pathfile = generateReport(nama, tipe);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			if (pathfile.length() > 0) {
				datapelanggaran.setFile(pathfile);
				dh.pelanggaranInsertData(datapelanggaran);

				Intent intent = new Intent();
				intent.putExtra("path", pathfile);
				setResult(RESULT_OK, intent);
				finish();

				Toast.makeText(context, "Buat Laporan Pelanggaran Berhasil.", Toast.LENGTH_LONG).show();
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
		int pageHeight = 1208;

		//foto attachment:
		File fl_foto = new File(datapelanggaran.getFoto());
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
			//pageHeight += newHeight + 100;
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
		canvas.drawText("LAPORAN PELANGGARAN PELTATIB", pageWidth/2, 50, paint);

		paint.setTextAlign(Paint.Align.LEFT);
		paint.setTextSize(12f);
		paint.setFakeBoldText(false);

		int x = 45;
		int y = 75;
		int space = 16;
		int maxLength = 90;
		int line = 1;
		String row = "";

		String datetime = datapelanggaran.getTanggaljam();

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
				"Mohon ijin melaporkan, pelanggaran PELTATIB di Lapas Kelas 1 Malang.";

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
		text = "Pada hari "+hari+", "+tanggal+" Pukul "+jam+" WIB di Lapas Kelas 1 Malang, melaporkan pelanggaran PELTATIB dengan rincian sbb:";

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

		ArrayList<dataitem> pelanggarlist = new ArrayList<>();
		pelanggarlist.add(new dataitem("Nama", 0, "", datapelanggaran.getNama()));
		pelanggarlist.add(new dataitem("Alamat", 0, "", datapelanggaran.getAlamat()));
		pelanggarlist.add(new dataitem("Pasal", 0, "", datapelanggaran.getPasal()));
		pelanggarlist.add(new dataitem("Pidana", 0, "", datapelanggaran.getPidana()));
		pelanggarlist.add(new dataitem("Blok", 0, "", datapelanggaran.getBlok()));
		pelanggarlist.add(new dataitem("Jenis Pelanggran", 0, "", datapelanggaran.getJenis_pelanggaran()));

		line++;
		paint.setFakeBoldText(true);
		canvas.drawText("Data Pelanggar:", x,y+(space*line), paint);

		line++;
		int maxLength2 = 60;
		paint.setFakeBoldText(false);
		for (dataitem item: pelanggarlist) {
			baris = y+(space*line);
			canvas.drawText(item.getNama(), x, baris, paint);
			canvas.drawText(":", x+jarak, baris, paint);

			String str = item.getKeterangan();
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

		line++;
		paint.setFakeBoldText(true);
		canvas.drawText("Keterangan:", x,y+(space*line), paint);

		line++;
		paint.setFakeBoldText(false);
		text = datapelanggaran.getKeterangan()+"\n\nDemikian yang dapat kami sampaikan, selanjutnya mohon petunjuk dan arahan.";

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

		line++;
		line++;
		x = 45;
		paint.setTextAlign(Paint.Align.LEFT);
		paint.setFakeBoldText(true);
		canvas.drawText("FOTO PELANGGARAN:", x,y+(space*line), paint);

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

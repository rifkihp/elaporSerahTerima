package elapor.application.com.libs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import elapor.application.com.model.pelanggaran;
import elapor.application.com.model.serahterima;

public class DatabaseHandler extends SQLiteOpenHelper {

	Context context;
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAMA = "lapas1malang";

	public static final String TABLE_SERAHTERIMA = "serahterima";
	public static final String TABLE_PELANGGARAN = "pelanggaran";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAMA, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public void createTable() {
		SQLiteDatabase db = this.getWritableDatabase();

		//db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERAHTERIMA);
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SERAHTERIMA + "(" +

				"id INTEGER PRIMARY KEY, " +
				"tanggaljam TEXT, " +

				"piket_dari TEXT, " +
				"regu_dari TEXT, " +
				"piket_kepada TEXT, " +
				"regu_kepada TEXT," +

				"rupam INTEGER," +
				"p2u INTEGER," +
				"kplp INTEGER," +
				"perawat INTEGER," +
				"satops1 INTEGER," +
				"satops2 INTEGER," +

				"ket_rupam TEXT," +
				"ket_p2u TEXT," +
				"ket_kplp TEXT," +
				"ket_perawat TEXT," +
				"ket_satops1 TEXT," +
				"ket_satops2 TEXT," +

				"narapidana INTEGER," +
				"tahanan INTEGER," +
				"pertanian_ngajum INTEGER," +
				"rssa INTEGER," +
				"rsud INTEGER," +

				"foto TEXT," +
				"keterangan TEXT," +

				"file TEXT)");

		//db.execSQL("DROP TABLE IF EXISTS " + TABLE_PELANGGARAN);
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PELANGGARAN + "(" +

				"id INTEGER PRIMARY KEY, " +
				"tanggaljam TEXT, " +

				"nama TEXT," +
				"alamat TEXT," +
				"pasal TEXT," +
				"pidana TEXT," +
				"blok TEXT," +
				"jenis_pelanggaran TEXT," +

				"foto TEXT," +
				"keterangan TEXT," +

				"file TEXT)");

		db.close();
	}

	public void serahterimaInsertData(serahterima data) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put("tanggaljam", data.getTanggaljam());

		values.put("piket_dari", data.getPiket_dari());
		values.put("regu_dari", data.getRegu_dari());
		values.put("piket_kepada", data.getPiket_kepada());
		values.put("regu_kepada", data.getRegu_kepada());

		values.put("rupam", data.getRupam());
		values.put("p2u", data.getP2u());
		values.put("kplp", data.getKplp());
		values.put("perawat", data.getPerawat());
		values.put("satops1", data.getSatops1());
		values.put("satops2", data.getSatops2());

		values.put("ket_rupam", data.getKet_rupam());
		values.put("ket_p2u", data.getKet_p2u());
		values.put("ket_kplp", data.getKet_kplp());
		values.put("ket_perawat", data.getKet_perawat());
		values.put("ket_satops1", data.getKet_satops1());
		values.put("ket_satops2", data.getKet_satops2());

		values.put("narapidana", data.getNarapidana());
		values.put("tahanan", data.getTahanan());
		values.put("pertanian_ngajum", data.getPertanian_ngajum());
		values.put("rssa", data.getRssa());
		values.put("rsud", data.getRsud());

		values.put("foto", data.getFoto());
		values.put("keterangan", data.getKeterangan());

		values.put("file", data.getFile());

		db.insert(TABLE_SERAHTERIMA, null, values);

		db.close();
	}

	public void serahterimaUpdateData(serahterima data) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put("tanggaljam", data.getTanggaljam());

		values.put("piket_dari", data.getPiket_dari());
		values.put("regu_dari", data.getRegu_dari());
		values.put("piket_kepada", data.getPiket_kepada());
		values.put("regu_kepada", data.getRegu_kepada());

		values.put("rupam", data.getRupam());
		values.put("p2u", data.getP2u());
		values.put("kplp", data.getKplp());
		values.put("perawat", data.getPerawat());
		values.put("satops1", data.getSatops1());
		values.put("satops2", data.getSatops2());

		values.put("ket_rupam", data.getKet_rupam());
		values.put("ket_p2u", data.getKet_p2u());
		values.put("ket_kplp", data.getKet_kplp());
		values.put("ket_perawat", data.getKet_perawat());
		values.put("ket_satops1", data.getKet_satops1());
		values.put("ket_satops2", data.getKet_satops2());

		values.put("narapidana", data.getNarapidana());
		values.put("tahanan", data.getTahanan());
		values.put("pertanian_ngajum", data.getPertanian_ngajum());
		values.put("rssa", data.getRssa());
		values.put("rsud", data.getRsud());

		values.put("foto", data.getFoto());
		values.put("keterangan", data.getKeterangan());

		values.put("file", data.getFile());

		db.update(TABLE_SERAHTERIMA, values, "id=?", new String[] { String.valueOf(data.getId())});
		db.close();
	}

	public void serahterimaDeleteData(serahterima data) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SERAHTERIMA, "id=?", new String[] { String.valueOf(data.getId())});
		db.close();
	}

	public ArrayList<serahterima> serahterimaListData() {

		ArrayList<serahterima> result = new ArrayList<>();
		try {
			String sql = "SELECT * FROM " + TABLE_SERAHTERIMA + " ORDER BY id DESC LIMIT 0, 10";
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, null);
			if(cursor.getCount() > 0) {
				cursor.moveToFirst();
				for(int i=0; i<cursor.getCount(); i++) {
					serahterima item = new serahterima(
							cursor.getInt(0),
							cursor.getString(1),

							cursor.getString(2),
							cursor.getString(3),
							cursor.getString(4),
							cursor.getString(5),

							cursor.getInt(6),
							cursor.getInt(7),
							cursor.getInt(8),
							cursor.getInt(9),
							cursor.getInt(10),
							cursor.getInt(11),

							cursor.getString(12),
							cursor.getString(13),
							cursor.getString(14),
							cursor.getString(15),
							cursor.getString(16),
							cursor.getString(17),

							cursor.getInt(18),
							cursor.getInt(19),
							cursor.getInt(20),
							cursor.getInt(21),
							cursor.getInt(22),

							cursor.getString(23),
							cursor.getString(24)
					);
					item.setFile(cursor.getString(25));
					result.add(item);
					cursor.moveToNext();
				}
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public void pelanggaranInsertData(pelanggaran data) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put("tanggaljam", data.getTanggaljam());

		values.put("nama", data.getNama());
		values.put("alamat", data.getAlamat());
		values.put("pasal", data.getPasal());
		values.put("pidana", data.getPidana());
		values.put("blok", data.getBlok());
		values.put("jenis_pelanggaran", data.getJenis_pelanggaran());

		values.put("foto", data.getFoto());
		values.put("keterangan", data.getKeterangan());

		values.put("file", data.getFile());

		db.insert(TABLE_PELANGGARAN, null, values);

		db.close();
	}

	public void pelanggaranUpdateData(pelanggaran data) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put("tanggaljam", data.getTanggaljam());

		values.put("nama", data.getNama());
		values.put("alamat", data.getAlamat());
		values.put("pasal", data.getPasal());
		values.put("pidana", data.getPidana());
		values.put("blok", data.getBlok());
		values.put("jenis_pelanggaran", data.getJenis_pelanggaran());

		values.put("foto", data.getFoto());
		values.put("keterangan", data.getKeterangan());

		values.put("file", data.getFile());

		db.update(TABLE_PELANGGARAN, values, "id=?", new String[] { String.valueOf(data.getId())});
		db.close();
	}

	public void pelanggaranDeleteData(pelanggaran data) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PELANGGARAN, "id=?", new String[] { String.valueOf(data.getId())});
		db.close();
	}

	public ArrayList<pelanggaran> pelanggaranListData() {

		ArrayList<pelanggaran> result = new ArrayList<>();
		try {
			String sql = "SELECT * FROM " + TABLE_PELANGGARAN + " ORDER BY id DESC LIMIT 0, 10";
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, null);
			if(cursor.getCount() > 0) {
				cursor.moveToFirst();
				for(int i=0; i<cursor.getCount(); i++) {
					pelanggaran item = new pelanggaran(
							cursor.getInt(0),
							cursor.getString(1),

							cursor.getString(2),
							cursor.getString(3),
							cursor.getString(4),
							cursor.getString(5),
							cursor.getString(6),
							cursor.getString(7),

							cursor.getString(8),
							cursor.getString(9)
					);
					item.setFile(cursor.getString(10));
					result.add(item);
					cursor.moveToNext();
				}
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}


}

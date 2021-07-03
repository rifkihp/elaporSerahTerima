package elapor.application.com.elapor;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import elapor.application.com.adapter.PelanggaranAdapter;
import elapor.application.com.adapter.SertahTerimaAdapter;
import elapor.application.com.fragment.PelanggaranFragment;
import elapor.application.com.fragment.SerahTerimaFragment;
import elapor.application.com.libs.CommonUtilities;
import elapor.application.com.libs.DatabaseHandler;
import elapor.application.com.model.pelanggaran;
import elapor.application.com.model.serahterima;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {

	final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
	final int REQUEST_FROM_BUAT_LAPORAN_SERAHTERIMA = 6;
	final int REQUEST_FROM_BUAT_LAPORAN_PELANGGARAN = 5;

	Context context;
	DatabaseHandler dh;

	ImageView menu;
	Toolbar toolbar;
	DrawerLayout drawer;
	LinearLayout mDrawerPane, menu_laporan, menu_pengaduan;


	public static int menu_selected = 0;

	// LAPORAN SERAH TERIMA LIST
	public static ArrayList<serahterima> serahterimalist = new ArrayList<>();
	public static SertahTerimaAdapter serahterimaAdapter;

	// LAPORAN PELANGGARAN LIST
	public static ArrayList<pelanggaran> pelanggaranlist = new ArrayList<>();
	public static PelanggaranAdapter pelanggaranAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
		StrictMode.setVmPolicy(builder.build());
		builder.detectFileUriExposure();

        if (Build.VERSION.SDK_INT >= 23) {
            insertDummyContactWrapper();
        }

        context = MainActivity.this;
		dh = new DatabaseHandler(context);
		dh.createTable();

		toolbar = findViewById(R.id.toolbar);
		menu = findViewById(R.id.menu);
		drawer = findViewById(R.id.drawer_layout);
		mDrawerPane = findViewById(R.id.drawerPane);
		menu_laporan = findViewById(R.id.menu_serahterima);
		menu_pengaduan = findViewById(R.id.menu_pengaduan);

		menu_laporan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				displayView(0);
			}
		});

		menu_pengaduan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				displayView(1);
			}
		});

		int width = getResources().getDisplayMetrics().widthPixels;
		width = width - (width / 3);
		DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) mDrawerPane.getLayoutParams();
		params.width = width;
		mDrawerPane.setLayoutParams(params);

		menu.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View view) {
				if (drawer.isDrawerOpen(GravityCompat.START)) {
					drawer.closeDrawer(GravityCompat.START);
				} else {
					drawer.openDrawer(GravityCompat.START);
				}
				closeSoftKeyboard();
			}
		});

		displayView(menu_selected);
	}

	public void displayView(int position) {
		menu_selected = position;
		TextView title = findViewById(R.id.title);
		closeSoftKeyboard();
		Fragment fragment = null;
		drawer.closeDrawer(GravityCompat.START);
		switch (position) {

			case 0: {
				title.setText("LAPORAN SERAH TERIMA");
				serahterimalist = dh.serahterimaListData();
				serahterimaAdapter = new SertahTerimaAdapter(context, serahterimalist);
				fragment = new SerahTerimaFragment();

				break;
			}
			case 1: {
				title.setText("LAPORAN PELANGGARAN");
				pelanggaranlist = dh.pelanggaranListData();
				pelanggaranAdapter = new PelanggaranAdapter(context, pelanggaranlist);
				fragment = new PelanggaranFragment();

				break;
			}
			default:

				break;
		}


		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
		}
	}

	@Override
    protected void onDestroy() {
        super.onDestroy();
    }

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {

		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {

				case REQUEST_FROM_BUAT_LAPORAN_SERAHTERIMA: {
					LoadDataSerahTerima();
					String openfile = intent.getStringExtra("path");
					openFile(new File(openfile));

					break;
				}
				case REQUEST_FROM_BUAT_LAPORAN_PELANGGARAN: {
					LoadDataPelanggaran();
					String openfile = intent.getStringExtra("path");
					openFile(new File(openfile));

					break;
				}
			}
		}
	}

	public void deleteSerahterima(serahterima datast) {
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle("Delete");
		alert.setMessage("Are you sure you want to delete?");
		alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dh.serahterimaDeleteData(datast);
				File file = new File(datast.getFile());
				if(file.exists()) {
					file.delete();
				}
				LoadDataSerahTerima();

				dialog.dismiss();
			}
		});

		alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		alert.show();
	}

	public void deletePelanggaran(pelanggaran datapl) {
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle("Delete");
		alert.setMessage("Are you sure you want to delete?");
		alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dh.pelanggaranDeleteData(datapl);
				File file = new File(datapl.getFile());
				if(file.exists()) {
					file.delete();
				}
				LoadDataSerahTerima();

				dialog.dismiss();
			}
		});

		alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		alert.show();
	}

	public void openFile(File file) {
		if (file.exists()) {
			String extension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".")).toLowerCase();
			displaypdf(file, extension);
		} else {
			Toast.makeText(context, "The file not exists! ", Toast.LENGTH_SHORT).show();
		}
	}

	public void displaypdf(File file, String tipe) {

		//Toast.makeText(context, file.toString() , Toast.LENGTH_LONG).show();
		if(file.exists()) {
			Intent target = new Intent(Intent.ACTION_VIEW);
			if(tipe.equalsIgnoreCase(".pdf")) {
				target.setDataAndType(Uri.fromFile(file), "application/pdf");
			} else if(tipe.equalsIgnoreCase(".jpg")) {
				target.setDataAndType(Uri.fromFile(file), "image/jpg");
			}
			target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

			Intent intent = Intent.createChooser(target, "Open File");
			try {
				startActivity(intent);
			} catch (ActivityNotFoundException e) {
				// Instruct the user to install a PDF reader here, or something
			}
		}
		else
			Toast.makeText(getApplicationContext(), "File path is incorrect." , Toast.LENGTH_LONG).show();
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem item) {

		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
	
	public void LoadDataSerahTerima() {
		serahterimalist = dh.serahterimaListData();
		serahterimaAdapter.UpdateSerahterimaAdapter(serahterimalist);
	}

	public void LoadDataPelanggaran() {
		pelanggaranlist = dh.pelanggaranListData();
		pelanggaranAdapter.UpdatePelanggaranAdapter(pelanggaranlist);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if (menu_selected > 0) {
					displayView(0);
					return false;
				}

				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void openSerahTerima(serahterima data) {
		Toast.makeText(context, "OPEN SERAH TERIMA", Toast.LENGTH_LONG).show();

	}

	void closeSoftKeyboard() {
		View view = getCurrentFocus();
		if (view != null) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	public void buatLaporanSerahTerima() {
		Intent intent = new Intent(context, BuatLaporanSerahTerimaActivity.class);
		startActivityForResult(intent, REQUEST_FROM_BUAT_LAPORAN_SERAHTERIMA);
	}

	public void buatLaporanPelanggaran() {
		Intent intent = new Intent(context, BuatLaporanPelanggaranActivity.class);
		startActivityForResult(intent, REQUEST_FROM_BUAT_LAPORAN_PELANGGARAN);
	}

    private void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<>();
        final List<String> permissionsList = new ArrayList<>();

        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("WRITE_EXTERNAL_STORAGE");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("READ_EXTERNAL_STORAGE");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("CAMERA");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);

                //showMessageOKCancel(message, new DialogInterface.OnClickListener() {
                //@Override
                //public void onClick(DialogInterface dialog, int which) {*/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                }
                //}
                //});
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            return;
        }
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
               	perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
					perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
					perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                    // All Permissions Granted
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    // Permission Denied
                    Toast.makeText(context, "Some Permission is Denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
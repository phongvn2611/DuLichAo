package com.example.virtualtravelapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.virtualtravelapp.R;
import com.example.virtualtravelapp.database.DBManager;
import com.example.virtualtravelapp.fragment.DiaDanhFragment;
import com.example.virtualtravelapp.fragment.ExpFragment;
import com.example.virtualtravelapp.fragment.FavoriteFragment;
import com.example.virtualtravelapp.fragment.SOSFragment;
import com.example.virtualtravelapp.fragment.ToolsFragment;
import com.example.virtualtravelapp.model.DiaDanh;
import com.example.virtualtravelapp.model.Hotel;
import com.example.virtualtravelapp.model.Place;
import com.example.virtualtravelapp.model.Restaurant;
import com.example.virtualtravelapp.model.Vehicle;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.nav_drawer)
	DrawerLayout drawerLayout;
	@BindView(R.id.nav_view)
	NavigationView navigationView;
	@BindView(R.id.nav_frame)
	FrameLayout contentFrame;
	DBManager db;

	//test
	String imString = "";
	Bitmap bitmap;
	static int i = 1;
	static int idVehicel = 1;
	static int idPlace = 1;
	static int idHotel = 1;
	static int idDiadanh = 1;
	static int idRestaurant = 1;

	private static final String PLACE = "p";
	private static final String RESTAURANT = "r";
	private static final String VEHICLE = "v";
	private static final String HOTEL = "h";
	String TAG;
	String chuoi = "";
	ArrayList<DiaDanh> listD;
	boolean doubleBack = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		setUpToolbar();
		setUpNavDrawer();
		startFragment(new DiaDanhFragment());

		//Database
		db = new DBManager(this);
		db.openDataBase();


//		bo thuoc tinh default black icon
		navigationView.setItemIconTintList(null);
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(MenuItem item) {
				item.setCheckable(true);
				switch (item.getItemId()) {
					case R.id.nav_diadanh:
						startFragment(new DiaDanhFragment());
						drawerLayout.closeDrawers();
						return true;
					case R.id.nav_favorite:
						startFragment(new FavoriteFragment());
						drawerLayout.closeDrawers();
						return true;
					case R.id.nav_experience:
						startFragment(new ExpFragment());
						drawerLayout.closeDrawers();
						return true;
					case R.id.nav_sos:
						drawerLayout.closeDrawers();
						Bundle bundle = new Bundle();
						bundle.putInt("TAG", 1);
						SOSFragment fragment = new SOSFragment();
						fragment.setArguments(bundle);
						startFragment(fragment);
						return true;
					case R.id.nav_tools:
						drawerLayout.closeDrawers();
						startFragment(new ToolsFragment());
						return true;
					case R.id.nav_update:
						drawerLayout.closeDrawers();
						showAlertDialog();
						return true;
					case R.id.nav_information:
						drawerLayout.closeDrawers();
						showDialogInfo();
						return true;
					case R.id.nav_send_mail:
						Intent emailIntent = new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", "hungmx94@gmail.com", null));
						emailIntent.putExtra("android.intent.extra.SUBJECT", "Phản hồi");
						startActivity(Intent.createChooser(emailIntent, "Phản hồi"));
						break;
					case R.id.nav_logout:
						Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
						startActivity(logoutIntent);
						Toast.makeText(MainActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
						finish();
						break;

				}
				return false;
			}
		});
	}

	private void setUpToolbar() {
		if (toolbar != null) {
			setSupportActionBar(toolbar);
		}
	}

	private void setUpNavDrawer() {
		if (toolbar != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			toolbar.setNavigationIcon(R.drawable.ic_drawer);
			toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					drawerLayout.openDrawer(GravityCompat.START);
				}
			});

		}
	}

	public void startFragment(Fragment fragment) {
		String fragmentName = fragment.getClass().getName();
		String fragmentTag = fragmentName;

		FragmentManager manager = getSupportFragmentManager();
		boolean fragmentPopp = manager.popBackStackImmediate(fragmentName, 0);

		if (!fragmentPopp && manager.findFragmentByTag(fragmentTag) == null) {
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.replace(R.id.nav_frame, fragment, fragmentTag);
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			transaction.commit();
		}
	}

	public void showAlertDialog() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
				.setIcon(R.drawable.ic_warning)
				.setTitle("Update")
				.setMessage("Bạn đang dùng phiên bản mới nhất")
				.show();
	}

	public void showDialogInfo() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Thông tin")
				.setMessage(Html.fromHtml("<p><strong>App du lịch ảo</strong></p>\n" +
						"<p>Version 1.0</p>\n" +
						"<p><span>Nguyễn Hoàng Huy - 18110122</span></p>\n" +
						"<p><span>Nguyễn Thu Ngân - 18110161</span></p>\n" +
						"<p><span>Võ Ngọc Phong - 18110174</span></p>"));
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})

				.show();
	}

	//ham download image
	private class LoadImageR extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {
			try {
				bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
				imString = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
				publishProgress(imString);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return imString;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			String image;
			Restaurant restaurant = new Restaurant();
			restaurant.setId(idRestaurant++);
			image = values[0];
			restaurant.setImage(image);
			db.editRestaurant(restaurant);

		}

		protected void onPostExecute(String imString) {
			Log.d("imString", imString);
		}

	}

	//ham download image
	private class LoadImageP extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {
			try {
				bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
				imString = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
				publishProgress(imString);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return imString;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			String image;
			Place place = new Place();
			place.setId(idPlace++);
			image = values[0];
			place.setImage(image);
			db.editPlace(place);

		}

		protected void onPostExecute(String imString) {
			Log.d("imString", imString);
		}

	}

	//ham download image
	private class LoadImageV extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {
			try {
				bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
				imString = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
				publishProgress(imString);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return imString;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			Log.d("TAG-image", values[0]);
			String image;
			Vehicle vehicle = new Vehicle();
			vehicle.setId(idVehicel++);
			image = values[0];
			vehicle.setImage(image);
			db.editVehicle(vehicle);

		}

		protected void onPostExecute(String imString) {
			Log.d("imString", imString);
		}

	}

	//ham download image
	private class LoadImageH extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {
			try {
				bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
				imString = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
				publishProgress(imString);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return imString;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			Log.d("TAG-image", values[0]);
			String image;
			Hotel hotel = new Hotel();
			hotel.setId(idHotel++);
			image = values[0];
			hotel.setImage(image);
			db.editHotel(hotel);

		}

		protected void onPostExecute(String imString) {
			Log.d("imString", imString);
		}

	}

	//ham download image
	private class LoadImageD extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {
			try {
				bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
				imString = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
				publishProgress(imString);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return imString;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			Log.d("TAG-image", values[0]);
			String image;
			DiaDanh diaDanh = new DiaDanh();
			diaDanh.setIdDiaDanh(idDiadanh++);
			image = values[0];
			diaDanh.setImage_int(image);
			db.editDiadanh(diaDanh);

		}

		protected void onPostExecute(String imString) {
			Log.d("imString", imString);
		}

	}

	public String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
		ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
		image.compress(compressFormat, quality, byteArrayOS);
		return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
	}

	@Override
	public void onBackPressed() {
		if (doubleBack) {
			super.onBackPressed();
			finish();
			//thoat app ra man hinh chinh'.
			moveTaskToBack(true);
		}
		this.doubleBack = true;
		Toast.makeText(getApplicationContext(), "Nhấn lần nữa để thoát", Toast.LENGTH_SHORT).show();
		//thời gian chờ là 2s
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				doubleBack = false;
			}
		}, 2000);

	}
}

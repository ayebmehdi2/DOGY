package com.ahmed.dogyapp;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ahmed.dogyapp.AUTH.CheckEmailActivity;
import com.ahmed.dogyapp.AUTH.RegisterActivity;
import com.ahmed.dogyapp.USER.AccountActivity;
import com.ahmed.dogyapp.USER.ProfilData;
import com.ahmed.dogyapp.databinding.ActivityHomeBinding;
import com.ahmed.dogyapp.ui.FrgsServices.ChooseAdress;
import com.ahmed.dogyapp.ui.FrgsServices.ChooseServieces;
import com.ahmed.dogyapp.ui.FrgsServices.ChooseSupllier;
import com.ahmed.dogyapp.ui.FrgsServices.ChooseTime;
import com.ahmed.dogyapp.ui.FrgsServices.Finish;
import com.ahmed.dogyapp.ui.rating.RatingshowFragment;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

public class HomeActivity extends AppCompatActivity implements ChooseServieces.clickInChooseServices,
                                ChooseTime.ClickInTime{

    private ProgressDialog PD;

    ActivityHomeBinding binding;
    FirebaseDatabase database;
    DrawerLayout drawer;
    DatabaseReference reference;
    public static Supplier supplier = null;
    private boolean tran = false, hot = false, vet = false;
    private String st = null, et = null, sd = null, ed = null;
    private int STEP = 1;
    public static SERVICES service = new SERVICES();
    public static Bitmap bitmap = null;
    String uid;

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        binding.appBarHome.navHostFragmentContentHome.setVisibility(View.GONE);

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        setupDrawerContent(navigationView);

        binding.appBarHome.menuButton.setOnClickListener(view -> drawer.openDrawer(GravityCompat.START));
        binding.appBarHome.viewPager.disableScroll(true);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        uid = preferences.getString("uid", null);

        reference.child("PROFILES").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ProfilData data = snapshot.getValue(ProfilData.class);
                        if (data == null) return;
                        ImageView imageView = binding.navView.getHeaderView(0).findViewById(R.id.userimg);
                        Glide.with(HomeActivity.this).load(data.getImage()).into(imageView);
                        TextView textView = binding.navView.getHeaderView(0).findViewById(R.id.username);
                        textView.setText(data.getUsername());
                        TextView textView1 = binding.navView.getHeaderView(0).findViewById(R.id.email);
                        textView1.setText(data.getEmail());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });

        binding.navView.getHeaderView(0).setOnClickListener(view ->
                startActivity(new Intent(HomeActivity.this, AccountActivity.class)));

        resetMenu();
        binding.appBarHome.ser.setBackground(getResources().getDrawable(R.drawable.back_tab));
        binding.appBarHome.ser.setTextColor(getResources().getColor(R.color.white));
        binding.appBarHome.des.setText("Choose services");
        checkNext();

        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(this.getSupportFragmentManager(),
                new Fragment[]{new ChooseServieces(), new ChooseSupllier(), new ChooseTime(), new ChooseAdress(), new Finish()});
        binding.appBarHome.viewPager.setAdapter(pagerAdapter);

        binding.appBarHome.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        resetMenu();
                        binding.appBarHome.ser.setBackground(getResources().getDrawable(R.drawable.back_tab));
                        binding.appBarHome.ser.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case 1:
                        resetMenu();
                        binding.appBarHome.sup.setBackground(getResources().getDrawable(R.drawable.back_tab));
                        binding.appBarHome.sup.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case 2:
                        resetMenu();
                        binding.appBarHome.tim.setBackground(getResources().getDrawable(R.drawable.back_tab));
                        binding.appBarHome.tim.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case 3:
                        resetMenu();
                        binding.appBarHome.add.setBackground(getResources().getDrawable(R.drawable.back_tab));
                        binding.appBarHome.add.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case 4:
                        resetMenu();
                        binding.appBarHome.fin.setBackground(getResources().getDrawable(R.drawable.back_tab));
                        binding.appBarHome.fin.setTextColor(getResources().getColor(R.color.white));
                        break;
                    default:
                        resetMenu();
                        binding.appBarHome.ser.setBackground(getResources().getDrawable(R.drawable.back_tab));
                        binding.appBarHome.ser.setTextColor(getResources().getColor(R.color.white));
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        binding.appBarHome.viewPager.setCurrentItem(0);

        binding.appBarHome.next.setOnClickListener(view -> {

            if (STEP == 1){
                if (tran || hot || vet){
                    service.setTraning(tran);
                    service.setHotel(hot);
                    service.setVetirinary(vet);
                    binding.appBarHome.back.setVisibility(View.VISIBLE);
                    binding.appBarHome.viewPager.setCurrentItem(1);
                    STEP = 10;
                    binding.appBarHome.des.setText("Choose a Supllier ");
                    binding.appBarHome.back.setOnClickListener(view1 -> {
                        binding.appBarHome.viewPager.setCurrentItem(0);
                        binding.appBarHome.des.setText("Choose services");
                        binding.appBarHome.back.setVisibility(View.GONE);
                        STEP = 1;
                    });
                }else {
                    Toast.makeText(HomeActivity.this, "Please select services to continue !", Toast.LENGTH_SHORT).show();;
                }
            }else if(STEP == 10){
                if (supplier != null){
                    service.setSupllierId(supplier.getEmail());
                    binding.appBarHome.viewPager.setCurrentItem(2);
                    binding.appBarHome.des.setText("Set date and time of services ");
                    STEP = 2;
                    binding.appBarHome.back.setOnClickListener(view19 -> {
                        binding.appBarHome.back.setVisibility(View.VISIBLE);
                        binding.appBarHome.viewPager.setCurrentItem(1);
                        STEP = 10;
                        binding.appBarHome.des.setText("Choose a Supllier ");
                        binding.appBarHome.back.setOnClickListener(view17 -> {
                            binding.appBarHome.viewPager.setCurrentItem(0);
                            binding.appBarHome.des.setText("Choose services");
                            binding.appBarHome.back.setVisibility(View.GONE);
                            STEP = 1;
                        });
                    });

                }else{
                    Toast.makeText(HomeActivity.this, "Please choose a supllier to continue !", Toast.LENGTH_SHORT).show();;
                }
            }else if (STEP == 2){
                 if ((st != null && et != null) && (sd != null || ed != null)){
                     String time = st + " - " + et;
                     service.setTime(time);
                     String date;
                     if (sd == null) date = ed;
                     else if (ed == null) date = sd;
                     else date = sd + " - " + ed;
                     service.setDate(date);
                     binding.appBarHome.viewPager.setCurrentItem(3);
                     STEP = 3;
                     binding.appBarHome.des.setText("Give us your location");
                     binding.appBarHome.back.setOnClickListener(view12 -> {
                         binding.appBarHome.viewPager.setCurrentItem(2);
                         binding.appBarHome.des.setText("Set date and time of services ");
                         STEP = 2;
                         binding.appBarHome.back.setOnClickListener(view18 -> {
                             binding.appBarHome.viewPager.setCurrentItem(1);
                             binding.appBarHome.des.setText("Choose a Supllier");
                             STEP = 10;
                         binding.appBarHome.back.setOnClickListener(view13 -> {
                             binding.appBarHome.viewPager.setCurrentItem(0);
                             binding.appBarHome.des.setText("Choose services");
                             binding.appBarHome.back.setVisibility(View.GONE);
                             STEP = 1;
                         });
                         });
                     });
                 }else {
                     Toast.makeText(HomeActivity.this, "Please select date and time to continue !", Toast.LENGTH_SHORT).show();;
                 }
            }else if(STEP == 3){
                if (checkStr(ChooseAdress.adress) && checkStr(ChooseAdress.city) &&
                        checkStr(ChooseAdress.state) && checkStr(ChooseAdress.country) &&
                        checkStr(ChooseAdress.number) ){
                    String adress = ChooseAdress.adress.getText().toString();
                    String city = ChooseAdress.city.getText().toString();
                    String state = ChooseAdress.state.getText().toString();
                    String country = ChooseAdress.country.getText().toString();
                    String Adress = adress + ", " + city + "\n" + state + ", " + country;
                    String phone = ChooseAdress.number.getText().toString();
                    service.setAdress(Adress);
                    service.setNumber(phone);
                    binding.appBarHome.viewPager.setCurrentItem(2);
                    binding.appBarHome.viewPager.setCurrentItem(4);
                    STEP = 4;
                    binding.appBarHome.des.setText("Verify you informations");
                    binding.appBarHome.txtNext.setText("Send");
                    binding.appBarHome.back.setOnClickListener(view14 -> {
                        binding.appBarHome.viewPager.setCurrentItem(3);
                        binding.appBarHome.des.setText("Give us your location");
                        STEP = 3;
                        binding.appBarHome.txtNext.setText("Send");
                        binding.appBarHome.back.setOnClickListener(view15 -> {
                            binding.appBarHome.viewPager.setCurrentItem(2);
                            binding.appBarHome.des.setText("Set date and time of services ");
                            STEP = 2;
                            binding.appBarHome.back.setOnClickListener(view18 -> {
                                binding.appBarHome.viewPager.setCurrentItem(1);
                                binding.appBarHome.des.setText("Choose a Supllier");
                                STEP = 10;
                                binding.appBarHome.back.setOnClickListener(view16 -> {
                                    binding.appBarHome.viewPager.setCurrentItem(0);
                                    binding.appBarHome.des.setText("Choose services");
                                    binding.appBarHome.back.setVisibility(View.GONE);
                                    STEP = 1;
                                });
                            });
                        });
                    });
                }else {
                    Toast.makeText(HomeActivity.this, "Please tell us your address to continue !", Toast.LENGTH_SHORT).show();;
                }

                }else if (STEP == 4){

                isStoragePermissionGranted();

            }

        });

    }

    public boolean checkStr(EditText a){
        return a.getText().toString().length() > 0;
    }

    public void resetMenu(){
        binding.appBarHome.ser.setBackgroundColor(0);
        binding.appBarHome.sup.setBackgroundColor(0);
        binding.appBarHome.tim.setBackgroundColor(0);
        binding.appBarHome.add.setBackgroundColor(0);
        binding.appBarHome.fin.setBackgroundColor(0);

        binding.appBarHome.ser.setTextColor(getResources().getColor(R.color.txt_clr));
        binding.appBarHome.tim.setTextColor(getResources().getColor(R.color.txt_clr));
        binding.appBarHome.add.setTextColor(getResources().getColor(R.color.txt_clr));
        binding.appBarHome.fin.setTextColor(getResources().getColor(R.color.txt_clr));
        binding.appBarHome.sup.setTextColor(getResources().getColor(R.color.txt_clr));

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void checkNext(){
        if (tran || hot || vet){
            binding.appBarHome.txtNext.setBackgroundColor(0);
            binding.appBarHome.txtNext.setTextColor(getResources().getColor(R.color.white));
        }else {
            binding.appBarHome.txtNext.setBackground(getResources().getDrawable(R.drawable.back_white));
            binding.appBarHome.txtNext.setTextColor(getResources().getColor(R.color.txt_clr));
        }
    }

    @Override
    public void clickT(boolean t) {
        tran = t;
        checkNext();
    }

    @Override
    public void clickH(boolean h) {
        hot = h;
        checkNext();
    }

    @Override
    public void clickV(boolean v) {
        vet = v;
        checkNext();
    }

    @Override
    public void startTime(String st) {
        this.st = st;
    }

    @Override
    public void endTime(String et) {
        this.et = et;
    }

    @Override
    public void startDate(String sd) {
        this.sd = sd;
    }

    @Override
    public void endDate(String ed) {
        this.ed = ed;
    }

    private static class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private final Fragment[] fragments;
        public ScreenSlidePagerAdapter(FragmentManager fm, Fragment[] fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return 5;
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
        switch(menuItem.getItemId()) {

            case R.id.services:
                fragmentClass=null;
                break;

            case R.id.rating:
                fragmentClass = RatingshowFragment.class;
                break;

            case R.id.visite:
                startActivity(new Intent(HomeActivity.this, AccountActivity.class));
                break;

            case R.id.about:

                break;

            case R.id.logout:
                SharedPreferences.Editor preference = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).edit();
                preference.putString("uid", null);
                preference.apply();
                //FirebaseMessaging.getInstance().unsubscribeFromTopic("user_" + uid).addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "Unsubscribe Success", Toast.LENGTH_LONG).show());
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, SplachActivity.class));
                drawer.closeDrawer(GravityCompat.START);

                break;    
            default:
                fragmentClass = null;
        }

        if (fragmentClass != null){
            binding.appBarHome.navHostFragmentContentHome.setVisibility(View.VISIBLE);
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
            menuItem.setChecked(true);
        }else {
            binding.appBarHome.navHostFragmentContentHome.setVisibility(View.GONE);
        }

        drawer.closeDrawers();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        if (item.getItemId() == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed () {
        DrawerLayout drawer = findViewById(R.id. drawer_layout ) ;
        if (drawer.isDrawerOpen(GravityCompat. START )) {
            drawer.closeDrawer(GravityCompat. START ) ;
        } else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            super .onBackPressed() ;
        }
    }

    public void isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                email(bitmap);
            } else {
                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
            email(bitmap);
        }
    }


    private Uri s = null;
    public void email(Bitmap bitmap){
        try {
            s = saveImage(bitmap, service.getDate());
        } catch (IOException e) {
            e.printStackTrace();

        }

        if (s == null) return;

        Intent i = new Intent(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{service.getSupllierId()});
        i.putExtra(Intent.EXTRA_SUBJECT,"New Client !");
        //Log.d("URI@!@#!#!@##!", Uri.fromFile(pic).toString() + "   " + pic.exists());
        i.putExtra(Intent.EXTRA_STREAM, s);
        i.putExtra("p", s.toString());
        i.setType("image/png");
        startActivityForResult(Intent.createChooser(i,"Share you on the jobing"), 104);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 104) return;
        PD.show();
        uploadImage(s.toString());
        }

    private void uploadImage(String filePath) {

        if (filePath == null) return;
        Bitmap bitmap = null;
        try {
            InputStream inputStream = getContentResolver().openInputStream(Uri.parse(filePath));
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (bitmap == null){
            return;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();

        final StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/"+ UUID.randomUUID().toString());
        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {


            PD.dismiss();
            Toast.makeText(this, "Something Wrong Happend please Try again later !", Toast.LENGTH_SHORT).show();

        }).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {

            String ser = uri.toString();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date date = new Date();
            String dat = formatter.format(date);
            String idSup = supplier.getId();

            PD.dismiss();
            reference.child("PROFILES/"+uid+"/ORDERS/"+dat).setValue(new ODER(dat, ser, idSup));
            s = null;
            startActivity(new Intent(this, WellDone.class));

        }));


    }


    private Uri saveImage(Bitmap bitmap, @NonNull String name) throws IOException {
        boolean saved;
        OutputStream fos;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + "DOGY");
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(imageUri);
            saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            if (saved){
                return imageUri;
            }
            return null;
        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).toString() + File.separator + "DOGY";

            File file = new File(imagesDir);

            if (!file.exists()) {
                file.mkdir();
            }

            File image = new File(imagesDir, name + ".png");
            fos = new FileOutputStream(image);
            saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            fos.flush();
            fos.close();

            if (saved){
                return Uri.parse(image.toString());
            }
            return null;

        }
    }

}
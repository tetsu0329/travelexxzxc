package com.travelex.asus.realtimeproject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.SearchView;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TouristDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    boolean doubleBackToExitPressedOnce = false;
    EditText location, joiner, description, otheritinerary, inclusions;
    DatePicker date;
    LinearLayout layout1, layout2, layout3;
    Button add1, add2, add3, browse1, browse2, browse3, save, browse4, save2;
    ImageView imageView, imageView2, imageView3, imageView4;
    Spinner spinner, places, places2, places3;

    private List<UserAccount> userAccounts;
    private Context context;
    FirebaseAuth auth;
    private static final int RESULT_IMAGE = 1;
    private static final int RESULT_IMAGE2 = 22;
    private static final int RESULT_IMAGE3 = 333;
    Uri uri, uri2, uri3;
    private DatabaseReference mDatabaseRef, mDatabaseRef2, mDatabaseRef3, mDatabaseRef4;
    private StorageReference mStorageRef;


    ProgressDialog progressDialog;
    public static final String FB_STORAGE_PATH = "tours/";
    public static final String FB_DATABASE_PATH = "tourinfo";

    private List<TourDetails3> travelList;
    private List<Joiner> travelList2;

    ListView listView, listView2, listView3;
    TravelViewAdapter travelViewAdapter;
    TravelViewAdapter2 travelViewAdapter2;

    String location2;
    String tourID, userID;

    ListView listView5;
    private List<ItineraryUpload> itineraryList;
    ItineraryViewAdapter itineraryViewAdapter;
    ArrayList<String> selectedStrings;

    private List<UserMessage> inboxList;
    InboxAdapter inboxAdapter;

    private List<Invitation> invitationList;
    ListView listView6;
    InvitationViewAdapter invitationViewAdapter;

    FloatingActionButton fab;
    String layoutstr;
    String uid;

    private List<UserAccount> useracc;
    ListView listView7;
    userViewAdapter userAdapter;

    NotificationCompat.Builder notification;
    private static final int uniqueID = 45612;

    String typess;
    Button browsee, savee;

    TextView textVieww, textVieww2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_dashboard);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);
        mDatabaseRef2 = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        int day3 = today.monthDay;
        int month3 = today.month;
        int year3 = today.year-1900;
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("MMMM dd yyyy");
        Date d3 = new Date(year3, month3, day3);
        final String date3 = dateFormat3.format(d3);

        Query searchinfo3 = mDatabaseRef2.child("tourinfo").orderByChild("tourDate").equalTo(date3);
        searchinfo3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    contactus();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layoutstr = "showtour";

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showorganize();
                fab.setVisibility(View.GONE);
            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        textVieww = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textName);
        textVieww2 = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textemail);
        final ImageView imageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView29);

        final String email = auth.getCurrentUser().getEmail();

        String currentuser = auth.getCurrentUser().getUid();
        Query search = mDatabaseRef2.child("userinfo").orderByChild("userID").startAt(currentuser).endAt(currentuser+"\uf8ff");
        search.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    UserAccount user = snapshot.getValue(UserAccount.class);
                    textVieww.setText(user.getUserName());
                    textVieww2.setText(email);
                    typess = user.getUserType();
                    if(typess.equals("TravelOrganizer")){
                        fab.setVisibility(View.VISIBLE);
                    }
                    if(typess.equals("Traveler")){
                        fab.setVisibility(View.GONE);
                    }
                    Picasso.with(getApplicationContext()).load(user.getUserPhoto()).placeholder(R.drawable.progress_animation).into(imageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        displaySelectedScreen(R.id.nav_camera);

        notification = new NotificationCompat.Builder(getApplicationContext());
        notification.setAutoCancel(true);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("pushnotif");
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("pushnotif").child(auth.getCurrentUser().getUid());
        Query search5 = databaseReference.child(auth.getCurrentUser().getUid()).orderByChild("notifUser").equalTo(auth.getCurrentUser().getUid());
        search5.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    int incid = uniqueID + 1;
                    PushNotification pushNotification = snapshot.getValue(PushNotification.class);
                    String pd = pushNotification.getnotifComment();

                    //Toast.makeText(getApplicationContext(), pd, Toast.LENGTH_SHORT).show();

                    notification.setSmallIcon(R.drawable.logo);
                    notification.setTicker(pd);
                    notification.setWhen(System.currentTimeMillis());
                    notification.setContentTitle("Travel Express");
                    notification.setContentText(pd);

                    Intent intent = new Intent(TouristDashboard.this, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(TouristDashboard.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    notification.setContentIntent(pendingIntent);

                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    nm.notify(incid, notification.build());

                    snapshot.getRef().removeValue();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Intent intent = new Intent(TouristDashboard.this, TouristDashboard.class);
                startActivity(intent);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //Toast.makeText(getApplicationContext(),"Change",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //Toast.makeText(getApplicationContext(),"Move",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        //else {
            //super.onBackPressed();
        //}
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            a.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(a);
            onQuitPressed();
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

    }
    public void onQuitPressed() {

        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.menusearch);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;

            }
            @Override
            public boolean onQueryTextChange(String newText) {
                    Query search = mDatabaseRef.orderByChild("categoryplace").startAt(newText).endAt(newText + "\uf8ff");
                    search.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            travelList.clear();
                            progressDialog.dismiss();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                TourDetails3 productUpload = snapshot.getValue(TourDetails3.class);
                                travelList.add(productUpload);
                            }
                            travelViewAdapter = new TravelViewAdapter(TouristDashboard.this, R.layout.travellayout, travelList);
                            listView.setAdapter(travelViewAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    //Toast.makeText(getApplicationContext(), newText, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void displaySelectedScreen(int id){

        switch (id) {
            case R.id.nav_camera:
                showtours();
                layoutstr = "showtour";
                final String email = auth.getCurrentUser().getEmail();
                String currentuser = auth.getCurrentUser().getUid();
                Query search = mDatabaseRef2.child("userinfo").orderByChild("userID").startAt(currentuser).endAt(currentuser+"\uf8ff");
                search.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            UserAccount user = snapshot.getValue(UserAccount.class);
                            textVieww.setText(user.getUserName());
                            textVieww2.setText(email);
                            typess = user.getUserType();
                            if(typess.equals("TravelOrganizer")){
                                fab.setVisibility(View.VISIBLE);
                                fab.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        fab.setVisibility(View.GONE);
                                        showorganize();
                                    }
                                });
                            }
                            if(typess.equals("Traveler")){
                                fab.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
            case R.id.nav_gallery:
                showorganizetours();
                fab.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_share:
                showprofile();
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            case R.id.nav_slideshow:
                showmessage();
                layoutstr = "message";
                fab.setVisibility(View.VISIBLE);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showuser();
                    }
                });
                break;
            case R.id.nav_send:
                signout();
                break;
            case R.id.nav_invite:
                invite();
                //Toast.makeText(getApplicationContext(), "Invitation", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_manage:
                contactus();
                break;
            case R.id.nav_report:
                report();
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displaySelectedScreen(id);
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == RESULT_IMAGE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_IMAGE);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_IMAGE && resultCode == RESULT_OK && data!=null){
            try{
                if(layout1.getVisibility() == View.VISIBLE && layout2.getVisibility() == View.GONE && layout3.getVisibility() == View.GONE){
                    uri = data.getData();
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    Bitmap yourselectedimage = BitmapFactory.decodeStream(inputStream);
                    int nh = (int) (yourselectedimage.getHeight() * (512.0 / yourselectedimage.getWidth()));
                    Bitmap scaled = Bitmap.createScaledBitmap(yourselectedimage, 512, nh, true);
                    imageView.setImageBitmap(scaled);
                    add1.setVisibility(View.VISIBLE);

                }
                if(layout1.getVisibility() == View.VISIBLE  && layout2.getVisibility() == View.VISIBLE && layout3.getVisibility() == View.GONE ){
                    uri2 = data.getData();
                    InputStream inputStream = getContentResolver().openInputStream(uri2);
                    Bitmap yourselectedimage = BitmapFactory.decodeStream(inputStream);
                    int nh = (int) (yourselectedimage.getHeight() * (512.0 / yourselectedimage.getWidth()));
                    Bitmap scaled = Bitmap.createScaledBitmap(yourselectedimage, 512, nh, true);
                    imageView2.setImageBitmap(scaled);
                    add2.setVisibility(View.VISIBLE);

                }
                if(layout1.getVisibility() == View.VISIBLE && layout2.getVisibility() == View.VISIBLE && layout3.getVisibility() == View.VISIBLE){
                    uri3 = data.getData();
                    InputStream inputStream = getContentResolver().openInputStream(uri3);
                    Bitmap yourselectedimage = BitmapFactory.decodeStream(inputStream);
                    int nh = (int) (yourselectedimage.getHeight() * (512.0 / yourselectedimage.getWidth()));
                    Bitmap scaled = Bitmap.createScaledBitmap(yourselectedimage, 512, nh, true);
                    imageView3.setImageBitmap(scaled);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void uploadtour(){
        //day counter
        int day = date.getDayOfMonth();
        int month = date.getMonth();
        int year = date.getYear()-1900;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy");
        Date d = new Date(year, month, day);
        String strdate = dateFormat.format(d);

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        int day2 = today.monthDay;
        int month2 = today.month;
        int year2 = today.year-1900;
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMMM dd yyyy");
        Date d2 = new Date(year2, month2, day2);
        String strdate2 = dateFormat.format(d2);

        long diff = d.getTime() - d2.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);

        String daysleft = String.valueOf(diffDays);

        //end

        if(places.getVisibility() == View.VISIBLE){
            location2 = places.getSelectedItem().toString();
        }
        if(places2.getVisibility() == View.VISIBLE){
            location2 = places2.getSelectedItem().toString();
        }
        if(places3.getVisibility() == View.VISIBLE){
            location2 = places3.getSelectedItem().toString();
        }
        final String location1 = spinner.getSelectedItem().toString();

        final String date1 = dateFormat.format(d);
        final String joiner1 = joiner.getText().toString();
        final String description1 = description.getText().toString();
        final String itinerary1 = otheritinerary.getText().toString();
        final String inclusion1 = inclusions.getText().toString();

        if(uri==null){
            Toast.makeText(getApplicationContext(), "Please choose an Image for Advertising", Toast.LENGTH_SHORT).show();
        }
        if(location1.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter a location", Toast.LENGTH_SHORT).show();
            location.requestFocus();
            return;
        }
        if(joiner1.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please how many joiners", Toast.LENGTH_SHORT).show();
            joiner.requestFocus();
            return;
        }
        if(description1.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter a description", Toast.LENGTH_SHORT).show();
            description.requestFocus();
            return;
        }
        if(inclusion1.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter inclusions", Toast.LENGTH_SHORT).show();
            inclusions.requestFocus();

        }
        else{
            String allItems = ""; //used to display in the toast

            for(String str : selectedStrings){
                allItems = allItems + "\n" + str; //adds a new line between items
            }
            final String itinerary2 = allItems.trim() +"\n"+ itinerary1;
            final String userid = auth.getCurrentUser().getUid();
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Please wait");
            dialog.show();
            if(layout1.getVisibility() == View.VISIBLE && layout2.getVisibility() == View.GONE && layout3.getVisibility() == View.GONE){
                StorageReference ref = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(uri));
                ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.dismiss();
                        String uploadID = mDatabaseRef.push().getKey();
                        TourDetails tourDetails = new TourDetails(uploadID,userid, location2, date1, joiner1, description1, itinerary2, inclusion1, taskSnapshot.getDownloadUrl().toString(), location1, location2);
                        mDatabaseRef.child(uploadID).setValue(tourDetails);
                        Toast.makeText(getApplicationContext(), "Travel has been set", Toast.LENGTH_LONG).show();

                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Item Failed Uploading", Toast.LENGTH_SHORT).show();
                    }
                })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
                        {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                            {
                                double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                dialog.setMessage("Setting up Travel "+ (int)progress+ "%");
                            }
                        });
            }
            if(layout1.getVisibility() == View.VISIBLE  && layout2.getVisibility() == View.VISIBLE && layout3.getVisibility() == View.GONE ){
                StorageReference ref = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(uri));

                ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        StorageReference ref2 = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(uri2));
                        ref2.putFile(uri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot2) {
                                dialog.dismiss();
                                String uploadID = mDatabaseRef.push().getKey();
                                TourDetails2 tourDetails2 = new TourDetails2(uploadID,userid, location2, date1, joiner1, description1, itinerary2, inclusion1, taskSnapshot.getDownloadUrl().toString(), taskSnapshot2.getDownloadUrl().toString(), location1, location2);
                                mDatabaseRef.child(uploadID).setValue(tourDetails2);
                                Toast.makeText(getApplicationContext(), "Travel has been set", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Image 2 Failed Uploading", Toast.LENGTH_SHORT).show();
                            }
                        })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
                                {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                                    {
                                        double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                        dialog.setMessage("Setting up Travel "+ (int)progress+ "%");
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Image 1 Failed Uploading", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        dialog.setMessage("Image 1 uploading "+ (int)progress+ "%");
                    }
                });
            }
            if(layout1.getVisibility() == View.VISIBLE && layout2.getVisibility() == View.VISIBLE && layout3.getVisibility() == View.VISIBLE) {
                StorageReference ref = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(uri));
                ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        StorageReference ref2 = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(uri2));
                        ref2.putFile(uri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot2) {
                                StorageReference ref3 = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(uri2));
                                ref3.putFile(uri3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot3) {
                                        dialog.dismiss();
                                        String uploadID = mDatabaseRef.push().getKey();
                                        TourDetails3 tourDetails3 = new TourDetails3(uploadID,userid, location2, date1, joiner1, description1, itinerary2, inclusion1, taskSnapshot.getDownloadUrl().toString(), taskSnapshot2.getDownloadUrl().toString(), taskSnapshot3.getDownloadUrl().toString(), location1, location2);
                                        mDatabaseRef.child(uploadID).setValue(tourDetails3);
                                        Toast.makeText(getApplicationContext(), "Travel has been set", Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener()
                                {
                                    @Override
                                    public void onFailure(@NonNull Exception e)
                                    {
                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Image 3 Failed Uploading", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
                                {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                                    {
                                        double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                        dialog.setMessage("Setting up Travel "+ (int)progress+ "%");
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Image 2 Failed Uploading", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
                        {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                            {
                                double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                dialog.setMessage("Image 2 uploading "+ (int)progress+ "%");
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Image 1 Failed Uploading", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        dialog.setMessage("Image 1 uploading "+ (int)progress+ "%");
                    }
                });
            }

        }
    }
    public String getImageExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    public void showtours(){
        LayoutInflater inflater = getLayoutInflater();
        RelativeLayout container = (RelativeLayout) findViewById(R.id.content_tourist_dashboard);
        final LinearLayout layout;
        container.removeAllViews();
        inflater.inflate(R.layout.activity_travel_view, container);
        layout = (LinearLayout) findViewById(R.id.activity_travel_view);
        //final LinearLayout layout5 = (LinearLayout)findViewById(R.id.travellocation);

        travelList = new ArrayList<>();
        listView = (ListView)layout.findViewById(R.id.travellist);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TourDetails3 tourDetails3 = (TourDetails3) adapterView.getAdapter().getItem(i);
                tourID = tourDetails3.getTourID();
                String tourslots = tourDetails3.getTourJoiner();
                Intent intent = new Intent(TouristDashboard.this, ShowTour.class);
                intent.putExtra("slots", tourslots);
                intent.putExtra("tourID", tourID);
                startActivity(intent);
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while loading travel information..... ");
        progressDialog.show();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                travelList.clear();
                progressDialog.dismiss();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    TourDetails3 productUpload = snapshot.getValue(TourDetails3.class);
                    travelList.add(productUpload);

                }
                travelViewAdapter = new TravelViewAdapter(TouristDashboard.this, R.layout.travellayout, travelList);
                listView.setAdapter(travelViewAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void showprofile(){
        LayoutInflater inflater = getLayoutInflater();
        RelativeLayout container = (RelativeLayout) findViewById(R.id.content_tourist_dashboard);
        LinearLayout layout;
        container.removeAllViews();
        inflater.inflate(R.layout.showprofile, container);
        layout = (LinearLayout) findViewById(R.id.showprofile);

        travelList2 = new ArrayList<>();

        imageView4 = (ImageView)layout.findViewById(R.id.imageView10);
        listView2 = (ListView)layout.findViewById(R.id.listView);
        //listView3 = (ListView)layout.findViewById(R.id.listView2);
        //browsee = (Button)layout.findViewById(R.id.button15);
        //savee = (Button)layout.findViewById(R.id.button14);

        String uID= auth.getCurrentUser().getUid();

        Query searchuser = mDatabaseRef2.child("userinfo").orderByChild("userID").startAt(uID).endAt(uID+"\uf8ff");
        searchuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserAccount user = snapshot.getValue(UserAccount.class);
                    Picasso.with(getApplicationContext()).load(user.getUserPhoto()).placeholder(R.drawable.progress_animation).into(imageView4);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query search = mDatabaseRef2.child("joinerinfo").orderByChild("joiner").equalTo(uID);
        search.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {
                travelList2.clear();
                for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()) {
                    Joiner productUpload = snapshot1.getValue(Joiner.class);
                    travelList2.add(productUpload);
                }
                travelViewAdapter2 = new TravelViewAdapter2(TouristDashboard.this, R.layout.showprofile_layout, travelList2);
                listView2.setAdapter(travelViewAdapter2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
    public void showorganize(){
        LayoutInflater inflater = getLayoutInflater();
        RelativeLayout container = (RelativeLayout) findViewById(R.id.content_tourist_dashboard);
        LinearLayout layout;
        container.removeAllViews();
        inflater.inflate(R.layout.activity_organize_travel, container);
        layout = (LinearLayout) findViewById(R.id.activity_organize_travel);

        layout1 = (LinearLayout)layout.findViewById(R.id.layout1);
        layout2 = (LinearLayout)layout.findViewById(R.id.layout2);
        layout3 = (LinearLayout)layout.findViewById(R.id.layout3);

        add1 = (Button)layout.findViewById(R.id.add1);
        add2 = (Button)layout.findViewById(R.id.add2);
        add3 = (Button)layout.findViewById(R.id.add3);

        browse1 = (Button)layout.findViewById(R.id.browse1);
        browse2 = (Button)layout.findViewById(R.id.browse2);
        browse3 = (Button)layout.findViewById(R.id.browse3);
        save = (Button)layout.findViewById(R.id.button11);

        date = (DatePicker)layout.findViewById(R.id.datePicker2);
        date.setMinDate(System.currentTimeMillis() - 1000);
        joiner = (EditText)layout.findViewById(R.id.editText11);
        description = (EditText)layout.findViewById(R.id.edit_text);
        otheritinerary = (EditText)layout.findViewById(R.id.edit_text2);
        inclusions = (EditText)layout.findViewById(R.id.edit_text3);

        layout2.setVisibility(View.GONE);
        layout3.setVisibility(View.GONE);

        add1.setVisibility(View.GONE);
        add2.setVisibility(View.GONE);
        add3.setVisibility(View.GONE);

        imageView = (ImageView)layout.findViewById(R.id.imageView2);
        imageView2 = (ImageView)layout.findViewById(R.id.imageView3);
        imageView3 = (ImageView)layout.findViewById(R.id.imageView4);

        spinner = (Spinner) findViewById(R.id.island_spinner);

        places = (Spinner) findViewById(R.id.luzon_spinner);
        places2 = (Spinner) findViewById(R.id.visayas_spinner);
        places3 = (Spinner) findViewById(R.id.mindanao_spinner);

        listView5 = (ListView)findViewById(R.id.listView);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String island = spinner.getItemAtPosition(i).toString();
                if (island.equals("Luzon")) {
                    places.setVisibility(View.VISIBLE);
                    places2.setVisibility(View.GONE);
                    places3.setVisibility(View.GONE);
                    listView5.setAdapter(null);
                    places.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView2, View view2, int i2, long l2) {
                            selectedStrings.clear();
                            String place = places.getItemAtPosition(i2).toString();
                            itineraryList = new ArrayList<>();

                            mDatabaseRef3 = FirebaseDatabase.getInstance().getReference("Location").child(place);
                            mDatabaseRef3.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    itineraryList.clear();
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        ItineraryUpload itineraryUpload = snapshot.getValue(ItineraryUpload.class);
                                        itineraryList.add(itineraryUpload);
                                    }
                                    itineraryViewAdapter = new ItineraryViewAdapter(TouristDashboard.this, R.layout.checklayout, itineraryList);
                                    listView5.setAdapter(itineraryViewAdapter);
                                    itineraryViewAdapter.notifyDataSetChanged();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView2) {

                        }
                    });
                }
                if (island.equals("Visayas")){
                    places2.setVisibility(View.VISIBLE);
                    places.setVisibility(View.GONE);
                    places3.setVisibility(View.GONE);
                    listView5.setAdapter(null);
                    places2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView2, View view2, int i2, long l2) {
                            selectedStrings.clear();
                            String place = places2.getItemAtPosition(i2).toString();
                            itineraryList = new ArrayList<>();
                            mDatabaseRef3 = FirebaseDatabase.getInstance().getReference("Location").child(place);
                            mDatabaseRef3.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    itineraryList.clear();
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        ItineraryUpload itineraryUpload = snapshot.getValue(ItineraryUpload.class);
                                        itineraryList.add(itineraryUpload);
                                    }
                                    itineraryViewAdapter = new ItineraryViewAdapter(TouristDashboard.this, R.layout.checklayout, itineraryList);
                                    listView5.setAdapter(itineraryViewAdapter);
                                    itineraryViewAdapter.notifyDataSetChanged();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
                if (island.equals("Mindanao")){
                    places3.setVisibility(View.VISIBLE);
                    places2.setVisibility(View.GONE);
                    places.setVisibility(View.GONE);
                    listView5.setAdapter(null);
                    places3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView2, View view2, int i2, long l2) {
                            selectedStrings.clear();
                            String place = places3.getItemAtPosition(i2).toString();
                            itineraryList = new ArrayList<>();
                            mDatabaseRef3 = FirebaseDatabase.getInstance().getReference("Location").child(place);
                            mDatabaseRef3.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    itineraryList.clear();
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                        ItineraryUpload itineraryUpload = snapshot.getValue(ItineraryUpload.class);
                                        itineraryList.add(itineraryUpload);
                                    }
                                    itineraryViewAdapter = new ItineraryViewAdapter(TouristDashboard.this, R.layout.checklayout, itineraryList);
                                    listView5.setAdapter(itineraryViewAdapter);
                                    itineraryViewAdapter.notifyDataSetChanged();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        selectedStrings = new ArrayList<String>();
        listView5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                String itinerary = checkBox.getText().toString();

                checkBox.setChecked(!checkBox.isChecked());
                if(checkBox.isChecked()) {
                    selectedStrings.add(itinerary);
                }
                else{
                    selectedStrings.remove(itinerary);
                }
            }
        });



        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);
                browse1.setVisibility(View.GONE);
            }
        });
        add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add2.setVisibility(View.GONE);
                layout3.setVisibility(View.VISIBLE);
                browse2.setVisibility(View.GONE);
            }
        });
        browse1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        TouristDashboard.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        RESULT_IMAGE
                );
            }
        });
        browse2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        TouristDashboard.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        RESULT_IMAGE
                );
            }
        });
        browse3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        TouristDashboard.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        RESULT_IMAGE
                );
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadtour();
            }
        });
    }
    public void showorganizetours(){
        LayoutInflater inflater = getLayoutInflater();
        RelativeLayout container = (RelativeLayout) findViewById(R.id.content_tourist_dashboard);
        LinearLayout layout;
        container.removeAllViews();
        inflater.inflate(R.layout.activity_travel_view, container);
        layout = (LinearLayout) findViewById(R.id.activity_travel_view);

        travelList = new ArrayList<>();
        listView = (ListView)layout.findViewById(R.id.travellist);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TourDetails3 tourDetails3 = (TourDetails3) adapterView.getAdapter().getItem(i);
                tourID = tourDetails3.getTourID();
                Intent intent = new Intent(TouristDashboard.this, ShowOwnTour.class);
                intent.putExtra("tourID", tourID);
                startActivity(intent);
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while loading travel information..... ");
        progressDialog.show();
        String userID = auth.getCurrentUser().getUid();
        Query search2 = mDatabaseRef2.child("tourinfo").orderByChild("userID").startAt(userID).endAt(userID+"\uf8ff");
        search2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                travelList.clear();
                progressDialog.dismiss();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    TourDetails3 productUpload = snapshot.getValue(TourDetails3.class);
                    travelList.add(productUpload);
                }
                travelViewAdapter = new TravelViewAdapter(TouristDashboard.this, R.layout.travellayout, travelList);
                listView.setAdapter(travelViewAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void showmessage(){
        LayoutInflater inflater = getLayoutInflater();
        RelativeLayout container = (RelativeLayout) findViewById(R.id.content_tourist_dashboard);
        LinearLayout layout;
        container.removeAllViews();
        inflater.inflate(R.layout.inbox, container);
        layout = (LinearLayout) findViewById(R.id.inbox);

        inboxList = new ArrayList<>();
        listView = (ListView)layout.findViewById(R.id.list_of_message);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String user2 = ((TextView) view.findViewById(R.id.textView50)).getText().toString();
                Intent intent = new Intent(TouristDashboard.this, LobbyActivity2.class);
                intent.putExtra("key", user2);
                startActivity(intent);
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while loading messages..... ");
        progressDialog.show();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("messageuser");
        uid = auth.getCurrentUser().getUid();

        Query search = mDatabaseRef.orderByChild("user1").equalTo(uid);
        search.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                inboxList.clear();
                progressDialog.dismiss();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    UserMessage messageUpload = snapshot.getValue(UserMessage.class);
                    inboxList.add(messageUpload);
                }
                inboxAdapter = new InboxAdapter(TouristDashboard.this, R.layout.ownmessagelayout, inboxList);
                listView.setAdapter(inboxAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void signout(){
        auth.signOut();
        Intent intent = new Intent(TouristDashboard.this, Loginscreen.class);
        startActivity(intent);
    }
    public void invite(){
        LayoutInflater inflater = getLayoutInflater();
        RelativeLayout container = (RelativeLayout) findViewById(R.id.content_tourist_dashboard);
        LinearLayout layout;
        container.removeAllViews();
        inflater.inflate(R.layout.invitelayout, container);
        layout = (LinearLayout) findViewById(R.id.invite_layout);

        invitationList = new ArrayList<>();
        listView6 = (ListView)layout.findViewById(R.id.list_of_invite);

        String uemail = auth.getCurrentUser().getEmail();

        Query search = mDatabaseRef2.child("invitation").orderByChild("invited").equalTo(uemail);
        search.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                invitationList.clear();
                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                    Invitation productUpload = snapshot1.getValue(Invitation.class);
                    invitationList.add(productUpload);
                }
                invitationViewAdapter = new InvitationViewAdapter(TouristDashboard.this, R.layout.invitation_row_layout, invitationList);
                listView6.setAdapter(invitationViewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView6.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView22 = (TextView)view.findViewById(R.id.textView28);
                TextView textView23 = (TextView)view.findViewById(R.id.textView29);
                String id = textView22.getText().toString();
                String joiner = textView23.getText().toString();
                Intent intent = new Intent(TouristDashboard.this, ShowTour.class);
                intent.putExtra("slots", joiner);
                intent.putExtra("tourID", id);
                startActivity(intent);
            }
        });

    }
    public void showuser(){
        LayoutInflater inflater = getLayoutInflater();
        RelativeLayout container = (RelativeLayout) findViewById(R.id.content_tourist_dashboard);
        LinearLayout layout;
        container.removeAllViews();
        inflater.inflate(R.layout.useraccountlayout, container);
        layout = (LinearLayout) findViewById(R.id.useracc_layout);

        useracc = new ArrayList<>();
        listView7 = (ListView)layout.findViewById(R.id.list_of_acc);
        listView7.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserAccount useracc = (UserAccount) adapterView.getAdapter().getItem(i);
                String userID2 = useracc.getUserID();

                Intent intent = new Intent(TouristDashboard.this, ShowProfile.class);

                intent.putExtra("key",userID2);
                startActivity(intent);
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while loading user information..... ");
        progressDialog.show();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("userinfo");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                travelList.clear();
                progressDialog.dismiss();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    UserAccount productUpload = snapshot.getValue(UserAccount.class);
                    useracc.add(productUpload);
                }
                userAdapter = new userViewAdapter(TouristDashboard.this, R.layout.useraccount_row_layout, useracc);
                listView7.setAdapter(userAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    public void contactus(){
        LayoutInflater inflater = getLayoutInflater();
        RelativeLayout container = (RelativeLayout) findViewById(R.id.content_tourist_dashboard);
        LinearLayout layout;
        container.removeAllViews();
        inflater.inflate(R.layout.activity_contactus, container);
        layout = (LinearLayout) findViewById(R.id.activity_contactus);
        mDatabaseRef4 = FirebaseDatabase.getInstance().getReference("ContactUs");

        final EditText ed = (EditText)layout.findViewById(R.id.editText10);
        final Button btn = (Button)layout.findViewById(R.id.button15);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cID = mDatabaseRef4.push().getKey();
                ContactUs tourDetails = new ContactUs(cID, auth.getCurrentUser().getUid(), ed.getText().toString());
                mDatabaseRef4.child(cID).setValue(tourDetails);
                Toast.makeText(getApplicationContext(), "Your Message has been sent to the Admin and waiting for review", Toast.LENGTH_SHORT).show();
                ed.setText(" ");
                btn.setEnabled(false);
            }
        });
    }
    public void report (){
        showuser();
    }

}

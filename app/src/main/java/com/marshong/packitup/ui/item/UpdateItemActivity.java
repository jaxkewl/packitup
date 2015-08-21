package com.marshong.packitup.ui.item;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.marshong.packitup.R;
import com.marshong.packitup.data.DBContract;
import com.marshong.packitup.model.Image;
import com.marshong.packitup.model.ImageD;
import com.marshong.packitup.model.ImageDelete;
import com.marshong.packitup.model.ImageUpload;
import com.marshong.packitup.model.Item;
import com.marshong.packitup.service.ImageDeleteService;
import com.marshong.packitup.service.ImageUploadService;
import com.marshong.packitup.ui.storage.SectionFragment;
import com.marshong.packitup.ui.storage.StorageListActivity;
import com.marshong.packitup.util.Constants;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UpdateItemActivity extends ActionBarActivity {

    public static final String TAG = UpdateItemActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);
        if (savedInstanceState == null) {

            //get the bundle that contains the ContainerId, Location Id, and Item Id
            Bundle bundle = getIntent().getExtras();

            /*int locationId = bundle.getInt(DBContract.Location.LOCATION_ID);
            int containerId = bundle.getInt(DBContract.Container.CONTAINER_ID);
            int itemId = bundle.getInt(DBContract.Item.ITEM_ID);*/

            //pass in the bundle to the fragment
            UpdateItemFragment fragment = new UpdateItemFragment();
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_item, menu);
        return true;
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class UpdateItemFragment extends Fragment {

        public static final String TAG = UpdateItemFragment.class.getSimpleName();

        //these fields will keep track of the container names and Ids
        ArrayList<String> mContainerNames;
        ArrayList<String> mContainerIds;

        private View mRootView;

        //this is the locationId, we need to know this so the container spinner can only show containers from this location
        private int mLocationId;

        //this is the primary key of the item we are updating
        private int mItemId;

        //this is the item container, it should have the item information populated
        private Item mItem;

        private EditText mEditTextUpdateItemName;
        private EditText mEditTextUpdateItemDescr;
        private Spinner mSpinnerUpdateContainers;

        static final int REQUEST_IMAGE_CAPTURE = 1;
        static final int REQUEST_TAKE_PHOTO = 1;

        private Button mPictureButton;
        private Button mAddToGalleryButton;

        @Bind(R.id.imageview_thumbnail)
        ImageView mImageView;


        // ---------------------------------------------
        //variables needed for camera
        // ---------------------------------------------
        private String mCurrentPhotoPath;
        private File mPhotoFile = null;
        private Uri mUriPicPath;


        // ---------------------------------------------
        //variables needed for Imgur upload and delete
        // ---------------------------------------------

        private String mUser;

        // Use NotificationCompat for backwards compatibility
        private NotificationManagerCompat mNotificationManager;

        // Unique (within app) ID for email Notification
        public static final int UPLOAD_NOTIFICATION_ID = 1;

        /**
         * Upload object containing image and meta data
         */
        private ImageUpload mUpload;

        /**
         * Delete object containing deletehash and meta data
         */
        private ImageDelete mDelete;


        /**
         * Chosen file from intent
         */
        private File mChosenFile;

        //@Bind(R.id.textview_image_location)
        private TextView mTextViewImageLocation;

        @Bind(R.id.button_upload_imgur)
        Button mButtonUploadImage;

        @Bind(R.id.button_delete_imgur)
        Button mButtonDeleteImage;

        @OnClick(R.id.button_delete_imgur)
        public void confirmDelete() {
            Log.d(TAG, "confirm delete called... ");

            String deleteHash = captureDeletehash();
            Log.d(TAG, "deleteHash found: " + deleteHash);
            deleteImage(deleteHash);


            //reload the activity
            getActivity().finish();
            startActivity(getActivity().getIntent());
        }


        //this method will use the URL in the argument to return the matching delete hash
        //that was saved off when the image was initially uploaded anonymously
        private String captureDeletehash() {
            String deletehash = "";

            SharedPreferences sharedPref = getActivity().getSharedPreferences(Constants.sharedPrefName, getActivity().MODE_PRIVATE);

            Map<String, ?> keys = sharedPref.getAll();

            //if the shred pref contains the upload key and ends with delete, then we found the deletehash
            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                if (entry.getKey().equals(mItemId + "_delete")) {
                    Log.d(TAG, "***************** found delete hash ***************** " + entry.getKey() + ": " + entry.getValue().toString());
                    return entry.getValue().toString();
                }
            }
            return deletehash;
        }

        private void deleteKeyFromSharedPref(String key) {
            Log.d(TAG, "deleteKeyFromSharedPref " + key);
            SharedPreferences sharedPref = getActivity().getSharedPreferences(Constants.sharedPrefName, getActivity().MODE_PRIVATE);

            Map<String, ?> keys = sharedPref.getAll();

            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                Log.d(TAG, "***************** ***************** " + entry.getKey() + ": " + entry.getValue().toString());

                if (entry.getKey().equals(key)) {
                    Log.d(TAG, "deleting key " + entry.getKey());
                    sharedPref.edit().remove(entry.getKey()).commit();
                } else if (entry.getKey().equals(key + "_delete")) {
                    Log.d(TAG, "deleting key " + entry.getKey());
                    sharedPref.edit().remove(entry.getKey()).commit();  //GOTCHA: call commit right after removing key
                }

            }
            displayAllSharedPref();
        }

        public void deleteImage(String deletehash) {
            Log.d(TAG, "deleteImage clicked " + deletehash);

            if (null == deletehash) {
                return;
            }

            // Wrap the chosen image in an upload object (to be sent to API).
            createDelete(deletehash);

            // Initiate delete
            new ImageDeleteService(getActivity()).execute(mDelete, new UiCallbackDel());

        }


        private class UiCallbackDel implements Callback<ImageD> {

            @Override
            public void success(ImageD imageResponse, Response response) {
                Log.d(TAG, "success called... this is a callback after deleting an image ");

                deleteKeyFromSharedPref(Integer.toString(mItemId));

                Snackbar.make(mRootView, "Delete Successful", Snackbar.LENGTH_LONG).show();
/*
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(getContext())
                            .setSmallIcon(R.drawable.notification_template_icon_bg)
                            .setContentTitle("Image Uploaded to Imgur")
                            .setContentText("Delete URL: " + deleteUrl);

            // Create the notification
            Notification basicNotification = builder.build();

            mNotificationManager.notify(UPLOAD_NOTIFICATION_ID, basicNotification);
*/


            }

            @Override
            public void failure(RetrofitError error) {
                //Assume we have no connection, since error is null
                if (error == null) {
                    Snackbar.make(mRootView, "No internet connection", Snackbar.LENGTH_SHORT).show();
                }

                Snackbar.make(mRootView, "failure to delete " + error.toString(), Snackbar.LENGTH_LONG).show();
                Log.d(TAG, "failure to delete... " + error.toString());

/*            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(getContext())
                            .setSmallIcon(R.drawable.notification_template_icon_bg)
                            .setContentTitle("Image Failed to upload to Imgur")
                            .setContentText("Try again");

            // Create the notification
            Notification basicNotification = builder.build();

            mNotificationManager.notify(UPLOAD_NOTIFICATION_ID, basicNotification);*/
            }
        }


        @OnClick(R.id.button_upload_imgur)
        public void uploadImage() {
            Log.d(TAG, "Upload Image button clicked, uploading image");

            if (mChosenFile == null) {
                return;
            }

            // Wrap the chosen image in an upload object (to be sent to API).
            createUpload(mChosenFile);

            // Initiate upload
            new ImageUploadService(getActivity()).execute(mUpload, new UiCallback());
        }

        private void createDelete(String deletehash) {
            mDelete = new ImageDelete();
            mDelete.deletehash = deletehash;
        }

        private void createUpload(File image) {
            Log.d(TAG, "createUpload called.... %%%% " + image.getPath());

            mUpload = new ImageUpload();
            mUpload.image = image;
            //mUpload.title = mUploadTitle.getText().toString();
            //mUpload.description = mUploadDesc.getText().toString();
        }


        private void captureUploadedImage() {
            Log.d(TAG, "captureUploadedImage called... mItemId: " + mItemId);

            displayAllSharedPref();

            SharedPreferences sharedPref = getActivity().getSharedPreferences(Constants.sharedPrefName, getActivity().MODE_PRIVATE);

            Map<String, ?> keys = sharedPref.getAll();

            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                Log.d(TAG, "***************** ***************** " + entry.getKey() + ": " + entry.getValue().toString());
                if (entry.getKey().equals(Integer.toString(mItemId))) {

                    String loadFile = entry.getValue().toString();
                    Log.d(TAG, "Found it " + loadFile);
                    mTextViewImageLocation.setText(loadFile);
                    mImageView.setImageResource(R.drawable.duct_tape);
                    //mImageView.setImageURI(Uri.parse(loadFile));
                    Picasso
                            .with(getActivity().getApplicationContext())
                            .load(loadFile)
                            .fit() // will explain later
                            .error(R.drawable.abc_btn_radio_material)
                            .into(mImageView);
                    break;
                }
            }
        }


        private void displayAllSharedPref() {
            Log.d(TAG, "displayAllSharedPref called");

            SharedPreferences sharedPref = getActivity().getSharedPreferences(Constants.sharedPrefName, MODE_PRIVATE);

            Map<String, ?> keys = sharedPref.getAll();

            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                Log.d(TAG, "***************** ***************** " + entry.getKey() + ": " + entry.getValue().toString());
            }
        }

        private void saveDeleteUrl(String deletehash) {
            Log.d(TAG, "saveDeleteUrl called... " + deletehash + " " + mItemId);

            SharedPreferences sharedPref = getActivity().getSharedPreferences(Constants.sharedPrefName, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(mItemId + "_delete", deletehash);
            editor.commit();

            displayAllSharedPref();
        }

        private void saveUploadedImage(String imageUrl) {
            Log.d(TAG, "saveUploadedImage called... " + mItemId + " " + imageUrl);

            SharedPreferences sharedPref = getActivity().getSharedPreferences(Constants.sharedPrefName, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(Integer.toString(mItemId), imageUrl);
            editor.commit();

            displayAllSharedPref();
        }


        private class UiCallback implements Callback<Image> {

            @Override
            public void success(Image imageResponse, Response response) {
                //String deleteUrl = "https://api.imgur.com/3/image/" + imageResponse.getDeletehash();
                Log.d(TAG, "success called... this is a callback after uploading an image " + imageResponse.getId() + " " + imageResponse.getLink() + " deletehash: " + imageResponse.getDeletehash());
                Snackbar.make(mRootView, "Image Uploaded", Snackbar.LENGTH_LONG).show();
                saveDeleteUrl(imageResponse.getDeletehash());
                saveUploadedImage(imageResponse.getLink());
                mTextViewImageLocation.setText(imageResponse.getLink());

                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(getActivity())
                                .setSmallIcon(R.drawable.notification_template_icon_bg)
                                .setContentTitle("Image Uploaded to Imgur")
                                .setContentText("Delete Hash: " + imageResponse.getDeletehash());

                // Create the notification
                //Notification basicNotification = builder.build();

                //mNotificationManager.notify(UPLOAD_NOTIFICATION_ID, basicNotification);

                // Reset the fields
                //clearInput();
            }

            @Override
            public void failure(RetrofitError error) {
                //Assume we have no connection, since error is null
                if (error == null) {
                    Snackbar.make(mRootView, "No internet connection", Snackbar.LENGTH_SHORT).show();
                    Log.e(TAG, "no internet connection");
                }

                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(getActivity())
                                .setSmallIcon(R.drawable.notification_template_icon_bg)
                                .setContentTitle("Image Failed to upload to Imgur")
                                .setContentText("Try again");

                // Create the notification
                Notification basicNotification = builder.build();

                mNotificationManager.notify(UPLOAD_NOTIFICATION_ID, basicNotification);
            }
        }


        public UpdateItemFragment() {
        }


        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                //Bundle extras = data.getExtras();
                Bitmap imageBitmap = null;
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mUriPicPath);
                    mImageView.setImageBitmap(imageBitmap);
                    saveUploadedImage(mUriPicPath.getPath());   //save off local path to the textview
                    mTextViewImageLocation.setText(mUriPicPath.getPath());
                    Log.d(TAG, "%%%%%% image saved to: " + mUriPicPath.getPath());
                    mChosenFile = new File(mUriPicPath.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Bitmap imageBitmap = (Bitmap) extras.get("data");
                //Bitmap imageBitmap = (Bitmap) mPhotoFile;
            }
        }

        private void galleryAddPic() {
            Snackbar.make(mRootView, "%%%%%%%%%Adding picture to gallery: " + mCurrentPhotoPath, Snackbar.LENGTH_SHORT).show();
            Log.d(TAG, "adding to gallery " + mCurrentPhotoPath);
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            getActivity().sendBroadcast(mediaScanIntent);
        }

        private void dispatchTakePictureIntent() {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go

                try {
                    mPhotoFile = createImageFile();
                    Snackbar.make(mRootView, mCurrentPhotoPath, Snackbar.LENGTH_SHORT).show();
                    Log.d(TAG, "%%%%%%%%% " + mCurrentPhotoPath);
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.e(TAG, ex.toString());
                }
                // Continue only if the File was successfully created
                if (mPhotoFile != null) {
                    mUriPicPath = Uri.fromFile(mPhotoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUriPicPath);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        }

        private File createImageFile() throws IOException {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = "file:" + image.getAbsolutePath();

            //check to see if file exists
            if (image.exists()) {
                Log.d(TAG, "%%%%%%%% file exists " + image);
            } else {
                Log.d(TAG, "%%%%%%%% file does not exist " + image);
            }

            return image;
        }


        private int findContainerPos(ArrayList<String> containers, String containerName) {
            Log.d(TAG, "findContainerPos using containerName: " + containerName + " and containers " + containers.size());
            for (String str : containers) {
                Log.d(TAG, str);
            }

            return containers.indexOf(containerName);
        }


        private void findValidContainers() {
            Log.d(TAG, "findValidContainers using location id: " + mLocationId);

            String selection = DBContract.Container.CONTAINER_LOCATION + "=?";
            String[] selectionArgs = {Integer.toString(mLocationId)};

            //use a content resolver and get the item information from the db
            //query the location table for all the location names.
            Cursor data = getActivity().getContentResolver().query(DBContract.Container.CONTENT_CONTAINER_URI,
                    DBContract.Container.CONTAINER_PROJECTION,
                    selection,
                    selectionArgs,
                    null);

            int i = 0;
            mContainerNames = new ArrayList<String>();
            mContainerIds = new ArrayList<String>();

            if (data.moveToFirst()) {

                do {
                    mContainerNames.add(data.getString(data.getColumnIndex(DBContract.Container.CONTAINER_NAME)));
                    mContainerIds.add(data.getString(data.getColumnIndex(DBContract.Container.CONTAINER_ID)));
                    Log.d(TAG, "found container: " + mContainerNames.get(i) + " with Id: " + mContainerIds.get(i));
                    i++;
                } while (data.moveToNext());
            }
            data.close();
        }

        private void createItem() {
            Log.d(TAG, "createItem using Id: " + mItemId);

            String selection = DBContract.Item.ITEM_ID + "= ?";
            String[] selectionArgs = {Integer.toString(mItemId)};

            //use a content resolver and get the item information from the db
            //query the location table for all the location names.
            Cursor data = getActivity().getContentResolver().query(DBContract.Item.CONTENT_ITEM_URI,
                    DBContract.Item.ITEM_PROJECTION,
                    selection,
                    selectionArgs,
                    null);

            int i = 0;

            if (data.moveToFirst()) {
                mItem = new Item("", "");
                do {
                    mItem.setName(data.getString(data.getColumnIndex(DBContract.Item.ITEM_NAME)));
                    mItem.setDescr(data.getString(data.getColumnIndex(DBContract.Item.ITEM_DESCR)));

                    int containerId = Integer.parseInt(data.getString(data.getColumnIndex(DBContract.Item.CONTAINER_REF)));
                    mItem.setContainerID(containerId);
                    mItem.setContainer("");

                    Log.d(TAG, "mItem: " + mItem);

                    i++;
                } while (data.moveToNext());
            }
            data.close();


        }

        private void init() {


        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_update_item, container, false);
            mRootView = rootView;
            Log.d(TAG, "onCreateView updateItemFragment");

            ButterKnife.bind(this, rootView);

            mTextViewImageLocation = (TextView) rootView.findViewById(R.id.textview_image_location);

            Bundle extras = getActivity().getIntent().getExtras();
            mItemId = extras.getInt(SectionFragment.ITEM_ID);
            mLocationId = extras.getInt(SectionFragment.LOCATION_ID);
            int containerId = extras.getInt(SectionFragment.CONTAINER_ID);

            Log.d(TAG, "Found locationId: " + mLocationId + " and itemId: " + mItemId + " containerId: " + containerId);

            //find which position the container name is at
            createItem();
            findValidContainers();

            mEditTextUpdateItemName = (EditText) rootView.findViewById(R.id.edit_text_item_name_update);
            mEditTextUpdateItemDescr = (EditText) rootView.findViewById(R.id.edit_text_item_descr_update);

            Log.d(TAG, "mItem is populated: " + mItem);


            mEditTextUpdateItemDescr.setText(mItem.getDescr());
            mEditTextUpdateItemName.setText(mItem.getName());

            //setup the container spinner
            mSpinnerUpdateContainers = (Spinner) rootView.findViewById(R.id.spinner_item_container_update);
            ArrayAdapter<String> aa = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mContainerNames);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            mSpinnerUpdateContainers.setAdapter(aa);

            int containerPos = findContainerPos(mContainerNames, mItem.getContainer());
            if (containerPos <= 0) {
                containerPos = 1;
            }
            mSpinnerUpdateContainers.setSelection(containerPos);


/*            mAddToGalleryButton = (Button) rootView.findViewById(R.id.button_add_to_gallery);
            mAddToGalleryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(rootView, "Adding to Gallery", Snackbar.LENGTH_SHORT).show();
                    galleryAddPic();
                }
            });*/

            mPictureButton = (Button) rootView.findViewById(R.id.button_take_picture);
            mPictureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(rootView, "Take a picture", Snackbar.LENGTH_SHORT).show();
                    dispatchTakePictureIntent();
                }
            });

            //mImageView = (ImageView) rootView.findViewById(R.id.imageview_thumbnail);


            rootView.findViewById(R.id.button_update_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Item item = new Item(mEditTextUpdateItemName.getText().toString(), mEditTextUpdateItemDescr.getText().toString());

                    int selectedItem = mSpinnerUpdateContainers.getSelectedItemPosition();
                    String contName = mContainerNames.get(selectedItem);
                    int contId = Integer.parseInt(mContainerIds.get(selectedItem));

                    item.setContainer(contName);
                    item.setContainerID(contId);

                    Log.d(TAG, "updating item: " + item);
                    //db.insertItem(item);
                    Toast.makeText(getActivity(), "Updated item " + item, Toast.LENGTH_LONG);

                    Intent intent = new Intent(getActivity(), StorageListActivity.class);
                    startActivity(intent);
                }
            });


            captureUploadedImage();

            return rootView;
        }
    }
}

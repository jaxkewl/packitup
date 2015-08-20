package com.marshong.packitup.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.marshong.packitup.api.ImgurRestApi;
import com.marshong.packitup.model.Image;
import com.marshong.packitup.model.ImageUpload;
import com.marshong.packitup.util.Constants;
import com.marshong.packitup.util.NetworkUtils;

import java.lang.ref.WeakReference;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class ImageUploadService extends Service {
    public final static String TAG = ImageUploadService.class.getSimpleName();

    private WeakReference<Context> mContext;

    public ImageUploadService(Context context) {
        this.mContext = new WeakReference<>(context);
    }


    public void execute(ImageUpload upload, final Callback<Image> callback) {

        Log.d(TAG, "********* execute called... " + upload);

        // Check network connection before making a call
        if (!NetworkUtils.isConnected(mContext.get())) {
            Log.e(TAG, "network error!!!");
            callback.failure(null);
            return;
        }

        RestAdapter restAdapter = buildRestAdapter();

        restAdapter.create(ImgurRestApi.class).uploadImage(
                Constants.getClientAuth(),                  // Client auth
                upload.title,                               // Image title
                upload.description,                         // Image description
                upload.albumId,                             // Image album ID
                null,                                       // Anonymous image upload, no username
                new TypedFile("image/*", upload.image),     // Image file
                new Callback<Image>() {
                    @Override
                    public void success(Image imageResponse, Response response) {
                        Log.d(TAG, "******** success called...");

                        if (callback != null) {
                            callback.success(imageResponse, response);
                        }

                        /**
                         * TODO: Notify image was NOT uploaded successfully
                         */
                        if (response == null) {
                            Log.d(TAG, "Image failed to upload successfully.");
                        }
                        /**
                         * TODO: Notify image was uploaded successfully
                         */
                        if (imageResponse.success) {
                            Log.d(TAG, "Image was uploaded successfully: " + imageResponse.toString());
                            Log.d(TAG, "image uploaded to: " + imageResponse.getLink());


                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (callback != null) {
                            callback.failure(error);
                        }
                        /**
                         * TODO: Notify image was NOT uploaded successfully
                         */
                    }
                });
    }

    private RestAdapter buildRestAdapter() {
        RestAdapter imgurAdapter = new RestAdapter.Builder()
                //.setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(ImgurRestApi.server)
                .build();

        /*
        Set rest adapter logging if we're already logging
        */
        if (Constants.LOGGING) {
            imgurAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        }
        return imgurAdapter;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

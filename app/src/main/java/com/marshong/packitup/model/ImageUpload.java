package com.marshong.packitup.model;


import java.io.File;


/**
 * Represents an image to be uploaded to imgur.
 */
public class ImageUpload {
    public File image;
    public String title;
    public String description;
    public String albumId;

    @Override
    public String toString() {
        return "path: " + image.getPath() + "  title: " + title + "   descr: " + description + "   albumnId: " + albumId;
    }
}

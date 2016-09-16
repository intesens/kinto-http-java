package com.intesens.kinto_http;

/**
 * Created by amalle on 16/09/16.
 * Endpoints templates
 *
 * Usage examples:
 * - ROOT.get()
 * - BACTH.get()
 * - PERMISSIONS.get()
 * - BUCKET.get("bucketname")
 * - HISTORY.get("bucketname")
 * - COLLECTION.get("bucketname", "collectioname")
 * - GROUP.get("bucketname", "groupname")
 * - RECORD.get("bucketname", "collectioname", 42) # record id
 * - ATTACHMENT.get("bucketname", "collectioname", 42) # record id
 */
public enum ENDPOINTS {
    ROOT("/"),
    BATCH("/batch"),
    PERMISSIONS("/permissions"),
    BUCKET("/buckets/%s"),
    HISTORY(BUCKET.path + "/history"),
    COLLECTION(BUCKET.path + "/collections/%s"),
    GROUP(BUCKET.path + "/groups/%s"),
    RECORD(COLLECTION.path + "/records/%s"),
    ATTACHMENT(RECORD.path + "/attachment");

    private String path;

    ENDPOINTS(String path) {
        this.path = path;
    }

    public String get(Object... args) {
        return String.format(path, args);
    }
}


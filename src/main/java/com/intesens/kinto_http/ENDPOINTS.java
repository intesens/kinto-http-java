package com.intesens.kinto_http;

/**
 * Created by amalle on 16/09/16.
 * Endpoints templates
 */
public enum ENDPOINTS {
    ROOT("{root}"),
    BATCH(ROOT.path + "/batch"),
    PERMISSIONS(ROOT.path + "/permissions"),
    BUCKETS(ROOT.path + "/buckets"),
    BUCKET(BUCKETS.path + "/{bucket}"),
    HISTORY(BUCKET.path + "/history"),
    COLLECTIONS(BUCKET.path + "/collections"),
    COLLECTION(COLLECTIONS.path + "/{collection}"),
    GROUPS(BUCKET.path + "/groups"),
    GROUP(GROUPS.path + "/{group}"),
    RECORDS(COLLECTION.path + "/records"),
    RECORD(RECORDS.path + "/{record}"),
    ATTACHMENT(RECORD.path + "/attachment");

    private String path;

    ENDPOINTS(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}


package com.intesens.kinto_http;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created by amalle on 16/09/16.
 */
public class ENDPOINTSTest {

    @Test
    public void testRoot() {
        String endpoint = ENDPOINTS.ROOT.get();
        assertEquals("/", endpoint);
    }

    @Test
    public void testBatch() {
        String endpoint = ENDPOINTS.BATCH.get();
        assertEquals("/batch", endpoint);
    }

    @Test
    public void testPermissions() {
        String endpoint = ENDPOINTS.PERMISSIONS.get();
        assertEquals("/permissions", endpoint);
    }

    @Test
    public void testBucket() {
        String endpoint = ENDPOINTS.BUCKET.get("unbucket");
        assertEquals("/buckets/unbucket", endpoint);
    }

    @Test
    public void testHistory() {
        String endpoint = ENDPOINTS.HISTORY.get("unbucket");
        assertEquals("/buckets/unbucket/history", endpoint);
    }

    @Test
    public void testCollection() {
        String endpoint = ENDPOINTS.COLLECTION.get("unbucket", "unecollection");
        assertEquals("/buckets/unbucket/collections/unecollection", endpoint);
    }

    @Test
    public void testGroup() {
        String endpoint = ENDPOINTS.GROUP.get("unbucket", "ungroupe");
        assertEquals("/buckets/unbucket/groups/ungroupe", endpoint);
    }

    @Test
    public void testRecord() {
        String endpoint = ENDPOINTS.RECORD.get("unbucket", "unecollection", 42);
        assertEquals("/buckets/unbucket/collections/unecollection/records/42", endpoint);
    }

    @Test
    public void testAttachment() {
        String endpoint = ENDPOINTS.ATTACHMENT.get("unbucket", "unecollection", 42);
        assertEquals("/buckets/unbucket/collections/unecollection/records/42/attachment", endpoint);
    }
}

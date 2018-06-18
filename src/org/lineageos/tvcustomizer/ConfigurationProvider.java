package org.lineageos.tvcustomize;

import android.content.ContentProvider;
import android.content.ContentProvider.PipeDataWriter;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ConfigurationProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = new UriMatcher(-1);

    static {
        sUriMatcher.addURI("tvlauncher.config", "configuration", 1);
    }

    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        InputStream stream;
        Log.d("ConfigurationProvider", "openFile called: " + uri + "  by: " + getCallingPackage());
        switch (sUriMatcher.match(uri)) {
            case 1:
                stream = getContext().getResources().openRawResource(R.raw.configuration);
                break;
            case 2:
                stream = getContext().getResources().openRawResource(R.raw.promotions);
                break;
            default:
                Log.d("ConfigurationProvider", "Invalid URI");
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return openPipeHelper(uri, "text/xml", null, null, new PipeDataWriter() {
            public void writeDataToPipe(ParcelFileDescriptor output, Uri uri, String mimeType, Bundle opts, Object args) {
                Throwable th;
                Throwable th2 = null;
                FileOutputStream out = null;
                try {
                    IOException e;
                    FileOutputStream out2 = new FileOutputStream(output.getFileDescriptor());
                    try {
                        int count;
                        byte[] buffer = new byte[8192];
                        while (true) {
                            count = stream.read(buffer);
                            if (count == -1) {
                                break;
                            }
                            Log.d("ConfigurationProvider", "Written: " + count + " bytes");
                            out2.write(buffer, 0, count);
                        }
                        Log.d("ConfigurationProvider", "Written: " + count + " bytes");
                        if (out2 != null) {
                            try {
                                out2.close();
                            } catch (Throwable th3) {
                                th2 = th3;
                            }
                        }
                        if (th2 != null) {
                            try {
                                throw th2;
                            } catch (IOException e2) {
                                e = e2;
                            }
                        } else {
                            return;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        out = out2;
                        if (out != null) {
                            try {
                                out.close();
                            } catch (Throwable th5) {
                                if (th2 == null) {
                                    th2 = th5;
                                } else if (th2 != th5) {
                                    th2.addSuppressed(th5);
                                }
                            }
                        }
                        if (th2 == null) {
                            throw th;
                        }
                        try {
                            throw th2;
                        } catch (IOException e3) {
                            e = e3;
                        }
                    }
                    Log.e("ConfigurationProvider", "Failed to send file " + uri, e);
                } catch (Throwable th6) {
                    th = th6;
                    if (out != null) {
                        out.close();
                    }
                    if (th2 == null) {
                        throw th2;
                    } else {
                        throw th;
                    }
                }
            }
        });
    }

    public boolean onCreate() {
        Log.d("ConfigurationProvider", "onCreate called: " + getCallingPackage());
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d("ConfigurationProvider", "query called: " + uri);
        sUriMatcher.match(uri);
        throw new IllegalArgumentException("Unknown URI: " + uri);
    }

    public Uri insert(Uri uri, ContentValues values) {
        Log.d("ConfigurationProvider", "insert called: " + uri);
        throw new UnsupportedOperationException();
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d("ConfigurationProvider", "delete called: " + uri);
        throw new UnsupportedOperationException();
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d("ConfigurationProvider", "update called: " + uri);
        throw new UnsupportedOperationException();
    }

    public String getType(Uri uri) {
        Log.d("ConfigurationProvider", "getType called: " + uri);
        throw new UnsupportedOperationException();
    }
}

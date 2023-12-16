import android.app.PendingIntent;
import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class MyVpnService extends VpnService {
    private static final String TAG = "MyVpnService";
    private static final String TARGET_URL = "https://xyy.shbizhen.com:443/aiapi/device/searchReserveOrder";
    private static final String FILE_NAME = "vpn_data.txt";

    private PendingIntent mConfigureIntent;
    private ParcelFileDescriptor mInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        mConfigureIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startVpn();
        return START_STICKY;
    }

    private void startVpn() {
        if (mInterface != null) {
            Log.d(TAG, "VPN is already running.");
            return;
        }

        Builder builder = new Builder();
        builder.setMtu(1500);
        builder.addAddress("10.0.0.1", 24);
        builder.addRoute("0.0.0.0", 0);
        try {
            mInterface = builder.setSession(getString(R.string.app_name))
                    .setConfigureIntent(mConfigureIntent)
                    .establish();
            Log.d(TAG, "VPN started.");

            FileChannel outputChannel = new FileOutputStream(getFilesDir().getPath() + "/" + FILE_NAME, true).getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(32767);
            while (true) {
                int size = mInterface.read(buffer);
                if (size > 0) {
                    buffer.limit(size);
                    outputChannel.write(buffer);
                    buffer.clear();
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to start VPN: " + e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopVpn();
    }

    private void stopVpn() {
        if (mInterface != null) {
            try {
                mInterface.close();
            } catch (IOException e) {
                Log.e(TAG, "Failed to stop VPN: " + e.getMessage());
            }
            mInterface = null;
            Log.d(TAG, "VPN stopped.");
        }
    }
}

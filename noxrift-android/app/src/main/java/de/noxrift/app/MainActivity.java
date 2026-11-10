package de.noxrift.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/** NoxRift: öffnet deine NoxRift-Zentrale in Chrome – mit deiner bestehenden Anmeldung. Speichert selbst nichts. */
public class MainActivity extends Activity {
  private static final String URL = "https://claude.ai/artifact/Fpjc1FcVpmD4bVA8D3zYZu";

  @Override protected void onCreate(Bundle b) {
    super.onCreate(b);
    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
    Bundle extras = new Bundle();
    extras.putBinder("android.support.customtabs.extra.SESSION", null);
    i.putExtras(extras);
    i.putExtra("android.support.customtabs.extra.TOOLBAR_COLOR", 0xFF0F172A);
    i.putExtra("androidx.browser.customtabs.extra.SHARE_STATE", 2);
    try { i.setPackage("com.android.chrome"); startActivity(i); }
    catch (Exception e) { i.setPackage(null); try { startActivity(i); } catch (Exception x) { } }
    finish();
  }
}

package de.noxrift.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/** NoxRift im Vollbild. Lädt bei jedem Öffnen den aktuellen Stand – Updates kommen automatisch. */
public class MainActivity extends Activity {
  private static final String START = "https://claude.ai/artifact/Fpjc1FcVpmD4bVA8D3zYZu";
  private WebView web;

  @Override protected void onCreate(Bundle b) {
    super.onCreate(b);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    web = new WebView(this);
    WebSettings s = web.getSettings();
    s.setJavaScriptEnabled(true);
    s.setDomStorageEnabled(true);
    s.setDatabaseEnabled(true);
    s.setMediaPlaybackRequiresUserGesture(false);
    s.setSupportMultipleWindows(false);
    CookieManager cm = CookieManager.getInstance();
    cm.setAcceptCookie(true);
    cm.setAcceptThirdPartyCookies(web, true);
    web.setBackgroundColor(0xFF0B1120);
    web.setWebChromeClient(new WebChromeClient());
    web.setWebViewClient(new WebViewClient() {
      @Override public boolean shouldOverrideUrlLoading(WebView v, WebResourceRequest r) {
        Uri u = r.getUrl(); String h = u.getHost() == null ? "" : u.getHost();
        if (h.endsWith("claude.ai") || h.endsWith("claudeusercontent.com") || h.endsWith("anthropic.com")) return false;
        try { startActivity(new Intent(Intent.ACTION_VIEW, u)); } catch (Exception e) { }
        return true;
      }
      @Override public void onPageFinished(WebView v, String url) { CookieManager.getInstance().flush(); }
    });
    setContentView(web);
    vollbild();
    Uri link = getIntent() != null ? getIntent().getData() : null;
    web.loadUrl(link != null ? link.toString() : START);
  }

  @Override protected void onNewIntent(Intent i) { super.onNewIntent(i); if (i.getData() != null) web.loadUrl(i.getData().toString()); }

  @Override public void onWindowFocusChanged(boolean f) { super.onWindowFocusChanged(f); if (f) vollbild(); }

  private void vollbild() {
    if (android.os.Build.VERSION.SDK_INT >= 30) {
      getWindow().setDecorFitsSystemWindows(false);
      WindowInsetsController c = getWindow().getInsetsController();
      if (c != null) { c.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars()); c.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE); }
    } else {
      getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
  }

  @Override protected void onResume() { super.onResume(); web.onResume(); }
  @Override protected void onPause() { super.onPause(); web.onPause(); CookieManager.getInstance().flush(); }

  @Override public void onBackPressed() { if (web.canGoBack()) web.goBack(); else super.onBackPressed(); }
}

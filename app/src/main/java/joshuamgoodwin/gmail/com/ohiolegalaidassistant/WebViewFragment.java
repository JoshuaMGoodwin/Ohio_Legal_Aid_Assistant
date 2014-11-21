package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.*;
import android.view.*;
import android.os.*;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.net.Uri;
import android.content.Context;
import java.io.File;

public class WebViewFragment extends Fragment
{

	private WebView webview;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.webview_layout, container, false);
		webview = (WebView) rootView.findViewById(R.id.webview);
		//webview.getSettings().setJavaScriptEnabled(true);
		//webview.loadUrl("www.cnn.com");
		return rootView;
		// TODO: Implement this method
		
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onViewCreated(view, savedInstanceState);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webview.setWebViewClient(new WebViewClient() {
        	@Override
      	 	public boolean shouldOverrideUrlLoading(WebView view, String url) {
            	view.loadUrl(url);
				boolean shouldOverride = false;
				if (url.endsWith(".docx") || url.endsWith(".doc") || url.endsWith(".pdf")) {
					shouldOverride = true;
					final DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

					// This is where downloaded files will be written, using the package name isn't required
					// but it's a good way to communicate who owns the directory
					final File destinationDir = new File (Environment.getExternalStorageDirectory(), getActivity().getPackageName());
					if (!destinationDir.exists()) {
						destinationDir.mkdir(); // Don't forget to make the directory if it's not there
					}
					Uri source = Uri.parse(url);

					// Make a new request pointin|g to the mp3 url
					DownloadManager.Request request = new DownloadManager.Request(source);
					// Use the same file name for the destination
					File destinationFile = new File (destinationDir, source.getLastPathSegment());
					request.setDestinationUri(Uri.fromFile(destinationFile));
					// Add it to the manager
					manager.enqueue(request);
				}
            	return shouldOverride;
        	}
    	});
		String address = getArguments().getString("address");
		webview.loadUrl(address);
		
		
	}
		
}

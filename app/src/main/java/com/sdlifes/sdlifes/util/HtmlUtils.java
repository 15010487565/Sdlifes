package com.sdlifes.sdlifes.util;

import android.webkit.WebView;

/**
 * Created by gs on 2020/8/18.
 */
public class HtmlUtils {

    public static void getHtmlData(String bodyHTML, WebView webView) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        webView.loadData("<html>" + head + "<body>" + bodyHTML + "</body></html>", "text/html; charset=UTF-8", null);
    }
}

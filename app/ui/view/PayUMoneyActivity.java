package com.bos.payment.appName.ui.view;

import static android.content.ContentValues.TAG;
import static com.bos.payment.appName.constant.CustomDateUtils.dateFormat;
import static com.bos.payment.appName.constant.CustomDateUtils.dateFormatServer12;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslError;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.bos.payment.appName.constant.CustomDateUtils;
import com.bos.payment.appName.databinding.TransactionFailedBinding;
import com.bos.payment.appName.data.model.AddFundModel;
import com.bos.payment.appName.ui.view.Dashboard.activity.DashboardActivity;
import com.bos.payment.appName.ui.viewmodel.AttendanceViewModel;
import com.bos.payment.appName.R;
import com.bos.payment.appName.utils.Constants;
import com.bos.payment.appName.utils.MStash;
import com.google.gson.Gson;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

//URL to implement Pay U Money
//https://stackoverflow.com/questions/29934378/how-to-integrate-payu-money-gateway-in-android
public class PayUMoneyActivity extends AppCompatActivity {
    WebView webView;
    Context activity;
    int mId;
    // Test Variables
   /* private String mMerchantKey = "FCyqqZ";
    private String mSalt = "sfBpGA8E";
    private String mBaseURL = "https://test.payu.in";*/

    // Final Variables
    private String mMerchantKey = "idpYvzfd";
    private String mSalt = "MDZsxiMB5h";
    private String mBaseURL = "https://secure.payu.in";


    private String mAction = ""; // For Final URL
    private String mTXNId; // This will create below randomly
    private String mHash; // This will create below randomly
    private String mProductInfo = "Food Items"; //Passing String only
    private String mFirstName; // From Previous Activity
    private String mEmailId; // From Previous Activity
    private double mAmount; // From Previous Activity
    private String mPhone; // From Previous Activity
    private String mServiceProvider = "payu_paisa";
    private String mSuccessUrl = "http://txncdn.payubiz.in/newResponse?mihpayid=";
    private String mFailedUrl = "Your Failure URL";

    private MStash mStash;
    private TransactionFailedBinding transactionFailedBinding;

    boolean isFromOrder;
    /**
     * Handler
     */
    Handler mHandler = new Handler();

    /**
     * @param savedInstanceState
     */

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled", "MissingInflatedId", "JavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);

        /**
         * Setting WebView to Screen
         */
        setContentView(R.layout.activity_pay_umoney);
        mStash = MStash.Companion.getInstance(this);

        /**
         * Creating WebView
         */
        webView = (WebView) findViewById(R.id.payumoney_webview);

        /**
         * Context Variable
         */
        activity = getApplicationContext();

        /**
         * Actionbar Settings
         */
        @SuppressLint("WrongViewCast") Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            // enabling action bar app icon and behaving it as toggle button
            ab.setHomeButtonEnabled(true);
            ab.setTitle("Pay U Money");
        }

        /**
         * Getting Intent Variables...
         */
        Bundle bundle = getIntent().getExtras();
        // if (bundle != null) {


        mFirstName = "Ashu Bhutani";
        mEmailId = "ashubhutani140@gmail.com";
        mAmount = Integer.parseInt(getIntent().getStringExtra("Amount"));
        // mPhone = "9812419368";
        mId = Integer.parseInt(generateRandomNumber());
        isFromOrder = false;

        Log.i(TAG, "" + mFirstName + " : " + mEmailId + " : " + mAmount + " : " + mPhone);

        /**
         * Creating Transaction Id
         */
        Random rand = new Random();
        String randomString = Integer.toString(rand.nextInt()) + (System.currentTimeMillis() / 1000L);
        mTXNId = hashCal("SHA-256", randomString).substring(0, 20);

        mAmount = new BigDecimal(mAmount).setScale(0, RoundingMode.UP).intValue();

        /**
         * Creating Hash Key
         */
        mHash = hashCal("SHA-512", mMerchantKey + "|" +
                mTXNId + "|" +
                mAmount + "|" +
                mProductInfo + "|" +
                mFirstName + "|" +
                mEmailId + "|||||||||||" +
                mSalt);

        /**
         * Final Action URL...
         */
        mAction = mBaseURL.concat("/_payment");

        /**
         * WebView Client
         */
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Toast.makeText(activity, "Oh no! " + error, Toast.LENGTH_SHORT).show();
            }

            //
            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(PayUMoneyActivity.this);
                String message = "SSL Certificate error. Do you want to continue anyway?\n";

                switch (error.getPrimaryError()) {
                    case SslError.SSL_UNTRUSTED:
                        message += "The certificate authority is not trusted.";
                        break;
                    case SslError.SSL_EXPIRED:
                        message += "The certificate has expired.";
                        break;
                    case SslError.SSL_IDMISMATCH:
                        message += "The certificate Hostname mismatch.";
                        break;
                    case SslError.SSL_NOTYETVALID:
                        message += "The certificate is not yet valid.";
                        break;
                    default:
                        message += "SSL Certificate error.";
                        break;
                }

                message += " Do you want to proceed anyway?";

                builder.setTitle("SSL Certificate Error");
                builder.setMessage(message);
                builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed(); // Proceed with the connection
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel(); // Cancel the connection
                    }
                });

                final AlertDialog dialog = builder.create();
                dialog.show();
            }

//            @Override
//            public void onReceivedSslError(WebView view,
//                                           SslErrorHandler handler, SslError error) {
//                Toast.makeText(activity, "SSL Error! " + error, Toast.LENGTH_SHORT).show();
//                handler.proceed();
//            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains(mSuccessUrl)) {
                    //   Toast.makeText(PayUMoneyActivity.this, "Payment Successfully.", Toast.LENGTH_SHORT).show();
                    successSevice(mTXNId);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e("URL", url);
                Log.e("mHash", url);
                Log.e("mSuccessUrl+mHash", mSuccessUrl + mHash);
                Log.e("mTXNId", mSuccessUrl + mHash);

                  /*  if (url.equals(mSuccessUrl+mHash)) {
                        Toast.makeText(PayUMoneyActivity.this, "Payment Successfully.", Toast.LENGTH_SHORT).show();
                        successSevice(mTXNId);
                    } else if (url.equals(mFailedUrl)) {
                        Toast.makeText(PayUMoneyActivity.this, "Payment Failed!", Toast.LENGTH_SHORT).show();
                        finish();
                    }*/
                super.onPageFinished(view, url);
            }
        });

        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDomStorageEnabled(true);
        webView.clearHistory();
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setLoadWithOverviewMode(false);
        webView.addJavascriptInterface(new PayUJavaScriptInterface(PayUMoneyActivity.this), "PayUMoney");

        /**
         * Mapping Compulsory Key Value Pairs
         */
        Map<String, String> mapParams = new HashMap<>();

        mapParams.put("key", mMerchantKey);
        mapParams.put("txnid", mTXNId);
        mapParams.put("amount", String.valueOf(mAmount));
        mapParams.put("productinfo", mProductInfo);
        mapParams.put("firstname", mFirstName);
        mapParams.put("email", mEmailId);
        mapParams.put("phone", mPhone);
        mapParams.put("surl", mSuccessUrl);
        mapParams.put("furl", mFailedUrl);
        mapParams.put("hash", mHash);
        mapParams.put("service_provider", mServiceProvider);

        webViewClientPost(webView, mAction, mapParams.entrySet());

    }

    /**
     * Posting Data on PayUMoney Site with Form
     *
     * @param webView
     * @param url
     * @param postData
     */
    public void webViewClientPost(WebView webView, String url,
                                  Collection<Map.Entry<String, String>> postData) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><head></head>");
        sb.append("<body onload='form1.submit()'>");
        sb.append(String.format("<form id='form1' action='%s' method='%s'>", url, "post"));

        for (Map.Entry<String, String> item : postData) {
            sb.append(String.format("<input name='%s' type='hidden' value='%s' />", item.getKey(), item.getValue()));
        }
        sb.append("</form></body></html>");

        Log.d("TAG", "webViewClientPost called: " + sb.toString());
        webView.loadData(sb.toString(), "text/html", "utf-8");
    }

    /**
     * Hash Key Calculation
     *
     * @param type
     * @param str
     * @return
     */
    public String hashCal(String type, String str) {
        byte[] hashSequence = str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashSequence);
            byte messageDigest[] = algorithm.digest();

            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1)
                    hexString.append("0");
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException NSAE) {
        }
        return hexString.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onPressingBack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        onPressingBack();
    }

    /**
     * On Pressing Back
     * Giving Alert...
     */
    private void onPressingBack() {

        final Intent intent;

        if (isFromOrder)
            intent = new Intent(PayUMoneyActivity.this, PayUMoneyActivity.class);
        else
            intent = new Intent(PayUMoneyActivity.this, PayUMoneyActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PayUMoneyActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Warning");

        // Setting Dialog Message
        alertDialog.setMessage("Do you cancel this transaction?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(activity, mTXNId.toString(), Toast.LENGTH_SHORT).show();
                final Dialog dialog1 = new Dialog(PayUMoneyActivity.this);
                transactionFailedBinding = TransactionFailedBinding.inflate(getLayoutInflater());
                dialog1.setContentView(transactionFailedBinding.getRoot());
                transactionFailedBinding.rechargeAmount.setText(String.valueOf(mAmount));
                transactionFailedBinding.transacton.setText(mTXNId);
                transactionFailedBinding.ivBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(PayUMoneyActivity.this, DashboardActivity.class));
                        dialog1.dismiss();
                    }
                });
//                startActivity(new Intent(PayUMoneyActivity.this, RechargeActivity.class));
                dialog1.show();
                Window window = dialog1.getWindow();
                assert window != null;
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

//                finish();
//                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public class PayUJavaScriptInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        PayUJavaScriptInterface(Context c) {
            mContext = c;
        }

        public void success(long id, final String paymentId) {
            mHandler.post(new Runnable() {

                @RequiresApi(api = Build.VERSION_CODES.M)
                public void run() {
                    mHandler = null;
                    //  successSevice(paymentId);
                }
            });
        }
    }

    public static String generateRandomNumber() {
        Random random = new Random();
        HashSet<String> uniqueNumbers = new HashSet<>();

        while (uniqueNumbers.size() < 5) {
            int randomNumber = random.nextInt(90000000) + 10000000;
            uniqueNumbers.add(Integer.toString(randomNumber));
        }

        return uniqueNumbers.iterator().next();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void successSevice(String paymentID) {
        String getRandom_id = generateRandomNumber();
//        String login_id = MySharedPreference.INSTANCE.getUserModelData(activity).component5();
        String login_id = mStash.getStringValue(Constants.INSTANCE.getRegistrationId(), "");
        AddFundModel addFundModel = new AddFundModel(getIP(), getRandom_id, getRandom_id, "test Naim", "test Naim2", "Deposit",
                "Test Name", Objects.requireNonNull(CustomDateUtils.INSTANCE.getCurrentDate(dateFormat)), "Admin",
                login_id, String.valueOf(mAmount),
                Objects.requireNonNull(CustomDateUtils.INSTANCE.getCurrentDate(dateFormatServer12)), "Admin",
                Objects.requireNonNull(CustomDateUtils.INSTANCE.getCurrentDate(dateFormatServer12)),
                "CMP1045" + "_CC_" + login_id + "_" + getRandom_id, "CMP1045");

        Log.d("successSevice: ", new Gson().toJson(addFundModel));
        new ViewModelProvider(this).get(AttendanceViewModel.class)
                .addFund(addFundModel)
                .observe(this, response -> {
                    if (response == null) {
                        Toast.makeText(activity, "Unknown error", Toast.LENGTH_SHORT).show();
                    } else if (response != null && !nullChecker(response.component3()).equals("")) {
                        Toast.makeText(PayUMoneyActivity.this, response.component3(), Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(this, LoginActivity.class));
                        Toast.makeText(PayUMoneyActivity.this, "Payment Successfully.", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                });
    }

    private String getIP() {
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }

    private String nullChecker(String value) {
        if (value == null) {
            value = "";
        }
        return value;
    }

    private String nullChecker2(String value) {
        if (value == null) {
            value = "cmp1045";
        }
        return value;
    }
}
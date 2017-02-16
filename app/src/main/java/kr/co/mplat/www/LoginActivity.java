package kr.co.mplat.www;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginDefine;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener,I_loaddata,I_startFinish{
    //final int CALLTYPE_SAVE = 1;
    final int CALLTYPE_LOGINCHECK = 2;
    final int CHANNEL_TYPE_MPLAT = 1;
    final int CHANNEL_TYPE_NAVER = 2;
    final int CHANNEL_TYPE_FACEBOOK = 3;
    final int CHANNEL_TYPE_KAKAO = 4;
    final int CHANNEL_TYPE_GOOGLE = 5;
    Common common = null;
    Intent intent = null;
    EditText login_etId = null;
    EditText login_etPw = null;
    TextView login_tvMessage;
    String version,sns_id,sns_type,sns_email = "";
    private boolean busyActivity = false;
    private int dialogType = 0;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    SessionCallback callback;
    private static final String TAG = "wtKim";
    /* 네이버 로그인*/
     //client 정보를 넣어준다.
    private static String OAUTH_CLIENT_ID = "Sa1FQaklKVXlRD815i5o";
    private static String OAUTH_CLIENT_SECRET = "kQPcGdfmee";
    private static String OAUTH_CLIENT_NAME = "엠플렛";

    private static OAuthLogin mOAuthLoginInstance;
    Map<String,String> mUserInfoMap;
    private static Context mContext;
    Activity mActivity;

    /** UI 요소들 */
    private TextView mApiResultText;
    private static TextView mOauthAT;
    private static TextView mOauthRT;
    private static TextView mOauthExpires;
    private static TextView mOauthTokenType;
    private static TextView mOAuthState;
    private OAuthLoginButton mOAuthLoginButton;

    /* 네이버 로그인 끝*/

    /* 구글 로그인 */
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private TextView mDetailTextView;

    /* 구글 로그인 끝 */

    /*페이스북 로그인 */
    ////FacebookLogin
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private TextView btnLogin;
    private ProgressDialog progressDialog;
    User user;

    /*페이스북 로그인 끝 */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize SDK before setContentView(Layout ID)
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        setTvTitle("로그인");
        String UID = Common.getPreference(getApplicationContext(), "UID");
        String KEY = Common.getPreference(getApplicationContext(), "KEY");
        Log.i("comm-loginActivity","UID==>"+UID);
        Log.i("comm-loginActivity","KEY==>"+KEY);

        //구글 로그인
        // Views
        //mStatusTextView = (TextView) findViewById(R.id.status);
        //mDetailTextView = (TextView) findViewById(R.id.detail);

        // Button listeners
        //findViewById(R.id.sign_in_button).setOnClickListener(this);
        //findViewById(R.id.sign_out_button).setOnClickListener(this);
        //findViewById(R.id.disconnect_button).setOnClickListener(this);

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    //Log.d("wtkim", "FirebaseAuth=>getUid:" + user.getUid());
                    //Log.d("wtkim", "FirebaseAuth=>getUid:" + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                updateUI(user);
                // [END_EXCLUDE]
            }
        };
        // [END auth_state_listener]
        //구글 로그인 끝
        //페이스북
        if(PrefUtils.getCurrentUser(LoginActivity.this) != null){
            PrefUtils.clearCurrentUser(LoginActivity.this);
            //Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);
            //startActivity(homeIntent);

            //finish();
        }
        //페이스북 끝

        /**카카오톡 로그아웃 요청**/
        //한번 로그인이 성공하면 세션 정보가 남아있어서 로그인창이 뜨지 않고 바로 onSuccess()메서드를 호출합니다.
        //테스트 하시기 편하라고 매번 로그아웃 요청을 수행하도록 코드를 넣었습니다 ^^
       /* UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                //로그아웃 성공 후 하고싶은 내용 코딩 ~
                Log.i("wtKim","테스트 로그아웃");
            }
        });*/

        //네이버 로그인
        OAuthLoginDefine.DEVELOPER_VERSION = true;
        mContext = this;
        mActivity = this;
        initNaver();
        //initView();


        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);

        //7A+7NIEZ5TA4vOncz6Gqi78HiU0=

        common = new Common(this);
        login_etId = (EditText) findViewById(R.id.login_etId);
        login_etPw  = (EditText) findViewById(R.id.login_etPw);
        login_etPw.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        login_etPw.setTransformationMethod(PasswordTransformationMethod.getInstance());
        //영문키보드 보이도록함
        login_etPw.setPrivateImeOptions("defaultInputmode=english;");
        ///sns_type 은 기본1 (MPLAT)으로 합니다.
        sns_type = String.valueOf(CHANNEL_TYPE_MPLAT);
        //앱 버전체크
        version = Common.getAppVersion(this).toString();
        //기존이미지 대체
        final com.kakao.usermgmt.LoginButton btn_com_kakao_login = (com.kakao.usermgmt.LoginButton) findViewById(R.id.com_kakao_login);
        //카카오 로그인버튼 클릭이벤트 등록
        findViewById(R.id.login_ibKakao).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                btn_com_kakao_login.performClick();
            }
        });
        //네이버 로그인버튼 클릭이벤트 등록
        findViewById(R.id.login_ibNaver).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mOAuthLoginInstance.startOauthLoginActivity(mActivity, mOAuthLoginHandler);
            }
        });
        //구글 로그인버튼 클릭이벤트 등록
        findViewById(R.id.login_ibGoogle).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i("wtkim","구글로그인버튼 클릭!");
                //findViewById(R.id.sign_in_button).performClick();
                signIn();
                //signOut();
            }
        });
        //페이스북 로그인버튼 클릭이벤트 등록
        /*findViewById(R.id.login_ibFacebook).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i("wtkim","페이스북로그인버튼 클릭!");

            }
        });*/
    }


    /****************************************************************************************************************
     * 구글 (firebase)
     ****************************************************************************************************************/
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    // [END on_stop_remove_listener]

    /****************************************************************************************************************
     * 구글 끝
     ****************************************************************************************************************/
    /****************************************************************************************************************
     * 페이스북
     ****************************************************************************************************************/



    /****************************************************************************************************************
     * 페이스북 끝
     ****************************************************************************************************************/

    /****************************************************************************************************************
     * 네이버
     ****************************************************************************************************************/
    private void initNaver() {
        mOAuthLoginInstance = OAuthLogin.getInstance();
        mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);
		/*
		 * 2015년 8월 이전에 등록하고 앱 정보 갱신을 안한 경우 기존에 설정해준 callback intent url 을 넣어줘야 로그인하는데 문제가 안생긴다.
		 * 2015년 8월 이후에 등록했거나 그 뒤에 앱 정보 갱신을 하면서 package name 을 넣어준 경우 callback intent url 을 생략해도 된다.
		 */
        //mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME, OAUTH_callback_intent_url);
    }


    /**
     * startOAuthLoginActivity() 호출시 인자로 넘기거나, OAuthLoginButton 에 등록해주면 인증이 종료되는 걸 알 수 있다.
     */
     private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
                String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
                long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
                String tokenType = mOAuthLoginInstance.getTokenType(mContext);
                new RequestApiTask().execute();

            } else {
                String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);
                Toast.makeText(mContext, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        };
    };

    private class DeleteTokenTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            boolean isSuccessDeleteToken = mOAuthLoginInstance.logoutAndDeleteToken(mContext);

            if (!isSuccessDeleteToken) {
                // 서버에서 token 삭제에 실패했어도 클라이언트에 있는 token 은 삭제되어 로그아웃된 상태이다
                // 실패했어도 클라이언트 상에 token 정보가 없기 때문에 추가적으로 해줄 수 있는 것은 없음
                Log.d(TAG, "errorCode:" + mOAuthLoginInstance.getLastErrorCode(mContext));
                Log.d(TAG, "errorDesc:" + mOAuthLoginInstance.getLastErrorDesc(mContext));
            }
            return null;
        }
        protected void onPostExecute(Void v) {
            //updateView();
        }
    }
    private class RequestApiTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            //mApiResultText.setText((String) "");
        }
        @Override
        protected String doInBackground(Void... params) {
            String url = "https://openapi.naver.com/v1/nid/getUserProfile.xml";
            String at = mOAuthLoginInstance.getAccessToken(mContext);
            mUserInfoMap = requestNaverUserInfo(mOAuthLoginInstance.requestApi(mContext, at, url));
            //return mOAuthLoginInstance.requestApi(mContext, at, url);
            return null;
        }
        protected void onPostExecute(String content) {
            //mApiResultText.setText((String) content);
            if (mUserInfoMap.get("email") == null) {
                Toast.makeText(mContext, "로그인 실패하였습니다.  잠시후 다시 시도해 주세요!!", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(LoginActivity.this, "네이버아이디 인증에 성공하였습니다.", Toast.LENGTH_SHORT).show();

                sns_id = String.valueOf(mUserInfoMap.get("enc_id"));
                sns_email = String.valueOf(mUserInfoMap.get("email"));

                //네이버 로그인 성공시
                Object[][] params = {
                        {"REGIST_JOIN_CHANNEL_TYPE", String.valueOf(CHANNEL_TYPE_NAVER)}
                        ,{"SNS_ID", sns_id}
                        ,{"AGENT", String.valueOf(version)}
                };
                common.loadData(CHANNEL_TYPE_NAVER, getString(R.string.url_snsLogin), params);

                /*Log.d("1111==>", String.valueOf(mUserInfoMap));
                Log.d("email==>",mUserInfoMap.get("email"));
                Log.d("nickname==>",mUserInfoMap.get("nickname"));
                Log.d("name==>",mUserInfoMap.get("name"));
                Log.d("gender==>",mUserInfoMap.get("gender"));
                Log.d("enc_id==>",mUserInfoMap.get("enc_id"));
                Log.d("birthday==>",mUserInfoMap.get("birthday"));*/
            }
        }
    }

    private class RefreshTokenTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            return mOAuthLoginInstance.refreshAccessToken(mContext);
        }
        protected void onPostExecute(String res) {
            //updateView();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //간편로그인시 호출 ,없으면 간편로그인시 로그인 성공화면으로 넘어가지 않음
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);

        //페이스북
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                Log.i("wtkim","getEmail==>"+account.getEmail());
                Log.i("wtkim","getId==>"+account.getId());
                sns_email = account.getEmail();
                sns_id = account.getId();
                //Toast.makeText(LoginActivity.this, "구글인증에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                //구글 로그인 성공시
                Object[][] params = {
                        {"REGIST_JOIN_CHANNEL_TYPE", String.valueOf(CHANNEL_TYPE_GOOGLE)}
                        ,{"SNS_ID", sns_id}
                        ,{"AGENT", String.valueOf(version)}
                };
                common.loadData(CHANNEL_TYPE_GOOGLE, getString(R.string.url_snsLogin), params);
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }



    }
    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            progressDialog.dismiss();

            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            Log.e("response: ", response + "");
                            try {
                                user = new User();
                                user.facebookID = object.getString("id").toString();
                                if(object.has("email")){
                                    user.email = object.getString("email").toString();
                                }
                                //user.name = object.getString("name").toString();
                                //user.gender = object.getString("gender").toString();
                                //PrefUtils.setCurrentUser(user,LoginActivity.this);

                                sns_id = user.facebookID;
                                sns_email = user.email;


                                //Toast.makeText(LoginActivity.this,"페이스북인증에 성공하였습니다",Toast.LENGTH_LONG).show();
                                //페이스북 로그인 성공시
                                Object[][] params = {
                                        {"REGIST_JOIN_CHANNEL_TYPE", String.valueOf(CHANNEL_TYPE_FACEBOOK)}
                                        ,{"SNS_ID", sns_id}
                                        ,{"AGENT", String.valueOf(version)}
                                };
                                common.loadData(CHANNEL_TYPE_FACEBOOK, getString(R.string.url_snsLogin), params);


                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            /*Intent intent=new Intent(LoginActivity.this,LogoutActivity.class);
                            startActivity(intent);*/
                            finish();

                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException e) {
            progressDialog.dismiss();
        }
    };
    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]
    // [START signin]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            //mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
            //mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            //findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            //findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            //mStatusTextView.setText(R.string.signed_out);
            //mDetailTextView.setText(null);

            //findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            //findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            UserManagement.requestMe(new MeResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "정보를 가져오는데 실패하였습니다 : " + errorResult;
                    Logger.d(message);

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        finish();
                    } else {
                        //redirectMainActivity();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {}

                @Override
                public void onNotSignedUp() {}

                @Override
                public void onSuccess(UserProfile userProfile) {
                    //Toast.makeText(LoginActivity.this, "카카오아이디 인증에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                    //로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
                    //사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.
                    /*Log.e("UserProfile", userProfile.toString());
                    Log.e("UserId",String.valueOf(userProfile.getId()));*/
                    sns_id = String.valueOf(userProfile.getId());
                    Log.i("wtkim","sns_id==>"+sns_id);
                    //카카오 로그인 성공시
                    Object[][] params = {
                             {"REGIST_JOIN_CHANNEL_TYPE", String.valueOf(CHANNEL_TYPE_KAKAO)}
                            ,{"SNS_ID", sns_id}
                            ,{"AGENT", String.valueOf(version)}
                    };
                    common.loadData(CHANNEL_TYPE_KAKAO, getString(R.string.url_snsLogin), params);
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            //intent = new Intent(LoginActivity.this,LoginActivity.class);
            //startActivity(intent);
            //Toast.makeText(LoginActivity.this, "카카오아이디 인증이 실패하였습니다.", Toast.LENGTH_SHORT).show();
            // 세션 연결이 실패했을때
            // 어쩔때 실패되는지는 테스트를 안해보았음 ㅜㅜ
        }
    }
    private Map<String,String> requestNaverUserInfo(String data) { // xml 파싱
        String f_array[] = new String[9];

        try {
            XmlPullParserFactory parserCreator = XmlPullParserFactory
                    .newInstance();
            XmlPullParser parser = parserCreator.newPullParser();
            InputStream input = new ByteArrayInputStream(data.getBytes("UTF-8"));
            parser.setInput(input, "UTF-8");

            int parserEvent = parser.getEventType();
            String tag;
            boolean inText = false;
            boolean lastMatTag = false;

            int colIdx = 0;

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        tag = parser.getName();
                        if (tag.compareTo("xml") == 0) {
                            inText = false;
                        } else if (tag.compareTo("data") == 0) {
                            inText = false;
                        } else if (tag.compareTo("result") == 0) {
                            inText = false;
                        } else if (tag.compareTo("resultcode") == 0) {
                            inText = false;
                        } else if (tag.compareTo("message") == 0) {
                            inText = false;
                        } else if (tag.compareTo("response") == 0) {
                            inText = false;
                        } else {
                            inText = true;

                        }
                        break;
                    case XmlPullParser.TEXT:
                        tag = parser.getName();
                        if (inText) {
                            if (parser.getText() == null) {
                                f_array[colIdx] = "";
                            } else {
                                f_array[colIdx] = parser.getText().trim();
                            }

                            colIdx++;
                        }
                        inText = false;
                        break;
                    case XmlPullParser.END_TAG:
                        tag = parser.getName();
                        inText = false;
                        break;

                }

                parserEvent = parser.next();
            }
        } catch (Exception e) {
            Log.e("dd", "Error in network call", e);
        }
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("email"           ,f_array[0]);
        resultMap.put("nickname"        ,f_array[1]);
        resultMap.put("enc_id"          ,f_array[2]);
        resultMap.put("profile_image"   ,f_array[3]);
        resultMap.put("age"             ,f_array[4]);
        resultMap.put("gender"          ,f_array[5]);
        resultMap.put("id"              ,f_array[6]);
        resultMap.put("name"            ,f_array[7]);
        resultMap.put("birthday"        ,f_array[8]);
        return resultMap;
    }
    //네이버 로그인 끝


    @Override
    protected void onResume() {
        LoginManager.getInstance().logOut();
        String UID = Common.getPreference(getApplicationContext(), "UID");
        String KEY = Common.getPreference(getApplicationContext(), "KEY");
        if (!UID.equals("") && !KEY.equals("")) {
            Common.setPreference(getApplicationContext(), "UID", UID);
            Common.setPreference(getApplicationContext(), "KEY", KEY);


            intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            super.onResume();
            start(null);
            return;
        }
        busyActivity = false;
        //네이버 로그인
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //네이버 로그인끝
        super.onResume();

        //페이스북
        callbackManager=CallbackManager.Factory.create();

        loginButton= (LoginButton)findViewById(R.id.login_button);

        loginButton.setReadPermissions("public_profile", "email","user_friends");

        ImageButton btnLogin= (ImageButton) findViewById(R.id.login_ibFacebook);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(PrefUtils.getCurrentUser(LoginActivity.this) != null){
                    PrefUtils.clearCurrentUser(LoginActivity.this);
                    LoginManager.getInstance().logOut();
                    //Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);
                    //startActivity(homeIntent);

                    //finish();
                }*/
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                loginButton.performClick();

                loginButton.setPressed(true);

                loginButton.invalidate();

                loginButton.registerCallback(callbackManager, mCallBack);

                loginButton.setPressed(false);

                loginButton.invalidate();

            }
        });
        //페이스북 끝
    }

    @Override
    public void onBackPressed() {
        dialogType = 9;//종료타입
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime){
            //super.onBackPressed();
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }else {
            backPressedTime = tempTime;
            Toast.makeText(this, "한번 더 누르면 MPLAT을 종료합니다", Toast.LENGTH_SHORT).show();
            /*common.setPreference(getApplicationContext(), "UID", "");
            common.setPreference(getApplicationContext(), "KEY", "");*/

        }
    }

    @Override
    public void start(View view) {
        //네트워크 상태 확인
        if(!common.isConnected()) {
            common.showCheckNetworkDialog();
            return;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_btnLogin:
                String id = login_etId.getText().toString().trim();
                String pwd = login_etPw.getText().toString().trim();

                login_tvMessage = (TextView) findViewById(R.id.login_tvMessage);
                if(id.equals("")){
                    login_tvMessage.setText("아이디 또는 비밀번호를 다시 한번 확인 해주세요.");
                    return;
                }else if(pwd.equals("")){
                    login_tvMessage.setText("아이디 또는 비밀번호를 다시 한번 확인 해주세요.");
                    return;
                }
                String NETWORK_OPERATOR = "";
                String MOBILE_TEL = "";
                try {
                    TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                    NETWORK_OPERATOR = tm.getNetworkOperatorName();
                    MOBILE_TEL = tm.getLine1Number();
                    MOBILE_TEL = MOBILE_TEL.substring(MOBILE_TEL.length() - 10, MOBILE_TEL.length());
                    MOBILE_TEL = "0" + MOBILE_TEL;
                } catch (Exception e) {
                    NETWORK_OPERATOR = "";
                    MOBILE_TEL = "";
                }

                String DEVICE_ID = Common.getPreference(getApplicationContext(), "DEVICE_ID");
                String BOARD = Build.BOARD;
                String BRAND = Build.BRAND;
                String DEVICE = Build.DEVICE;
                String DISPLAY = Build.DISPLAY;
                String FINGERPRINT = Build.FINGERPRINT;
                String HOST = Build.HOST;
                String INFO_ID = Build.ID;
                String MANUFACTURER = Build.MANUFACTURER;
                String MODEL = Build.MODEL;
                String PRODUCT = Build.PRODUCT;
                String TAGS = Build.TAGS;
                String TYPE = Build.TYPE;
                String USER = Build.USER;
                String VERSION_RELEASE = Build.VERSION.RELEASE;
                if (NETWORK_OPERATOR == null) NETWORK_OPERATOR = "";
                if (BOARD == null) BOARD = "";
                if (BRAND == null) BRAND = "";
                if (DEVICE == null) DEVICE = "";
                if (DISPLAY == null) DISPLAY = "";
                if (FINGERPRINT == null) FINGERPRINT = "";
                if (HOST == null) HOST = "";
                if (INFO_ID == null) INFO_ID = "";
                if (MANUFACTURER == null) MANUFACTURER = "";
                if (MODEL == null) MODEL = "";
                if (PRODUCT == null) PRODUCT = "";
                if (TAGS == null) TAGS = "";
                if (TYPE == null) TYPE = "";
                if (USER == null) USER = "";
                if (VERSION_RELEASE == null) VERSION_RELEASE = "";

                Object[][] params = {
                          {"ID", id}, {"PW", pwd}, {"AGENT",version}
                };

                common.loadData(CHANNEL_TYPE_MPLAT, getString(R.string.url_login), params);
                break;
            //아이디 찾기
            case R.id.login_tvSearchId :
                intent = new Intent(LoginActivity.this,SearchIdActivity.class);
                startActivity(intent);
                break;
            //비밀번호 찾기
            case R.id.login_tvSearchPw :
                intent = new Intent(LoginActivity.this,SearchPwActivity.class);
                startActivity(intent);
                break;
            //회원가입
            case R.id.login_tvRegist :
                intent = new Intent(LoginActivity.this,AgreementActivity.class);
                intent.putExtra("SNS_ID",String.valueOf(sns_id));
                intent.putExtra("SNS_TYPE",String.valueOf(CHANNEL_TYPE_MPLAT));
                intent.putExtra("SNS_EMAIL",sns_email);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CHANNEL_TYPE_MPLAT) saveHandler(str);
        else if (calltype == CHANNEL_TYPE_KAKAO) kakaoHandler(str);
        else if (calltype == CHANNEL_TYPE_NAVER) naverHandler(str);
        else if (calltype == CHANNEL_TYPE_GOOGLE) googleHandler(str);
        else if (calltype == CHANNEL_TYPE_FACEBOOK) facebookHandler(str);
    }

    //저장 처리
    public void saveHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                if (result.equals("OK")) {
                    String UID = json.getString("UID");
                    String KEY = json.getString("KEY");
                    Common.setPreference(getApplicationContext(), "UID", UID);
                    Common.setPreference(getApplicationContext(), "KEY", KEY);
                    //this.finish();
                    intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }else if(result.equals("WITHDRAW")){
                    //탈퇴회원일경우, 탈퇴취소페이지로 보냄
                    String uid = json.getString("UID");
                    String key = json.getString("KEY");
                    Common.setPreference(getApplicationContext(), "UID", uid);
                    Common.setPreference(getApplicationContext(), "KEY", key);

                    String withdraw_date = json.getString("WITHDRAW_DATE");
                    String delete_date = json.getString("DELETE_DATE");
                    String email = json.getString("EMAIL");
                    intent = new Intent(LoginActivity.this,WithdrawCancelActivity.class);
                    /*intent.putExtra("UID",String.valueOf(uid));
                    intent.putExtra("KEY",String.valueOf(key));*/
                    intent.putExtra("WITHDRAW_DATE",String.valueOf(withdraw_date));
                    intent.putExtra("DELETE_DATE",String.valueOf(delete_date));
                    intent.putExtra("EMAIL",String.valueOf(email));
                    startActivity(intent);
                    finish();
                }
            } else {
                login_tvMessage.setText(err);
            }
        } catch (Exception e) {
            Log.i("wtKim", e.toString());
        }
    }

    //카카오 로그인 핸들러
    public void kakaoHandler(String str){
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                Log.i("wtkim","result==>"+result);
                if (result.equals("OK")) {
                    Log.i("wtkim","카카오가입했음");
                   //로그인 성공(기존회원가입함)
                    //this.finish();
                    String uid = json.getString("UID");
                    String key = json.getString("KEY");
                    Common.setPreference(getApplicationContext(), "UID", uid);
                    Common.setPreference(getApplicationContext(), "KEY", key);
                    intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }else if(result.equals("NEED JOIN")){
                    //가입을 안한 회원이 카카오 로그인시 가입으로 보냄
                    intent = new Intent(LoginActivity.this,AgreementActivity.class);
                    intent.putExtra("SNS_ID",String.valueOf(sns_id));
                    intent.putExtra("SNS_TYPE",String.valueOf(CHANNEL_TYPE_KAKAO));
                    intent.putExtra("SNS_EMAIL",sns_email);

                    startActivity(intent);
                }else if(result.equals("WITHDRAW")){
                    //탈퇴회원일경우, 탈퇴취소페이지로 보냄
                    String uid = json.getString("UID");
                    String key = json.getString("KEY");
                    Common.setPreference(getApplicationContext(), "UID", uid);
                    Common.setPreference(getApplicationContext(), "KEY", key);

                    String withdraw_date = json.getString("WITHDRAW_DATE");
                    String delete_date = json.getString("DELETE_DATE");
                    String email = json.getString("EMAIL");
                    intent = new Intent(LoginActivity.this,WithdrawCancelActivity.class);
                    /*intent.putExtra("UID",String.valueOf(uid));
                    intent.putExtra("KEY",String.valueOf(key));*/
                    intent.putExtra("WITHDRAW_DATE",String.valueOf(withdraw_date));
                    intent.putExtra("DELETE_DATE",String.valueOf(delete_date));
                    intent.putExtra("EMAIL",String.valueOf(email));
                    startActivity(intent);
                    finish();
                }
            } else {
                Log.i("wtKim","정보가져오기 실패!");
            }
        } catch (Exception e) {
            Log.i("wtKim", e.toString());
        }
    }

    //네이버 로그인 핸들러
    public void naverHandler(String str){
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                Log.i("wtkim","result==>"+result);
                if (result.equals("OK")) {
                    Log.i("wtkim","네이버가입했음");
                    //로그인 성공(기존회원가입함)
                    //this.finish();
                    String uid = json.getString("UID");
                    String key = json.getString("KEY");
                    Common.setPreference(getApplicationContext(), "UID", uid);
                    Common.setPreference(getApplicationContext(), "KEY", key);
                    intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }else if(result.equals("NEED JOIN")){
                    //가입을 안한 회원이 네이버 로그인시 가입으로 보냄
                    intent = new Intent(LoginActivity.this,AgreementActivity.class);
                    intent.putExtra("SNS_ID",String.valueOf(sns_id));
                    intent.putExtra("SNS_TYPE",String.valueOf(CHANNEL_TYPE_NAVER));
                    intent.putExtra("SNS_EMAIL",sns_email);
                    startActivity(intent);
                }else if(result.equals("WITHDRAW")){
                    //탈퇴회원일경우, 탈퇴취소페이지로 보냄
                    String uid = json.getString("UID");
                    String key = json.getString("KEY");
                    Common.setPreference(getApplicationContext(), "UID", uid);
                    Common.setPreference(getApplicationContext(), "KEY", key);

                    String withdraw_date = json.getString("WITHDRAW_DATE");
                    String delete_date = json.getString("DELETE_DATE");
                    String email = json.getString("EMAIL");
                    intent = new Intent(LoginActivity.this,WithdrawCancelActivity.class);
                    /*intent.putExtra("UID",String.valueOf(uid));
                    intent.putExtra("KEY",String.valueOf(key));*/
                    intent.putExtra("WITHDRAW_DATE",String.valueOf(withdraw_date));
                    intent.putExtra("DELETE_DATE",String.valueOf(delete_date));
                    intent.putExtra("EMAIL",String.valueOf(email));
                    startActivity(intent);
                    finish();
                }
            } else {
                Log.i("wtKim","정보가져오기 실패!");
            }
        } catch (Exception e) {
            Log.i("wtKim", e.toString());
        }
    }

    //구글 로그인 핸들러
    public void googleHandler(String str){
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                Log.i("wtkim","result==>"+result);
                if (result.equals("OK")) {
                    Log.i("wtkim","구글가입했음");
                    //로그인 성공(기존회원가입함)
                    //this.finish();
                    String uid = json.getString("UID");
                    String key = json.getString("KEY");
                    Common.setPreference(getApplicationContext(), "UID", uid);
                    Common.setPreference(getApplicationContext(), "KEY", key);
                    intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }else if(result.equals("NEED JOIN")){
                    //가입을 안한 회원이 카카오 로그인시 가입으로 보냄
                    intent = new Intent(LoginActivity.this,AgreementActivity.class);
                    intent.putExtra("SNS_ID",String.valueOf(sns_id));
                    intent.putExtra("SNS_TYPE",String.valueOf(CHANNEL_TYPE_GOOGLE));
                    intent.putExtra("SNS_EMAIL",sns_email);
                    startActivity(intent);
                }else if(result.equals("WITHDRAW")){
                    //탈퇴회원일경우, 탈퇴취소페이지로 보냄
                    String uid = json.getString("UID");
                    String key = json.getString("KEY");
                    Common.setPreference(getApplicationContext(), "UID", uid);
                    Common.setPreference(getApplicationContext(), "KEY", key);

                    String withdraw_date = json.getString("WITHDRAW_DATE");
                    String delete_date = json.getString("DELETE_DATE");
                    String email = json.getString("EMAIL");
                    intent = new Intent(LoginActivity.this,WithdrawCancelActivity.class);
                    /*intent.putExtra("UID",String.valueOf(uid));
                    intent.putExtra("KEY",String.valueOf(key));*/
                    intent.putExtra("WITHDRAW_DATE",String.valueOf(withdraw_date));
                    intent.putExtra("DELETE_DATE",String.valueOf(delete_date));
                    intent.putExtra("EMAIL",String.valueOf(email));
                    startActivity(intent);
                    finish();
                }
            } else {
                Log.i("wtKim","정보가져오기 실패!");
            }
        } catch (Exception e) {
            Log.i("wtKim", e.toString());
        }
    }

    //페이스북 로그인 핸들러
    public void facebookHandler(String str){
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                if (result.equals("OK")) {
                    Log.i("wtkim","페이스북가입했음");
                    //로그인 성공(기존회원가입함)
                    //this.finish();
                    String uid = json.getString("UID");
                    String key = json.getString("KEY");
                    Common.setPreference(getApplicationContext(), "UID", uid);
                    Common.setPreference(getApplicationContext(), "KEY", key);
                    Log.i("wtkim","UID==>"+uid);
                    Log.i("wtkim","KEY==>"+key);
                    intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }else if(result.equals("NEED JOIN")){
                    //가입을 안한 회원이 페이스북 로그인시 가입으로 보냄
                    intent = new Intent(LoginActivity.this,AgreementActivity.class);
                    intent.putExtra("SNS_ID",String.valueOf(sns_id));
                    intent.putExtra("SNS_TYPE",String.valueOf(CHANNEL_TYPE_FACEBOOK));
                    intent.putExtra("SNS_EMAIL",sns_email);
                    startActivity(intent);
                }else if(result.equals("WITHDRAW")){
                    //탈퇴회원일경우, 탈퇴취소페이지로 보냄
                    String uid = json.getString("UID");
                    String key = json.getString("KEY");
                    Common.setPreference(getApplicationContext(), "UID", uid);
                    Common.setPreference(getApplicationContext(), "KEY", key);

                    String withdraw_date = json.getString("WITHDRAW_DATE");
                    String delete_date = json.getString("DELETE_DATE");
                    String email = json.getString("EMAIL");
                    intent = new Intent(LoginActivity.this,WithdrawCancelActivity.class);
                    /*intent.putExtra("UID",String.valueOf(uid));
                    intent.putExtra("KEY",String.valueOf(key));*/
                    intent.putExtra("WITHDRAW_DATE",String.valueOf(withdraw_date));
                    intent.putExtra("DELETE_DATE",String.valueOf(delete_date));
                    intent.putExtra("EMAIL",String.valueOf(email));
                    startActivity(intent);
                    finish();
                }
            } else {
                Log.i("wtKim","정보가져오기 실패!");
            }
        } catch (Exception e) {
            Log.i("wtKim", e.toString());
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        setBackBtnVisible(false);
        return ret;
    }
}

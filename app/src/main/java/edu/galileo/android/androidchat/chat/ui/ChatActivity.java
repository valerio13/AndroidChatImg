package edu.galileo.android.androidchat.chat.ui;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.provider.MediaStore;
import android.net.Uri;
import android.os.Parcelable;
import android.content.pm.ResolveInfo;
import android.support.design.widget.Snackbar;
import android.content.Context;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import edu.galileo.android.androidchat.R;
import edu.galileo.android.androidchat.addMultimedia.ui.AddMultimediaFragment;
import edu.galileo.android.androidchat.addcontact.ui.AddContactFragment;
import edu.galileo.android.androidchat.chat.ui.adapters.ChatAdapter;
import edu.galileo.android.androidchat.chat.ChatPresenter;
import edu.galileo.android.androidchat.chat.chatPresenterImpl;
import edu.galileo.android.androidchat.domain.AvatarHelper;
import edu.galileo.android.androidchat.entities.ChatMessage;
import edu.galileo.android.androidchat.lib.GlideImageLoader;
import edu.galileo.android.androidchat.lib.ImageLoader;

public class ChatActivity extends AppCompatActivity implements ChatView {

    @Bind(R.id.imgAvatar)
    CircleImageView imgAvatar;
    @Bind(R.id.txtUser)
    TextView txtUser;
    @Bind(R.id.txtStatus)
    TextView txtStatus;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.messageRecyclerView)
    RecyclerView messageRecyclerView;
    @Bind(R.id.editTxtMessage)
    EditText editTxtMessage;


    private ChatAdapter adapter;
    private ChatPresenter presenter;
    private String photoPath;

    //Nuevo
    private ImageView imgChat;

    public static final String EMAIL_KEY = "email";
    public static final String ONLINE_KEY = "online";
    private static final int REQUEST_PICTURE = 1;
    private static final int REQUEST_RESOLVE_ERROR = 0;
    private static final int PERMISSIONS_REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        setupAdapter();
        setupRecyclerView();

        presenter = new chatPresenterImpl(this);
        setupToolbar(getIntent());
        presenter.onCreate();
    }

    private void setupAdapter() {
        adapter = new ChatAdapter(this, new ArrayList<ChatMessage>());
    }

    private void setupRecyclerView() {
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageRecyclerView.setAdapter(adapter);
    }

    private void setupToolbar(Intent i) {
        String recipient = i.getStringExtra(EMAIL_KEY);
        presenter.setChatRecipient(recipient);

        boolean online = i.getBooleanExtra(ONLINE_KEY, false);
        String status = online ? "online": "offline";
        int color = online ? Color.GREEN : Color.RED;

        txtUser.setText(recipient);
        txtStatus.setText(status);
        txtStatus.setTextColor(color);

        ImageLoader imageLoader = new GlideImageLoader(getApplicationContext());
        imageLoader.load(imgAvatar, AvatarHelper.getAvatarUrl(recipient));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onResume() {
        presenter.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onMessageReceived(ChatMessage msg) {
        adapter.add(msg);
        messageRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    @OnClick(R.id.btnSendMessage)
    public void SendMessage(){
        presenter.sendMessage(editTxtMessage.getText().toString());
        editTxtMessage.setText("");
    }

    //Nuevo
    @OnClick(R.id.fabMultimedia)
    public void takePicture() { //Aqui se preparan los intents para la maquina de foto o las galerias
        Intent chooserIntent = null;

        List<Intent> intentList = new ArrayList<>();
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        cameraIntent.putExtra("return-data", true);
//
//        File photoFile = getFile();
//        if (photoFile != null) {
//            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
//            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
//                intentList = addIntentsToList(intentList, cameraIntent);
//            }
//        }

        if (pickIntent.resolveActivity(getPackageManager()) != null) {
            intentList = addIntentsToList(intentList, pickIntent);
        }

        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1),
                    getString(R.string.chat_message_picture_source));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
        }

        if (chooserIntent != null) {
            startActivityForResult(chooserIntent, REQUEST_PICTURE);
        }
    }


    private File getFile(){
        File photoFile = null;
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";
//        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        Context c = getApplicationContext();
        File storageDir = c.getFilesDir();

        try {
            photoFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            showSnackbar(R.string.chat_error_dispatch_camera);
        }
        photoPath = imageFileName + ".jpg"; //photoFile.getAbsolutePath();
        return  photoFile;
    }


    private List<Intent> addIntentsToList(List<Intent> list, Intent intent){
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(intent, 0);
        for(ResolveInfo resolveInfo : resInfo){
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetIntent = new Intent(intent);
            targetIntent.setPackage(packageName);
            list.add(targetIntent);
        }

        return list;
    }

    private void showSnackbar(String msg){
        Snackbar.make(messageRecyclerView, msg, Snackbar.LENGTH_SHORT).show();
    }


    private void showSnackbar(int strResource){
        Snackbar.make(messageRecyclerView, strResource, Snackbar.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == REQUEST_RESOLVE_ERROR){
//            resolvingError = false;
//            if(resultCode == RESULT_OK){
//                if(!apiClient.isConnecting() && !apiClient.isConnected()){
//                    apiClient.connect();
//                }
//            }
//        } else
        if(requestCode == REQUEST_PICTURE){
            if(resultCode == RESULT_OK){
                boolean fromCamera = (data == null || data.getData() == null);
                if(fromCamera){
                    addToGallery();
                } else {
                    photoPath = getRealPathFromURI(data.getData());
                }
                presenter.uploadPhoto(photoPath);
            }
        }
    }

    private void addToGallery(){

        //Nuevo
        String storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        photoPath = storageDir + "/" + photoPath;
        // Fin nuevo

        File file = new File(photoPath);

        Uri contentUri = Uri.fromFile(file);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri);
//        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }


    private String getRealPathFromURI(Uri contentURI){
        String result = null;

        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if(cursor == null){
            result = contentURI.getPath();
        } else {
            if(contentURI.toString().contains("mediaKey")){
                cursor.close();

                try {
                    File file = File.createTempFile("tempImg", ".jpg", getCacheDir());
                    InputStream input = getContentResolver().openInputStream(contentURI);
                    OutputStream output = new FileOutputStream(file);

                    try{
                        byte[] buffer = new byte[4 * 1024];
                        int read;

                        while((read = input.read(buffer)) != -1 ){
                            output.write(buffer, 0, read);
                        }
                        output.flush();
                        result = file.getAbsolutePath();
                    }finally {
                        output.close();
                        input.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                cursor.moveToFirst();
                int dataColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(dataColumn);
                cursor.close();
            }
        }
        return result;
    }


    @Override
    public void onUploadInit() {
        showSnackbar(R.string.chat_notice_upload_init);
    }

    @Override
    public void onUploadComplete() {
        showSnackbar(R.string.chat_notice_upload_complete);
    }

    @Override
    public void onUploadError(String error) {
        showSnackbar(error);
    }

//    public void loadImgFromChat(){
//        ImageLoader imageLoader = new GlideImageLoader(getApplicationContext());
//        imageLoader.load(imgAvatar, AvatarHelper.getAvatarUrl(recipient));
//    }

    //FIN DEL NUEVO
}

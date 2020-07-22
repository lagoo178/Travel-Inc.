package com.travel.travelinc.post;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.travel.travelinc.R;

public class CreatePost extends AppCompatActivity {

    private EditText Area, Name, Description;
    private String des; //description
    private ImageView postImage;
    private Button postSubmit;
    private Uri SelectImageUri = null;
    private DatabaseReference postedData;
    StorageReference imageStore = FirebaseStorage.getInstance().getReference("Posted Image");
    private RadioGroup radioGroup;
    String location;
    private static final int Gallery_Request = 1;
    static String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        radioGroup = findViewById(R.id.radio_createPost_rGrp);
        postImage = findViewById(R.id.postImage_createPost_imageView);
        postedData = FirebaseDatabase.getInstance().getReference();
        Description = findViewById(R.id.description_createPost_edittext);
        Area = findViewById(R.id.area_createPost_edittext);
        Name = findViewById(R.id.name_createPost_edittext);
        postSubmit = findViewById(R.id.postSubmit_createPost_button);

        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Request);
            }
        });
        postSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selected = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selected);
                type = radioButton.getText().toString().trim();
                startPosting();
            }
        });
    }

    private void startPosting() {
        des = Description.getText().toString().trim();
        final String area = Area.getText().toString().trim();
        final String name = Name.getText().toString().trim();

        location = postedData.child("Posts").child(type).push().getKey();
        UploadImage(type, area, name);
    }

    private void UploadImage(final String type, final String area, final String name) {
        if (SelectImageUri != null) {
            StorageReference filePath = imageStore.child(location).child(SelectImageUri.getLastPathSegment());

            filePath.putFile(SelectImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getStorage().getDownloadUrl().getResult();

//                    System.out.println(downloadUri.toString() + "This is downloadURI");
                    assert downloadUri != null;
                    PostingSupport postingSupport = new PostingSupport(type, area, name,des, downloadUri.toString(), UserFeedActivity.uid, UserFeedActivity.username);
                    postedData.child("Posts").child(type).child(location).setValue(postingSupport);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Gallery_Request && resultCode == RESULT_OK) {
            SelectImageUri = data.getData();
            postImage.setImageURI(SelectImageUri);

        }
    }
}
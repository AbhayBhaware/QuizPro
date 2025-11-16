package com.example.quizpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    CardView card1, card2, card3, card4;
    FirebaseFirestore db;
    ImageView truefalseIMG, menuBTN;
    FrameLayout profileFrame;
    RelativeLayout overlay;
    ProgressBar imageloading;
    TextView usernameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        card1=findViewById(R.id.card1);
        card2=findViewById(R.id.card2);
        card3=findViewById(R.id.card3);
        card4=findViewById(R.id.card4);
        usernameText=findViewById(R.id.usernameText);
        truefalseIMG=findViewById(R.id.truefalseIMG);
        imageloading=findViewById(R.id.imageLoader);
        overlay=findViewById(R.id.loadingOverLay);
        menuBTN=findViewById(R.id.menuBTN);
        profileFrame=findViewById(R.id.profileFrame);
        db=FirebaseFirestore.getInstance();

        Map<String, ImageView> imageMap = new HashMap<>();
        imageMap.put("GDJ5SzUkshoO1FtYsyJg", findViewById(R.id.rushmodeIMG));
        imageMap.put("KWGoSXyFvS3BCPeaNuFx", findViewById(R.id.truefalseIMG));
        imageMap.put("rE2Ob7reY1NncuMluP3T", findViewById(R.id.badgeIMG));
        imageMap.put("sRGChD6g0J3wy1oKFYGW", findViewById(R.id.coinsIMG));

        imageloading.setVisibility(View.VISIBLE);
        overlay.setVisibility(View.VISIBLE);

        db.collection("Images").get().addOnSuccessListener(queryDocumentSnapshots -> {
            int totalDocs=queryDocumentSnapshots.size();
            final int[] loadCount ={0};
            for (DocumentSnapshot document : queryDocumentSnapshots)
            {
                String base64Image=document.getString("Image");
                ImageView targetView =imageMap.get(document.getId());

                if (base64Image != null && targetView !=null)
                {
                    if (base64Image.startsWith("data:image"))
                    {
                        base64Image=base64Image.substring(base64Image.indexOf(",")+1);
                    }

                    try {
                        byte[] decodeBytes = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodeBytes, 0, decodeBytes.length);

                        targetView.setImageBitmap(bitmap);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(this, "Failed to Decode Images", Toast.LENGTH_SHORT).show();
                    }

                }

                loadCount[0]++;
                if (loadCount[0]==totalDocs)
                {
                    imageloading.setVisibility(View.GONE);
                    overlay.setVisibility(View.GONE);
                }

            }
        }).addOnFailureListener(e -> {
            imageloading.setVisibility(View.GONE);
            overlay.setVisibility(View.GONE);
            Toast.makeText(this, "Failed to Load Images", Toast.LENGTH_SHORT).show();
        });


        String userName=getIntent().getStringExtra("name");
        if (userName!=null)
        {
            usernameText.setText("Hello, "+userName);
        }
        else
        {
            usernameText.setText("Hello, User");
        }

        profileFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfileLayout();
            }
        });

        menuBTN.setOnClickListener(v -> {
            View popupView=getLayoutInflater().inflate(R.layout.custom_menu_layout,null);

            PopupWindow popupWindow=new PopupWindow(
                    popupView,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    true
            );

            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupWindow.setElevation(10);

            int xoffset=(int)(getResources().getDisplayMetrics().density * -75);
            popupWindow.showAsDropDown(menuBTN,xoffset, 0);

            View.OnClickListener menuClickListner = view-> {
                int id=view.getId();

                if (id==R.id.menu_about)
                {
                    Toast.makeText(this, "About Clicked", Toast.LENGTH_SHORT).show();
                } else if (id==R.id.menu_rate)
                {
                    Toast.makeText(this, "Rate us Clicked", Toast.LENGTH_SHORT).show();
                } else if (id==R.id.menu_share)
                {
                    Toast.makeText(this, "Share Clicked", Toast.LENGTH_SHORT).show();
                } else if (id==R.id.menu_logout)
                {
                    getSharedPreferences("QuizProPrefs", MODE_PRIVATE).edit().clear().apply();
                    Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                popupWindow.dismiss();
            };
            popupView.findViewById(R.id.menu_about).setOnClickListener(menuClickListner);
            popupView.findViewById(R.id.menu_rate).setOnClickListener(menuClickListner);
            popupView.findViewById(R.id.menu_share).setOnClickListener(menuClickListner);
            popupView.findViewById(R.id.menu_logout).setOnClickListener(menuClickListner);

        });


        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomeActivity.this, MainActivity.class);
                i.putExtra("category", "Programming");
                startActivity(i);

            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomeActivity.this, MainActivity.class);
                i.putExtra("category", "History");
                startActivity(i);

            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomeActivity.this, MainActivity.class);
                i.putExtra("category", "ScienceandTech");
                startActivity(i);

            }
        });

        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomeActivity.this, MainActivity.class);
                i.putExtra("category", "Sports");
                startActivity(i);

            }
        });



    }

    public void showProfileLayout()
    {
        View popupView = getLayoutInflater().inflate(R.layout.custom_profile_layout,null);

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                (int)(Resources.getSystem().getDisplayMetrics().widthPixels * 0.85),
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
        );


        FirebaseFirestore fb=FirebaseFirestore.getInstance();

        fb.collection("Avatars").get().addOnSuccessListener(queryDocumentSnapshots -> {

            Log.d("AVATAR_FIRESTORE", "Docs count = " + queryDocumentSnapshots.size());

            ArrayList<String> base64List =new ArrayList<>();

            for (DocumentSnapshot doc : queryDocumentSnapshots)
            {
                String base64 =doc.getString("image");
                base64List.add(base64);
            }

            loadAvatarImages(base64List, popupView);


        });


        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setElevation(10);

        int xoffset=(int) (getResources().getDisplayMetrics().density* 6);
        popupWindow.showAsDropDown(profileFrame, xoffset, 0);

        View.OnClickListener profileClickListner = view -> {

            popupWindow.dismiss();
        };
    }

    public void decodeAvatar(String base64Image, ImageView targetView) {

        Log.d("AVATAR_DEBUG", "decodeAvatar() called");

        // if no Base64 image stored -> DO NOTHING
        if (base64Image == null || base64Image.trim().isEmpty()) {
            Log.e("AVATAR_DEBUG", "Base64 is NULL or EMPTY");
            return; // keep the original XML image
        }

        Log.d("AVATAR_DEBUG", "Original Base64 length: " + base64Image.length());

        try {
            // Check and remove prefix
            if (base64Image.startsWith("data:image")) {
                Log.d("AVATAR_DEBUG", "Data URL detected. Removing prefix...");
                base64Image = base64Image.substring(base64Image.indexOf(",") + 1);
            }

            Log.d("AVATAR_DEBUG", "Decoding Base64...");

            byte[] decodeBytes = Base64.decode(base64Image, Base64.DEFAULT);

            Log.d("AVATAR_DEBUG", "Decoded bytes length: " + decodeBytes.length);

            Bitmap bitmap = BitmapFactory.decodeByteArray(decodeBytes, 0, decodeBytes.length);

            if (bitmap != null) {
                Log.d("AVATAR_DEBUG", "Bitmap decoded successfully! Width="
                        + bitmap.getWidth() + " Height=" + bitmap.getHeight());
                targetView.setImageBitmap(bitmap);
            } else {
                Log.e("AVATAR_DEBUG", "Bitmap is NULL after decoding!!");
            }

        } catch (Exception e) {
            Log.e("AVATAR_DEBUG", "Exception: " + e.getMessage());
        }
    }




    public void loadAvatarImages(ArrayList<String> base64List, View popupView)
    {
        ImageView[] Avatars = {
                popupView.findViewById(R.id.avtar1),
                popupView.findViewById(R.id.avtar2),
                popupView.findViewById(R.id.avtar3),
                popupView.findViewById(R.id.avtar4),
                popupView.findViewById(R.id.avtar5),
                popupView.findViewById(R.id.avtar6),
                popupView.findViewById(R.id.avtar7),
                popupView.findViewById(R.id.avtar8)
        };

        for (int i = 0; i < Avatars.length; i++) {
            if (i < base64List.size()) {
                decodeAvatar(base64List.get(i), Avatars[i]);
            }
            // DO NOT set default here
        }
    }

}
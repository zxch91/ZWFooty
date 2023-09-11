package com.example.cw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class newsArticleActivity extends AppCompatActivity {
    private ShareActionProvider mShareActionProvider;
    private TextView newsTitle;
    private TextView newsContent;
    private ImageView newsImage;
    private DatabaseReference mDatabase;
    private RecyclerView commentsRecyclerView;
    private CommentsAdapter commentsAdapter;
    private List<Comment> commentList;
    private EditText commentInput;
    private Button submitComment;
    private newsContent currentNewsArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_article);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        newsTitle = findViewById(R.id.news_title);
        newsContent = findViewById(R.id.news_content);
        newsImage = findViewById(R.id.news_image);

        // Retrieve the data from the intent
        Intent intent = getIntent();
        currentNewsArticle = (newsContent) intent.getSerializableExtra("KEY_NEWS_ARTICLE");

        // Populate the views with the data from the news article
        newsTitle.setText(currentNewsArticle.getName());
        newsContent.setText(currentNewsArticle.getArticle());
        newsImage.setImageResource(currentNewsArticle.getImage());

        mDatabase = FirebaseDatabase.getInstance().getReference("comments");
        commentsRecyclerView = findViewById(R.id.comments_recyclerview);
        commentList = new ArrayList<>();
        commentsAdapter = new CommentsAdapter(commentList);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(commentsAdapter);

        commentInput = findViewById(R.id.comment_input);
        submitComment = findViewById(R.id.submit_comment);

        submitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitComment();
            }
        });

        fetchComments();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_article, menu);

        MenuItem shareItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        setShareIntent(createShareIntent());

        return true;
    }

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        // Add the content you want to share, e.g., the news article's title and URL
        String shareText = "Check out this news article: ";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        return shareIntent;
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private void fetchComments() {
        DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference("comments");

        // Get the articleId of the current article
        int articleId = currentNewsArticle.getId();
        // Query comments based on the articleId
        commentsRef.orderByChild("articleId").equalTo(articleId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
                    Comment comment = commentSnapshot.getValue(Comment.class);
                    commentList.add(comment);
                }
                commentsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(newsArticleActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitComment() {
        String content = commentInput.getText().toString().trim();
        if (!content.isEmpty()) {
            String author = "Anonymous"; // Replace this with the user's name or ID if you want users of the app to know who comments
            int articleId = currentNewsArticle.getId(); // Get the articleId from the current news article
            Comment comment = new Comment(author, content, articleId);
            mDatabase.push().setValue(comment);
            commentInput.setText("");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

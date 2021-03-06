import org.sql2o.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.sql.Timestamp;

public class Comment {
  private String content;
  private int id;
  private int postId;
  private int userId;

  public Comment(String content, int postId, int userId) {
    this.content = content;
    this.postId = postId;
    this.userId = userId;
  }

  public String getContent() {
    return this.content;
  }

  public int getId() {
    return this.id;
  }

  public int getPostId() {
    return this.postId;
  }

  public int getUserId() {
    return this.userId;
  }

  public String getUserName() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT name FROM users WHERE id = :userId;";
      return con.createQuery(sql)
        .addParameter("userId", this.userId)
        .executeAndFetchFirst(String.class);
    }
  }

  @Override
  public boolean equals(Object otherComment) {
    if (!(otherComment instanceof Comment)) {
      return false;
    } else {
      Comment newComment = (Comment) otherComment;
      return this.getContent().equals(newComment.getContent()) &&
             this.getPostId() == newComment.getPostId() &&
             this.getUserId() == newComment.getUserId() &&
             this.getId() == newComment.getId();
    }
  }

  public void save() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO comments (content, postId, userId) VALUES (:content, :postId, :userId);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("content", this.content)
        .addParameter("postId", this.postId)
        .addParameter("userId", this.userId)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Comment> all() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM comments;";
      return con.createQuery(sql)
        .executeAndFetch(Comment.class);
    }
  }

  public static Comment find(int id) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM comments WHERE id = :id;";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Comment.class);
    }
  }

  public void update(String content) {
    this.content = content;
    try (Connection con = DB.sql2o.open()) {
      String sql = "UPDATE comments SET content = :content WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("content", this.content)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public void delete() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM comments WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

}

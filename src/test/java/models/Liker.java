package models;

public class Liker {
    private int postId;
    private String likeStatus;

    public Liker(int postId, String likeStatus) {
        this.setPostId(postId);
        this.setLikeStatus(likeStatus);
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(String likeStatus) {
        this.likeStatus = likeStatus;
    }
}

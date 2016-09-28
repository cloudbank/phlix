
package com.anubis.flickr.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "photo_id",
    "commentList"
})
public class Comments_ {

    @JsonProperty("photo_id")
    private String photoId;
    @JsonProperty("comment")
    private List<Comment> commentList = new ArrayList<Comment>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The photoId
     */
    @JsonProperty("photo_id")
    public String getPhotoId() {
        return photoId;
    }

    /**
     * 
     * @param photoId
     *     The photo_id
     */
    @JsonProperty("photo_id")
    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    /**
     * 
     * @return
     *     The commentList
     */
    @JsonProperty("comment")
    public List<Comment> getCommentList() {
        return commentList;
    }

    /**
     * 
     * @param commentList
     *     The commentList
     */
    @JsonProperty("comment")
    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

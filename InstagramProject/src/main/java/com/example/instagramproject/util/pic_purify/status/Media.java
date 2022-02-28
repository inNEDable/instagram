package com.example.instagramproject.util.pic_purify.status;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Media {

    @JsonProperty("url_image")
    String urlImage;

    @JsonProperty("file_image")
    String fileImage;

    @JsonProperty("media_id")
    String mediaId;

    @JsonProperty("reference_id")
    String referenceId;

    @JsonProperty("origin_id")
    String originId;
}

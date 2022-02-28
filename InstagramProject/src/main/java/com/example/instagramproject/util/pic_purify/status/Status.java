package com.example.instagramproject.util.pic_purify.status;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Status {

    private Error error;

    @JsonProperty("final_decision")
    private String finalDecision;

    @JsonProperty("confidence_score_decision")
    private String confidenceScoreDecision;

    @JsonProperty("porn_moderation")
    PornModeration pornModeration;

    @JsonProperty("suggestive_nudity_moderation")
    NudityModeration nudityModeration;

    Media media;

    @JsonProperty("total_compute_time")
    Double totalComputeTime;

    public PornModeration getPornModeration() {
        return pornModeration;
    }

    ////////////////////// CLASSES ////////////////////////


    @Getter
    @Setter
    @ToString
    public static class Moderation {
        @JsonProperty("compute_time")
        protected Double computeTime;

        @JsonProperty("confidence_score")
        protected Double confidenceScore;
    }

    @Data
    @ToString(callSuper = true)
    public static class PornModeration extends Moderation {
        @JsonProperty("porn_content")
        private Boolean pornContent;

    }

    @Data
    @ToString(callSuper = true)
    public static class NudityModeration extends Moderation{

        @JsonProperty("suggestive_nudity_content")
        private Boolean nudityContent;
    }

}

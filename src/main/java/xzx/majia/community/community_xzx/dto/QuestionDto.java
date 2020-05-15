package xzx.majia.community.community_xzx.dto;

import lombok.Data;
import xzx.majia.community.community_xzx.model.User;

@Data
public class QuestionDto {

    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;

}

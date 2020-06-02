package xzx.majia.community.community_xzx.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//用来封装 页面信息
@Data
public class PaginationDto {
    private List<QuestionDto> questions;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer Page;
    private List<Integer> pages = new ArrayList<>();

    private Integer totalPage;

    public void setPagination(Integer totalPage,Integer page) {
        this.totalPage = totalPage;
        this.Page = page;

        pages.add(page);
        for (int i = 1;i <= 3;i++){
            if (page - i > 0){
                pages.add(0,page - i);
            }
            if (page + i <= totalPage){
                pages.add(page+i);
            }
        }


        /*是否展示上一页*/
        if (page == 1){
            showPrevious = false;
        }else {
            showPrevious =true;
        }
        /*是否展示下一页*/
        if ( page == totalPage){
            showNext = false;
        }else {
            showNext = true;
        }

        /*是否展示第一页*/
        if (pages.contains(1)){
            showFirstPage =false;
        }else {
            showFirstPage = true;
        }
        /*是否展示最后一页*/
        if (pages.contains(totalPage)){
            showEndPage = false;
        }else {
            showEndPage = true;
        }
    }

}



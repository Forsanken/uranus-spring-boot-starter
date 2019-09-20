/**
 * FileName: PageInfo
 * Author:   chy
 * Date:     2019/8/27 15:12
 * Description: 分页数据
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.uranus.framework.web.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈分页数据〉
 *
 * @author chy 2019/8/27
 * @since 1.0.0
 */
@Data
@ToString
@ApiModel(description = "基础分页返回对象")
public class PageInfo<T> implements Serializable {

    @ApiModelProperty(value = "当前页数", dataType = "Integer", name = "pageNum", example = "2")
    private int pageNum;

    @ApiModelProperty(value = "每页行数", dataType = "Integer", name = "pageSize", example = "20")
    private int pageSize;

    @ApiModelProperty(value = "当前页长度", dataType = "Integer", name = "size", example = "15")
    private int size;

    @ApiModelProperty(value = "开始行数", dataType = "Integer", name = "startRow", example = "21")
    private int startRow;

    @ApiModelProperty(value = "结束行数", dataType = "Integer", name = "endRow", example = "40")
    private int endRow;

    @ApiModelProperty(value = "总页数", dataType = "Integer", name = "pages", example = "10")
    private int pages;

    @ApiModelProperty(value = "前一页", dataType = "Integer", name = "prePage", example = "1")
    private int prePage;

    @ApiModelProperty(value = "下一页", dataType = "Integer", name = "nextPage", example = "3")
    private int nextPage;

    @ApiModelProperty(value = "是否第一页", dataType = "Boolean", name = "isFirstPage", example = "false")
    private boolean isFirstPage;

    @ApiModelProperty(value = "是否最后一页", dataType = "Boolean", name = "isLastPage", example = "false")
    private boolean isLastPage;

    @ApiModelProperty(value = "是否有前一页", dataType = "Boolean", name = "hasPreviousPage", example = "true")
    private boolean hasPreviousPage;

    @ApiModelProperty(value = "是否有下一页", dataType = "Boolean", name = "hasNextPage", example = "true")
    private boolean hasNextPage;

    @ApiModelProperty(value = "页码栏显示页码数", dataType = "Integer", name = "navigatePages", example = "8")
    private int navigatePages;

    @ApiModelProperty(value = "页码栏数据", dataType = "Integer[]", name = "code", example = "\"[1,2,3,4,5,6,7,8]\"")
    private int[] navigatepageNums;

    @ApiModelProperty(value = "页码栏第一页", dataType = "Integer", name = "navigateFirstPage", example = "1")
    private int navigateFirstPage;

    @ApiModelProperty(value = "页码栏最后一页", dataType = "Integer", name = "navigateLastPage", example = "8")
    private int navigateLastPage;

    @ApiModelProperty(value = "总数", dataType = "Long", name = "total", example = "400")
    private long total;

    @ApiModelProperty(value = "显示数据", dataType = "List", name = "list", example = "\"{a,b,c}\"")
    private List<T> list;

    public PageInfo() {
        this.isFirstPage = false;
        this.isLastPage = false;
        this.hasPreviousPage = false;
        this.hasNextPage = false;
    }

    public PageInfo(List<T> list) {
        this(list, list.size());
    }

    public PageInfo(List<T> list, long total) {
        this(list, 8, 1, list.size(), total);
    }

    public PageInfo(List<T> list, Page page, long total) {
        this(list, 8, page, total);
    }

    public PageInfo(List<T> list, int navigatePages, Page page, long total) {
        this(list, navigatePages, page.getPageNum(), page.getPageSize(), total);
    }

    public PageInfo(List<T> list, int pageNum, int pageSize, long total) {
        this(list, 8, pageNum, pageSize, total);
    }

    public PageInfo(List<T> list, int navigatePages, int pageNum, int pageSize, long total) {
        this.list = list;
        this.isFirstPage = false;
        this.isLastPage = false;
        this.hasPreviousPage = false;
        this.hasNextPage = false;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.size = list.size();
        this.navigatePages = navigatePages;
        this.total = total;
        this.calcPages();
        this.calcNavigatepageNums();
        this.calcNavigatePage();
        this.judgePageBoudary();
    }

    private void calcPages() {
        if (this.total == -1L) {
            this.pages = 1;
        } else {
            if (this.pageSize > 0) {
                this.pages = (int)(this.total / (long)this.pageSize + (long)(this.total % (long)this.pageSize == 0L ? 0 : 1));
            } else {
                this.pages = 0;
            }

            if (this.pageNum > this.pages) {
                if (this.pages != 0) {
                    this.pageNum = this.pages;
                }
                this.startRow = this.pageNum > 0 ? (this.pageNum - 1) * this.pageSize : 0;
                this.endRow = this.startRow + this.pageSize * (this.pageNum > 0 ? 1 : 0);
            }
        }
    }

    private void calcNavigatepageNums() {
        if (this.pages <= this.navigatePages) {
            this.navigatepageNums = new int[this.pages];
            for(int i = 0; i < this.pages; ++i) {
                this.navigatepageNums[i] = i + 1;
            }
        } else {
            this.navigatepageNums = new int[this.navigatePages];
            int beginNum = this.pageNum - this.navigatePages / 2;
            int endNum = this.pageNum + this.navigatePages / 2;

            if (beginNum >= 1 && endNum > this.pages) {
                endNum = this.pages;
                for (int i = this.navigatePages - 1; i >= 0; --i) {
                    this.navigatepageNums[i] = endNum--;
                }
            } else {
                for(int i = 0; i < this.navigatePages; ++i) {
                    this.navigatepageNums[i] = i++;
                }
            }
        }

    }

    private void calcNavigatePage() {
        if (this.navigatepageNums != null && this.navigatepageNums.length > 0) {
            this.navigateFirstPage = this.navigatepageNums[0];
            this.navigateLastPage = this.navigatepageNums[this.navigatepageNums.length - 1];
            if (this.pageNum > 1) {
                this.prePage = this.pageNum - 1;
            }

            if (this.pageNum < this.pages) {
                this.nextPage = this.pageNum + 1;
            }
        }

    }

    private void judgePageBoudary() {
        this.isFirstPage = this.pageNum == 1;
        this.isLastPage = this.pageNum == this.pages || this.pages == 0;
        this.hasPreviousPage = this.pageNum > 1;
        this.hasNextPage = this.pageNum < this.pages;
    }
}
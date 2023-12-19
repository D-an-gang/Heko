package project.heko.models;

public class Pagination {
    public long TotalItem;
    public long TotalPage;
    public long CurrentPage;

    public long PageSize;


    public Pagination(long totalItem, long pageSize) {
        TotalItem = totalItem;
        PageSize = pageSize;
        TotalPage = totalItem % pageSize == 0 ? (totalItem / pageSize) : (totalItem / pageSize) + 1;
        CurrentPage = 1;
    }
}

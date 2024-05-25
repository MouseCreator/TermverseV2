package mouse.project.termverseweb.dto.data;

import lombok.Data;

@Data
public class StudySetSearchParams {
    Integer pageNumber;
    Integer pageSize;
    String searchParam;
    String category;
    String sortBy;
    Long userId;

    private StudySetSearchParams() {
        pageNumber = null;
        pageSize = null;
        searchParam = null;
        category = null;
        sortBy = null;
        userId = null;
    }

    public StudySetSearchParams(Integer pageNumber,
                                Integer pageSize,
                                String searchParam,
                                String category,
                                String sortBy,
                                Long userId) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.searchParam = searchParam;
        this.category = category;
        this.sortBy = sortBy;
        this.userId = userId;
    }
    public static StudySetSearchParamsBuilder build() {
        return new StudySetSearchParamsBuilder();
    }
    public static class StudySetSearchParamsBuilder {
        private final StudySetSearchParams instance;
        public StudySetSearchParamsBuilder() {
            instance = new StudySetSearchParams();
        }
        public StudySetSearchParamsBuilder pageNumber(Integer pageNumber) {
            instance.setPageNumber(pageNumber);
            return this;
        }
        public StudySetSearchParamsBuilder pageSize(Integer arg) {
            instance.setPageSize(arg);
            return this;
        }
        public StudySetSearchParamsBuilder searchParam(String arg) {
            instance.setSearchParam(arg);
            return this;
        }
        public StudySetSearchParamsBuilder category(String arg) {
            instance.setCategory(arg);
            return this;
        }
        public StudySetSearchParamsBuilder sort(String arg) {
            instance.setSortBy(arg);
            return this;
        }
        public StudySetSearchParamsBuilder user(Long arg) {
            instance.setUserId(arg);
            return this;
        }
        public StudySetSearchParams get() {
            return instance;
        }
    }
}

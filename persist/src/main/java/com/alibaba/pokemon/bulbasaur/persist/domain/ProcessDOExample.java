package com.alibaba.pokemon.bulbasaur.persist.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProcessDOExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private Integer limit;

    private Integer offset;

    public ProcessDOExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getOffset() {
        return offset;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andBizIdIsNull() {
            addCriterion("biz_id is null");
            return (Criteria) this;
        }

        public Criteria andBizIdIsNotNull() {
            addCriterion("biz_id is not null");
            return (Criteria) this;
        }

        public Criteria andBizIdEqualTo(String value) {
            addCriterion("biz_id =", value, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdNotEqualTo(String value) {
            addCriterion("biz_id <>", value, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdGreaterThan(String value) {
            addCriterion("biz_id >", value, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdGreaterThanOrEqualTo(String value) {
            addCriterion("biz_id >=", value, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdLessThan(String value) {
            addCriterion("biz_id <", value, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdLessThanOrEqualTo(String value) {
            addCriterion("biz_id <=", value, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdLike(String value) {
            addCriterion("biz_id like", value, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdNotLike(String value) {
            addCriterion("biz_id not like", value, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdIn(List<String> values) {
            addCriterion("biz_id in", values, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdNotIn(List<String> values) {
            addCriterion("biz_id not in", values, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdBetween(String value1, String value2) {
            addCriterion("biz_id between", value1, value2, "bizId");
            return (Criteria) this;
        }

        public Criteria andBizIdNotBetween(String value1, String value2) {
            addCriterion("biz_id not between", value1, value2, "bizId");
            return (Criteria) this;
        }

        public Criteria andDefinitionNameIsNull() {
            addCriterion("definition_name is null");
            return (Criteria) this;
        }

        public Criteria andDefinitionNameIsNotNull() {
            addCriterion("definition_name is not null");
            return (Criteria) this;
        }

        public Criteria andDefinitionNameEqualTo(String value) {
            addCriterion("definition_name =", value, "definitionName");
            return (Criteria) this;
        }

        public Criteria andDefinitionNameNotEqualTo(String value) {
            addCriterion("definition_name <>", value, "definitionName");
            return (Criteria) this;
        }

        public Criteria andDefinitionNameGreaterThan(String value) {
            addCriterion("definition_name >", value, "definitionName");
            return (Criteria) this;
        }

        public Criteria andDefinitionNameGreaterThanOrEqualTo(String value) {
            addCriterion("definition_name >=", value, "definitionName");
            return (Criteria) this;
        }

        public Criteria andDefinitionNameLessThan(String value) {
            addCriterion("definition_name <", value, "definitionName");
            return (Criteria) this;
        }

        public Criteria andDefinitionNameLessThanOrEqualTo(String value) {
            addCriterion("definition_name <=", value, "definitionName");
            return (Criteria) this;
        }

        public Criteria andDefinitionNameLike(String value) {
            addCriterion("definition_name like", value, "definitionName");
            return (Criteria) this;
        }

        public Criteria andDefinitionNameNotLike(String value) {
            addCriterion("definition_name not like", value, "definitionName");
            return (Criteria) this;
        }

        public Criteria andDefinitionNameIn(List<String> values) {
            addCriterion("definition_name in", values, "definitionName");
            return (Criteria) this;
        }

        public Criteria andDefinitionNameNotIn(List<String> values) {
            addCriterion("definition_name not in", values, "definitionName");
            return (Criteria) this;
        }

        public Criteria andDefinitionNameBetween(String value1, String value2) {
            addCriterion("definition_name between", value1, value2, "definitionName");
            return (Criteria) this;
        }

        public Criteria andDefinitionNameNotBetween(String value1, String value2) {
            addCriterion("definition_name not between", value1, value2, "definitionName");
            return (Criteria) this;
        }

        public Criteria andDefinitionVersionIsNull() {
            addCriterion("definition_version is null");
            return (Criteria) this;
        }

        public Criteria andDefinitionVersionIsNotNull() {
            addCriterion("definition_version is not null");
            return (Criteria) this;
        }

        public Criteria andDefinitionVersionEqualTo(Integer value) {
            addCriterion("definition_version =", value, "definitionVersion");
            return (Criteria) this;
        }

        public Criteria andDefinitionVersionNotEqualTo(Integer value) {
            addCriterion("definition_version <>", value, "definitionVersion");
            return (Criteria) this;
        }

        public Criteria andDefinitionVersionGreaterThan(Integer value) {
            addCriterion("definition_version >", value, "definitionVersion");
            return (Criteria) this;
        }

        public Criteria andDefinitionVersionGreaterThanOrEqualTo(Integer value) {
            addCriterion("definition_version >=", value, "definitionVersion");
            return (Criteria) this;
        }

        public Criteria andDefinitionVersionLessThan(Integer value) {
            addCriterion("definition_version <", value, "definitionVersion");
            return (Criteria) this;
        }

        public Criteria andDefinitionVersionLessThanOrEqualTo(Integer value) {
            addCriterion("definition_version <=", value, "definitionVersion");
            return (Criteria) this;
        }

        public Criteria andDefinitionVersionIn(List<Integer> values) {
            addCriterion("definition_version in", values, "definitionVersion");
            return (Criteria) this;
        }

        public Criteria andDefinitionVersionNotIn(List<Integer> values) {
            addCriterion("definition_version not in", values, "definitionVersion");
            return (Criteria) this;
        }

        public Criteria andDefinitionVersionBetween(Integer value1, Integer value2) {
            addCriterion("definition_version between", value1, value2, "definitionVersion");
            return (Criteria) this;
        }

        public Criteria andDefinitionVersionNotBetween(Integer value1, Integer value2) {
            addCriterion("definition_version not between", value1, value2, "definitionVersion");
            return (Criteria) this;
        }

        public Criteria andOwnSignIsNull() {
            addCriterion("own_sign is null");
            return (Criteria) this;
        }

        public Criteria andOwnSignIsNotNull() {
            addCriterion("own_sign is not null");
            return (Criteria) this;
        }

        public Criteria andOwnSignEqualTo(String value) {
            addCriterion("own_sign =", value, "ownSign");
            return (Criteria) this;
        }

        public Criteria andOwnSignNotEqualTo(String value) {
            addCriterion("own_sign <>", value, "ownSign");
            return (Criteria) this;
        }

        public Criteria andOwnSignGreaterThan(String value) {
            addCriterion("own_sign >", value, "ownSign");
            return (Criteria) this;
        }

        public Criteria andOwnSignGreaterThanOrEqualTo(String value) {
            addCriterion("own_sign >=", value, "ownSign");
            return (Criteria) this;
        }

        public Criteria andOwnSignLessThan(String value) {
            addCriterion("own_sign <", value, "ownSign");
            return (Criteria) this;
        }

        public Criteria andOwnSignLessThanOrEqualTo(String value) {
            addCriterion("own_sign <=", value, "ownSign");
            return (Criteria) this;
        }

        public Criteria andOwnSignLike(String value) {
            addCriterion("own_sign like", value, "ownSign");
            return (Criteria) this;
        }

        public Criteria andOwnSignNotLike(String value) {
            addCriterion("own_sign not like", value, "ownSign");
            return (Criteria) this;
        }

        public Criteria andOwnSignIn(List<String> values) {
            addCriterion("own_sign in", values, "ownSign");
            return (Criteria) this;
        }

        public Criteria andOwnSignNotIn(List<String> values) {
            addCriterion("own_sign not in", values, "ownSign");
            return (Criteria) this;
        }

        public Criteria andOwnSignBetween(String value1, String value2) {
            addCriterion("own_sign between", value1, value2, "ownSign");
            return (Criteria) this;
        }

        public Criteria andOwnSignNotBetween(String value1, String value2) {
            addCriterion("own_sign not between", value1, value2, "ownSign");
            return (Criteria) this;
        }

        public Criteria andCurrentStateNameIsNull() {
            addCriterion("current_state_name is null");
            return (Criteria) this;
        }

        public Criteria andCurrentStateNameIsNotNull() {
            addCriterion("current_state_name is not null");
            return (Criteria) this;
        }

        public Criteria andCurrentStateNameEqualTo(String value) {
            addCriterion("current_state_name =", value, "currentStateName");
            return (Criteria) this;
        }

        public Criteria andCurrentStateNameNotEqualTo(String value) {
            addCriterion("current_state_name <>", value, "currentStateName");
            return (Criteria) this;
        }

        public Criteria andCurrentStateNameGreaterThan(String value) {
            addCriterion("current_state_name >", value, "currentStateName");
            return (Criteria) this;
        }

        public Criteria andCurrentStateNameGreaterThanOrEqualTo(String value) {
            addCriterion("current_state_name >=", value, "currentStateName");
            return (Criteria) this;
        }

        public Criteria andCurrentStateNameLessThan(String value) {
            addCriterion("current_state_name <", value, "currentStateName");
            return (Criteria) this;
        }

        public Criteria andCurrentStateNameLessThanOrEqualTo(String value) {
            addCriterion("current_state_name <=", value, "currentStateName");
            return (Criteria) this;
        }

        public Criteria andCurrentStateNameLike(String value) {
            addCriterion("current_state_name like", value, "currentStateName");
            return (Criteria) this;
        }

        public Criteria andCurrentStateNameNotLike(String value) {
            addCriterion("current_state_name not like", value, "currentStateName");
            return (Criteria) this;
        }

        public Criteria andCurrentStateNameIn(List<String> values) {
            addCriterion("current_state_name in", values, "currentStateName");
            return (Criteria) this;
        }

        public Criteria andCurrentStateNameNotIn(List<String> values) {
            addCriterion("current_state_name not in", values, "currentStateName");
            return (Criteria) this;
        }

        public Criteria andCurrentStateNameBetween(String value1, String value2) {
            addCriterion("current_state_name between", value1, value2, "currentStateName");
            return (Criteria) this;
        }

        public Criteria andCurrentStateNameNotBetween(String value1, String value2) {
            addCriterion("current_state_name not between", value1, value2, "currentStateName");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(String value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(String value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(String value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(String value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(String value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(String value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLike(String value) {
            addCriterion("status like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotLike(String value) {
            addCriterion("status not like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<String> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<String> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(String value1, String value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(String value1, String value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andGmtCreateIsNull() {
            addCriterion("gmt_create is null");
            return (Criteria) this;
        }

        public Criteria andGmtCreateIsNotNull() {
            addCriterion("gmt_create is not null");
            return (Criteria) this;
        }

        public Criteria andGmtCreateEqualTo(Date value) {
            addCriterion("gmt_create =", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateNotEqualTo(Date value) {
            addCriterion("gmt_create <>", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateGreaterThan(Date value) {
            addCriterion("gmt_create >", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateGreaterThanOrEqualTo(Date value) {
            addCriterion("gmt_create >=", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateLessThan(Date value) {
            addCriterion("gmt_create <", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateLessThanOrEqualTo(Date value) {
            addCriterion("gmt_create <=", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateIn(List<Date> values) {
            addCriterion("gmt_create in", values, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateNotIn(List<Date> values) {
            addCriterion("gmt_create not in", values, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateBetween(Date value1, Date value2) {
            addCriterion("gmt_create between", value1, value2, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateNotBetween(Date value1, Date value2) {
            addCriterion("gmt_create not between", value1, value2, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedIsNull() {
            addCriterion("gmt_modified is null");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedIsNotNull() {
            addCriterion("gmt_modified is not null");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedEqualTo(Date value) {
            addCriterion("gmt_modified =", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedNotEqualTo(Date value) {
            addCriterion("gmt_modified <>", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedGreaterThan(Date value) {
            addCriterion("gmt_modified >", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedGreaterThanOrEqualTo(Date value) {
            addCriterion("gmt_modified >=", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedLessThan(Date value) {
            addCriterion("gmt_modified <", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedLessThanOrEqualTo(Date value) {
            addCriterion("gmt_modified <=", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedIn(List<Date> values) {
            addCriterion("gmt_modified in", values, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedNotIn(List<Date> values) {
            addCriterion("gmt_modified not in", values, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedBetween(Date value1, Date value2) {
            addCriterion("gmt_modified between", value1, value2, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedNotBetween(Date value1, Date value2) {
            addCriterion("gmt_modified not between", value1, value2, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andAliasIsNull() {
            addCriterion("alias is null");
            return (Criteria) this;
        }

        public Criteria andAliasIsNotNull() {
            addCriterion("alias is not null");
            return (Criteria) this;
        }

        public Criteria andAliasEqualTo(String value) {
            addCriterion("alias =", value, "alias");
            return (Criteria) this;
        }

        public Criteria andAliasNotEqualTo(String value) {
            addCriterion("alias <>", value, "alias");
            return (Criteria) this;
        }

        public Criteria andAliasGreaterThan(String value) {
            addCriterion("alias >", value, "alias");
            return (Criteria) this;
        }

        public Criteria andAliasGreaterThanOrEqualTo(String value) {
            addCriterion("alias >=", value, "alias");
            return (Criteria) this;
        }

        public Criteria andAliasLessThan(String value) {
            addCriterion("alias <", value, "alias");
            return (Criteria) this;
        }

        public Criteria andAliasLessThanOrEqualTo(String value) {
            addCriterion("alias <=", value, "alias");
            return (Criteria) this;
        }

        public Criteria andAliasLike(String value) {
            addCriterion("alias like", value, "alias");
            return (Criteria) this;
        }

        public Criteria andAliasNotLike(String value) {
            addCriterion("alias not like", value, "alias");
            return (Criteria) this;
        }

        public Criteria andAliasIn(List<String> values) {
            addCriterion("alias in", values, "alias");
            return (Criteria) this;
        }

        public Criteria andAliasNotIn(List<String> values) {
            addCriterion("alias not in", values, "alias");
            return (Criteria) this;
        }

        public Criteria andAliasBetween(String value1, String value2) {
            addCriterion("alias between", value1, value2, "alias");
            return (Criteria) this;
        }

        public Criteria andAliasNotBetween(String value1, String value2) {
            addCriterion("alias not between", value1, value2, "alias");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}

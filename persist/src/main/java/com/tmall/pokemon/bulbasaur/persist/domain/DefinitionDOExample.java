package com.tmall.pokemon.bulbasaur.persist.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DefinitionDOExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private Integer limit;

    private Integer offset;

    public DefinitionDOExample() {
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

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Boolean value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Boolean value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Boolean value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Boolean value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Boolean value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Boolean value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Boolean> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Boolean> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Boolean value1, Boolean value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Boolean value1, Boolean value2) {
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

        public Criteria andDefinitionAliasIsNull() {
            addCriterion("definition_alias is null");
            return (Criteria) this;
        }

        public Criteria andDefinitionAliasIsNotNull() {
            addCriterion("definition_alias is not null");
            return (Criteria) this;
        }

        public Criteria andDefinitionAliasEqualTo(String value) {
            addCriterion("definition_alias =", value, "definitionAlias");
            return (Criteria) this;
        }

        public Criteria andDefinitionAliasNotEqualTo(String value) {
            addCriterion("definition_alias <>", value, "definitionAlias");
            return (Criteria) this;
        }

        public Criteria andDefinitionAliasGreaterThan(String value) {
            addCriterion("definition_alias >", value, "definitionAlias");
            return (Criteria) this;
        }

        public Criteria andDefinitionAliasGreaterThanOrEqualTo(String value) {
            addCriterion("definition_alias >=", value, "definitionAlias");
            return (Criteria) this;
        }

        public Criteria andDefinitionAliasLessThan(String value) {
            addCriterion("definition_alias <", value, "definitionAlias");
            return (Criteria) this;
        }

        public Criteria andDefinitionAliasLessThanOrEqualTo(String value) {
            addCriterion("definition_alias <=", value, "definitionAlias");
            return (Criteria) this;
        }

        public Criteria andDefinitionAliasLike(String value) {
            addCriterion("definition_alias like", value, "definitionAlias");
            return (Criteria) this;
        }

        public Criteria andDefinitionAliasNotLike(String value) {
            addCriterion("definition_alias not like", value, "definitionAlias");
            return (Criteria) this;
        }

        public Criteria andDefinitionAliasIn(List<String> values) {
            addCriterion("definition_alias in", values, "definitionAlias");
            return (Criteria) this;
        }

        public Criteria andDefinitionAliasNotIn(List<String> values) {
            addCriterion("definition_alias not in", values, "definitionAlias");
            return (Criteria) this;
        }

        public Criteria andDefinitionAliasBetween(String value1, String value2) {
            addCriterion("definition_alias between", value1, value2, "definitionAlias");
            return (Criteria) this;
        }

        public Criteria andDefinitionAliasNotBetween(String value1, String value2) {
            addCriterion("definition_alias not between", value1, value2, "definitionAlias");
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
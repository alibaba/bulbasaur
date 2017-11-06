package com.tmall.pokemon.bulbasaur.persist.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JobDOExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private Integer limit;

    private Integer offset;

    public JobDOExample() {
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

        public Criteria andStateNameIsNull() {
            addCriterion("state_name is null");
            return (Criteria) this;
        }

        public Criteria andStateNameIsNotNull() {
            addCriterion("state_name is not null");
            return (Criteria) this;
        }

        public Criteria andStateNameEqualTo(String value) {
            addCriterion("state_name =", value, "stateName");
            return (Criteria) this;
        }

        public Criteria andStateNameNotEqualTo(String value) {
            addCriterion("state_name <>", value, "stateName");
            return (Criteria) this;
        }

        public Criteria andStateNameGreaterThan(String value) {
            addCriterion("state_name >", value, "stateName");
            return (Criteria) this;
        }

        public Criteria andStateNameGreaterThanOrEqualTo(String value) {
            addCriterion("state_name >=", value, "stateName");
            return (Criteria) this;
        }

        public Criteria andStateNameLessThan(String value) {
            addCriterion("state_name <", value, "stateName");
            return (Criteria) this;
        }

        public Criteria andStateNameLessThanOrEqualTo(String value) {
            addCriterion("state_name <=", value, "stateName");
            return (Criteria) this;
        }

        public Criteria andStateNameLike(String value) {
            addCriterion("state_name like", value, "stateName");
            return (Criteria) this;
        }

        public Criteria andStateNameNotLike(String value) {
            addCriterion("state_name not like", value, "stateName");
            return (Criteria) this;
        }

        public Criteria andStateNameIn(List<String> values) {
            addCriterion("state_name in", values, "stateName");
            return (Criteria) this;
        }

        public Criteria andStateNameNotIn(List<String> values) {
            addCriterion("state_name not in", values, "stateName");
            return (Criteria) this;
        }

        public Criteria andStateNameBetween(String value1, String value2) {
            addCriterion("state_name between", value1, value2, "stateName");
            return (Criteria) this;
        }

        public Criteria andStateNameNotBetween(String value1, String value2) {
            addCriterion("state_name not between", value1, value2, "stateName");
            return (Criteria) this;
        }

        public Criteria andEventTypeIsNull() {
            addCriterion("event_type is null");
            return (Criteria) this;
        }

        public Criteria andEventTypeIsNotNull() {
            addCriterion("event_type is not null");
            return (Criteria) this;
        }

        public Criteria andEventTypeEqualTo(String value) {
            addCriterion("event_type =", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeNotEqualTo(String value) {
            addCriterion("event_type <>", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeGreaterThan(String value) {
            addCriterion("event_type >", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeGreaterThanOrEqualTo(String value) {
            addCriterion("event_type >=", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeLessThan(String value) {
            addCriterion("event_type <", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeLessThanOrEqualTo(String value) {
            addCriterion("event_type <=", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeLike(String value) {
            addCriterion("event_type like", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeNotLike(String value) {
            addCriterion("event_type not like", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeIn(List<String> values) {
            addCriterion("event_type in", values, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeNotIn(List<String> values) {
            addCriterion("event_type not in", values, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeBetween(String value1, String value2) {
            addCriterion("event_type between", value1, value2, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeNotBetween(String value1, String value2) {
            addCriterion("event_type not between", value1, value2, "eventType");
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

        public Criteria andRepetitionIsNull() {
            addCriterion("repetition is null");
            return (Criteria) this;
        }

        public Criteria andRepetitionIsNotNull() {
            addCriterion("repetition is not null");
            return (Criteria) this;
        }

        public Criteria andRepetitionEqualTo(String value) {
            addCriterion("repetition =", value, "repetition");
            return (Criteria) this;
        }

        public Criteria andRepetitionNotEqualTo(String value) {
            addCriterion("repetition <>", value, "repetition");
            return (Criteria) this;
        }

        public Criteria andRepetitionGreaterThan(String value) {
            addCriterion("repetition >", value, "repetition");
            return (Criteria) this;
        }

        public Criteria andRepetitionGreaterThanOrEqualTo(String value) {
            addCriterion("repetition >=", value, "repetition");
            return (Criteria) this;
        }

        public Criteria andRepetitionLessThan(String value) {
            addCriterion("repetition <", value, "repetition");
            return (Criteria) this;
        }

        public Criteria andRepetitionLessThanOrEqualTo(String value) {
            addCriterion("repetition <=", value, "repetition");
            return (Criteria) this;
        }

        public Criteria andRepetitionLike(String value) {
            addCriterion("repetition like", value, "repetition");
            return (Criteria) this;
        }

        public Criteria andRepetitionNotLike(String value) {
            addCriterion("repetition not like", value, "repetition");
            return (Criteria) this;
        }

        public Criteria andRepetitionIn(List<String> values) {
            addCriterion("repetition in", values, "repetition");
            return (Criteria) this;
        }

        public Criteria andRepetitionNotIn(List<String> values) {
            addCriterion("repetition not in", values, "repetition");
            return (Criteria) this;
        }

        public Criteria andRepetitionBetween(String value1, String value2) {
            addCriterion("repetition between", value1, value2, "repetition");
            return (Criteria) this;
        }

        public Criteria andRepetitionNotBetween(String value1, String value2) {
            addCriterion("repetition not between", value1, value2, "repetition");
            return (Criteria) this;
        }

        public Criteria andIgnoreWeekendIsNull() {
            addCriterion("ignore_weekend is null");
            return (Criteria) this;
        }

        public Criteria andIgnoreWeekendIsNotNull() {
            addCriterion("ignore_weekend is not null");
            return (Criteria) this;
        }

        public Criteria andIgnoreWeekendEqualTo(Boolean value) {
            addCriterion("ignore_weekend =", value, "ignoreWeekend");
            return (Criteria) this;
        }

        public Criteria andIgnoreWeekendNotEqualTo(Boolean value) {
            addCriterion("ignore_weekend <>", value, "ignoreWeekend");
            return (Criteria) this;
        }

        public Criteria andIgnoreWeekendGreaterThan(Boolean value) {
            addCriterion("ignore_weekend >", value, "ignoreWeekend");
            return (Criteria) this;
        }

        public Criteria andIgnoreWeekendGreaterThanOrEqualTo(Boolean value) {
            addCriterion("ignore_weekend >=", value, "ignoreWeekend");
            return (Criteria) this;
        }

        public Criteria andIgnoreWeekendLessThan(Boolean value) {
            addCriterion("ignore_weekend <", value, "ignoreWeekend");
            return (Criteria) this;
        }

        public Criteria andIgnoreWeekendLessThanOrEqualTo(Boolean value) {
            addCriterion("ignore_weekend <=", value, "ignoreWeekend");
            return (Criteria) this;
        }

        public Criteria andIgnoreWeekendIn(List<Boolean> values) {
            addCriterion("ignore_weekend in", values, "ignoreWeekend");
            return (Criteria) this;
        }

        public Criteria andIgnoreWeekendNotIn(List<Boolean> values) {
            addCriterion("ignore_weekend not in", values, "ignoreWeekend");
            return (Criteria) this;
        }

        public Criteria andIgnoreWeekendBetween(Boolean value1, Boolean value2) {
            addCriterion("ignore_weekend between", value1, value2, "ignoreWeekend");
            return (Criteria) this;
        }

        public Criteria andIgnoreWeekendNotBetween(Boolean value1, Boolean value2) {
            addCriterion("ignore_weekend not between", value1, value2, "ignoreWeekend");
            return (Criteria) this;
        }

        public Criteria andDealStrategyIsNull() {
            addCriterion("deal_strategy is null");
            return (Criteria) this;
        }

        public Criteria andDealStrategyIsNotNull() {
            addCriterion("deal_strategy is not null");
            return (Criteria) this;
        }

        public Criteria andDealStrategyEqualTo(String value) {
            addCriterion("deal_strategy =", value, "dealStrategy");
            return (Criteria) this;
        }

        public Criteria andDealStrategyNotEqualTo(String value) {
            addCriterion("deal_strategy <>", value, "dealStrategy");
            return (Criteria) this;
        }

        public Criteria andDealStrategyGreaterThan(String value) {
            addCriterion("deal_strategy >", value, "dealStrategy");
            return (Criteria) this;
        }

        public Criteria andDealStrategyGreaterThanOrEqualTo(String value) {
            addCriterion("deal_strategy >=", value, "dealStrategy");
            return (Criteria) this;
        }

        public Criteria andDealStrategyLessThan(String value) {
            addCriterion("deal_strategy <", value, "dealStrategy");
            return (Criteria) this;
        }

        public Criteria andDealStrategyLessThanOrEqualTo(String value) {
            addCriterion("deal_strategy <=", value, "dealStrategy");
            return (Criteria) this;
        }

        public Criteria andDealStrategyLike(String value) {
            addCriterion("deal_strategy like", value, "dealStrategy");
            return (Criteria) this;
        }

        public Criteria andDealStrategyNotLike(String value) {
            addCriterion("deal_strategy not like", value, "dealStrategy");
            return (Criteria) this;
        }

        public Criteria andDealStrategyIn(List<String> values) {
            addCriterion("deal_strategy in", values, "dealStrategy");
            return (Criteria) this;
        }

        public Criteria andDealStrategyNotIn(List<String> values) {
            addCriterion("deal_strategy not in", values, "dealStrategy");
            return (Criteria) this;
        }

        public Criteria andDealStrategyBetween(String value1, String value2) {
            addCriterion("deal_strategy between", value1, value2, "dealStrategy");
            return (Criteria) this;
        }

        public Criteria andDealStrategyNotBetween(String value1, String value2) {
            addCriterion("deal_strategy not between", value1, value2, "dealStrategy");
            return (Criteria) this;
        }

        public Criteria andRepeatTimesIsNull() {
            addCriterion("repeat_times is null");
            return (Criteria) this;
        }

        public Criteria andRepeatTimesIsNotNull() {
            addCriterion("repeat_times is not null");
            return (Criteria) this;
        }

        public Criteria andRepeatTimesEqualTo(Long value) {
            addCriterion("repeat_times =", value, "repeatTimes");
            return (Criteria) this;
        }

        public Criteria andRepeatTimesNotEqualTo(Long value) {
            addCriterion("repeat_times <>", value, "repeatTimes");
            return (Criteria) this;
        }

        public Criteria andRepeatTimesGreaterThan(Long value) {
            addCriterion("repeat_times >", value, "repeatTimes");
            return (Criteria) this;
        }

        public Criteria andRepeatTimesGreaterThanOrEqualTo(Long value) {
            addCriterion("repeat_times >=", value, "repeatTimes");
            return (Criteria) this;
        }

        public Criteria andRepeatTimesLessThan(Long value) {
            addCriterion("repeat_times <", value, "repeatTimes");
            return (Criteria) this;
        }

        public Criteria andRepeatTimesLessThanOrEqualTo(Long value) {
            addCriterion("repeat_times <=", value, "repeatTimes");
            return (Criteria) this;
        }

        public Criteria andRepeatTimesIn(List<Long> values) {
            addCriterion("repeat_times in", values, "repeatTimes");
            return (Criteria) this;
        }

        public Criteria andRepeatTimesNotIn(List<Long> values) {
            addCriterion("repeat_times not in", values, "repeatTimes");
            return (Criteria) this;
        }

        public Criteria andRepeatTimesBetween(Long value1, Long value2) {
            addCriterion("repeat_times between", value1, value2, "repeatTimes");
            return (Criteria) this;
        }

        public Criteria andRepeatTimesNotBetween(Long value1, Long value2) {
            addCriterion("repeat_times not between", value1, value2, "repeatTimes");
            return (Criteria) this;
        }

        public Criteria andTaskIdIsNull() {
            addCriterion("task_id is null");
            return (Criteria) this;
        }

        public Criteria andTaskIdIsNotNull() {
            addCriterion("task_id is not null");
            return (Criteria) this;
        }

        public Criteria andTaskIdEqualTo(Long value) {
            addCriterion("task_id =", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdNotEqualTo(Long value) {
            addCriterion("task_id <>", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdGreaterThan(Long value) {
            addCriterion("task_id >", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdGreaterThanOrEqualTo(Long value) {
            addCriterion("task_id >=", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdLessThan(Long value) {
            addCriterion("task_id <", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdLessThanOrEqualTo(Long value) {
            addCriterion("task_id <=", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdIn(List<Long> values) {
            addCriterion("task_id in", values, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdNotIn(List<Long> values) {
            addCriterion("task_id not in", values, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdBetween(Long value1, Long value2) {
            addCriterion("task_id between", value1, value2, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdNotBetween(Long value1, Long value2) {
            addCriterion("task_id not between", value1, value2, "taskId");
            return (Criteria) this;
        }

        public Criteria andModNumIsNull() {
            addCriterion("mod_num is null");
            return (Criteria) this;
        }

        public Criteria andModNumIsNotNull() {
            addCriterion("mod_num is not null");
            return (Criteria) this;
        }

        public Criteria andModNumEqualTo(Long value) {
            addCriterion("mod_num =", value, "modNum");
            return (Criteria) this;
        }

        public Criteria andModNumNotEqualTo(Long value) {
            addCriterion("mod_num <>", value, "modNum");
            return (Criteria) this;
        }

        public Criteria andModNumGreaterThan(Long value) {
            addCriterion("mod_num >", value, "modNum");
            return (Criteria) this;
        }

        public Criteria andModNumGreaterThanOrEqualTo(Long value) {
            addCriterion("mod_num >=", value, "modNum");
            return (Criteria) this;
        }

        public Criteria andModNumLessThan(Long value) {
            addCriterion("mod_num <", value, "modNum");
            return (Criteria) this;
        }

        public Criteria andModNumLessThanOrEqualTo(Long value) {
            addCriterion("mod_num <=", value, "modNum");
            return (Criteria) this;
        }

        public Criteria andModNumIn(List<Long> values) {
            addCriterion("mod_num in", values, "modNum");
            return (Criteria) this;
        }

        public Criteria andModNumNotIn(List<Long> values) {
            addCriterion("mod_num not in", values, "modNum");
            return (Criteria) this;
        }

        public Criteria andModNumBetween(Long value1, Long value2) {
            addCriterion("mod_num between", value1, value2, "modNum");
            return (Criteria) this;
        }

        public Criteria andModNumNotBetween(Long value1, Long value2) {
            addCriterion("mod_num not between", value1, value2, "modNum");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNull() {
            addCriterion("end_time is null");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNotNull() {
            addCriterion("end_time is not null");
            return (Criteria) this;
        }

        public Criteria andEndTimeEqualTo(Date value) {
            addCriterion("end_time =", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotEqualTo(Date value) {
            addCriterion("end_time <>", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThan(Date value) {
            addCriterion("end_time >", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("end_time >=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThan(Date value) {
            addCriterion("end_time <", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThanOrEqualTo(Date value) {
            addCriterion("end_time <=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIn(List<Date> values) {
            addCriterion("end_time in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotIn(List<Date> values) {
            addCriterion("end_time not in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeBetween(Date value1, Date value2) {
            addCriterion("end_time between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotBetween(Date value1, Date value2) {
            addCriterion("end_time not between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andLastExceptionStackIsNull() {
            addCriterion("last_exception_stack is null");
            return (Criteria) this;
        }

        public Criteria andLastExceptionStackIsNotNull() {
            addCriterion("last_exception_stack is not null");
            return (Criteria) this;
        }

        public Criteria andLastExceptionStackEqualTo(String value) {
            addCriterion("last_exception_stack =", value, "lastExceptionStack");
            return (Criteria) this;
        }

        public Criteria andLastExceptionStackNotEqualTo(String value) {
            addCriterion("last_exception_stack <>", value, "lastExceptionStack");
            return (Criteria) this;
        }

        public Criteria andLastExceptionStackGreaterThan(String value) {
            addCriterion("last_exception_stack >", value, "lastExceptionStack");
            return (Criteria) this;
        }

        public Criteria andLastExceptionStackGreaterThanOrEqualTo(String value) {
            addCriterion("last_exception_stack >=", value, "lastExceptionStack");
            return (Criteria) this;
        }

        public Criteria andLastExceptionStackLessThan(String value) {
            addCriterion("last_exception_stack <", value, "lastExceptionStack");
            return (Criteria) this;
        }

        public Criteria andLastExceptionStackLessThanOrEqualTo(String value) {
            addCriterion("last_exception_stack <=", value, "lastExceptionStack");
            return (Criteria) this;
        }

        public Criteria andLastExceptionStackLike(String value) {
            addCriterion("last_exception_stack like", value, "lastExceptionStack");
            return (Criteria) this;
        }

        public Criteria andLastExceptionStackNotLike(String value) {
            addCriterion("last_exception_stack not like", value, "lastExceptionStack");
            return (Criteria) this;
        }

        public Criteria andLastExceptionStackIn(List<String> values) {
            addCriterion("last_exception_stack in", values, "lastExceptionStack");
            return (Criteria) this;
        }

        public Criteria andLastExceptionStackNotIn(List<String> values) {
            addCriterion("last_exception_stack not in", values, "lastExceptionStack");
            return (Criteria) this;
        }

        public Criteria andLastExceptionStackBetween(String value1, String value2) {
            addCriterion("last_exception_stack between", value1, value2, "lastExceptionStack");
            return (Criteria) this;
        }

        public Criteria andLastExceptionStackNotBetween(String value1, String value2) {
            addCriterion("last_exception_stack not between", value1, value2, "lastExceptionStack");
            return (Criteria) this;
        }

        public Criteria andOutGoingIsNull() {
            addCriterion("out_going is null");
            return (Criteria) this;
        }

        public Criteria andOutGoingIsNotNull() {
            addCriterion("out_going is not null");
            return (Criteria) this;
        }

        public Criteria andOutGoingEqualTo(String value) {
            addCriterion("out_going =", value, "outGoing");
            return (Criteria) this;
        }

        public Criteria andOutGoingNotEqualTo(String value) {
            addCriterion("out_going <>", value, "outGoing");
            return (Criteria) this;
        }

        public Criteria andOutGoingGreaterThan(String value) {
            addCriterion("out_going >", value, "outGoing");
            return (Criteria) this;
        }

        public Criteria andOutGoingGreaterThanOrEqualTo(String value) {
            addCriterion("out_going >=", value, "outGoing");
            return (Criteria) this;
        }

        public Criteria andOutGoingLessThan(String value) {
            addCriterion("out_going <", value, "outGoing");
            return (Criteria) this;
        }

        public Criteria andOutGoingLessThanOrEqualTo(String value) {
            addCriterion("out_going <=", value, "outGoing");
            return (Criteria) this;
        }

        public Criteria andOutGoingLike(String value) {
            addCriterion("out_going like", value, "outGoing");
            return (Criteria) this;
        }

        public Criteria andOutGoingNotLike(String value) {
            addCriterion("out_going not like", value, "outGoing");
            return (Criteria) this;
        }

        public Criteria andOutGoingIn(List<String> values) {
            addCriterion("out_going in", values, "outGoing");
            return (Criteria) this;
        }

        public Criteria andOutGoingNotIn(List<String> values) {
            addCriterion("out_going not in", values, "outGoing");
            return (Criteria) this;
        }

        public Criteria andOutGoingBetween(String value1, String value2) {
            addCriterion("out_going between", value1, value2, "outGoing");
            return (Criteria) this;
        }

        public Criteria andOutGoingNotBetween(String value1, String value2) {
            addCriterion("out_going not between", value1, value2, "outGoing");
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
package com.upgrad.quora.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name="answer",schema="quora")
public class AnswerEntity implements Serializable {
    @Column(name="ID")
    private Integer id;

    @Column(name = "UUID")
    @Size(max=200)
    @NotNull
    private String uuid;

    @Column(name="ANS")
    @Size(max=255)
    @NotNull
    private String ans;

    @ManyToOne
    @Column(name="USER_ID")
    @NotNull
    private UserEntity user;

    @ManyToOne
    @Column(name="QUESTION_ID")
    @NotNull
    private  QuestionEntity question;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public QuestionEntity getQuestion() {
        return question;
    }

    public void setQuestion(QuestionEntity question) {
        this.question = question;
    }
}


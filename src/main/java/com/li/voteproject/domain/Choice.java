package com.li.voteproject.domain;

import javax.persistence.*;

@Entity
@Table(name = "choice")
public class Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer choice_id;
    private String choicename;
    private Integer choicenumble;

    public Integer getChoice_id() {
        return choice_id;
    }

    public void setChoice_id(Integer choice_id) {
        this.choice_id = choice_id;
    }

    public String getChoicename() {
        return choicename;
    }

    public void setChoicename(String choicename) {
        this.choicename = choicename;
    }

    public Integer getChoicenumble() {
        return choicenumble;
    }

    public void setChoicenumble(Integer choicenumble) {
        this.choicenumble = choicenumble;
    }
}

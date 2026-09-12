package com.example.deliverytracker.notice.entity;

import com.example.deliverytracker.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private boolean important;

    @Column(nullable = false)
    private boolean visible;

    @Column(nullable = false)
    private boolean deleted;

    @Column(nullable = false)
    private int viewCount;

    public Notice(String title, String content, boolean important) {
        this.title = title;
        this.content = content;
        this.important = important;
        this.visible = true;
        this.viewCount = 0;
    }

    public void update(String title, String content, boolean important, boolean visible) {
        this.title = title;
        this.content = content;
        this.important = important;
        this.visible = visible;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void delete(){
        this.deleted = true;
    }

    public void restore(boolean visible){
        this.deleted = false;
        this.visible = visible;
    }
}

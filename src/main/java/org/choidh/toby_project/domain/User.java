package org.choidh.toby_project.domain;

import lombok.*;

@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    String id;
    String name;
    String password;
    Level level;
    int login;
    int recommend;

//    Date lastUpgraded;

    public int getLogin() {
        return login;
    }

    public int getRecommend() {
        return recommend;
    }

    public Level getLevel() {
        return level;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public String getPassword() {
        return password == null ? "" : password;
    }

    public User clone(){
        return new User(this.id, this.name, this.password, this.level, this.login, this.recommend);
    }

    public void upgradeLevel() {
        final Level nextLevel = this.level.nextLevel();

        if (nextLevel == null) {
            throw new IllegalStateException(this.level + "은 업그레이드가 불가능합니다.");
        }else {
            this.level = nextLevel;
//            this.lastUpgraded = new Date();
        }
    }

}

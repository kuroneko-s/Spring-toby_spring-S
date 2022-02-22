package org.choidh.toby_project.domain;

public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);

    void upgradeLevel(User user) throws RuntimeException;
}

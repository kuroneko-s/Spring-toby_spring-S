package org.choidh.toby_project.policy;

import org.choidh.toby_project.domain.user.User;

public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);

    void upgradeLevel(User user) throws RuntimeException;
}

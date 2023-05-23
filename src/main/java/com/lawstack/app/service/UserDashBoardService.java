package com.lawstack.app.service;


import com.lawstack.app.model.UserDashboard;

public interface UserDashBoardService {

    UserDashboard saveDashboard(UserDashboard dashboard);

    UserDashboard getuserInfo(String email);

    UserDashboard getInfoByUserId(String id);

    UserDashboard getUserDashboardByEmail(String email);

    UserDashboard updateDashboard(UserDashboard userDashboard);
}

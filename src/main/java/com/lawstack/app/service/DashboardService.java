package com.lawstack.app.service;

import com.lawstack.app.model.Dashboard;

public interface DashboardService {
    
    Dashboard saveDashboard(Dashboard dashboard);

    Dashboard getDashboard(int id);

    Dashboard updateDashboard(Dashboard dashboard);
}

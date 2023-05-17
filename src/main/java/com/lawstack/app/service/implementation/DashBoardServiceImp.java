package com.lawstack.app.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawstack.app.model.Dashboard;
import com.lawstack.app.repository.DashboardRespository;
import com.lawstack.app.service.DashboardService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DashBoardServiceImp implements DashboardService {

    @Autowired
    private DashboardRespository dashRepo;

    @Override
    public Dashboard saveDashboard(Dashboard dashboard) {
        log.info("Saving the dashboard info");
        return this.dashRepo.save(dashboard);
    }

    @Override
    public Dashboard getDashboard(int id) {
        log.info("Fetching the Dashbaord info by id: {}", id);
        return this.dashRepo.findById(id).get();
    }

    @Override
    public Dashboard updateDashboard(Dashboard dashboard) {

        Dashboard info = this.dashRepo.findById(1).get();

        if (info == null)
            return null;

        if (dashboard.getDewDropper() != 0) {
            info.setDewDropper(info.getDewDropper());
        }
        if (dashboard.getSprinkle() != 0) {
            info.setSprinkle(info.getSprinkle() + 1);
        }
        if (dashboard.getRainmaker() != 0) {
            info.setRainmaker(info.getRainmaker() + 1);
        }
        if (dashboard.getUsers() != 0) {
            info.setUsers(info.getUsers() + 1);
        }
        if (dashboard.getSellers() != 0) {
            info.setSellers(info.getSellers() + 1);
        }
        if (dashboard.getIncome() != 0.0) {
            info.setIncome(info.getIncome() + 1);
        }

        if (dashboard.getJobs() != 0) {
            info.setJobs(info.getJobs() + 1);
        }

        return this.saveDashboard(info);
    }

}

package com.lawstack.app.service;

import java.util.List;

import com.lawstack.app.model.WithDraw;

public interface WithDrawService {

    WithDraw saveWithDraw(WithDraw withDraw);

    WithDraw updateDraw(WithDraw withDraw);

    List<WithDraw> getAllWithDrawsByUserId(String userId);

    List<WithDraw> getAllWithDraws();

    int getPendingCount(boolean status);
}

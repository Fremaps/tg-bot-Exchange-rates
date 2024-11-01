package ru.sheshukov.exchangeratesbot.service;

import ru.sheshukov.exchangeratesbot.exception.ServiceException;

public interface ExchangeRatesService {


    String getUSDExchangeRate() throws ServiceException;

    String getEURExchangeRate() throws ServiceException;

    String getCNYExchangeRate() throws ServiceException;

    String getAUDExchangeRate() throws ServiceException;

    String getGBPExchangeRate() throws ServiceException;

    void clearUSDCache();

    void clearEURCache();

    void clearCNYCache();

    void clearAUDCache();

    void clearGBPCache();
}
